
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

import java.io.*;
import java.util.*;

public class ExtStorage implements Component {

    private static final String TAG = ExtStorage.class.getSimpleName();

    private static final String FILE_PATH = "/Android/data/com.trajan.android.games.Quado/files/";

    // File names
    public static final String HIGH_SCORE_NORMAL_FILE = "high_score.txt";
    public static final String HIGH_SCORE_ARCADE_FILE = "high_score_arcade.txt";
    public static final String SETTINGS_FILE = "settings.txt";

    // Settings properties
    public static final String SETTINGS_THEME = "theme";
    public static final String SETTINGS_VOLUME = "volume";



    private boolean isStorage = false;
    private Context context;

    public ExtStorage(Context context) {
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

    public void saveHighScore(int scorePoint, int scoreHelper, String fileName) {

        try {

            HashMap<Integer, Integer> highScore = new HashMap<Integer, Integer>();
            highScore.put(scorePoint, scoreHelper);

            List<Integer> sortHelper = new ArrayList<Integer>();

            File file = getOrCreateFile(fileName);
            if (file != null) {

                InputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    int scoreValue = Integer.parseInt(line.split("\\|")[0]);
                    int secondsValue = Integer.parseInt(line.split("\\|")[1]);

                    highScore.put(scoreValue, secondsValue);
                }


                sortHelper.addAll(highScore.keySet());
                Collections.sort(sortHelper);
                if (sortHelper.size() > 5) {
                    sortHelper.remove(0);
                }

                PrintWriter pw = new PrintWriter(new FileWriter(file, false));

                for (int i = sortHelper.size() - 1; i >= 0; i--) {

                    int key = sortHelper.get(i);
                    int value = highScore.get(key);

                    pw.println(key + "|" + value);
                }

                pw.flush();
                pw.close();
            } else {
            }

        } catch (IOException e) {
        }
    }

    public List<String> getHighScore(String fileName) {

        try {

            List<String> highScoreList = new ArrayList<String>();

            File highScoreFile = getOrCreateFile(fileName);
            InputStream inputStream = new FileInputStream(highScoreFile);

            if (inputStream != null) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    highScoreList.add(line);
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


}
