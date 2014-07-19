
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

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.trajan.android.game.Quado.model.Player;
import com.trajan.android.game.Quado.model.ScoreRecord;

import java.io.*;
import java.util.*;

public class LocalPersistenceService implements Component {

    private static final String TAG = LocalPersistenceService.class.getSimpleName();

    private static final String FILE_PATH = "/Android/data/com.trajan.android.games.Quado/files/";

    // File names
    public static final String HIGH_SCORE_NORMAL_FILE = "high_score.txt";
    public static final String HIGH_SCORE_ARCADE_FILE = "high_score_arcade.txt";
    public static final String SETTINGS_FILE = "settings.txt";
    public static final String PLAYERS_FILE = "players.txt";

    // Settings properties
    public static final String SETTINGS_THEME = "theme";
    public static final String SETTINGS_VOLUME = "volume";
    public static final String SETTINGS_PLAYER = "player";

    private boolean isStorage = false;
    private Context context;

    public LocalPersistenceService(Context context) {
        this.context = context;
        checkStorage();
    }


    private void checkStorage() {

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            isStorage = true;
        }

    }

    private File getOrCreateFile(String fileName) {

        if (isStorage) {

            File file = new File(context.getExternalFilesDir(null), fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                }
            }
            return file;

        } else {
            return null;
        }
    }


    public void saveHighScore(Integer scoreValue, Integer scoreInfo, String fileName) {

        try {
            File file = getOrCreateFile(fileName);
            List<ScoreRecord> scores = getHighScore(fileName);

            Player player = getSelectedPlayer();
            scores.add(new ScoreRecord(player.getUUID(), player.getName(), scoreInfo.toString(), scoreValue.toString()));
            sortScores(scores);
            if (scores.size() > 10) {
                scores.remove(10);
            }

            PrintWriter pw = new PrintWriter(new FileWriter(file, false));
            for (ScoreRecord score : scores) {
                pw.println(score.getPlayerUUID() + "|" + score.getPlayerName() + "|" + score.getInfo() + "|" + score.getScore());
            }
            pw.flush();
            pw.close();

        } catch (IOException e) {
        }
    }

    private void sortScores(List<ScoreRecord> scores) {
        Collections.sort(scores, new Comparator<ScoreRecord>() {
            @Override
            public int compare(ScoreRecord s1, ScoreRecord s2) {
                if (Integer.parseInt(s1.getScore()) < Integer.parseInt(s2.getScore())) {
                    return 1;
                } else if (Integer.parseInt(s1.getScore()) > Integer.parseInt(s2.getScore())) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public List<ScoreRecord> getHighScore(String fileName) {

        try {

            List<ScoreRecord> highScoreList = new ArrayList<ScoreRecord>();

            File highScoreFile = getOrCreateFile(fileName);
            InputStream inputStream = new FileInputStream(highScoreFile);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] cols = line.split("\\|");
                    highScoreList.add(new ScoreRecord(cols[0], cols[1], cols[2], cols[3]));
                }
                return highScoreList;
            }
        } catch (IOException e) {
        }

        return null;
    }

    public Properties getSettings() {

        try {

            File settingsFile = getOrCreateFile(SETTINGS_FILE);
            InputStream inputStream = new FileInputStream(settingsFile);

            if (inputStream != null) {

                Properties properties = new Properties();
                properties.load(inputStream);

                // Create default settings if they don't exist
                checkDefaultSettings(properties, settingsFile);

                return properties;
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return null;
    }

    private void checkDefaultSettings(Properties properties, File settingsFile) {


        if (properties.getProperty(SETTINGS_VOLUME) == null) {
            properties.setProperty(SETTINGS_VOLUME, "0");
        }

        if (properties.getProperty(SETTINGS_THEME) == null) {
            properties.setProperty(SETTINGS_THEME, "0");
        }

        if (properties.getProperty(SETTINGS_PLAYER) == null) {
            properties.setProperty(SETTINGS_PLAYER,  UUID.randomUUID().toString() + "|Change me!");
        }


        try {
            // Save default settings
            properties.store(new FileOutputStream(settingsFile), null);
        } catch (IOException e) {
        }

    }

    public void saveSettings(String property, String value) {

        try {

            // Get properties and update value
            Properties properties = getSettings();
            properties.setProperty(property, value);

            // Store updated properties
            File settingsFile = getOrCreateFile(SETTINGS_FILE);
            properties.store(new FileOutputStream(settingsFile), null);

        } catch (IOException e) {
        }
    }
    public void createNewPlayer(String playerName) {

        try {
            File file = getOrCreateFile(PLAYERS_FILE);
            if (file != null) {
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                playerName.replace("|", "");
                String player = UUID.randomUUID().toString() + "|" + playerName;
                pw.println(player);
                pw.flush();
                pw.close();

                saveSettings(SETTINGS_PLAYER, player);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Create new player failed: " + e.getMessage());
        }
    }

    public Player getSelectedPlayer() {
        String player = getSettings().getProperty(LocalPersistenceService.SETTINGS_PLAYER);
        return new Player(player);
    }

    public Player getNextPlayer(Player currentPlayer) {
        try {

            List<Player> players = new ArrayList<Player>();

            File playersFile = getOrCreateFile(PLAYERS_FILE);
            InputStream inputStream = new FileInputStream(playersFile);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    players.add(new Player(line));
                }

                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i).getUUID().equals(currentPlayer.getUUID())) {
                        Player selectedPlayer;
                        if ((i + 1) < players.size()) {
                            selectedPlayer = players.get((i + 1));
                        } else {
                            selectedPlayer = players.get(0);
                        }
                        saveSettings(SETTINGS_PLAYER, selectedPlayer.toPersistenceString());
                        return selectedPlayer;
                    }
                }

            }
        } catch (IOException e) {
            Log.d(TAG, "Get next player failed: " + e.getMessage());
        }

        return null;
    }
}
