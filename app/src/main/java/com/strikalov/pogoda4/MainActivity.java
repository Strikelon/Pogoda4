package com.strikalov.pogoda4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.TextView;
import android.widget.Toast;

import com.strikalov.pogoda4.activities.AboutDeveloperActivity;
import com.strikalov.pogoda4.activities.ChangeBackgroundActivity;
import com.strikalov.pogoda4.activities.FeedbackActivity;
import com.strikalov.pogoda4.activities.FindGeoPositionActivity;
import com.strikalov.pogoda4.activities.HumiditySensorActivity;
import com.strikalov.pogoda4.activities.SearchCityInDataActivity;
import com.strikalov.pogoda4.activities.SecondActivity;
import com.strikalov.pogoda4.activities.SelectedCitiesActivity;
import com.strikalov.pogoda4.activities.SettingsActivity;
import com.strikalov.pogoda4.activities.TemperatureSensorActivity;
import com.strikalov.pogoda4.constants.CityPreferenceConstants;
import com.strikalov.pogoda4.databases.BackgroundPictureDatabaseHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String SQL_EXCEPTION_TAG = "sql_exception";
    private ImageView backgroundPicture;

    private SharedPreferences sharedPref;

    private String loadedCityIndex;
    private String loadedCityName;

    private TextView textViewCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textViewCityName = findViewById(R.id.main_city_name);

        sharedPref = getSharedPreferences(CityPreferenceConstants.CITY_INDEX_PREFERENCE, MODE_PRIVATE);

        loadedCityIndex = sharedPref.getString(CityPreferenceConstants.KEY_CITY_INDEX_PREFERENCE,
                CityPreferenceConstants.DEFAULT_CITY_INDEX);
        loadedCityName = sharedPref.getString(CityPreferenceConstants.KEY_CITY_NAME_PREFERENCE,
                CityPreferenceConstants.DEFAULT_CITY_NAME);

        textViewCityName.setText(loadedCityName);

        backgroundPicture = findViewById(R.id.background_picture);
        updateBackgroundImage(backgroundPicture);

    }

    @Override
    public void onRestart() {
        super.onRestart();

        loadedCityIndex = sharedPref.getString(CityPreferenceConstants.KEY_CITY_INDEX_PREFERENCE,
                CityPreferenceConstants.DEFAULT_CITY_INDEX);
        loadedCityName = sharedPref.getString(CityPreferenceConstants.KEY_CITY_NAME_PREFERENCE,
                CityPreferenceConstants.DEFAULT_CITY_NAME);

        textViewCityName.setText(loadedCityName);

        updateBackgroundImage(backgroundPicture);
    }

    public void onClickShowWeather(View view) {

        if(!loadedCityIndex.equals(CityPreferenceConstants.DEFAULT_CITY_INDEX)) {

            sharedPref.edit().putString(CityPreferenceConstants.KEY_CITY_INDEX_PREFERENCE, loadedCityIndex).apply();
            sharedPref.edit().putString(CityPreferenceConstants.KEY_CITY_NAME_PREFERENCE, loadedCityName).apply();

            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra(SecondActivity.INTENT_CITY_INDEX, loadedCityIndex);
            intent.putExtra(SecondActivity.INTENT_CITY_NAME, loadedCityName);
            startActivity(intent);

        }else {

            Toast.makeText(MainActivity.this, R.string.city_is_not_accepted ,Toast.LENGTH_SHORT).show();

        }

    }

    public void onClickShowSearchCityInData(View view){
        Intent intent = new Intent(this, SearchCityInDataActivity.class);
        startActivity(intent);
    }

    public void onClickStartFindGeoPosition(View view){
        Intent intent = new Intent(this, FindGeoPositionActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        sharedPref.edit().putString(CityPreferenceConstants.KEY_CITY_INDEX_PREFERENCE, loadedCityIndex).apply();
        sharedPref.edit().putString(CityPreferenceConstants.KEY_CITY_NAME_PREFERENCE, loadedCityName).apply();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;

        switch (id) {
            case R.id.nav_backgroud:
                intent = new Intent(this, ChangeBackgroundActivity.class);
                break;
            case R.id.nav_temperature_sensor:
                intent = new Intent(this, TemperatureSensorActivity.class);
                break;
            case R.id.nav_humidity_sensor:
                intent = new Intent(this, HumiditySensorActivity.class);
                break;
            case R.id.nav_about_developer:
                intent = new Intent(this, AboutDeveloperActivity.class);
                break;
            case R.id.nav_feedback:
                intent = new Intent(this, FeedbackActivity.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.nav_selected_cities:
                intent = new Intent(this, SelectedCitiesActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void updateBackgroundImage(ImageView backgroundPicture) {

        GetImageIdTask getImageIdTask = new GetImageIdTask(backgroundPicture);
        getImageIdTask.execute();

    }

    private class GetImageIdTask extends AsyncTask<Void, Void, Integer> {

        private final ImageView backgroundPicture;

        private GetImageIdTask(ImageView backgroundPicture) {
            this.backgroundPicture = backgroundPicture;
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            int imageIndex = -1;

            SQLiteOpenHelper backgroundPictureDatabaseHelper = new BackgroundPictureDatabaseHelper(MainActivity.this);
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {

                db = backgroundPictureDatabaseHelper.getReadableDatabase();
                cursor = db.query(BackgroundPictureDatabaseHelper.TABLE_PICTURE,
                        new String[]{BackgroundPictureDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID},
                        BackgroundPictureDatabaseHelper.COLUMN_SELECTED +" = 1",
                        null, null, null, null);

                if (cursor.moveToFirst()) {
                    imageIndex = cursor.getInt(cursor.getColumnIndex(BackgroundPictureDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID));
                }

                return imageIndex;

            } catch (SQLiteException e) {

                Log.e(SQL_EXCEPTION_TAG, "Database unavailable", e);
                return -1;

            }finally {

                if(cursor != null) {
                    cursor.close();
                }
                if(db != null) {
                    db.close();
                }

            }

        }

        @Override
        protected void onPostExecute(Integer imageId) {

            if (imageId != -1) {
                backgroundPicture.setImageResource(imageId);
            }

        }

    }


}
