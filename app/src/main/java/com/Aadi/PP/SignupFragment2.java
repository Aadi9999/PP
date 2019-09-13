package com.Aadi.PP;

import android.annotation.SuppressLint;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SignupFragment2 extends Fragment {

    private EditText mName;
    private Button btnContinue;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.signup2, container, false);
        mName = rootView.findViewById(R.id.name);
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

                final String name = mName.getText().toString();


                if (name.isEmpty() || name.length() < 4) {
                    mName.setError("Username must be at least 3 characters");
                } else {
                    mName.setError(null);
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    Map userInfo = new HashMap<>();
                    userInfo.put("name", name);
                    currentUserDb.updateChildren(userInfo);

                    Fragment fragment = new SignupFragment3();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(android.R.id.content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return;
                }


            }

        });
    }
}
