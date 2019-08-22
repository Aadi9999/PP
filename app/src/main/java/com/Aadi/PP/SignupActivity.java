package com.Aadi.PP;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;

public class SignupActivity extends AppCompatActivity {
    Fragment fragment ;
    private DatabaseReference mUserDatabase;
    private String currentUId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("myFragmentTag");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new SignupFragment1();
            ft.add(android.R.id.content, fragment, "myFragmentTag");
            ft.commit();
        }



    }
}





