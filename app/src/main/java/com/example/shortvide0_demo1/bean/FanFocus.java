package com.example.shortvide0_demo1.bean;

import androidx.annotation.NonNull;

public class FanFocus {

    private String user;

    private String focusUser;

    public FanFocus() {
    }

    public FanFocus(String user, String focusUser) {
        this.user = user;
        this.focusUser = focusUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFocusUser() {
        return focusUser;
    }

    public void setFocusUser(String focusUser) {
        this.focusUser = focusUser;
    }

    @NonNull
    @Override
    public String toString() {
        return "用户:"+this.user+" 关注了:"+this.focusUser;
    }
}
