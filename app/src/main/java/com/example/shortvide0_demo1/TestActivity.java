package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.bean.Video;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        HashMap<String,String> headers = new HashMap<>();

        OkHttpUtils.getInstance()
                .doGet("http://192.168.0.102:8080/demo/video", headers, new INetCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("GET", response);
                        List<Video> videoList = FromGson.getInstance().getVideoBean(response);
                        for (Video video : videoList) {
                            Log.d("GET", "视频url："+video.getVideoUrl());
                            Log.d("GET", "视频上传用户："+video.getUploadUser());
                            Log.d("GET", "视频描述信息："+video.getVideoInfo());
                            Log.d("GET", "视频点赞数："+video.getLoveAmount());
                        }

                    }

                    @Override
                    public void onFailed(Throwable ex) {
                        Log.d("GET", "网络请求失败");
                    }
                });
    }
}
