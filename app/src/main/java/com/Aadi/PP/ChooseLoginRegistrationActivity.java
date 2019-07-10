package com.Aadi.PP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChooseLoginRegistrationActivity extends AppCompatActivity {

    private Button mLogin, mRegister;
    private FirebaseAuth mAuth;
    private int SavedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registration);

        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);

        SharedPreferences prefs = getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
        Boolean restoredText = prefs.getBoolean("isLoginKey", false);



                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(ChooseLoginRegistrationActivity.this, PhonetwoActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Intent i = new Intent(ChooseLoginRegistrationActivity.this, PhonetwoActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }








    }
}
