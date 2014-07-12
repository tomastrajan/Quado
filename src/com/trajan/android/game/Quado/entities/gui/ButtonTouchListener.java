package com.trajan.android.game.Quado.entities.gui;

import android.view.MotionEvent;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.GameState;
import com.trajan.android.game.Quado.components.Sounds;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-11
 */
public interface ButtonTouchListener {

    public void excute(MainGamePanel game, Sounds sounds, GameState gameState);

}
