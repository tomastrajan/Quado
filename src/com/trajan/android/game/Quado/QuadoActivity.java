
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

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.levels.LevelList;

public class QuadoActivity extends Activity {

    private static final String TAG = QuadoActivity.class.getSimpleName();

    private MainGamePanel game;

    private PowerManager.WakeLock wakeLock;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LevelList.resetLevelId();
        GameState.resetGameState();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        game = new MainGamePanel(this);
        setContentView(game);

    }

    @Override
    protected void onResume() {
        super.onResume();

        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();

        GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);
        if (gameState.isStateNormal() || gameState.isStateArcade()) {
            gameState.setStatePause();
        }

        wakeLock.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        game.destroyThread();
        game = null;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
