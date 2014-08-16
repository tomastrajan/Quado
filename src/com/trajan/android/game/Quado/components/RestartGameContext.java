package com.trajan.android.game.Quado.components;

/*

   Created with IntelliJ IDEA.

   Ing. Tomáš Herich
   --------------------------- 
   05. 08. 2014
   20:31

*/

public class RestartGameContext {

    private Score score;
    private boolean arcadeNextLevel;

    private RestartGameContext() { }

    public static RestartGameContext create() {
        return new RestartGameContext();
    }

    public RestartGameContext setArcadeNextLevel(boolean arcadeNextLevel) {
        this.arcadeNextLevel = arcadeNextLevel;
        return this;
    }

    public RestartGameContext setScore(Score score) {
        this.score = score;
        return this;
    }

    public boolean isArcadeNextLevel() {
        return arcadeNextLevel;
    }

    public Score getScore() {
        return score;
    }
}