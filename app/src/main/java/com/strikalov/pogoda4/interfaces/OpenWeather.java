package com.strikalov.pogoda4.interfaces;

import com.strikalov.pogoda4.pojogson.ForecastRequest;
import com.strikalov.pogoda4.pojogson.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("id") int cityId, @Query("appid") String keyApi);

    @GET("data/2.5/forecast")
    Call<ForecastRequest> loadForecast(@Query("id") int cityId, @Query("appid") String keyApi);

}
