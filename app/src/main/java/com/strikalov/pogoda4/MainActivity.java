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

import com.strikalov.pogoda4.activities.AboutDeveloperActivity;
import com.strikalov.pogoda4.activities.ChangeBackgroundActivity;
import com.strikalov.pogoda4.activities.FeedbackActivity;
import com.strikalov.pogoda4.activities.HumiditySensorActivity;
import com.strikalov.pogoda4.activities.SecondActivity;
import com.strikalov.pogoda4.activities.SettingsActivity;
import com.strikalov.pogoda4.activities.TemperatureSensorActivity;
import com.strikalov.pogoda4.databases.BackgroundPictureDatabaseHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner citySpinner;
    private final String SQL_EXCEPTION_TAG = "sql_exception";
    private ImageView backgroundPicture;

    private SharedPreferences sharedPref;

    private final String CITY_INDEX_PREFERENCE = "city_index_preference";
    private final String KEY_CITY_INDEX_PREFERENCE = "key_city_index";
    private final int DEFAULT_CITY_INDEX = 0;

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

        sharedPref = getSharedPreferences(CITY_INDEX_PREFERENCE, MODE_PRIVATE);
        int loadedCityIndex = sharedPref.getInt(KEY_CITY_INDEX_PREFERENCE, DEFAULT_CITY_INDEX);

        citySpinner = findViewById(R.id.city_spinner);
        citySpinner.setSelection(loadedCityIndex);

        backgroundPicture = findViewById(R.id.background_picture);
        updateBackgroundImage(backgroundPicture);

    }

    @Override
    public void onRestart() {
        super.onRestart();

        int loadedCityIndex = sharedPref.getInt(KEY_CITY_INDEX_PREFERENCE, DEFAULT_CITY_INDEX);
        citySpinner.setSelection(loadedCityIndex);

        updateBackgroundImage(backgroundPicture);
    }

    public void onClickShowWeather(View view) {

        int cityIndex = citySpinner.getSelectedItemPosition();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_CITY_INDEX_PREFERENCE, cityIndex);
        editor.apply();

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(SecondActivity.CITY_QUERY, cityIndex);
        startActivity(intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int cityIndex = citySpinner.getSelectedItemPosition();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_CITY_INDEX_PREFERENCE, cityIndex);
        editor.apply();
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

            SQLiteOpenHelper backgroundPictureDatabaseHelper = new BackgroundPictureDatabaseHelper(MainActivity.this, getResources());
            SQLiteDatabase db = null;
            Cursor cursor = null;

            try {

                db = backgroundPictureDatabaseHelper.getReadableDatabase();
                cursor = db.query("PICTURE", new String[]{"IMAGE_RESOURCE_ID"}, "SELECTED = 1",
                        null, null, null, null);

                if (cursor.moveToFirst()) {
                    imageIndex = cursor.getInt(0);
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
