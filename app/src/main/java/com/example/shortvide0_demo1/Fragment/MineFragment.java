package com.example.shortvide0_demo1.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shortvide0_demo1.FanListActivity;
import com.example.shortvide0_demo1.FocusListActivity;
import com.example.shortvide0_demo1.MenuActivity;
import com.example.shortvide0_demo1.R;
import com.example.shortvide0_demo1.VideoAdapter;
import com.example.shortvide0_demo1.bean.Video;

public class MineFragment extends Fragment {



    private TextView tvLoveAmount,tvFanAmount,tvFocusAmount;
    private int allLoveAmount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_mine, container, false);
        //初始化控件
        tvLoveAmount=view.findViewById(R.id.tv_love_amount);
        tvFocusAmount=view.findViewById(R.id.tv_focus_amount);
        tvFanAmount=view.findViewById(R.id.tv_fan_amount);
        //MenuActivity中获取当前用户所有信息并显示
        for (Video video : MenuActivity.myVideoList) {
            allLoveAmount+=video.getLoveAmount();
        }
        tvLoveAmount.setText(allLoveAmount+"点赞");
        tvFocusAmount.setText(MenuActivity.focusList.size()+"关注");
        tvFanAmount.setText(MenuActivity.fanList.size()+"粉丝");
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
