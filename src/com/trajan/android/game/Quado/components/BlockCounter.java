
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


public class BlockCounter implements Component {

    private int remainingBlockCount;
    private int totalBlockCount;


    public BlockCounter() {
        this.totalBlockCount = 0;
        this.remainingBlockCount = 0;
    }

    public int getRemainingBlockCount() {
        return remainingBlockCount;
    }

    public void setRemainingBlockCount(int remainingBlockCount) {
        this.remainingBlockCount = remainingBlockCount;
    }

    public int getTotalBlockCount() {
        return totalBlockCount;
    }

    public void setTotalBlockCount(int totalBlockCount) {
        this.totalBlockCount = totalBlockCount;
    }

    public void incrementTotalBlockCount() {
        totalBlockCount++;
    }

    public void decrementRemainingBlockCount() {
        if (remainingBlockCount > 0) {
            remainingBlockCount--;
        }
    }

    public void incrementRemainingBlockCount() {
        remainingBlockCount++;
    }

    public void initializeRemainingBlockCount() {
        remainingBlockCount = totalBlockCount;
    }

    public float getRemainingToTotalBlockCountRatio() {
        return 1 - ((float) remainingBlockCount / totalBlockCount);
    }

}
