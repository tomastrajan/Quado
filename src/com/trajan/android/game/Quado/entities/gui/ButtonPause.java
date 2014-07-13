
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

package com.trajan.android.game.Quado.entities.gui;

import android.graphics.*;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.entities.BasicEntity;
import com.trajan.android.game.Quado.helpers.*;

public class ButtonPause extends BasicEntity implements TouchEventListener {

    private static final String TAG = ButtonPause.class.getSimpleName();

    public ButtonPause(Dimensions dimensions) {
        super(dimensions);
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        setzIndex(2);

        if (gameState.isStateGame() || gameState.isStateArcade()) {

            Paint paint = new Paint();
            paint.setColor(MyColors.getGuiElementColor());
            paint.setColorFilter(MyColors.getFilterGui());

            canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

            int left =  x - width / 2;
            int right = x + width / 2;
            int top = y - height / 2;
            int bottom = y + height / 2;

            paint.setStrokeWidth(2f);

            canvas.drawLine(left, bottom, right, top, paint);
            canvas.drawLine(left, top, right, bottom, paint);
        }

    }
    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        // Select button
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            float eventX = event.getX();
            float eventY = event.getY();

            if (eventX >= (x - width  * 1.5) && (eventX <= (x + width  * 1.5))) {
                if (eventY >= (y - height  * 1.5) && (eventY <= (y + height  * 1.5))) {

                    setTouched(true);
                } else {
                    setTouched(false);
                }
            } else {
                setTouched(false);
            }

            if (isTouched()) {

                GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

                if (gameState.isStateGame() || gameState.isStateArcade()) {
                    gameState.setStatePause();
                }
            }
        }
    }

}
