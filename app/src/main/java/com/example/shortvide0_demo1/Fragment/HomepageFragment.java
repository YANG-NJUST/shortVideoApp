package com.example.shortvide0_demo1.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shortvide0_demo1.MenuActivity;
import com.example.shortvide0_demo1.R;

public class HomepageFragment extends Fragment {


    boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);


        //视频信息填充页面(视频，用户名，视频描述信息)

        //实例化适配器(RecyclerView.Adapter)
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            //创建
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_video, parent, false);
                Log.d("HomepageFragment", "onCreateViewHolder: success");
                return new ViewHolder(v);
            }

            //绑定（为ViewHolder里面的控件设置显示内容）
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//                ViewHolder holder1 = (ViewHolder) holder;
//                holder1.videoView.setVideoPath(.get(position));
//                holder1.videoView.start();
                ViewHolder holder1 = (ViewHolder) holder;
                holder1.videoView.setVideoURI(Uri.parse(MenuActivity.videoList.get(position).getVideoUrl()));
                holder1.tvUserName.setText(MenuActivity.videoList.get(position).getUploadUser());
                holder1.tvVideoInfo.setText(MenuActivity.videoList.get(position).getVideoInfo());
                holder1.videoView.start();
                //videoView设置点击事件 控制视频的播放暂停并给提示图片
                holder1.videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder1.videoView.isPlaying()){
                            holder1.ivPlay.setVisibility(View.VISIBLE);
                            holder1.videoView.pause();
                        }else{
                            holder1.ivPlay.setVisibility(View.INVISIBLE);
                            holder1.videoView.start();
                        }
                    }
                });
            }

            //数量
            @Override
            public int getItemCount() {
                Log.d("HomepageFragment", "getItemCount: " + MenuActivity.videoList.size());
                return MenuActivity.videoList.size();
            }
        };


        //找到ViewPager 设置适配器
        ViewPager2 pager2 = (ViewPager2) view.findViewById(R.id.pagers_homepage);
        pager2.setAdapter(adapter);

        return view;

    }

    //自定义ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public VideoView videoView;
        public ImageButton imageButton;
        public TextView tvUserName;
        public TextView tvVideoInfo;
        private ImageView ivPlay;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvVideoInfo = itemView.findViewById(R.id.tv_video_info);
            ivPlay=itemView.findViewById(R.id.iv_play);
        }

    }


}
