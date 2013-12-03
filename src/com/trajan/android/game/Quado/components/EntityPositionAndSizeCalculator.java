
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

package com.trajan.android.game.Quado.components;

import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.levels.LevelMap;

public class EntityPositionAndSizeCalculator implements Component {

    private static final String TAG = EntityPositionAndSizeCalculator.class.getSimpleName();

    private static final double HALF = 0.5;
    private static final double RATIO_80 = 0.8;
    private static final double RATIO_20 = 0.2;
    private static final double BALL_STARTING_HEIGHT_RATIO = 0.7;
    private static final double BAR_STARTING_HEIGHT_RATIO = 0.88;
    private static final double BAR_WIDTH_TO_HEIGHT_RATIO = 6;
    private static final double BUTTON_SIZE_RATIO = 0.07;
    private static final double BUTTON_POSITION_RATIO = 0.035;

    private int surfaceWidth;
    private int surfaceHeight;
    private LevelMap level;

    private int blockSize;
    private int blockSpacing;
    private int topOffset;


    public EntityPositionAndSizeCalculator(LevelMap level) {
        this.surfaceWidth = DeviceInfo.INSTANCE.getSurfaceWidth();
        this.surfaceHeight = DeviceInfo.INSTANCE.getSurfaceHeight();
        this.level = level;

        createSmallObjectSize();
    }

    private void createSmallObjectSize() {

        float usableHeight = (float) (surfaceHeight * HALF);

        if (level.getRows() >= level.getCols()) {
            int usableRowHeight = (int) (usableHeight / level.getRows());
            blockSize = (int) (usableRowHeight * RATIO_80);
            blockSpacing = (int) (usableRowHeight * RATIO_20);
        }

        if (level.getRows() < level.getCols()) {
            int usableColWidth = (int) ((surfaceWidth / level.getCols()) * 0.9);
            blockSize = (int) (usableColWidth * RATIO_80);
            blockSpacing = (int) (usableColWidth * RATIO_20);
        }

        // Based on top button offset
        topOffset = (int) ((surfaceWidth * BUTTON_SIZE_RATIO) * 3);

    }

    public Dimensions getBall() {
        int x;
        int y;
        int width;
        int height;

        height = blockSize;
        width = height;

        x = (int) (surfaceWidth * HALF);
        y = (int) (surfaceHeight * BALL_STARTING_HEIGHT_RATIO);

        return new Dimensions(x, y, width, height);
    }

    public Dimensions getBlock() {
        int x;
        int y;
        int width;
        int height;

        height = blockSize;
        width = height;

        x = 0;
        y = 0;

        return new Dimensions(x, y, width, height);
    }

    public Dimensions getBar() {
        int x;
        int y;
        int width;
        int height;

        height = blockSize;
        width = (int) (height * BAR_WIDTH_TO_HEIGHT_RATIO);

        x = (int) (surfaceWidth * HALF);
        y = (int) (surfaceHeight * BAR_STARTING_HEIGHT_RATIO);

        return new Dimensions(x, y, width, height);
    }

    public Dimensions getCloseButton() {
        int x;
        int y;
        int width;
        int height;

        int offsetHelper = (int) (surfaceWidth * BUTTON_POSITION_RATIO);

        width = (int) (surfaceWidth * BUTTON_SIZE_RATIO);
        height = width;

        x = (int) (surfaceWidth - offsetHelper - (width * HALF));
        y = (int) (offsetHelper + (height * HALF));

        return new Dimensions(x, y, width, height);
    }

    public Dimensions getRetryButton() {
        int x;
        int y;
        int width;
        int height;

        int offsetHelper = (int) (surfaceWidth * BUTTON_POSITION_RATIO);

        width = (int) (surfaceWidth * BUTTON_SIZE_RATIO);
        height = width;

        x = (int) (offsetHelper + (width * HALF));
        y = (int) (offsetHelper + (height * HALF));

        return new Dimensions(x, y, width, height);
    }

    public Dimensions getScoreDisplay() {
        int x;
        int y;
        int width;
        int height;

        int offsetHelper = (int) (surfaceWidth * BUTTON_POSITION_RATIO);

        width = (int) (surfaceWidth * BUTTON_SIZE_RATIO);
        height = width;

        x = (int) (surfaceWidth * HALF);
        y = (int) (offsetHelper + (height * HALF * 1.05));

        return new Dimensions(x, y, width, height);
    }

    public Dimensions getFullCenterMessage() {
        int x;
        int y;
        int width;
        int height;

        width = (int) (surfaceWidth * RATIO_80);
        height = width;

        x = (int) (surfaceWidth * HALF);
        y = (int) (surfaceHeight * HALF);

        return new Dimensions(x, y, width, height);
    }


    public int getBlockSpacing() {
        return blockSpacing;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getTopOffset() {
        return topOffset;
    }

}
