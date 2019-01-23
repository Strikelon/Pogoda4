package com.strikalov.pogoda4.pojogson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoPositionRequest {

    @SerializedName("id")
    @Expose
    private String cityId;

    @SerializedName("name")
    @Expose
    private String cityName;

    public String getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return "GeoPositionRequest{" +
                "cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }

}
