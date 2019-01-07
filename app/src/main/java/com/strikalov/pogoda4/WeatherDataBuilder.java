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
        String pressureString = resources.getString(R.string.pressure);
        String humidityString = resources.getString(R.string.humidity);

        int length = weatherList.size();

        String[] dates = new String[length];
        int[] pictures = new int[length];
        String[] temperature = new String[length];
        String[] wind = new String[length];
        String[] pressure = new String[length];
        String[] humidity = new String[length];
        int[] windDirectionsPictures = new int[length];

        for (int i = 0; i < length; i++) {
            dates[i] = getDayString(weatherList.get(i).getCalendar(), weatherList.get(i).getTime(),dayOfWeek, month);

            switch (weatherList.get(i).getWeatherPicture()) {
                case D01:
                    pictures[i] = R.drawable.d01;
                    break;
                case N01:
                    pictures[i] = R.drawable.n01;
                    break;
                case D02:
                    pictures[i] = R.drawable.d02;
                    break;
                case N02:
                    pictures[i] = R.drawable.n02;
                    break;
                case D03:
                    pictures[i] = R.drawable.d03;
                    break;
                case N03:
                    pictures[i] = R.drawable.n03;
                    break;
                case D04:
                    pictures[i] = R.drawable.d04;
                    break;
                case N04:
                    pictures[i] = R.drawable.n04;
                    break;
                case D09:
                    pictures[i] = R.drawable.d09;
                    break;
                case N09:
                    pictures[i] = R.drawable.n09;
                    break;
                case D10:
                    pictures[i] = R.drawable.d10;
                    break;
                case N10:
                    pictures[i] = R.drawable.n10;
                    break;
                case D11:
                    pictures[i] = R.drawable.d11;
                    break;
                case N11:
                    pictures[i] = R.drawable.n11;
                    break;
                case D13:
                    pictures[i] = R.drawable.d13;
                    break;
                case N13:
                    pictures[i] = R.drawable.n13;
                    break;
                case D50:
                    pictures[i] = R.drawable.d50;
                    break;
                case N50:
                    pictures[i] = R.drawable.n50;
                    break;
                case NO_ICON:
                    pictures[i] = R.drawable.no_icon_temperature;
            }

            switch (weatherList.get(i).getWindDirection()){
                case NORTH:
                    windDirectionsPictures[i] = R.drawable.north;
                    break;
                case NORTH_WEST:
                    windDirectionsPictures[i] = R.drawable.north_west;
                    break;
                case NORTH_EAST:
                    windDirectionsPictures[i] = R.drawable.north_east;
                    break;
                case WEST:
                    windDirectionsPictures[i] = R.drawable.west;
                    break;
                case EAST:
                    windDirectionsPictures[i] = R.drawable.east;
                    break;
                case SOUTH:
                    windDirectionsPictures[i] = R.drawable.south;
                    break;
                case SOUTH_WEST:
                    windDirectionsPictures[i] = R.drawable.south_west;
                    break;
                case SOUTH_EAST:
                    windDirectionsPictures[i] = R.drawable.south_east;
                    break;
                case NO_DIRECTION:
                    windDirectionsPictures[i] = R.drawable.no_direction_wind;
                    break;
            }

            temperature[i] = getForecastString(temperatureString, weatherList.get(i).getTemperature());
            wind[i] = getForecastString(windString, weatherList.get(i).getWind());
            pressure[i] = getForecastString(pressureString, weatherList.get(i).getPressure());
            humidity[i] = getForecastString(humidityString, weatherList.get(i).getHumidity());

        }

        for (int i = 0; i < length; i++) {
            weatherDataList.add(new WeatherData(dates[i], pictures[i], temperature[i], wind[i],
                    pressure[i], humidity[i], windDirectionsPictures[i]));
        }

        return weatherDataList;
    }


    private String getDayString(Calendar calendar, String time,String[] dayOfWeek, String[] month) {

        StringBuilder result = new StringBuilder();
        int dayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int dayDate = calendar.get(Calendar.DAY_OF_MONTH);
        int monthIndex = calendar.get(Calendar.MONTH);

        return result.append(dayOfWeek[dayWeekIndex]).append("\n")
                .append(dayDate).append(" ").append(month[monthIndex]).append(" ").append(time).toString();
    }

    private String getForecastString(String name, String value){

        StringBuilder result = new StringBuilder(name);
        result.append(":").append(" ").append(value);

        return result.toString();

    }



}
