package com.example.gaffneca.fypfirebase;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaffneca.fypfirebase.route.GetDirectionsData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class FinalMap extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap mMap;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private double startLatitude2;
    private double startLongitude2;
    private double endLatitude2;
    private double endLongitude2;
    private double startLatitude3;
    private double startLongitude3;
    private double endLatitude3;
    private double endLongitude3;
    private double startLatitude4;
    private double startLongitude4;
    private double endLatitude4;
    private double endLongitude4;
    private FirebaseAuth mAuth;
    private String user_id;
    private TextView tvYourCost;
    private TextView tvActualCost;
    private TextView tvYourDur;
    private TextView tvActualDur;
    private TextView tvWith;
    private TextView tvWith2;
    private TextView tvWith3;
    private Button conBut;

    private ImageView homeBut;
    private ImageView logoutBut;
    private TextView homeTxt;
    private TextView logoutTxt;
    private boolean logOpen;
    private ImageView open;
    private ImageView close;
    private DrawerLayout mDrawerLayout;


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();


        Intent intent = getIntent();
        final String mergeID = intent.getStringExtra("Merge");
        final String JRID = intent.getStringExtra("JR");
        final String name = intent.getStringExtra("Name");
        final String id = intent.getStringExtra("ID");





        DatabaseReference merge = FirebaseDatabase.getInstance().getReference().child("Finalised Merges").child(mergeID);
        merge.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int amount = Integer.parseInt( dataSnapshot.child("Amount").getValue().toString());

                Toast.makeText(FinalMap.this, ""+amount, Toast.LENGTH_SHORT).show();



                //Toast.makeText(MergeMap.this, "Merge Master: " +mergeID, Toast.LENGTH_SHORT).show();
                startLatitude = (double) dataSnapshot.child("StartLat1").getValue();
                startLongitude = (double) dataSnapshot.child("StartLon1").getValue();
                startLatitude2 = (double) dataSnapshot.child("StartLat2").getValue();
                startLongitude2 = (double) dataSnapshot.child("StartLon2").getValue();
                endLatitude = (double) dataSnapshot.child("EndLat1").getValue();
                endLongitude = (double) dataSnapshot.child("EndLon1").getValue();
                endLatitude2 = (double) dataSnapshot.child("EndLat2").getValue();
                endLongitude2 = (double) dataSnapshot.child("EndLon2").getValue();

                if(amount>2){
                    startLatitude3 = (double) dataSnapshot.child("StartLat3").getValue();
                    startLongitude3 = (double) dataSnapshot.child("StartLon3").getValue();
                    endLatitude3 = (double) dataSnapshot.child("EndLat3").getValue();
                    endLongitude3 = (double) dataSnapshot.child("EndLon3").getValue();
                }
                if(amount>3){
                    startLatitude4 = (double) dataSnapshot.child("StartLat4").getValue();
                    startLongitude4 = (double) dataSnapshot.child("StartLon4").getValue();
                    endLatitude4 = (double) dataSnapshot.child("EndLat4").getValue();
                    endLongitude4 = (double) dataSnapshot.child("EndLon4").getValue();

                }



                double yourSLat =(double) dataSnapshot.child(user_id).child("StartLat").getValue();
                double yourSLon =(double) dataSnapshot.child(user_id).child("StartLon").getValue();
                LatLng yourPickUp = new LatLng(yourSLat,yourSLon);
                double yourELat =(double) dataSnapshot.child(user_id).child("EndLat").getValue();
                double yourELon =(double) dataSnapshot.child(user_id).child("EndLon").getValue();

                String yourCost = dataSnapshot.child(user_id).child("Cost").getValue().toString();
                String yourDuration = dataSnapshot.child(user_id).child("Duration").getValue().toString();
                String desCost = dataSnapshot.child(user_id).child("Desired Cost").getValue().toString();
                String desDuration = dataSnapshot.child(user_id).child("Desired Duration").getValue().toString();

                Log.e("TESTYING", yourSLat+" "+yourSLon+" "+yourELat +" "+ yourCost+" "+desDuration);

                tvYourCost = findViewById(R.id.textView8);
                tvActualCost = findViewById(R.id.textView10);
                tvYourDur = findViewById(R.id.textView5);
                tvActualDur = findViewById(R.id.textView6);
                conBut = findViewById(R.id.confirmBut);

                tvActualCost.setText("€"+formatDecimal(Float.parseFloat(yourCost)).replace(" ",""));
                tvActualDur.setText(yourDuration+" min");
                tvYourCost.setText("€"+desCost);
                tvYourDur.setText(desDuration+ " min");



                LatLng yourDropOff = new LatLng(yourELat,yourELon);


                final LatLng pickUp = new LatLng(startLatitude,startLongitude);
                MarkerOptions marker = new MarkerOptions();
                marker.position(pickUp);
                marker.title("Pick Up Point");
                if(startLatitude==yourSLat){
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    marker.title("Your Pick Up Point");

                }
                mMap.addMarker(marker);

                final LatLng dropOff = new LatLng(endLatitude,endLongitude);
                MarkerOptions marker2 = new MarkerOptions();
                marker2.position(dropOff);
                marker2.title("Drop Off Point");
                if(endLatitude==yourELat){
                    marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    marker2.title("Your Drop Off Point");

                }                mMap.addMarker(marker2);

                LatLng pickUp2 = new LatLng(startLatitude2,startLongitude2);
                marker = new MarkerOptions();
                marker.position(pickUp2);
                marker.title("Pick Up Point 2");
                if(startLatitude2==yourSLat){
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    marker.title("Your Pick Up Point");

                }                mMap.addMarker(marker);

                LatLng dropOff2 = new LatLng(endLatitude2,endLongitude2);
                marker2 = new MarkerOptions();
                marker2.position(dropOff2);
                marker2.title("Drop Off Point 2");
                if(endLatitude2==yourELat){
                    marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    marker2.title("Your Drop Off Point");

                }
                mMap.addMarker(marker2);

                if(amount>2) {
                    LatLng pickUp3 = new LatLng(startLatitude3, startLongitude3);
                    marker = new MarkerOptions();
                    marker.position(pickUp3);
                    marker.title("Pick Up Point 3");
                    if (startLatitude3 == yourSLat) {
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        marker.title("Your Pick Up Point");

                    }
                    mMap.addMarker(marker);

                    LatLng dropOff3 = new LatLng(endLatitude3, endLongitude3);
                    marker2 = new MarkerOptions();
                    marker2.position(dropOff3);
                    marker2.title("Drop Off Point 3");
                    if (endLatitude3 == yourELat) {
                        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        marker2.title("Your Drop Off Point");

                    }

                    mMap.addMarker(marker2);
                }

                if(amount>3) {
                    LatLng pickUp4 = new LatLng(startLatitude4, startLongitude4);
                    marker = new MarkerOptions();
                    marker.position(pickUp4);
                    marker.title("Pick Up Point 4");
                    if (startLatitude3 == yourSLat) {
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        marker.title("Your Pick Up Point");

                    }
                    mMap.addMarker(marker);

                    LatLng dropOff4 = new LatLng(endLatitude4, endLongitude4);
                    marker2 = new MarkerOptions();
                    marker2.position(dropOff4);
                    marker2.title("Drop Off Point 4");
                    if (endLatitude4 == yourELat) {
                        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        marker2.title("Your Drop Off Point");

                    }

                    mMap.addMarker(marker2);
                }






                LatLngBounds.Builder twoPin = new LatLngBounds.Builder();

                twoPin.include(pickUp);
                twoPin.include(dropOff);
                LatLngBounds bounds = twoPin.build();
                float results[] = new float[10];
                Location.distanceBetween(startLatitude,startLongitude,startLatitude2,startLongitude2,results);
                double pad = (results[0]/100000)+500;

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, ((int) pad)));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));




//

                //SECTIONS OF JOURNEY
                final Object dataTransfer[];
                dataTransfer = new Object[3];
                final String url = getDirectionsUrl2(startLatitude,startLongitude,startLatitude2,startLongitude2);
                final GetDirectionsData getDirectionsData = new GetDirectionsData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(endLatitude, endLongitude);
                getDirectionsData.execute(dataTransfer);

                Object dataTransfer2[];
                dataTransfer2 = new Object[3];
                final String url2 = getDirectionsUrl2(endLatitude,endLongitude,endLatitude2,endLongitude2);
                final GetDirectionsData getDirectionsData2 = new GetDirectionsData();
                dataTransfer2[0] = mMap;
                dataTransfer2[1] = url2;
                dataTransfer2[2] = new LatLng(endLatitude, endLongitude);
                getDirectionsData2.execute(dataTransfer2);



                if(amount>2){
                    final Object dataTransfer5[];
                    dataTransfer5 = new Object[3];
                    final String url5 = getDirectionsUrl2(startLatitude2,startLongitude2,startLatitude3,startLongitude3);
                    final GetDirectionsData getDirectionsData5 = new GetDirectionsData();
                    dataTransfer5[0] = mMap;
                    dataTransfer5[1] = url5;
                    dataTransfer5[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData5.execute(dataTransfer5);

                    Object dataTransfer6[];
                    dataTransfer6 = new Object[3];
                    final String url6 = getDirectionsUrl2(endLatitude2,endLongitude2,endLatitude3,endLongitude3);
                    final GetDirectionsData getDirectionsData6 = new GetDirectionsData();
                    dataTransfer6[0] = mMap;
                    dataTransfer6[1] = url6;
                    dataTransfer6[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData6.execute(dataTransfer6);
                }

                if(amount>3){
                    final Object dataTransfer7[];
                    dataTransfer7 = new Object[3];
                    final String url7 = getDirectionsUrl2(startLatitude3,startLongitude3,startLatitude4,startLongitude4);
                    final GetDirectionsData getDirectionsData7 = new GetDirectionsData();
                    dataTransfer7[0] = mMap;
                    dataTransfer7[1] = url7;
                    dataTransfer7[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData7.execute(dataTransfer7);

                    Object dataTransfer3[];
                    dataTransfer3 = new Object[3];
                    final String url3 = getDirectionsUrl2(endLatitude3,endLongitude3,endLatitude4,endLongitude4);
                    final GetDirectionsData getDirectionsData3 = new GetDirectionsData();
                    dataTransfer3[0] = mMap;
                    dataTransfer3[1] = url3;
                    dataTransfer3[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData3.execute(dataTransfer3);
                }

                if(amount==2){
                    final Object dataTransfer4[];
                    dataTransfer4 = new Object[3];
                    final String url4 = getDirectionsUrl2(startLatitude2,startLongitude2,endLatitude,endLongitude);
                    final GetDirectionsData getDirectionsData4 = new GetDirectionsData();
                    dataTransfer4[0] = mMap;
                    dataTransfer4[1] = url4;
                    dataTransfer4[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData4.execute(dataTransfer4);
                }
                else if(amount==3){
                    final Object dataTransfer4[];
                    dataTransfer4 = new Object[3];
                    final String url4 = getDirectionsUrl2(startLatitude3,startLongitude3,endLatitude,endLongitude);
                    final GetDirectionsData getDirectionsData4 = new GetDirectionsData();
                    dataTransfer4[0] = mMap;
                    dataTransfer4[1] = url4;
                    dataTransfer4[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData4.execute(dataTransfer4);
                }
                else{
                    final Object dataTransfer4[];
                    dataTransfer4 = new Object[3];
                    final String url4 = getDirectionsUrl2(startLatitude4,startLongitude4,endLatitude,endLongitude);
                    final GetDirectionsData getDirectionsData4 = new GetDirectionsData();
                    dataTransfer4[0] = mMap;
                    dataTransfer4[1] = url4;
                    dataTransfer4[2] = new LatLng(endLatitude, endLongitude);
                    getDirectionsData4.execute(dataTransfer4);
                }





            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_map);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapM);
        mapFragment.getMapAsync(this);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tvYourCost = findViewById(R.id.textView8);
        tvActualCost = findViewById(R.id.textView10);
        tvYourDur = findViewById(R.id.textView5);
        tvActualDur = findViewById(R.id.textView6);
        conBut = findViewById(R.id.confirmBut);

        mAuth = FirebaseAuth.getInstance();


        logoutBut = findViewById(R.id.logout);
        logoutTxt = findViewById(R.id.logoutTxt);
        logOpen = false;


        logoutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logOpen) {
                    logoutTxt.setVisibility(View.GONE);
                    logOpen = false;
                } else {
                    logoutTxt.setVisibility(View.VISIBLE);
                    logOpen = true;
                }
            }
        });
        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent LogoutIntent = new Intent(FinalMap.this, MainActivity.class);
                FinalMap.this.startActivity(LogoutIntent);
            }
        });


        open = findViewById(R.id.ic_menu);
        close = findViewById(R.id.ic_back);
        mDrawerLayout = findViewById(R.id.drawerWin);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);


        sideBar();

        open.setOnClickListener(new View.OnClickListener() {
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


        Intent intent = getIntent();
        if (intent.getStringExtra("Merge") != null) {
            final String mergeID = intent.getStringExtra("Merge");
            final String JRID = intent.getStringExtra("JR");
            final String name = intent.getStringExtra("Name");
            final String id = intent.getStringExtra("ID");


            final int amount = intent.getIntExtra("Amount", 1);
            Toast.makeText(this, "Create "+amount, Toast.LENGTH_SHORT).show();




            tvWith = findViewById(R.id.textView1);
            tvWith.setText(name);
            tvWith.setVisibility(View.VISIBLE);
            tvWith.setTextColor(0xff33b5e5);
            tvWith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(FinalMap.this, ViewOtherProfile.class);
                    profileIntent.putExtra("ID", id);
                    profileIntent.putExtra("JR", JRID);
                    profileIntent.putExtra("Merge", mergeID);
                    profileIntent.putExtra("Name", name);
                    profileIntent.putExtra("Amount",amount);




                    FinalMap.this.startActivity(profileIntent);

                }
            });

            if (amount > 2) {
                final String name2 = intent.getStringExtra("Name2");
                final String id2 = intent.getStringExtra("ID2");

                tvWith2 = findViewById(R.id.textView1a);
                tvWith2.setText(name2);
                tvWith2.setVisibility(View.VISIBLE);
                tvWith2.setTextColor(0xff33b5e5);
                tvWith2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(FinalMap.this, ViewOtherProfile.class);
                        profileIntent.putExtra("ID", id2);
                        profileIntent.putExtra("JR", JRID);
                        profileIntent.putExtra("ID2", id2);
                        profileIntent.putExtra("Name2", name2);
                        profileIntent.putExtra("Merge", mergeID);
                        profileIntent.putExtra("Name", name);
                        profileIntent.putExtra("Amount",amount);



                        FinalMap.this.startActivity(profileIntent);

                    }
                });
            }
            if (amount > 3) {
                final String name2 = intent.getStringExtra("Name2");
                final String id2 = intent.getStringExtra("ID2");
                final String name3 = intent.getStringExtra("Name3");
                final String id3 = intent.getStringExtra("ID3");


                tvWith3 = findViewById(R.id.textView1b);
                tvWith3.setText(name);
                tvWith3.setTextColor(0xff33b5e5);
                tvWith3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(FinalMap.this, ViewOtherProfile.class);
                        profileIntent.putExtra("ID", id);
                        profileIntent.putExtra("JR", JRID);
                        profileIntent.putExtra("ID2", id2);
                        profileIntent.putExtra("Name2", name2);
                        profileIntent.putExtra("ID3", id3);
                        profileIntent.putExtra("Name3", name3);
                        profileIntent.putExtra("Merge", mergeID);
                        profileIntent.putExtra("Name", name);
                        profileIntent.putExtra("Amount", amount);


                        FinalMap.this.startActivity(profileIntent);

                    }
                });
            }
        }

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }




    private String getDirectionsUrl2(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+startLatitude+","+startLongitude);
        googleDirectionsUrl.append("&destination="+endLatitude+","+endLongitude);
        googleDirectionsUrl.append("&key="+"AIzaSyDlMqMklLecEjG2-3MCzPYIkHtAd4X_ZLs");

        return googleDirectionsUrl.toString();
    }

    private int getTimeDistance(String[] info){


        double priceNum = timeToMinutes(info[0]);
        int duration = timeToMinutes(info[0]);

        return duration;
    }

    private double getFare(double priceNum){

        priceNum -= 1;
        priceNum*=0.51;
        priceNum+=4.20;
        String priceS = formatDecimal((float) priceNum);
        priceS = priceS.replace(" ", "");
        double cost = Double.parseDouble(priceS);

        return cost;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(FinalMap.this, MakeJourneyRequest.class);
            FinalMap.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(FinalMap.this, JR_List.class);
            FinalMap.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(FinalMap.this, FinalisedMerges.class);
            FinalMap.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(FinalMap.this, MainActivity.class);
            FinalMap.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(FinalMap.this, Image.class);
            FinalMap.this.startActivity(MergeIntent);
        }
        return false;
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
                Picasso.with(FinalMap.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
