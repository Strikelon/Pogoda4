package com.strikalov.pogoda4;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    private String textAfterSendFeedBack;
    private EditText topic;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar_feedback);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.nav_feedback));

        textAfterSendFeedBack = getString(R.string.after_click_send_feedback);

        topic = findViewById(R.id.topic_feedback);
        text = findViewById(R.id.text_feedback);
    }

    public void onClickSendFeedback(View view){
        topic.setText("");
        text.setText("");
        Toast.makeText(this,textAfterSendFeedBack, Toast.LENGTH_SHORT).show();
    }
}
