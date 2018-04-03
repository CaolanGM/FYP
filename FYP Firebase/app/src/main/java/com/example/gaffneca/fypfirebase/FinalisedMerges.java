package com.example.gaffneca.fypfirebase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FinalisedMerges extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalised_merges);




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
                    Intent LogoutIntent = new Intent(FinalisedMerges.this, MainActivity.class);
                    FinalisedMerges.this.startActivity(LogoutIntent);
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
                mDrawerLayout.setElevation(0);
                close.setVisibility(View.GONE);
                open.setVisibility(View.VISIBLE);


            }
        });


        mAuth = FirebaseAuth.getInstance();
            final String user_id = mAuth.getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("").child("Trinity College");
            final LinearLayout mylinlay = (LinearLayout) findViewById(R.id.linlay);


            tvMerge = (TextView) findViewById(R.id.noMerge);
            Intent intent = getIntent();



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

                for (int i = 0; i < size; i++) {
                    buts[i] = new Button(this);
                    status[i] = new TextView(this);
                    tvs[i] = new TextView(this);
                    lins[i] = new LinearLayout(this);


                    status[i].setPadding(0, 80, 0, 0);
                    tvs[i].setPadding(20, 0, 0, 20);

                    status[i].setTextSize(13);
                    tvs[i].setTextSize(17);
                    buts[i].setHeight(50);


                    status[i].setVisibility(View.GONE);
                    tvs[i].setVisibility(View.GONE);
                    buts[i].setVisibility(View.GONE);

                    mylinlay.addView(lins[i]);
                    mylinlay.addView(tvs[i]);
                    mylinlay.addView(buts[i]);

                }


                DatabaseReference propMerge = FirebaseDatabase.getInstance().getReference().child("New Users").child(user_id).child("Finalised Merges");
                propMerge.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        //tvMerge.setText("Heres your merges");
                        int j = 0;
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot pMerges : children) {
                            final int finalJ = j;


                            final String mergeID = pMerges.getKey().toString();
                            final String compat = pMerges.child("Compat").getValue().toString();
                            final String JRID = "dfgdfgdfhg";

                            DatabaseReference merge = FirebaseDatabase.getInstance().getReference().child("Finalised Merges").child(mergeID);


                            merge.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                    } else {

                                        String id = "";
                                        String id2 = "";
                                        String id3 = "";
                                        final int amount = Integer.parseInt(dataSnapshot.child("Amount").getValue().toString());
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
                                            String withName,withName2,withName3;

                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (amount == 2) {
                                                    withName = dataSnapshot.child(finalId).child("Name").getValue().toString();
                                                    tvs[finalJ].setText("With: " + withName + getString(R.string.tab) + getString(R.string.tab) + getString(R.string.tab) + "\nCompatibility: " + compat);
                                                } else if (amount == 3) {
                                                    withName = dataSnapshot.child(finalId).child("Name").getValue().toString();
                                                     withName2 = dataSnapshot.child(finalId2).child("Name").getValue().toString();
                                                    tvs[finalJ].setText("With: " + withName + " and " + withName2 + getString(R.string.tab) + getString(R.string.tab) + "\nCompatibility: " + compat);
                                                } else if (amount == 4) {
                                                    withName = dataSnapshot.child(finalId).child("Name").getValue().toString();
                                                    withName2 = dataSnapshot.child(finalId2).child("Name").getValue().toString();
                                                     withName3 = dataSnapshot.child(finalId3).child("Name").getValue().toString();
                                                    tvs[finalJ].setText("With: " + withName + ", " + withName2 + " and " + withName3 + getString(R.string.tab) + "\nCompatibility: " + compat);
                                                }


                                                final String with = withName;
                                                tvs[finalJ].setTextColor(0xFF000000);
                                                buts[finalJ].setText("View Finalised Merge");
                                                buts[finalJ].setBackgroundColor(0xFF3BB9FF);
                                                buts[finalJ].setTextColor(0xFFFFFFFF);
                                                tvMerge.setVisibility(View.GONE);

                                                buts[finalJ].setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent MergeIntent = new Intent(FinalisedMerges.this, FinalMap.class);
                                                        MergeIntent.putExtra("Merge", mergeID);
                                                        MergeIntent.putExtra("JR", JRID);
                                                        MergeIntent.putExtra("Name", withName);
                                                        MergeIntent.putExtra("ID", finalId);
                                                        MergeIntent.putExtra("Amount", amount);

                                                        if(amount>2)
                                                            MergeIntent.putExtra("ID2", finalId2);
                                                            MergeIntent.putExtra("Name2", withName2);
                                                        if(amount>3)
                                                            MergeIntent.putExtra("ID3", finalId3);
                                                            MergeIntent.putExtra("Name3", withName3);
                                                        FinalisedMerges.this.startActivity(MergeIntent);
                                                    }

                                                });

                                                status[finalJ].setVisibility(View.VISIBLE);
                                                tvs[finalJ].setVisibility(View.VISIBLE);
                                                buts[finalJ].setVisibility(View.VISIBLE);


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

                                                lins[finalJ].setLayoutParams(linearParams3);
                                                lins[finalJ].setBackgroundColor(0xff00ff00);

                                                status[finalJ].setLayoutParams(linearParams2);
                                                tvs[finalJ].setLayoutParams(linearParams2);
                                                buts[finalJ].setLayoutParams(linearParams2);

                                                status[finalJ].setBackgroundColor(0xffffffff);
                                                tvs[finalJ].setBackgroundColor(0xffffffff);

                                                status[finalJ].setVisibility(View.VISIBLE);
                                                tvs[finalJ].setVisibility(View.VISIBLE);
                                                buts[finalJ].setVisibility(View.VISIBLE);
                                                lins[finalJ].setVisibility(View.VISIBLE);

                                                status[finalJ].setElevation(20);
                                                tvs[finalJ].setElevation(20);
                                                buts[finalJ].setElevation(20);
                                                lins[finalJ].setVisibility(View.VISIBLE);


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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(FinalisedMerges.this, MakeJourneyRequest.class);
            FinalisedMerges.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(FinalisedMerges.this, JR_List.class);
            FinalisedMerges.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(FinalisedMerges.this, FinalisedMerges.class);
            FinalisedMerges.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(FinalisedMerges.this, MainActivity.class);
            FinalisedMerges.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(FinalisedMerges.this, Image.class);
            FinalisedMerges.this.startActivity(MergeIntent);
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
                Picasso.with(FinalisedMerges.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


