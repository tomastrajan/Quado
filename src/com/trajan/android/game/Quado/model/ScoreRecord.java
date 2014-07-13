package com.trajan.android.game.Quado.model;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-13
 */
public class ScoreRecord {

    private String playerUUID;
    private String playerName;
    private String info;
    private String score;

    public ScoreRecord(String playerUUID, String playerName, String info, String score) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.info = info;
        this.score = score;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
