package com.strikalov.pogoda4.activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.strikalov.pogoda4.R;

public class HumiditySensorActivity extends AppCompatActivity {

    private TextView humidity_text;
    private SensorManager sensorManager;
    private Sensor sensorHumidity;
    private String humidityMeasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity_sensor);

        Toolbar toolbar = findViewById(R.id.toolbar_humidity_sensor);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.humidity_sensor));

        humidity_text = findViewById(R.id.humidity_sensor_text);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        humidityMeasure = getString(R.string.humidity_measure);

        if(sensorHumidity == null){

            humidity_text.setText(getString(R.string.sensor_null));

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sensorHumidity != null){

            sensorManager.registerListener(listenerHumidity, sensorHumidity,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerHumidity, sensorHumidity);
    }


    private void showSensorValue(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.values[0]).append(" ").append(humidityMeasure);
        humidity_text.setText(stringBuilder);
    }


    private final SensorEventListener listenerHumidity = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showSensorValue(event);
        }
    };

}
