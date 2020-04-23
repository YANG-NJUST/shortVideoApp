package com.example.shortvide0_demo1.net;

public interface INetCallBack {
    void onSuccess(String response);

    void onFailed(Throwable ex);
}