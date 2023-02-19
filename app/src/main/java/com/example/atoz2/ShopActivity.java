package com.example.atoz2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ShopActivity extends AppCompatActivity {

    private Button adsFree;
    private Button subShort;
    private Button subLong;
    private ImageView back;

    private boolean subscription;
    private boolean removeAds;
    private final long MONTHtIMESRAMP = 2678400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        findViews();

        adsFree.setOnClickListener(view -> askRemoveAds());
        subShort.setOnClickListener(view -> askSubscription(1));
        subLong.setOnClickListener(view -> askSubscription(3));
        back.setOnClickListener(view -> exit());

        checkPremium();
    }

    private void checkPremium() {
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
    }

    private void askRemoveAds() {
        if(!removeAds){
            dialog("Remove ads" , 3);
        }else{
            Toast.makeText(ShopActivity.this, "You already removed the ads", Toast.LENGTH_SHORT).show();
        }
    }

    private void RemoveAds(){
        MSPV.getMe().putInt("removeAds",1);
    }

    private void askSubscription(int months){
        if(!subscription){
            if(months == 1){
                dialog1("Subscription" , 5, months);
            }else {
                dialog1("Subscription" , 10, months);
            }
        }else{
            Toast.makeText(ShopActivity.this, "You already subscription", Toast.LENGTH_SHORT).show();
        }
    }

    private void Subscription(int months){
        Long tsLong = System.currentTimeMillis();
        MSPV.getMe().putLong("Subscription", ((Long.valueOf(months * MONTHtIMESRAMP)) + tsLong));
    }

    private void dialog(String type , int price){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        removeAds = true;
                        RemoveAds();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
        builder.setMessage("Are you sure you want buy " + type + " in" + price + "$" + "?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void dialog1(String type , int price, int months){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        subscription = true;
                        Subscription(months);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
        builder.setMessage("Are you sure you want buy " + type + " in" + price + "$" + "?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void findViews() {
        adsFree = findViewById(R.id.adsFree);
        subShort = findViewById(R.id.subShort);
        subLong = findViewById(R.id.subLong);
        back = findViewById(R.id.back);
    }

    private void exit() {
        Intent intent = new Intent(ShopActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}