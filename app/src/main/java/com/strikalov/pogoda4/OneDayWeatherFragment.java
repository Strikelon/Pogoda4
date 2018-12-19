package com.strikalov.pogoda4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class OneDayWeatherFragment extends Fragment {

    private static final String CITY_INDEX = "city_index";

    public static OneDayWeatherFragment newInstance(int city_index){

        OneDayWeatherFragment oneDayWeatherFragment = new OneDayWeatherFragment();
        Bundle args = new Bundle();
        args.putInt(CITY_INDEX, city_index);
        oneDayWeatherFragment.setArguments(args);
        return oneDayWeatherFragment;
    }

    private int getCityIndex() {
        return getArguments().getInt(CITY_INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one_day_weather, container, false);

        String windMeasure = getString(R.string.wind_measure);
        String pressureMeasure = getString(R.string.pressure_measure);
        String humidityMeasure = getString(R.string.humidity_measure);

        TextView textTemperature = view.findViewById(R.id.temperature_today);
        TextView textWind = view.findViewById(R.id.wind_today);
        TextView textPressure = view.findViewById(R.id.pressure_today);
        TextView textHumidity = view.findViewById(R.id.humidity_today);

        ImageView imageTemperature = view.findViewById(R.id.picture_temperature_today);
        ImageView imageWind = view.findViewById(R.id.picture_wind_today);
        ImageView imagePressure = view.findViewById(R.id.picture_pressure_today);
        ImageView imageHumidity = view.findViewById(R.id.picture_humidity_today);

        int cityIndex = getCityIndex();

        WeatherCreator weatherCreator = WeatherCreator.getInstance();

        Weather weatherToday = weatherCreator.getWeatherToday(cityIndex);

        if(weatherToday != null){

            textTemperature.setText(weatherToday.getTemperature());
            textWind.setText(getForecastString(windMeasure, weatherToday.getWind()));
            textPressure.setText(getForecastString(pressureMeasure, weatherToday.getPressure()));
            textHumidity.setText(getForecastString(humidityMeasure, weatherToday.getHumidity()));

            imagePressure.setImageResource(R.drawable.barometr);
            imageHumidity.setImageResource(R.drawable.humidity);

            switch (weatherToday.getWeatherPicture()) {
                case SUN:
                    imageTemperature.setImageResource(R.drawable.sun);
                    break;
                case RAIN:
                    imageTemperature.setImageResource(R.drawable.rain);
                    break;
                case SNOW:
                    imageTemperature.setImageResource(R.drawable.snow);
                    break;
                case CLOUD:
                    imageTemperature.setImageResource(R.drawable.cloud);
                    break;
            }

            switch (weatherToday.getWindDirection()) {
                case NORTH:
                    imageWind.setImageResource(R.drawable.north);
                    break;
                case NORTH_WEST:
                    imageWind.setImageResource(R.drawable.north_west);
                    break;
                case NORTH_EAST:
                    imageWind.setImageResource(R.drawable.north_east);
                    break;
                case WEST:
                    imageWind.setImageResource(R.drawable.west);
                    break;
                case EAST:
                    imageWind.setImageResource(R.drawable.east);
                    break;
                case SOUTH:
                    imageWind.setImageResource(R.drawable.south);
                    break;
                case SOUTH_WEST:
                    imageWind.setImageResource(R.drawable.south_west);
                    break;
                case SOUTH_EAST:
                    imageWind.setImageResource(R.drawable.south_east);
                    break;
            }

        }

        return view;
    }

    private String getForecastString(String measure, String value){

        StringBuilder result = new StringBuilder(value);
        result.append(" ").append(measure);

        return result.toString();

    }

}
