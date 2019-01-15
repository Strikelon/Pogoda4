package com.strikalov.pogoda4.models;

public class WeatherData {

    private String date;
    private int picture;
    private String temperature;
    private String wind;
    private String pressure;
    private String humidity;
    private int windDirectionPicture;

    public WeatherData(String date, int picture, String temperature, String wind, String pressure, String humidity, int windDirectionPicture){
        this.date = date;
        this.picture = picture;
        this.temperature = temperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windDirectionPicture = windDirectionPicture;
    }

    public int getWindDirectionPicture() {
        return windDirectionPicture;
    }

    public void setWindDirectionPicture(int windDirectionPicture) {
        this.windDirectionPicture = windDirectionPicture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
