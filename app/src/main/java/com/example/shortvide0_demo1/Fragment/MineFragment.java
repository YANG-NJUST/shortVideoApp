package com.example.shortvide0_demo1.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shortvide0_demo1.Constant;
import com.example.shortvide0_demo1.FanListActivity;
import com.example.shortvide0_demo1.FocusListActivity;
import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.MenuActivity;
import com.example.shortvide0_demo1.R;
import com.example.shortvide0_demo1.VideoAdapter;
import com.example.shortvide0_demo1.bean.FanFocus;
import com.example.shortvide0_demo1.bean.Video;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MineFragment extends Fragment {



    private TextView tvLoveAmount,tvFanAmount,tvFocusAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_mine, container, false);
        //初始化控件
        tvLoveAmount=view.findViewById(R.id.tv_love_amount);
        tvFocusAmount=view.findViewById(R.id.tv_focus_amount);
        tvFanAmount=view.findViewById(R.id.tv_fan_amount);
        //MenuActivity中获取当前用户所有信息并显示
        tvLoveAmount.setText(MenuActivity.user.getLoveAmount()+"点赞");
        tvFocusAmount.setText(MenuActivity.user.getFocusAmount()+"关注");
        tvFanAmount.setText(MenuActivity.user.getFanAmount()+"粉丝");
        //控件点击事件
        initEvent();

        //本用户视频缩略图列表
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.video_recyclerView);//滑动布局
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);//内部布局
        recyclerView.setLayoutManager(layoutManager);//设置内部布局应用于滑动布局
        VideoAdapter videoAdapter=new VideoAdapter(MenuActivity.myVideoList);//初始化适配器
        recyclerView.setAdapter(videoAdapter);//设置适配器

        return view;
    }

    private void initEvent() {
        tvFocusAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FocusListActivity.class));
            }
        });
        tvFanAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FanListActivity.class));
            }
        });
    }





}
