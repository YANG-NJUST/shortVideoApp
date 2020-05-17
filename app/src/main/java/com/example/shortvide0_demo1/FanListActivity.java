package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.shortvide0_demo1.bean.Video;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FanListActivity extends AppCompatActivity {

    private List<Map<String, Object>> data = new ArrayList<>();
    private ListView fanListView;
    private List<Video> fanVideoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_list);

        fanListView = findViewById(R.id.fan_listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,MenuActivity.fanList);
        fanListView.setAdapter(arrayAdapter);
        fanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userName = MenuActivity.fanList.get(position);
                //获取Video
                fanVideoList.clear();
                for (Video video : LoginActivity.videoList) {
                    if(video.getUploadUser().equals(userName)){
                        fanVideoList.add(video);
                    }
                }
                Intent intent=new Intent(FanListActivity.this, ShowVideoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("VideoList",(Serializable) fanVideoList);
                intent.putExtras(bundle);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });
    }
}
