
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
import com.trajan.android.game.Quado.entities.gui.Label;
import com.trajan.android.game.Quado.entities.gui.Table;
import com.trajan.android.game.Quado.helpers.*;

import java.util.ArrayList;
import java.util.List;

public class ScreenDefeatArcade extends Screen {

    private static final String TAG = ScreenVictory.class.getSimpleName();

    private Button buttonQuit;
    private Button buttonRetry;
    private Table table;
    private Label achievedScore;
    private List<List<String>> scores;

    public ScreenDefeatArcade(Dimensions dimensions, Context context, int messageId) {
        super(dimensions, context, messageId);
        buttonRetry = new Button(Button.BUTTON_2_1, Button.BUTTON_PRIMARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "RETRY");
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
        achievedScore = new Label(dContainerWidth, dMargin, (int) (dCanvasHeight * 0.20f), "");
        List<String> headers = new ArrayList<String>();
        headers.add("#");
        headers.add("Name");
        headers.add("Time");
        headers.add("Score");
        table = new Table(dContainerWidth, dMargin, (int) (dCanvasHeight * 0.25),  headers, null);
    }

    public void resetScore() {
        scores = null;
    }

    private void initScore(MainGamePanel game) {
        ExtStorage ext = (ExtStorage) game.getElements().getComponent(Elements.EXTERNAL_STORAGE_PROVIDER);
        String actualScore = String.valueOf(((Score) game.getElements().getComponent(Elements.SCORE)).getScore());
        if (scores == null) {
            scores = new ArrayList<>();

            List<String> scoresFromStorage;
            int i = 1;
            if (ext != null) {
                scoresFromStorage = ext.getHighScore(ExtStorage.HIGH_SCORE_ARCADE_FILE);
                for (String score : scoresFromStorage) {
                    List<String> scoreRow = new ArrayList<>();
                    scoreRow.add("" + i);
                    scoreRow.add("Player");
                    scoreRow.add(score.split("\\|")[1] + " s");
                    scoreRow.add(score.split("\\|")[0]);
                    scores.add(scoreRow);
                    if (score.split("\\|")[0].equals(actualScore)) {
                        table.addHighlightedRow(i - 1);
                    }
                    i++;
                }
            }
            table.setRows(scores);
        }
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {
        if (gameState.isStateDefeatArcade()) {
            setzIndex(5);

            canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);

            prepareDefautlPaint(canvas);
            renderBorder(canvas);
            renderTitle(canvas);

            initScore(game);

            String time = String.valueOf(((gameState.getArcadeTimeEnd() - gameState.getArcadeTimeStart()) / 1000.0f));
            String score = String.valueOf(((Score) game.getElements().getComponent(Elements.SCORE)).getScore());
            achievedScore.setValue("Score: " + score + ", time: " + time + " s");
            achievedScore.setColor(MyColors.getGuiNewHighScoreColor());
            achievedScore.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas) / 2);
            achievedScore.render(game, canvas, gameState);

            table.render(game, canvas, gameState);
            buttonRetry.render(game, canvas, gameState);
            buttonQuit.render(game, canvas, gameState);
        }
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

        if (gameState.isStateDefeatArcade()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonQuit.handleTouchEvent(game, event);
                buttonRetry.handleTouchEvent(game, event);
            }
        }
    }

}