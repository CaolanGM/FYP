package com.example.gaffneca.fypfirebase;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaffneca.fypfirebase.models.PlaceInfo;
import com.example.gaffneca.fypfirebase.route.DownloadUrl;
import com.example.gaffneca.fypfirebase.route.GetDirectionsData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;



public class MapActivity2 extends AppCompatActivity implements OnMapReadyCallback,
GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, NavigationView.OnNavigationItemSelectedListener{

    @Override
    public void onMapReady(GoogleMap googleMap) {
      //  Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        drawGrid();
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMarkerClickListener(this);
            init();
        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168), new LatLng(71,136));


    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    private Button confirmBut;
    private RelativeLayout searchArea;
    private RelativeLayout journeyDetails;
    private TextView durationText;
    private TextView cost;
    private TextView pickUpTV;
    private TextView dropOffTV;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private double longitude;
    private double latitude;
    private double startLongitude;
    private double startLatitude;
    private double endLongitude;
    private double endLatitude;
    private boolean pickUpChosen;
    private boolean dropOff;
    private DatabaseReference mDatabase;
    private String pickUpPoint;
    private String dropOffPoint;
    private String pricing;
    private String duration;
    private String dateTime;
    private int dayFinal;
    private int monthFinal;
    private int yearFinal;
    private int hourFinal;
    private int minuteFinal;
    private FirebaseAuth mAuth;


    private ImageView homeBut;
    private ImageView logoutBut;
    private TextView homeTxt;
    private TextView logoutTxt;
    private boolean logOpen;

    private Button greenRev;
    private RelativeLayout popUp;
    private RelativeLayout tint ;
    private ImageView mImgView;
    private TextView revUser;
    private TextView cancel;


    private ImageView open;
    private ImageView close;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        mAuth=FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        clearOldJourneys(user_id);

        confirmBut = (Button) findViewById(R.id.confirmBut);
        searchArea = (RelativeLayout) findViewById(R.id.relLayout);
        mGps = (ImageView) findViewById(R.id.ic_gps);

//        Intent intent = getIntent();
//        String setUp = intent.getStringExtra("setup");
//        if(setUp!=null)
//        {
//            Object dataTransfer = new Object[3];
//            String url = getDirectionsUrl();
//            GetDirectionsData getDirectionsData = new GetDirectionsData();
//
//            startLatitude =53.31762186558897;
//            startLongitude=6.285163722932339;
//            endLatitude=53.294411849999996;
//            endLongitude= 6.1338951999999995;
//            String[] info = getDirectionsData.getInfo(url);
//        }


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        if(intent.getStringExtra("Plain Map")!=null)
        {
         searchArea.setVisibility(View.GONE);
         confirmBut.setVisibility(View.GONE);
            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)mGps.getLayoutParams();
            relativeParams.setMargins(0, 170, 30, 0);  // left, top, right, bottom
            mGps.setLayoutParams(relativeParams);

        }
        else {
            dateTime = intent.getStringExtra("date/time");
            dayFinal = intent.getIntExtra("dayFinal", 7);
            monthFinal = intent.getIntExtra("monthFinal", 7);
            yearFinal = intent.getIntExtra("yearFinal", 7);
            hourFinal = intent.getIntExtra("hourFinal", 7);
            minuteFinal = intent.getIntExtra("minuteFinal", 7);
        }



        mSearchText = (AutoCompleteTextView) findViewById(R.id.search);
        mGps = (ImageView) findViewById(R.id.ic_gps);

        journeyDetails = (RelativeLayout) findViewById(R.id.relLayout2);
        durationText = (TextView) findViewById(R.id.textView8);
        cost = (TextView) findViewById(R.id.textView10);
        pickUpTV = (TextView) findViewById(R.id.textView5);
        dropOffTV = (TextView) findViewById(R.id.textView6);
        greenRev = (Button) findViewById(R.id.greenRev);
        popUp = (RelativeLayout) findViewById(R.id.popUp);
        tint = (RelativeLayout) findViewById(R.id.backTint);
        mImgView = (ImageView) findViewById(R.id.img);
        revUser = (TextView) findViewById(R.id.revName);
        cancel = (TextView) findViewById(R.id.cancel);



        journeyDetails.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Co-Ordinates");
        pickUpChosen = false;
        dropOff=false;
        startLatitude=0;
        startLongitude=0;
        pickUpPoint = "";
        dropOffPoint = "";
        duration = "";
        pricing = "";
        getLocationPermission();



        popUp.setVisibility(View.GONE);
        tint.setVisibility(View.GONE);



        open = findViewById(R.id.ic_menu);
        close = findViewById(R.id.ic_back);
        mDrawerLayout = findViewById(R.id.drawerWin);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        sideBar();

        open.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.START);
                mDrawerLayout.setElevation(50);
                open.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
            }

        });
        close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawers();
                mDrawerLayout.setElevation(0);
                close.setVisibility(View.GONE);
                open.setVisibility(View.VISIBLE);


            }
        });



        final DatabaseReference reviewsTab = FirebaseDatabase.getInstance().getReference().child("New Users").child(user_id).child("Pending Reviews");
        reviewsTab.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Log.e("tes", "ENTERED");
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        Log.e("ON THE ", "DOUBLE");
                        final String id = child.getKey();
                        final String name = child.getValue().toString();
                        reviewsTab.child(id).removeValue();
                        revUser.setText(name);
                        popUp.setVisibility(View.VISIBLE);
                        tint.setVisibility(View.VISIBLE);
                        offClick();

                        DatabaseReference img = FirebaseDatabase.getInstance().getReference().child("New Users").child(id).child("image");
                        img.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String photo = dataSnapshot.getValue().toString();
                                Picasso.with(MapActivity2.this).load(photo).fit().centerCrop().into(mImgView);
                                NavigationView naview = findViewById(R.id.navView);
                                View header = naview.getHeaderView(0);
                                final ImageView sideimg = (ImageView) header.findViewById(R.id.hamimg);
                                Picasso.with(MapActivity2.this).load(photo).fit().centerCrop().into(sideimg);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        greenRev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent revIntent = new Intent(MapActivity2.this, MakeReview.class);
                                revIntent.putExtra("name", name );
                                revIntent.putExtra("id", id );
                                MapActivity2.this.startActivity(revIntent);
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.setVisibility(View.GONE);
                tint.setVisibility(View.GONE);
                clickOn();
            }
        });




        logoutBut = findViewById(R.id.logout);
        logoutTxt = findViewById(R.id.logoutTxt);
        logOpen = false;




        logoutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logOpen){
                    logoutTxt.setVisibility(View.GONE);
                    logOpen=false;
                }
                else{
                    logoutTxt.setVisibility(View.VISIBLE);
                    logOpen=true;
                }
            }
        });
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent LogoutIntent = new Intent(MapActivity2.this, MainActivity.class);
                MapActivity2.this.startActivity(LogoutIntent);
            }
        });





    }

    private void init(){
        Log.d(TAG, "init: initializing");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
                hideKeyboard();
            }
        });

        hideKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity2.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0)+", "+address.getAddressLine(1));
            latitude = address.getLatitude();
            longitude =address.getLongitude();
            String id = makeID();
            mDatabase = mDatabase.child(id);
            mDatabase.child("Latitude").setValue(latitude);
            mDatabase.child("Longitude").setValue(longitude);

            confirmBut.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(dropOff)
                    {
                       infoIntent();
                    }
                    else if(pickUpChosen)
                    {
                        endLatitude=latitude;
                        endLongitude=longitude;
                        dropOff=true;
                        moveCamera(new LatLng(endLatitude, endLongitude),DEFAULT_ZOOM,"");
                        float results[] = new float[10];
                        Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                        mMap.clear();
                        drawGrid();

                        LatLng pickUp = new LatLng(startLatitude,startLongitude);
                        MarkerOptions marker = new MarkerOptions();
                        marker.position(pickUp);
                        marker.title("Pick Up Point");
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        mMap.addMarker(marker);

                        LatLng dropOff = new LatLng(endLatitude,endLongitude);
                        MarkerOptions marker2 = new MarkerOptions();
                        marker2.position(dropOff);
                        marker2.title("Drop Off Point");
                        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        mMap.addMarker(marker2);
                       // Toast.makeText(MapActivity2.this, "Drop Off Point Selected", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        pickUpChosen=true;
                        startLatitude= latitude;
                        startLongitude = longitude;
                        String id = makeID();
                        mDatabase = mDatabase.child(id);
                        mDatabase.child("Latitude").setValue(latitude);
                        mDatabase.child("Longitude").setValue(longitude);
                        confirmBut.setText("Confirm Destination");
                        mSearchText.setText("");
                        mSearchText.setHint("Enter Destination");
                        mMap.clear();
                        drawGrid();

                        LatLng pickUp = new LatLng(startLatitude,startLongitude);
                        MarkerOptions marker = new MarkerOptions();
                        marker.position(pickUp);
                        marker.title("Pick Up Point");
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        mMap.addMarker(marker);
                        Toast.makeText(MapActivity2.this, "Pick Up Point Selected", Toast.LENGTH_SHORT).show();

                    }
                }

            });


        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            //LatLng mark = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            //MarkerOptions markerOptions = new MarkerOptions();
                            //markerOptions.position(mark);
                            //markerOptions.title("Current Location");
                            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            if(currentLocation!=null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                                latitude = currentLocation.getLatitude();
                                longitude = currentLocation.getLongitude();
                            }
                            confirmBut.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    if(dropOff)
                                    {
                                        infoIntent();
                                    }
                                    else if(pickUpChosen)
                                    {
                                        endLatitude=latitude;
                                        endLongitude=longitude;
                                        dropOff=true;
                                        moveCamera(new LatLng(endLatitude, endLongitude),DEFAULT_ZOOM,"");
                                        float results[] = new float[10];
                                        Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                                        mMap.clear();
                                        drawGrid();

                                        LatLng pickUp = new LatLng(startLatitude,startLongitude);
                                        MarkerOptions marker = new MarkerOptions();
                                        marker.position(pickUp);
                                        marker.title("Pick Up Point");
                                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                        mMap.addMarker(marker);

                                        LatLng dropOff = new LatLng(endLatitude,endLongitude);
                                        MarkerOptions marker2 = new MarkerOptions();
                                        marker2.position(dropOff);
                                        marker2.title("Drop Off Point");
                                        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                        mMap.addMarker(marker2);
                                      //  Toast.makeText(MapActivity2.this, "Drop Off Point Selected", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        pickUpChosen=true;
                                        startLatitude= latitude;
                                        startLongitude = longitude;
                                        String id = makeID();
                                        mDatabase = mDatabase.child(id);
                                        mDatabase.child("Latitude").setValue(latitude);
                                        mDatabase.child("Longitude").setValue(longitude);
                                        confirmBut.setText("Confirm Destination");
                                        mSearchText.setText("");
                                        mSearchText.setHint("Enter Destination");
                                        mMap.clear();
                                        drawGrid();

                                        LatLng pickUp = new LatLng(startLatitude,startLongitude);
                                        MarkerOptions marker = new MarkerOptions();
                                        marker.position(pickUp);
                                        marker.title("Pick Up Point");
                                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                        mMap.addMarker(marker);
                                        Toast.makeText(MapActivity2.this, "Pick Up Point Selected", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            });


                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity2.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
        catch (NumberFormatException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }


    }

    private void moveCamera(LatLng latLng, float zoom,String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        hideKeyboard();
        if(dropOff){
            LatLngBounds.Builder twoPin = new LatLngBounds.Builder();

            LatLng pickupPoint = new LatLng(startLatitude,startLongitude);


            twoPin.include(latLng);
            twoPin.include(pickupPoint);
            LatLngBounds bounds = twoPin.build();
            float results[] = new float[10];
            Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
            //Toast.makeText(MapActivity2.this, "Distance: " + results[0] , Toast.LENGTH_SHORT).show();
            double pad = (results[0]/100000)+500;

           mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, ((int) pad)));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
            mSearchText.setVisibility(View.GONE);
            searchArea.setVisibility(View.GONE);
            mGps.setVisibility(View.GONE);
            journeyDetails.setVisibility(View.VISIBLE);
            Object dataTransfer[];
            String url;

            dataTransfer = new Object[3];
            url = getDirectionsUrl();
            GetDirectionsData getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            Log.d(TAG, "BIG TEST HERE URL:" + url);

            dataTransfer[2] = new LatLng(endLatitude, endLongitude);
            getDirectionsData.execute(dataTransfer);

           String[] info = getDirectionsData.getInfo(url);
           String price = info[0].replace(" min", "");
           price = price.replace(" mi", "");
           //int priceNum = Integer.parseInt(price);
           //priceNum = priceNum*5;
           confirmBut.setText("Confirm Journey" );
           durationText.setText(info[0]);
           duration = info[0];
           double priceNum = timeToMinutes(info[0]);
           priceNum -= 1;
           priceNum*=0.51;
           priceNum+=4.20;
           String priceS = formatDecimal((float) priceNum);
           pricing = priceS;
           cost.setText("€"+priceS.replace(" ","")+"");



               try {
                   pickUpPoint=getAddress(startLatitude,startLongitude);
               } catch (IOException e) {
                   e.printStackTrace();
               }



                try {
                    dropOffPoint=getAddress(endLatitude,endLongitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            pickUpTV.setText(" " +pickUpPoint);
            dropOffTV.setText(" " +dropOffPoint);
            //Toast.makeText(MapActivity2.this, "DURATION: " + info[0] + "DISTANCE" + info[1] , Toast.LENGTH_SHORT).show();


        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);

        }
        hideKeyboard();


    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity2.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

  private void hideKeyboard(){
      View view = this.getCurrentFocus();
      if (view != null) {
          InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
  }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener(){
      @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
          hideKeyboard();

          final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
          final String placeID =item.getPlaceId();

          PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeID);
          placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
      }
    };

  private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
      @Override
      public void onResult(@NonNull PlaceBuffer places) {
          if(!places.getStatus().isSuccess()){
              places.release();
              return;
          }
          Log.d(TAG, "TESTING PLACES:" +  places);
          Log.d(TAG, "TESTING PLACES GET NAME:" +  places.get(0).getName());
          Log.d(TAG, "TESTING PLACES GET LATLNG:" +  places.get(0).getLatLng());
          Log.d(TAG, "TESTING PLACES GET ADDRES:" +  places.get(0).getAddress());
          String point = places.get(0).getName() + ", " + places.get(0).getAddress();
          if(pickUpChosen){
              dropOffPoint = point;
          }
          else{
              pickUpPoint =point;
          }
          final Place place = places.get(0);
          try{
          mPlace = new PlaceInfo();
          //mPlace.setName(place.getName().toString());
          mPlace.setAddress(place.getAddress().toString());
          mPlace.setId(place.getId().toString());
          mPlace.setLatLng(place.getLatLng());
          mPlace.setName("Pick Up Point");

          }catch(NullPointerException e){}

          moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                  place.getViewport().getCenter().longitude),DEFAULT_ZOOM,mPlace.getName());

          latitude = place.getViewport().getCenter().latitude;
          longitude = place.getViewport().getCenter().longitude;

          places.release();

          confirmBut.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v) {
                  if(dropOff)
                  {
                      infoIntent();
                  }
                  else if(pickUpChosen)
                  {
                      endLatitude=latitude;
                      endLongitude=longitude;
                      dropOff=true;
                      moveCamera(new LatLng(endLatitude, endLongitude),DEFAULT_ZOOM,"");
                      float results[] = new float[10];
                      Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                      mMap.clear();
                      drawGrid();

                      LatLng pickUp = new LatLng(startLatitude,startLongitude);
                      MarkerOptions marker = new MarkerOptions();
                      marker.position(pickUp);
                      marker.title("Pick Up Point");
                      marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                      mMap.addMarker(marker);

                      LatLng dropOff = new LatLng(endLatitude,endLongitude);
                      MarkerOptions marker2 = new MarkerOptions();
                      marker2.position(dropOff);
                      marker2.title("Drop Off Point");
                      marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                      mMap.addMarker(marker2);
                     // Toast.makeText(MapActivity2.this, "Drop Off Point Selected", Toast.LENGTH_SHORT).show();

                  }
                  else{
                      pickUpChosen=true;
                      startLatitude= latitude;
                      startLongitude = longitude;
                      String id = makeID();
                      mDatabase = mDatabase.child(id);
                      mDatabase.child("Latitude").setValue(latitude);
                      mDatabase.child("Longitude").setValue(longitude);
                      confirmBut.setText("Confirm Destination");
                      mSearchText.setText("");
                      mSearchText.setHint("Enter Destination");
                      mMap.clear();
                      drawGrid();

                      LatLng pickUp = new LatLng(startLatitude,startLongitude);
                      MarkerOptions marker = new MarkerOptions();
                      marker.position(pickUp);
                      marker.title("Pick Up Point");
                      marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                      mMap.addMarker(marker);
                      Toast.makeText(MapActivity2.this, "Pick Up Point Selected", Toast.LENGTH_SHORT).show();

                  }
              }

          });

      }
  };

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+startLatitude+","+startLongitude);
        googleDirectionsUrl.append("&destination="+endLatitude+","+endLongitude);
        googleDirectionsUrl.append("&key="+"AIzaSyDlMqMklLecEjG2-3MCzPYIkHtAd4X_ZLs");

        return googleDirectionsUrl.toString();
    }

    private String makeID(){

        String contain = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(27);
        for(int i=0; i<27;i++)
        {
            sb.append(contain.charAt(rand.nextInt(contain.length())));
        }
        String random = sb.toString();
        return random;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        //marker.setTitle();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if((marker.getPosition().latitude==startLatitude && marker.getPosition().longitude==startLongitude)||
                (marker.getPosition().latitude==endLatitude && marker.getPosition().longitude==endLongitude)  )
        {
            marker.setDraggable(false);
        }
        else {
            marker.setDraggable(true);
        }return false;
    }

        public void onClick(View v) {
            if (v.getId() == R.id.confirmBut) {
                if (!pickUpChosen) {
                    pickUpChosen = true;
                    startLatitude = latitude;
                    startLongitude = longitude;
                    String id = makeID();
                    mDatabase = mDatabase.child(id);
                    mDatabase.child("Latitude").setValue(latitude);
                    mDatabase.child("Longitude").setValue(longitude);
                    confirmBut.setText("Confirm Destination");
                    mSearchText.setHint("Enter Destination");
                    mMap.clear();
                    drawGrid();
                    LatLng pickUp = new LatLng(startLatitude, startLongitude);
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(pickUp);
                    marker.title("Pick Up Point");
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mMap.addMarker(marker);
                } else {
                    endLatitude = latitude;
                    endLongitude = longitude;
                    dropOff = true;
//                                        Intent MergeIntent = new Intent(MapActivity2.this, HomeScreen.class);
//                                        MergeIntent.putExtra("StartLat",startLatitude);
//                                        MergeIntent.putExtra("StartLong",startLongitude);
//                                        MergeIntent.putExtra("EndLat",endLatitude);
//                                        MergeIntent.putExtra("EndLong",endLongitude);
//                                        MapActivity2.this.startActivity(MergeIntent);
                    moveCamera(new LatLng(endLatitude, endLongitude), DEFAULT_ZOOM, "");
                    // moveCamera(new LatLng(endLatitude, endLongitude),DEFAULT_ZOOM,"");


                }
            }
        }

        public int timeToMinutes(String time) {
                int minutes = 0;
            if (time.contains("day")) {
                time = time.replace(" day", "");
                time = time.replace("s", "");
                time = time.replace(" hour", "");
                String[] parts = time.split(" ");
                String part1 = parts[0];
                String part2 = parts[1];

                int days = Integer.parseInt(part1);
                int hours = Integer.parseInt(part2);
                hours *= 60;
                days *= 1440;
                minutes+=days;
                minutes += hours;

            } else if (time.contains("hour")) {
                time = time.replace(" min", "");
                time = time.replace("s", "");
                time = time.replace(" hour", "");
                String[] parts = time.split(" ");
                String part1 = parts[0];
                String part2 = parts[1];

                int hours = Integer.parseInt(part1);
                minutes = Integer.parseInt(part2);
                hours *= 60;
                minutes += hours;



            }
            else{
                time = time.replace(" min", "");
                time = time.replace("s", "");
                minutes = Integer.parseInt(time);

            }
            return minutes;
        }

    public String formatDecimal(float number) {
        float epsilon = 0.004f; // 4 tenths of a cent
        if (Math.abs(Math.round(number) - number) < epsilon) {
            return String.format("%10.0f", number); // sdb
        } else {
            return String.format("%10.2f", number); // dj_segfault
        }
    }

    public void infoIntent(){

        Intent sendInfo = new Intent(MapActivity2.this, MakeJourneyRequest.class);
        sendInfo.putExtra("Start", pickUpPoint);
        sendInfo.putExtra("End", dropOffPoint);
        sendInfo.putExtra("Cost", pricing);
        sendInfo.putExtra("Duration",duration);
        sendInfo.putExtra("SLat",startLatitude);
        sendInfo.putExtra("SLong",startLongitude);
        sendInfo.putExtra("ELat",endLatitude);
        sendInfo.putExtra("ELong",endLongitude);
        sendInfo.putExtra("date", dateTime);

        sendInfo.putExtra("dayFinal", dayFinal);
        sendInfo.putExtra("monthFinal", monthFinal);
        sendInfo.putExtra("yearFinal", yearFinal);
        sendInfo.putExtra("hourFinal", hourFinal);
        sendInfo.putExtra("minuteFinal", minuteFinal);


        MapActivity2.this.startActivity(sendInfo);
    }

    public String getAddress(Double lat, Double lon) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return address+", " +city;
    }

    private void drawGrid(){
        double blLat = 53.27;


        for(int j=0;j<100;j+=10){

            double blLong = -6.4;
            for(int i=0;i<10;i++){

                LatLng point1 = new LatLng(blLat,blLong);
                LatLng point2 = new LatLng(blLat,blLong+.032);
                LatLng point3 = new LatLng(blLat+.018,blLong+.032);
                LatLng point4 = new LatLng(blLat+.018,blLong);

                PolylineOptions poptions = new PolylineOptions().add(point1)
                        .add(point2).add(point3).add(point4).add(point1).width(2).color(Color.BLUE).geodesic(true);
                mMap.addPolyline(poptions);
                int colour =0;
                if(j==(i*10)){
                    colour =0x0F00FF00;
                }
                else if(i==5){
                    colour = 0x0F0000FF;
                }
                else
                    colour=0x0FFF0000;


                    PolygonOptions pgoptions = new PolygonOptions().add(point1)
                            .add(point2).add(point3).add(point4).add(point1).strokeWidth(0).geodesic(true).fillColor(colour);
                    mMap.addPolygon(pgoptions);

                blLong+=.032;
            }
            blLat+=.018;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(MapActivity2.this, MakeJourneyRequest.class);
            MapActivity2.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(MapActivity2.this, JR_List.class);
            MapActivity2.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(MapActivity2.this, FinalisedMerges.class);
            MapActivity2.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(MapActivity2.this, MainActivity.class);
            MapActivity2.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(MapActivity2.this, Image.class);
            MapActivity2.this.startActivity(MergeIntent);
        }
        return false;
    }

    public void offClick(){

        confirmBut.setClickable(false);
        searchArea.setClickable(false);

    }
    public void clickOn(){


        confirmBut.setClickable(true);
        searchArea.setClickable(true);

    }
    public void sideBar(){

        mAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference profile = FirebaseDatabase.getInstance().getReference().child("New Users").child(user_id);
        profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String photo = dataSnapshot.child("image").getValue().toString();

                NavigationView naview = findViewById(R.id.navView);
                View header = naview.getHeaderView(0);
                final ImageView sideImg = (ImageView) header.findViewById(R.id.hamimg);
                TextView sideTxt = header.findViewById(R.id.hamtxt);
                sideTxt.setText(name);
                Picasso.with(MapActivity2.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clearOldJourneys(String userID){
        DatabaseReference finalised = FirebaseDatabase.getInstance().getReference().child("New Users").child(userID).child("Finalised Merges");
        finalised.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    Iterable<DataSnapshot> fms = dataSnapshot.getChildren();
                    for (DataSnapshot finMerge : fms) {

                        String finmergeID = finMerge.getKey();
                        DatabaseReference finalMerge = FirebaseDatabase.getInstance().getReference().child("Finalised Merges").child(finmergeID);
                        finalMerge.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String date = dataSnapshot.child("Date").getValue().toString();
                                String time = dataSnapshot.child("Time").getValue().toString();
                                if(oldTime(date,time)) {
                                    DatabaseReference saveData = FirebaseDatabase.getInstance().getReference().child("Old Journeys").child(finmergeID);
                                    saveData.setValue(dataSnapshot.getValue());
                                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                    ArrayList<String> users = new ArrayList();
                                    for (DataSnapshot child : children) {
                                        String user = child.getKey();
                                        if(user.length()==28){
                                            Log.e("ADDING USER", user);
                                            users.add(user);
                                        }
                                    }
                                        for(int i=0;i<users.size();i++){
                                        String user = users.get(i);
                                            Log.e("USER FOUND", user);
                                            DatabaseReference userFinal = FirebaseDatabase.getInstance().getReference().child("New Users").child(user).child("Finalised Merges").child(finmergeID);
                                            userFinal.removeValue();
                                            for(int j=0;j<users.size();j++){
                                                String user2 = users.get(j);
                                                if (!user.equals(user2)) {
                                                    Log.e("Other USER FOUND", user2);
                                                    DatabaseReference user2name = FirebaseDatabase.getInstance().getReference().child("New Users").child(user2);
                                                    user2name.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String name = dataSnapshot.child("Name").getValue().toString();
                                                            DatabaseReference rev = FirebaseDatabase.getInstance().getReference().child("New Users").child(user).child("Pending Reviews");
                                                            rev.child(user2).setValue(name);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                }
                                            }




                                    }
                                    finalMerge.removeValue();
                                }
                                else{
                                    Log.e("Date",date + "Time: " + time);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        DatabaseReference oldJRS = FirebaseDatabase.getInstance().getReference().child("New Users").child(userID).child("Journey Requests");
//        oldJRS.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    Iterable<DataSnapshot> jrs = dataSnapshot.getChildren();
//                    for (DataSnapshot jreq : jrs) {
//
//                        String JRID = jreq.getKey();
//                        String start = dataSnapshot.child(JRID).child("StartGrid").getValue().toString();
//                        String end = dataSnapshot.child(JRID).child("EndGrid").getValue().toString();
//
//                        DatabaseReference JRs = FirebaseDatabase.getInstance().getReference().child("Journey Requests").child(start).child(end).child(JRID);
//                        JRs.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String time = dataSnapshot.child("Time").getValue().toString();
//                                String date = dataSnapshot.child("Date").getValue().toString();
//                                if(oldTime(date,time)){
//                                    if(dataSnapshot.child("Proposed Merges").getValue()!=null){
//                                        Iterable<DataSnapshot> mps = dataSnapshot.child("Proposed Merges").getChildren();
//                                        for (DataSnapshot pm : mps) {
//
//                                        }
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }
    public boolean oldTime(String date, String time){

        int curhour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int curminute = Calendar.getInstance().get(Calendar.MINUTE);
        int curday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int curmonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        int curYEAR = Calendar.getInstance().get(Calendar.YEAR);

        final String[] dates = date.split(Pattern.quote("/"));
        int day = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int YEAR = Integer.parseInt(dates[2]);

        final String[] times = time.split(Pattern.quote(":"));
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);


        if(YEAR<curYEAR)
        {
            return true;
        }
        else if(YEAR==curYEAR){
            if(month<curmonth)
            {
                return true;
            }
            else if(month==curmonth){
                if(day<curday)
                {
                    return true;
                }
                else if(day==curday){
                    if(hour+1<curhour)
                    {
                        return true;
                    }
                    else if(hour+1==curhour){
                        if(minute<curminute)
                        {
                            return true;
                        }

                    }

                }

            }
        }

        Log.e("Entered Date", day+"/"+month+"/"+YEAR+ "    Time: "+ hour+":"+minute);
        Log.e("Current Date", curday+"/"+curmonth+"/"+curYEAR+ "    Time: "+ curhour+":"+curminute);
        return false;
    }
}