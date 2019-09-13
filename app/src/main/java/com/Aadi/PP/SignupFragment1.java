package com.Aadi.PP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Aadi.PP.Chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SignupFragment1 extends Fragment {

    private EditText mEmail;
    private Button btnContinue;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String currentUId;
    private Fragment fragment;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.signup1, container, false);
        mEmail = rootView.findViewById(R.id.email);
        btnContinue = rootView.findViewById(R.id.btn_continue);
        mAuth = FirebaseAuth.getInstance();

        Savesettings();


        return rootView;
    }



    public  void Savesettings() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                final String email = mEmail.getText().toString();


                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Enter a valid email address");
                } else {
                    mEmail.setError(null);
                }


                if (!email.isEmpty()) {
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    Map userInfo = new HashMap<>();
                    userInfo.put("Email", email);
                    userInfo.put("profileImageUrl", "default");
                    userInfo.put("sex", "Male");
                    currentUserDb.updateChildren(userInfo);




                    fragment = new SignupFragment2();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }

            }

        });




    }


}