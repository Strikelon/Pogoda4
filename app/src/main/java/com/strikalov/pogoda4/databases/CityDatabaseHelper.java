package com.strikalov.pogoda4.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.gson.Gson;
import com.strikalov.pogoda4.pojogson.City;
import com.strikalov.pogoda4.pojogson.CityList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CityDatabaseHelper extends SQLiteOpenHelper{

    private static final String FILENAME = "citybase.json";

    private static final String TAG = "CityDatabaseHelper";

    public static final String TABLE_CITIES = "CITIES";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_CITY_ID = "CITY_ID";
    public static final String COLUMN_CITY_NAME = "CITY_NAME";

    private static final String DB_NAME = "cities_database";
    private static final int DB_VERSION = 1;

    private Context context;

    public CityDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(context.getAssets().open(FILENAME)));
            String line;
            while ((line = fileReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            fileReader.close();
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "JsonFileError", e);
        }

        Gson gson = new Gson();
        CityList cityList = gson.fromJson(text.toString(), CityList.class);
        City[] cities = cityList.getCity();

        db.execSQL("CREATE TABLE "+ TABLE_CITIES +" ("+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CITY_ID + " TEXT, "
                + COLUMN_CITY_NAME + " TEXT);");

        for(int i=0; i < cities.length; i++){
            insertCity(db, cities[i].getId(), cities[i].getName());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    private static void insertCity(SQLiteDatabase db, String cityId, String cityName) {

        ContentValues cityValues = new ContentValues();
        cityValues.put(COLUMN_CITY_ID, cityId);
        cityValues.put(COLUMN_CITY_NAME, cityName);
        db.insert(TABLE_CITIES, null, cityValues);

    }

}
