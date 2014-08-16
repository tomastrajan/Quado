package com.trajan.android.game.Quado.entities.effects;

/*

   Created with IntelliJ IDEA.

   Ing. Tomáš Herich
   --------------------------- 
   05. 08. 2014
   22:45

*/

import android.graphics.Paint;
import com.trajan.android.game.Quado.entities.BasicEntity;
import com.trajan.android.game.Quado.helpers.Dimensions;

public abstract class AbstractHitEffect extends BasicEntity {

    protected static final int FINISH_STATE = 0;

    protected int left;
    protected int top;
    protected int right;
    protected int bottom;

    protected int state;
    protected Paint paint;

    protected float effectSizeMultiplier;

    protected AbstractHitEffect(Dimensions dimensions) {
        super(dimensions);
    }

    public boolean isFinished() {
        if (state <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public Paint getPaint() {
        return paint;
    }
}
