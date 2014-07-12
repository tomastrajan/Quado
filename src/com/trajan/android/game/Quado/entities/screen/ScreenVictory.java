
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
import com.trajan.android.game.Quado.components.Score;
import com.trajan.android.game.Quado.components.Sounds;
import com.trajan.android.game.Quado.entities.gui.Button;
import com.trajan.android.game.Quado.entities.gui.ButtonTouchListener;
import com.trajan.android.game.Quado.helpers.*;
import com.trajan.android.game.Quado.levels.LevelList;

import java.util.List;


public class ScreenVictory extends Screen {

    private static final String TAG = ScreenVictory.class.getSimpleName();

    private Button buttonQuit;
    private Button buttonContinue;

    public ScreenVictory(Dimensions dimensions, Context context, int messageId) {
        super(dimensions, context, messageId);
        buttonContinue = new Button(Button.BUTTON_2_1, Button.BUTTON_PRIMARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "CONTINUE");
        buttonContinue.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playHit();
                gameState.setStateGame();
                game.restart(true);
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
    }

    @Override
    public void render(MainGamePanel game,Canvas canvas, GameState gameState) {

        setzIndex(5);

        if (gameState.isStateVictory()) {
            canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

            prepareDefautlPaint(canvas);
            renderBorder(canvas);
            renderTitle(canvas);

            int highScoreTextSize = (int) (TextSizeCalculator.getDefaultTextSize(canvas) * 0.7);
            List<String> highScoreList = null;
            ExtStorage ext = (ExtStorage) game.getElements().getComponent(Elements.EXTERNAL_STORAGE_PROVIDER);
            if (ext != null) {
                highScoreList = ext.getHighScore(ExtStorage.HIGH_SCORE_NORMAL_FILE);
            }

            if (highScoreList != null) {

                int lineHeight = TextSizeCalculator.getHeightFromTextSize(highScoreTextSize) * 2;

                int left = dMargin;
                int right = canvas.getWidth() - dMargin;

                int baseTop = dMargin * 4;
                int counter = 0;

                paint.setTextSize(highScoreTextSize);
                canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);
                counter++;

                String hightScoreMessage = "HIGH SCORE";
                canvas.drawText(hightScoreMessage, canvas.getWidth() / 2, baseTop + (counter * lineHeight) - lineHeight / 4, paint);

                canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);
                counter++;

                for (String score : highScoreList) {

                    canvas.drawLine(left, baseTop + (counter * lineHeight), right, baseTop + (counter * lineHeight), paint);

                    String highScoreValue = score.split("\\|")[0];
                    String secondaryHighScoreValue = score.split("\\|")[1];
                    Score scoreComponent =  (Score) game.getElements().getComponent(Elements.SCORE);

                    if (Integer.parseInt(highScoreValue) == scoreComponent.getScore() && Integer.parseInt(secondaryHighScoreValue) == LevelList.getLevelId() + 1) {
                        paint.setColor(MyColors.getGuiNewHighScoreColor());
                    } else {
                        paint.setColor(MyColors.getGuiElementColor());
                    }

                    // Score values
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(highScoreValue, canvas.getWidth() / 2 - dMargin / 2, baseTop + (counter * lineHeight) - lineHeight / 4, paint);
                    canvas.drawText(secondaryHighScoreValue + ". level", canvas.getWidth() - dMargin * 1.5f, baseTop + (counter * lineHeight) - lineHeight / 4, paint);
                    paint.setColor(MyColors.getGuiElementColor());
                    // Line
                    canvas.drawLine(canvas.getWidth() / 2, baseTop + (counter * lineHeight), canvas.getWidth() / 2, baseTop + ((counter - 1) * lineHeight), paint);

                    counter++;
                }
            }

            buttonContinue.render(game, canvas, gameState);
            buttonQuit.render(game, canvas, gameState);
        }
    }

    private float getTextY(int buttonTop, int buttonBottom, int textHeight) {
        return buttonTop + ((buttonBottom - buttonTop) / 2) + textHeight * 0.55f;
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

        if (gameState.isStateVictory()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonContinue.handleTouchEvent(game, event);
                buttonQuit.handleTouchEvent(game, event);
            }
        }
    }

}