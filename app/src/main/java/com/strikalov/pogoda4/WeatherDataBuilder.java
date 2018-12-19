package com.strikalov.pogoda4;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeatherDataBuilder {

    private List<WeatherData> weatherDataList;
    private Resources resources;
    private List<Weather> weatherList;

    public WeatherDataBuilder(List<Weather> weatherList, Resources resources) {
        weatherDataList = new ArrayList<>();
        this.weatherList = weatherList;
        this.resources = resources;
    }

    public List<WeatherData> build() {

        String[] dayOfWeek = resources.getStringArray(R.array.day_of_week);
        String[] month = resources.getStringArray(R.array.month);

        String temperatureString = resources.getString(R.string.temperature);
        String windString = resources.getString(R.string.wind);
        String windMeasure = resources.getString(R.string.wind_measure);
        String pressureString = resources.getString(R.string.pressure);
        String pressureMeasure = resources.getString(R.string.pressure_measure);
        String humidityString = resources.getString(R.string.humidity);
        String humidityMeasure = resources.getString(R.string.humidity_measure);

        int length = weatherList.size();

        String[] dates = new String[length];
        int[] pictures = new int[length];
        String[] temperature = new String[length];
        String[] wind = new String[length];
        String[] pressure = new String[length];
        String[] humidity = new String[length];

        for (int i = 0; i < length; i++) {
            dates[i] = getDayString(weatherList.get(i).getCalendar(), dayOfWeek, month);

            switch (weatherList.get(i).getWeatherPicture()) {
                case SUN:
                    pictures[i] = R.drawable.sun;
                    break;
                case RAIN:
                    pictures[i] = R.drawable.rain;
                    break;
                case SNOW:
                    pictures[i] = R.drawable.snow;
                    break;
                case CLOUD:
                    pictures[i] = R.drawable.cloud;
                    break;
            }

            temperature[i] = getTemperatureString(temperatureString, weatherList.get(i).getTemperature());
            wind[i] = getForecastString(windString, windMeasure ,weatherList.get(i).getWind());
            pressure[i] = getForecastString(pressureString, pressureMeasure ,weatherList.get(i).getPressure());
            humidity[i] = getForecastString(humidityString, humidityMeasure, weatherList.get(i).getHumidity());

        }

        for (int i = 0; i < length; i++) {
            weatherDataList.add(new WeatherData(dates[i], pictures[i], temperature[i], wind[i], pressure[i],humidity[i]));
        }

        return weatherDataList;
    }


    private String getDayString(Calendar calendar, String[] dayOfWeek, String[] month) {

        StringBuilder result = new StringBuilder();
        int dayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int dayDate = calendar.get(Calendar.DAY_OF_MONTH);
        int monthIndex = calendar.get(Calendar.MONTH);

        return result.append(dayOfWeek[dayWeekIndex]).append("\n").append(dayDate).append(" ").append(month[monthIndex]).toString();
    }

    private String getTemperatureString(String temperatureString, String value) {

        StringBuilder result = new StringBuilder(temperatureString);
        result.append(":").append(" ").append(value);

        return result.toString();
    }

    private String getForecastString(String name, String measure, String value){

        StringBuilder result = new StringBuilder(name);
        result.append(":").append(" ").append(value).append(" ").append(measure);

        return result.toString();

    }



}
