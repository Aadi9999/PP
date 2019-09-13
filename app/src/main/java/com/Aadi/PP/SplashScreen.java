package com.Aadi.PP;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Aadi.PP.Intro.SliderActivity;
import com.google.firebase.auth.FirebaseAuth;


public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            Intent i = new Intent(SplashScreen.this, SliderActivity.class);
            startActivity(i);
            finish();



    }
}
