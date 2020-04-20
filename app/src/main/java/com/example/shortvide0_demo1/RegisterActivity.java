package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText regiAccount, regiPassword, regiPassword2;
    private String JsonObjectMsg;
    private List<String> accountList= new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //控件初始化
        regiAccount = findViewById(R.id.et_register_account);
        regiPassword = findViewById(R.id.et_register_password);
        regiPassword2 = findViewById(R.id.et_register_password2);
        //取出当前服务器里所有用户的账号 以便后面做判断 不能有重复的用户名
        getUser();

    }

    public void getUser() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //向服务器发送GET请求拿到返回的JSON数据
                JsonObjectMsg=httpGet();
                try {
                    JSONArray ja = new JSONArray(JsonObjectMsg);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        accountList.add(jo.getString("useraccount"));
                        Log.d("TAG", jo.getString("useraccount"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected String httpGet() {
        try {
            //1.实例化一个URL对象
            URL url = new URL("http://192.168.0.100:8088/demo1/user");
            //2.获取HttpURLConnection实例
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //3.设置和请求相关的属性
            //请求方式
            conn.setRequestMethod("GET");
            //请求超时时长
            conn.setConnectTimeout(6000);
            //4.获取响应码 200:成功 404:NotFound 500:服务器异常
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //5.判断响应码并获取响应数据（响应的正文）
                //获取响应的流
                InputStream in = conn.getInputStream();
                //在循环中读取输入流
                byte[] b = new byte[1024];
                int len = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //在循环中读取输入流
                //in.read(b); //该方法返回值是int类型数据 代表的是实际读到的数据长度
                while ((len = in.read(b)) > -1) {
                    //将字节数组里面的内容写入缓存流
                    baos.write(b, 0, len);
                }
                String msg = new String(baos.toString());
                Log.e("TAG", msg);
                return msg;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void httpPost(String account, String pwd) {
        try {
            //1.实例化一个URL对象
            URL url = new URL("http://192.168.0.100:8088/demo1/user");
            //2.获取HttpURLConnection实例
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //3.设置和请求相关的属性
            //请求方式
            conn.setRequestMethod("POST");
            //请求超时时长
            conn.setConnectTimeout(6000);
            //设置允许输出
            conn.setDoOutput(true);
            //设置提交数据的类型
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //获取输出流(请求正文)
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(("account=" + account + "&pwd=" + pwd).getBytes());
            //4.获取响应码 200:成功 404:NotFound 500:服务器异常
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //5.判断响应码并获取响应数据（响应的正文）
                //获取响应的流
                InputStream in = conn.getInputStream();
                //在循环中读取输入流
                byte[] b = new byte[1024];
                int len = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //在循环中读取输入流
                //in.read(b); //该方法返回值是int类型数据 代表的是实际读到的数据长度
                while ((len = in.read(b)) > -1) {
                    //将字节数组里面的内容写入缓存流
                    baos.write(b, 0, len);
                }
                String msg = new String(baos.toString());
                Log.e("TAG", msg);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String account = regiAccount.getText().toString();
                    String pwd = regiPassword.getText().toString();
                    httpPost(account, pwd);
                    Intent intent = new Intent();
                    intent.putExtra("account", account);
                    intent.putExtra("pwd", pwd);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }.start();
        }

    }

    //判断用户要注册的用户名是否已存在
    public boolean userIsExist(final String account) {
        for (int i = 0; i < accountList.size(); i++) {
            if (account.equals(accountList.get(i))) {
                return true;
            }
        }
        return false;
    }
}
