package com.Aadi.PP;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.Aadi.PP.Chat.ChatActivity;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import es.dmoral.toasty.Toasty;
import im.delight.android.location.SimpleLocation;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;



public class MapFragment extends Fragment {



    private SharedPreferences sharePrefObje;
    private GoogleMap googleMap;
    private String longitude2, latitude2, latitude2User, longitude2User, uid, name, currentUId, text, profileImageUrl;
    private Double lat2, lat2User;
    private Double long2, long2User;
    private DatabaseReference usersDb;
    private Dialog myDialog;
    private Marker marker, marker2;
    private FrameLayout frame_layout;
    private LinearLayout rootll;
    private ActionBarDrawerToggle t;
    private SimpleLocation location;
    private Button btnFollow;
    private TextView mName;
    private FusedLocationProviderClient mFusedLocationClient;
    private NavigationView nv;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    Location userLocation;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 858;
    private static final int REQUEST_PLACE_PICKER = 1001;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getActivity().getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        myDialog = new Dialog(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        frame_layout = rootView.findViewById(R.id.frame_layout);
        rootll = rootView.findViewById(R.id.rootll);



        rootView.post(new Runnable() {
            @Override
            public void run() {

                startRevealAnimation();

            }
        });


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                googleMap.setBuildingsEnabled(true);
                googleMap.setIndoorEnabled(true);
                googleMap.setMaxZoomPreference(20);


                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getActivity(), R.raw.sportconnectmap));

                    if (!success) {
                        Log.e("Map", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("Map", "Can't find style.", e);
                }

                DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);
                userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if(map.get("location")!=null){
                                location = new SimpleLocation(getActivity());

                                longitude2User = dataSnapshot.child("location").child("longitude").getValue().toString();
                                latitude2User = dataSnapshot.child("location").child("latitude").getValue().toString();
                                long2User = Double.valueOf(longitude2User);
                                lat2User = Double.valueOf(latitude2User);

                                LatLng latlngUser = new LatLng(lat2User, long2User);


                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target((latlngUser))
                                        .zoom(12)
                                        .build();


                                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




                usersDb.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.child("location").getValue() != null) {
                            location = new SimpleLocation(getActivity());

                            longitude2 = dataSnapshot.child("location").child("longitude").getValue().toString();
                            latitude2 = dataSnapshot.child("location").child("latitude").getValue().toString();
                            long2 = Double.valueOf(longitude2);
                            lat2 = Double.valueOf(latitude2);

                            LatLng latlng = new LatLng(lat2, long2);


                           if (currentUId != dataSnapshot.getKey()) {

                            if (dataSnapshot.child("sports").getValue() != null) {
                                name = dataSnapshot.child("name").getValue().toString();
                                uid = dataSnapshot.getKey();


                                        if (dataSnapshot.child("sports").getValue().toString() == "Badminton") {

                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.badminton);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Basketball")) {

                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.basketball);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Football")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.soccer);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Table Tennis")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.pingpong);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Exercising")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gym);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Baseball")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.baseball);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Tennis")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.tennis);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Archery")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.archery);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Snooker")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.eightball);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Cycling")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cycling);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Golf")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.golf);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Rugby")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.rugby);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Running")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.shoe);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));
                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("American Football")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.americanfootball);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Volleyball")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.volleyball);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));


                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("Cricket")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cricket);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }

                                        if (dataSnapshot.child("sports").getValue().toString().equals("default")) {
                                            int height = 100;
                                            int width = 100;
                                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.footballer);
                                            Bitmap b = bitmapdraw.getBitmap();
                                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                            marker = googleMap.addMarker(new MarkerOptions().position(latlng)
                                                    .title(name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                    .snippet(uid));

                                        }


                                    }

                                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                    @Override
                                    public View getInfoWindow(Marker arg0) {
                                        return null;
                                    }
                                    @Override
                                    public View getInfoContents(Marker marker) {
                                        View myContentView = getLayoutInflater().inflate(
                                                R.layout.customer_marker, null);
                                        TextView tvTitle = ((TextView) myContentView
                                                .findViewById(R.id.title));
                                        TextView tvSnippet = ((TextView) myContentView
                                                .findViewById(R.id.snippet));
                                        tvSnippet.setText(marker.getTitle());
                                        return myContentView;
                                    }
                                });




                                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        marker.hideInfoWindow();

                                        myDialog.setContentView(R.layout.activity_popup);
                                        //mName = myDialog.findViewById(R.id.name);
                                        TextView Headover = myDialog.findViewById(R.id.headover);
                                        //mName.setText(marker.getTitle());
                                        btnFollow = myDialog.findViewById(R.id.btnfollow);


                                        String ButtonText = btnFollow.getText().toString();

                                        String userId = marker.getSnippet();
                                        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
                                        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {

                                                    btnFollow.setText("Connected");
                                                    Headover.setText("Head over to Connects page to chat now!");
                                                    btnFollow.setClickable(false);

                                                }
                                                else {

                                                    btnFollow.setText("Connect");
                                                    btnFollow.setText("Connect");
                                                    Headover.setText("Click button to Connect");
                                                    btnFollow.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Headover.setText("Head over to Connects page to chat now!");
                                                            btnFollow.setText("Connected!!!");
                                                            usersDb.child(currentUId).child("connections").child("yeps").child(marker.getSnippet()).setValue(true);
                                                            isConnectionMatch(marker.getSnippet());
                                                            Toasty.success(getActivity(), "Connect!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });


                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.show();
                                    }
                                });


                            }


                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


        });
                    return rootView;
            }




    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }





    private void startOverlayAnimation(final GroundOverlay groundOverlay) {

        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator vAnimator = ValueAnimator.ofInt(0, 100);
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);
        vAnimator.setInterpolator(new LinearInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final Integer val = (Integer) valueAnimator.getAnimatedValue();
                groundOverlay.setDimensions(val);

            }
        });

        ValueAnimator tAnimator = ValueAnimator.ofFloat(0, 1);
        tAnimator.setRepeatCount(ValueAnimator.INFINITE);
        tAnimator.setRepeatMode(ValueAnimator.RESTART);
        tAnimator.setInterpolator(new LinearInterpolator());
        tAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float val = (Float) valueAnimator.getAnimatedValue();
                groundOverlay.setTransparency(val);
            }
        });

        animatorSet.setDuration(3000);
        animatorSet.playTogether(vAnimator, tAnimator);
        animatorSet.start();
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



    void startRevealAnimation() {

        int cx = frame_layout.getMeasuredWidth() / 2;
        int cy = frame_layout.getMeasuredHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(rootll, cx, cy, 50, frame_layout.getWidth());

        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator(2));
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });

        anim.start();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (location != null) {
            location.beginUpdates();
        }

        // ...
    }

    @Override
    public void onPause() {
        // stop location updates (saves battery)
        if (location != null) {
        location.endUpdates();
        }
        // ...

        super.onPause();
    }

    private boolean checkPermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Ask for the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            Toast.makeText(getActivity(), "Please give location permission", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



    private void isConnectionMatch(String userId) {
                DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
                currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            btnFollow.setText("Connected");

                            String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                            usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                            usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                        }
                        else {

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }


            public void updateUserStatus(String state) {
                String saveCurrentDate, saveCurrentTime;

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                saveCurrentTime = currentTime.format(calForTime.getTime());

                Map currentStateMap = new HashMap();
                currentStateMap.put("time", saveCurrentTime);
                currentStateMap.put("date", saveCurrentDate);
                currentStateMap.put("type", state);


                usersDb.child(currentUId).child("userState")
                        .updateChildren(currentStateMap);
            }
        }

