
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

public class Score implements Component {

    private static final int BLOCK_SCORE_VALUE = 1;

    private int scoreMultiplicatorMax;
    private int scoreMultiplicatorMin;
    private int scoreMultiplicator = scoreMultiplicatorMin;
    private int score;


    public Score() {
        scoreMultiplicatorMin = 1;
        scoreMultiplicatorMax = 20;
    }

    public void incrementScoreMultiplicator() {
        scoreMultiplicatorMin++;
        scoreMultiplicatorMax++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreMultiplicator() {
        return scoreMultiplicator;
    }

    public void brickHit() {
        score += BLOCK_SCORE_VALUE * scoreMultiplicator;
        if (scoreMultiplicator > scoreMultiplicatorMin) {
            scoreMultiplicator--;
        }

        if (scoreMultiplicator < scoreMultiplicator) {
            scoreMultiplicator = scoreMultiplicatorMin;
        }
    }

    public void padHit() {
        scoreMultiplicator = scoreMultiplicatorMax;
    }

}
