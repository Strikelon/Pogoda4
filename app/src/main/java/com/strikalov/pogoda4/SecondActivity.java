package com.strikalov.pogoda4;

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

import java.util.ArrayList;


public class SecondActivity extends AppCompatActivity {

    public static final String CITY_QUERY = "CityQuery";

    private String[] cities;
    private int cityIndex;
    private int cityId;

    private ServiceConnection connection;
    private GetWeatherService service;
    private boolean bind = false;

    private boolean isGetWeather = false;
    private boolean isOneDayChecked = true;
    private boolean isGetWeatherArrayList = false;
    private boolean isFiveDaysChecked = false;

    private OneDayWeatherFragment oneDayWeatherFragment;
    private SevenDaysWeatherFragment sevenDaysWeatherFragment;

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
                initFragments(cityId, sharedPrefMeasureSettings);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bind = false;
            }
        };

        cities = getResources().getStringArray(R.array.cities);

        if(getIntent() != null && getIntent().getExtras()!= null){

            cityIndex = getIntent().getExtras().getInt(CITY_QUERY);

            setTitle(cities[cityIndex]);

            CityIdData cityIdData = new CityIdData();

            cityId = cityIdData.getId(cityIndex);

            Intent intent = new Intent(getBaseContext(), GetWeatherService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);

            sharedPrefMeasureSettings = getSharedPreferences(SettingsConstants.MEASURE_SETTINGS, MODE_PRIVATE);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, new ShowDownloadProgressFragment());
            fragmentTransaction.commit();

        }else {
            finish();
            return;
        }
    }

    private void initFragments(int cityId, SharedPreferences sharedPrefMeasureSettings){
        if(bind){

            service.downloadWeather(cityId, sharedPrefMeasureSettings, new GetWeatherService.DownloadWeatherListener() {
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

            service.downloadWeatherArrayList(cityId, sharedPrefMeasureSettings, new GetWeatherService.DownloadWeatherArrayListListener() {
                @Override
                public void onComplete(ArrayList<Weather> weatherArrayList) {
                    isGetWeatherArrayList = true;
                    sevenDaysWeatherFragment = SevenDaysWeatherFragment.newInstance(weatherArrayList);

                    if(isFiveDaysChecked) {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, sevenDaysWeatherFragment);
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
                    fragmentTransaction.replace(R.id.fragment_container, new ShowDownloadProgressFragment());
                    fragmentTransaction.commit();
                }
                return true;
            case R.id.five_days:
                item.setChecked(true);
                isOneDayChecked = false;
                isFiveDaysChecked = true;
                if(isGetWeatherArrayList) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, sevenDaysWeatherFragment);
                    fragmentTransaction.commit();
                }else {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new ShowDownloadProgressFragment());
                    fragmentTransaction.commit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
