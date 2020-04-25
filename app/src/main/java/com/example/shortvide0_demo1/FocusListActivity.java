package com.example.shortvide0_demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.shortvide0_demo1.Fragment.MineFragment;
import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.bean.FanFocus;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FocusListActivity extends AppCompatActivity {


    private List<Map<String, Object>> data = new ArrayList<>();
    private ListView focusListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_list);

        focusListView = findViewById(R.id.focus_listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MineFragment.focusList);
//        String[] from={"focusUserName"};
//        int[] to={R.id.focus_user_name};
//        SimpleAdapter adapter=new SimpleAdapter(this,focusList,R.layout.item_focus,from,to);

        focusListView.setAdapter(arrayAdapter);
    }

}
