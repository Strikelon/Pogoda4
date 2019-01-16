package com.strikalov.pogoda4.pojogson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class GroupTemperatureRequest {

    @SerializedName("list")
    @Expose
    TemperatureRequest[] temperatureRequests;

    public TemperatureRequest[] getTemperatureRequests() {
        return temperatureRequests;
    }

    @Override
    public String toString() {
        return "GroupTemperatureRequest{" +
                "temperatureRequests=" + Arrays.toString(temperatureRequests) +
                '}';
    }
}
