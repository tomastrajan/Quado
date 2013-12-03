
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

package com.trajan.android.game.Quado.entities.effects;

import android.graphics.*;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.entities.BasicEntity;
import com.trajan.android.game.Quado.helpers.Dimensions;

public class BlockHitEffect extends BasicEntity implements HitEffect {

    private static final String TAG = BlockHitEffect.class.getSimpleName();

    private static final int START_STATE = 20;
    private static final int FINISH_STATE = 0;

    private int left;
    private int top;
    private int right;
    private int bottom;

    private int state;
    private float effectSizeMultiplier;
    private Paint paint;

    public BlockHitEffect(int x, int y, int width, int height, Paint paint) {
        super(new Dimensions(x, y, width, height));

        this.left = x - width / 2;
        this.top = y - height / 2;
        this.right = x + width / 2;
        this.bottom = y + height / 2;

        state = START_STATE;
        effectSizeMultiplier = (float) (((height * 2.5) / START_STATE) * ((Math.random() * 0.75) + 0.25));
        this.paint = paint;
        zIndex = -1;
    }

    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {

        setzIndex(4);

//        ColorFilter filter = new PorterDuffColorFilter(MyColors.getBackgroundGradientLighter(), PorterDuff.Mode.ADD);
//        paint.setColorFilter(filter);

        int alpha = (int) Math.ceil(state * (125 / (float) START_STATE));
        paint.setAlpha(alpha);

        int diff = (int) ((START_STATE - state) * effectSizeMultiplier);

        canvas.clipRect(new Rect(left - diff, top - diff, right + diff, bottom + diff), Region.Op.REPLACE);
        canvas.drawRect(left - diff, top - diff, right + diff, bottom + diff, paint);

        if (state > FINISH_STATE) {
            state--;
        }

    }

    public boolean isFinished() {
        if (state <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
