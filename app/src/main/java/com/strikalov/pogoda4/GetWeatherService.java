package com.strikalov.pogoda4;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetWeatherService extends Service {

    private int windSetting;
    private int temperatureSetting;
    private int pressureSetting;

    private String windMeasure = "";
    private String temperatureMeasure = "";
    private String pressureMeasure = "";
    private String humidityMeasure = "";

    private final String ONE_DAY_REQUEST = "http://api.openweathermap.org/data/2.5/weather?id=%d&appid=e397147f38c213ae53717fb01f417e20";
    private final String FIVE_DAYS_REQUEST = "http://api.openweathermap.org/data/2.5/forecast?id=%d&appid=e397147f38c213ae53717fb01f417e20";
    private final int SERVER_OK = 200;
    private final String RESPONSE = "cod";

    private final IBinder binder = new GetWeatherBinder();

    public interface DownloadWeatherListener {
        void onComplete(Weather weather);
    }

    public interface DownloadWeatherArrayListListener {
        void onComplete(ArrayList<Weather> weatherArrayList);
    }

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
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initPrefVariables(SharedPreferences sharedPrefMeasureSettings) {

        windSetting = sharedPrefMeasureSettings.getInt(SettingsConstants.KEY_WIND_MEASURE_SETTINGS, SettingsConstants.DEFAULT_WIND_SETTING);
        temperatureSetting = sharedPrefMeasureSettings.getInt(SettingsConstants.KEY_TEMPERATURE_MEASURE_SETTINGS, SettingsConstants.DEFAULT_TEMPERATURE_SETTING);
        pressureSetting = sharedPrefMeasureSettings.getInt(SettingsConstants.KEY_PRESSURE_MEASURE_SETTINGS, SettingsConstants.DEFAULT_PRESSURE_SETTING);

        humidityMeasure = getString(R.string.render_humidity_measure);

        switch (windSetting) {
            case SettingsConstants.FIRST_SETTING_CHECKED:
                windMeasure = getString(R.string.render_wind_measure_meters);
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                windMeasure = getString(R.string.render_wind_measure_kilometers);
                break;
        }

        switch (temperatureSetting) {
            case SettingsConstants.FIRST_SETTING_CHECKED:
                temperatureMeasure = getString(R.string.render_temperature_measure_celsius);
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                temperatureMeasure = getString(R.string.render_temperature_measure_fahrenheit);
                break;
        }

        switch (pressureSetting) {
            case SettingsConstants.FIRST_SETTING_CHECKED:
                pressureMeasure = getString(R.string.render_pressure_measure_mm);
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                pressureMeasure = getString(R.string.render_pressure_measure_hpa);
                break;
        }

    }


    private String getTemperature(double temp){

        switch (temperatureSetting) {
            case SettingsConstants.FIRST_SETTING_CHECKED:
                temp = temp - 273.15;
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                temp = 9 * (temp - 273.15) / 5 + 32;
                break;
        }
        long tempLong = Math.round(temp);

        return Long.toString(tempLong) + " " + temperatureMeasure;
    }

    private String getPressure(double pressure){

        switch (pressureSetting) {
            case SettingsConstants.FIRST_SETTING_CHECKED:
                pressure = pressure * 0.75006;
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                break;
        }
        long pressureLong = Math.round(pressure);

        return Long.toString(pressureLong) + " " + pressureMeasure;

    }

    private String getWindSpeed(double windSpeed){

        switch (windSetting) {
            case SettingsConstants.FIRST_SETTING_CHECKED:
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                windSpeed = windSpeed * 3.6;
                break;
        }
        long windSpeedLong = Math.round(windSpeed);

        return Long.toString(windSpeedLong) + " " + windMeasure;
    }

    private boolean isInRange(int deg, int start, int end) {
        return start <= deg && deg <= end;
    }

    private WindDirection gerWindDirection(int deg){
        WindDirection windDirection;

        if (isInRange(deg, 337, 360)) {
            windDirection = WindDirection.NORTH;
        } else if (isInRange(deg, 292, 336)) {
            windDirection = WindDirection.NORTH_WEST;
        } else if (isInRange(deg, 247, 291)) {
            windDirection = WindDirection.WEST;
        } else if (isInRange(deg, 202, 246)) {
            windDirection = WindDirection.SOUTH_WEST;
        } else if (isInRange(deg, 157, 201)) {
            windDirection = WindDirection.SOUTH;
        } else if (isInRange(deg, 112, 156)) {
            windDirection = WindDirection.SOUTH_EAST;
        } else if (isInRange(deg, 67, 111)) {
            windDirection = WindDirection.EAST;
        } else if (isInRange(deg, 22, 66)) {
            windDirection = WindDirection.NORTH_EAST;
        } else if (isInRange(deg, 0, 21)) {
            windDirection = WindDirection.NORTH;
        } else {
            windDirection = WindDirection.NO_DIRECTION;
        }

        return windDirection;
    }

    private WeatherPicture getIcon(String iconName){

        WeatherPicture weatherPicture;

        switch (iconName) {
            case "01d":
                weatherPicture = WeatherPicture.D01;
                break;
            case "01n":
                weatherPicture = WeatherPicture.N01;
                break;
            case "02d":
                weatherPicture = WeatherPicture.D02;
                break;
            case "02n":
                weatherPicture = WeatherPicture.N02;
                break;
            case "03d":
                weatherPicture = WeatherPicture.D03;
                break;
            case "03n":
                weatherPicture = WeatherPicture.N03;
                break;
            case "04d":
                weatherPicture = WeatherPicture.D04;
                break;
            case "04n":
                weatherPicture = WeatherPicture.N04;
                break;
            case "09d":
                weatherPicture = WeatherPicture.D09;
                break;
            case "09n":
                weatherPicture = WeatherPicture.N09;
                break;
            case "10d":
                weatherPicture = WeatherPicture.D10;
                break;
            case "10n":
                weatherPicture = WeatherPicture.N10;
                break;
            case "11d":
                weatherPicture = WeatherPicture.D11;
                break;
            case "11n":
                weatherPicture = WeatherPicture.N11;
                break;
            case "13d":
                weatherPicture = WeatherPicture.D13;
                break;
            case "13n":
                weatherPicture = WeatherPicture.N13;
                break;
            case "50d":
                weatherPicture = WeatherPicture.D50;
                break;
            case "50n":
                weatherPicture = WeatherPicture.N50;
                break;
            default:
                weatherPicture = WeatherPicture.NO_ICON;
                break;
        }
        return weatherPicture;
    }

    public void downloadWeather(int cityId, SharedPreferences sharedPrefMeasureSettings, DownloadWeatherListener downloadWeatherListener) {

        final DownloadWeatherListener onDownloadWeatherListener = downloadWeatherListener;

        initPrefVariables(sharedPrefMeasureSettings);

        String url = String.format(ONE_DAY_REQUEST, cityId);

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            final Handler handler = new Handler();

            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {

                final String answer = response.body().string();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            renderOneDayWeather(onDownloadWeatherListener, answer);
                    }
                });
            }

            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("GetWeather", "downloadWeather failed", e);
            }
        });

    }

    private void renderOneDayWeather(DownloadWeatherListener downloadWeatherListener, String answer) {

        try {
            JSONObject jsonObject = new JSONObject(answer);

            if (jsonObject.getInt(RESPONSE) == SERVER_OK) {

                JSONObject main = jsonObject.getJSONObject("main");

                String tempString = getTemperature(main.getDouble("temp"));

                String pressureString = getPressure(main.getDouble("pressure"));

                String humidityString = main.getString("humidity") + " " + humidityMeasure;

                JSONObject wind = jsonObject.getJSONObject("wind");

                String windSpeedString = getWindSpeed(wind.getDouble("speed"));

                WindDirection windDirection;

                if(wind.has("deg")){
                    windDirection = gerWindDirection(wind.getInt("deg"));
                }else {
                    windDirection = WindDirection.NO_DIRECTION;
                }

                JSONObject weatherDetails = jsonObject.getJSONArray("weather").getJSONObject(0);

                WeatherPicture weatherPicture;

                if(weatherDetails.has("icon")) {
                    weatherPicture = getIcon(weatherDetails.getString("icon"));
                }else {
                    weatherPicture = WeatherPicture.NO_ICON;
                }

                Weather weather = new Weather(new GregorianCalendar(), weatherPicture, tempString, windSpeedString,
                        windDirection, pressureString, humidityString);

                downloadWeatherListener.onComplete(weather);

            } else {
                Log.e("GetWeather", "renderOneDayWeather failed");
            }

        } catch (Exception e) {
            Log.e("GetWeather", "renderOneDayWeather failed", e);
        }

    }

    public void downloadWeatherArrayList(int cityId, SharedPreferences sharedPrefMeasureSettings,
                                DownloadWeatherArrayListListener downloadWeatherArrayListListener) {

        final DownloadWeatherArrayListListener onDownloadWeatherArrayListListener = downloadWeatherArrayListListener;

        initPrefVariables(sharedPrefMeasureSettings);

        String url = String.format(FIVE_DAYS_REQUEST, cityId);

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            final Handler handler = new Handler();

            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {

                final String answer = response.body().string();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        renderFiveDaysWeather(onDownloadWeatherArrayListListener, answer);
                    }
                });
            }

            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("GetWeather", "downloadWeatherArrayList failed", e);
            }
        });

    }

    private void renderFiveDaysWeather(DownloadWeatherArrayListListener downloadWeatherArrayListListener, String answer) {

        try {
            JSONObject jsonObject = new JSONObject(answer);

            if (jsonObject.getInt(RESPONSE) == SERVER_OK) {

                JSONArray listArray = jsonObject.getJSONArray("list");

                ArrayList<Weather> weatherArrayList = new ArrayList<>();

                for(int i=0; i < listArray.length(); i++){

                    JSONObject currentJsonObject = listArray.getJSONObject(i);

                    Date date = new Date(currentJsonObject.getLong("dt")*1000);

                    Calendar calendar = new GregorianCalendar(date.getYear()+1900,date.getMonth(),date.getDate());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    String time = dateFormat.format(date);

                    JSONObject main = currentJsonObject.getJSONObject("main");

                    String tempString = getTemperature(main.getDouble("temp"));

                    String pressureString = getPressure(main.getDouble("pressure"));

                    String humidityString = main.getString("humidity") + " " + humidityMeasure;

                    JSONObject wind = currentJsonObject.getJSONObject("wind");

                    String windSpeedString = getWindSpeed(wind.getDouble("speed"));

                    WindDirection windDirection;

                    if(wind.has("deg")){
                        windDirection = gerWindDirection(wind.getInt("deg"));
                    }else {
                        windDirection = WindDirection.NO_DIRECTION;
                    }

                    JSONObject weatherDetails = currentJsonObject.getJSONArray("weather").getJSONObject(0);

                    WeatherPicture weatherPicture;

                    if(weatherDetails.has("icon")) {
                        weatherPicture = getIcon(weatherDetails.getString("icon"));
                    }else {
                        weatherPicture = WeatherPicture.NO_ICON;
                    }

                    weatherArrayList.add(new Weather(calendar, weatherPicture, tempString, windSpeedString,
                            windDirection, pressureString, humidityString, time));
                }

                downloadWeatherArrayListListener.onComplete(weatherArrayList);

            } else {
                Log.e("GetWeather", "renderFiveDaysWeather failed");
            }

        } catch (Exception e) {
            Log.e("GetWeather", "renderFiveDaysWeather failed", e);
        }

    }



}
