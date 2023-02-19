package com.example.atoz2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout form_EDT_phoneNumber;
    private MaterialButton form_BTN_submit;
    private Button about;
    private Button privacyPolicy;
    private Button termsAndConditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        form_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = form_EDT_phoneNumber.getEditText().getText().toString();
                if(number.isEmpty()||number.length()<8){
                    form_EDT_phoneNumber.setError("number is required");
                    form_EDT_phoneNumber.requestFocus();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, VerifyPhoneNumber.class );
                Bundle bundle = new Bundle();
                bundle.putString(VerifyPhoneNumber.PHONENUMBER,number);
                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(view ->
                openHtmlTextDialog(LoginActivity.this, "privacyPolicy.html"));


        termsAndConditions.setOnClickListener(view ->
                openHtmlTextDialog(LoginActivity.this, "terms.html"));


        about.setOnClickListener(view ->
                openHtmlTextDialog(LoginActivity.this, "about.html"));
    }

    public static void openHtmlTextDialog(Activity activity, String fileNameInAssets) {
        String str = "";
        InputStream is = null;

        try {
            is = activity.getAssets().open(fileNameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        materialAlertDialogBuilder.setPositiveButton("Close", null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str));
        }

        AlertDialog al = materialAlertDialogBuilder.show();
        TextView alertTextView = al.findViewById(android.R.id.message);
        alertTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            String pNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            Intent intent = new Intent(this,MenuActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            Bundle bundle = new Bundle();
//            bundle.putString(VerifyPhoneNumber.PHONENUMBER,pNumber);
//            intent.putExtra("Bundle", bundle);
            startActivity(intent);
        }
    }

    private void findView() {
        form_EDT_phoneNumber = findViewById(R.id.form_EDT_phoneNumber);
        form_BTN_submit = findViewById(R.id.form_BTN_submit);
        about = findViewById(R.id.about);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        termsAndConditions = findViewById(R.id.termsAndConditions);
    }


}