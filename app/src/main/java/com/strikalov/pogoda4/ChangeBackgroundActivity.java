package com.strikalov.pogoda4;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChangeBackgroundActivity extends AppCompatActivity {

    private volatile List<BackgroundPicture> backgroundPicturesList = new ArrayList<>();
    private final String SQL_EXCEPTION_TAG = "sql_exception";
    private RecyclerView changeBackgroundRecyclerView;
    private PictureBackgroundRecyclerViewAdapter pictureBackgroundRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_background);

        Toolbar toolbar = findViewById(R.id.toolbar_change_background);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.change_background));

        changeBackgroundRecyclerView = findViewById(R.id.change_background_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        changeBackgroundRecyclerView.setLayoutManager(gridLayoutManager);

        downloadPicruresToRecyclerView();

    }

    private void downloadPicruresToRecyclerView(){
        DownloadPicturesFromSQLTask downloadPicturesFromSQLTask = new DownloadPicturesFromSQLTask();
        downloadPicturesFromSQLTask.execute();
    }

    private class DownloadPicturesFromSQLTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            SQLiteOpenHelper backgroundPictureDatabaseHelper = new BackgroundPictureDatabaseHelper(
                    ChangeBackgroundActivity.this, getResources());

            try {

                SQLiteDatabase db = backgroundPictureDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("PICTURE", new String[]{"_id", "NAME", "IMAGE_RESOURCE_ID_MINI", "SELECTED"},
                        null, null, null, null, null);

                while (cursor.moveToNext()) {

                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int imageResourceId = cursor.getInt(2);
                    boolean isChecked = (cursor.getInt(3) == 1);

                    backgroundPicturesList.add(new BackgroundPicture(id, name, imageResourceId, isChecked));

                }

                cursor.close();
                db.close();
                return true;

            } catch (SQLiteException e) {
                Log.e(SQL_EXCEPTION_TAG, "Database unavailable");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                pictureBackgroundRecyclerViewAdapter = new PictureBackgroundRecyclerViewAdapter(
                        backgroundPicturesList, getResources());
                changeBackgroundRecyclerView.setAdapter(pictureBackgroundRecyclerViewAdapter);
            }
        }
    }


    public void onClickAcceptBackgroundPicture(View view){
        acceptBackgroundPicture();
    }

    private void acceptBackgroundPicture(){
        AcceptBackgroundTask acceptBackgroundTask = new AcceptBackgroundTask();
        acceptBackgroundTask.execute();
    }

    private class AcceptBackgroundTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids){

            if(backgroundPicturesList.size()>0) {

                SQLiteOpenHelper backgroundPictureDatabaseHelper = new BackgroundPictureDatabaseHelper(
                        ChangeBackgroundActivity.this, getResources());

                try {

                    SQLiteDatabase db = backgroundPictureDatabaseHelper.getWritableDatabase();

                    for (BackgroundPicture backgroundPicture : backgroundPicturesList) {

                        ContentValues pictureValues = new ContentValues();
                        pictureValues.put("SELECTED", backgroundPicture.isChecked());

                        db.update("PICTURE", pictureValues, "_id = ?",
                                new String[]{Integer.toString(backgroundPicture.getId())});

                    }

                    db.close();
                    return true;

                } catch (SQLiteException e) {
                    Log.e(SQL_EXCEPTION_TAG, "Database unavailable");
                    return false;
                }

            }else {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean success){
            if(success){
                Toast toast = Toast.makeText(ChangeBackgroundActivity.this, getString(R.string.picture_updated),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
