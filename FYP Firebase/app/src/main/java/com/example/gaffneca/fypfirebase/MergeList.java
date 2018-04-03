package com.example.gaffneca.fypfirebase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MergeList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button Accept;
    private TextView tvMerge;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String startS;
    private String endS;
    private String jrS;
    private String mergeID;
    private String compat;
    private ImageView homeBut;
    private ImageView logoutBut;
    private TextView homeTxt;
    private TextView logoutTxt;
    private boolean logOpen;
    private ImageView open;
    private ImageView close;
    private DrawerLayout mDrawerLayout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_list);
        mAuth=FirebaseAuth.getInstance();


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
                Intent LogoutIntent = new Intent(MergeList.this, MainActivity.class);
                MergeList.this.startActivity(LogoutIntent);
            }
        });



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
                close.setVisibility(View.GONE);
                open.setVisibility(View.VISIBLE);


            }
        });



        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.setElevation(0);

        }

        mAuth=FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("").child("Trinity College");
        final LinearLayout mylinlay = (LinearLayout) findViewById(R.id.linlay);







        tvMerge = (TextView) findViewById(R.id.noMerge);
        Intent intent = getIntent();
        final String start = intent.getStringExtra("start");
        final String end = intent.getStringExtra("end");
        final String JRID = intent.getStringExtra("JRID");
        int anyMerges = intent.getIntExtra("mergesFound",7);
        if(anyMerges==1)
        {
            ArrayList<String> pMerges = intent.getStringArrayListExtra("Array");



            final Button[] buts;
            final TextView[] tvs;
            final TextView[] status;
            final LinearLayout[] lins;
            int size = 100; // total number of TextViews to add
            //Toast.makeText(MergeList.this, Integer.toString(pMerges.size()), Toast.LENGTH_LONG).show();

            buts = new Button[size];
            tvs = new TextView[size];
            status = new TextView[size];
            lins = new LinearLayout[size];
            Button temp;

            for (int i = 0; i < size; i++)
            {
                buts[i] = new Button(this);
                status[i] = new TextView(this);
                tvs[i] = new TextView(this);
                lins[i] = new LinearLayout(this);

                status[i].setPadding(20,0,0,0);
                tvs[i].setPadding(20,0,0,20);

                status[i].setTextSize(13);
                tvs[i].setTextSize(17);
                buts[i].setHeight(50);

                status[i].setVisibility(View.GONE);
                tvs[i].setVisibility(View.GONE);
                buts[i].setVisibility(View.GONE);

                mylinlay.addView(lins[i]);
                mylinlay.addView(status[i]);
                mylinlay.addView(tvs[i]);
                mylinlay.addView(buts[i]);


//                lins[i].setVisibility(View.GONE);
//                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
//                        new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT));
//                linearParams.setMargins(0, 80, 0, 0);
//                lins[i].setLayoutParams(linearParams);
//                lins[i].requestLayout();
//                lins[i].setBackgroundColor(0x0333);


//                mylinlay.addView(lins[i]);

            }


            DatabaseReference propMerge =  FirebaseDatabase.getInstance().getReference().child("Journey Requests").child(start).child(end).child(JRID).child("Proposed Merges");
            propMerge.orderByChild("Compat").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {



                    //tvMerge.setText("Heres your merges");
                    int j =0;
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot pMerges : children) {
                        final int finalJ = j;



                        final String mergeID = pMerges.getKey().toString();
                        final String compat = pMerges.child("Compat").getValue().toString();
                        final String accepted = pMerges.child("Accepted").getValue().toString();

                        DatabaseReference merge = FirebaseDatabase.getInstance().getReference().child("Merge Proposals").child(mergeID);


                        merge.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                        } else {

                            String id = "";
                            String id2 = "";
                            String id3 = "";
                            final int amount = Integer.parseInt(dataSnapshot.child("Amount").getValue().toString());
                            final int amountAccepted = Integer.parseInt(dataSnapshot.child("AmountAccepted").getValue().toString());
                            int count = 0;
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {

                                String user = child.getKey();
                                if (user.length() == 28) {
                                    if (!user.equals(user_id)) {
                                        if (count == 0) {
                                            id = user;
                                            count++;
                                        } else if (count == 1) {
                                            id2 = user;
                                            count++;
                                        } else if (count == 2) {
                                            id3 = user;
                                            count++;
                                        }

                                    }
                                }


                            }

                            // final int amount = count+1;
                            Log.e("USER ID", id);
                            DatabaseReference userName = FirebaseDatabase.getInstance().getReference().child("New Users");
                            final String finalId = id;
                            final String finalId2 = id2;
                            final String finalId3 = id3;
                            userName.addListenerForSingleValueEvent(new ValueEventListener() {
                                String withName;
                                String withName2;
                                String withName3;

                                String acceptTotal = amountAccepted+ "/"+amount+" Passengers Accepted";

                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (amount == 2) {
                                        withName = dataSnapshot.child(finalId).child("Name").getValue().toString();
                                        tvs[size-finalJ-1].setText("With: " + withName + getString(R.string.tab) + getString(R.string.tab) + getString(R.string.tab) + "\nCompatibility: " + compat);
                                    } else if (amount == 3) {
                                        withName = dataSnapshot.child(finalId).child("Name").getValue().toString();
                                        withName2 = dataSnapshot.child(finalId2).child("Name").getValue().toString();
                                        tvs[size-finalJ-1].setText("With: " + withName + " and " + withName2 + getString(R.string.tab) + getString(R.string.tab) + "\nCompatibility: " + compat);
                                    } else if (amount == 4) {
                                        withName = dataSnapshot.child(finalId).child("Name").getValue().toString();
                                         withName2 = dataSnapshot.child(finalId2).child("Name").getValue().toString();
                                         withName3 = dataSnapshot.child(finalId3).child("Name").getValue().toString();
                                        tvs[size-finalJ-1].setText("With: " + withName + ", " + withName2 + " and " + withName3 + getString(R.string.tab) + "\nCompatibility: " );
                                    }
                                    if (accepted.equals("YES")) {
                                        status[size-finalJ-1].setText("Accepted           "+getString(R.string.tab)+acceptTotal);
                                        status[size-finalJ-1].setTextColor(0xff32CD32);

                                    } else {
                                        status[size-finalJ-1].setText("Not Accepted Yet"+getString(R.string.tab)+acceptTotal);
                                        status[size-finalJ-1].setTextColor(0xffF6A34F);
                                    }

                                    final String with = withName;
                                    tvs[size-finalJ-1].setTextColor(0xFF000000);
                                    buts[size-finalJ-1].setText("View Proposed Merge");
                                    buts[size-finalJ-1].setBackgroundColor(0xFF3BB9FF);
                                    buts[size-finalJ-1].setTextColor(0xFFFFFFFF);
//                                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(750, 120);
//                                    linearParams.setMargins(50,0,50,0);
//
//                                    buts[size-finalJ-1].setLayoutParams(linearParams);



                                    tvMerge.setVisibility(View.GONE);

                                    buts[size-finalJ-1].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent MergeIntent = new Intent(MergeList.this, MergeMap.class);
                                            MergeIntent.putExtra("Merge", mergeID);
                                            MergeIntent.putExtra("JR", JRID);
                                            MergeIntent.putExtra("End", end);
                                            MergeIntent.putExtra("Start", start);
                                            MergeIntent.putExtra("Name", withName);
                                            MergeIntent.putExtra("ID", finalId);
                                            MergeIntent.putExtra("Amount", amount);
                                            if(amount>2){
                                                MergeIntent.putExtra("Name2", withName2);
                                                MergeIntent.putExtra("ID2", finalId2);
                                            }
                                            if(amount>3){
                                                MergeIntent.putExtra("Name3", withName3);
                                                MergeIntent.putExtra("ID3", finalId3);
                                            }
                                            MergeList.this.startActivity(MergeIntent);
                                        }

                                    });

                                                    LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                linearParams2.setMargins(50, 0, 50, 0);

                                    LinearLayout.LayoutParams linearParams3 = new LinearLayout.LayoutParams(
                                            new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                                    linearParams3.setMargins(40, 60, 0, 0);

                                    lins[size-finalJ-1].setLayoutParams(linearParams3);
                                    lins[size-finalJ-1].setBackgroundColor(0xff00ff00);

                                    status[size-finalJ-1].setLayoutParams(linearParams2);
                                    tvs[size-finalJ-1].setLayoutParams(linearParams2);
                                    buts[size-finalJ-1].setLayoutParams(linearParams2);

                                    status[size-finalJ-1].setBackgroundColor(0xffffffff);
                                    tvs[size-finalJ-1].setBackgroundColor(0xffffffff);

                                    status[size-finalJ-1].setVisibility(View.VISIBLE);
                                    tvs[size-finalJ-1].setVisibility(View.VISIBLE);
                                    buts[size-finalJ-1].setVisibility(View.VISIBLE);
                                    lins[size-finalJ-1].setVisibility(View.VISIBLE);

                                    status[size-finalJ-1].setElevation(20);
                                    tvs[size-finalJ-1].setElevation(20);
                                    buts[size-finalJ-1].setElevation(20);
                                    lins[size-finalJ-1].setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



//                tvs[j].setText("With: John" + getString(R.string.tab)+"Compatibility: " + compat);
//                tvs[j].setPadding(0,20, 0,0);
//                buts[j].setText("View Merge");
//                tvMerge.setVisibility(View.GONE);
                        j++;


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        else{
           tvMerge.setText("No Merges");

        }


       // tvMerge.setText(start + "+" + end);

        final DatabaseReference dbmerge = mDatabase.child(user_id).child("Journey Requests");
        //DatabaseReference start = dbmerge.child("Start");

//        start.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                 startS = dataSnapshot.getValue().toString();
//                DatabaseReference end = dbmerge.child("End");
//                end.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        endS = dataSnapshot.getValue().toString();
//                        DatabaseReference jr = dbmerge.child("JR");
//
//                        jr.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                jrS = dataSnapshot.getValue().toString();
//
//                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Journey Requests");
//                                mDatabase = mDatabase.child(startS).child(endS).child(jrS).child("Proposed Merges");
//                                mDatabase.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//
//                                        for(DataSnapshot child: children){
//                                            mergeID = child.getKey().toString();
//                                            mDatabase = mDatabase.child(mergeID).child("Compat");
//
//                                            mDatabase.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                    compat = dataSnapshot.getValue().toString();
//                                                    tvMerge.setText("Merge ID: " + mergeID + "      Compatibility: " + compat + "%");
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//
//                                        }
//
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });






    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(MergeList.this, MakeJourneyRequest.class);
            MergeList.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(MergeList.this, JR_List.class);
            MergeList.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(MergeList.this, FinalisedMerges.class);
            MergeList.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(MergeList.this, MainActivity.class);
            MergeList.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(MergeList.this, Image.class);
            MergeList.this.startActivity(MergeIntent);
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
                Picasso.with(MergeList.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
