package com.trajan.android.game.Quado.entities.gui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.view.MotionEvent;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.entities.BasicEntity;
import com.trajan.android.game.Quado.helpers.Dimensions;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;
import com.trajan.android.game.Quado.helpers.TouchEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-11
 */
public class Button extends BasicEntity implements TouchEventListener {

    public static final int PRIMARY = 10;
    public static final int SECONDARY = 11;
    public static final int BUTTON_1 = 0;
    public static final int BUTTON_2_1 = 1;
    public static final int BUTTON_2_2 = 2;
    public static final int BUTTON_3_1 = 3;
    public static final int BUTTON_3_2 = 4;
    public static final int BUTTON_3_3 = 5;

    private List<ButtonTouchListener> buttonTouchListeners;

    private boolean switcher = false;

    private String buttonText;
    private int selectedPaintType;

    public Button(int buttonWidth, int paintType, int containerWidth, int margin, int topOffset, String buttonText) {
        buttonTouchListeners = new ArrayList<ButtonTouchListener>();
        selectedPaintType = paintType;
        Dimensions dimensions;
        switch (buttonWidth) {
            case BUTTON_1: {
                dimensions = new Dimensions(containerWidth / 2 + margin,
                        topOffset,
                        containerWidth - 2 * margin,
                        containerWidth / 8);
                break;
            }
            case BUTTON_2_1: {
                dimensions = new Dimensions(containerWidth / 4 + margin + margin / 4,
                        topOffset,
                        (containerWidth - 3 * margin) / 2,
                        containerWidth / 8);
                break;
            }
            case BUTTON_2_2: {
                dimensions = new Dimensions(containerWidth * 3 / 4 + margin - margin / 4,
                        topOffset,
                        (containerWidth - 3 * margin) / 2,
                        containerWidth / 8);
                break;
            }
            case BUTTON_3_1: {
                dimensions = new Dimensions(containerWidth / 6 + margin + margin / 3,
                        topOffset,
                        (containerWidth - 4 * margin) / 3,
                        containerWidth / 8);
                break;
            }
            case BUTTON_3_2: {
                dimensions = new Dimensions(containerWidth * 3 / 6 + margin,
                        topOffset,
                        (containerWidth - 4 * margin) / 3,
                        containerWidth / 8);
                break;
            }
            case BUTTON_3_3: {
                dimensions = new Dimensions(containerWidth * 5 / 6 + margin - margin / 3,
                        topOffset,
                        (containerWidth - 4 * margin) / 3,
                        containerWidth / 8);
                break;
            }
            default: {
                dimensions = new Dimensions(containerWidth / 2 + margin, topOffset, containerWidth, containerWidth / 8);
                break;
            }
        }
        setDimensions(dimensions);
        this.buttonText = buttonText;
    }

    public void addButtonTouchListener(ButtonTouchListener buttonTouchListener) {
        buttonTouchListeners.add(buttonTouchListener);
    }

    public void removeButtonTouchListener(List<ButtonTouchListener> buttonTouchListener) {
        buttonTouchListeners.remove(buttonTouchListener);
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getSelectedPaintType() {
        return selectedPaintType;
    }

    public void setSelectedPaintType(int selectedPaintType) {
        this.selectedPaintType = selectedPaintType;
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {
        switch (selectedPaintType) {
            case PRIMARY: {

                canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);
                paint.setColor(MyColors.getGuiElementColor());
                canvas.drawRect(getRect(), paint);

                paint.setColor(MyColors.getBackgroundGradientLighter());
                if (switcher) {
                    paint.setStrokeWidth(4f);
                    paint.setStyle(Paint.Style.STROKE);
                    renderArrows(canvas);
                }


                paint.setStrokeWidth(2f);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(height * 0.7f);
                int textHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
                paint.setTextSize(textHeight);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setFakeBoldText(true);
                canvas.drawText(buttonText, x, y + (float) textHeight / 2.5f, paint);
                break;
            }
            case SECONDARY: {

                canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);
                paint.setColor(MyColors.getGuiElementColor());
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2f);
                canvas.drawRect(getRect(), paint);

                if (switcher) {
                    paint.setColor(MyColors.getGuiElementColor());
                    paint.setStrokeWidth(4f);
                    paint.setStyle(Paint.Style.STROKE);
                    renderArrows(canvas);
                    paint.setStrokeWidth(2f);
                    paint.setStyle(Paint.Style.FILL);
                }

                paint.setColor(MyColors.getGuiElementColor());
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(height * 0.7f);
                int textHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
                paint.setTextSize(textHeight);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setFakeBoldText(true);
                canvas.drawText(buttonText, x, y + (float) textHeight / 2.5f, paint);
                break;
            }
        }
    }

    private void renderArrows(Canvas canvas) {
        int step = height / 4;
        canvas.drawLine(x - width / 2 + step * 2, y - height / 2 + step, x - width / 2 + step, y, paint);
        canvas.drawLine(x - width / 2 + step, y, x - width / 2 + step * 2, y + height / 2 - step, paint);
        canvas.drawLine(x + width / 2 - step * 2, y - height / 2 + step, x + width / 2 - step, y, paint);
        canvas.drawLine(x + width / 2 - step, y, x + width / 2 - step * 2, y + height / 2 - step, paint);
    }

    @Override
    public void handleTouchEvent(final MainGamePanel game, MotionEvent event) {

        float eventX = event.getX();
        float eventY = event.getY();

        if (eventX >= x - width / 2 && eventX <= x + width / 2) {
            if (eventY >= y - height / 2 && eventY <= y + height / 2) {

                for (ButtonTouchListener listener : buttonTouchListeners) {
                    listener.excute(game, game.getSounds(), game.getGameState());
                }

            }
        }
    }

    public boolean isSwitcher() {
        return switcher;
    }

    public void setSwitcher(boolean switcher) {
        this.switcher = switcher;
    }
}
