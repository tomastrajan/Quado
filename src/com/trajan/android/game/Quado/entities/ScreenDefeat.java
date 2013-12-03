
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

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.ExtStorage;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.Score;
import com.trajan.android.game.Quado.components.Sounds;
import com.trajan.android.game.Quado.helpers.MyTouchEventListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;
import com.trajan.android.game.Quado.levels.LevelList;

import java.util.List;

public class ScreenDefeat extends BasicEntity implements MyTouchEventListener {

    private static final String TAG = ScreenVictory.class.getSimpleName();

    private String message;

    private int buttonLeft;
    private int buttonRight;
    private int buttonRetryTop;
    private int buttonRetryBottom;
    private int buttonMainMenuTop;
    private int buttonMainMenuBottom;

    public ScreenDefeat(Dimensions dimensions) {
        super(dimensions);
    }

    public ScreenDefeat(Dimensions dimensions, Context context, int messageId) {
        super(dimensions);
        this.message = context.getResources().getString(messageId);
    }

    @Override
    public void render(MainGamePanel game,Canvas canvas, GameState gameState) {

        setzIndex(5);

        if (gameState.isStateDefeat()) {

            canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);

            int border = canvas.getWidth() / 15;

            paint.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas));
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2f);
            paint.setColorFilter(MyColors.getFilterGui());

            paint.setColor(MyColors.getGuiElementColor());
            canvas.drawLine(border, border, canvas.getWidth() - border, border, paint);
            canvas.drawLine(border, border, border, canvas.getHeight() - border, paint);
            canvas.drawLine(border, canvas.getHeight() - border, canvas.getWidth() - border, canvas.getHeight() - border, paint);
            canvas.drawLine(canvas.getWidth() - border, border, canvas.getWidth() - border, canvas.getHeight() - border, paint);

            int headingHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
            int highScoreTextSize = (int) (TextSizeCalculator.getDefaultTextSize(canvas) * 0.7);


            ExtStorage ext = (ExtStorage) game.getElements().getComponent(Elements.EXTERNAL_STORAGE_PROVIDER);

            String heading = "";
            String secondaryScoreName= "";
            String secondaryScoreShortName= "";
            String secondaryScoreValue = "";
            int secondaryScoreIntValue = 0;
            List<String> highScoreList = null;

            // Normal
            if (gameState.getPreviousState() == GameState.STATE_GAME) {

                heading = "LOL NOOB";
                secondaryScoreName = "level";
                secondaryScoreShortName = ". level";

                // Level value
                secondaryScoreIntValue = LevelList.getLevelId() + 1;
                secondaryScoreValue = Integer.toString(secondaryScoreIntValue);

                if (ext != null) {
                    highScoreList = ext.getHighScore(ExtStorage.HIGH_SCORE_NORMAL_FILE);
                }

            // Arcade
            } else if (gameState.getPreviousState() == GameState.STATE_ARCADE) {

                heading = "NICE TRY!";
                secondaryScoreName = "seconds";
                secondaryScoreShortName = " sec";

                // Seconds value
                secondaryScoreIntValue = (int) ((gameState.getArcadeTimeEnd() - gameState.getArcadeTimeStart()) / 1000.0f);
                secondaryScoreValue = Integer.toString(secondaryScoreIntValue);

                if (ext != null) {
                    highScoreList = ext.getHighScore(ExtStorage.HIGH_SCORE_ARCADE_FILE);
                }

            }

            // Heading
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(heading, border * 1.5f, border * 2.5f + headingHeight * 0.5f, paint);

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(paint.getTextSize()* 0.9f);
            canvas.drawText(secondaryScoreValue, canvas.getWidth() - border *2.5f, border * 2.7f, paint);
            paint.setTextSize(paint.getTextSize()* 0.4f);
            canvas.drawText(secondaryScoreName, canvas.getWidth() - border * 2.5f, border * 3.5f, paint);
            canvas.drawLine(canvas.getWidth() - border * 4f, border, canvas.getWidth() - border * 4f, border * 4f, paint);

            // Restore defaults
            paint.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas));
            paint.setTextAlign(Paint.Align.CENTER);


            if (highScoreList != null) {

                int lineHeight = TextSizeCalculator.getHeightFromTextSize(highScoreTextSize) * 2;

                int left = border;
                int right = canvas.getWidth() - border;

                int baseTop = border * 4;
                int counter = 0;

                paint.setColor(MyColors.getGuiElementColor());
                paint.setTextSize(highScoreTextSize);
                canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);
                counter++;

                canvas.drawText("HIGH SCORE", canvas.getWidth() / 2, baseTop + (counter * lineHeight) - lineHeight / 4, paint);

                canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);
                counter++;

                for (String score : highScoreList) {

                    if (score != null && !score.equals("") && score.split("\\|").length == 2) {

                        canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);

                        String highScoreValue = score.split("\\|")[0];
                        String secondaryHighScoreValue = score.split("\\|")[1];

                        // Arcade
                        if (gameState.getPreviousState() == GameState.STATE_ARCADE) {

                            Score scoreComponent = (Score) game.getElements().getComponent(Elements.SCORE);

                            if (Integer.parseInt(highScoreValue) == scoreComponent.getScore() && Integer.parseInt(secondaryHighScoreValue) == secondaryScoreIntValue) {
                                paint.setColor(MyColors.getGuiNewHighScoreColor());
                            }
                        }

                        // Score values
                        paint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(highScoreValue, canvas.getWidth() / 2 - border / 2, baseTop + (counter * lineHeight) - lineHeight / 4, paint);
                        canvas.drawText(secondaryHighScoreValue + secondaryScoreShortName, canvas.getWidth() - border * 1.5f, baseTop + (counter * lineHeight) - lineHeight / 4, paint);
                        paint.setColor(MyColors.getGuiElementColor());
                        // Line
                        canvas.drawLine(canvas.getWidth() / 2, baseTop + (counter * lineHeight), canvas.getWidth() / 2, baseTop + ((counter - 1) * lineHeight), paint);

                        counter++;

                    }
                }
            }

            paint.setTextAlign(Paint.Align.CENTER);

            buttonLeft = 2 * border;
            buttonRight = canvas.getWidth() - 2 * border;
            buttonRetryTop = canvas.getHeight() - 7 * border;
            buttonRetryBottom = canvas.getHeight() - 5 * border;
            buttonMainMenuTop = canvas.getHeight() - 4 * border;
            buttonMainMenuBottom = canvas.getHeight() - 2 * border;

            // Draw buttons
            canvas.drawRect(buttonLeft, buttonRetryTop, buttonRight, buttonRetryBottom, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(buttonLeft, buttonMainMenuTop, buttonRight, buttonMainMenuBottom, paint);

            // Draw button texts
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(MyColors.getGuiElementTextColor());
            int textHeight = TextSizeCalculator.getHeightFromTextSize(highScoreTextSize);
            canvas.drawText("RETRY", canvas.getWidth() / 2, getTextY(buttonRetryTop, buttonRetryBottom, textHeight), paint);

            paint.setColor(MyColors.getGuiElementColor());
            canvas.drawText("QUIT TO MENU", canvas.getWidth() / 2, getTextY(buttonMainMenuTop, buttonMainMenuBottom, textHeight), paint);

        }
    }

    private float getTextY(int buttonTop, int buttonBottom, int textHeight) {
        return buttonTop + ((buttonBottom - buttonTop) / 2) + textHeight * 0.55f;
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);
        Sounds sounds = (Sounds) game.getElements().getComponent(Elements.SOUNDS);

        if (gameState.isStateDefeat()) {


            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                float eventX = event.getX();
                float eventY = event.getY();

                // Retry button
                if (eventX >= buttonLeft && eventX <= buttonRight) {
                    if (eventY <= buttonRetryBottom && eventY >= buttonRetryTop) {
                        sounds.playBarHit();
                        if (gameState.getPreviousState() == GameState.STATE_GAME) {
                            gameState.setStateGame();
                        } else {
                            gameState.setStateArcade();
                        }
                        game.restart(false);
                    }
                }

                // Quit to menu button
                if (eventX >= buttonLeft && eventX <= buttonRight) {
                    if (eventY <= buttonMainMenuBottom && eventY >= buttonMainMenuTop) {
                        sounds.playBarHit();
                        gameState.setStateMenu();
                        game.restart(false);
                    }
                }

            }
        }


    }



}