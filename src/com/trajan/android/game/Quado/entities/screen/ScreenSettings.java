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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.ExtStorage;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.RenderHelper;
import com.trajan.android.game.Quado.components.Sounds;
import com.trajan.android.game.Quado.entities.gui.Button;
import com.trajan.android.game.Quado.entities.gui.ButtonTouchListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;

public class ScreenSettings extends Screen {

    private static final String TAG = ScreenVictory.class.getSimpleName();

    private static final String VOLUME_HIGH_STRING = "VOLUME: HIGH";
    private static final String VOLUME_MUTE_STRING = "VOLUME: MUTE";
    private static final String VOLUME_MEDIUM_STRING = "VOLUME: MEDIUM";
    private static final String VOLUME_LOW_STRING = "VOLUME: LOW";

    private static final float VOLUME_HIGH_VALUE = 1.0f;
    private static final float VOLUME_MEDIUM_VALUE = 0.5f;
    public static final float VOLUME_LOW_VALUE = 0.2f;
    private static final float VOLUME_MUTE_VALUE = 0.0f;

    private String selectedVolume;
    private String selectedTheme;

    private Button buttonQuit;
    private Button buttonResume;
    private Button buttonNextLevel;
    private Button buttonChangeTheme;
    private Button buttonChangeVolume;

    public ScreenSettings(Dimensions dimensions, Context context, int messageId) {
        super(dimensions, context, messageId);

        initializeSelectedTheme();

        buttonResume = new Button(Button.BUTTON_2_1, Button.BUTTON_PRIMARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "RESUME");
        buttonResume.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                switch (gameState.getPreviousState()) {
                    case GameState.STATE_GAME: {
                        gameState.setStateGame();
                        break;
                    }
                    case GameState.STATE_ARCADE: {
                        gameState.setStateArcade();
                        break;
                    }
                    default: {
                        gameState.setStateMenu();
                    }
                }
            }
        });
        buttonQuit = new Button(Button.BUTTON_2_2, Button.BUTTON_SECONDARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight / 2, "QUIT");
        buttonQuit.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                if (gameState.getPreviousState() != gameState.STATE_MENU) {
                    gameState.setStateMenu();
                    game.restart(false);
                } else {
                    gameState.setStateMenu();
                }
            }
        });
        buttonNextLevel = new Button(Button.BUTTON_1, Button.BUTTON_PRIMARY, dContainerWidth, dMargin, dCanvasHeight - dMargin * 2 - dButtonHeight - dButtonHeight / 2 - dMargin, "NEXT LEVEL");
        buttonNextLevel.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                sounds.playBarHit();
                game.restart(false);
            }
        });
        buttonChangeVolume = new Button(Button.BUTTON_1, Button.BUTTON_PRIMARY, dContainerWidth, dMargin, (int) (dCanvasHeight * 0.2), selectedVolume);
        buttonChangeVolume.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                changeVolume(sounds);
            }
        });
        buttonChangeTheme = new Button(Button.BUTTON_1, Button.BUTTON_PRIMARY, dContainerWidth, dMargin, (int) (dCanvasHeight * 0.2) + dButtonHeight + dMargin, selectedTheme);
        buttonChangeTheme.addButtonTouchListener(new ButtonTouchListener() {
            @Override
            public void excute(MainGamePanel game, Sounds sounds, GameState gameState) {
                changeTheme(game, sounds);
            }
        });
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        setzIndex(5);

        if (gameState.isStatePause()) {

            canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

            prepareDefautlPaint(canvas);
            renderBorder(canvas);
            renderTitle(canvas);

            buttonChangeVolume.setButtonText(getVolumeName(game));
            buttonChangeTheme.setButtonText(selectedTheme);

            buttonQuit.render(game, canvas, gameState);
            buttonChangeTheme.render(game, canvas, gameState);
            buttonChangeVolume.render(game, canvas, gameState);

            if (gameState.getPreviousState() == gameState.STATE_ARCADE) {
                buttonNextLevel.render(game, canvas, gameState);
            }

            if (gameState.getPreviousState() != gameState.STATE_MENU) {
                buttonResume.render(game, canvas, gameState);
            }
        }
    }


    private String getVolumeName(MainGamePanel game) {

        if (selectedVolume == null) {

            Sounds sounds = (Sounds) game.getElements().getComponent(Elements.SOUNDS);

            if (sounds.getVolume() == VOLUME_MUTE_VALUE) {
                selectedVolume = VOLUME_MUTE_STRING;
            } else if (sounds.getVolume() == VOLUME_LOW_VALUE) {
                selectedVolume = VOLUME_LOW_STRING;
            } else if (sounds.getVolume() == VOLUME_MEDIUM_VALUE) {
                selectedVolume = VOLUME_MEDIUM_STRING;
            } else if (sounds.getVolume() == VOLUME_HIGH_VALUE) {
                selectedVolume = VOLUME_HIGH_STRING;
            }

        }
        return selectedVolume;
    }

    private void changeTheme(MainGamePanel game, Sounds sounds) {
        MyColors.selectNextTheme();

        selectedTheme = MyColors.getSelectedTheme();

        ExtStorage extStorage = (ExtStorage) game.getElements().getComponent(Elements.EXTERNAL_STORAGE_PROVIDER);
        extStorage.saveSettings("theme", Integer.toString(MyColors.getSelectedThemeConst()));


        RenderHelper renderHelper = (RenderHelper) game.getElements().getComponent(Elements.RENDER_HELPER);
        renderHelper.createGradientBitmap();

        sounds.playHit();
    }

    private void changeVolume(Sounds sounds) {
        if (selectedVolume.equals(VOLUME_MEDIUM_STRING)) {
            sounds.setVolume(VOLUME_HIGH_VALUE);
            selectedVolume = VOLUME_HIGH_STRING;
        } else if (selectedVolume.equals(VOLUME_LOW_STRING)) {
            sounds.setVolume(VOLUME_MEDIUM_VALUE);
            selectedVolume = VOLUME_MEDIUM_STRING;
        } else if (selectedVolume.equals(VOLUME_MUTE_STRING)) {
            sounds.setVolume(VOLUME_LOW_VALUE);
            selectedVolume = VOLUME_LOW_STRING;
        } else if (selectedVolume.equals(VOLUME_HIGH_STRING)) {
            sounds.setVolume(VOLUME_MUTE_VALUE);
            selectedVolume = VOLUME_MUTE_STRING;
        }

        sounds.playHit();
    }

    private void initializeSelectedTheme() {
        selectedTheme = MyColors.getSelectedTheme();
    }


    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

        if (gameState.isStatePause()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonQuit.handleTouchEvent(game, event);
                buttonChangeTheme.handleTouchEvent(game, event);
                buttonChangeVolume.handleTouchEvent(game, event);

                if (gameState.getPreviousState() == gameState.STATE_ARCADE) {
                    buttonNextLevel.handleTouchEvent(game, event);
                }

                if (gameState.getPreviousState() != gameState.STATE_MENU) {
                    buttonResume.handleTouchEvent(game, event);
                }
            }
        }

    }
}