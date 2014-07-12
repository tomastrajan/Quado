
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

package com.trajan.android.game.Quado.entities.screen;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.ExtStorage;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.Sounds;
import com.trajan.android.game.Quado.entities.gui.Button;
import com.trajan.android.game.Quado.entities.gui.ButtonTouchListener;
import com.trajan.android.game.Quado.entities.gui.Table;
import com.trajan.android.game.Quado.helpers.*;
import com.trajan.android.game.Quado.levels.LevelList;

import java.util.ArrayList;
import java.util.List;

public class ScreenDefeat extends Screen {

    private static final String TAG = ScreenVictory.class.getSimpleName();

    private Button buttonQuit;
    private Button buttonRetry;
    private Table table;

    public ScreenDefeat(Dimensions dimensions, Context context, int messageId) {
        super(dimensions, context, messageId);
        buttonRetry = new Button(Button.BUTTON_2_1, Button.BUTTON_PRIMARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "CONTINUE");
        buttonRetry.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                if (gameState.getPreviousState() == GameState.STATE_GAME) {
                    gameState.setStateGame();
                } else {
                    gameState.setStateArcade();
                }
                game.restart(false);
            }
        });
        buttonQuit = new Button(Button.BUTTON_2_2, Button.BUTTON_SECONDARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "QUIT");
        buttonQuit.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                gameState.setStateMenu();
                game.restart(false);
            }
        });
        List<String> headers = new ArrayList<String>();
        headers.add("#");
        headers.add("Name");
        headers.add("Time");
        headers.add("Score");
        List<List<String>> rows = new ArrayList<List<String>>();
        List<String> row = new ArrayList<String>();
        row.add("78954");
        row.add("Janko");
        row.add("158 sec");
        row.add("85000");
        rows.add(row);
        List<String> row1 = new ArrayList<String>();
        row1.add("78955");
        row1.add("Peternosis");
        row1.add("321 sec");
        row1.add("158900");
        rows.add(row1);
        table = new Table(dContainerWidth, dMargin, (int) (dCanvasHeight * 0.2),  headers, rows);
    }

    @Override
    public void render(MainGamePanel game,Canvas canvas, GameState gameState) {

        setzIndex(5);

        if (gameState.isStateDefeat()) {

            canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);

            int border = canvas.getWidth() / 15;

            prepareDefautlPaint(canvas);
            renderBorder(canvas);

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

            table.render(game, canvas, gameState);
            buttonRetry.render(game, canvas, gameState);
            buttonQuit.render(game, canvas, gameState);


//            if (highScoreList != null) {
//
//                int lineHeight = TextSizeCalculator.getHeightFromTextSize(highScoreTextSize) * 2;
//
//                int left = border;
//                int right = canvas.getWidth() - border;
//
//                int baseTop = border * 4;
//                int counter = 0;
//
//                paint.setColor(MyColors.getGuiElementColor());
//                paint.setTextSize(highScoreTextSize);
//                canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);
//                counter++;
//
//                canvas.drawText("HIGH SCORE", canvas.getWidth() / 2, baseTop + (counter * lineHeight) - lineHeight / 4, paint);
//
//                canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);
//                counter++;
//
//                for (String score : highScoreList) {
//
//                    if (score != null && !score.equals("") && score.split("\\|").length == 2) {
//
//                        canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);
//
//                        String highScoreValue = score.split("\\|")[0];
//                        String secondaryHighScoreValue = score.split("\\|")[1];
//
//                        // Arcade
//                        if (gameState.getPreviousState() == GameState.STATE_ARCADE) {
//
//                            Score scoreComponent = (Score) game.getElements().getComponent(Elements.SCORE);
//
//                            if (Integer.parseInt(highScoreValue) == scoreComponent.getScore() && Integer.parseInt(secondaryHighScoreValue) == secondaryScoreIntValue) {
//                                paint.setColor(MyColors.getGuiNewHighScoreColor());
//                            }
//                        }
//
//                        // Score values
//                        paint.setTextAlign(Paint.Align.RIGHT);
//                        canvas.drawText(highScoreValue, canvas.getWidth() / 2 - border / 2, baseTop + (counter * lineHeight) - lineHeight / 4, paint);
//                        canvas.drawText(secondaryHighScoreValue + secondaryScoreShortName, canvas.getWidth() - border * 1.5f, baseTop + (counter * lineHeight) - lineHeight / 4, paint);
//                        paint.setColor(MyColors.getGuiElementColor());
//                        // Line
//                        canvas.drawLine(canvas.getWidth() / 2, baseTop + (counter * lineHeight), canvas.getWidth() / 2, baseTop + ((counter - 1) * lineHeight), paint);
//
//                        counter++;
//
//                    }
//                }
//            }

        }
    }

    private float getTextY(int buttonTop, int buttonBottom, int textHeight) {
        return buttonTop + ((buttonBottom - buttonTop) / 2) + textHeight * 0.55f;
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

        if (gameState.isStateDefeat()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonQuit.handleTouchEvent(game, event);
                buttonRetry.handleTouchEvent(game, event);
            }
        }
    }

}