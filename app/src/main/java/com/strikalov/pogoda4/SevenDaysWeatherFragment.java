package com.strikalov.pogoda4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class SevenDaysWeatherFragment extends Fragment {

    private static final String CITY_INDEX = "city_index";

    public static SevenDaysWeatherFragment newInstance(int city_index){

        SevenDaysWeatherFragment sevenDaysWeatherFragment = new SevenDaysWeatherFragment();
        Bundle args = new Bundle();
        args.putInt(CITY_INDEX, city_index);
        sevenDaysWeatherFragment.setArguments(args);
        return sevenDaysWeatherFragment;
    }

    private int getCityIndex() {
        return getArguments().getInt(CITY_INDEX);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView sevenDaysRecycler = (RecyclerView)inflater.inflate(
                R.layout.fragment_seven_days_weather, container, false);

        int cityIndex = getCityIndex();


        WeatherCreator weatherCreator = WeatherCreator.getInstance();

        List<Weather> weatherForecast = weatherCreator.getWeather(cityIndex);

        WeatherDataBuilder weatherDataBuilder = new WeatherDataBuilder(weatherForecast, getResources());

        WeatherAdapter weatherAdapter = new WeatherAdapter(weatherDataBuilder.build());

        sevenDaysRecycler.setAdapter(weatherAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        sevenDaysRecycler.setLayoutManager(linearLayoutManager);

        return sevenDaysRecycler;
    }

}
