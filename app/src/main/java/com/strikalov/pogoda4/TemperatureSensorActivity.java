package com.strikalov.pogoda4;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class TemperatureSensorActivity extends AppCompatActivity {

    private TextView temperature_text;
    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private String temperatureMeasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_sensor);

        Toolbar toolbar = findViewById(R.id.toolbar_temperature_sensor);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.temperature_sensor));

        temperature_text = findViewById(R.id.temperature_sensor_text);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        temperatureMeasure = getString(R.string.temperature_measure);


        if(sensorTemperature == null){

            temperature_text.setText(getString(R.string.sensor_null));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sensorTemperature != null){

            sensorManager.registerListener(listenerTemperature, sensorTemperature,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerTemperature, sensorTemperature);
    }

    private void showSensorValue(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.values[0]).append(" ").append(temperatureMeasure);
        temperature_text.setText(stringBuilder);
    }


    private final SensorEventListener listenerTemperature = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showSensorValue(event);
        }
    };
}
