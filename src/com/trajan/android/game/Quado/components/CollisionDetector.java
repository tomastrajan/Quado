
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

import android.graphics.*;
import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.entities.*;
import com.trajan.android.game.Quado.entities.effects.ScoreDisplayHitEffect;
import com.trajan.android.game.Quado.entities.screen.ScreenArcadeEnd;
import com.trajan.android.game.Quado.entities.screen.ScreenNormalEnd;
import com.trajan.android.game.Quado.helpers.MyUpdateEventListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.model.GameMode;
import com.trajan.android.game.Quado.model.Player;
import com.trajan.android.game.Quado.rest.DefaultRestService;
import com.trajan.android.game.Quado.rest.dto.ScoreDto;

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

    private double effectiveCollisionDistanceX;
    private double effectiveCollisionDistanceY;

    private Region clip;

    public CollisionDetector() {
        this.surfaceWidth = DeviceInfo.INSTANCE.getSurfaceWidth();
        this.surfaceHeight = DeviceInfo.INSTANCE.getSurfaceHeight();
        clip = new Region(0, 0, surfaceWidth, surfaceHeight);
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

                    if (diffX < 0) {
                        speed.setxDirection(Speed.DIRECTION_RIGHT);
                    } else {
                        speed.setxDirection(Speed.DIRECTION_LEFT);
                    }

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

    private Point[] getRectPoints(Rect rect) {
        Point[] points = new Point[9];
        points[TOP_LEFT] = new Point(rect.left, rect.top);
        points[TOP_RIGHT] = new Point(rect.right, rect.top);
        points[BOTTOM_RIGHT] = new Point(rect.right, rect.bottom);
        points[BOTTOM_LEFT] = new Point(rect.left, rect.bottom);
        return points;
    }

    private void removeContainedPoint(Point[] dest, Rect destRect, Rect sourceRect) {
        int x = sourceRect.centerX() - destRect.centerX();
        int y = sourceRect.centerY() - destRect.centerY();

        if (x < 0 && y < 0) {
            dest[5] = null;
        }
        if (x > 0 && y < 0) {
            dest[6] = null;
        }
        if (x > 0 && y > 0) {
            dest[7] = null;
        }
        if (x < 0 && y > 0) {
            dest[8] = null;
        }
    }

    private Path createBallCorrPolygon(Point[] ballPoints, Point[] ballCorrPoints) {
        Path path = new Path();
        int containedPoint = 0;
        for (int i = 5; i <= 8; i++) {
            if(ballPoints[i] == null) {
                containedPoint = i;
                break;
            }
        }
        boolean first = true;
        int[] sequence = {};
        if (containedPoint == 5) {
            sequence = new int[] {50,60,6,7,8,80,50};
        } else if (containedPoint == 6) {
            sequence = new int[] {5,50,60,70,7,8,5};
        } else if (containedPoint == 7) {
            sequence = new int[] {5,6,60,70,80,8,5};
        } else if (containedPoint == 8) {
            sequence = new int[] {5,6,7,70,80,50,5};
        }
         for (int i = 0; i < sequence.length; i++) {
            Point point;
            int value = sequence[i];
            if (value % 10 == 0) {
                point = ballCorrPoints[value / 10];
            } else {
                point = ballPoints[value];
            }
            if (point != null) {
                if (first) {
                    first = false;
                    path.moveTo(point.x, point.y);
                } else {
                    path.lineTo(point.x, point.y);
                }
            } else {
                throw new RuntimeException("ITS FUCKED");
            }
        }
        return path;
    }

    public void checkBallBlocksCollision(MainGamePanel game) {

        Map<Integer, Block> detectedCollisions = new HashMap<Integer, Block>();
        Ball ball = (Ball) game.getElements().getEntity(Elements.BALL);
        Speed speed = ball.getSpeed();

        effectiveCollisionDistanceX = game.getElements().getLevel().getBlocks().get(0).getWidth() * 2.5;
        effectiveCollisionDistanceY = game.getElements().getLevel().getBlocks().get(0).getHeight() * 2.5;

        int corrX = (int) Math.ceil(speed.getXv() * speed.getSpeedMultiplicator()) * (speed.getxDirection());
        int corrY = (int) Math.ceil(speed.getYv() * speed.getSpeedMultiplicator()) * (speed.getyDirection());

        Rect ballRect = createEntityRect(ball);
        Rect ballRectCorr = createEntityRectWithCorrections(ball, corrX, corrY);

        Point[] ballPoints = getRectPoints(ballRect);
        Point[] ballPointsCorr = getRectPoints(ballRectCorr);

        removeContainedPoint(ballPoints, ballRect, ballRectCorr);
        removeContainedPoint(ballPointsCorr, ballRectCorr, ballRect);

        Path ballPolygonPath = createBallCorrPolygon(ballPoints, ballPointsCorr);

        Region ballPolygon = new Region();
        ballPolygon.setPath(ballPolygonPath, clip);
        ball.setBallPolygon(ballPolygonPath);

        for (Block block : game.getElements().getLevel().getBlocks()) {

            if (block.getHitPoints() > 0) {

                // Calculate rough distance to discard most blocks from further calculations
                boolean distanceX = Math.abs(block.getX() - ball.getCorrX()) < effectiveCollisionDistanceX;
                boolean distanceY = Math.abs(block.getY() - ball.getCorrY()) < effectiveCollisionDistanceY;

                // Rough distance satisfied
                if (distanceX && distanceY) {

                    int hitDirection = checkHitDirection(ballPolygon, ballRect, ballRectCorr, speed, block);

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

            switch (hitType) {
                case TOP: {
                    directionArray[BOTTOM] = false;
                    break;
                }
                case RIGHT: {
                    directionArray[LEFT] = false;
                    break;
                }
                case BOTTOM: {
                    directionArray[TOP] = false;
                    break;
                }
                case LEFT: {
                    directionArray[RIGHT] = false;
                    break;
                }
                case TOP_LEFT: {
                    directionArray[BOTTOM] = false;
                    directionArray[RIGHT] = false;
                    break;
                }
                case TOP_RIGHT: {
                    directionArray[BOTTOM] = false;
                    directionArray[LEFT] = false;
                    break;
                }
                case BOTTOM_LEFT: {
                    directionArray[TOP] = false;
                    directionArray[RIGHT] = false;
                    break;
                }
                case BOTTOM_RIGHT: {
                    directionArray[TOP] = false;
                    directionArray[LEFT] = false;
                    break;
                }
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

    private int checkHitDirection(Region ballPolygonRegion, Rect ballRect, Rect ballRectCorr, Speed speed, Block block) {

        Rect blockRect = createEntityRect(block);

        Region blockRegion = new Region();
        blockRegion.set(blockRect);

        if (blockRegion.op(ballPolygonRegion, Region.Op.INTERSECT)) {

            int diffXd = block.getX() - ballRect.centerX();
            int diffYd = block.getY() - ballRect.centerY();

            // Top hit
            if (diffYd > 0 && Math.abs(diffXd) < Math.abs(diffYd)) {
                return TOP;
            }

            // Bottom hit
            if (diffYd < 0 && Math.abs(diffXd) < Math.abs(diffYd)) {
                return BOTTOM;
            }

            // Left hit
            if (diffXd > 0 && Math.abs(diffXd) > Math.abs(diffYd)) {
                return LEFT;

            }

            // Right hit
            if (diffXd < 0 && Math.abs(diffXd) > Math.abs(diffYd)) {
                return RIGHT;
            }

            if (Math.abs(diffXd) != Math.abs(diffYd)) {
                throw new RuntimeException("NOT SO CORNER HIT ARE YOU ?");
            }

            // TopLeft hit
            if (diffXd > 0 && diffYd > 0) {
                return TOP_LEFT;
            }

            // TopRight hit
            if (diffXd < 0 && diffYd > 0) {
                return TOP_RIGHT;
            }

            // BottomLeft hit
            if (diffXd > 0 && diffYd < 0) {
                return BOTTOM_LEFT;
            }

            // BottomRight hit
            if (diffXd < 0 && diffYd < 0) {
                return BOTTOM_RIGHT;
            }

            throw new RuntimeException("NO HIT IS FUCKED UP!");
            //return NO_HIT;

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

            if (gameState.isStateMenu() || gameState .isStateNormal() || gameState.isStateArcade()) {

                checkBallBlocksCollision(game);
                checkBarCollision(game);
                checkGameSpaceCollisions(game);


                if (defeatEvent) {

                    if (gameState.isStateArcade()) {
                        gameState.setArcadeTimeEnd(System.currentTimeMillis());

                        int scoreValue  = ((Score) game.getElements().getComponent(Elements.SCORE)).getScore();
                        int seconds = (int) (gameState.getArcadeTimeEnd() - gameState.getArcadeTimeStart()) / 1000;

                        // Save new high score
                        LocalPersistenceService lps = game.getLocalPersistenceService();
                        lps.saveHighScore(scoreValue, seconds, LocalPersistenceService.HIGH_SCORE_ARCADE_FILE);

                        DefaultRestService rest = game.getRest();
                        Player player = lps.getSelectedPlayer();
                        rest.createOrUpdateScore(new ScoreDto(player.getUUID(), player.getName(),
                                GameMode.ARCADE.name().toLowerCase(), scoreValue, seconds, null));


                        ((ScreenArcadeEnd) game.getElements().getEntity(Elements.SCREEN_ARCADE_END)).resetScore();
                        gameState.setStateArcadeDefeat();
                    } else {
                        ((ScreenNormalEnd) game.getElements().getEntity(Elements.SCREEN_NORMAL_END)).resetScore();
                        gameState.setStateNormalDefeat();
                    }
                }
            }

        }

    }
}
