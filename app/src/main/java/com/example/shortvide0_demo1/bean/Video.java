package com.example.shortvide0_demo1.bean;

import androidx.annotation.NonNull;

public class Video {

    private int id;

    private String videoUrl;//视频url地址

    private String uploadUser;//上传视频的用户

    private int loveAmount;//该视频的点赞数

    private String videoInfo;//视频的描述信息

    public Video(){

    }

    public Video(int id, String videoUrl, String uploadUser, int loveAmount, String videoInfo) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.uploadUser = uploadUser;
        this.loveAmount = loveAmount;
        this.videoInfo = videoInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public int getLoveAmount() {
        return loveAmount;
    }

    public void setLoveAmount(int loveAmount) {
        this.loveAmount = loveAmount;
    }

    public String getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(String videoInfo) {
        this.videoInfo = videoInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return "id:"+this.getId()+",uploadUser:"+this.getUploadUser()+",videoUrl:"+this.getVideoUrl()+",videoInfo:"+this.getVideoInfo()+",loveAmount:"+this.getLoveAmount();
    }
}
