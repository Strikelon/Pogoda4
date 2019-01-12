package com.strikalov.pogoda4;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Weather implements Parcelable {

    private Calendar calendar;
    private WeatherPicture weatherPicture;
    private String temperature;
    private String wind;
    private WindDirection windDirection;
    private String pressure;
    private String humidity;
    private String time;

    public Weather(Calendar calendar, WeatherPicture weatherPicture, String temperature, String wind,
                   WindDirection windDirection, String pressure, String humidity) {

        this.calendar = calendar;
        this.weatherPicture = weatherPicture;
        this.temperature = temperature;
        this.wind = wind;
        this.windDirection = windDirection;
        this.pressure = pressure;
        this.humidity = humidity;
        this.time = "";
    }

    public Weather(Calendar calendar, WeatherPicture weatherPicture, String temperature, String wind, WindDirection windDirection,
                   String pressure, String humidity, String time) {

        this.calendar = calendar;
        this.weatherPicture = weatherPicture;
        this.temperature = temperature;
        this.wind = wind;
        this.windDirection = windDirection;
        this.pressure = pressure;
        this.humidity = humidity;
        this.time = time;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    protected Weather(Parcel in) {
        calendar = (Calendar) in.readValue(Calendar.class.getClassLoader());
        weatherPicture = (WeatherPicture) in.readValue(WeatherPicture.class.getClassLoader());
        temperature = in.readString();
        wind = in.readString();
        windDirection = (WindDirection) in.readValue(WindDirection.class.getClassLoader());
        pressure = in.readString();
        humidity = in.readString();
        time = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(calendar);
        dest.writeValue(weatherPicture);
        dest.writeString(temperature);
        dest.writeString(wind);
        dest.writeValue(windDirection);
        dest.writeString(pressure);
        dest.writeString(humidity);
        dest.writeString(time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

}
