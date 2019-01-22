package com.strikalov.pogoda4.activities;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.constants.CityPreferenceConstants;
import com.strikalov.pogoda4.databases.CityDatabaseHelper;
import com.strikalov.pogoda4.databases.SelectedCitiesDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchCityInDataActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;

    private List<String> cityNamesList = new ArrayList<>();

    private SharedPreferences sharedPrefCity;

    private Button buttonAcceptCity;

    private static final String SQL_EXCEPTION_TAG = "sql_exception";
    private static final String NO_DATA = "n/a";
    private static final int CURSOR_IS_EMPTY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city_in_data);

        Toolbar toolbar = findViewById(R.id.toolbar_search_city_in_data);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.search_city_in_data));

        buttonAcceptCity = findViewById(R.id.button_accept_city_in_data);

        sharedPrefCity = getSharedPreferences(CityPreferenceConstants.CITY_INDEX_PREFERENCE, MODE_PRIVATE);

        autoCompleteTextView = findViewById(R.id.auto_complete_text_search_city);
        autoCompleteTextView.setThreshold(1);

        downloadCityNamesToAutoCompleteTextView();
    }

    public void onClickAcceptCityPicture(View view){
        String name = autoCompleteTextView.getText().toString();
        int index = cityNamesList.indexOf(name);
        if(index != -1){
            AcceptCityTask acceptCityTask = new AcceptCityTask(index);
            acceptCityTask.execute();
            buttonAcceptCity.setEnabled(false);
        }else {
            Toast.makeText(this, R.string.city_is_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private class AcceptCityTask extends AsyncTask<Void, Void, Boolean> {

        private int cityIndex;
        private String cityId;
        private String cityName;

        public AcceptCityTask(int cityIndex) {
            this.cityIndex = cityIndex;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            SQLiteOpenHelper cityDatabaseHelper = new CityDatabaseHelper(
                    SearchCityInDataActivity.this);
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {

                db = cityDatabaseHelper.getReadableDatabase();
                cursor = db.query(CityDatabaseHelper.TABLE_CITIES,
                        new String[]{CityDatabaseHelper.COLUMN_CITY_ID, CityDatabaseHelper.COLUMN_CITY_NAME},
                        CityDatabaseHelper.COLUMN_ID + " = ?", new String[]{Integer.toString(cityIndex + 1)},
                        null, null, null);

                if (cursor.moveToFirst()) {

                    cityId = cursor.getString(cursor.getColumnIndex(CityDatabaseHelper.COLUMN_CITY_ID));
                    cityName = cursor.getString(cursor.getColumnIndex(CityDatabaseHelper.COLUMN_CITY_NAME));

                }
                return true;

            } catch (SQLiteException e) {
                Log.e(SQL_EXCEPTION_TAG, "Database Cities error", e);
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
        protected void onPostExecute(Boolean success){
            if(success){

                sharedPrefCity.edit().putString(CityPreferenceConstants.KEY_CITY_INDEX_PREFERENCE, cityId).apply();
                sharedPrefCity.edit().putString(CityPreferenceConstants.KEY_CITY_NAME_PREFERENCE, cityName).apply();

                InsertCityInSelectedCitiesDatabaseTask insertCityInSelectedCitiesDatabaseTask =
                        new InsertCityInSelectedCitiesDatabaseTask(cityId, cityName);
                insertCityInSelectedCitiesDatabaseTask.execute();
            }
        }

    }

    private class InsertCityInSelectedCitiesDatabaseTask extends AsyncTask<Void, Void, Boolean>{

        private String cityId;
        private String cityName;

        public InsertCityInSelectedCitiesDatabaseTask(String cityId, String cityName){
            this.cityId = cityId;
            this.cityName = cityName;
        }

        @Override
        protected Boolean doInBackground(Void... voids){

            SQLiteOpenHelper selectedCitiesDatabaseHelper = new SelectedCitiesDatabaseHelper(
                    SearchCityInDataActivity.this);
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try{

                db = selectedCitiesDatabaseHelper.getWritableDatabase();

                cursor = db.query(SelectedCitiesDatabaseHelper.TABLE_SELECTED_CITIES,
                        new String[]{SelectedCitiesDatabaseHelper.COLUMN_CITY_ID},
                        SelectedCitiesDatabaseHelper.COLUMN_CITY_ID + " = ?",
                        new String[]{cityId},
                        null, null, null);

                if(cursor.getCount() == CURSOR_IS_EMPTY) {

                    ContentValues selectedCityValues = new ContentValues();
                    selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_CITY_ID, cityId);
                    selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_CITY_NAME, cityName);
                    selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_DATE, NO_DATA);
                    selectedCityValues.put(SelectedCitiesDatabaseHelper.COLUMN_TEMPERATURE, NO_DATA);
                    db.insert(SelectedCitiesDatabaseHelper.TABLE_SELECTED_CITIES, null, selectedCityValues);

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
        protected void onPostExecute(Boolean success){

            if(success){
                Toast.makeText(SearchCityInDataActivity.this, R.string.city_is_accepted, Toast.LENGTH_SHORT).show();
                buttonAcceptCity.setEnabled(true);
                finish();
            }else {
                buttonAcceptCity.setEnabled(true);
            }

        }

    }


    private void downloadCityNamesToAutoCompleteTextView(){
        DownloadCityNamesFromSQLTask downloadCityNamesFromSQLTask = new DownloadCityNamesFromSQLTask();
        downloadCityNamesFromSQLTask.execute();
    }

    private class DownloadCityNamesFromSQLTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            SQLiteOpenHelper cityDatabaseHelper = new CityDatabaseHelper(
                    SearchCityInDataActivity.this);
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {

                db = cityDatabaseHelper.getReadableDatabase();
                cursor = db.query(CityDatabaseHelper.TABLE_CITIES, new String[]{CityDatabaseHelper.COLUMN_CITY_NAME},
                        null, null, null, null, null);

                while (cursor.moveToNext()) {

                    String cityName = cursor.getString(cursor.getColumnIndex(CityDatabaseHelper.COLUMN_CITY_NAME));

                    cityNamesList.add(cityName);

                }
                return true;

            } catch (SQLiteException e) {
                Log.e(SQL_EXCEPTION_TAG, "Database Cities error", e);
                return false;
            } finally {
                if(cursor != null) {
                    cursor.close();
                }
                if(db != null) {
                    db.close();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        SearchCityInDataActivity.this, android.R.layout.simple_dropdown_item_1line, cityNamesList);
                autoCompleteTextView.setAdapter(adapter);
            }
        }
    }

}
