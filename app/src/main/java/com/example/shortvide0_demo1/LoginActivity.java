package com.example.shortvide0_demo1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.shortvide0_demo1.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //声明控件
    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private String JsonObjectMsg;
    private HashMap<Integer, User> userInfo = new HashMap<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //控件初始化
        initView();
        //控件点击事件
        initEvent();
        verifyStoragePermissions(this);
    }

    @Override
    protected void onResume() {
        //读取服务器中所有用户信息来执行后续登录操作
        super.onResume();
        Log.d("TAG", "LoginActivity的onResume执行了");
        final RegisterActivity registerActivity = new RegisterActivity();
        new Thread() {
            @Override
            public void run() {
                super.run();
                JsonObjectMsg = registerActivity.httpGet();
                try {
                    JSONArray ja = new JSONArray(JsonObjectMsg);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        userInfo.put(jo.getInt("id"),
                                new User(jo.getInt("id"), jo.getString("useraccount"), jo.getString("userpwd")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
            //遍历存储用户信息的HashMap
            Iterator iterator = userInfo.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                User user = (User) entry.getValue();
                if (user.getUserName().equals(mEtAccount.getText().toString()) &&
                        user.getUserPassword().equals(mEtPassword.getText().toString())) {
                    Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MenuActivity.class));
                    return;
                }
            }
            Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
        }
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        String msg=data.getStringExtra("UserJsonObject");
//        Log.d("TAG",msg );
//        //解析JSONObject取出传回的用户名和密码
//        try {
//            JSONArray ja=new JSONArray(msg);
//            JSONObject jo = ja.getJSONObject(0);
//            int id = jo.getInt("id");
//            String useraccount = jo.getString("useraccount");
//            String userpwd = jo.getString("userpwd");
//            //Log.d("TAG", "id="+id+"&useraccount"+useraccount+"&userpwd="+userpwd);
//            mEtAccount.setText(useraccount);
//            mEtPassword.setText(userpwd);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        mEtAccount.setText(data.getStringExtra("account"));
        mEtPassword.setText(data.getStringExtra("pwd"));
    }

    private void initView() {
        mEtAccount = findViewById(R.id.et_login_account);
        mEtPassword = findViewById(R.id.et_login_password);
        mEtAccount.setText("admin");
        mEtPassword.setText("123");
        mBtnLogin = findViewById(R.id.login_button);
        mBtnRegister = findViewById(R.id.register_button);
    }

}
