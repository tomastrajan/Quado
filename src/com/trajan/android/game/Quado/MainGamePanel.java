
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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.trajan.android.game.Quado.helpers.MyTouchEventListener;
import com.trajan.android.game.Quado.helpers.MyUpdateEventListener;
import com.trajan.android.game.Quado.helpers.MyColors;
import com.trajan.android.game.Quado.levels.Level;
import com.trajan.android.game.Quado.levels.LevelList;
import com.trajan.android.game.Quado.components.*;
import com.trajan.android.game.Quado.entities.*;
import com.trajan.android.game.Quado.levels.LevelMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback, Serializable {

    private static final String TAG = MainGamePanel.class.getSimpleName();

    // Thread
    private MainThread thread;

    // Game entities
    private Bar bar;
    private Ball ball;
    private ButtonPause buttonPause;
    private ScoreDisplay scoreDisplay;
    private ScreenDefeat screenDefeat;
    private ScreenVictory screenVictory;
    private ScreenPause screenPause;
    private ScreenMenu screenMenu;

    // Game components
    private Level level;
    private GameState gameState;
    private Score score;
    private CollisionDetector collisionDetector;
    private RenderHelper renderHelper;
    private EntityPositionAndSizeCalculator posSizeCalc;
    private ExtStorage resExtStorage;
    private Sounds resSounds;


    // Listeners
    private List<MyTouchEventListener> touchEventListeners;
    private List<MyUpdateEventListener> updateEventListeners;

    // Elements
    private Elements elements;

    public MainGamePanel(Context context) {
        super(context);

        // Intercept surface holder to intercept events
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        setFocusable(true);

        thread = new MainThread(getHolder(), this);

        // Initialize resources
        resExtStorage = new ExtStorage(context);
        resSounds = new Sounds(context, resExtStorage);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        DeviceInfo.INSTANCE.setSurfaceHeight(getHeight());
        DeviceInfo.INSTANCE.setSurfaceWidth(getWidth());
        DeviceInfo.INSTANCE.setContext(getContext());

        initializeGame(null);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (MyTouchEventListener listener : touchEventListeners) {
            listener.handleTouchEvent(this, event);
        }
        return true;
    }

    public void update() {
        if (thread.isRunning()) {
            for (MyUpdateEventListener listener : updateEventListeners) {
                listener.handleUpdateEvent(this);
            }
        }
    }

    public void render(Canvas canvas) {
        if (thread.isRunning()) {
            renderHelper.render(this, canvas);
        }
    }

    private void initializeGame(Score victoryScore) {

        touchEventListeners = new ArrayList<MyTouchEventListener>();
        updateEventListeners = new ArrayList<MyUpdateEventListener>();

        elements = new Elements();

        // Initialize color theme
        Properties settings = resExtStorage.getSettings();
        MyColors.setColorTheme(Integer.parseInt(settings.getProperty("theme")));

        // Initialize game components
        collisionDetector = new CollisionDetector();
        renderHelper = new RenderHelper();
        gameState = new GameState();

        // Initialize level, score and game state
        LevelList levelList = new LevelList();
        LevelMap levelMap;

        // Main menu
        if (gameState.isStateMenu()) {

            score = new Score();
            levelMap = levelList.getIntro();

        // Arcade
        } else if (gameState.isStateArcade()) {

            score = new Score();
            gameState.setArcadeTimeStart(System.currentTimeMillis());
            levelMap = levelList.getRandomLevel();

        // Normal
        } else {

            // Continue after victory
            if (victoryScore != null) {
                score = victoryScore;
                score.incrementScoreMultiplicator();
                score.padHit();
                levelMap = levelList.getNextLevel();

            // New game
            } else {
                score = new Score();
                levelMap = levelList.getFirstLevel();
            }
        }

        posSizeCalc = new EntityPositionAndSizeCalculator(levelMap);
        level = new Level(levelMap, posSizeCalc);

        // Initialize game entities
        bar = new Bar(posSizeCalc.getBar());
        ball = new Ball(posSizeCalc.getBall());
        buttonPause = new ButtonPause(posSizeCalc.getCloseButton());
        scoreDisplay = new ScoreDisplay(posSizeCalc.getScoreDisplay());
        screenDefeat = new ScreenDefeat(posSizeCalc.getFullCenterMessage(), getContext(), R.string.message_defeat);
        screenVictory = new ScreenVictory(posSizeCalc.getFullCenterMessage(), getContext(), R.string.message_victory);
        screenPause = new ScreenPause(posSizeCalc.getFullCenterMessage(), getContext(), R.string.message_pause);
        screenMenu = new ScreenMenu(posSizeCalc.getFullCenterMessage(), getContext(), R.string.message_menu);

        // Set score value
        scoreDisplay.setScore(score);

        // Add level to elements
        elements.setLevel(level);

        // Add entities to elements
        elements.addEntity(Elements.BAR, bar);
        elements.addEntity(Elements.BALL, ball);
        elements.addEntity(Elements.BUTTON_CLOSE, buttonPause);
        elements.addEntity(Elements.SCORE_DISPLAY, scoreDisplay);
        elements.addEntity(Elements.SCREEN_DEFEAT, screenDefeat);
        elements.addEntity(Elements.SCREEN_VICTORY, screenVictory);
        elements.addEntity(Elements.SCREEN_PAUSE, screenPause);
        elements.addEntity(Elements.SCREEN_MENU, screenMenu);

        // Add components to elements
        elements.addComponent(Elements.SCORE, score);
        elements.addComponent(Elements.SOUNDS, resSounds);
        elements.addComponent(Elements.RENDER_HELPER, renderHelper);
        elements.addComponent(Elements.GAME_STATE, gameState);
        elements.addComponent(Elements.EXTERNAL_STORAGE_PROVIDER, resExtStorage);

        // Add entities to render helper
        renderHelper.addGameEntities(elements);

        // Add MyTouchEventListeners
        addMyTouchEventListener(bar);
        addMyTouchEventListener(buttonPause);
        addMyTouchEventListener(screenDefeat);
        addMyTouchEventListener(screenPause);
        addMyTouchEventListener(screenVictory);
        addMyTouchEventListener(screenMenu);

        // Add MyUpdateEventListeners
        addMyUpdateEventListener(ball);
        addMyUpdateEventListener(collisionDetector);
        addMyUpdateEventListener(level);
        addMyUpdateEventListener(bar);

    }

    public void restart(boolean isVictory) {

        destroyThread();

        thread = new MainThread(getHolder(), this);

        if (isVictory) {
            initializeGame(score);
        } else {
            initializeGame(null);
        }

        thread.setRunning(true);
        thread.start();
    }

    public void destroyThread() {
        boolean retry = true;

        while (retry) {

            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public MainThread getThread() {
        return thread;
    }

    public Elements getElements() {
        return elements;
    }

    public void addMyTouchEventListener(MyTouchEventListener listener) {
        touchEventListeners.add(listener);
    }

    public void removeMyTouchEventListener(MyTouchEventListener listener) {
        touchEventListeners.remove(listener);
    }

    public void addMyUpdateEventListener(MyUpdateEventListener listener) {
        updateEventListeners.add(listener);
    }

    public void removeMyUpdateEventListener(MyUpdateEventListener listener) {
        updateEventListeners.remove(listener);
    }
}
