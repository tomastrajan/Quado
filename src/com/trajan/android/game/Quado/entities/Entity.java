
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

import android.graphics.Canvas;
import android.graphics.Rect;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.helpers.Dimensions;

public interface Entity {

    public void setDimensions(Dimensions dimensions);

    public int getX();
    public void setX(int x);
    public int getY();
    public void setY(int y);
    public int getWidth();
    public void setWidth(int width);
    public int getHeight();
    public void setHeight(int height);
    public boolean isTouched();
    public void setTouched(boolean touched);
    public int getzIndex();
    public void setzIndex(int zIndex);

    public Rect getRect();

    public void render(MainGamePanel game, Canvas canvas, GameState gameState);

}
