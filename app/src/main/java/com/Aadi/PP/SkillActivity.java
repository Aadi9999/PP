package com.Aadi.PP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.harjot.crollerTest.Croller;

import java.util.HashMap;
import java.util.Map;




public class SkillActivity extends AppCompatActivity {

    private TextView mExperienceLevel;
    private int mProgress;
    private int mProgress2, restoredText;
    private TextView mValue;
    private String mValue2, mValue3;
    private DatabaseReference usersDb;
    private String currentUId2, sports, skill, skillint;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUId2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId2);



        SharedPreferences prefs = getSharedPreferences("Radius", MODE_PRIVATE);
        restoredText = prefs.getInt("myInt", 3);

        getUserInfo();

        mValue = findViewById(R.id.value);
        Button mSaveRadius = findViewById(R.id.confirm_radius);

        mSaveRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadiusSelected();
                onBackPressed();
            }
        });



        Croller croller = findViewById(R.id.croller);


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
            @Override
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
        SharedPreferences.Editor editor = getSharedPreferences("Radius", MODE_PRIVATE).edit();
        editor.putInt("myInt", mProgress2);
        editor.apply();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SkillActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
