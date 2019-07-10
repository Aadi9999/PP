package com.Aadi.PP.ChangeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Aadi.PP.ProfileActivity;
import com.Aadi.PP.R;
import com.Aadi.PP.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangeNameActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText mNameField;
    private String name, userId;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private Button mConfirm, mButon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changename);

        EditText editText = findViewById(R.id.name);
        editText.setFocusableInTouchMode(true);


        mNameField = findViewById(R.id.name);
        mConfirm = findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();


        mConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        mNameField.setText(name);

                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void saveUserInformation() {
        name = mNameField.getText().toString();

        if ( name.length() > 3)
        {
            mNameField.setError(null);
            Map userInfo = new HashMap();
            userInfo.put("name", name);
            mUserDatabase.updateChildren(userInfo);
            Intent intent = new Intent(ChangeNameActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
            return;

        } else {
            mNameField.setError("Username must be at least 3 letters");
        }
    }}

