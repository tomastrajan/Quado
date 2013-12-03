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

package com.trajan.android.game.Quado.helpers;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;

public class MyColors {

    private static final String TAG = MyColors.class.getSimpleName();

    public static final int THEME_RED = 0;
    public static final int THEME_GREEN = 1;
    public static final int THEME_DARK_BLUE = 2;
    public static final int THEME_PURPLE = 3;
    public static final int THEME_YELLOW = 4;
    public static final int THEME_BLACK = 5;
    public static final int THEME_WHITE = 6;

    private static String selectedTheme;
    private static int selectedThemeConst;

    // Gui
    private static int guiElementColor;
    private static int guiElementTextColor;
    private static int guiNewHighScoreColor;

    // Background
    private static int backgroundGradientLighter;
    private static int backgroundGradientDarker;

    // Entities
    private static int ballColor;
    private static int barColor;

    // Blocks
    private static int blockRemovedColor;
    private static int blockColor1;
    private static int blockColor2;
    private static int blockColor3;
    private static int blockColor4;

    // Alphas
    private static int alphaAlmostTransparent;

    // Filters
    private static ColorFilter filterGui;
    private static ColorFilter filterScoreDisplayHitEffect;

    public static void selectNextTheme() {

        if (setColorTheme(selectedThemeConst + 1)) {

        } else {
            selectedThemeConst = 0;
            setColorTheme(selectedThemeConst);
        }

    }

    public static boolean setColorTheme(int colorTheme) {

        boolean themeSuccessfullySelected = false;

        if (colorTheme == THEME_DARK_BLUE) {

            selectedTheme = "THEME: BLUE";
            selectedThemeConst = THEME_DARK_BLUE;

            // Gui
            guiElementColor = Color.WHITE;
            guiElementTextColor = Color.BLACK;
            guiNewHighScoreColor = Color.YELLOW;

            // Background
            backgroundGradientLighter = Color.parseColor("#0b5090");
            backgroundGradientDarker = Color.parseColor("#010b3b");

            // Entities
            ballColor = Color.parseColor("#ffe400");
            barColor = Color.WHITE;

            // Blocks
            blockRemovedColor = Color.WHITE;
            blockColor1 = Color.parseColor("#8bc7ff");
            blockColor2 = Color.parseColor("#5b9de8");
            blockColor3 = Color.parseColor("#3288e8");
            blockColor4 = Color.parseColor("#1065c5");

            // Alphas
            alphaAlmostTransparent = 15;

            // Filters
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB){
                filterGui = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.ADD);
                filterScoreDisplayHitEffect = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.OVERLAY);
            } else{
                filterGui = null;
                filterScoreDisplayHitEffect = null;
            }

            themeSuccessfullySelected = true;

        }

        if (colorTheme == THEME_RED) {

            selectedTheme = "THEME: RED";
            selectedThemeConst = THEME_RED;

            // Gui
            guiElementColor = Color.parseColor("#ffffff");
            guiElementTextColor = Color.BLACK;
            guiNewHighScoreColor = Color.parseColor("#ffe400");

            // Background
            backgroundGradientLighter = Color.parseColor("#d7423c");
            backgroundGradientDarker = Color.parseColor("#7c1812");

            // Entities
            ballColor = Color.parseColor("#ffe400");
            barColor = Color.parseColor("#000000");

            // Blocks
            blockRemovedColor = Color.BLACK;
            blockColor1 = Color.parseColor("#ffffff");
            blockColor2 = Color.parseColor("#ffffff");
            blockColor3 = Color.parseColor("#ffffff");
            blockColor4 = Color.parseColor("#ffffff");

            // Alphas
            alphaAlmostTransparent = 25;

            // Filters
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB){
                filterGui = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.ADD);
                filterScoreDisplayHitEffect = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.MULTIPLY);
            } else{
                filterGui = null;
                filterScoreDisplayHitEffect = null;
            }

            themeSuccessfullySelected = true;

        }

        if (colorTheme == THEME_YELLOW) {

            selectedTheme = "THEME: YELLOW";
            selectedThemeConst = THEME_YELLOW;

            // Gui
            guiElementColor = Color.parseColor("#000000");
            guiElementTextColor = Color.WHITE;
            guiNewHighScoreColor = Color.parseColor("#ff0000");

            // Background
            backgroundGradientLighter = Color.parseColor("#fdee87");
            backgroundGradientDarker = Color.parseColor("#e3af2d");

            // Entities
            ballColor = Color.parseColor("#ff0000");
            barColor = Color.parseColor("#000000");

            // Blocks
            blockRemovedColor = Color.BLACK;
            blockColor1 = Color.parseColor("#000000");
            blockColor2 = Color.parseColor("#000000");
            blockColor3 = Color.parseColor("#000000");
            blockColor4 = Color.parseColor("#000000");

            // Alphas
            alphaAlmostTransparent = 15;

            // Filters
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB){
                filterGui = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.MULTIPLY);
                filterScoreDisplayHitEffect = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.MULTIPLY);
            } else{
                filterGui = null;
                filterScoreDisplayHitEffect = null;
            }

            themeSuccessfullySelected = true;

        }

        if (colorTheme == THEME_BLACK) {

            selectedTheme = "THEME: BLACK";
            selectedThemeConst = THEME_BLACK;

            // Gui
            guiElementColor = Color.parseColor("#ffffff");
            guiElementTextColor = Color.BLACK;
            guiNewHighScoreColor = Color.parseColor("#8dc63f");

            // Background
            backgroundGradientLighter = Color.parseColor("#555555");
            backgroundGradientDarker = Color.parseColor("#000000");

            // Entities
            ballColor = Color.parseColor("#8dc63f");
            barColor = Color.parseColor("#ffffff");

            // Blocks
            blockRemovedColor = Color.WHITE;
            blockColor1 = Color.parseColor("#f78800");
            blockColor2 = Color.parseColor("#f79e31");
            blockColor3 = Color.parseColor("#f7b563");
            blockColor4 = Color.parseColor("#f7cb94");

            // Alphas
            alphaAlmostTransparent = 30;

            // Filters
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB){
                filterGui = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.ADD);
                filterScoreDisplayHitEffect = null;
            } else{
                filterGui = null;
                filterScoreDisplayHitEffect = null;
            }

            themeSuccessfullySelected = true;

        }

        if (colorTheme == THEME_PURPLE) {

            selectedTheme = "THEME: PURPLE";
            selectedThemeConst = THEME_PURPLE;

            // Gui
            guiElementColor = Color.parseColor("#ffffff");
            guiElementTextColor = Color.BLACK;
            guiNewHighScoreColor = Color.parseColor("#ed145b");

            // Background
            backgroundGradientLighter = Color.parseColor("#744787");
            backgroundGradientDarker = Color.parseColor("#341043");

            // Entities
            ballColor = Color.parseColor("#ed145b");
            barColor = Color.parseColor("#ffffff");

            // Blocks
            blockRemovedColor = Color.WHITE;
            blockColor1 = Color.parseColor("#ffdd00");
            blockColor2 = Color.parseColor("#ffe433");
            blockColor3 = Color.parseColor("#ffeb66");
            blockColor4 = Color.parseColor("#fff199");

            // Alphas
            alphaAlmostTransparent = 30;

            // Filters
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB){
                filterGui = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.ADD);
                filterScoreDisplayHitEffect = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.OVERLAY);
            } else{
                filterGui = null;
                filterScoreDisplayHitEffect = null;
            }

            themeSuccessfullySelected = true;

        }

        if (colorTheme == THEME_GREEN) {

            selectedTheme = "THEME: GREEN";
            selectedThemeConst = THEME_GREEN;

            // Gui
            guiElementColor = Color.parseColor("#ffffff");
            guiElementTextColor = Color.BLACK;
            guiNewHighScoreColor = Color.parseColor("#4fff70");

            // Background
            backgroundGradientLighter = Color.parseColor("#6c9832");
            backgroundGradientDarker = Color.parseColor("#36540e");

            // Entities
            ballColor = Color.parseColor("#ffffff");
            barColor = Color.parseColor("#ffcb05");

            // Blocks
            blockRemovedColor = Color.WHITE;
            blockColor1 = Color.parseColor("#4fff70");
            blockColor2 = Color.parseColor("#66ff82");
            blockColor3 = Color.parseColor("#80ff97");
            blockColor4 = Color.parseColor("#99ffac");

            // Alphas
            alphaAlmostTransparent = 30;

            // Filters
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB){
                filterGui = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.ADD);
                filterScoreDisplayHitEffect = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.OVERLAY);
            } else{
                filterGui = null;
                filterScoreDisplayHitEffect = null;
            }

            themeSuccessfullySelected = true;

        }

        if (colorTheme == THEME_WHITE) {

            selectedTheme = "THEME: WHITE";
            selectedThemeConst = THEME_WHITE;

            // Gui
            guiElementColor = Color.parseColor("#cc2b24");
            guiElementTextColor = Color.WHITE;
            guiNewHighScoreColor = Color.parseColor("#000000");

            // Background
            backgroundGradientLighter = Color.parseColor("#ffffff");
            backgroundGradientDarker = Color.parseColor("#cdcdcd");

            // Entities
            ballColor = Color.parseColor("#000000");
            barColor = Color.parseColor("#cc2b24");

            // Blocks
            blockRemovedColor = Color.BLACK;
            blockColor1 = Color.parseColor("#cc2b24");
            blockColor2 = Color.parseColor("#cc2b24");
            blockColor3 = Color.parseColor("#cc2b24");
            blockColor4 = Color.parseColor("#cc2b24");

            // Alphas
            alphaAlmostTransparent = 30;

            // Filters
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB){
                filterGui = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.MULTIPLY);
                filterScoreDisplayHitEffect = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.OVERLAY);
            } else{
                filterGui = null;
                filterScoreDisplayHitEffect = null;
            }

            themeSuccessfullySelected = true;

        }

        return themeSuccessfullySelected;

    }

    public static int getGuiElementColor() {
        return guiElementColor;
    }

    public static int getGuiElementTextColor() {
        return guiElementTextColor;
    }

    public static int getBackgroundGradientLighter() {
        return backgroundGradientLighter;
    }

    public static int getBackgroundGradientDarker() {
        return backgroundGradientDarker;
    }

    public static int getBallColor() {
        return ballColor;
    }

    public static int getBarColor() {
        return barColor;
    }

    public static int getGuiNewHighScoreColor() {
        return guiNewHighScoreColor;
    }

    public static int getBlockRemovedColor() {
        return blockRemovedColor;
    }

    public static int getAlphaAlmostTransparent() {
        return alphaAlmostTransparent;
    }

    public static int getBlockColorByConstant(int colorConstant) {

        if (colorConstant == 1) {
            return blockColor1;
        }
        if (colorConstant == 2) {
            return blockColor2;
        }
        if (colorConstant == 3) {
            return blockColor3;
        }
        if (colorConstant == 4) {
            return blockColor4;
        }

        return blockColor1;
    }

    public static int getRandomBlockColor() {

        int selected = (int) Math.ceil(Math.random() * 4);

        return selected;
    }

    public static ColorFilter getFilterGui() {
        return filterGui;
    }

    public static String getSelectedTheme() {
        return selectedTheme;
    }

    public static int getSelectedThemeConst() {
        return selectedThemeConst;
    }

    public static ColorFilter getFilterScoreDisplayHitEffect() {
        return filterScoreDisplayHitEffect;
    }
}

