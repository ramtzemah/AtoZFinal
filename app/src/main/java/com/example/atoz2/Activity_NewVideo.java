package com.example.atoz2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atoz2.R;
import com.example.atoz2.VideoAd;
import com.google.android.material.button.MaterialButton;

public class Activity_NewVideo extends AppCompatActivity {

    public static final String TAG = "PTTT_Activity_VideoNew";

    private MaterialButton action_a;


    VideoAd coinVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        action_a.setOnClickListener(view -> start());

        initAds();
    }

    private void start() {
        if (coinVideo.isLoaded()) {
            action_a.setEnabled(false);
            coinVideo.show();
        } else {
            initAds();
            // move to next level
        }
    }

    VideoAd.CallBack callBack = new VideoAd.CallBack() {
        @Override
        public void unitLoaded() {
            action_a.setEnabled(true);
        }

        @Override
        public void earned() {

        }

        @Override
        public void canceled() {

        }

        @Override
        public void failed() {

        }
    };

    private void initAds() {
        action_a.setEnabled(false);
//        String UNIT_ID = "ca-app-pub-3940256099942544~3347511713";
        String UNIT_ID = "ca-app-pub-9282786562744054~4212167008";
        coinVideo = new VideoAd(this, UNIT_ID, callBack);
    }


    private void findViews() {
        //add if premium
      //  action_a = findViewById(R.id.action_a);
    }

}