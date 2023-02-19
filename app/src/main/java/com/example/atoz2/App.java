package com.example.atoz2;

import android.app.Application;

//import com.example.atoz2.DataManager;
import com.google.android.gms.ads.MobileAds;
import com.example.atoz2.MyInApp.Item;

public class App extends Application {
    Item[] items = new Item[]{
            new Item(MyInApp.TYPE.Subscription, ""),
            new Item(MyInApp.TYPE.OneTimeInApp, ""),
    };

    String LICENSE_KEY = "sdsdfdsfdsfffds";

    @Override
    public void onCreate() {
        super.onCreate();
        MSPV.initHelper(this);
        MobileAds.initialize(this);
        MyInApp.initHelper(this, LICENSE_KEY, items);
    }
}