package com.example.android.audiobydarthvader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void exit(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void GoTo2Act(View view) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

}