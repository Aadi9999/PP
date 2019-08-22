package com.Aadi.PP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Aadi.PP.Pager.PagerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


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

        ProgressBar progressBar = rootView.findViewById(R.id.my_progressBar);
        progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.color1), PorterDuff.Mode.SRC_IN );

        return rootView;
    }


    public  void Savesettings() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                final String name = mName.getText().toString();


                if (name.isEmpty() || name.length() < 3) {
                    mName.setError("Username must be at least 3 characters");
                } else {
                    mName.setError(null);
                }


                if (!name.isEmpty()) {
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
