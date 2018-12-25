package com.strikalov.pogoda4;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChangeBackgroundActivity extends AppCompatActivity {

    private volatile List<BackgroundPicture> backgroundPicturesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_background);

        Toolbar toolbar = findViewById(R.id.toolbar_change_background);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.change_background));

        RecyclerView changeBackgroundRecyclerView = findViewById(R.id.change_background_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        changeBackgroundRecyclerView.setLayoutManager(gridLayoutManager);

        SQLiteOpenHelper backgroundPictureDatabaseHelper = new BackgroundPictureDatabaseHelper(this, getResources());

        try {

            SQLiteDatabase db = backgroundPictureDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("PICTURE", new String[]{"_id","NAME", "IMAGE_RESOURCE_ID_MINI", "SELECTED"},
                    null, null, null, null, null);

            while (cursor.moveToNext()){

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int imageResourceId = cursor.getInt(2);
                boolean isChecked = (cursor.getInt(3) == 1);

                backgroundPicturesList.add(new BackgroundPicture(id, name, imageResourceId, isChecked));

            }

            cursor.close();
            db.close();


        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        PictureBackgroundRecyclerViewAdapter pictureBackgroundRecyclerViewAdapter =
                new PictureBackgroundRecyclerViewAdapter(backgroundPicturesList, getResources());

        changeBackgroundRecyclerView.setHasFixedSize(true);

        changeBackgroundRecyclerView.setAdapter(pictureBackgroundRecyclerViewAdapter);

    }


    public void onClickAcceptBackgroundPicture(View view){

        SQLiteOpenHelper backgroundPictureDatabaseHelper = new BackgroundPictureDatabaseHelper(this, getResources());

        try {

            SQLiteDatabase db = backgroundPictureDatabaseHelper.getWritableDatabase();

            for(BackgroundPicture backgroundPicture : backgroundPicturesList){

                ContentValues pictureValues = new ContentValues();
                pictureValues.put("SELECTED", backgroundPicture.isChecked());

                db.update("PICTURE", pictureValues, "_id = ?",
                        new String[] {Integer.toString(backgroundPicture.getId())});

            }

            db.close();

        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Toast toast = Toast.makeText(this, getString(R.string.picture_updated), Toast.LENGTH_SHORT);
        toast.show();

    }

}
