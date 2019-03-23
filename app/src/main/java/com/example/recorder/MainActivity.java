package com.example.recorder;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "Error";
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private Button record = null;
    private Button stop = null;
    private Button play = null;
    private Button playStop = null;

    private MediaPlayer   player = null;
    //File soundFile;
    private static String soundFile = null;
    private static String fileName = null;
    MediaRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //get 4 button from layout
        record = findViewById(R.id.record);
        record.setOnClickListener(this);
        stop = findViewById(R.id.stop);
        stop.setOnClickListener(this);
        play = findViewById(R.id.play);
        play.setOnClickListener(this);
        playStop = findViewById(R.id.play_stop);
        playStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View source) {
        switch (source.getId()) {
            //Start record when click record button
            case R.id.record:
//                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    Toast.makeText(MainActivity.this, "SD카드가 없습니다.SD카드를 꽂여주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                startRecord();
                break;
            case R.id.stop:
                stopRecord();
                break;
            case R.id.play:
                    startPlaying();
                break;
            case R.id.play_stop:
                    stopPlaying();
                break;
        }
    }

    //start record
    private void startRecord() {
        if (mRecorder == null) {

            soundFile = getExternalFilesDir("/recordtest").getAbsolutePath();
            soundFile += "/audiorecordtest.amr";
        }
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //Set up sound source files
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB); //format setting
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB); //encode setting
        mRecorder.setOutputFile(soundFile); //output file setting
//        mRecorder.setOutputFile(soundFile.getAbsoluteFile()); //output file setting

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(soundFile);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
}
