package com.strikalov.pogoda4.pojogson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TemperatureRequest {

    @SerializedName("main")
    @Expose
    private Main main;


    public class Main {

        @SerializedName("temp")
        @Expose
        private double temp;

        public double getTemp() {
            return temp;
        }
    }

    public Main getMain() {
        return main;
    }

    @Override
    public String toString() {
        return "TemperatureRequest{" +
                "temp = " + main.getTemp() +
                "}\n";
    }
}
