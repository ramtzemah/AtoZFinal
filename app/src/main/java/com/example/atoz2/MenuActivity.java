package com.example.atoz2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MenuActivity extends AppCompatActivity {
    private Button easy;
    private Button hard;
    private Button shop;
    private RewardedAd mRewardedAd;
    private FrameLayout main_LAY_banner;
    private boolean subscription = false;
    private boolean removeAds = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        checkPremium();

        easy.setOnClickListener(view ->
                easyClick()
        );

        hard.setOnClickListener(view ->
                hardClick()
        );

        shop.setOnClickListener(view ->
                openShop());

    }

    private void openShop() {
        Intent intent = new Intent(MenuActivity.this, ShopActivity.class);
        startActivity(intent);
    }

    private void ad() {
        showVideoAd();
    }

    private void showBanner() {
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
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });
    }

    private void checkPremium() {
        checksServer();
        Long tsLong = System.currentTimeMillis();
        long js = MSPV.getMe().getLong("Subscription", 0);
        if(js != 0){
            if ( js > tsLong){
                subscription = true;
            }else{
                subscription = false;
            }
        }

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
        loadVideoAd();
    }

    private void checksServer() {
        // if real money check in firebase (because cancellation and staff like that)
    }

    private void hardClick() {
        if (subscription) {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.LEVEL, "hard");
            intent.putExtra("Bundle", bundle);
            startActivity(intent);
        }else{
          //  "Must be subscription to play in this level",
             Toast.makeText(getApplicationContext(),"Must be subscription to play in this level",Toast.LENGTH_SHORT).show();
        }
    }

    private void easyClick() {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.LEVEL,"easy");
        intent.putExtra("Bundle", bundle);
        startActivity(intent);
    }


    private void findViews() {
        easy = findViewById(R.id.easy);
        hard = findViewById(R.id.hard);
        shop = findViewById(R.id.shop);
        main_LAY_banner = findViewById(R.id.main_LAY_banner);
    }
}