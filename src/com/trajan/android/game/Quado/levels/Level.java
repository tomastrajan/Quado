
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

import com.trajan.android.game.Quado.DeviceInfo;
import com.trajan.android.game.Quado.Elements;
import com.trajan.android.game.Quado.MainGamePanel;
import com.trajan.android.game.Quado.components.*;
import com.trajan.android.game.Quado.entities.Block;
import com.trajan.android.game.Quado.helpers.MyUpdateEventListener;
import com.trajan.android.game.Quado.model.GameMode;
import com.trajan.android.game.Quado.model.Player;
import com.trajan.android.game.Quado.rest.DefaultRestService;
import com.trajan.android.game.Quado.rest.dto.ScoreDto;

import java.util.ArrayList;
import java.util.List;

public class Level implements MyUpdateEventListener {

    private static final String TAG = Level.class.getSimpleName();

    private List<Block> blocks;
    private BlockCounter blockCounter;
    private EntityPositionAndSizeCalculator posSizeCalc;
    private LevelMap levelMap;
    private int levelCenterY;

    public Level(LevelMap levelMap, EntityPositionAndSizeCalculator posSizeCalc) {

        blocks = new ArrayList<Block>();
        blockCounter = new BlockCounter();
        this.posSizeCalc = posSizeCalc;
        this.levelMap = levelMap;

        createLevel();

    }

    private void createLevel() {

        int blockHeight = 0;

        // Create blocks
        BlockType[][] blockMap = levelMap.getBlockMap();
        for (int row = 0; row < levelMap.getRows(); row++) {
            for (int col = 0; col < levelMap.getCols(); col++) {

                boolean normalBlock = blockMap[row][col] == BlockType.NORMAL;
                boolean indestructibleBlock = blockMap[row][col] == BlockType.INDESTRUCTIBLE;

                if (normalBlock || indestructibleBlock) {

                    Block block = new Block(posSizeCalc.getBlock(), indestructibleBlock);

                    int spacing = posSizeCalc.getBlockSpacing();
                    int horizontalUsedSpace = (block.getWidth() * (levelMap.getCols() - 1)) + (spacing * (levelMap.getCols() - 1));
                    int horizontalOffset = (DeviceInfo.INSTANCE.getSurfaceWidth() - horizontalUsedSpace) / 2 + spacing / 2;

                    block.setX(horizontalOffset + col * block.getWidth() + col * spacing);
                    block.setY(posSizeCalc.getTopOffset() + row * block.getHeight() + row * spacing);

                    block.initializeBlock();

                    blocks.add(block);

                    blockHeight = block.getHeight();

                    if (normalBlock) {
                        blockCounter.incrementTotalBlockCount();
                    }
                }
            }
        }

        // Initialize level vertical center value
        float rows = levelMap.getRows();
        rows = rows / 2;
        levelCenterY = (int) (posSizeCalc.getTopOffset() + rows * blockHeight + (rows - 1) * posSizeCalc.getBlockSpacing() + posSizeCalc.getBlockSpacing() / 2);


        blockCounter.initializeRemainingBlockCount();

    }

    public List<Block> getBlocks() {

        return blocks;

    }

    public BlockCounter getBlockCounter() {

        return blockCounter;

    }


    public int getLevelCenterY() {

        return levelCenterY;

    }

    @Override
    public void handleUpdateEvent(MainGamePanel game) {

        if (getBlockCounter().getRemainingBlockCount() == 0) {

            GameState gameState = (GameState) game.getElements().getComponent(Elements.GAME_STATE);
            Score score = (Score) game.getElements().getComponent(Elements.SCORE);

            if (gameState.isStateNormal()) {

                // Set game state to victory
                gameState.setStateNormalVictory();

                // Get actual score
                int scoreValue = score.getScore();
                int levelId = LevelList.getLevelId() + 1;

                // Save new high score
                LocalPersistenceService lps = game.getLocalPersistenceService();
                lps.saveHighScore(scoreValue, levelId, LocalPersistenceService.HIGH_SCORE_NORMAL_FILE);

                DefaultRestService rest = game.getRest();
                Player player = lps.getSelectedPlayer();
                rest.createOrUpdateScore(new ScoreDto(player.getUUID(), player.getName(),
                        GameMode.NORMAL.name().toLowerCase(), scoreValue, null, levelId));
            }
        }
    }

}
