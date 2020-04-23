package com.example.shortvide0_demo1;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 上传视频
 */
public class AddVideoActivity extends AppCompatActivity {


    private static final String TAG = "AddVideoActivity";
    public static final String HTTP_192_168_0_100_8080_DEMO_FILE_UPLOAD_FILE = "http://192.168.0.100:8080/demo/file/uploadFile";
    private int REQUEST_VIDEO_CODE = 1;
    private ImageView ivAddVideo;
    private Button btnCommit;
    private EditText etInfo;
    private String videoPath;//上传视频的路径

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvideo);
        btnCommit = findViewById(R.id.btn_commit);
        ivAddVideo = findViewById(R.id.iv_addVideo);
        etInfo = findViewById(R.id.et_info);
        //从本地选择视频
        ivAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_VIDEO_CODE);
            }
        });
        //上传视频
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(videoPath);
                OkHttpClient httpClient=new OkHttpClient();
                MediaType mediaType=MediaType.parse("application/octet-stream");
                RequestBody requestBody=RequestBody.create(mediaType,file);
                MultipartBody multipartBody=new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uploaduser",getIntent().getStringExtra("userAccount"))
                        .addFormDataPart("info",etInfo.getText().toString())
                        .addFormDataPart("fileName",file.getName(),requestBody)
                        .build();
                Request request=new Request.Builder()
                        .url(HTTP_192_168_0_100_8080_DEMO_FILE_UPLOAD_FILE)
                        .post(multipartBody)
                        .build();
                Call call=httpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Toast.makeText(AddVideoActivity.this,"上传视频失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Toast.makeText(AddVideoActivity.this,"上传视频成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CODE) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                Cursor cursor = cr.query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // 视频ID:MediaStore.Audio.Media._ID
                        int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        // 视频名称：MediaStore.Audio.Media.TITLE
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        // 视频路径：MediaStore.Audio.Media.DATA
                        videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        // 视频时长：MediaStore.Audio.Media.DURATION
                        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        // 视频大小：MediaStore.Audio.Media.SIZE
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                        // 视频缩略图路径：MediaStore.Images.Media.DATA
                        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        // 缩略图ID:MediaStore.Audio.Media._ID
                        int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                        //ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                        // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
                        Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                        Log.d(TAG, "videoPath: " + videoPath);
                        Log.d(TAG, "duration: " + duration);
                        Log.d(TAG, "title: " + title);
                        Log.d(TAG, "videoId: " + videoId);
                        Log.d(TAG, "size: " + size);
                        Log.d(TAG, "imagePath: " + imagePath);
                        Log.d(TAG, "imageId: " + imageId);
                        ivAddVideo.setImageBitmap(bitmap2);
                    }
                }
            }
        }
    }

}
