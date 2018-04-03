package com.example.gaffneca.fypfirebase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class JR_List extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button ViewMerges;
    private TextView tvMerge;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button[] buts;
    private TextView[] tvs;
    private TextView[] status;
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
        setContentView(R.layout.activity_jr__list);
        //ViewMerges = (Button) findViewById(R.id.button2) ;
       tvMerge = (TextView) findViewById(R.id.tvAnyJR);
        final LinearLayout mylinlay = (LinearLayout) findViewById(R.id.linlay);
        final String test;
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
                Intent LogoutIntent = new Intent(JR_List.this, MainActivity.class);
                JR_List.this.startActivity(LogoutIntent);
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



        final LinearLayout[] lins;



        int size = 100; // total number of TextViews to add

        buts = new Button[size];
        tvs = new TextView[size];
        status = new TextView[size];
        lins = new LinearLayout[size];
        Button temp;

        for (int i = 0; i < size; i++)
        {
            buts[i] = new Button(this);
            tvs[i] = new TextView(this);
            status[i] = new TextView(this);
            lins[i] = new LinearLayout(this);

            status[i].setPadding(20,0,0,0);
            tvs[i].setPadding(20,0,0,20);


            mylinlay.addView(lins[i]);
            //temp.setText("Alarm: " + i);
            mylinlay.addView(status[i]);
            mylinlay.addView(tvs[i]);
            tvs[i].setVisibility(View.GONE);
            status[i].setVisibility(View.GONE);
            mylinlay.addView(buts[i]);
            buts[i].setVisibility(View.GONE);


        }



        String user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("New Users").child(user_id).child("Journey Requests");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

             public void onDataChange(DataSnapshot dataSnapshot) {
                 if (dataSnapshot == null) {
                     Toast.makeText(JR_List.this, "Toasty ", Toast.LENGTH_SHORT).show();
                 } else {
                     Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                     for (DataSnapshot child : children) {
                         String JRID = child.getKey().toString();
                         final String jID = JRID;
                         DatabaseReference mchild = mDatabase.child(JRID);
                         //String test = dataSnapshot.getValue().toString();
                         final DatabaseReference startDB = mchild.child("Start");
                         final DatabaseReference enddb = mchild.child("End");
                         final DatabaseReference startGdb = mchild.child("StartGrid");
                         final DatabaseReference endGdb = mchild.child("EndGrid");

                         startGdb.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 final String startG = dataSnapshot.getValue().toString();

                                 endGdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         final String endG = dataSnapshot.getValue().toString();

                                         startDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                 String i = "hey";
                                                 final String start = dataSnapshot.getValue().toString();
                                                 enddb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                                         final String end = dataSnapshot.getValue().toString();
                                                         final DatabaseReference merges = FirebaseDatabase.getInstance().getReference().child("Journey Requests").child(startG).child(endG).child(jID);
                                                         merges.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                             @Override
                                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                                 int j = 0;
                                                                 while (tvs[j].getText().toString() != "" && j < tvs.length - 1) {
                                                                     j++;
                                                                 }
                                                                 final int i = j;
                                                                 Iterable<DataSnapshot> children2 = dataSnapshot.getChildren();
                                                                 for (DataSnapshot child2 : children2) {

                                                                     if (child2.getKey().toString().equals("Proposed Merges")) {
                                                                         DatabaseReference mFound = merges.child("Proposed Merges");

                                                                         mFound.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                             @Override
                                                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                 final ArrayList<String> pMerges = new ArrayList<String>();
                                                                                 Iterable<DataSnapshot> children3 = dataSnapshot.getChildren();
                                                                                 int count = 0;
                                                                                 for (DataSnapshot child3 : children3) {

                                                                                     count++;
                                                                                 }
                                                                                 tvMerge.setVisibility(View.GONE);
                                                                                 String merges = " Merges";
                                                                                 if (count == 1)
                                                                                     merges = " Merge";
                                                                                 buts[i].setText("View " + count + " Proposed"+ merges);
                                                                                 tvs[i].setTextColor(0xFF000000);
                                                                                 tvs[i].setText("From :   " + start + "\nTo      :   " + end );
                                                                                 tvs[i].setPadding(30, 20, 0, 0);
                                                                                 tvs[i].setVisibility(View.VISIBLE);
                                                                                 status[i].setText( count+ " Proposed" + merges);
                                                                                 status[i].setTextColor( 0xff32CD32);
                                                                                 status[i].setTextSize(14);
                                                                                 status[i].setPadding(10, 20, 0, 0);
                                                                                 status[i].setVisibility(View.VISIBLE);
                                                                                 buts[i].setVisibility(View.VISIBLE);
                                                                                 buts[i].setBackgroundColor(0xFF3BB9FF);
                                                                                 buts[i].setTextColor(0xFFFFFFFF);


                                                                                 LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(
                                                                                         new LinearLayout.LayoutParams(
                                                                                                 LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                                 LinearLayout.LayoutParams.WRAP_CONTENT));
                                                                                 linearParams2.setMargins(50, 0, 50, 0);

                                                                                 LinearLayout.LayoutParams linearParams3 = new LinearLayout.LayoutParams(
                                                                                         new LinearLayout.LayoutParams(
                                                                                                 LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                                 LinearLayout.LayoutParams.WRAP_CONTENT));
                                                                                 linearParams3.setMargins(40, 50, 0, 0);

                                                                                 lins[i].setLayoutParams(linearParams3);
                                                                                 lins[i].setBackgroundColor(0xff00ff00);

                                                                                 status[i].setLayoutParams(linearParams2);
                                                                                 tvs[i].setLayoutParams(linearParams2);
                                                                                 buts[i].setLayoutParams(linearParams2);

                                                                                 status[i].setBackgroundColor(0xffffffff);
                                                                                 tvs[i].setBackgroundColor(0xffffffff);







                                                                                 buts[i].setOnClickListener(new View.OnClickListener() {
                                                                                     @Override
                                                                                     public void onClick(View v) {
                                                                                         Intent MergeIntent = new Intent(JR_List.this, MergeList.class);
                                                                                         MergeIntent.putExtra("mergesFound", 1);
                                                                                         MergeIntent.putExtra("start", startG);
                                                                                         MergeIntent.putExtra("end", endG);
                                                                                         MergeIntent.putExtra("JRID", jID);
                                                                                         JR_List.this.startActivity(MergeIntent);
                                                                                     }
                                                                                 });

                                                                             }

                                                                             @Override
                                                                             public void onCancelled(DatabaseError databaseError) {

                                                                             }
                                                                         });
                                                                     }

                                                                 }


                                                                 tvMerge.setVisibility(View.GONE);
                                                                 buts[i].setText("View Journey Request");
                                                                 buts[i].setVisibility(View.VISIBLE);
                                                                 buts[i].setBackgroundColor(0xFF3BB9FF);
                                                                 buts[i].setTextColor(0xFFFFFFFF);
                                                                 tvs[i].setTextColor(0xFF000000);
                                                                 tvs[i].setText("From :   " + start + "\nTo      :   " + end );
                                                                 tvs[i].setPadding(30, 20, 0, 0);
                                                                 tvs[i].setTextSize(18);
                                                                 tvs[i].setVisibility(View.VISIBLE);
                                                                 status[i].setText("No Merges");
                                                                 status[i].setTextSize(14);
                                                                 status[i].setTextColor(0xFFFF0000);
                                                                 status[i].setPadding(10, 20, 0, 0);
                                                                 status[i].setVisibility(View.VISIBLE);

                                                                 LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(
                                                                         new LinearLayout.LayoutParams(
                                                                                 LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                 LinearLayout.LayoutParams.WRAP_CONTENT));
                                                                 linearParams2.setMargins(50, 0, 50, 0);

                                                                 LinearLayout.LayoutParams linearParams3 = new LinearLayout.LayoutParams(
                                                                         new LinearLayout.LayoutParams(
                                                                                 LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                 LinearLayout.LayoutParams.WRAP_CONTENT));
                                                                 linearParams3.setMargins(40, 50, 0, 0);

                                                                 lins[i].setLayoutParams(linearParams3);
                                                                 lins[i].setBackgroundColor(0xff00ff00);

                                                                 status[i].setLayoutParams(linearParams2);
                                                                 tvs[i].setLayoutParams(linearParams2);
                                                                 buts[i].setLayoutParams(linearParams2);

                                                                 status[i].setElevation(20);
                                                                 tvs[i].setElevation(20);
                                                                 buts[i].setElevation(20);

                                                                 status[i].setBackgroundColor(0xffffffff);
                                                                 tvs[i].setBackgroundColor(0xffffffff);


                                                                 buts[i].setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         Intent MergeIntent = new Intent(JR_List.this, MergeList.class);
                                                                         MergeIntent.putExtra("mergesFound", 0);
                                                                         MergeIntent.putExtra("JRID", jID);
                                                                         MergeIntent.putExtra("start", start);
                                                                         MergeIntent.putExtra("end", end);


                                                                         JR_List.this.startActivity(MergeIntent);
                                                                     }
                                                                 });


//
                                                             }


                                                             @Override
                                                             public void onCancelled(DatabaseError databaseError) {

                                                             }
                                                         });


                                                     }

                                                     @Override
                                                     public void onCancelled(DatabaseError databaseError) {

                                                     }

                                                 });
                                             }

                                             @Override
                                             public void onCancelled(DatabaseError databaseError) {

                                             }
                                         });


                                     }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 });
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


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(JR_List.this, MakeJourneyRequest.class);
            JR_List.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(JR_List.this, JR_List.class);
            JR_List.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(JR_List.this, FinalisedMerges.class);
            JR_List.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(JR_List.this, MainActivity.class);
            JR_List.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(JR_List.this, Image.class);
            JR_List.this.startActivity(MergeIntent);
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
                Picasso.with(JR_List.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
