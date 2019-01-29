package com.strikalov.pogoda4.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.constants.SettingsConstants;
import com.strikalov.pogoda4.databases.SelectedCitiesDatabaseHelper;
import com.strikalov.pogoda4.fragments.SelectedCityFragment;
import com.strikalov.pogoda4.fragments.ShowDownloadProgressFragment;
import com.strikalov.pogoda4.models.SelectedCityData;
import com.strikalov.pogoda4.services.GetWeatherService;

import java.util.ArrayList;

public class SelectedCitiesActivity extends AppCompatActivity {

    private static final String SQL_EXCEPTION_TAG = "sql_exception";
    private static final int CURSOR_IS_EMPTY = 0;

    private Button buttonRefresh;
    private Button buttonAddCity;

    private ServiceConnection connection;
    private GetWeatherService service;
    private boolean bind;

    private SharedPreferences sharedPrefMeasureSettings;

    private ShowDownloadProgressFragment showDownloadProgressFormDatabaseFragment;
    private ShowDownloadProgressFragment showDownloadProgressFormInternetFragment;
    private ShowDownloadProgressFragment showDatabaseEmptyFragment;

    private SelectedCityFragment selectedCityFragment;

    private volatile ArrayList<SelectedCityData> selectedCityDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_cities);

        Toolbar toolbar = findViewById(R.id.toolbar_selected_cities);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.selected_cities));

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = ((GetWeatherService.GetWeatherBinder) binder).getService();
                bind = true;
                updateSelectedCities();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bind = false;
            }
        };

        sharedPrefMeasureSettings = getSharedPreferences(SettingsConstants.MEASURE_SETTINGS, MODE_PRIVATE);

        buttonRefresh = findViewById(R.id.button_selected_city_refresh);
        buttonAddCity = findViewById(R.id.button_selected_city_add);

        disableButtons();

        String showDownloadDatabseText = getString(R.string.show_download_databse_text);
        showDownloadProgressFormDatabaseFragment = ShowDownloadProgressFragment.newInstance(showDownloadDatabseText);

        String showDatabaseEmptyText = getString(R.string.selected_cities_emppty);
        showDatabaseEmptyFragment = ShowDownloadProgressFragment.newInstance(showDatabaseEmptyText);

        String showInternetDownloadText = getString(R.string.show_download_progress);
        showDownloadProgressFormInternetFragment = ShowDownloadProgressFragment.newInstance(showInternetDownloadText);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.selected_cities_fragment_container, showDownloadProgressFormDatabaseFragment);
        fragmentTransaction.commit();

        downloadSelectedCities();

    }

    @Override
    public void onRestart() {
        super.onRestart();

        disableButtons();

        selectedCityDataList.clear();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.selected_cities_fragment_container, showDownloadProgressFormDatabaseFragment);
        fragmentTransaction.commit();

        downloadSelectedCities();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind) {
            unbindService(connection);
            bind = false;
        }
    }

    private void disableButtons(){
        buttonRefresh.setEnabled(false);
        buttonAddCity.setEnabled(false);
    }

    private void enableButtons(){
        buttonRefresh.setEnabled(true);
        buttonAddCity.setEnabled(true);
    }

    public void onStartSearchCityInData(View view){
        Intent intent = new Intent(this, SearchCityInDataActivity.class);
        startActivity(intent);
    }

    public void onClickUpdateSelectedCities(View view){

        if(bind){
            service.initPrefVariables(sharedPrefMeasureSettings);
            service.downloadTemperatureGroup(selectedCityDataList, new GetWeatherService.DownloadTemperatureGroupListener() {
                @Override
                public void onComplete() {
                    selectedCityDataList.clear();
                    downloadSelectedCities();
                }
            });
        }else {

            Intent intent = new Intent(getBaseContext(), GetWeatherService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);

        }

        disableButtons();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.selected_cities_fragment_container, showDownloadProgressFormInternetFragment);
        fragmentTransaction.commit();

    }

    private void updateSelectedCities(){

        //работает только до 20 городов, т.к. это лимит для оптового запроса на сервер, если городов больше
        //нужно писать улучшенный метод с разбивкой на части

        if(bind){
            service.initPrefVariables(sharedPrefMeasureSettings);
            service.downloadTemperatureGroup(selectedCityDataList, new GetWeatherService.DownloadTemperatureGroupListener() {
                @Override
                public void onComplete() {
                    selectedCityDataList.clear();
                    downloadSelectedCities();
                }
            });
        }


    }

    private void downloadSelectedCities(){

        DownloadSelectedCitiesTask downloadSelectedCitiesTask = new DownloadSelectedCitiesTask();
        downloadSelectedCitiesTask.execute();

    }


    private class DownloadSelectedCitiesTask extends AsyncTask<Void, Void, Boolean>{

        private boolean baseIsEmpty;

        @Override
        protected Boolean doInBackground(Void... voids){

            SQLiteOpenHelper selectedCitiesDatabaseHelper = new SelectedCitiesDatabaseHelper(
                    SelectedCitiesActivity.this);
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try{

                db = selectedCitiesDatabaseHelper.getReadableDatabase();

                cursor = db.query(SelectedCitiesDatabaseHelper.TABLE_SELECTED_CITIES,
                        new String[]{SelectedCitiesDatabaseHelper.COLUMN_CITY_ID,
                                SelectedCitiesDatabaseHelper.COLUMN_CITY_NAME,
                        SelectedCitiesDatabaseHelper.COLUMN_DATE,
                        SelectedCitiesDatabaseHelper.COLUMN_TEMPERATURE},
                        null, null, null, null,
                        SelectedCitiesDatabaseHelper.COLUMN_CITY_NAME + " ASC");

                if(cursor.getCount() == CURSOR_IS_EMPTY) {

                    baseIsEmpty = true;

                }else {

                    baseIsEmpty = false;

                    while (cursor.moveToNext()){

                        String cityId = cursor.getString(cursor.getColumnIndex(SelectedCitiesDatabaseHelper.COLUMN_CITY_ID));
                        String cityName = cursor.getString(cursor.getColumnIndex(SelectedCitiesDatabaseHelper.COLUMN_CITY_NAME));
                        String date = cursor.getString(cursor.getColumnIndex(SelectedCitiesDatabaseHelper.COLUMN_DATE));
                        String temperature = cursor.getString(cursor.getColumnIndex(SelectedCitiesDatabaseHelper.COLUMN_TEMPERATURE));

                        selectedCityDataList.add(new SelectedCityData(cityId, cityName, date, temperature));

                    }

                }

                return true;


            } catch (SQLiteException e) {
                Log.e(SQL_EXCEPTION_TAG, "Database SelectedCities error", e);
                return false;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (db != null) {
                    db.close();
                }
            }

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {

                if(baseIsEmpty){

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.selected_cities_fragment_container, showDatabaseEmptyFragment);
                    fragmentTransaction.commit();

                    buttonAddCity.setEnabled(true);

                }else {

                    selectedCityFragment = SelectedCityFragment.newInstance(selectedCityDataList);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.selected_cities_fragment_container, selectedCityFragment);
                    fragmentTransaction.commit();

                    enableButtons();

                }

            }
        }

    }

}
