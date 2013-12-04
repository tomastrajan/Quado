
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
import com.trajan.android.game.Quado.components.RenderHelper;
import com.trajan.android.game.Quado.components.Sounds;
import com.trajan.android.game.Quado.helpers.MyTouchEventListener;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;

public class ScreenPause extends BasicEntity implements MyTouchEventListener {

    private static final String TAG = ScreenVictory.class.getSimpleName();

    private String message;
    private int buttonLeft;
    private int buttonRight;
    private int buttonNextLevelTop;
    private int buttonNextLevelBottom;
    private int buttonResumeTop;
    private int buttonResumeBottom;
    private int buttonMainMenuTop;
    private int buttonMainMenuBottom;
    private int buttonVolumeTop;
    private int buttonVolumeBottom;
    private int buttonThemeTop;
    private int buttonThemeBottom;

    private static final String VOLUME_HIGH_STRING = "VOLUME: HIGH";
    private static final String VOLUME_MUTE_STRING = "VOLUME: MUTE";
    private static final String VOLUME_MEDIUM_STRING = "VOLUME: MEDIUM";
    private static final String VOLUME_LOW_STRING = "VOLUME: LOW";
    private String selectedVolume;
    private String selectedTheme;

    private static final float VOLUME_HIGH_VALUE = 1.0f;
    private static final float VOLUME_MEDIUM_VALUE = 0.5f;
    public static final float VOLUME_LOW_VALUE = 0.2f;
    private static final float VOLUME_MUTE_VALUE = 0.0f;

    public ScreenPause(Dimensions dimensions) {
        super(dimensions);
    }

    public ScreenPause(Dimensions dimensions, Context context, int messageId) {
        super(dimensions);
        this.message = context.getResources().getString(messageId);
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        initializeSelectedVolume(game);
        initializeSelectedTheme();

        setzIndex(5);

        if (gameState.isStatePause()) {

            canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

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
            canvas.drawText(message, canvas.getWidth() / 2, border * 2.5f + headingHeight * 0.5f, paint);


            buttonLeft = 2 * border;
            buttonRight = canvas.getWidth() - 2 * border;
            buttonNextLevelTop = canvas.getHeight() - 10 * border;
            buttonNextLevelBottom = canvas.getHeight() - 8 * border;
            buttonResumeTop = canvas.getHeight() - 7 * border;
            buttonResumeBottom = canvas.getHeight() - 5 * border;
            buttonMainMenuTop = canvas.getHeight() - 4 * border;
            buttonMainMenuBottom = canvas.getHeight() - 2 * border;

            // Draw buttons
            if (gameState.getPreviousState() == gameState.STATE_ARCADE) {
                canvas.drawRect(buttonLeft, buttonNextLevelTop, buttonRight, buttonNextLevelBottom, paint);
            }
            canvas.drawRect(buttonLeft, buttonResumeTop, buttonRight, buttonResumeBottom, paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(buttonLeft, buttonMainMenuTop, buttonRight, buttonMainMenuBottom, paint);

            // Draw button texts
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(MyColors.getGuiElementTextColor());
            int textHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize() * 0.7f);
            paint.setTextSize((int) (paint.getTextSize() * 0.7f));
            if (gameState.getPreviousState() == gameState.STATE_ARCADE) {
                canvas.drawText("NEXT LEVEL", canvas.getWidth() / 2, getTextY(buttonNextLevelTop, buttonNextLevelBottom, textHeight), paint);
            }
            canvas.drawText("RESUME", canvas.getWidth() / 2, getTextY(buttonResumeTop, buttonResumeBottom, textHeight), paint);

            paint.setColor(MyColors.getGuiElementColor());
            canvas.drawText("QUIT TO MENU", canvas.getWidth() / 2, getTextY(buttonMainMenuTop, buttonMainMenuBottom, textHeight), paint);

            // Draw settings
            buttonVolumeTop = 4 * border;
            buttonVolumeBottom = 6 * border;
            buttonThemeTop = 7 * border;
            buttonThemeBottom = 9 * border;

            canvas.drawRect(buttonLeft, buttonVolumeTop, buttonRight, buttonVolumeBottom, paint);
            canvas.drawRect(buttonLeft, buttonThemeTop, buttonRight, buttonThemeBottom, paint);

            // Draw settings texts
            paint.setColor(MyColors.getGuiElementTextColor());
            canvas.drawText(selectedVolume, canvas.getWidth() / 2, getTextY(buttonVolumeTop, buttonVolumeBottom, textHeight), paint);
            canvas.drawText(selectedTheme, canvas.getWidth() / 2, getTextY(buttonThemeTop, buttonThemeBottom, textHeight), paint);

        }
    }

    private float getTextY(int buttonTop, int buttonBottom, int textHeight) {
        return buttonTop + ((buttonBottom - buttonTop) / 2) + textHeight * 0.55f;
    }

    private void initializeSelectedVolume(MainGamePanel game) {

        if (selectedVolume == null) {

            Sounds sounds = (Sounds) game.getElements().getComponent(Elements.SOUNDS);

            if (sounds.getVolume() == VOLUME_MUTE_VALUE) {
                selectedVolume = VOLUME_MUTE_STRING;
            } else if (sounds.getVolume() == VOLUME_LOW_VALUE ) {
                selectedVolume = VOLUME_LOW_STRING;
            } else if (sounds.getVolume() == VOLUME_MEDIUM_VALUE) {
                selectedVolume = VOLUME_MEDIUM_STRING;
            } else if (sounds.getVolume() == VOLUME_HIGH_VALUE) {
                selectedVolume = VOLUME_HIGH_STRING;
            }

        }

    }

    private void initializeSelectedTheme() {
        selectedTheme = MyColors.getSelectedTheme();
    }

    @Override
    public void handleTouchEvent(MainGamePanel game, MotionEvent event) {

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);
        Sounds sounds = (Sounds) game.getElements().getComponent(Elements.SOUNDS);

        if (gameState.isStatePause()) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                float eventX = event.getX();
                float eventY = event.getY();

                // Next level button
                if (gameState.getPreviousState() == gameState.STATE_ARCADE &&
                        eventX >= buttonLeft && eventX <= buttonRight) {
                    if (eventY <= buttonNextLevelBottom && eventY >= buttonNextLevelTop) {
                        sounds.playBarHit();
                        gameState.setStateArcade();
                        game.restart(false);
                    }
                }

                // Resume button
                if (eventX >= buttonLeft && eventX <= buttonRight) {
                    if (eventY <= buttonResumeBottom && eventY >= buttonResumeTop) {
                        sounds.playBarHit();
                        if (gameState.getPreviousState() == GameState.STATE_GAME) {
                            gameState.setStateGame();
                        } else {
                            gameState.setStateArcade();
                        }
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

                // Volume button
                if (eventX >= buttonLeft && eventX <= buttonRight) {
                    if (eventY <= buttonVolumeBottom && eventY >= buttonVolumeTop) {

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
                }

                // Theme button
                if (eventX >= buttonLeft && eventX <= buttonRight) {
                    if (eventY <= buttonThemeBottom && eventY >= buttonThemeTop) {

                        MyColors.selectNextTheme();

                        ExtStorage extStorage = (ExtStorage) game.getElements().getComponent(Elements.EXTERNAL_STORAGE_PROVIDER);
                        extStorage.saveSettings("theme", Integer.toString(MyColors.getSelectedThemeConst()));

                        RenderHelper renderHelper = (RenderHelper) game.getElements().getComponent(Elements.RENDER_HELPER);
                        renderHelper.createGradientBitmap();

                        sounds.playHit();
                    }
                }

            }
        }

    }
}