package com.strikalov.pogoda4.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectedCityData implements Parcelable {

    private String cityId;
    private String cityName;
    private String date;
    private String temperature;

    public SelectedCityData(String cityId, String cityName, String date, String temperature){

        this.cityId = cityId;
        this.cityName = cityName;
        this.date = date;
        this.temperature = temperature;

    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    protected SelectedCityData(Parcel in) {
        cityId = in.readString();
        cityName = in.readString();
        date = in.readString();
        temperature = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityId);
        dest.writeString(cityName);
        dest.writeString(date);
        dest.writeString(temperature);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SelectedCityData> CREATOR = new Parcelable.Creator<SelectedCityData>() {
        @Override
        public SelectedCityData createFromParcel(Parcel in) {
            return new SelectedCityData(in);
        }

        @Override
        public SelectedCityData[] newArray(int size) {
            return new SelectedCityData[size];
        }
    };

}
