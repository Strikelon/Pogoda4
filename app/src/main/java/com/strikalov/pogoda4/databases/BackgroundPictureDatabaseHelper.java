package com.strikalov.pogoda4.databases;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.strikalov.pogoda4.R;

public class BackgroundPictureDatabaseHelper  extends SQLiteOpenHelper{

    private static final String DB_NAME = "background_picture";
    private static final int DB_VERSION = 1;

    private Resources resources;

    public BackgroundPictureDatabaseHelper(Context context, Resources resources){
        super(context, DB_NAME, null, DB_VERSION);
        this.resources = resources;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1) {

            db.execSQL("CREATE TABLE PICTURE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
                    + "IMAGE_RESOURCE_ID_MINI INTEGER, "
                    + "SELECTED NUMERIC);");

            insertPicture(db, resources.getString(R.string.sky), R.drawable.sky_background, R.drawable.sky_background_mini,true);
            insertPicture(db, resources.getString(R.string.forest), R.drawable.forest_background, R.drawable.forest_background_mini,false);
            insertPicture(db, resources.getString(R.string.desert), R.drawable.desert_background, R.drawable.desert_background_mini,false);
            insertPicture(db, resources.getString(R.string.christmas), R.drawable.christmas_background, R.drawable.christmas_background_mini,false);
            insertPicture(db, resources.getString(R.string.sea), R.drawable.sea_background, R.drawable.sea_background_mini,false);
            insertPicture(db, resources.getString(R.string.mountains), R.drawable.mountains_background, R.drawable.mountains_background_mini,false);

        }

    }

    private static void insertPicture(SQLiteDatabase db, String name, int resourceId, int resourceIdMini,boolean checked) {
        ContentValues pictureValues = new ContentValues();
        pictureValues.put("NAME", name);
        pictureValues.put("IMAGE_RESOURCE_ID", resourceId);
        pictureValues.put("IMAGE_RESOURCE_ID_MINI", resourceIdMini);
        pictureValues.put("SELECTED", checked);
        db.insert("PICTURE", null, pictureValues);
    }

}
