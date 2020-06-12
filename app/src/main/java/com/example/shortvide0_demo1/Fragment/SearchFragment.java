package com.example.shortvide0_demo1.Fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shortvide0_demo1.MenuActivity;
import com.example.shortvide0_demo1.net.Constant;
import com.example.shortvide0_demo1.Gson.FromGson;
import com.example.shortvide0_demo1.R;
import com.example.shortvide0_demo1.bean.Video;
import com.example.shortvide0_demo1.net.INetCallBack;
import com.example.shortvide0_demo1.net.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private Button searchButton;
    private static final String TAG = "SearchFragment";
    private String urlLove = Constant.url + "/videoLove/";
    private String urlFocus = Constant.url + "/focus?";
    private String urlSearchByInfo = Constant.url + "/videoSearchByInfo/";
    private String urlSearchByUser = Constant.url + "/videoSearchByUser/";
    private String searchInfo;
    private List<Video> searchVideoList = new ArrayList<>();
    private View view;
    private ProgressBar pbSearch;
    private RecyclerView.Adapter adapter;
    private ProgressTask pTask;
    private ViewPager2 pager2;
    private Spinner searchSpinner;
    public boolean isFocused;//是否关注

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton = view.findViewById(R.id.btn_search);
        searchInput = view.findViewById(R.id.et_search);
        pbSearch = view.findViewById(R.id.pb_search);
        searchSpinner = view.findViewById(R.id.searchSpinner);

        //搜索功能
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchInput.getText())) {
                    Toast.makeText(getContext(), "请输入搜索信息！", Toast.LENGTH_SHORT).show();
                } else {
                    pager2.setVisibility(View.INVISIBLE);
                    pbSearch.setVisibility(View.VISIBLE);
                    pTask = new ProgressTask();
                    pTask.execute();
                }
            }
        });

        //视频信息填充页面(视频，用户名，视频描述信息)
        showVideo();

        return view;

    }


    private void showVideo() {
        //实例化适配器(RecyclerView.Adapter)
        adapter = new RecyclerView.Adapter() {
            //创建
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_video, parent, false);
                Log.d(TAG, "onCreateViewHolder: success");
                return new ViewHolder(v);
            }

            //绑定（为ViewHolder里面的控件设置显示内容）
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ViewHolder holder1 = (ViewHolder) holder;
                holder1.videoView.setVideoURI(Uri.parse(searchVideoList.get(position).getVideoUrl()));
                holder1.tvUserName.setText(searchVideoList.get(position).getUploadUser());
                holder1.tvVideoInfo.setText(searchVideoList.get(position).getVideoInfo());
                holder1.tvLoveAmount.setText(searchVideoList.get(position).getLoveAmount() + "");
                //若已关注 图标变为红色 状态为修改
                isFocused = false;
                for (String focusUser : MenuActivity.focusList) {
                    if (focusUser.equals(searchVideoList.get(position).getUploadUser())) {
                        holder1.imageButtonFocus.setImageResource(R.mipmap.icon_focused);
                        isFocused = true;
                    }
                }
                holder1.videoView.start();
                //videoView设置点击事件 控制视频的播放暂停并给提示图片
                holder1.videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder1.videoView.isPlaying()) {
                            holder1.ivPlay.setVisibility(View.VISIBLE);
                            holder1.videoView.pause();
                        } else {
                            holder1.ivPlay.setVisibility(View.INVISIBLE);
                            holder1.videoView.start();
                        }
                    }
                });
                //点赞按钮点击事件
                holder1.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点赞数+1
                        int amount = searchVideoList.get(position).getLoveAmount() + 1;
                        //不允许重复点赞
                        if (holder1.tvLoveAmount.getText().equals(amount + "")) {
                            Toast.makeText(getActivity(), "请勿重复点赞", Toast.LENGTH_SHORT).show();
                        } else {
                            //该视频的点赞数量+1
                            OkHttpUtils.getInstance().doGet(urlLove + searchVideoList.get(position).getId()
                                    , null, new INetCallBack() {
                                        @Override
                                        public void onSuccess(String response) {
                                            holder1.imageButton.setImageResource(R.mipmap.icon_loveit_selected);
                                            holder1.tvLoveAmount.setText(amount + "");
                                        }

                                        @Override
                                        public void onFailed(Throwable ex) {
                                            Toast.makeText(getActivity(), "点赞失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
                //关注按钮点击事件
                holder1.imageButtonFocus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isFocused){
                            Toast.makeText(getActivity(), "请勿重复关注！", Toast.LENGTH_SHORT).show();
                        }else if(MenuActivity.user.getUserAccount().equals(searchVideoList.get(position).getUploadUser())){
                            Toast.makeText(getActivity(),"无法关注自己！",Toast.LENGTH_SHORT).show();
                        } else{
                            HashMap<String, String> params = new HashMap<>();
                            params.put("user",MenuActivity.user.getUserAccount());
                            params.put("focusUser",searchVideoList.get(position).getUploadUser());
                            OkHttpUtils.getInstance().doPost(urlFocus, null, params, new INetCallBack() {
                                @Override
                                public void onSuccess(String response) {
                                    holder1.imageButtonFocus.setImageResource(R.mipmap.icon_focused);
                                    isFocused = true;
                                    Toast.makeText(getActivity(),"关注成功！",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(Throwable ex) {
                                    Toast.makeText(getActivity(),"关注失败！请检查网络连接",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            //数量
            @Override
            public int getItemCount() {
                Log.d(TAG, "getItemCount: " + searchVideoList.size());
                return searchVideoList.size();
            }
        };


        //找到ViewPager 设置适配器
        pager2 = (ViewPager2) view.findViewById(R.id.pagers_search);
        pager2.setAdapter(adapter);
    }


    //搜索视频功能
    private void search() {
        String select = searchSpinner.getSelectedItem().toString();
        searchInfo = searchInput.getText().toString();
        searchVideoList.clear();
        if (select.equals("用户")) {
            OkHttpUtils.getInstance().doGet(urlSearchByUser + searchInfo, null, new INetCallBack() {
                @Override
                public void onSuccess(String response) {
                    searchVideoList = FromGson.getInstance().getVideoBean(response);
                    Toast.makeText(getContext(), "根据用户名搜索到" + searchVideoList.size() + "个符合条件的视频", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Throwable ex) {
                    Toast.makeText(getContext(), "搜索失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (select.equals("视频")) {
            OkHttpUtils.getInstance().doGet(urlSearchByInfo + searchInfo, null, new INetCallBack() {
                @Override
                public void onSuccess(String response) {
                    searchVideoList = FromGson.getInstance().getVideoBean(response);
                    Toast.makeText(getContext(), "根据视频信息搜索到" + searchVideoList.size() + "个符合条件的视频", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Throwable ex) {
                    Toast.makeText(getContext(), "搜索失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public VideoView videoView;
        public ImageButton imageButton;
        public TextView tvUserName;
        public TextView tvVideoInfo;
        public ImageView ivPlay;
        public TextView tvLoveAmount;
        public ImageButton imageButtonFocus;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvVideoInfo = itemView.findViewById(R.id.tv_video_info);
            ivPlay = itemView.findViewById(R.id.iv_play);
            imageButton = itemView.findViewById(R.id.button_loveit);
            tvLoveAmount = itemView.findViewById(R.id.love_amount_textView);
            imageButtonFocus=itemView.findViewById(R.id.button_focus);
        }

    }

    /**
     * Params:   execute方法的参数类型，doInBackground方法的参数类型
     * Progress:进度,Integer
     * Result:
     */
    class ProgressTask extends AsyncTask<Void, Void, Void> {

        //执行线程任务之前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //作用：接受输入的参数，执行任务中的耗时操作，返回线程任务的执行结果
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d(TAG, "加载中=============================");
                //进入主界面前获取服务器端视频
                search();
                Thread.sleep(500);
                Log.d(TAG, "加载完成=============================");
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

//        //在主线程中显示线程任务的执行进度，在doInBackground方法中调用publishProgress方法则触发该方法
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//
//        }

        //接受线程任务的执行结果
        @Override
        protected void onPostExecute(Void Void) {
            super.onPostExecute(null);
            pbSearch.setVisibility(View.GONE);
            pager2.setVisibility(View.VISIBLE);
            showVideo();
        }

        //取消(cancel)异步任务时触发该方法
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

}
