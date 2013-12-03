
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

package com.trajan.android.game.Quado.components;

import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.entities.Ball;
import com.trajan.android.game.Quado.entities.Bar;
import com.trajan.android.game.Quado.entities.Block;
import com.trajan.android.game.Quado.entities.Entity;
import com.trajan.android.game.Quado.entities.effects.HitEffect;
import com.trajan.android.game.Quado.helpers.MyColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RenderHelper implements Component {

    private static final String TAG = RenderHelper.class.getSimpleName();

    private List<Entity> entities;
    private List<Integer> zIndexValues;

    private List<Rect> parallaxBackground;
    private List<Rect> parallaxForeground;

    private Paint paint;
    private Bitmap gradientBitmap;

    public RenderHelper() {

        entities = new ArrayList<Entity>();
        zIndexValues = new ArrayList<Integer>();
        parallaxBackground = new ArrayList<Rect>();
        parallaxForeground = new ArrayList<Rect>();
        paint = new Paint();

        generateParallaxRectangles();

    }

    private void generateParallaxRectangles() {

        for (int i = 0; i < 8; i++) {

            boolean isolatedPosition = true;

            int width = DeviceInfo.INSTANCE.getSurfaceWidth();
            int height = DeviceInfo.INSTANCE.getSurfaceHeight();

            int x = (int) ((Math.random() * (width + (width / 4))) - (width / 8));
            int y = (int) ((Math.random() * (height + (height / 8))) - (height / 16));

            int size = (int) ((width / 3) + Math.random() * (width / 6));

            Rect rect = new Rect(x, y, x + size, y + size);

            if (!parallaxBackground.isEmpty()) {

                for (Rect storedRect : parallaxBackground) {

                    if (rect.intersect(storedRect)) {
                        i--;
                        isolatedPosition = false;
                    }
                }
            } else {
                parallaxBackground.add(rect);
                isolatedPosition = false;
            }

            if (isolatedPosition) {
                parallaxBackground.add(rect);
            }

        }

        for (int i = 0; i < 16; i++) {

            boolean isolatedPosition = true;

            int width = DeviceInfo.INSTANCE.getSurfaceWidth();
            int height = DeviceInfo.INSTANCE.getSurfaceHeight();

            int x = (int) ((Math.random() * (width + (width / 4))) - (width / 8));
            int y = (int) ((Math.random() * (height + (height / 8))) - (height / 16));

            int size = (int) ((width / 8) + Math.random() * (width / 16));

            Rect rect = new Rect(x, y, x + size, y + size);

            if (!parallaxForeground.isEmpty()) {

                for (Rect storedRect : parallaxForeground) {

                    if (rect.intersect(storedRect)) {
                        i--;
                        isolatedPosition = false;
                    }
                }
            } else {
                parallaxForeground.add(rect);
                isolatedPosition = false;
            }

            if (isolatedPosition) {
                parallaxForeground.add(rect);
            }

        }

    }

    public void addGameEntities(Elements elements) {

        entities = elements.getAllEntities();

        for (Block block : elements.getLevel().getBlocks()) {
            entities.add(block);
        }

    }

    public void addHitEffect(Entity hitEffect) {
        entities.add(hitEffect);
    }

    public void render(MainGamePanel game, Canvas canvas) {

        // Clear screen to black
        initializeCanvas(game, canvas);

        // Prepare unique zIndex values
        zIndexValues.clear();

        for (Entity entity : entities) {
            if (entity != null && !zIndexValues.contains(entity.getzIndex())) {
                zIndexValues.add(entity.getzIndex());
            }
        }


        // Sort unique zIndex values for layered rendering
        Collections.sort(zIndexValues);

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);

        // Render game entities in order based on zIndex value
        for (Integer zIndex : zIndexValues) {

            Iterator entitiesIterator = entities.iterator();
            while (entitiesIterator.hasNext()) {

                Entity entity = (Entity) entitiesIterator.next();

                if (entity != null && entity.getzIndex() == zIndex) {

                    entity.render(game, canvas, gameState);

                    if (entity instanceof HitEffect) {

                        HitEffect hitEffect = (HitEffect) entity;

                        if (!hitEffect.isFinished()) {
                            entity.render(game, canvas, gameState);
                        } else {
                            entitiesIterator.remove();
                        }
                    }

                }
            }
        }

    }

    public void createGradientBitmap() {

        int width = DeviceInfo.INSTANCE.getSurfaceWidth();

        int[] colors = {MyColors.getBackgroundGradientLighter(), MyColors.getBackgroundGradientDarker()};
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradient.setGradientRadius(width * 0.75f);
        gradient.setDither(true);
        gradient.setGradientCenter(0.5f, 0.5f);
        gradient.setBounds(0, 0, (int) (width * 1.5f), (int) (width * 1.5f));

        gradientBitmap = Bitmap.createBitmap((int) (width * 1.5), (int) (width * 1.5), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(gradientBitmap);
        gradient.draw(c);

    }

    private void initializeCanvas(MainGamePanel game, Canvas canvas) {

        canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.REPLACE);
        canvas.drawColor(MyColors.getBackgroundGradientDarker());

        Ball ball = (Ball) game.getElements().getEntity(Elements.BALL);
        Bar bar = (Bar) game.getElements().getEntity(Elements.BAR);

        float xPos = (float) ball.getX() - canvas.getWidth() * 0.75f;
        float yPos = (float) ball.getY() - canvas.getWidth() * 0.75f;

        if (gradientBitmap == null) {
            createGradientBitmap();
        }

        canvas.drawBitmap(gradientBitmap, xPos, yPos, new Paint());

        paint.setColor(MyColors.getBackgroundGradientDarker());
        paint.setAlpha(80);

        float barRatio = (((float) bar.getX() / canvas .getWidth()) - 0.5f) * 2 * (-1);
        float ballRatio = (((float) ball.getY() / canvas .getHeight()) - 0.5f) * 2 * (-1);

        int i = 1;
        for (Rect rect : parallaxBackground) {

            float screenMultiplier = ((float) canvas.getWidth() / 18);
            float extraDiff = 1 + ((float) i / parallaxForeground.size()) ;
            float xDiff =  (screenMultiplier * barRatio * extraDiff);
            float yDiff =  (screenMultiplier * ballRatio * extraDiff);

            canvas.drawRect(rect.left + xDiff, rect.top + yDiff, rect.right + xDiff, rect.bottom + yDiff, paint);

            i++;
        }

        i = 1;
        for (Rect rect : parallaxForeground) {

            float screenMultiplier = ((float) canvas.getWidth() / 6);
            float extraDiff = 1 + ((float) i / parallaxForeground.size()) ;
            float xDiff =  (screenMultiplier * barRatio * extraDiff);
            float yDiff =  (screenMultiplier * ballRatio * extraDiff);

            canvas.drawRect(rect.left + xDiff, rect.top + yDiff, rect.right + xDiff, rect.bottom + yDiff, paint);

            i++;
        }
    }

}
