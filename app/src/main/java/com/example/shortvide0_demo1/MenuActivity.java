package com.example.shortvide0_demo1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.shortvide0_demo1.Fragment.FocusFragment;
import com.example.shortvide0_demo1.Fragment.HomepageFragment;
import com.example.shortvide0_demo1.Fragment.MessageFragment;
import com.example.shortvide0_demo1.Fragment.MineFragment;

public class MenuActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_fragment);

        /**
         * 得到FragmentManager打开事务处理
         */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, new HomepageFragment());
        transaction.commit();
        /**
         * add button前往添加视频Activity
         */
        findViewById(R.id.ib_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, AddVideoActivity.class));
            }
        });
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
