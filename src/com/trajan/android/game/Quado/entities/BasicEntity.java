
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
import android.graphics.Paint;
import android.graphics.Rect;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.helpers.Dimensions;

public abstract class BasicEntity implements Entity {

    protected int x;   // the X coordinate
    protected int y;   // the Y coordinate

    protected int width;
    protected int height;

    protected int zIndex = 0;

    protected boolean touched;

    protected Paint paint;

    protected BasicEntity() {
        this.paint = new Paint();
    }

    protected BasicEntity(Dimensions dimensions) {
        this.paint = new Paint();
        this.x = dimensions.getX();
        this.y = dimensions.getY();
        this.width = dimensions.getWidth();
        this.height = dimensions.getHeight();
    }

    @Override
    public void setDimensions(Dimensions dimensions) {
        this.x = dimensions.getX();
        this.y = dimensions.getY();
        this.width = dimensions.getWidth();
        this.height = dimensions.getHeight();
    }

    @Override
    public Rect getRect() {
        return new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    @Override
    public abstract void render(MainGamePanel game, Canvas canvas, GameState gameState);


}
