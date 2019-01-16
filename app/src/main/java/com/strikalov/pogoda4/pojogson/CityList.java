package com.strikalov.pogoda4.pojogson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class CityList {

    @SerializedName("citylist")
    @Expose
    private City[] city;

    @Override
    public String toString() {
        return "CityList{\n" +
                Arrays.toString(city) +
                "\n}";
    }

    public City[] getCity() {
        return city;
    }

    public void setCity(City[] city) {
        this.city = city;
    }

}
