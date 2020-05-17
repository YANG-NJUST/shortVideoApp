package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.shortvide0_demo1.bean.Video;

import org.w3c.dom.Text;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView videoUser;
    private TextView videoInfo;
    private TextView videoLove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        //绑定控件
        videoInfo=findViewById(R.id.tv_video_info_PlayVideo);
        videoUser=findViewById(R.id.tv_user_name_PlayVideo);
        videoView=findViewById(R.id.videoView_PlayVideo);
        videoLove=findViewById(R.id.love_amount_textView_PlayVideo);
        //接收传来的Video（单个）
        Intent intent = getIntent();
        Video video= (Video) intent.getSerializableExtra("singleVideo");
        //填充控件
        videoInfo.setText(video.getVideoInfo());
        videoUser.setText(video.getUploadUser());
        videoLove.setText(video.getLoveAmount()+"");
        videoView.setVideoURI(Uri.parse(video.getVideoUrl()));
        videoView.start();
    }

}
