package com.strikalov.pogoda4;

public enum WeatherPicture {

    SUN("Sun"), CLOUD("Cloud"), RAIN("Rain"), SNOW("Snow");

    private String description;

    WeatherPicture(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
