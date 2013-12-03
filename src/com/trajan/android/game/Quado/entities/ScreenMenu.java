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

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.Sounds;
import com.trajan.android.game.Quado.helpers.MyTouchEventListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;
import com.trajan.android.game.Quado.levels.Level;

public class ScreenMenu extends BasicEntity implements MyTouchEventListener {

    private static final String TAG = ScreenVictory.class.getSimpleName();

    private String message;
    private int buttonLeft;
    private int buttonRight;
    private int buttonLeftHalf;
    private int buttonRightHalf;
    private int buttonStartTop;
    private int buttonStartBottom;
    private int buttonQuitTop;
    private int buttonQuitBottom;

    private int startHelper;
    private float textSize = 0;

    public ScreenMenu(Dimensions dimensions) {
        super(dimensions);
    }

    public ScreenMenu(Dimensions dimensions, Context context, int messageId) {
        super(dimensions);
        this.message = context.getResources().getString(messageId);
        startHelper = 20;
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        setzIndex(5);

        if (gameState.isStateMenu()) {

            // Do once
            if (textSize == 0) {
                paint.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas));
            }

            canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);
            int border = canvas.getWidth() / 15;

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2f);
            paint.setColor(MyColors.getGuiElementColor());
            paint.setColorFilter(MyColors.getFilterGui());

            paint.setAlpha((int) (255 * ((20 - startHelper) * 0.05f)));
            if (startHelper > 0) {
                startHelper--;
            }

            canvas.drawLine(border, border, canvas.getWidth() - border, border, paint);
            canvas.drawLine(border, border, border, canvas.getHeight() - border, paint);
            canvas.drawLine(border, canvas.getHeight() - border, canvas.getWidth() - border, canvas.getHeight() - border, paint);
            canvas.drawLine(canvas.getWidth() - border, border, canvas.getWidth() - border, canvas.getHeight() - border, paint);


            Level level = game.getElements().getLevel();
            int nameHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
            canvas.drawText(message, canvas.getWidth() / 2, level.getLevelCenterY() + nameHeight * 0.45f, paint);

            paint.setTextSize(paint.getTextSize() * 0.3f);
            paint.setAlpha(64);
            canvas.drawText("Trajan 2013", canvas.getWidth() / 2, level.getLevelCenterY() + nameHeight, paint);
            paint.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas));
            paint.setAlpha(255);

            buttonLeft = 2 * border;
            buttonRight = canvas.getWidth() - 2 * border;
            buttonLeftHalf = (canvas.getWidth() / 2) - (border / 2);
            buttonRightHalf = (canvas.getWidth() / 2) + (border / 2);
            buttonStartTop = canvas.getHeight() - 7 * border;
            buttonStartBottom = canvas.getHeight() - 5 * border;
            buttonQuitTop = canvas.getHeight() - 4 * border;
            buttonQuitBottom = canvas.getHeight() - 2 * border;

            // Draw buttons
            canvas.drawRect(buttonLeft, buttonStartTop, buttonLeftHalf, buttonStartBottom, paint);
            canvas.drawRect(buttonRightHalf, buttonStartTop, buttonRight, buttonStartBottom, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(buttonLeft, buttonQuitTop, buttonRight, buttonQuitBottom, paint);

            // Draw button texts
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(MyColors.getGuiElementTextColor());
            paint.setTextSize((int) (paint.getTextSize() * 0.7f));
            int textHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());

            canvas.drawText("NORMAL", (2 * border) + ((buttonLeftHalf - buttonLeft) / 2), getTextY(buttonStartTop, buttonStartBottom, textHeight), paint);
            canvas.drawText("ARCADE", ((canvas.getWidth() / 2) + (border / 2)) + ((buttonRight - buttonRightHalf) / 2), getTextY(buttonStartTop, buttonStartBottom, textHeight), paint);

            paint.setColor(MyColors.getGuiElementColor());
            canvas.drawText("QUIT", canvas.getWidth() / 2, getTextY(buttonQuitTop, buttonQuitBottom, textHeight), paint);

        }
    }

    private float getTextY(int buttonTop, int buttonBottom, int textHeight) {
        return buttonTop + ((buttonBottom - buttonTop) / 2) + textHeight * 0.55f;
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);
        Sounds sounds = (Sounds) game.getElements().getComponent(Elements.SOUNDS);

        if (gameState.isStateMenu()) {

            if (startHelper == 0) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    float eventX = event.getX();
                    float eventY = event.getY();

                    // Normal button
                    if (eventX >= buttonLeft && eventX <= buttonLeftHalf) {
                        if (eventY <= buttonStartBottom && eventY >= buttonStartTop) {
                            sounds.playBarHit();
                            gameState.setStateGame();
                            game.restart(false);
                        }
                    }

                    // Arcade button
                    if (eventX >= buttonRightHalf && eventX <= buttonRight) {
                        if (eventY <= buttonStartBottom && eventY >= buttonStartTop) {
                            sounds.playBarHit();
                            gameState.setStateArcade();
                            game.restart(false);
                        }
                    }

                    // Quit button
                    if (eventX >= buttonLeft && eventX <= buttonRight) {
                        if (eventY <= buttonQuitBottom && eventY >= buttonQuitTop) {
                            sounds.playBarHit();
                            game.getThread().setRunning(false);
                            ((Activity) game.getContext()).finish();
                        }
                    }

                }
            }
        }

    }
}