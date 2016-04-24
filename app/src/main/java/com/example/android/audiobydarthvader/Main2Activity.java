package com.example.android.audiobydarthvader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Main2Activity extends Activity  {

    Button play,stop,record;
    private MediaRecorder myAudioRecorder;
    public String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        play=(Button)findViewById(R.id.play_button);
        stop=(Button)findViewById(R.id.stop_button);
        record=(Button)findViewById(R.id.record_button);

        stop.setEnabled(false);
        stop.setTextColor(Color.parseColor("#9E9E9E"));
        play.setEnabled(false);
        play.setTextColor(Color.parseColor("#9E9E9E"));
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.mp3";

        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);
                stop.setTextColor(Color.parseColor("#000000"));
                record.setTextColor(Color.parseColor("#9E9E9E"));

                Toast.makeText(getApplicationContext(), "Запись началась..", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;

                stop.setEnabled(false);
                play.setEnabled(true);
                play.setTextColor(Color.parseColor("#000000"));
                stop.setTextColor(Color.parseColor("#9E9E9E"));

                Toast.makeText(getApplicationContext(), "Успешно записано!", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Воспроизведение аудио", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void send(View view) {

        EditText subj = (EditText)findViewById(R.id.subject_mail);
        EditText text = (EditText)findViewById(R.id.text_mail);

        String subj_text = subj.getText().toString();
        String mail_text = text.getText().toString();

        File fileIn = new File(outputFile);

        Uri uri = Uri.fromFile(fileIn);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("Audio/mp3");
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subj_text);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mail_text);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
            Toast.makeText(this, "Отправляем...", Toast.LENGTH_SHORT).show();
        }
    }

//    public void doDarth(View view){
//
//    }

    public void back2Act(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
    }

}