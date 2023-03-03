package com.jkcq.homebike.ride.course;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.jkcq.homebike.R;
import com.jkcq.homebike.util.FileUtil;

import java.io.File;

public class CourseTestActivity extends Activity {

    ZVideoView video_loader;
    ImageView iv_option_pause;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_ride);
        video_loader=findViewById(R.id.video_loader);
        iv_option_pause=findViewById(R.id.iv_option_pause);

       String path = getIntent().getStringExtra("path");
        File file =new File(
                FileUtil.getVideoCorseDir().toString() + path
        );
        video_loader.setVideoURI(Uri.parse(file.getPath()));
        video_loader.start();
        iv_option_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_loader.pause();
            }
        });
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }*/
}
