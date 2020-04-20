package com.example.shortvide0_demo1.Fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.shortvide0_demo1.R;

public class HomepageFragment extends Fragment {


    private VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        videoView = (VideoView) view.findViewById(R.id.videoView_homepage);
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/shanghai.mp4");
        videoView.start();
        MediaController controller = new MediaController(getActivity());
        videoView.setMediaController(controller);
        controller.setMediaPlayer(videoView);
        return view;
    }

}
