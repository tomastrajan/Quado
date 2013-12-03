
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

package com.trajan.android.game.Quado.levels;

import android.content.Context;
import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelList {

    private static final String TAG = LevelList.class.getSimpleName();

    private static int levelId = 0;
    private List<Integer> levelList;
    private Context context;

    public LevelList() {

        levelList = new ArrayList<Integer>();
        context = DeviceInfo.INSTANCE.getContext();

        addLevelsToList();

    }

    public static void resetLevelId() {
        levelId = 0;
    }

    private void addLevelsToList() {

        //levelList.add(R.raw.level_test);
        levelList.add(R.raw.level_1_columns);
        levelList.add(R.raw.level_2_hollow_box);
        levelList.add(R.raw.level_3_cross);
        levelList.add(R.raw.level_4_full_box);
        levelList.add(R.raw.level_5_points);
        levelList.add(R.raw.level_6_lines);
        levelList.add(R.raw.level_7_spiral);
        levelList.add(R.raw.level_8_boxes);
        levelList.add(R.raw.level_9_triangles);
        levelList.add(R.raw.level_10_pipes);
        levelList.add(R.raw.level_11_basket);
        levelList.add(R.raw.level_12_triangle);
        levelList.add(R.raw.level_13_rows);
        levelList.add(R.raw.level_14_four_boxes);
        levelList.add(R.raw.level_15_cupcackes);

    }

    public LevelMap getNextLevel() {

        levelId++;
        if (levelId >= levelList.size()) {
            levelId = 0;
        }

        // Get next level
        int levelResourceConstant = levelList.get(levelId);

        return new RawLevelMap(context, levelResourceConstant);
    }

    public LevelMap getFirstLevel() {

        levelId = 0;

        // Get first level
        int levelResourceConstant = levelList.get(levelId);
        return new RawLevelMap(context, levelResourceConstant);
    }

    public LevelMap getIntro() {

        return new RawLevelMap(context, R.raw.level_intro);

    }


    public LevelMap getRandomLevel() {

        // Get random level
        levelId = new Random().nextInt(levelList.size() - 1); ;
        int levelResourceConstant = levelList.get(levelId);

        return new RawLevelMap(context, levelResourceConstant);

        //return new RawLevelMap(context, R.raw.level_15_cupcackes);

    }

    public LevelMap getLevelByResourceConstant(int levelResourceConstant) {

        // Get specified level
        return new RawLevelMap(context, levelResourceConstant);

    }

    public static int getLevelId() {
        return levelId;
    }
}
