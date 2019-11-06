package com.Aadi.PP;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class BetweenActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private String currentUId;
    private FirebaseAuth mAuth;
    private SharedPreferences sharePrefObje;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_between);

        mAuth = FirebaseAuth.getInstance();
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if (map.get("phone") != null) {
                                String sports = map.get("phone").toString();
                                SharedPreferences prefs = getSharedPreferences("ph_num", MODE_PRIVATE);
                                String restoredText = prefs.getString("myNum", null);
                                if (sports.equals(restoredText)) {
                                    sharePrefObje = getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharePrefObje.edit();
                                    editor.putBoolean("isLoginKey",true);
                                    editor.commit();
                                    Intent intent = new Intent(BetweenActivity.this, mActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                                else {
                                    Intent intent2 = new Intent(BetweenActivity.this, SignupActivity.class);
                                    startActivity(intent2);
                                    finish();
                                    return;
                                }
                            }
                            else {
                                Intent intent2 = new Intent(BetweenActivity.this, SignupActivity.class);
                                startActivity(intent2);
                                finish();
                                return;
                            }

                        }
                        else {
                            Intent intent2 = new Intent(BetweenActivity.this, SignupActivity.class);
                            startActivity(intent2);
                            finish();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toasty.error(BetweenActivity.this, "Please turn on Location permission to use SportConnect", Toast.LENGTH_SHORT).show();
                Intent inte = new Intent(BetweenActivity.this, PhoneoneActivity.class);
                mAuth.signOut();
                startActivity(inte);

            }

        };


        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("We need location permission to use SportConnect\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();



    }
}