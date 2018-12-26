package com.strikalov.pogoda4;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GetWeatherService extends Service {

    private final IBinder binder = new GetWeatherBinder();
    private WeatherCreator weatherCreator;

    public class GetWeatherBinder extends Binder {
        public GetWeatherService getService(){
            return GetWeatherService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        weatherCreator = WeatherCreator.getInstance();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherCreator = null;
    }

    public Weather getWeatherToday(int cityIndex){
        if(weatherCreator != null) {
            return weatherCreator.getWeatherToday(cityIndex);
        }else {
            return null;
        }
    }

    public ArrayList<Weather> getWeather(int cityIndex){
        if(weatherCreator != null) {
            return weatherCreator.getWeather(cityIndex);
        }else {
            return null;
        }
    }

}
