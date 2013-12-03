
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
import android.media.AudioManager;
import android.media.SoundPool;
import com.trajan.android.game.Quado.R;

import java.util.Properties;

public class Sounds implements Component {

    private static final String TAG = Sounds.class.getSimpleName();

    private SoundPool sounds;
    private int soundHit;
    private int soundLastHit;
    private int soundBarHit;

    private float volume;

    private ExtStorage resExtStorage;

    public Sounds(Context context, ExtStorage resExtStorage) {

        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundHit = sounds.load(context, R.raw.hit, 1);
        soundBarHit = sounds.load(context, R.raw.bar, 1);
        soundLastHit = sounds.load(context, R.raw.hit_last, 1);

        this.resExtStorage = resExtStorage;

        initializeVolume();
    }

    private void initializeVolume() {

        Properties properties = this.resExtStorage.getSettings();
        if (properties != null && !properties.isEmpty() && properties.getProperty("volume") != null) {
            volume = Float.parseFloat(properties.getProperty("volume"));
        }

    }

    public void playHit() {
        sounds.play(soundHit, volume, volume, 0, 0, 1.0f);
    }

    public void playLastHit() {
        sounds.play(soundLastHit, volume, volume, 0, 0, 1.0f);
    }

    public void playBarHit() {
        sounds.play(soundBarHit, volume, volume, 0, 0, 1.0f);
    }

    public void setVolume(float newVolume) {

        if (newVolume > 1) {
            newVolume = 1;
        }

        if (newVolume < 0) {
            newVolume = 0;
        }


        volume = newVolume;

        // Save volume to external settings
        if (resExtStorage != null) {
            resExtStorage.saveSettings("volume", String.valueOf(volume));
        }

    }

    public float getVolume() {
        return volume;
    }


}
