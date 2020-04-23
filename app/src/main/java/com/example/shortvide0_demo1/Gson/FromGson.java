package com.example.shortvide0_demo1.Gson;

import android.util.Log;

import com.example.shortvide0_demo1.bean.User;
import com.example.shortvide0_demo1.bean.Video;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 * 使用GSON解析Json数据
 */
public class FromGson {

    private static FromGson sInstance = new FromGson();

    public static FromGson getInstance() {
        return sInstance;
    }

    /**
     * 解析获取的视频信息转换为Video类
     * @param jsonData
     * @return
     */
    public List getVideoBean(String jsonData) {
        Gson gson = new Gson();//创建Gson对象
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(jsonData).getAsJsonArray();//获取JsonArray对象
        List<Video> videoList = new ArrayList<>();
        for (JsonElement element : jsonElements) {
            Video video = gson.fromJson(element, Video.class);//解析
            videoList.add(video);
            Log.d("FromGson", video.toString());
        }
        Log.d("FromGson", "videoList size= "+videoList.size());
        return videoList;
    }

    /**
     * 解析获取的用户信息转换为User类
     * @param jsonData
     * @return
     */
    public List getUserBean(String jsonData){
        Gson gson = new Gson();
        JsonParser jsonParser=new JsonParser();
        JsonArray jsonElements=jsonParser.parse(jsonData).getAsJsonArray();
        List<User> userList=new ArrayList<>();
        for (JsonElement element : jsonElements) {
            User user=gson.fromJson(element,User.class);
            userList.add(user);
            Log.d("FromGson", user.toString());
        }
        return userList;
    }


}









