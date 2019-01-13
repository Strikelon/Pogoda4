package com.strikalov.pogoda4.pojogson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class ForecastRequest {

    @SerializedName("list")
    @Expose
    private WeatherRequest[] weatherRequests;

    public WeatherRequest[] getWeatherRequests() {
        return weatherRequests;
    }

    @Override
    public String toString() {
        return "ForecastRequest{" +
                "weatherRequests=" + Arrays.toString(weatherRequests) +
                '}';
    }
}
