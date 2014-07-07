
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

package com.trajan.android.game.Quado.entities;

import android.graphics.*;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.Speed;
import com.trajan.android.game.Quado.helpers.MyUpdateEventListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;

import java.util.Timer;
import java.util.TimerTask;

public class Ball extends BasicEntity implements MyUpdateEventListener {

    private static final String TAG = Ball.class.getSimpleName();
    private Speed speed;

    private float moveX;
    private float moveY;

    private int corrX;
    private int corrY;

    private boolean isArcadeTimer = false;

    private Path ballPolygon;

    public Ball(Dimensions dimensions, Speed speed) {
        super(dimensions);
        this.speed = speed;

        moveX = x;
        moveY = y;
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        paint.setColor(MyColors.getBallColor());
        canvas.clipRect(0,0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);
        canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, paint);

//        Paint helperPiant = new Paint();
//        helperPiant.setColor(Color.parseColor("#ff0000"));
//        if (ballPolygon != null) {
//            paint.setColor(MyColors.getBlockColorByConstant(1));
//            canvas.drawPath(ballPolygon, helperPiant);
//        }


    }

    public Speed getSpeed() {
        return speed;
    }

    public void move() {

        speed.enforceSpeedLimits();

        moveX += speed.getXv() * speed.getSpeedMultiplicator() * speed.getxDirection();
        moveY += speed.getYv() * speed.getSpeedMultiplicator() * speed.getyDirection();

        x = (int) moveX;
        y = (int) moveY;


        corrX =  x + (int) Math.ceil(speed.getXv() * speed.getSpeedMultiplicator()) * (speed.getxDirection());
        corrY =  y + (int) Math.ceil(speed.getYv() * speed.getSpeedMultiplicator()) * (speed.getyDirection());

    }

    @Override
    public void handleUpdateEvent(MainGamePanel game) {

        if (game.getThread().isRunning()) {

            GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

            if (gameState.isStateGame() || gameState.isStateMenu() || gameState.isStateArcade()) {

                // Update speed for menu and normal
                if (gameState.isStateGame() || gameState.isStateMenu()) {
                    float ratio = game.getElements().getLevel().getBlockCounter().getRemainingToTotalBlockCountRatio();
                    speed.updateSpeedMultiplicator(ratio);
                }

                // Update speed for arcade
                if (gameState.isStateArcade() && !isArcadeTimer) {

                    isArcadeTimer = true;

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            speed.updateSpeedMultiplicatorArcade();
                            isArcadeTimer = false;
                        }
                    }, 500);
                }

                // Move ball
                move();

            }
        }
    }

    public int getCorrX() {
        return corrX;
    }

    public int getCorrY() {
        return corrY;
    }

    public void setBallPolygon(Path ballPolygon) {
        this.ballPolygon = ballPolygon;
    }
}