
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

import android.graphics.*;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.BlockCounter;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.entities.effects.BlockHitEffect;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;

import java.util.Timer;
import java.util.TimerTask;

public class Block extends BasicEntity {

    private static final String TAG = Block.class.getSimpleName();

    private int hitPoints = 1;
    private boolean isHittable = true;
    private int recentlyHit = 0;
    private boolean indestructable;

    private int colorConstant;
    private Timer timer;
    private Rect blockRectangle;
    private Rect blockRectangleIndestructible;

    private int top;
    private int right;
    private int bottom;
    private int left;


    public Block(Dimensions dimensions, boolean indestructable) {
        super(dimensions);
        this.indestructable = indestructable;
    }

    public void initializeBlock() {
        colorConstant = MyColors.getRandomBlockColor();
        blockRectangle = new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2);

        this.left = x - width / 2 + 1;
        this.top = y - height / 2 + 1;
        this.right = x + width / 2 - 1;
        this.bottom = y + height / 2 - 1;

        blockRectangleIndestructible = new Rect(left, top, right, bottom);
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        paint.setColor(MyColors.getBlockColorByConstant(colorConstant));

        if (gameState.isStateMenu() || gameState.isStateNormal() || gameState.isStateArcade()) {
            paint.setAlpha(255);

            if (hitPoints == 0) {
                paint.setColor(MyColors.getBlockRemovedColor());
                paint.setAlpha(MyColors.getAlpha());
            }
        } else {
            paint.setAlpha(MyColors.getAlpha());
        }

        if (indestructable) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2f);
            paint.setStrokeJoin(Paint.Join.MITER);
            canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);
            canvas.drawRect(blockRectangleIndestructible, paint);
            canvas.drawLine(left, top, right, bottom, paint);
            canvas.drawLine(left, bottom, right, top, paint);
        } else {
            paint.setStyle(Paint.Style.FILL);
            canvas.clipRect(blockRectangle, Region.Op.REPLACE);
            canvas.drawRect(blockRectangle, paint);
        }


        if (!isHittable && recentlyHit > 0) {
            recentlyHit--;
        } else {
            isHittable = true;
        }

    }

    public int getHitPoints() {
        return hitPoints;
    }

    public Entity decrementHitPoints(GameState gameState, BlockCounter blockCounter) {

        if (isHittable && !indestructable) {

            hitPoints--;

            if (hitPoints == 0) {
                zIndex = -2;
            }

            recentlyHit = 5;
            isHittable = false;

            if (gameState.isStateMenu() && hitPoints == 0) {
                refreshBlock(15, blockCounter);
            }

            if (gameState.isStateArcade() && hitPoints == 0) {
                refreshBlock(15, blockCounter);
            }

            Paint hitEffectPaint = new Paint();
            hitEffectPaint.setColor(MyColors.getBlockColorByConstant(colorConstant));
            return new BlockHitEffect(x, y, width, height, hitEffectPaint);
        }

        if (indestructable) {
            Paint hitEffectPaint = new Paint();
            hitEffectPaint.setColor(MyColors.getBlockColorByConstant(colorConstant));
            return new BlockHitEffect(x, y, width, height, hitEffectPaint);
        }

        return null;
    }

    private void refreshBlock(int delaySeconds, final BlockCounter blockCounter) {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isHittable = true;
                zIndex = 0;
                hitPoints = 1;
                blockCounter.incrementRemainingBlockCount();
            }
        }, delaySeconds * 1000);

    }

}
