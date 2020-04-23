package com.example.shortvide0_demo1.net.interceptor;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request originRequest = chain.request();

        Request newRequest = originRequest.newBuilder()
                .addHeader("author", "yang_interceptor")
                .build();

        return chain.proceed(newRequest);
    }
}

