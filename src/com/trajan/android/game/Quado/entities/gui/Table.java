package com.trajan.android.game.Quado.entities.gui;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.entities.BasicEntity;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.helpers.TextSizeCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-12
 */
public class Table extends BasicEntity {

    private int containerWidth;
    private int margin;
    private int topOffset;
    private int rowHeight;
    private int containerLeft;
    private int containerRight;
    private List<String> headers;
    private List<List<String>> rows;
    private List<Integer> highlightedRows;

    public Table(int containerWidth, int margin, int topOffset, List<String> headers, List<List<String>> rows) {
        this.containerWidth = containerWidth;
        this.margin = margin;
        this.topOffset = topOffset;
        this.headers = headers;
        this.rows = rows;

        rowHeight = (int) (DeviceInfo.INSTANCE.getSurfaceHeight() * 0.04f);
        containerLeft = margin;
        containerRight = margin + containerWidth;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    public void addHighlightedRow(int row) {
        if (highlightedRows == null) {
            highlightedRows = new ArrayList<>();
        }
        highlightedRows.add(row);
    }

    public void resetHighlightedRows() {
        highlightedRows = null;
    }

    @Override
    public void render(MainGamePanel game, Canvas canvas, GameState gameState) {
        renderHeaders(canvas);
        renderContent(canvas);
    }

    private void renderHeaders(Canvas canvas) {
        paint.setTextSize(rowHeight * 1.5f * 0.7f);
        int textHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
        paint.setTextSize(textHeight);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);

        paint.setAlpha(MyColors.getAlpha());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(containerLeft, topOffset, containerRight, topOffset + (int) (rowHeight * 1.5), paint);

        paint.setColor(MyColors.getGuiElementColor());
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        canvas.drawLine(containerLeft, topOffset, containerRight, topOffset, paint);
        canvas.drawLine(containerLeft, topOffset + (int) (rowHeight * 1.5), containerRight, topOffset + (int) (rowHeight * 1.5), paint);

        int columnWidth = containerWidth / headers.size();
        for (int i = 1; i <= headers.size(); i++) {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(containerLeft + columnWidth * i, topOffset, containerLeft + columnWidth * i, topOffset + (int) (rowHeight * 1.5), paint);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(headers.get(i - 1), (containerLeft + columnWidth * i) - columnWidth / 2, topOffset + (int) (rowHeight * 1.5) / 2 + (float) textHeight / 2.5f, paint);
        }
    }

    private void renderContent(Canvas canvas) {
        for (int i = 0; i < rows.size(); i++) {
            drawRow(canvas, i, rows.get(i));
        }
        if (rows.size() == 0) {
            List<String> noRecordsFound = new ArrayList<>();
            noRecordsFound.add("Nothing found");
            drawRow(canvas, 0, noRecordsFound);
        }
    }

    private void drawRow(Canvas canvas, int rowNumber, List<String> rowData) {
        paint.setTextSize(rowHeight * 0.7f);
        int textHeight = TextSizeCalculator.getHeightFromTextSize(paint.getTextSize());
        paint.setTextSize(textHeight);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(false);

        paint.setColor(MyColors.getGuiElementColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        int headerOffset = (int) (rowHeight * 1.5);
        int rowOffset = rowNumber * rowHeight;
        int offset = headerOffset + rowOffset;

        if (highlightedRows != null && highlightedRows.contains(rowNumber)) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(MyColors.getGuiNewHighScoreColor());
            canvas.drawRect(containerLeft, topOffset + offset, containerRight, topOffset + rowHeight + offset, paint);
        }
        paint.setColor(MyColors.getGuiElementColor());
        canvas.drawLine(containerLeft, topOffset + offset, containerRight, topOffset + offset, paint);
        canvas.drawLine(containerLeft, topOffset + rowHeight + offset, containerRight, topOffset + rowHeight + offset, paint);

        int columnWidth = containerWidth / rowData.size();
        for (int i = 1; i <= rowData.size(); i++) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setFakeBoldText(false);
            paint.setColor(MyColors.getGuiElementColor());
            canvas.drawLine(containerLeft + columnWidth * i, topOffset + offset, containerLeft + columnWidth * i, topOffset + rowHeight + offset, paint);

            paint.setStyle(Paint.Style.FILL);
            if (highlightedRows != null && highlightedRows.contains(rowNumber)) {
                paint.setColor(MyColors.getGuiElementTextColor());
                paint.setFakeBoldText(true);
            }
            canvas.drawText(rowData.get(i - 1), (containerLeft + columnWidth * i) - columnWidth / 2, topOffset + offset + rowHeight / 2 + (float) textHeight / 2.5f, paint);
        }
    }

}
