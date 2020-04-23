package com.example.shortvide0_demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.emergency.EmergencyNumber;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private VideoView videoView;
    //private Button btnPlay, btnPause, btnReplay;

    List<String> videoPath=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoPath.add("/storage/emulated/0/DCIM/Camera/shanghai.mp4");
        videoPath.add("/storage/emulated/0/DCIM/Camera/kobe.mp4");
        videoPath.add("/storage/emulated/0/DCIM/Camera/firework.mp4");
        videoPath.add("/storage/emulated/0/DCIM/Camera/mountain.mp4");


//        btnPlay=findViewById(R.id.btn_play);
//        btnPause=findViewById(R.id.btn_pause);
//        btnReplay=findViewById(R.id.btn_replay);
        //videoView = (VideoView) findViewById(R.id.videoView);
        /**
         * 本地视频播放
         */
        //videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/shanghai.mp4");
        /**
         * 使用MediaController控制视频播放
         */
        final MediaController controller = new MediaController(this);
        /**
         * 设置VideoView和MediaController相互建立关联
         */
        //videoView.setMediaController(controller);
        //controller.setMediaPlayer(videoView);


        /**
         * 实例化适配器(RecyclerView.Adapter)
         */
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_video,parent,false);
                return new ViewHolder(v);
            }

            //绑定（为ViewHolder里面的控件设置显示内容）
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ViewHolder holder1=(ViewHolder) holder;
                holder1.videoView.setVideoPath(videoPath.get(position));
                holder1.videoView.start();
            }

            @Override
            public int getItemCount() {
                return videoPath.size();
            }
        };
        /**
         * 找到ViewPager 设置适配器
         */
        ViewPager2 pager2 =  findViewById(R.id.pagers);
        pager2.setAdapter(adapter);


    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public VideoView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView=itemView.findViewById(R.id.videoView);
        }
    }

//    public void mainClick(View view) {
//        switch (view.getId()){
//            case R.id.btn_play:
//                videoView.start();
//                break;
//            case R.id.btn_pause:
//                videoView.pause();
//                break;
//            case R.id.btn_replay:
//                videoView.resume();
//                break;
//            default:
//                break;
//        }
//   }
}
