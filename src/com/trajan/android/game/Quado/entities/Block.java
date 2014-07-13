
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

    private int colorConstant;
    private Timer timer;
    private Rect blockRectangle;

    public Block(Dimensions dimensions) {
        super(dimensions);
    }

    public void initializeBlock() {
        colorConstant = MyColors.getRandomBlockColor();
        blockRectangle = new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        paint.setColor(MyColors.getBlockColorByConstant(colorConstant));

        if (gameState.isStateMenu() || gameState.isStateGame() || gameState.isStateArcade()) {
            paint.setAlpha(255);

            if (hitPoints == 0) {
                paint.setColor(MyColors.getBlockRemovedColor());
                paint.setAlpha(MyColors.getAlpha());
            }
        } else {
            paint.setAlpha(MyColors.getAlpha());
        }

        canvas.clipRect(blockRectangle, Region.Op.REPLACE);
        canvas.drawRect(blockRectangle, paint);


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

        if (isHittable) {

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

        } else {
            return null;
        }
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
