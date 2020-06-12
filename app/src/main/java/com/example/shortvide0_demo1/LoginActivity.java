package com.example.shortvide0_demo1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.bean.User;
import com.example.shortvide0_demo1.bean.Video;
import com.example.shortvide0_demo1.net.Constant;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //声明控件
    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private ProgressBar mPbLogin;
    private List<User> userList = new ArrayList<>();
    public static List<Video> videoList = new ArrayList<>();
    private ProgressTask pTask;
    //所需权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //控件初始化
        initView();
        //控件点击事件
        initEvent();
        //请求权限
        verifyStoragePermissions(this);

    }


    @Override
    protected void onResume() {
        //读取服务器中所有用户信息来执行后续登录操作
        super.onResume();
        mPbLogin.setVisibility(View.INVISIBLE);
        OkHttpUtils.getInstance().doGet(Constant.url+"/user", null, new INetCallBack() {
            @Override
            public void onSuccess(String response) {
                userList = FromGson.getInstance().getUserBean(response);
            }

            @Override
            public void onFailed(Throwable ex) {
                Toast.makeText(LoginActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前往主界面
                toMenuActivity();
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前往注册界面
                toRegisterActivity();
            }
        });
    }

    private void toMenuActivity() {
        if (TextUtils.isEmpty(mEtAccount.getText()) || TextUtils.isEmpty(mEtPassword.getText())) {
            Toast.makeText(this, "请输入账号密码!", Toast.LENGTH_SHORT).show();
        } else {
            //遍历存储用户信息的List<User>
            for (User user : userList) {
                if (mEtAccount.getText().toString().equals(user.getUserAccount()) && mEtPassword.getText().toString().equals(user.getUserPwd())) {
                    //进入MenuActivity之前获取服务器端的视频
                    pTask = new ProgressTask();
                    pTask.execute(user);
                    return;
                }
            }
            Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
        }
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userList", (Serializable) userList);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    /**
     * 注册成功后返回注册信息
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册成功返回
        if(resultCode == RESULT_OK){
            mEtAccount.setText(data.getStringExtra("account"));
            mEtPassword.setText(data.getStringExtra("pwd"));
        }
        //返回键返回
    }

    //绑定控件
    private void initView() {
        mEtAccount = findViewById(R.id.et_login_account);
        mEtPassword = findViewById(R.id.et_login_password);
        mEtAccount.setText("user1");
        mEtPassword.setText("123");
        mBtnLogin = findViewById(R.id.login_button);
        mBtnRegister = findViewById(R.id.register_button);
        mPbLogin = findViewById(R.id.pb_login);
    }

    private void getVideo() {

        OkHttpUtils.getInstance().doGet(Constant.url+"/video", null, new INetCallBack() {
            @Override
            public void onSuccess(String response) {
                videoList = FromGson.getInstance().getVideoBean(response);
                Log.d(TAG, "videoList获取成功,视频数量：" + videoList.size());
            }

            @Override
            public void onFailed(Throwable ex) {
                Toast.makeText(LoginActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Params:   execute方法的参数类型，doInBackground方法的参数类型
     * Progress:进度,Integer
     * Result:
     */
    class ProgressTask extends AsyncTask<User, Void, User> {


        //执行线程任务之前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPbLogin.setVisibility(View.VISIBLE);
        }

        //作用：接受输入的参数，执行任务中的耗时操作，返回线程任务的执行结果
        @Override
        protected User doInBackground(User... users) {
            try {
                Log.d(TAG, "加载中=============================");
                //进入主界面前获取服务器端视频
                getVideo();
                Thread.sleep(500);
                Log.d(TAG, "加载完成=============================");
                return users[0];
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

//        //在主线程中显示线程任务的执行进度，在doInBackground方法中调用publishProgress方法则触发该方法
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//
//        }

        //接受线程任务的执行结果
        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        //取消(cancel)异步任务时触发该方法
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    //登录页面不可见时cancel AsyncTASK
    @Override
    protected void onStop() {
        super.onStop();
        //pTask.cancel(true);
    }
}
