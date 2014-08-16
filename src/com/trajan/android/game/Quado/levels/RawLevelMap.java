
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RawLevelMap implements LevelMap {

    private static final String TAG = RawLevelMap.class.getSimpleName();

    private Integer rowCount = 0;
    private Integer colCount = 0;
    private BlockType[][] blockMap;
    private InputStream inputStream;

    public RawLevelMap(Context context, int levelResourceConstant) {

        inputStream = context.getResources().openRawResource(levelResourceConstant);
        initializeMap();

    }

    @Override
    public void initializeMap() {

        // Initialize
        List<String> rowList = new ArrayList<String>();
        int lineCount = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;


        try {
            while (((line = reader.readLine()) != null) && (!line.equals(""))) {
                if (colCount == 0) {
                    colCount = line.length();
                }
                lineCount++;
                rowList.add(line);
            }
        } catch (IOException e) {

        }
        rowCount = lineCount;

        // Create block map
        blockMap = new BlockType[rowCount][colCount];

        int rowCount = 0;
        for (String row : rowList) {

            for (int col = 0; col < row.length(); col++) {
                if (row.charAt(col) == BlockType.NORMAL.getCode()) {
                    blockMap[rowCount][col] = BlockType.NORMAL;
                } else if (row.charAt(col) ==  BlockType.INDESTRUCTIBLE.getCode()) {
                    blockMap[rowCount][col] = BlockType.INDESTRUCTIBLE;
                } else {
                    blockMap[rowCount][col] = BlockType.EMPTY;
                }
            }
            rowCount++;
        }
    }


    @Override
    public int getRows() {
        return rowCount;
    }

    @Override
    public int getCols() {
        return colCount;
    }

    @Override
    public BlockType[][] getBlockMap() {
        return blockMap;
    }
}
