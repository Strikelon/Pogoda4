package com.strikalov.pogoda4.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ibm.icu.text.Transliterator;
import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.activities.SearchCityInDataActivity;
import com.strikalov.pogoda4.constants.SettingsConstants;
import com.strikalov.pogoda4.databases.SelectedCitiesDatabaseHelper;
import com.strikalov.pogoda4.models.SelectedCityData;
import com.strikalov.pogoda4.models.Weather;
import com.strikalov.pogoda4.models.WeatherPicture;
import com.strikalov.pogoda4.models.WindDirection;
import com.strikalov.pogoda4.pojogson.ForecastRequest;
import com.strikalov.pogoda4.pojogson.GeoPositionRequest;
import com.strikalov.pogoda4.pojogson.GroupTemperatureRequest;
import com.strikalov.pogoda4.pojogson.TemperatureRequest;
import com.strikalov.pogoda4.pojogson.WeatherRequest;
import com.strikalov.pogoda4.interfaces.OpenWeather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetWeatherService extends Service {

    private int windSetting;
    private int temperatureSetting;
    private int pressureSetting;

    private String windMeasure = "";
    private String temperatureMeasure = "";
    private String pressureMeasure = "";
    private String humidityMeasure = "";

    private final String URL_REQUEST = "http://api.openweathermap.org/";
    private final String KEY_API = "e397147f38c213ae53717fb01f417e20";
    private static final String SQL_EXCEPTION_TAG = "sql_exception";

    private final String tag ="GetWeather";

    //Для обновления базы данных при запросе погоды для одного города
    private String downloadWeatherCityindex;
    private String downloadWeatherDate;
    private String downloadWeatherTemperature;

    //Для группового запроса
    private ArrayList<SelectedCityData> selectedCityDataList;

    private final IBinder binder = new GetWeatherBinder();

    public interface DownloadCityFromGeoListener{
        void onComplete(String cityId, String cityName);
    }

    public interface DownloadWeatherListener {
        void onComplete(Weather weather);
    }

    public interface DownloadWeatherArrayListListener {
        void onComplete(ArrayList<Weather> weatherArrayList);
    }

    public interface DownloadTemperatureGroupListener{
        void onComplete();
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

    public void initPrefVariables(SharedPreferences sharedPrefMeasureSettings) {

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

    private Weather weatherRequestRender(WeatherRequest weatherRequest){

        Date date = new Date(weatherRequest.getDt()*1000);

        Calendar calendar = new GregorianCalendar(date.getYear()+1900,date.getMonth(),date.getDate());

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(date);

        String tempString = getTemperature(weatherRequest.getTemp());

        String pressureString = getPressure(weatherRequest.getPressure());

        String humidityString = weatherRequest.getHumidity() + " " + humidityMeasure;

        String windSpeedString = getWindSpeed(weatherRequest.getSpeed());

        WindDirection windDirection = gerWindDirection((int)weatherRequest.getDeg());

        WeatherPicture weatherPicture = getIcon(weatherRequest.getIcon());

        return new Weather(calendar, weatherPicture, tempString, windSpeedString,
                windDirection, pressureString, humidityString, time);

    }

    public void downloadCityFormGeoPosition(String lat, String lon, DownloadCityFromGeoListener downloadCityFromGeoListener){

        final DownloadCityFromGeoListener onDownloadCityFromGeoListener = downloadCityFromGeoListener;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_REQUEST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeather openWeather = retrofit.create(OpenWeather.class);

        openWeather.loadFromGeoPosition(lat, lon, KEY_API)
                .enqueue(new Callback<GeoPositionRequest>() {

                    final Handler handler = new Handler();

                    @Override
                    public void onResponse(Call<GeoPositionRequest> call, Response<GeoPositionRequest> response) {
                        if(response.body() != null){
                            final GeoPositionRequest geoPositionRequest = response.body();

                            Transliterator transliterator = Transliterator.getInstance("Latin-Russian/BGN");

                            final String cityName = transliterator.transliterate(geoPositionRequest.getCityName());

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    onDownloadCityFromGeoListener.onComplete(
                                            geoPositionRequest.getCityId(), cityName);
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<GeoPositionRequest> call, Throwable t) {
                        Log.e(tag, "downloadFormGeo failed", t);
                    }
                });

    }

    public void downloadWeather(String cityIndex, DownloadWeatherListener downloadWeatherListener) {

        final DownloadWeatherListener onDownloadWeatherListener = downloadWeatherListener;

        downloadWeatherCityindex = cityIndex;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_REQUEST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeather openWeather = retrofit.create(OpenWeather.class);

        openWeather.loadWeather(cityIndex, KEY_API)
                .enqueue(new Callback<WeatherRequest>() {
                    final Handler handler = new Handler();

                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            final WeatherRequest weatherRequest = response.body();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    renderOneDayWeather(onDownloadWeatherListener, weatherRequest);
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequest> call,
                                          @NonNull Throwable throwable) {
                        Log.e(tag, "downloadWeather failed", throwable);
                    }
                });

    }

    private void renderOneDayWeather(DownloadWeatherListener downloadWeatherListener, WeatherRequest weatherRequest) {

        Weather weather = weatherRequestRender(weatherRequest);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        downloadWeatherDate = dateFormat.format(new Date());
        downloadWeatherTemperature = weather.getTemperature();
        updateSelectedCityDatabase();

        downloadWeatherListener.onComplete(weather);

    }

    public void downloadTemperatureGroup(ArrayList<SelectedCityData> selectedCityList, final DownloadTemperatureGroupListener downloadTemperatureGroupListener){

        selectedCityDataList = selectedCityList;

        StringBuilder cityIdRequest = new StringBuilder();

        for(SelectedCityData selectedCityData : selectedCityList){

            cityIdRequest.append(selectedCityData.getCityId()).append(",");

        }

        String idsString = cityIdRequest.substring(0, cityIdRequest.length()-1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_REQUEST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeather openWeather = retrofit.create(OpenWeather.class);

        openWeather.loadGroup(idsString, KEY_API).
                enqueue(new Callback<GroupTemperatureRequest>() {

                    final Handler handler = new Handler();

                    @Override
                    public void onResponse(Call<GroupTemperatureRequest> call, final Response<GroupTemperatureRequest> response) {

                        if (response.body() != null) {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    renderTemperatureGroup(response.body(), downloadTemperatureGroupListener);
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<GroupTemperatureRequest> call, Throwable t) {
                        Log.e(tag, "downloadGroup failed", t);
                    }
                });

    }

    private void renderTemperatureGroup(GroupTemperatureRequest groupTemperatureRequest, DownloadTemperatureGroupListener downloadTemperatureGroupListener){

        TemperatureRequest[] temperatureRequests = groupTemperatureRequest.getTemperatureRequests();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String currentDate = dateFormat.format(new Date());

        for(int i=0; i<selectedCityDataList.size(); i++ ){

            SelectedCityData currentsSelectedCityData = selectedCityDataList.get(i);

            String newTemperature = getTemperature(temperatureRequests[i].getMain().getTemp());

            currentsSelectedCityData.setDate(currentDate);
            currentsSelectedCityData.setTemperature(newTemperature);

        }

        updateSelectedCitiesTemperaturesDatabase(downloadTemperatureGroupListener);

    }

    private void updateSelectedCitiesTemperaturesDatabase(DownloadTemperatureGroupListener downloadTemperatureGroupListener){

        UpdateSelectedCityTemperaturesTask updateSelectedCityTemperaturesTask
                = new UpdateSelectedCityTemperaturesTask(downloadTemperatureGroupListener);
        updateSelectedCityTemperaturesTask.execute();

    }

    private class UpdateSelectedCityTemperaturesTask extends AsyncTask<Void, Void, Boolean> {

        private DownloadTemperatureGroupListener downloadTemperatureGroupListener;

        public UpdateSelectedCityTemperaturesTask(DownloadTemperatureGroupListener downloadTemperatureGroupListener){

            this.downloadTemperatureGroupListener = downloadTemperatureGroupListener;

        }

        @Override
        protected Boolean doInBackground(Void... voids){

            SQLiteOpenHelper selectedCitiesDatabaseHelper = new SelectedCitiesDatabaseHelper(
                    getApplicationContext());
            SQLiteDatabase db = null;

            try{

                db = selectedCitiesDatabaseHelper.getWritableDatabase();

                for(SelectedCityData selectedCityData : selectedCityDataList) {

                    ContentValues selectedCityValues = new ContentValues();
                    selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_DATE, selectedCityData.getDate());
                    selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_TEMPERATURE, selectedCityData.getTemperature());

                    db.update(SelectedCitiesDatabaseHelper.TABLE_SELECTED_CITIES, selectedCityValues,
                            SelectedCitiesDatabaseHelper.COLUMN_CITY_ID + " = ?",
                            new String[]{selectedCityData.getCityId()});

                }

                return true;


            } catch (SQLiteException e) {
                Log.e(SQL_EXCEPTION_TAG, "Database SelectedCities error", e);
                return false;
            } finally {
                if (db != null) {
                    db.close();
                }
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {

                downloadTemperatureGroupListener.onComplete();

            }
        }

    }



    public void downloadWeatherArrayList(String cityIndex,
                                DownloadWeatherArrayListListener downloadWeatherArrayListListener) {

        final DownloadWeatherArrayListListener onDownloadWeatherArrayListListener = downloadWeatherArrayListListener;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_REQUEST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeather openWeather = retrofit.create(OpenWeather.class);

        openWeather.loadForecast(cityIndex, KEY_API)
                .enqueue(new Callback<ForecastRequest>() {

                    final Handler handler = new Handler();

                    @Override
                    public void onResponse(@NonNull Call<ForecastRequest> call,
                                           @NonNull Response<ForecastRequest> response) {
                        if (response.body() != null){

                            final ForecastRequest forecastRequest = response.body();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    renderFiveDaysWeather(onDownloadWeatherArrayListListener, forecastRequest);
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ForecastRequest> call,
                                          @NonNull Throwable throwable) {
                        Log.e(tag, "downloadWeatherArrayList failed", throwable);
                    }
                });


    }

    private void renderFiveDaysWeather(DownloadWeatherArrayListListener downloadWeatherArrayListListener,
                                       ForecastRequest forecastRequest) {

        ArrayList<Weather> weatherArrayList = new ArrayList<>();

        WeatherRequest[] weatherRequests = forecastRequest.getWeatherRequests();

        for(int i=0; i < weatherRequests.length; i++){

            weatherArrayList.add(weatherRequestRender(weatherRequests[i]));

        }

        downloadWeatherArrayListListener.onComplete(weatherArrayList);

    }

    private void updateSelectedCityDatabase(){

        UpdateSelectedCityDatabaseTask updateSelectedCityDatabaseTask = new UpdateSelectedCityDatabaseTask();
        updateSelectedCityDatabaseTask.execute();

    }


    private class UpdateSelectedCityDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids){

            SQLiteOpenHelper selectedCitiesDatabaseHelper = new SelectedCitiesDatabaseHelper(
                    getApplicationContext());
            SQLiteDatabase db = null;

            try{

                db = selectedCitiesDatabaseHelper.getWritableDatabase();

                ContentValues selectedCityValues = new ContentValues();
                selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_DATE, downloadWeatherDate);
                selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_TEMPERATURE, downloadWeatherTemperature);

                db.update(SelectedCitiesDatabaseHelper.TABLE_SELECTED_CITIES, selectedCityValues,
                        SelectedCitiesDatabaseHelper.COLUMN_CITY_ID + " = ?",
                        new String[]{downloadWeatherCityindex});

                return true;


            } catch (SQLiteException e) {
                Log.e(SQL_EXCEPTION_TAG, "Database SelectedCities error", e);
                return false;
            } finally {
                if (db != null) {
                    db.close();
                }
            }

        }

    }

}
