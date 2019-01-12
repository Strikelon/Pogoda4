package com.strikalov.pogoda4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class WeatherCreator {

    private ArrayList<Weather> PetersburgForecast = new ArrayList<>();
    private ArrayList<Weather> MoscowForecast = new ArrayList<>();
    private ArrayList<Weather> RostovForecast = new ArrayList<>();
    private ArrayList<Weather> SochiForecast = new ArrayList<>();

    private Calendar calendar;

    private static WeatherCreator instance;

    public static synchronized WeatherCreator getInstance() {
        if (instance == null) {
            instance = new WeatherCreator();
        }
        return instance;
    }

    private WeatherCreator() {

        calendar = new GregorianCalendar();

        PetersburgForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-2°C", "2,0", WindDirection.SOUTH_WEST, "753", "90"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        PetersburgForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-3°C", "4,2", WindDirection.NORTH_WEST, "758", "81"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        PetersburgForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-4°C", "3,7", WindDirection.WEST, "770", "86"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        PetersburgForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-3°C", "5,5", WindDirection.SOUTH_WEST, "773", "71"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        PetersburgForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-1°C", "6,1", WindDirection.SOUTH_WEST, "769", "52"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        PetersburgForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-5°C", "4,6", WindDirection.SOUTH, "768", "54"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        PetersburgForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-6°C", "3,0", WindDirection.SOUTH, "759", "80"));

        calendar.add(Calendar.DAY_OF_WEEK, -6);
        MoscowForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-1°C", "3,1", WindDirection.NORTH, "741", "84"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        MoscowForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-1°C", "3,3", WindDirection.SOUTH_WEST, "742", "87"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        MoscowForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-2°C", "4,6", WindDirection.NORTH, "752", "83"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        MoscowForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-4°C", "3,7", WindDirection.WEST, "762", "83"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        MoscowForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-4°C", "3,8", WindDirection.SOUTH_WEST, "765", "71"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        MoscowForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-2°C", "3,8", WindDirection.SOUTH_EAST, "761", "75"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        MoscowForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-2°C", "3,7", WindDirection.SOUTH_EAST, "751", "84"));

        calendar.add(Calendar.DAY_OF_WEEK, -6);
        RostovForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+2°C", "2,5", WindDirection.SOUTH, "758", "92"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        RostovForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+6°C", "3,8", WindDirection.SOUTH, "750", "94"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        RostovForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-1°C", "5,6", WindDirection.NORTH, "757", "93"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        RostovForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-4°C", "7,2", WindDirection.NORTH_EAST, "770", "80"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        RostovForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "-4°C", "8,7", WindDirection.EAST, "772", "77"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        RostovForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+1°C", "8,4", WindDirection.EAST, "763", "88"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        RostovForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+2°C", "2,2", WindDirection.SOUTH_WEST, "762", "88"));

        calendar.add(Calendar.DAY_OF_WEEK, -6);
        SochiForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+14°C", "4,7", WindDirection.SOUTH_EAST, "757", "70"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        SochiForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+14°C", "6,6", WindDirection.SOUTH_EAST, "756", "74"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        SochiForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+17°C", "6,2", WindDirection.SOUTH_EAST, "755", "78"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        SochiForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+13°C", "5,6", WindDirection.SOUTH_EAST, "757", "91"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        SochiForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+16°C", "1,8", WindDirection.NORTH, "758", "78"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        SochiForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+14°C", "1,5", WindDirection.NORTH_EAST, "757", "82"));
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        SochiForecast.add(new Weather((Calendar) calendar.clone(), WeatherPicture.D01, "+11°C", "1,5", WindDirection.NORTH, "762", "72"));

        calendar.add(Calendar.DAY_OF_WEEK, -6);
    }


    public  ArrayList<Weather> getWeather(int cityIndex) {
        switch (cityIndex) {
            case 0:
                return PetersburgForecast;
            case 1:
                return MoscowForecast;
            case 2:
                return RostovForecast;
            case 3:
                return SochiForecast;
            default:
                return null;
        }
    }

    public Weather getWeatherToday(int cityIndex){
        switch (cityIndex) {
            case 0:
                return PetersburgForecast.get(0);
            case 1:
                return MoscowForecast.get(0);
            case 2:
                return RostovForecast.get(0);
            case 3:
                return SochiForecast.get(0);
            default:
                return null;
        }
    }

}
