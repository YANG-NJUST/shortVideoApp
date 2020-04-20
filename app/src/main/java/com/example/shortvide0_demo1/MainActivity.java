package com.example.shortvide0_demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.emergency.EmergencyNumber;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button btnPlay, btnPause, btnReplay;
    // Storage Permissions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        btnPlay=findViewById(R.id.btn_play);
        btnPause=findViewById(R.id.btn_pause);
        btnReplay=findViewById(R.id.btn_replay);
        videoView = (VideoView) findViewById(R.id.videoView);
        /**
         * 本地视频播放
         */
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/shanghai.mp4");
        /**
         * 使用MediaController控制视频播放
         */
        //MediaController controller = new MediaController(this);
        /**
         * 设置VideoView和MediaController相互建立关联
         */
        //videoView.setMediaController(controller);
        //controller.setMediaPlayer(videoView);

    }

    public void mainClick(View view) {
        switch (view.getId()){
            case R.id.btn_play:
                videoView.start();
                break;
            case R.id.btn_pause:
                videoView.pause();
                break;
            case R.id.btn_replay:
                videoView.resume();
                break;
            default:
                break;
        }
    }
}
