package com.example.shortvide0_demo1.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.shortvide0_demo1.net.interceptor.AuthInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class OkHttpUtils {

    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private OkHttpClient mOkHttpClient;

    private OkHttpUtils() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("yang", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
//                .connectTimeout()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(logging)
                .build();
    }

    private static OkHttpUtils sInstance = new OkHttpUtils();

    public static OkHttpUtils getInstance() {
        return sInstance;
    }


    /**
     *
     * @param url
     * @param headers
     * @param callBack
     */
    public void doGet(String url, HashMap<String, String> headers, INetCallBack callBack) {
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder
                .url(url)
                .build();
        executeRequest(callBack, request);
    }

    /**
     *
     * @param url
     * @param headers
     * @param params
     * @param callBack
     */
    public void doPost(String url, HashMap<String, String> headers, HashMap<String, String> params, INetCallBack callBack) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (String param : params.keySet()) {
                formBodyBuilder.add(param, params.get(param));
            }
        }

        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        Request request = requestBuilder
                .url(url)
                .post(formBodyBuilder.build())
                .build();
        executeRequest(callBack, request);
    }

    public void doPostMultiPart(String url, HashMap<String, String> headers, HashMap<String, String> params, INetCallBack callBack) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        if (params != null) {
            for (String param : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(param, params.get(param));
            }
        }

        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        Request request = requestBuilder
                .url(url)
                .post(multipartBodyBuilder.build())
                .build();
        executeRequest(callBack, request);
    }

    public void doPostJson(String url, HashMap<String, String> headers, String jsonStr, INetCallBack callBack) {
        MediaType jsonMediaType = MediaType.get("application/json");
        RequestBody requestBody = RequestBody.create(jsonStr, jsonMediaType);

        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
        }

        Request request = requestBuilder
                .url(url)
                .post(requestBody)
                .build();

        executeRequest(callBack, request);
    }

    /**
     *
     * @param callBack
     * @param request
     */
    public void executeRequest(INetCallBack callBack, Request request) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String respStr = null;
                try {
                    respStr = response.body().string();
                } catch (IOException e) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(e);
                        }
                    });
                    return;
                }
                String finalRespStr = respStr;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalRespStr);
                    }
                });
            }
        });
    }

}
