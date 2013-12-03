
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
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;

public class ButtonRetry extends BasicEntity implements MyTouchEventListener {

    private static final String TAG = ButtonRetry.class.getSimpleName();

    public ButtonRetry(Dimensions dimensions) {
        super(dimensions);
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        setzIndex(2);

        if (gameState.isStateGame() || gameState.isStateArcade()) {

            paint.setColor(MyColors.getGuiElementColor());
            paint.setColorFilter(MyColors.getFilterGui());

            canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);

            int top = y - height / 2;
            int left = x - width / 2;
            int right = x + width / 2;
            int bottom = y + height / 2;
            int centerHorizontal =  right - (Math.abs(right - left) / 2);
            int centerVertical =  bottom - (Math.abs(bottom - top) / 2);
            int stub =  left + (Math.abs(right - left) / 4);

            if (gameState.isStateVictory()) {
                paint.setStrokeWidth(5f);

                canvas.drawLine(left, centerVertical, right, centerVertical, paint);
                canvas.drawLine(centerHorizontal, top, centerHorizontal, bottom, paint);
            } else {
                paint.setStrokeWidth(2f);

                canvas.drawLine(left, bottom, left, top, paint);
                canvas.drawLine(left, top, right, top, paint);
                canvas.drawLine(right, top, right, bottom, paint);
                canvas.drawLine(right, bottom, centerHorizontal, bottom, paint);
                canvas.drawLine(left, bottom, stub, bottom, paint);
                canvas.drawLine(centerHorizontal, bottom, centerHorizontal + height / 5, bottom + height / 5, paint);
                canvas.drawLine(centerHorizontal, bottom, centerHorizontal + height / 5, bottom - height / 5, paint);
            }
        }
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        // Select button
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            float eventX = event.getX();
            float eventY = event.getY();

            if (eventX >= (x - width * 1.5) && (eventX <= (x + width * 1.5))) {
                if (eventY >= (y - height * 1.5) && (eventY <= (y + height * 1.5))) {

                    GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);
                    if (gameState.isStateGame()) {
                        game.restart(false);
                    }
                }
            }

        }
    }


}
