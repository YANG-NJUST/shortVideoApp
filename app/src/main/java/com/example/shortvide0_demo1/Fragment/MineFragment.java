package com.example.shortvide0_demo1.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.example.shortvide0_demo1.FanListActivity;
import com.example.shortvide0_demo1.FocusListActivity;
import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.MenuActivity;
import com.example.shortvide0_demo1.R;
import com.example.shortvide0_demo1.bean.FanFocus;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment {


    public static List<String>  focusList= new ArrayList<>();//该用户的关注列表;
    public static List<String>  fanList= new ArrayList<>();//该用户的粉丝列表;
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
        //该用户关注列表获取
        OkHttpUtils.getInstance().doGet("http://192.168.74.233:8080/demo/focus/" + MenuActivity.user
                .getUserAccount(), null, new INetCallBack() {
            @Override
            public void onSuccess(String response) {
                List<FanFocus> fanFocusList = FromGson.getInstance().getFanFocusBean(response);
                for (FanFocus fanFocus : fanFocusList) {
                    focusList.add(fanFocus.getFocusUser());
                }
            }

            @Override
            public void onFailed(Throwable ex) {
                Toast.makeText(getActivity(), "获取关注列表失败！", Toast.LENGTH_LONG).show();
            }
        });
        //该用户粉丝列表获取
        OkHttpUtils.getInstance().doGet("http://192.168.74.233:8080/demo/focusUser/" + MenuActivity.user
                .getUserAccount(), null, new INetCallBack() {
            @Override
            public void onSuccess(String response) {
                List<FanFocus> fanFocusList = FromGson.getInstance().getFanFocusBean(response);
                for (FanFocus fanFocus : fanFocusList) {
                    fanList.add(fanFocus.getUser());
                }
            }

            @Override
            public void onFailed(Throwable ex) {
                Toast.makeText(getActivity(), "获取关注列表失败！", Toast.LENGTH_LONG).show();
            }
        });
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
