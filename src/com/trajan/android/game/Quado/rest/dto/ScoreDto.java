package com.trajan.android.game.Quado.rest.dto;

import com.trajan.android.game.Quado.model.GameMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-19
 */
public class ScoreDto {

    private Integer position;
    private String playerUuid;
    private String playerName;
    private String gameMode;
    private Integer score;
    private Integer time;
    private Integer level;

    public ScoreDto() {
    }

    public ScoreDto(String playerUuid, String playerName, String gameMode, Integer score, Integer time, Integer level) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.gameMode = gameMode;
        this.score = score;
        this.time = time;
        this.level = level;
    }

    public List<String> getAsList(GameMode gameMode) {
        List<String> result = new ArrayList<>();
        result.add(position.toString());
        result.add(playerName);
        if (gameMode == GameMode.ARCADE) {
            result.add(time.toString());
        } else if (gameMode == GameMode.NORMAL) {
            result.add(level.toString());
        }
        result.add(score.toString());
        return result;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
