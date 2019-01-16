package com.strikalov.pogoda4.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SelectedCitiesDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_SELECTED_CITIES = "SELECTED_CITIES";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_CITY_ID = "CITY_ID";
    public static final String COLUMN_CITY_NAME = "CITY_NAME";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TEMPERATURE = "TEMPERATURE";

    private static final String DB_NAME = "selected_cities_database";
    private static final int DB_VERSION = 1;

    private Context context;

    public SelectedCitiesDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE "+ TABLE_SELECTED_CITIES +" ("+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CITY_ID + " TEXT, "
                + COLUMN_CITY_NAME + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TEMPERATURE + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
