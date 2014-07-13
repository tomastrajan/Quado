package com.trajan.android.game.Quado.entities.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.entities.BasicEntity;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;
import com.trajan.android.game.Quado.helpers.TouchEventListener;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-12
 */
public abstract class Screen extends BasicEntity implements TouchEventListener {

    protected String message;

    protected final int dCanvasHeight;
    protected final int dCanvasWidth;
    protected final int dContainerWidth;
    protected final int dMargin;
    protected final int dButtonHeight;


    protected Screen(Dimensions dimensions, Context context,  int messageId) {
        super(dimensions);
        this.message = context.getResources().getString(messageId);
        dCanvasHeight = DeviceInfo.INSTANCE.getSurfaceHeight();
        dCanvasWidth = DeviceInfo.INSTANCE.getSurfaceWidth();
        dContainerWidth = dCanvasWidth - 2 * dCanvasWidth / 15;
        dMargin = dCanvasWidth / 15;
        dButtonHeight = dContainerWidth / 8;
    }

    protected  void prepareDefautlPaint(Canvas canvas) {
        paint.setTextSize(TextSizeCalculator.getDefaultTextSize(canvas));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setSubpixelText(true);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2f);
        paint.setColorFilter(MyColors.getFilterGui());
        paint.setColor(MyColors.getGuiElementColor());
    }

    protected void renderBorder(Canvas canvas) {
        int border = canvas.getWidth() / 15;
        canvas.drawLine(border, border, canvas.getWidth() - border, border, paint);
        canvas.drawLine(border, border, border, canvas.getHeight() - border, paint);
        canvas.drawLine(border, canvas.getHeight() - border, canvas.getWidth() - border, canvas.getHeight() - border, paint);
        canvas.drawLine(canvas.getWidth() - border, border, canvas.getWidth() - border, canvas.getHeight() - border, paint);
    }

    protected void renderTitle(Canvas canvas) {
        int border = canvas.getWidth() / 15;
        int headingHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
        canvas.drawText(message, canvas.getWidth() / 2, border * 2.5f + headingHeight * 0.5f, paint);
    }

}
