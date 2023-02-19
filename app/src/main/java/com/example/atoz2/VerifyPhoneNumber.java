package com.example.atoz2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumber extends AppCompatActivity {
    public static final String PHONENUMBER = "PHONENUMBER";
    private TextInputLayout form_EDT_code;
    private MaterialButton form_BTN_submit;
    private String verifcationId;
    private FirebaseAuth mAuth;
    private String phoneNumber;

    private static final int PERMISSION_SEND_SMS = 123;

    private String fullNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        findViews();
        String number;
        Bundle extras = getIntent().getBundleExtra("Bundle");
        if (extras != null) {
            phoneNumber = extras.getString(PHONENUMBER);
        }
        number = phoneNumber;
        String perfix = "+972";
        fullNumber = perfix+number.substring(1);
        requestSmsPermission();
//        sendVerfiyPhoneNumber(perfix+number.substring(1));
        form_BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = form_EDT_code.getEditText().getText().toString();

                if(code.isEmpty() || code.length()<6){
                    form_EDT_code.setError("Enter code..");
                    form_EDT_code.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential Credential = PhoneAuthProvider.getCredential(verifcationId,code);
        singInWithCredential(Credential);
    }

    private void singInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(VerifyPhoneNumber.this,MenuActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            Bundle bundle = new Bundle();
//                            bundle.putString(VerifyPhoneNumber.PHONENUMBER,phoneNumber);
//                            intent.putExtra("Bundle", bundle);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(VerifyPhoneNumber.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void findViews() {
        form_EDT_code = findViewById(R.id.form_EDT_code);
        form_BTN_submit = findViewById(R.id.form_BTN_submit);
        mAuth = FirebaseAuth.getInstance();
    }

    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(VerifyPhoneNumber.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(VerifyPhoneNumber.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            sendVerfiyPhoneNumber(fullNumber);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    sendVerfiyPhoneNumber(fullNumber);
                } else {
                    Toast.makeText(VerifyPhoneNumber.this,"Try to give permission from the settings",Toast.LENGTH_SHORT);
                }
                return;
            }
        }
    }

    private void sendVerfiyPhoneNumber(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verifcationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!= null){
                form_EDT_code.getEditText().setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneNumber.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

}