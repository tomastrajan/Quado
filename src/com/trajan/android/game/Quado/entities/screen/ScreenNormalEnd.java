package com.trajan.android.game.Quado.entities.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.*;
import com.trajan.android.game.Quado.entities.gui.Button;
import com.trajan.android.game.Quado.entities.gui.ButtonTouchListener;
import com.trajan.android.game.Quado.entities.gui.Label;
import com.trajan.android.game.Quado.entities.gui.Table;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;
import com.trajan.android.game.Quado.levels.LevelList;
import com.trajan.android.game.Quado.model.GameMode;
import com.trajan.android.game.Quado.model.ScoreRecord;
import com.trajan.android.game.Quado.rest.dto.ScoreDto;

import java.util.ArrayList;
import java.util.List;

import static com.trajan.android.game.Quado.entities.screen.ScoreType.*;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-12
 */
public class ScreenNormalEnd extends Screen {

    private static final String TAG = ScreenNormalEnd.class.getSimpleName();

    private Button buttonQuit;
    private Button buttonRetry;
    private Button buttonScoreLocal;
    private Button buttonScoreGobal;
    private Button buttonScoreTop10;
    private Table table;
    private Label achievedScore;
    private List<List<String>> scores;

    private ScoreType scoreType = LOCAL;

    public ScreenNormalEnd(Dimensions dimensions, Context context, int messageId) {
        super(dimensions, context, messageId);
        buttonScoreLocal = new Button(Button.BUTTON_3_1, Button.PRIMARY, dContainerWidth, dMargin, (int) (dCanvasHeight * 0.30f) - dButtonHeight / 2, "LOCAL");
        buttonScoreLocal.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                highlightActiveButton(buttonScoreLocal);
                resetScore();
                scoreType = LOCAL;
            }
        });
        buttonScoreGobal = new Button(Button.BUTTON_3_2, Button.SECONDARY, dContainerWidth, dMargin, (int) (dCanvasHeight * 0.30f) - dButtonHeight / 2, "YOU");
        buttonScoreGobal.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                highlightActiveButton(buttonScoreGobal);
                resetScore();
                scoreType = GLOBAL;
            }
        });
        buttonScoreTop10 = new Button(Button.BUTTON_3_3, Button.SECONDARY, dContainerWidth, dMargin, (int) (dCanvasHeight * 0.30f) - dButtonHeight / 2, "TOP 10");
        buttonScoreTop10.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                highlightActiveButton(buttonScoreTop10);
                resetScore();
                scoreType = TOP10;
            }
        });
        buttonRetry = new Button(Button.BUTTON_2_1, Button.PRIMARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "CONTINUE");
        buttonRetry.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                if (gameState.isStateNormalDefeat()) {
                    gameState.setStateNormal();
                    game.restart(RestartGameContext.create());
                } else {
                    gameState.setStateNormal();
                    game.restart(RestartGameContext.create().setScore(game.getScore()));
                }
            }
        });
        buttonQuit = new Button(Button.BUTTON_2_2, Button.SECONDARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "QUIT");
        buttonQuit.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                gameState.setStateMenu();
                game.restart(RestartGameContext.create());
            }
        });
        achievedScore = new Label(dContainerWidth, dMargin, (int) (dCanvasHeight * 0.20f), "");
        List<String> headers = new ArrayList<String>();
        headers.add("#");
        headers.add("Name");
        headers.add("Level");
        headers.add("Score");
        table = new Table(dContainerWidth, dMargin, (int) (dCanvasHeight * 0.35),  headers, null);
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {
        if (gameState.isStateNormalDefeat() ||  gameState.isStateNormalVictory()) {

            if (gameState.isStateNormalDefeat()) {
                message = "LOL NOOB";
            } else {
                message = "EPIC WIN";
            }

            setzIndex(5);

            canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);

            prepareDefautlPaint(canvas);
            renderBorder(canvas);
            renderTitle(canvas);

            String level = String.valueOf((LevelList.getLevelId() + 1));
            String score = String.valueOf(((Score) game.getElements().getComponent(Elements.SCORE)).getScore());
            achievedScore.setValue(level + ". level - score : " + score);
            achievedScore.setColor(MyColors.getGuiNewHighScoreColor());
            achievedScore.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas) / 2);
            achievedScore.render(game, canvas, gameState);

            switch (scoreType) {
                case LOCAL: {
                    initLocalScore(game, gameState);
                    break;
                }
                case GLOBAL: {
                    getRemoteScores(game, GLOBAL);
                    break;
                }
                case TOP10: {
                    getRemoteScores(game, TOP10);
                    break;
                }
            }

            table.render(game, canvas, gameState);

            buttonScoreLocal.render(game, canvas, gameState);
            buttonScoreGobal.render(game, canvas, gameState);
            buttonScoreTop10.render(game, canvas, gameState);
            buttonRetry.render(game, canvas, gameState);
            buttonQuit.render(game, canvas, gameState);
        }
    }

    private void initLocalScore(MainGamePanel game, GameState gameState) {
        LocalPersistenceService lps = game.getLocalPersistenceService();
        String actualScore = String.valueOf(game.getScore().getScore());

        if (scores == null) {
            scores = new ArrayList<>();

            List<ScoreRecord> scoresFromStorage;
            int i = 1;
            if (lps != null) {
                scoresFromStorage = lps.getHighScore(LocalPersistenceService.HIGH_SCORE_NORMAL_FILE);
                for (ScoreRecord score : scoresFromStorage) {
                    List<String> scoreRow = new ArrayList<>();
                    scoreRow.add("" + i);
                    scoreRow.add(score.getPlayerName());
                    scoreRow.add(score.getInfo() + ".");
                    scoreRow.add(score.getScore());
                    scores.add(scoreRow);
                    if (gameState.isStateNormalVictory() &&
                            score.getScore().equals(actualScore) && lps.getSelectedPlayer().getUUID().equals(score.getPlayerUUID())) {
                        table.addHighlightedRow(i - 1);
                    }
                    i++;
                }
            }
            table.setRows(scores);
        }
    }

    private void getRemoteScores(MainGamePanel game, ScoreType scoreType) {
        if (scores == null) {
            LocalPersistenceService lps = game.getLocalPersistenceService();
            scores = new ArrayList<>();
            table.resetHighlightedRows();

            List<ScoreDto> remoteScores;
            if (scoreType == TOP10) {
                remoteScores = game.getRest().findTopTenScores(GameMode.NORMAL);
            } else {
                remoteScores = game.getRest().findScoresForPlayer(GameMode.NORMAL, lps.getSelectedPlayer().getUUID());
            }
            int i = 0;
            for (ScoreDto score : remoteScores) {
                scores.add(score.getAsList(GameMode.NORMAL));
                if (lps.getSelectedPlayer().getUUID().equals(score.getPlayerUuid())) {
                    table.addHighlightedRow(i);
                }
                i++;
            }
            table.setRows(scores);
        }
    }

    public void resetScore() {
        scores = null;
    }

    private void highlightActiveButton(Button activeButton) {
        buttonScoreLocal.setSelectedPaintType(Button.SECONDARY);
        buttonScoreGobal.setSelectedPaintType(Button.SECONDARY);
        buttonScoreTop10.setSelectedPaintType(Button.SECONDARY);
        activeButton.setSelectedPaintType(Button.PRIMARY);
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

        if (gameState.isStateNormalDefeat() || gameState.isStateNormalVictory()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonQuit.handleTouchEvent(game, event);
                buttonRetry.handleTouchEvent(game, event);
                if (buttonScoreLocal.getSelectedPaintType() == Button.SECONDARY) {
                    buttonScoreLocal.handleTouchEvent(game, event);
                }
                if (buttonScoreGobal.getSelectedPaintType() == Button.SECONDARY) {
                    buttonScoreGobal.handleTouchEvent(game, event);
                }
                if (buttonScoreTop10.getSelectedPaintType() == Button.SECONDARY) {
                    buttonScoreTop10.handleTouchEvent(game, event);
                }
            }
        }
    }
}
