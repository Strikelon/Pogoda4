package com.strikalov.pogoda4;

import java.util.Calendar;

public class Weather  {

    private Calendar calendar;
    private WeatherPicture weatherPicture;
    private String temperature;
    private String wind;
    private WindDirection windDirection;
    private String pressure;
    private String humidity;

    public Weather(Calendar calendar, WeatherPicture weatherPicture, String temperature, String wind, WindDirection windDirection, String pressure, String humidity) {

        this.calendar = calendar;
        this.weatherPicture = weatherPicture;
        this.temperature = temperature;
        this.wind = wind;
        this.windDirection = windDirection;
        this.pressure = pressure;
        this.humidity = humidity;

    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public WeatherPicture getWeatherPicture() {
        return weatherPicture;
    }

    public void setWeatherPicture(WeatherPicture weatherPicture) {
        this.weatherPicture = weatherPicture;
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
