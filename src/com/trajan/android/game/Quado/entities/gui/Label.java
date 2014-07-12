package com.trajan.android.game.Quado.entities.gui;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.entities.BasicEntity;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-12
 */
public class Label extends BasicEntity {

    private int containerWidth;
    private int margin;
    private int topOffset;
    private String value;
    private Float textSize;

    public Label(int containerWidth, int margin, int topOffset, String value) {
        this.containerWidth = containerWidth;
        this.margin = margin;
        this.topOffset = topOffset;
        this.value = value;

        // Defaults
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(MyColors.getGuiElementColor());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAlign(Paint.Align align) {
        paint.setTextAlign(align);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {
        paint.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas) * 0.5f);
        if (textSize != null) {
            paint.setTextSize(textSize);
        }
        canvas.drawText(value, margin + containerWidth / 2, topOffset, paint);
    }
}
