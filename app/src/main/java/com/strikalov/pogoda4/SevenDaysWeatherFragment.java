package com.strikalov.pogoda4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SevenDaysWeatherFragment extends Fragment {

    private static final String WEATHER_LIST = "weather_list";

    public static SevenDaysWeatherFragment newInstance(ArrayList<Weather> weatherList){

        SevenDaysWeatherFragment sevenDaysWeatherFragment = new SevenDaysWeatherFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(WEATHER_LIST,weatherList);
        sevenDaysWeatherFragment.setArguments(args);
        return sevenDaysWeatherFragment;
    }

    private ArrayList<Weather> getWeatherList(){
        return getArguments().getParcelableArrayList(WEATHER_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView sevenDaysRecycler = (RecyclerView)inflater.inflate(
                R.layout.fragment_seven_days_weather, container, false);

        ArrayList<Weather> weatherForecast = getWeatherList();

        WeatherDataBuilder weatherDataBuilder = new WeatherDataBuilder(weatherForecast, getResources());

        WeatherAdapter weatherAdapter = new WeatherAdapter(weatherDataBuilder.build());

        sevenDaysRecycler.setAdapter(weatherAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        sevenDaysRecycler.setLayoutManager(linearLayoutManager);

        return sevenDaysRecycler;
    }

}
