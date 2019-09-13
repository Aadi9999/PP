package com.Aadi.PP;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hbb20.CountryCodePicker;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class PhoneoneActivity extends AppCompatActivity {


    private EditText editTextMobile;
    private FirebaseAuth mAuth;
    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneone);

        ccp = findViewById(R.id.ccp);
        editTextMobile = findViewById(R.id.editTextMobile);



        SharedPreferences prefs = getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
        Intent intent = null;
        if (prefs.getBoolean("isLoginKey", false)){ //user logged in before
            intent = new Intent(PhoneoneActivity.this, mActivity.class);
            startActivity(intent);
            finish();
        } else {

        }

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();
                ccp.registerCarrierNumberEditText(editTextMobile);
                String phoneNumber = ccp.getFormattedFullNumber();


                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }else {

                Intent intent = new Intent(PhoneoneActivity.this, PhonetwoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("mobile", phoneNumber);
                startActivity(intent);

                }
            }
        });
    }

}