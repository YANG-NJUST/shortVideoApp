package com.example.shortvide0_demo1;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shortvide0_demo1.net.Constant;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 上传视频
 */
public class AddVideoActivity extends AppCompatActivity {


    private static final String TAG = "AddVideoActivity";
    public static final String url = Constant.url+"/file/uploadFile";
    private int REQUEST_VIDEO_CODE_1 = 1;
    private int REQUEST_VIDEO_CODE_2 = 2;
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
                AlertDialog.Builder builder=new AlertDialog.Builder(AddVideoActivity.this);
                builder.setTitle("请选择");
                builder.setPositiveButton("本地视频", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_VIDEO_CODE_1);
                    }
                });
                builder.setNeutralButton("录制视频", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(AddVideoActivity.this,RecordVideoActivity.class);
                        startActivityForResult(intent,REQUEST_VIDEO_CODE_2);
                    }
                });
                builder.show();
            }
        });


        /**
         * 上传视频
         */
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPath == null) {
                    Toast.makeText(AddVideoActivity.this, "请先添加视频！", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(etInfo.getText())){
                    Toast.makeText(AddVideoActivity.this,"视频描述信息不能为空！",Toast.LENGTH_SHORT).show();
                }else{
                    File file = new File(videoPath);
                    OkHttpClient httpClient = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/octet-stream");
                    RequestBody requestBody = RequestBody.create(mediaType, file);
                    MultipartBody multipartBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uploaduser", getIntent().getStringExtra("userAccount"))
                            .addFormDataPart("info", etInfo.getText().toString())
                            .addFormDataPart("fileName", file.getName(), requestBody)
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(multipartBody)
                            .build();
                    OkHttpUtils.getInstance().executeRequest(new INetCallBack() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(AddVideoActivity.this, "上传视频成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailed(Throwable ex) {
                            Toast.makeText(AddVideoActivity.this, "上传视频失败", Toast.LENGTH_SHORT).show();
                        }
                    },request);
//                    Call call = httpClient.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                            Toast.makeText(AddVideoActivity.this, "上传视频失败", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                            Toast.makeText(AddVideoActivity.this, "上传视频成功", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CODE_1) {
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
                        Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MINI_KIND);
                        ivAddVideo.setImageBitmap(bitmap2);
                    }
                }
            }else if(requestCode == REQUEST_VIDEO_CODE_2){
                videoPath= data.getStringExtra("uri");
                //获取缩略图 显示
                Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(videoPath,MediaStore.Video.Thumbnails.MINI_KIND);
                ivAddVideo.setImageBitmap(bitmap);
            }


        }
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind); //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        if(bitmap!= null){
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        }
        return bitmap;
    }

    /**
     * 获取视频文件截图
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    public  static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return  media.getFrameAtTime();
    }

}
