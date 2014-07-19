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
    public static final int STATE_NORMAL = 1;
    public static final int STATE_ARCADE = 2;
    public static final int STATE_NORMAL_VICTORY = 3;
    public static final int STATE_NORMAL_DEFEAT = 4;
    public static final int STATE_ARCADE_DEFEAT = 5;
    public static final int STATE_PAUSE = 6;

    private static int previousState = STATE_MENU;
    private static int currentState = STATE_MENU;

    private static long arcadeTimeStart;
    private static long arcadeTimeEnd;

    public GameState() {

    }

    public static void setPreviousState(int previousState) {
        GameState.previousState = previousState;
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

    public void setStateNormal() {
        setNewState(STATE_NORMAL);
    }

    public void setStateNormalVictory() {
        setNewState(STATE_NORMAL_VICTORY);
    }

    public void setStateNormalDefeat() {
        setNewState(STATE_NORMAL_DEFEAT);
    }

    public void setStateArcadeDefeat() {
        setNewState(STATE_ARCADE_DEFEAT);
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

    public boolean isStateNormal() {
        return currentState == STATE_NORMAL;
    }

    public boolean isStateNormalVictory() {
        return currentState == STATE_NORMAL_VICTORY;
    }

    public boolean isStateArcadeDefeat() {
        return currentState == STATE_ARCADE_DEFEAT;
    }

    public boolean isStateNormalDefeat() {
        return currentState == STATE_NORMAL_DEFEAT;
    }

    public boolean isAnyDefeatState() {
        return currentState == STATE_NORMAL_DEFEAT || currentState == STATE_ARCADE_DEFEAT;
    }

    public boolean isStatePause() {
        return currentState == STATE_PAUSE;
    }

    public boolean isStateArcade() {
        return currentState == STATE_ARCADE;
    }

    public boolean isGameStateAnyActiveState() {
        if (isStateNormal() || isStateArcade() || isStateMenu()) {
            return true;
        } else {
            return false;
        }
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
