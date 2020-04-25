package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.shortvide0_demo1.Fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FanListActivity extends AppCompatActivity {

    private List<Map<String, Object>> data = new ArrayList<>();
    private ListView fanListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_list);

        fanListView = findViewById(R.id.fan_listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MineFragment.fanList);
        fanListView.setAdapter(arrayAdapter);
    }
}
