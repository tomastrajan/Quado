package com.trajan.android.game.Quado.model;

/**
 * @author Tomas Trajan
 * @creaded 2014-07-13
 */
public class Player {

    private String UUID;
    private String name;

    public Player(String UUID, String name) {
        this.UUID = UUID;
        this.name = name;
    }

    public Player(String persistenceString) {
        this.UUID = persistenceString.split("\\|")[0];
        this.name = persistenceString.split("\\|")[1];
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toPersistenceString() {
        return UUID + "|" + name;
    }
}
