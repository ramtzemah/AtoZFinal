package com.example.atoz2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final String LEVEL = "LECEL";
    private TextView title;
    private TextView timer;
    private ImageView bulb;
    private ImageView back;
    private Button click;
    private Button retry;
    private Double time;
    private int DELAY = 7000;
    private TimerTask tsRandom;
    private TimerTask tsCount;
    private TimerTask tsStart;
    private boolean firstTime;
    private Random random;
    private Timer timerClock;
    private int hardLevel = 5;
    private int counter = 0;
    private RewardedAd mRewardedAd;
    private boolean removeAds = false;
    private FrameLayout main_LAY_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        checkPremium();

        Bundle extras = getIntent().getBundleExtra("Bundle");
        if (extras != null) {
            String level = extras.getString(LEVEL);
            switch ( extras.getString(LEVEL)){
                case "easy":
                    hardLevel = 6;
                    break;
                case "hard":
                    hardLevel = 9;
                    break;
            }
        }

        manageTheGame();

        click.setOnClickListener(view ->
                afterClick(time));

        retry.setOnClickListener(view ->
                manageTheGame());

        back.setOnClickListener(view ->
                exit());
    }

    private void checkPremium() {
        loadVideoAd();
        int js1 = MSPV.getMe().getInt("removeAds", 0);
        if(js1!=0){
            if ( js1 > 0){
                removeAds = true;
            }else{
                removeAds = false;
            }
        }
        if(removeAds == false){
            addBanner();
        }
    }

    private void showBanner() {
//        String UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
        String UNIT_ID = BuildConfig.ADMOB_BANNER_AD_ID;
        AdView adView = new AdView(this);
        adView.setAdUnitId(UNIT_ID);
        main_LAY_banner.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    private void addBanner() {
        showBanner();
    }

    private void waitForStart() {
        if(!firstTime){
            firstTime = true;
        }else {
            tsStart.cancel();
        }
    }


    private void exit() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void manageTheGame() {
        retry.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.INVISIBLE);
        click.setVisibility(View.INVISIBLE);
        bulb.setImageResource(R.drawable.light_bulb_off);
        title.setText("Wait..");
        time = 0.0;
        callTimerOfRandomise();
    }

    private void callTimerOfRandomise() {
        timerClock.scheduleAtFixedRate(tsRandom = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    lightTheBulb();
                });
            }
        }, 0, DELAY);
    }

    private void lightTheBulb() {
        if(random.nextInt(11) > hardLevel){
            whenChange();
            tsRandom.cancel();
        }
    }

    private void whenChange(){
        sound();
        click.setVisibility(View.VISIBLE);
        click.setVisibility(View.VISIBLE);
        bulb.setImageResource(R.drawable.light_bulb_on);
        title.setText("Click");
        callTimerOfCount();
    }

    private void callTimerOfCount() {
        timerClock.scheduleAtFixedRate(tsCount = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    addSecond();
                });
            }
        }, 0, 10);
    }

    private void addSecond() {
        time += 0.01;
    }

    private void afterClick(double time){
        counter++;
        tsCount.cancel();
        title.setText("Done");
        timer.setText(String.format("%,.3f", time));
        click.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.VISIBLE);
        retry.setVisibility(View.VISIBLE);

        if( counter % 2 == 0){
            if(!removeAds){
                ad();
            }
        }
    }

    private void ad() {
        showVideoAd();
    }

    private void showVideoAd() {
        if (mRewardedAd != null) {
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    loadVideoAd();
                }
            });
        } else {
            loadVideoAd();
        }
    }

    private void loadVideoAd() {
        String UNIT_ID = BuildConfig.ADMOB_VIDEO_AD_ID;

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, UNIT_ID,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("pttt", loadAdError.toString());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        //action_b.setEnabled(true);
                        Log.d("pttt", "Ad was loaded.");
                    }
                });
    }


    private void findViews() {
        title = findViewById(R.id.title);
        timer = findViewById(R.id.timer);
        bulb = findViewById(R.id.bulb);
        retry = findViewById(R.id.retry);
        back = findViewById(R.id.back);
        click = findViewById(R.id.click);
        main_LAY_banner = findViewById(R.id.main_LAY_banner);
        random = new Random();
        timerClock = new Timer();
        firstTime = false;
    }

    private void sound() {
        MediaActionSound sound = new MediaActionSound();
        sound.play(MediaActionSound.START_VIDEO_RECORDING);
    }
}