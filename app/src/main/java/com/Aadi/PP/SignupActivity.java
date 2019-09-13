package com.Aadi.PP;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Aadi.PP.Chat.ChatActivity;
import com.google.firebase.database.DatabaseReference;

public class SignupActivity extends AppCompatActivity {
    Fragment fragment ;
    private DatabaseReference mUserDatabase;
    private String currentUId;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.toolBar);
        setActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_ios_black_18);
        getActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentByTag("myFragmentTag");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new SignupFragment1();
            ft.add(android.R.id.content, fragment, "myFragmentTag");
            ft.commit();
        }




    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}





