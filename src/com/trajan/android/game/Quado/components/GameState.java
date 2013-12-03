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

public class GameState implements Component {


    public static final int STATE_MENU = 0;
    public static final int STATE_GAME = 1;
    public static final int STATE_ARCADE = 2;
    public static final int STATE_VICTORY = 3;
    public static final int STATE_DEFEAT = 4;
    public static final int STATE_PAUSE = 5;

    private static int previousState = STATE_MENU;
    private static int currentState = STATE_MENU;

    private static long arcadeTimeStart;
    private static long arcadeTimeEnd;

    public GameState() {

    }

    private void setNewState(int newState) {
        if (newState != currentState) {
            previousState = currentState;
            currentState = newState;
        }
    }

    public int getPreviousState() {
        return previousState;
    }

    public void setStateMenu() {
        setNewState(STATE_MENU);
    }

    public void setStateGame() {
        setNewState(STATE_GAME);
    }

    public void setStateVictory() {
        setNewState(STATE_VICTORY);
    }

    public void setStateDefeat() {
        setNewState(STATE_DEFEAT);
    }

    public void setStatePause() {
        setNewState(STATE_PAUSE);
    }

    public void setStateArcade() {
        setNewState(STATE_ARCADE);
    }

    public boolean isStateMenu() {
        return currentState == STATE_MENU;
    }

    public boolean isStateGame() {
        return currentState == STATE_GAME;
    }

    public boolean isStateVictory() {
        return currentState == STATE_VICTORY;
    }

    public boolean isStateDefeat() {
        return currentState == STATE_DEFEAT;
    }

    public boolean isStatePause() {
        return currentState == STATE_PAUSE;
    }

    public boolean isStateArcade() {
        return currentState == STATE_ARCADE;
    }

    public static void resetGameState() {
        currentState = STATE_MENU;
    }

    public long getArcadeTimeStart() {
        return arcadeTimeStart;
    }

    public void setArcadeTimeStart(long arcadeTimeStart) {
        GameState.arcadeTimeStart = arcadeTimeStart;
    }

    public long getArcadeTimeEnd() {
        return arcadeTimeEnd;
    }

    public void setArcadeTimeEnd(long arcadeTimeEnd) {
        GameState.arcadeTimeEnd = arcadeTimeEnd;
    }

}
