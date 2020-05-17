package com.example.shortvide0_demo1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shortvide0_demo1.Fragment.MineFragment;
import com.example.shortvide0_demo1.bean.User;
import com.example.shortvide0_demo1.bean.Video;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShowVideoActivity extends AppCompatActivity {

    private static final String TAG = "ShowVideoActivity";
    private List<Video> videoList=new ArrayList<>();
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_video);
        tvTitle=findViewById(R.id.title_showVideo);


        //接收传来的videoList
        videoList.clear();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String userName=intent.getStringExtra("userName");
        videoList= (List<Video>) bundle.getSerializable("VideoList");

        if(videoList.size()==0){
            tvTitle.setText(userName+"用户没有发布视频");
        }else{
            tvTitle.setText(userName+"用户发布的视频");
        }

        //填充RecyclerView
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rv_showVideo);//滑动布局
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);//内部布局
        recyclerView.setLayoutManager(layoutManager);//设置内部布局应用于滑动布局
        VideoAdapter videoAdapter=new VideoAdapter(videoList);//初始化适配器
        recyclerView.setAdapter(videoAdapter);//设置适配器
    }


}












