package com.example.android.dictophonemail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Main2Activity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    Button play, stop, record, send;
    private MediaRecorder myAudioRecorder;
    public String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getPermissionToRecordAudio();

        play=(Button)findViewById(R.id.play_button);
        stop=(Button)findViewById(R.id.stop_button);
        record=(Button)findViewById(R.id.record_button);
        send = (Button)findViewById(R.id.send_button);

        send.setEnabled(false);
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
                send.setEnabled(true);
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

    public void back2Act(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
    }

    private static final int RECORD_AUDIO_PERMISSIONS_REQUEST = 1;

    public void getPermissionToRecordAudio() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this, "This is necessary so that you can record an audio message", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        RECORD_AUDIO_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        if (requestCode == RECORD_AUDIO_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Record audio permission granted", Toast.LENGTH_SHORT).show();
                record.setEnabled(true);
            } else {
                Toast.makeText(this, "Record audio permission denied", Toast.LENGTH_SHORT).show();
                record.setEnabled(false);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
