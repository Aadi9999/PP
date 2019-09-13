package com.Aadi.PP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.Aadi.PP.ChangeActivities.ChangeNameActivity;
import com.Aadi.PP.Matches.MatchesFragment;
import com.Aadi.PP.Pager.PagerActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onesignal.OneSignal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import bolts.Task;
import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;
import static com.firebase.ui.auth.AuthUI.TAG;

public class SettingsFragment extends Fragment {
    private Switch darkModeSwitch;
    private SharedPreferences sharePrefObje;
    private TextView EditProfile;
    private TextView Logout;
    private int PICK_IMAGE_REQUEST = 1;
    private CircularImageView mProfileImage;
    private DatabaseReference mUserDatabase;
    private Uri resultUri;
    private String userId, profileImageUrl;
    private FirebaseAuth mAuth;
    private Switch Notif;
    private TextView mName;
    private ImageView sportsicon;
    private TextView Number;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.settings, container, false);


        Number = rootView.findViewById(R.id.EditNumber);
        sportsicon = rootView.findViewById(R.id.EditSports);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mProfileImage = rootView.findViewById(R.id.profileCircleImageView);
        getUserInfo();

        final TextView mButon = rootView.findViewById(R.id.EditName);

        mButon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ChangeNameActivity.class);
                startActivity(intent);
                return;

            }
        });



        final TextView mButon4 = rootView.findViewById(R.id.Pager);

        mButon4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), PagerActivity.class);
                startActivity(intent);
                return;

            }
        });

        final TextView mDelete = rootView.findViewById(R.id.Delete);

        mDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });




        EditProfile = rootView.findViewById(R.id.editprofile);
        Logout = rootView.findViewById(R.id.Logout);
        mName = rootView.findViewById(R.id.name);

        Notif = rootView.findViewById(R.id.notif);
        setNotifSwitch();


        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("Notif", MODE_PRIVATE);
        Notif.setChecked(sharedPrefs.getBoolean("Notif", true));

                EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout From SportConnect")
                        .setMessage("Would you like to Log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                OneSignal.setSubscription(false);
                                sharePrefObje = getActivity().getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharePrefObje.edit();
                                editor.clear();
                                editor.apply();
                                //updateUserStatus("offline");
                                Intent inte = new Intent(getActivity(), PhoneoneActivity.class);
                                FirebaseAuth.getInstance().signOut();
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
        });



        return rootView;
    }


    private void setNotifSwitch() {


        Notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Notif.isChecked()) {
                    OneSignal.setSubscription(true);
                    OneSignal.startInit(getActivity()).init();
                    OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                        @Override
                        public void idsAvailable(String userId, String registrationId) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);
                        }
                    });
                    OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("Notif", MODE_PRIVATE).edit();
                    editor.putBoolean("Notif", true);
                    editor.commit();

                } else {
                    OneSignal.setSubscription(false);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("Notif", MODE_PRIVATE).edit();
                    editor.putBoolean("Notif", false);
                    editor.commit();
                }
            }
        });
    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if(map.get("name")!=null){
                        String name = map.get("name").toString();
                        mName.setText(name);
                    }

                    if(map.get("phone")!=null){
                        String number = map.get("phone").toString();
                        Number.setText(number);
                    }

                    if (dataSnapshot.child("sports").getValue()!=null) {
                        String sports = dataSnapshot.child("sports").getValue().toString();


                        if (sports.equals("Badminton")) {
                            sportsicon.setImageResource(R.drawable.badminton);
                        }

                        if (sports.equals("Basketball")) {
                            sportsicon.setImageResource(R.drawable.basketball);
                        }

                        if (sports.equals("Football")) {
                            sportsicon.setImageResource(R.drawable.soccer);
                        }

                        if (sports.equals("Table tennis")) {
                            sportsicon.setImageResource(R.drawable.pingpong);
                        }

                        if (sports.equals("Exercising")) {
                            sportsicon.setImageResource(R.drawable.gym);
                        }

                        if (sports.equals("Baseball")) {
                            sportsicon.setImageResource(R.drawable.baseball);
                        }

                        if (sports.equals("Tennis")) {
                            sportsicon.setImageResource(R.drawable.tennis);
                        }

                        if (sports.equals("Archery")) {
                            sportsicon.setImageResource(R.drawable.archery);
                        }

                        if (sports.equals("Snooker")) {
                            sportsicon.setImageResource(R.drawable.eightball);
                        }

                        if (sports.equals("Cycling")) {
                            sportsicon.setImageResource(R.drawable.cycling);
                        }

                        if (sports.equals("Golf")) {
                            sportsicon.setImageResource(R.drawable.golf);
                        }

                        if (sports.equals("Rugby")) {
                            sportsicon.setImageResource(R.drawable.rugby);
                        }

                        if (sports.equals("Running")) {
                            sportsicon.setImageResource(R.drawable.shoe);
                        }

                        if (sports.equals("American Football")) {
                            sportsicon.setImageResource(R.drawable.americanfootball);
                        }

                        if (sports.equals("Volleyball")) {
                            sportsicon.setImageResource(R.drawable.volleyball);
                        }

                        if (sports.equals("Cricket")) {
                            sportsicon.setImageResource(R.drawable.cricket);
                        }

                        if (sports.equals("default")) {
                            sportsicon.setImageResource(R.drawable.shoe);
                        }
                    }

                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getActivity().getApplication()).load(R.drawable.man).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getActivity().getApplication()).load(profileImageUrl).into(mProfileImage);
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



    private void saveUserInformation() {

        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = handleSamplingAndRotationBitmap(getContext(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(e -> getActivity().getSupportFragmentManager().popBackStack());
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

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }



    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){

            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
            saveUserInformation();
            Toasty.success(getActivity(), "New profile picture saved!", Toast.LENGTH_SHORT).show();

        }


    }}
