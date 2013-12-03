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

package com.trajan.android.game.Quado;

import com.trajan.android.game.Quado.components.Component;
import com.trajan.android.game.Quado.entities.Entity;
import com.trajan.android.game.Quado.levels.Level;

import java.util.*;

public class Elements {

    // Game entities
    public static final int BALL = 0;
    public static final int BAR = 1;
    public static final int BUTTON_CLOSE = 2;
    public static final int BUTTON_RETRY = 3;
    public static final int SCORE_DISPLAY = 4;
    public static final int SCREEN_DEFEAT = 5;
    public static final int SCREEN_VICTORY = 6;
    public static final int SCREEN_PAUSE = 7;
    public static final int SCREEN_MENU = 8;

    // Game component
    public static final int EXTERNAL_STORAGE_PROVIDER = 1001;
    public static final int RENDER_HELPER = 1002;
    public static final int SCORE = 1003;
    public static final int SOUNDS = 1004;
    public static final int GAME_STATE = 1005;

    // Level
    private Level level;

    private Map<Integer, Entity> entities;
    private Map<Integer, Component> components;

    public Elements() {
        entities = new HashMap<Integer, Entity>();
        components = new HashMap<Integer, Component>();
    }

    public void addEntity(int entityId, Entity entity) {
        entities.put(entityId, entity);
    }

    public Entity getEntity(int entityId) {
        return entities.get(entityId);
    }

    public List<Entity> getAllEntities() {
        return new ArrayList<Entity>(entities.values());
    }

    public void addComponent(int componentId, Component component) {
        components.put(componentId, component);
    }

    public Component getComponent(int componentId) {
        return components.get(componentId);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
