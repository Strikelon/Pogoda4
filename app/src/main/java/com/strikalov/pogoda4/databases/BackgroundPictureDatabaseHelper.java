package com.strikalov.pogoda4.databases;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.strikalov.pogoda4.R;

public class BackgroundPictureDatabaseHelper  extends SQLiteOpenHelper{

    private static final String DB_NAME = "background_picture";
    private static final int DB_VERSION = 1;

    public static final String TABLE_PICTURE = "PICTURE";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_IMAGE_RESOURCE_ID = "IMAGE_RESOURCE_ID";
    public static final String COLUMN_IMAGE_RESOURCE_ID_MINI = "IMAGE_RESOURCE_ID_MINI";
    public static final String COLUMN_SELECTED = "SELECTED";

    private Resources resources;

    public BackgroundPictureDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.resources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE "+ TABLE_PICTURE +" ("+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_IMAGE_RESOURCE_ID + " INTEGER, "
                + COLUMN_IMAGE_RESOURCE_ID_MINI + " INTEGER, "
                + COLUMN_SELECTED + " NUMERIC);");

        insertPicture(db, resources.getString(R.string.sky), R.drawable.sky_background, R.drawable.sky_background_mini,true);
        insertPicture(db, resources.getString(R.string.forest), R.drawable.forest_background, R.drawable.forest_background_mini,false);
        insertPicture(db, resources.getString(R.string.desert), R.drawable.desert_background, R.drawable.desert_background_mini,false);
        insertPicture(db, resources.getString(R.string.christmas), R.drawable.christmas_background, R.drawable.christmas_background_mini,false);
        insertPicture(db, resources.getString(R.string.sea), R.drawable.sea_background, R.drawable.sea_background_mini,false);
        insertPicture(db, resources.getString(R.string.mountains), R.drawable.mountains_background, R.drawable.mountains_background_mini,false);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    private static void insertPicture(SQLiteDatabase db, String name, int resourceId, int resourceIdMini,boolean checked) {
        ContentValues pictureValues = new ContentValues();
        pictureValues.put(COLUMN_NAME, name);
        pictureValues.put(COLUMN_IMAGE_RESOURCE_ID, resourceId);
        pictureValues.put(COLUMN_IMAGE_RESOURCE_ID_MINI, resourceIdMini);
        pictureValues.put(COLUMN_SELECTED, checked);
        db.insert(TABLE_PICTURE, null, pictureValues);
    }

}
