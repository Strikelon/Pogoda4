package com.strikalov.pogoda4;

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

    private ServiceConnection connection;
    private GetWeatherService service;
    private boolean bind = false;

    private OneDayWeatherFragment oneDayWeatherFragment;
    private SevenDaysWeatherFragment sevenDaysWeatherFragment;



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
                initFragments(cityIndex);
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

            Intent intent = new Intent(getBaseContext(), GetWeatherService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);

        }else {
            finish();
            return;
        }
    }

    private Weather getWeatherToday(int cityIndex){
        if(bind){
            return service.getWeatherToday(cityIndex);
        }else {
            return null;
        }
    }

    private ArrayList<Weather> getWeatherList(int cityIndex){
        if(bind){
            return service.getWeather(cityIndex);
        }else {
            return null;
        }
    }

    private void initFragments(int cityIndex){

        Weather weather = getWeatherToday(cityIndex);
        ArrayList<Weather> weatherList = getWeatherList(cityIndex);

        if(weather != null && weatherList != null) {

            oneDayWeatherFragment = OneDayWeatherFragment.newInstance(weather);
            sevenDaysWeatherFragment = SevenDaysWeatherFragment.newInstance(weatherList);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, oneDayWeatherFragment);
            fragmentTransaction.commit();
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
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container,oneDayWeatherFragment);
                fragmentTransaction1.commit();
                return true;
            case R.id.seven_days:
                item.setChecked(true);
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.fragment_container,sevenDaysWeatherFragment);
                fragmentTransaction2.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
