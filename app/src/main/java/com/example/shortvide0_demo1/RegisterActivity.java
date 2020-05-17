package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.bean.User;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText regiAccount, regiPassword, regiPassword2;
    private List<User> userList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //控件初始化
        regiAccount = findViewById(R.id.et_register_account);
        regiPassword = findViewById(R.id.et_register_password);
        regiPassword2 = findViewById(R.id.et_register_password2);
        //接受从LoginActivity传递的userList；
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        userList= (List<User>) bundle.getSerializable("userList");
        Log.d("TAG", "userList: "+userList.size());
    }


    public void myClick(View view) {
        //注册账号密码不能为空
        //两次密码必须一致
        //用户名重复判断
        if (TextUtils.isEmpty(regiAccount.getText()) || TextUtils.isEmpty(regiPassword.getText()) || TextUtils.isEmpty(regiPassword2.getText())) {
            Toast.makeText(this, "请输入账号密码!", Toast.LENGTH_SHORT).show();
        } else if (!regiPassword.getText().toString().equals(regiPassword2.getText().toString())) {
            Toast.makeText(this, "两次密码输入不一致!", Toast.LENGTH_SHORT).show();
        } else if (userIsExist(regiAccount.getText().toString())) {
            Toast.makeText(this, "用户名已存在！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "注册成功!", Toast.LENGTH_SHORT).show();
            HashMap<String, String> params = new HashMap<>();
            params.put("account",regiAccount.getText().toString());
            params.put("pwd",regiPassword.getText().toString());
            OkHttpUtils.getInstance().doPost(Constant.url+"/user", null,
                    params, new INetCallBack() {
                        @Override
                        public void onSuccess(String response) {
                            Log.d("注册成功后服务器返回的response：", response);
                            Intent intent=new Intent();
                            intent.putExtra("account",regiAccount.getText().toString());
                            intent.putExtra("pwd",regiPassword.getText().toString());
                            setResult(RESULT_OK,intent);
                            finish();
                        }

                        @Override
                        public void onFailed(Throwable ex) {
                            Toast.makeText(RegisterActivity.this,"创建用户失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    //判断用户要注册的用户名是否已存在
    public boolean userIsExist(final String account) {
        for (User user : userList) {
            if(account.equals(user.getUserAccount())){
                return true;
            }
        }
        return false;
    }
}
