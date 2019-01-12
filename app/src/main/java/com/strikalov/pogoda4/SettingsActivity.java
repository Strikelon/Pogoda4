package com.strikalov.pogoda4;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity{

    private RadioButton radioButtonMeters;
    private RadioButton radioButtonKilometers;
    private RadioButton radioButtonCelsius;
    private RadioButton radioButtonFahrenheit;
    private RadioButton radioButtonMm;
    private RadioButton radioButtonHpa;

    private SharedPreferences sharedPrefMeasureSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.nav_settings_name));

        radioButtonMeters = findViewById(R.id.radio_meters);
        radioButtonKilometers = findViewById(R.id.radio_kilometers);
        radioButtonCelsius = findViewById(R.id.radio_celsius);
        radioButtonFahrenheit = findViewById(R.id.radio_fahrenheit);
        radioButtonMm = findViewById(R.id.radio_mm);
        radioButtonHpa = findViewById(R.id.radio_hpa);

        sharedPrefMeasureSettings = getSharedPreferences(SettingsConstants.MEASURE_SETTINGS, MODE_PRIVATE);

        initMeasureSettings();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initMeasureSettings();
    }

    private void initMeasureSettings(){

        int windSettings = sharedPrefMeasureSettings.getInt(SettingsConstants.KEY_WIND_MEASURE_SETTINGS, SettingsConstants.DEFAULT_WIND_SETTING);
        int temperatureSettings = sharedPrefMeasureSettings.getInt(SettingsConstants.KEY_TEMPERATURE_MEASURE_SETTINGS, SettingsConstants.DEFAULT_TEMPERATURE_SETTING);
        int pressureSettings = sharedPrefMeasureSettings.getInt(SettingsConstants.KEY_PRESSURE_MEASURE_SETTINGS, SettingsConstants.DEFAULT_PRESSURE_SETTING);

        switch (windSettings) {
            case SettingsConstants.FIRST_SETTING_CHECKED:
                radioButtonMeters.setChecked(true);
                radioButtonKilometers.setChecked(false);
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                radioButtonKilometers.setChecked(true);
                radioButtonMeters.setChecked(false);
                break;
        }

        switch (temperatureSettings){
            case SettingsConstants.FIRST_SETTING_CHECKED:
                radioButtonCelsius.setChecked(true);
                radioButtonFahrenheit.setChecked(false);
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                radioButtonFahrenheit.setChecked(true);
                radioButtonCelsius.setChecked(false);
                break;
        }

        switch (pressureSettings){
            case SettingsConstants.FIRST_SETTING_CHECKED:
                radioButtonMm.setChecked(true);
                radioButtonHpa.setChecked(false);
                break;
            case SettingsConstants.SECOND_SETTING_CHECKED:
                radioButtonHpa.setChecked(true);
                radioButtonMm.setChecked(false);
                break;
        }

    }


    public void onRadioButtonWindSpeedGroupClicked(View view) {
        RadioGroup radioGroupWindSpeedGroup = findViewById(R.id.wind_speed_group);
        int id = radioGroupWindSpeedGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.radio_meters:
                sharedPrefMeasureSettings.edit().
                        putInt(SettingsConstants.KEY_WIND_MEASURE_SETTINGS, SettingsConstants.FIRST_SETTING_CHECKED).apply();
                break;
            case R.id.radio_kilometers:
                sharedPrefMeasureSettings.edit().
                        putInt(SettingsConstants.KEY_WIND_MEASURE_SETTINGS, SettingsConstants.SECOND_SETTING_CHECKED).apply();
                break;
        }
    }

    public void onRadioButtonTemperatureGroupClicked(View view){
        RadioGroup radioGroupTemperatureGroup = findViewById(R.id.temperature_group);
        int id  = radioGroupTemperatureGroup.getCheckedRadioButtonId();
        switch (id){
            case R.id.radio_celsius:
                sharedPrefMeasureSettings.edit().
                        putInt(SettingsConstants.KEY_TEMPERATURE_MEASURE_SETTINGS, SettingsConstants.FIRST_SETTING_CHECKED).apply();
                break;
            case R.id.radio_fahrenheit:
                sharedPrefMeasureSettings.edit().
                        putInt(SettingsConstants.KEY_TEMPERATURE_MEASURE_SETTINGS, SettingsConstants.SECOND_SETTING_CHECKED).apply();
                break;
        }
    }

    public void onRadioButtonPressureGroupClicked(View view){
        RadioGroup radioGroupPressureGroup = findViewById(R.id.pressure_group);
        int id = radioGroupPressureGroup.getCheckedRadioButtonId();
        switch (id){
            case R.id.radio_mm:
                sharedPrefMeasureSettings.edit().
                        putInt(SettingsConstants.KEY_PRESSURE_MEASURE_SETTINGS, SettingsConstants.FIRST_SETTING_CHECKED).apply();
                break;
            case R.id.radio_hpa:
                sharedPrefMeasureSettings.edit().
                        putInt(SettingsConstants.KEY_PRESSURE_MEASURE_SETTINGS, SettingsConstants.SECOND_SETTING_CHECKED).apply();
                break;
        }
    }

}
