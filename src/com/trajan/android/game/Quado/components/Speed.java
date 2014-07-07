
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

public class Speed implements Component {

    private static final String TAG = Speed.class.getSimpleName();

    public static final float BASE_SPEED_MULTIPLICATOR = 4.0f;

    public static final int DIRECTION_RIGHT	= 1;
    public static final int DIRECTION_LEFT	= -1;
    public static final int DIRECTION_UP	= -1;
    public static final int DIRECTION_DOWN	= 1;

    public static final float X_MIN_SPEED = 0.05f;
    public static final float X_MAX_SPEED = 1.95f;
    public static final float Y_MIN_SPEED = 0.5f;
    public static final float Y_MAX_SPEED = 1.5f;

    private float xv;
    private float yv;

    private int xDirection;
    private int yDirection;

    private float speedMultiplicator;
    private float displaySizeSpeedMultiplicator = 1;
    private float arcadeSpeedIncrement = 0;

    public Speed(boolean isArcade) {

        if (isArcade) {
            speedMultiplicator = BASE_SPEED_MULTIPLICATOR * displaySizeSpeedMultiplicator;
        } else {
            speedMultiplicator = BASE_SPEED_MULTIPLICATOR * displaySizeSpeedMultiplicator;
        }

        displaySizeSpeedMultiplicator = (DeviceInfo.INSTANCE.getSurfaceHeight() / 800.0f);
        arcadeSpeedIncrement = 0;

        xv = 0.5f;
        yv = (float) (1.3f + (Math.random() * 0.3f));

        if (Math.random() >= 0.5) {
            xDirection = DIRECTION_RIGHT;
        } else {
            xDirection = DIRECTION_LEFT;
        }
        yDirection = DIRECTION_DOWN;

    }

    public float getXv() {
        return xv;
    }
    public void setXv(float xv) {
        this.xv = xv;
    }
    public float getYv() {
        return yv;
    }
    public void setYv(float yv) {
        this.yv = yv;
    }

    public int getxDirection() {
        return xDirection;
    }
    public void setxDirection(int xDirection) {
        this.xDirection = xDirection;
    }
    public int getyDirection() {
        return yDirection;
    }
    public void setyDirection(int yDirection) {
        this.yDirection = yDirection;
    }

    // changes the direction on the X axis
    public void toggleXDirection() {
        xDirection = xDirection * -1;
    }

    // changes the direction on the Y axis
    public void toggleYDirection() {
        yDirection = yDirection * -1;
    }

    public float getSpeedMultiplicator() {

        return speedMultiplicator;

    }

    public void updateSpeedMultiplicator(float remainingToTotalBlockCountRatio) {

        speedMultiplicator = ((remainingToTotalBlockCountRatio * 5) + BASE_SPEED_MULTIPLICATOR) * displaySizeSpeedMultiplicator;

    }

    public void updateSpeedMultiplicatorArcade() {

        arcadeSpeedIncrement = arcadeSpeedIncrement + 0.02f;
        speedMultiplicator = (BASE_SPEED_MULTIPLICATOR + arcadeSpeedIncrement) * displaySizeSpeedMultiplicator;

    }

    public void enforceSpeedLimits() {

        if (xv < X_MIN_SPEED) {
            xv = X_MIN_SPEED;
            yv = X_MAX_SPEED;
        }

        if (yv < Y_MIN_SPEED) {
            yv = Y_MIN_SPEED;
            xv = Y_MAX_SPEED;
        }

    }

}
