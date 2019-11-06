package com.Aadi.PP;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
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
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

import com.Aadi.PP.Matches.MatchesFragment;
import com.Aadi.PP.Matches.MatchesObject;
import com.Aadi.PP.Pager.CustomAnimationsUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.maps.DirectionsApi;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import com.google.maps.GeoApiContext;
import im.delight.android.location.SimpleLocation;




public class MapFragment extends Fragment {



    private SharedPreferences sharePrefObje;
    private GoogleMap googleMap;
    private String latitude2User, longitude2User, uid, name, currentUId, CurrentUID, latitude_Display, longitude_Display, sport, userID, getUserID;
    private Double lat2, lat2User;
    private Double long2, long2User;
    private DatabaseReference usersDb;
    private TextView changeName, changeSport;
    private Dialog myDialog;
    private Marker marker, marker2;
    private FrameLayout frame_layout;
    private LinearLayout rootll;
    private ActionBarDrawerToggle t;
    private SimpleLocation location;
    private Button btnFollow;

    private TextView mName;
    private DatabaseReference userDb;
    private FusedLocationProviderClient mFusedLocationClient;
    private NavigationView nv;
    private LinearLayout mInfo;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private LatLng currentLatLng;
    private LatLng latlng;
    private Bitmap smallMarker;
    private String originString;

    Location userLocation;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 858;
    private static final int REQUEST_PLACE_PICKER = 1001;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private List<MatchesObject> matchesList;

    private static final int overview = 0;

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

    private DirectionsResult getDirectionsDetails(String origin,String destination,TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        frame_layout = rootView.findViewById(R.id.frame_layout);

        changeName = rootView.findViewById(R.id.nameInfoMap);
        changeSport = rootView.findViewById(R.id.sportInfoMap);

        rootll = rootView.findViewById(R.id.rootll);
        mInfo = rootView.findViewById(R.id.infoMap);

        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(getActivity());

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);

        rootView.post(new Runnable() {
            @Override
            public void run() {

                startRevealAnimation();

            }
        });


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (googleMap == null) {
                    return;
                }
                try {



                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                    googleMap.setBuildingsEnabled(true);
                    googleMap.setMaxZoomPreference(20);
                    getUserInfo();

                    Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {

                                Location location = task.getResult();
                                currentLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16F));

                            }
                        }
                    });



                } catch (SecurityException e) {
                    Log.e("Exception: %s", e.getMessage());
                }


                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usersRef = rootRef
                        .child("Users");



                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                location = new SimpleLocation(getActivity());


                                latitude_Display = ds
                                        .child("location")
                                        .child("latitude")
                                        .getValue().toString();

                                longitude_Display = ds
                                        .child("location")
                                        .child("longitude")
                                        .getValue().toString();

                                name = ds
                                        .child("name")
                                        .getValue().toString();




                                userID = ds
                                        .child("userID")
                                        .getValue().toString();

                                sport = ds
                                        .child("sports")
                                        .getValue().toString();




                                originString = latitude_Display + "," + longitude_Display;




                                CurrentUID = currentUId;
                                if (!userID.equals(CurrentUID)) {
                                    if (sport.equals("Badminton")) {

                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.badminton);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));


                                    }

                                    if (sport.equals("Basketball")) {

                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.basketball);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b, width, height, false);

                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker1))
                                                .title(name));

                                    }

                                    if (sport.equals("Football")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.soccer);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("Table Tennis")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.pingpong);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("Exercising")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.gym);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("Baseball")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.baseball);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));

                                    }

                                    if (sport.equals("Tennis")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.tennis);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        Bitmap smallMarker1 = Bitmap.createScaledBitmap(b, width, height, false);

                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker1))
                                                .title(name));
                                    }

                                    if (sport.equals("Archery")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.archery);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));

                                    }

                                    if (sport.equals("Snooker")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.eightball);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("Cycling")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cycling);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("Golf")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.golf);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("Rugby")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.rugby);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));

                                    }

                                    if (sport.equals("Running")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.shoe);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("American Football")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.americanfootball);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("Volleyball")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.volleyball);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));

                                    }

                                    if (sport.equals("Cricket")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cricket);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }

                                    if (sport.equals("default")) {
                                        int height = 100;
                                        int width = 100;
                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.footballer);
                                        Bitmap b = bitmapdraw.getBitmap();
                                        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                                        String latLng = latitude_Display;
                                        String latLng1 = longitude_Display;


                                        double latitude = Double.parseDouble(latLng);
                                        double longitude = Double.parseDouble(latLng1);


                                        // map.clear();
                                        LatLng currentLocation = new LatLng(latitude, longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(currentLocation);

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latitude, longitude))
                                                .snippet(userID)
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                                .title(name));
                                    }


                                }



                            }
                        }

                    

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                usersRef.addListenerForSingleValueEvent(eventListener);



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


                usersDb.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                                    usersDb.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            if (dataSnapshot.child("location").getValue() != null) {
                                                if (userDb != null) {


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

                                                                getUserID = marker.getSnippet();


                                                                marker.hideInfoWindow();

                                                                myDialog.setContentView(R.layout.activity_popup);
                                                                //mName = myDialog.findViewById(R.id.name);
                                                                TextView Headover = myDialog.findViewById(R.id.headover);
                                                                //mName.setText(marker.getTitle());
                                                                btnFollow = myDialog.findViewById(R.id.btnfollow);



                                                                DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(getUserID);
                                                                currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                            if (dataSnapshot.exists()) {

                                                                                btnFollow.setText("Connected");
                                                                                Headover.setText("Head over to Connects page to chat now!");
                                                                                btnFollow.setClickable(false);

                                                                            } else {

                                                                                btnFollow.setText("Connect");
                                                                                btnFollow.setText("Connect");
                                                                                Headover.setText("Click button to Connect");
                                                                                btnFollow.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {

                                                                                        Headover.setText("Head over to Connects page to chat now.");
                                                                                        btnFollow.setText("Connected");
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


            



    private void animateIn() {
        // start translationY - 2000
        // final translationY - 0
        // Duration - 1 sec
        mInfo.setVisibility(View.VISIBLE);
        CustomAnimationsUtils.animateY(mInfo, 400, 0, 1000);
    }


    private void getUserInfo() {
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        String name2 = map.get("name").toString();
                        changeName.setText("Greetings, " + name2);

                    }
                    if(map.get("sports")!=null){
                        String sport2 = map.get("sports").toString();
                        changeSport.setText("Look around and find others who love " + sport2 + " around you!");
                    }

                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

                animateIn();

            }
        });

        anim.start();
    }


    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey(getString(R.string.key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }



    private void isConnectionMatch(String userId) {
                DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
                currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

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


        }

