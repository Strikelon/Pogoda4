package com.strikalov.pogoda4;

public enum WindDirection {

    NORTH("NORTH"),
    NORTH_WEST("NORTH_WEST"),
    NORTH_EAST("NORTH_EAST"),
    WEST("WEST"),
    EAST("EAST"),
    SOUTH("SOUTH"),
    SOUTH_WEST("SOUTH_WEST"),
    SOUTH_EAST("SOUTH_EAST");

    private String description;

    WindDirection(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
