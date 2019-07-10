package com.Aadi.PP;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Aadi.PP.ChangeActivities.ChangeNameActivity;
import com.Aadi.PP.Matches.MatchesFragment;
import com.Aadi.PP.Pager.PagerActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Allow user to edit name and phone

        final Button mButon = findViewById(R.id.buton);

        mButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ChangeNameActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });



        final Button mButon3 = findViewById(R.id.buton3);

        mButon3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, SkillActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        final Button mButon4 = findViewById(R.id.buton4);

        mButon4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, PagerActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });

        final Button mButon5 = findViewById(R.id.buton5);

        mButon5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });





        dl = findViewById(R.id.activity_settings);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);




        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.matches:
                        Intent intent = new Intent(ProfileActivity.this, MatchesFragment.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.settings:
                        Intent intent2 = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.browse:
                        Intent intent3 = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent3);
                        finish();
                        return true;
                    case android.R.id.home:
                        dl.openDrawer(GravityCompat.START);
                        return true;

                    case R.id.log_out:
                        new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Logout From SportConnect")
                                .setMessage("Would you like to Log out?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent inte = new Intent(ProfileActivity.this, ChooseLoginRegistrationActivity.class);
                                        mAuth.signOut();
                                        startActivity(inte);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                }

                return true;


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

        getUserInfo();
        checkFirstOpen();



        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                saveUserInformation();
                Toasty.success(ProfileActivity.this, "New profile picture saved!", Toast.LENGTH_SHORT).show();
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
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



    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();
                        mNameField.setText(name);

                        View header = nv.getHeaderView(0);

                        ImageView img = header.findViewById(R.id.img);
                        TextView nam = header.findViewById(R.id.nam);

                        nam.setText(name);
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
                    View header = nv.getHeaderView(0);
                    ImageView img = header.findViewById(R.id.img);
                    Glide.clear(img);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.profilepic).into(img);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(img);
                                break;
                        }
                    }
                    Glide.clear(mProfileImage);
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
        name = mNameField.getText().toString();
        about = mAboutField.getText().toString();
        sports = mSportsField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("about", about);
        userInfo.put("sports", sports);
        mUserDatabase.updateChildren(userInfo);
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
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Map userInfo = new HashMap();

                    mUserDatabase.updateChildren(userInfo);

                    finish();
                    return;
                }
            });
        }else{
            finish();
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
        }
    }
}