package com.Aadi.PP;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Aadi.PP.ChangeActivities.ChangeNameActivity;
import com.Aadi.PP.Pager.PagerActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView mNameField, mPhoneField, mAboutField, mSportsField;

    private Button mBack, mConfirm, mButon, logout;

    private ImageView mProfileImage;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private DatabaseReference usersDb;

    private String userId, name, phone, about, sports, profileImageUrl, userSex;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Allow user to edit name and phone

        final LinearLayout mButon = findViewById(R.id.buton);

        mButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ChangeNameActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });



        final LinearLayout mButon3 = findViewById(R.id.buton3);

        mButon3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, mActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        final LinearLayout mButon4 = findViewById(R.id.buton4);

        mButon4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, PagerActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });




        mNameField = findViewById(R.id.name);
        mAboutField = findViewById(R.id.about);
        mSportsField = findViewById(R.id.sports);

        mProfileImage = findViewById(R.id.profileImage);



        mConfirm = findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        checkFirstOpen();
        getUserInfo();


        mProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        });


    }




    private void checkFirstOpen(){
        Boolean isFirstRun2 = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun2", true);

        if (isFirstRun2){


            new MaterialTapTargetPrompt.Builder(ProfileActivity.this)
                    .setTarget(R.id.confirm)
                    .setPrimaryText("Click to change profile picture")
                    .show();

        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun2",
                false).apply();
    }




    public void updateUserStatus(String state)
    {
        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        Map currentStateMap = new HashMap();
        currentStateMap.put("time",saveCurrentTime);
        currentStateMap.put("date",saveCurrentDate);
        currentStateMap.put("type",state);


        usersDb.child(userId).child("userState")
                .updateChildren(currentStateMap);
    }



    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();
                        mNameField.setText(name);
                    }

                    if(map.get("skill")!=null){
                        about = map.get("skill").toString();
                        mAboutField.setText(about);
                    }
                    if(map.get("sports")!=null){
                        sports = map.get("sports").toString();
                        mSportsField.setText(sports);
                    }
                    if(map.get("sex")!=null){
                        userSex = map.get("sex").toString();
                    }

                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.profilepic).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        updateUserStatus("offline");
    }


    private void saveUserInformation() {

        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(e -> finish());
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Map userInfo = new HashMap();
                userInfo.put("profileImageUrl", downloadUrl.toString());
                mUserDatabase.updateChildren(userInfo);

                return;
            });
        }else{
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){

            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
            saveUserInformation();
            Toasty.success(ProfileActivity.this, "New profile picture saved!", Toast.LENGTH_SHORT).show();

        }
    }
}