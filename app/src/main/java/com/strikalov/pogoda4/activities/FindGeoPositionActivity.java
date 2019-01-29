package com.strikalov.pogoda4.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.constants.CityPreferenceConstants;
import com.strikalov.pogoda4.databases.SelectedCitiesDatabaseHelper;
import com.strikalov.pogoda4.fragments.ShowDownloadProgressFragment;
import com.strikalov.pogoda4.services.GetWeatherService;

public class FindGeoPositionActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final String SQL_EXCEPTION_TAG = "sql_exception";
    private static final String NO_DATA = "n/a";
    private static final int CURSOR_IS_EMPTY = 0;
    public static boolean geolocationEnabled;

    private ShowDownloadProgressFragment searchSatelliteFragment;
    private ShowDownloadProgressFragment serverConnectionFragment;
    private ShowDownloadProgressFragment notAvailableFragment;
    private ShowDownloadProgressFragment satelliteNoPermissionFragment;

    private String lat;
    private String lon;
    private SharedPreferences sharedPrefCity;

    private ServiceConnection connection;
    private GetWeatherService service;
    private boolean bind;

    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_geo_position);

        Toolbar toolbar = findViewById(R.id.toolbar_find_geo_position);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.find_geo_position);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = ((GetWeatherService.GetWeatherBinder) binder).getService();
                bind = true;
                updateTextView(lat, lon);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bind = false;
            }
        };

        sharedPrefCity = getSharedPreferences(CityPreferenceConstants.CITY_INDEX_PREFERENCE, MODE_PRIVATE);

        String searchSatelliteString = getString(R.string.search_satellite);
        searchSatelliteFragment = ShowDownloadProgressFragment.newInstance(searchSatelliteString);
        String serverConnectionString = getString(R.string.after_satellite_server_connections);
        serverConnectionFragment = ShowDownloadProgressFragment.newInstance(serverConnectionString);
        String notAvailableString = getString(R.string.search_satellite_not_available);
        notAvailableFragment = ShowDownloadProgressFragment.newInstance(notAvailableString);
        String satelliteNoPermissionString = getString(R.string.satellite_no_permission);
        satelliteNoPermissionFragment = ShowDownloadProgressFragment.newInstance(satelliteNoPermissionString);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.find_geo_position_fragment_container, satelliteNoPermissionFragment);
        fragmentTransaction.commit();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.find_geo_position_fragment_container, satelliteNoPermissionFragment);
        fragmentTransaction.commit();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind) {
            unbindService(connection);
            bind = false;
        }
    }

    private void updateTextView(String lat, String lon){
        if(bind){

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.find_geo_position_fragment_container, serverConnectionFragment);
            fragmentTransaction.commit();

            service.downloadCityFormGeoPosition(lat, lon, new GetWeatherService.DownloadCityFromGeoListener() {
                @Override
                public void onComplete(String cityId, String cityName) {

                    sharedPrefCity.edit().putString(CityPreferenceConstants.KEY_CITY_INDEX_PREFERENCE, cityId).apply();
                    sharedPrefCity.edit().putString(CityPreferenceConstants.KEY_CITY_NAME_PREFERENCE, cityName).apply();

                    InsertCityInSelectedCitiesDatabaseTask insertCityInSelectedCitiesDatabaseTask =
                            new InsertCityInSelectedCitiesDatabaseTask(cityId, cityName);
                    insertCityInSelectedCitiesDatabaseTask.execute();

                }
            });

        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    private void checkLocationServiceEnabled(){

        geolocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        buildAlertMessageNoLocationService(geolocationEnabled);
    }

    private void buildAlertMessageNoLocationService(boolean networkEnabled){

        String msg = getString(R.string.ask_geo_message);

        if (!networkEnabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(msg)
                    .setPositiveButton(R.string.button_geo_turn_on, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton(R.string.button_geo_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;

        } else {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            checkLocationServiceEnabled();
            provider = locationManager.getBestProvider(criteria, true);
            if (provider != null) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.find_geo_position_fragment_container, searchSatelliteFragment);
                fragmentTransaction.commit();

                locationManager.requestLocationUpdates(provider, 2000, 10, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitudeD = Math.round(location.getLatitude() * 100.0) / 100.0;
                        String latitude = Double.toString(latitudeD);
                        double longitudeD = Math.round(location.getLongitude() * 100.0) / 100.0;
                        String longitude = Double.toString(longitudeD);
                        lat = latitude;
                        lon = longitude;
                        locationManager.removeUpdates(this);
                        Intent intent = new Intent(getBaseContext(), GetWeatherService.class);
                        bindService(intent, connection, BIND_AUTO_CREATE);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });
            }else {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.find_geo_position_fragment_container, notAvailableFragment);
                fragmentTransaction.commit();

            }
        }
    }



    private class InsertCityInSelectedCitiesDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        private String cityId;
        private String cityName;

        public InsertCityInSelectedCitiesDatabaseTask(String cityId, String cityName){
            this.cityId = cityId;
            this.cityName = cityName;
        }

        @Override
        protected Boolean doInBackground(Void... voids){

            SQLiteOpenHelper selectedCitiesDatabaseHelper = new SelectedCitiesDatabaseHelper(
                    FindGeoPositionActivity.this);
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
                Toast.makeText(FindGeoPositionActivity.this, R.string.city_was_find, Toast.LENGTH_SHORT).show();
                finish();
            }

        }

    }



}
