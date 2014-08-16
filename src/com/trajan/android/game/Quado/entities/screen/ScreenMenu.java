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

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.RestartGameContext;
import com.trajan.android.game.Quado.components.Sounds;
import com.trajan.android.game.Quado.entities.gui.Button;
import com.trajan.android.game.Quado.entities.gui.ButtonTouchListener;
import com.trajan.android.game.Quado.entities.gui.DialogAddPlayer;
import com.trajan.android.game.Quado.entities.gui.DialogListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;
import com.trajan.android.game.Quado.levels.Level;
import com.trajan.android.game.Quado.model.Player;

public class ScreenMenu extends Screen {

    private static final String TAG = ScreenMenu.class.getSimpleName();

    private static final int START_DURATION = 10;

    private int startHelper = START_DURATION;
    private float textSize = 0;

    private Button buttonNormal;
    private Button buttonArcade;
    private Button buttonQuit;
    private Button buttonSettings;

    public ScreenMenu(Dimensions dimensions, final Context context, int messageId) {
        super(dimensions, context, messageId);

        startHelper = START_DURATION;

        buttonNormal = new Button(Button.BUTTON_1, Button.SECONDARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 3 - dButtonHeight - dButtonHeight / 2, "CLASSIC");
        buttonNormal.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(final MainGamePanel game, Sounds sounds, GameState gameState) {
                DialogListener success = new DialogListener() {
                    @Override
                    public void execute() {
                        startNormal(game);
                    }
                };
                if (isPlayer(game, success)) {
                    startNormal(game);
                }
            }
        });
        buttonArcade = new Button(Button.BUTTON_1, Button.PRIMARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 4 - dButtonHeight * 2 - dButtonHeight / 2, "ARCADE");
        buttonArcade.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(final MainGamePanel game, Sounds sounds, GameState gameState) {
                DialogListener success = new DialogListener() {
                    @Override
                    public void execute() {
                        startArcade(game);
                    }
                };
                if (isPlayer(game, success)) {
                    startArcade(game);
                }
            }
        });
        buttonSettings = new Button(Button.BUTTON_2_1, Button.SECONDARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "SETTINGS");
        buttonSettings.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(final MainGamePanel game, Sounds sounds, GameState gameState) {
                DialogListener success = new DialogListener() {
                    @Override
                    public void execute() {
                        game.getGameState().setStatePause();
                    }
                };
                if (isPlayer(game, success)) {
                    game.getGameState().setStatePause();
                }
            }
        });
        buttonQuit = new Button(Button.BUTTON_2_2, Button.SECONDARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "QUIT");
        buttonQuit.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                game.getThread().setRunning(false);
                ((Activity) game.getContext()).finish();
            }
        });
    }

    private void startArcade(MainGamePanel game) {
        game.getSounds().playBarHit();
        game.getGameState().setStateArcade();
        game.restart(RestartGameContext.create());
    }

    private void startNormal(MainGamePanel game) {
        game.getSounds().playBarHit();
        game.getGameState().setStateNormal();
        game.restart(RestartGameContext.create());
    }

    private boolean isPlayer(final MainGamePanel game, DialogListener success) {
        Player player = game.getLocalPersistenceService().getSelectedPlayer();
        if (player == null) {
            new DialogAddPlayer(game, success, null);
            return false;
        }
        return true;
    }

    @Override
    public void render(final MainGamePanel game, Canvas canvas, final GameState gameState) {

        setzIndex(5);

        if (gameState.isStateMenu()) {

            if (startHelper > 0) {
                startHelper--;
            }

            // Do once
            if (textSize == 0) {
                paint.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas));
            }

            canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2f);
            paint.setColor(MyColors.getGuiElementColor());
            paint.setColorFilter(MyColors.getFilterGui());

            renderBorder(canvas);


            Level level = game.getElements().getLevel();
            int nameHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
            canvas.drawText(message, canvas.getWidth() / 2, level.getLevelCenterY() + nameHeight * 0.45f, paint);

            paint.setTextSize(paint.getTextSize() * 0.3f);
            paint.setAlpha(64);
            canvas.drawText("Trajan 2014", canvas.getWidth() / 2, level.getLevelCenterY() + nameHeight, paint);

            buttonNormal.render(game, canvas, gameState);
            buttonArcade.render(game, canvas, gameState);
            buttonSettings.render(game, canvas, gameState);
            buttonQuit.render(game, canvas, gameState);

        }
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

        if (gameState.isStateMenu()) {
            if (gameState.getPreviousState() == gameState.STATE_PAUSE) {
                gameState.setPreviousState(GameState.STATE_MENU);
                startHelper = START_DURATION;
            }
            if (startHelper <= 0) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonArcade.handleTouchEvent(game, event);
                    buttonNormal.handleTouchEvent(game, event);
                    buttonSettings.handleTouchEvent(game, event);
                    buttonQuit.handleTouchEvent(game, event);
                }
            }
        }

    }
}