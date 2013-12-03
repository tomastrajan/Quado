
/*
 * Quado
 * Copyright (C) 2013  Ing. Tomas Herich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/gpl.html
 */

package com.trajan.android.game.Quado.components;

import android.graphics.Paint;
import android.graphics.Rect;
import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.entities.*;
import com.trajan.android.game.Quado.entities.effects.ScoreDisplayHitEffect;
import com.trajan.android.game.Quado.helpers.MyUpdateEventListener;
import com.trajan.android.game.Quado.helpers.Dimensions;

import java.util.*;

public class CollisionDetector implements MyUpdateEventListener, Component {

    private static final String TAG = CollisionDetector.class.getSimpleName();

    private static final int NO_HIT = 0;
    private static final int TOP = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 3;
    private static final int LEFT = 4;
    private static final int TOP_LEFT = 5;
    private static final int TOP_RIGHT = 6;
    private static final int BOTTOM_RIGHT = 7;
    private static final int BOTTOM_LEFT = 8;

    private int surfaceWidth;
    private int surfaceHeight;

    private int barRecentCollisionHelper = 0;

    private boolean defeatEvent = false;

    public CollisionDetector() {
        this.surfaceWidth = DeviceInfo.INSTANCE.getSurfaceWidth();
        this.surfaceHeight = DeviceInfo.INSTANCE.getSurfaceHeight();
    }

    public void checkBarCollision(MainGamePanel game) {

        Ball ball = (Ball) game.getElements().getEntity(Elements.BALL);
        Bar bar = (Bar) game.getElements().getEntity(Elements.BAR);

        Speed speed = ball.getSpeed();

        if (barRecentCollisionHelper <= 0) {

            int corrX = (int) Math.ceil(speed.getXv() * speed.getSpeedMultiplicator());
            int corrY = (int) Math.ceil(speed.getYv() * speed.getSpeedMultiplicator());

            Rect barRect = createEntityRectWithCorrections(bar, corrX, corrY);
            Rect ballRect = createEntityRectWithCorrections(ball, corrX, corrY);

            if (ballRect.intersect(barRect)) {

                Sounds sounds = (Sounds) game.getElements().getComponent(Elements.SOUNDS);

                if (bar.getY() - (ball.getY() - corrY) + corrY < bar.getHeight() / 2 + ball.getHeight() / 2 + corrY) {

                    speed.toggleXDirection();

                } else {

                    Score score = (Score) game.getElements().getComponent(Elements.SCORE);
                    ScoreDisplay scoreDisplay = (ScoreDisplay) game.getElements().getEntity(Elements.SCORE_DISPLAY);
                    RenderHelper renderHelper = (RenderHelper) game.getElements().getComponent(Elements.RENDER_HELPER);

                    int diffX = bar.getX() - ball.getX();
                    int maxDiff = bar.getWidth() / 2;

                    float diffRatio = ((Math.abs(diffX) / (float) maxDiff)) * 2f;
                    speed.setXv(diffRatio);
                    speed.setYv(2 - diffRatio);

                    speed.toggleYDirection();

                    score.padHit();

                    // Add effect to score display
                    Dimensions d = scoreDisplay.getSquareDimensions();
                    Paint paint = scoreDisplay.getPaint();
                    Entity hitEffect = new ScoreDisplayHitEffect(d.getX(), d.getY(), d.getWidth(), d.getHeight(), paint);
                    if (hitEffect != null) {
                        renderHelper.addHitEffect(hitEffect);
                    }

                }

                barRecentCollisionHelper = 10;
                sounds.playBarHit();

            }
        } else {
            barRecentCollisionHelper--;
        }

    }

    public void checkBallBlocksCollision(MainGamePanel game) {

        Map<Integer, Block> detectedCollisions = new HashMap<Integer, Block>();
        Ball ball = (Ball) game.getElements().getEntity(Elements.BALL);

        for (Block block : game.getElements().getLevel().getBlocks()) {

            if (block.getHitPoints() > 0) {

                // Calculate rough distance to discard most blocks from further calculations
                boolean distanceX = Math.abs(block.getX() - ball.getCorrX()) < block.getWidth() * 1.5;
                boolean distanceY = Math.abs(block.getY() - ball.getCorrY()) < block.getHeight() * 1.5;

                // Rough distance satisfied
                if (distanceX && distanceY) {

                    int hitDirection = checkHitDirection(ball, block);

                    if (hitDirection != NO_HIT) {

                        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

                        detectedCollisions.put(hitDirection, block);
                        Entity hitEffect = block.decrementHitPoints(gameState, game.getElements().getLevel().getBlockCounter());

                        if (hitEffect != null) {

                            Sounds sounds = (Sounds) game.getElements().getComponent(Elements.SOUNDS);
                            Score score = (Score) game.getElements().getComponent(Elements.SCORE);
                            RenderHelper renderHelper = (RenderHelper) game.getElements().getComponent(Elements.RENDER_HELPER);

                            renderHelper.addHitEffect(hitEffect);

                            if (block.getHitPoints() == 0) {
                                game.getElements().getLevel().getBlockCounter().decrementRemainingBlockCount();
                                score.brickHit();
                            } else {
                                score.brickHit();
                            }

                            if (block.getHitPoints() > 0) {
                                sounds.playHit();
                            } else {
                                sounds.playLastHit();
                            }
                        }
                    }

                }

            }
        }

        // Don't change ball direction if no collision occurred
        if (detectedCollisions.size() == 0) {
            return;
        } else {
            boolean[] directions = calculatePossibleBallDirections(detectedCollisions);
            setBallDirectionBasedOnPossibleDirections(ball, directions);
        }
    }

    private boolean[] calculatePossibleBallDirections(Map<Integer, Block> collisionDetected) {

        // Because top =1, right = 4
        boolean[] directionArray = new boolean[5];
        directionArray[TOP] = true;
        directionArray[LEFT] = true;
        directionArray[BOTTOM] = true;
        directionArray[RIGHT] = true;

        Set<Integer> hitTypes = collisionDetected.keySet();

        for (Integer hitType : hitTypes) {

            if (hitType == TOP) {
                directionArray[BOTTOM] = false;
            }

            if (hitType == RIGHT) {
                directionArray[LEFT] = false;
            }

            if (hitType == BOTTOM) {
                directionArray[TOP] = false;
            }

            if (hitType == LEFT) {
                directionArray[RIGHT] = false;
            }

            if (hitType == TOP_LEFT) {
                directionArray[BOTTOM] = false;
                directionArray[RIGHT] = false;
            }

            if (hitType == TOP_RIGHT) {
                directionArray[BOTTOM] = false;
                directionArray[LEFT] = false;
            }

            if (hitType == BOTTOM_LEFT) {
                directionArray[TOP] = false;
                directionArray[RIGHT] = false;
            }

            if (hitType == BOTTOM_RIGHT) {
                directionArray[TOP] = false;
                directionArray[LEFT] = false;
            }

        }

        return directionArray;
    }

    private void setBallDirectionBasedOnPossibleDirections(Ball ball, boolean[] directions) {

        Speed speed = ball.getSpeed();

        if (directions[TOP] && !directions[BOTTOM]) {
            speed.setyDirection(Speed.DIRECTION_UP);
        }

        if (directions[BOTTOM] && !directions[TOP]) {
            speed.setyDirection(Speed.DIRECTION_DOWN);
        }

        if (directions[RIGHT] && !directions[LEFT]) {
            speed.setxDirection(Speed.DIRECTION_RIGHT);
        }

        if (directions[LEFT] && !directions[RIGHT]) {
            speed.setxDirection(Speed.DIRECTION_LEFT);
        }

    }


    public void checkGameSpaceCollisions(MainGamePanel game) {

        Ball ball = (Ball) game.getElements().getEntity(Elements.BALL);
        Speed speed = ball.getSpeed();

        // check collision with right wall if heading right
        if (speed.getxDirection() == Speed.DIRECTION_RIGHT && ball.getX() + ball.getWidth() / 2 >= surfaceWidth) {
            speed.toggleXDirection();

        }
        // check collision with left wall if heading left
        if (speed.getxDirection() == Speed.DIRECTION_LEFT && ball.getX() - ball.getWidth() / 2 <= 0) {
            speed.toggleXDirection();

        }
        // check collision with bottom wall if heading down
        if (speed.getyDirection() == Speed.DIRECTION_DOWN && ball.getY() + ball.getHeight() / 2 >= surfaceHeight) {
            speed.toggleYDirection();

            // Game over event
            defeatEvent = true;
        }
        // check collision with top wall if heading up
        if (speed.getyDirection() == Speed.DIRECTION_UP && ball.getY() - ball.getHeight() / 2 <= 0) {
            speed.toggleYDirection();
        }
    }

    private Rect createEntityRectWithCorrections(BasicEntity entity, int corrX, int corrY) {

        int left =  (entity.getX() - entity.getWidth() / 2) + corrX;
        int right = (entity.getX() + entity.getWidth() / 2) + corrX;
        int top = (entity.getY() - entity.getHeight() / 2) + corrY;
        int bottom = (entity.getY() + entity.getHeight() / 2) + corrY;

        return new Rect(left, top, right, bottom);
    }

    private int checkHitDirection(Ball ball, Block block) {

        Speed speed = ball.getSpeed();

        int corrX = (int) Math.ceil(speed.getXv() * speed.getSpeedMultiplicator()) * (speed.getxDirection());
        int corrY = (int) Math.ceil(speed.getYv() * speed.getSpeedMultiplicator()) * (speed.getyDirection());

        Rect blockRect = createEntityRect(block);
        Rect ballRect = createEntityRectWithCorrections(ball, corrX, corrY);

        if (ballRect.intersect(blockRect)) {

            int diffXd = block.getX() - ballRect.centerX();
            int diffYd = block.getY() - ballRect.centerY();

            int diffX = Math.abs(block.getX() - ballRect.centerX()) - (ballRect.width() / 2) - (block.getWidth() / 2);
            int diffY = Math.abs(block.getY() - ballRect.centerY()) - (ballRect.height() / 2) - (block.getHeight() / 2);

            int absDiffX = Math.abs(diffX);
            int absDiffY = Math.abs(diffY);
            boolean cornerHit = Math.abs(absDiffX - absDiffY) < Math.max(absDiffX, absDiffY) * 0.05f;

            // TopLeft hit
            if (diffYd > 0 && diffY <= 0 && diffXd > 0 && diffX <= 0 && cornerHit) {
                return TOP_LEFT;
            }

            // TopRight hit
            if (diffYd > 0 && diffY <= 0 && diffXd < 0 && diffX <= 0 && cornerHit) {
                return TOP_RIGHT;
            }

            // BottomLeft hit
            if (diffYd < 0 && diffY <= 0 && diffXd > 0 && diffX <= 0 && cornerHit) {
                return BOTTOM_LEFT;
            }

            // BottomRight hit
            if (diffYd < 0 && diffY <= 0 && diffXd < 0 && diffX <= 0 && cornerHit) {
                return BOTTOM_RIGHT;
            }

            // Top hit
            if (diffYd > 0 && diffY <= 0 && diffY > diffX && speed.getyDirection() == Speed.DIRECTION_DOWN) {
                return TOP;
            }

            // Bottom hit
            if (diffYd < 0 && diffY <= 0 && diffY > diffX && speed.getyDirection() == Speed.DIRECTION_UP) {
                return BOTTOM;
            }

            // Left hit
            if (diffXd > 0 && diffX <= 0 && diffY < diffX && speed.getxDirection() == Speed.DIRECTION_RIGHT) {
                return LEFT;

            }

            // Right hit
            if (diffXd < 0 && diffX <= 0 && diffY < diffX && speed.getxDirection() == Speed.DIRECTION_LEFT) {
                return RIGHT;
            }

            return NO_HIT;

        }
        return NO_HIT;
    }

    private Rect createEntityRect(BasicEntity entity) {

        int left =  entity.getX() - entity.getWidth() / 2;
        int top = entity.getY() - entity.getHeight() / 2;
        int right = entity.getX() + entity.getWidth() / 2;
        int bottom = entity.getY() + entity.getHeight() / 2;

        return new Rect(left, top, right, bottom);
    }



    @Override
    public void handleUpdateEvent(MainGamePanel game) {


        if (game.getThread().isRunning()) {

            GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

            if (gameState.isStateMenu() || gameState .isStateGame() || gameState.isStateArcade()) {

                checkBallBlocksCollision(game);
                checkBarCollision(game);
                checkGameSpaceCollisions(game);


                if (defeatEvent) {

                    if (gameState.isStateArcade()) {
                        gameState.setArcadeTimeEnd(System.currentTimeMillis());
                        ExtStorage storage = (ExtStorage) game.getElements().getComponent(Elements.EXTERNAL_STORAGE_PROVIDER);

                        int scoreValue  = ((Score) game.getElements().getComponent(Elements.SCORE)).getScore();
                        int seconds = (int) (gameState.getArcadeTimeEnd() - gameState.getArcadeTimeStart()) / 1000;

                        storage.saveHighScore(scoreValue, seconds, ExtStorage.HIGH_SCORE_ARCADE_FILE);
                    }

                    gameState.setStateDefeat();
                }
            }

        }

    }
}
