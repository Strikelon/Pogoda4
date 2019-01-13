package com.strikalov.pogoda4.pojogson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherRequest {

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("weather")
    @Expose
    private Weather[] weather;

    @SerializedName("dt")
    @Expose
    private long dt;


    public double getTemp() {
        return main.getTemp();
    }

    public double getPressure() {
        return main.getPressure();
    }

    public String getHumidity() {
        return main.getHumidity();
    }

    public double getSpeed() {
        return wind.getSpeed();
    }

    public double getDeg() {
        return wind.getDeg();
    }

    public String getIcon() {
        return weather[0].getIcon();
    }

    public long getDt() {
        return dt;
    }

    public class Main {

        @SerializedName("temp")
        @Expose
        private double temp;

        @SerializedName("pressure")
        @Expose
        private double pressure;

        @SerializedName("humidity")
        @Expose
        private String humidity;

        public double getTemp() {
            return temp;
        }

        public double getPressure() {
            return pressure;
        }

        public String getHumidity() {
            return humidity;
        }
    }

    public class Wind {

        @SerializedName("speed")
        @Expose
        private double speed;
        @SerializedName("deg")
        @Expose
        private double deg;

        public double getSpeed() {
            return speed;
        }

        public double getDeg() {
            return deg;
        }
    }

    public class Weather {
        @SerializedName("icon")
        @Expose
        private String icon;

        public String getIcon() {
            return icon;
        }
    }

    @Override
    public String toString() {
        return "WeatherRequest{" +
                "temp=" + getTemp() +
                ", pressure=" + getPressure() +
                ", humidity=" + getHumidity() +
                ", speed=" + getSpeed() +
                ", deg=" + getDeg() +
                ", icon=" + getIcon() +
                ", dt=" + getDt() +
                "}\n";
    }
}
