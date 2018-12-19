package com.strikalov.pogoda4;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class SecondActivity extends AppCompatActivity {

    public static final String CITY_QUERY = "CityQuery";

    private String[] cities;
    private int cityIndex;

    private OneDayWeatherFragment oneDayWeatherFragment;
    private SevenDaysWeatherFragment sevenDaysWeatherFragment;

    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar_second);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cities = getResources().getStringArray(R.array.cities);

        if(getIntent() != null && getIntent().getExtras()!= null){

            cityIndex = getIntent().getExtras().getInt(CITY_QUERY);

            setTitle(cities[cityIndex]);

            oneDayWeatherFragment = OneDayWeatherFragment.newInstance(cityIndex);
            sevenDaysWeatherFragment = SevenDaysWeatherFragment.newInstance(cityIndex);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container,oneDayWeatherFragment);
            fragmentTransaction.commit();

        }else {
            finish();
            return;
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
