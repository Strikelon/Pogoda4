package com.strikalov.pogoda4;

public enum WeatherPicture {

    D01("CLEAR_SKY_DAY"),
    N01("CLEAR_SKY_NIGHT"),
    D02("FEW_CLOUDS_DAY"),
    N02("FEW_CLOUDS_NIGHT"),
    D03("SCATTERED_CLOUDS_DAY"),
    N03("SCATTERED_CLOUDS_NIGHT"),
    D04("BROKEN_CLOUDS_DAY"),
    N04("BROKEN_CLOUDS_NIGHT"),
    D09("SHOWER_RAIN_DAY"),
    N09("SHOWER_RAIN_NIGHT"),
    D10("RAIN_DAY"),
    N10("RAIN_NIGHT"),
    D11("THUNDERSTORM_DAY"),
    N11("THUNDERSTORM_NIGHT"),
    D13("SNOW_DAY"),
    N13("SNOW_NIGHT"),
    D50("MIST_DAY"),
    N50("MIST_NIGHT"),
    NO_ICON("NO_ICON");

    private String description;

    WeatherPicture(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
