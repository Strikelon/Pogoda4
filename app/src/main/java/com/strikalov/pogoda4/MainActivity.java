package com.strikalov.pogoda4;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner citySpinner;
    private final String SQL_EXCEPTION_TAG = "sql_exception";
    private ImageView backgroundPicture;

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

        citySpinner = findViewById(R.id.city_spinner);

        backgroundPicture = findViewById(R.id.background_picture);
        updateBackgroundImage(backgroundPicture);

    }

    @Override
    public void onRestart() {
        super.onRestart();
        updateBackgroundImage(backgroundPicture);
    }

    public void onClickShowWeather(View view) {

        int cityIndex = citySpinner.getSelectedItemPosition();

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(SecondActivity.CITY_QUERY, cityIndex);
        startActivity(intent);

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

            try {

                SQLiteDatabase db = backgroundPictureDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("PICTURE", new String[]{"IMAGE_RESOURCE_ID"}, "SELECTED = 1",
                        null, null, null, null);

                if (cursor.moveToFirst()) {
                    imageIndex = cursor.getInt(0);
                }

                cursor.close();
                db.close();

                return imageIndex;

            } catch (SQLiteException e) {
                Log.e(SQL_EXCEPTION_TAG, "Database unavailable");
                return -1;
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
