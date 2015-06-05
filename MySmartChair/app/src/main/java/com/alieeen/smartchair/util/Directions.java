package com.alieeen.smartchair.util;

/**
 * Created by alinekborges on 05/06/15.
 */
public enum Directions {

    /**
     * 1 - front
     * 2 - right
     * 3 - left
     * 4 - back
     * 5 - stop
     */

    Front("1"),
    Back("2"),
    Right("3"),
    Left("4"),
    Stop("5");

    private final String command;

    Directions(String command) {
        this.command = command;
    }

    public String getString() { return command; }

}
