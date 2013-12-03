
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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class TextSizeCalculator {

    public static int getTextSizeFromHeight(Dimensions dimensions) {

        int textSize = 0;

        Paint paint = new Paint();

        for (int i = 1; i <= 1000; i++) {

            paint.setTextSize(i);
            Rect testBounds = new Rect();
            String test = "test";
            paint.getTextBounds(test, 0, test.length(), testBounds);


            if (testBounds.height() > dimensions.getHeight()) {
                textSize = i;
                break;
            }


        }

        return textSize;

    }

    public static int getTextSizeFromWidth(Dimensions dimensions, String message) {

        int textSize = 0;

        Paint paint = new Paint();

        for (int i = 1; i <= 1000; i++) {

            paint.setTextSize(i);
            Rect testBounds = new Rect();
            paint.getTextBounds(message, 0, message.length(), testBounds);


            if (testBounds.width() > dimensions.getWidth() * 0.9) {
                textSize = i;
                break;
            }


        }

        return textSize;

    }

    public static int getDefaultTextSize(Canvas canvas) {

        int textSize = 0;

        Paint paint = new Paint();
        for (int i = 1; i <= 1000; i++) {

            paint.setTextSize(i);
            Rect testBounds = new Rect();
            paint.getTextBounds("012345678901234", 0, 14, testBounds);

            if (testBounds.width() > canvas.getWidth() * 0.85) {
                textSize = i;
                break;
            }
        }

        return textSize;
    }

    public static int getHeightFromTextSize(float textSize) {

        Paint paint = new Paint();

        paint.setTextSize(textSize);
        Rect testBounds = new Rect();
        String test = "test";
        paint.getTextBounds(test, 0, test.length(), testBounds);

        return testBounds.height();
    }

    public static int getWidthFromTextSize(float textSize, String message) {

        Paint paint = new Paint();

        paint.setTextSize(textSize);
        Rect testBounds = new Rect();
        paint.getTextBounds(message, 0, message.length(), testBounds);

        return testBounds.width();
    }

}
