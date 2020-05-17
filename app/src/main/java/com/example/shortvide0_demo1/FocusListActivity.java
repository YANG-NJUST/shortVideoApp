package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.shortvide0_demo1.Fragment.MineFragment;
import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.bean.FanFocus;
import com.example.shortvide0_demo1.bean.Video;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FocusListActivity extends AppCompatActivity {


    private List<Map<String, Object>> data = new ArrayList<>();
    private ListView focusListView;
    private List<Video> focusVideoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_list);

        focusListView = findViewById(R.id.focus_listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MenuActivity.focusList);
        focusListView.setAdapter(arrayAdapter);
        focusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userName = MenuActivity.focusList.get(position);
                //获取Video
                focusVideoList.clear();
                for (Video video : LoginActivity.videoList) {
                    if(video.getUploadUser().equals(userName)){
                        focusVideoList.add(video);
                    }
                }
                Intent intent=new Intent(FocusListActivity.this, ShowVideoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("VideoList",(Serializable) focusVideoList);
                intent.putExtras(bundle);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });
    }

}
