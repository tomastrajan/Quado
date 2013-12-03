
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
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.helpers.MyTouchEventListener;
import com.trajan.android.game.Quado.helpers.MyUpdateEventListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;

public class Bar extends BasicEntity implements MyTouchEventListener, MyUpdateEventListener {

    private static final String TAG = Bar.class.getSimpleName();

    private int sineHelper;

    public Bar(Dimensions dimensions) {
        super(dimensions);
        sineHelper = (int) (Math.random() * Math.PI * 100);
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        setzIndex(0);

        if (gameState.isStateGame() || gameState.isStateMenu() || gameState.isStateArcade()) {

            paint.setColor(MyColors.getBarColor());

            canvas.clipRect(new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2), Region.Op.REPLACE);

            int top = y - height / 2;
            int bottom = y + height / 2;
            int left = x - width / 2;
            int right = x + width / 2;

            canvas.drawRect(left, top, right, bottom, paint);


            canvas.clipRect(0, 0 , canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);

            paint.setAlpha(MyColors.getAlphaAlmostTransparent());
            canvas.drawCircle(x, (float) (y + (height / 2) + 1.5 * height), height, paint);
        }

    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {


        // Select bar
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            float eventX = event.getX();
            float eventY = event.getY();

            if (eventX >= (x - height * 2) && (eventX <= (x + height * 2))) {
                if (eventY >= (y + height / 2) && (eventY <= (y + height / 2 + 4 * height))) {

                    setTouched(true);
                } else {
                    setTouched(false);
                }
            } else {
                setTouched(false);
            }

        }

        // Move bar
        if (isTouched()) {

            GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

            if (gameState.isStateGame() || gameState.isStateArcade()) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    setX((int) event.getX());
                }
            }
        }

        // Unselect bar
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setTouched(false);
        }
    }

    @Override
    public void handleUpdateEvent(MainGamePanel game) {

        if (game.getThread().isRunning()) {

            GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);
            Ball ball = (Ball) game.getElements().getEntity(Elements.BALL) ;

            if (gameState.isStateMenu()) {

                x = (int) (ball.getX() + ((width / 2) * Math.sin((double) sineHelper / 100)));
                sineHelper++;

                if (sineHelper >= Math.PI * 100) {
                    sineHelper = 0;
                }

            }
        }
    }


}
