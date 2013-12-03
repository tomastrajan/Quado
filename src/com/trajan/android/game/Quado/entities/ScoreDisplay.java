
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
import android.graphics.Region;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.Score;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;

public class ScoreDisplay extends BasicEntity {

    private static final String TAG = ScoreDisplay.class.getSimpleName();

    private Score score;
    private int textSize;
    private Dimensions squareDimensions;

    public ScoreDisplay(Dimensions dimensions) {
        super(dimensions);
        squareDimensions = new Dimensions(0,0,0,0);
        textSize = TextSizeCalculator.getTextSizeFromHeight(dimensions);
    }

    @Override
    public void render(MainGamePanel game,Canvas canvas, GameState gameState) {

        if (gameState.isStateGame() || gameState.isStateArcade()) {

            setzIndex(2);

            canvas.clipRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), Region.Op.REPLACE);

            paint.setColor(MyColors.getGuiElementColor());
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setAntiAlias(true);
            paint.setTextSize(textSize);
            paint.setStrokeWidth(2f);
            paint.setColorFilter(MyColors.getFilterGui());

            String scoreValue = Integer.toString(score.getScore());
            String plus = "+";

            int heightRatio = (int) (height * 0.65);
            int scoreWidth = TextSizeCalculator.getWidthFromTextSize(paint.getTextSize(), scoreValue);
            int plusWidth = TextSizeCalculator.getWidthFromTextSize(paint.getTextSize(), plus);
            int textWidth = scoreWidth + plusWidth + (heightRatio * 2);
            int fullWidth = textWidth + (heightRatio * 2);

            int top = y - heightRatio;
            int bottom = y + heightRatio;
            int left = x - (fullWidth / 2) + scoreWidth + heightRatio * 3;
            int right = left + heightRatio * 2;

            canvas.drawText(scoreValue, x - fullWidth / 2, y + height / 2, paint);
            canvas.drawText(plus, x - (fullWidth / 2) + scoreWidth + heightRatio, y + height / 2, paint);

            canvas.drawLine(left, top, right, top, paint);
            canvas.drawLine(right, top, right, bottom, paint);
            canvas.drawLine(right, bottom, left, bottom, paint);
            canvas.drawLine(left, bottom, left, top, paint);

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setTextSize((float) Math.ceil(textSize * 0.6));
            String scoreMultiplicator = Integer.toString(score.getScoreMultiplicator());
            int scoreMultiplicatorTextHeight = TextSizeCalculator.getHeightFromTextSize((int) paint.getTextSize());
            canvas.drawText(scoreMultiplicator, left + heightRatio, (float) (y + scoreMultiplicatorTextHeight / 2), paint);

            squareDimensions.setX(left + (right - left) / 2);
            squareDimensions.setY(top + (bottom - top) / 2);
            squareDimensions.setHeight(right - left);
            squareDimensions.setWidth(bottom - top);

        }

    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Dimensions getSquareDimensions() {
        return squareDimensions;
    }

    public Paint getPaint() {
        Paint paint = new Paint();
        paint.setColor(this.paint.getColor());
        return paint;
    }
}