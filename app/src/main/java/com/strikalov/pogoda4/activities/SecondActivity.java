package com.strikalov.pogoda4.activities;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.constants.SettingsConstants;
import com.strikalov.pogoda4.fragments.FiveDaysWeatherFragment;
import com.strikalov.pogoda4.fragments.OneDayWeatherFragment;
import com.strikalov.pogoda4.fragments.ShowDownloadProgressFragment;
import com.strikalov.pogoda4.models.Weather;
import com.strikalov.pogoda4.services.GetWeatherService;

import java.util.ArrayList;


public class SecondActivity extends AppCompatActivity {

    public static final String INTENT_CITY_INDEX = "CityQuery";
    public static final String INTENT_CITY_NAME = "CityQueryName";

    private String cityIndex;
    private String cityName;

    private ServiceConnection connection;
    private GetWeatherService service;
    private boolean bind;

    private boolean isGetWeather = false;
    private boolean isOneDayChecked = true;
    private boolean isGetWeatherArrayList = false;
    private boolean isFiveDaysChecked = false;

    private OneDayWeatherFragment oneDayWeatherFragment;
    private FiveDaysWeatherFragment fiveDaysWeatherFragment;
    private ShowDownloadProgressFragment showDownloadProgressFragment;

    private SharedPreferences sharedPrefMeasureSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar_second);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = ((GetWeatherService.GetWeatherBinder) binder).getService();
                bind = true;
                initFragments(cityIndex, sharedPrefMeasureSettings);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bind = false;
            }
        };

        if(getIntent() != null && getIntent().getExtras()!= null){

            cityIndex = getIntent().getExtras().getString(INTENT_CITY_INDEX);
            cityName = getIntent().getExtras().getString(INTENT_CITY_NAME);

            setTitle(cityName);

            Intent intent = new Intent(getBaseContext(), GetWeatherService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);

            sharedPrefMeasureSettings = getSharedPreferences(SettingsConstants.MEASURE_SETTINGS, MODE_PRIVATE);

            String showDownloadText = getString(R.string.show_download_progress);
            showDownloadProgressFragment = ShowDownloadProgressFragment.newInstance(showDownloadText);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, showDownloadProgressFragment);
            fragmentTransaction.commit();

        }else {
            finish();
        }
    }

    private void initFragments(String cityIndex, SharedPreferences sharedPrefMeasureSettings){
        if(bind){

            service.initPrefVariables(sharedPrefMeasureSettings);

            service.downloadWeather(cityIndex, new GetWeatherService.DownloadWeatherListener() {
                @Override
                public void onComplete(Weather weather) {
                    isGetWeather = true;
                    oneDayWeatherFragment = OneDayWeatherFragment.newInstance(weather);

                    if(isOneDayChecked) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, oneDayWeatherFragment);
                        fragmentTransaction.commit();
                    }
                }
            });

            service.downloadWeatherArrayList(cityIndex, new GetWeatherService.DownloadWeatherArrayListListener() {
                @Override
                public void onComplete(ArrayList<Weather> weatherArrayList) {
                    isGetWeatherArrayList = true;
                    fiveDaysWeatherFragment = FiveDaysWeatherFragment.newInstance(weatherArrayList);

                    if(isFiveDaysChecked) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fiveDaysWeatherFragment);
                        fragmentTransaction.commit();
                    }
                }
            });

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id) {
            case R.id.one_day:
                item.setChecked(true);
                isOneDayChecked = true;
                isFiveDaysChecked = false;
                if(isGetWeather) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, oneDayWeatherFragment);
                    fragmentTransaction.commit();
                }else {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, showDownloadProgressFragment);
                    fragmentTransaction.commit();
                }
                return true;
            case R.id.five_days:
                item.setChecked(true);
                isOneDayChecked = false;
                isFiveDaysChecked = true;
                if(isGetWeatherArrayList) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fiveDaysWeatherFragment);
                    fragmentTransaction.commit();
                }else {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, showDownloadProgressFragment);
                    fragmentTransaction.commit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
