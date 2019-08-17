package com.Aadi.PP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.harjot.crollerTest.Croller;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class SignupFragment4 extends Fragment {

    private TextView mExperienceLevel;
    private SharedPreferences sharePrefObje;
    private int mProgress;
    private int mProgress2, restoredText;
    private TextView mValue;
    private String mValue2, mValue3;
    private DatabaseReference usersDb;
    private String currentUId2, phoneNum, sports;
    private DatabaseReference mUserDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_skill, container, false);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUId2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId2);

        ProgressBar progressBar = rootView.findViewById(R.id.my_progressBar);
        progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.color1), PorterDuff.Mode.SRC_IN );

        SharedPreferences prefs = getContext().getSharedPreferences("Radius", MODE_PRIVATE);
        restoredText = prefs.getInt("myInt", 3);

        SharedPreferences prefs2 = getContext().getSharedPreferences("ph_num", MODE_PRIVATE);
        phoneNum = prefs2.getString("myNum", null);

        getUserInfo();

        mValue = rootView.findViewById(R.id.value);
        Button mSaveRadius = rootView.findViewById(R.id.confirm_radius);

        mSaveRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadiusSelected();
                onBackPressed();
            }
        });

        Croller croller = rootView.findViewById(R.id.croller);


        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
        croller.setMainCircleColor(Color.WHITE);
        croller.setProgress(restoredText);
        croller.setMax(3);
        croller.setStartOffset(45);
        croller.setIsContinuous(true);
        croller.setLabelColor(Color.BLACK);
        croller.setLabel("I am a:");
        croller.setProgressPrimaryColor(Color.parseColor("#6200EE"));
        croller.setIndicatorColor(Color.parseColor("#6200EE"));
        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));

        croller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {

            public void onProgressChanged(int progress) {
                mProgress = progress;


                if (mProgress == 1){
                    mValue.setText("BEGINNER");
                    mValue2 = "Beginner";
                    mValue3 = "1";
                }

                if (mProgress == 2){
                    mValue.setText("INTERMEDIATE");
                    mValue2 = "Intermediate";
                    mValue3 = "2";
                }

                if (mProgress == 3){
                    mValue.setText("PROFESSIONAL");
                    mValue2 = "Professional";
                    mValue3 = "3";
                }


            }


        });


        return rootView;
    }



    private void getUserInfo() {


        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("sports") != null) {
                        sports = map.get("sports").toString();

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void RadiusSelected() {

        Map currentSkillMap = new HashMap();
        currentSkillMap.put("skill", mValue2);
        usersDb.child(currentUId2)
                .updateChildren(currentSkillMap);

        mProgress2 = Integer.parseInt(mValue3);
        SharedPreferences.Editor editor = getContext().getSharedPreferences("Radius", MODE_PRIVATE).edit();
        editor.putInt("myInt", mProgress2);
        editor.apply();

        Map currentPhoneMap = new HashMap();
        currentPhoneMap.put("phone", phoneNum);
        usersDb.child(currentUId2)
                .updateChildren(currentPhoneMap);

    }



    public void onBackPressed() {
        sharePrefObje = getActivity().getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePrefObje.edit();
        editor.putBoolean("isLoginKey",true);
        editor.commit();

        Intent intent = new Intent(getActivity(), mActivity.class);
        startActivity(intent);
        getActivity().finish();
        return;
    }



}
