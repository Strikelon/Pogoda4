package com.strikalov.pogoda4.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.models.Weather;
import com.strikalov.pogoda4.adapters.WeatherAdapter;
import com.strikalov.pogoda4.utils.WeatherDataBuilder;

import java.util.ArrayList;


public class FiveDaysWeatherFragment extends Fragment {

    private static final String WEATHER_LIST = "weather_list";

    public static FiveDaysWeatherFragment newInstance(ArrayList<Weather> weatherList){

        FiveDaysWeatherFragment fiveDaysWeatherFragment = new FiveDaysWeatherFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(WEATHER_LIST,weatherList);
        fiveDaysWeatherFragment.setArguments(args);
        return fiveDaysWeatherFragment;
    }

    private ArrayList<Weather> getWeatherList(){
        return getArguments().getParcelableArrayList(WEATHER_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView fiveDaysRecycler = (RecyclerView)inflater.inflate(
                R.layout.fragment_five_days_weather, container, false);

        ArrayList<Weather> weatherForecast = getWeatherList();

        WeatherDataBuilder weatherDataBuilder = new WeatherDataBuilder(weatherForecast, getResources());

        WeatherAdapter weatherAdapter = new WeatherAdapter(weatherDataBuilder.build());

        fiveDaysRecycler.setAdapter(weatherAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        fiveDaysRecycler.setLayoutManager(linearLayoutManager);

        return fiveDaysRecycler;
    }

}
