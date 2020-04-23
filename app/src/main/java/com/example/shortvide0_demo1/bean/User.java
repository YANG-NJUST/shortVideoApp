package com.example.shortvide0_demo1.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String userAccount;
    private String userPwd;
    private int fanAmount;//粉丝数量
    private int focusAmount;//关注人数
    private int loveAmount;//点赞数


    public User() {

    }

    public User(int id, String userName, String userPassword, int fanAmount, int focusAmount, int loveAmount) {
        this.id = id;
        this.userAccount = userName;
        this.userPwd = userPassword;
        this.fanAmount = fanAmount;
        this.focusAmount = focusAmount;
        this.loveAmount = loveAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getFanAmount() {
        return fanAmount;
    }

    public void setFanAmount(int fanAmount) {
        this.fanAmount = fanAmount;
    }

    public int getFocusAmount() {
        return focusAmount;
    }

    public void setFocusAmount(int focusAmount) {
        this.focusAmount = focusAmount;
    }

    public int getLoveAmount() {
        return loveAmount;
    }

    public void setLoveAmount(int loveAmount) {
        this.loveAmount = loveAmount;
    }

    @NonNull
    @Override
    public String toString() {
        return "id:"+this.getId()+",userAccount:"+this.getUserAccount()+",userPwd:"+this.getUserPwd()+",fanAmount:"+this.getFanAmount()
                +",focusAmount:"+this.getFocusAmount()+",loveAmount:"+this.loveAmount;
    }
}
