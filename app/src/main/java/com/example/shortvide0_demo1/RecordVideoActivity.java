package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecordVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RecordVideoActivity";
    private Button record, stop;
    private File videoFile;
    private MediaRecorder mediaRecorder;
    private SurfaceView sView;
    private boolean isRecording = false;
    private boolean isPause = false;
    private Camera mCamera;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        record = findViewById(R.id.btn_record);
        stop = findViewById(R.id.btn_stop);
        sView = findViewById(R.id.sv_video);
        stop.setEnabled(false);
        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sView.getHolder().setFixedSize(320, 280);
        sView.getHolder().setKeepScreenOn(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:
                if (!isRecording) {
                    try {
                        //刷新SurfaceView控件
                        sView.setVisibility(View.GONE);
                        sView.setVisibility(View.VISIBLE);
                        //开启摄像头（后置）
                        mCamera = Camera.open(0);
                        //创建文件
                        videoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + System.currentTimeMillis() + "_video.mp4");
                        //MediaRecorder 录制视频
                        mediaRecorder = new MediaRecorder();
                        mediaRecorder.reset();
                        //设置摄像头方向 预览方向
                        mCamera.setDisplayOrientation(90);
                        //解锁摄像头
                        mCamera.unlock();
                        //绑定摄像头
                        mediaRecorder.setCamera(mCamera);
                        //设置音频源
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        //设置摄像源
                        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        //设置文件输出格式
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        //设置音频解码
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                        //设置视频解码
                        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                        //设置视频解码帧数
                        mediaRecorder.setVideoEncodingBitRate(8 * 1024 * 1024);
                        //设置视频大小
                        mediaRecorder.setVideoSize(1280, 720);
                        //设置视频方向 输出
                        mediaRecorder.setOrientationHint(90);
                        //设置实时预览画面
                        mediaRecorder.setPreviewDisplay(sView.getHolder().getSurface());
                        //设置输出文件路径
                        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
                        //准备 启动
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        isPause = true;
                        record.setText("暂停录制");
                        stop.setEnabled(true);
                        isRecording = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "调用摄像头出现问题!", Toast.LENGTH_SHORT).show();
                    }
                } else if (isPause) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mediaRecorder.pause();
                    }
                    record.setText("继续录制");
                    isPause = false;
                } else if (!isPause) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mediaRecorder.resume();
                    }
                    record.setText("暂停录制");
                    isPause = true;
                }
                break;
            case R.id.btn_stop:
                if (isRecording) {
                    try {
                        //结束录制
                        isRecording = false;
                        record.setText("重新录制");
                        stop.setText("确定");
                        //关闭MediaRecorder流
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        mCamera.lock();
                        //刷新SurfaceView控件用于显示视频
                        sView.setVisibility(View.GONE);
                        sView.setVisibility(View.VISIBLE);
                        //创建MediaPlayer用于播放视频
                        mediaPlayer = new MediaPlayer();
                        //与SurfaceView绑定
                        mediaPlayer.setDisplay(sView.getHolder());
                        //设置音频流
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        //设置文件路径
                        mediaPlayer.setDataSource(videoFile.getAbsolutePath());
                        //准备 启动
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    //确定后 将视频地址Uri传回
                    Intent intent = new Intent();
                    intent.putExtra("uri",videoFile.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }


}
