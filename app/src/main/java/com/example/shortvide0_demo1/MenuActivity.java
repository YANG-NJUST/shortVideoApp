package com.example.shortvide0_demo1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.shortvide0_demo1.Fragment.FocusFragment;
import com.example.shortvide0_demo1.Fragment.HomepageFragment;
import com.example.shortvide0_demo1.Fragment.MessageFragment;
import com.example.shortvide0_demo1.Fragment.MineFragment;
import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.bean.User;
import com.example.shortvide0_demo1.bean.Video;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    public static User user;
    public static List<Video> videoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_fragment);

        //获取从LoginActivity传来的User对象 即当前成功登录用户
        user = (User) getIntent().getSerializableExtra("user");
        Log.d("MenuActivity", "当前用户信息： " + user);

        //获取当前服务器端的视频列表
        videoList = LoginActivity.videoList;
        for (Video video : videoList) {
            Log.d("MenuActivity", "当前所有视频信息: " + video);
        }

        //得到FragmentManager打开事务处理
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, new HomepageFragment());
        transaction.commit();

        //add button前往添加视频Activity
        findViewById(R.id.ib_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this,AddVideoActivity.class);
                intent.putExtra("userAccount",user.getUserAccount());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void myClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.rb_homepage:
                transaction.replace(R.id.container, new HomepageFragment());
                break;
            case R.id.rb_focus:
                transaction.replace(R.id.container, new FocusFragment());
                break;
            case R.id.rb_message:
                transaction.replace(R.id.container, new MessageFragment());
                break;
            case R.id.rb_mine:
                transaction.replace(R.id.container, new MineFragment());
                break;
            default:
        }
        transaction.commit();
    }


}
