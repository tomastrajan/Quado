
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

package com.trajan.android.game.Quado;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Timer;
import java.util.TimerTask;

public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    private final static int MAX_FPS = 40;                    // desired fps
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;   // the frame period

    // Original thread template attributes
    private Canvas canvas;
    private long beginTime;       // the time when the cycle begun
    private long timeDiff;        // the time it took for the cycle to execute
    private int sleepTime;        // ms to sleep (<0 if we're behind)

    // My stats
    private long statsStartTime = 0;
    private int statsCallPerSecond = 0;
    private int statsUpdatesPerSecond = 0;

    private boolean running;
    private SurfaceHolder surfaceHolder;
    private MainGamePanel game;


    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel game) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    @Override
    public void run() {

        while (running) {

            statsCallPerSecond++;

            canvas = null;

            // try locking the canvas for exclusive pixel editing on the surface
            try {

                beginTime = System.currentTimeMillis();

                if (statsStartTime == 0) {
                    statsStartTime = beginTime;
                }

                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {

                    if (canvas != null) {
                        game.update();
                        game.render(canvas);

                        statsUpdatesPerSecond++;
                    }

                    timeDiff = System.currentTimeMillis() - beginTime;

                    // calculate sleep time
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);


                    while (sleepTime < 0) {
                        // we need to catch up
                        // update without rendering
                        this.game.update();

                        // add frame period to check if in next frame
                        sleepTime += FRAME_PERIOD;


                        statsUpdatesPerSecond++;
                    }

                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }

                    // Stats
//                    if (System.currentTimeMillis() - statsStartTime > 1000) {
//
//                        Log.d(TAG, "myquado calls: " + statsCallPerSecond + ", updates:" + statsUpdatesPerSecond);
//                        statsStartTime = 0;
//                        statsCallPerSecond = 0;
//                        statsUpdatesPerSecond = 0;
//
//                    }

                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }


}
