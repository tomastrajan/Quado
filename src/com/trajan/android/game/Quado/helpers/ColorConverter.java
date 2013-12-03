
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

import android.graphics.Paint;

public class ColorConverter {

    public static int getRed(Paint sourcePaint) {
        int color = sourcePaint.getColor();
        return (color >> 16) & 0xFF;
    }

    public static int getGreen(Paint sourcePaint) {
        int color = sourcePaint.getColor();
        return  (color >> 8) & 0xFF;
    }

    public static int getBlue(Paint sourcePaint) {
        int color = sourcePaint.getColor();
        return  (color >> 0) & 0xFF;
    }

}
