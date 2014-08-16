package com.trajan.android.game.Quado.levels;

/*

   Created with IntelliJ IDEA.

   Ing. Tomáš Herich
   --------------------------- 
   16. 08. 2014
   12:41

*/

public enum BlockType {
    EMPTY('o'),
    NORMAL('x'),
    INDESTRUCTIBLE('Z');

    private char code;

    BlockType(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }
}
