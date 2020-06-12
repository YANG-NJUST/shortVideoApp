package com.example.shortvide0_demo1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.shortvide0_demo1.Fragment.FocusFragment;
import com.example.shortvide0_demo1.Fragment.HomepageFragment;
import com.example.shortvide0_demo1.Fragment.SearchFragment;
import com.example.shortvide0_demo1.Fragment.MineFragment;
import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.bean.FanFocus;
import com.example.shortvide0_demo1.bean.User;
import com.example.shortvide0_demo1.bean.Video;
import com.example.shortvide0_demo1.net.Constant;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    public static User user;   //标示当前登录用户
    public static List<Video> videoList;
    public static List<String> focusList = new ArrayList<>();//该用户的关注列表;
    public static List<String> fanList = new ArrayList<>();//该用户的粉丝列表;
    public static List<Video> focusVideoList=new ArrayList<>();//该用户关注的视频
    public static List<Video> myVideoList=new ArrayList<>();//该用户发布的视频
    private ProgressTask pTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //防止布局输入框顶起
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_tab_fragment);

        //获取从LoginActivity传来的User对象 即当前成功登录用户
        user = (User) getIntent().getSerializableExtra("user");
        Log.d(TAG, "当前用户信息： " + user);

        //获取当前服务器端的视频列表
        videoList = LoginActivity.videoList;
        for (Video video : videoList) {
            Log.d(TAG, "当前所有视频信息: " + video);
        }
        //获取该用户关注的视频
        pTask = new ProgressTask();
        pTask.execute();

        //得到FragmentManager打开事务处理
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, new HomepageFragment());
        transaction.commit();

        //add button前往添加视频Activity
        findViewById(R.id.ib_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AddVideoActivity.class);
                intent.putExtra("userAccount", user.getUserAccount());
                startActivity(intent);
            }
        });


    }

    private void getFocusList() {
        //该用户关注列表获取
        List<String> list=new ArrayList<>();
        OkHttpUtils.getInstance().doGet(Constant.url+"/focus/" + MenuActivity.user
                .getUserAccount(), null, new INetCallBack() {
            @Override
            public void onSuccess(String response) {
                focusList.clear();
                List<FanFocus> fanFocusList = FromGson.getInstance().getFanFocusBean(response);
                for (FanFocus fanFocus : fanFocusList) {
                    focusList.add(fanFocus.getFocusUser());
                    Log.d(TAG, fanFocus.toString());
                }
            }

            @Override
            public void onFailed(Throwable ex) {
                Toast.makeText(MenuActivity.this, "获取关注列表失败！", Toast.LENGTH_LONG).show();
            }
        });
        //该用户粉丝列表获取
        OkHttpUtils.getInstance().doGet(Constant.url+"/focusUser/" + MenuActivity.user
                .getUserAccount(), null, new INetCallBack() {
            @Override
            public void onSuccess(String response) {
                fanList.clear();
                List<FanFocus> fanFocusList = FromGson.getInstance().getFanFocusBean(response);
                for (FanFocus fanFocus : fanFocusList) {
                    fanList.add(fanFocus.getUser());
                }
            }

            @Override
            public void onFailed(Throwable ex) {
                Toast.makeText(MenuActivity.this, "获取关注列表失败！", Toast.LENGTH_LONG).show();
            }
        });
    }

    class ProgressTask extends AsyncTask<Void, Void, List<String>> {


        //执行线程任务之前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //作用：接受输入的参数，执行任务中的耗时操作，返回线程任务的执行结果
        @Override
        protected List<String> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "加载开始===================");
                getFocusList();
                Thread.sleep(1000);
                Log.d(TAG, "加载完成===================");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return focusList;
        }

        //接受线程任务的执行结果
        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            Log.d(TAG, "onPostExecute: ");
            //获取当前用户关注的视频
            focusVideoList.clear();
            for (Video video : LoginActivity.videoList) {
                for (String upLoadUser : list) {
                    if(video.getUploadUser().equals(upLoadUser)){
                        focusVideoList.add(video);
                        Log.d(TAG, "当前用户关注的视频信息："+video);
                    }
                }
            }
            //获取当前用户发布的视频
            myVideoList.clear();
            for (Video video : LoginActivity.videoList) {
                if(video.getUploadUser().equals(user.getUserAccount())){
                    myVideoList.add(video);
                    Log.d(TAG, "当前用户发布的视频信息： "+video);
                }
            }
        }

        //取消(cancel)异步任务时触发该方法
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
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
                transaction.replace(R.id.container, new SearchFragment());
                break;
            case R.id.rb_mine:
                transaction.replace(R.id.container, new MineFragment());
                break;
            default:
        }
        transaction.commit();
    }


    /**
     * 退出前提示
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder=new AlertDialog.Builder(MenuActivity.this);
        builder.setTitle("确定要退出吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNeutralButton("点错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }
}
