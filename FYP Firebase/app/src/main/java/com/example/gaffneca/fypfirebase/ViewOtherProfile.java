package com.example.gaffneca.fypfirebase;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

public class ViewOtherProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Button mImage;
    private ImageView mImgView;
    private StorageReference mStore;
    private static final int GALLERY_INTENT = 2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mStars;
    private EditText rating;
    private EditText nameT;
    private RatingBar ratbar;
    private TextView testtv;
    private TextView[] tvs;
    private EditText[] ets;
    private View[] views;


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
        setContentView(R.layout.activity_view_other_profile);

        mStore = FirebaseStorage.getInstance().getReference();
        mImgView = (ImageView) findViewById(R.id.imageView);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("New Users");
        mStars = FirebaseDatabase.getInstance().getReference().child("Reviews");
        ratbar = (RatingBar) findViewById(R.id.ratBar);
        nameT = (EditText) findViewById(R.id.etName);
        rating = (EditText) findViewById(R.id.etRat);
        LinearLayout mylinlay = (LinearLayout) findViewById(R.id.revLinLay);
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
                Intent LogoutIntent = new Intent(ViewOtherProfile.this, MainActivity.class);
                ViewOtherProfile.this.startActivity(LogoutIntent);
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





        final int size = 100; // total number of TextViews to add
        tvs = new TextView[size];
        ets = new EditText[size];
        views = new View[size];
        for (int i = 0; i < size; i++)
        {
            ets[i] = new EditText(this);
            tvs[i] = new TextView(this);
            mylinlay.addView(tvs[i]);
            mylinlay.addView(ets[i]);
            tvs[i].setVisibility(View.GONE);
            ets[i].setVisibility(View.GONE);
            tvs[i].setPadding(10,30,0,30);
            ets[i].setFocusable(false);
            ets[i].setClickable(false);
            //tvs[i]. setTextSize(23);
            //tvs[i].setBackground(ContextCompat.getDrawable(Image.this, R.drawable.border_style));
        }


        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        final String mergeID = intent.getStringExtra("Merge");
        final String JRID = intent.getStringExtra("JR");
        final String name = intent.getStringExtra("Name");
        final int amount = intent.getIntExtra("Amount", 1);




        close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Toast.makeText(ViewOtherProfile.this, "Clickef", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                if(intent.getStringExtra("MergeMap")!=null){

                    final String start = intent.getStringExtra("Start");
                    final String end = intent.getStringExtra("End");


                    Intent profileIntent = new Intent(ViewOtherProfile.this, MergeMap.class);
                    profileIntent.putExtra("ID",id);
                    profileIntent.putExtra("MergeMap","merge");
                    profileIntent.putExtra("JR",JRID);
                    profileIntent.putExtra("Merge",mergeID);
                    profileIntent.putExtra("Name",name);
                    profileIntent.putExtra("Start",start);
                    profileIntent.putExtra("End",end);
                    profileIntent.putExtra("Amount",amount);

                    if(amount>2) {
                        final String name2 = intent.getStringExtra("Name2");
                        final String id2 = intent.getStringExtra("ID2");
                        profileIntent.putExtra("Name2",name2);
                        profileIntent.putExtra("ID2",id2);
                    }
                    if(amount>3) {
                        final String name3 = intent.getStringExtra("Name2");
                        final String id3 = intent.getStringExtra("ID2");
                        profileIntent.putExtra("Name3",name3);
                        profileIntent.putExtra("ID3",id3);
                    }
                    ViewOtherProfile.this.startActivity(profileIntent);

                }
                else{
                    Intent profileIntent = new Intent(ViewOtherProfile.this, FinalMap.class);
                    profileIntent.putExtra("ID",id);
                    profileIntent.putExtra("MergeMap","merge");
                    profileIntent.putExtra("JR",JRID);
                    profileIntent.putExtra("Merge",mergeID);
                    profileIntent.putExtra("Name",name);
                    profileIntent.putExtra("Amount",amount);
                    if(amount>2) {
                        final String name2 = intent.getStringExtra("Name2");
                        final String id2 = intent.getStringExtra("ID2");
                        profileIntent.putExtra("Name2",name2);
                        profileIntent.putExtra("ID2",id2);
                    }
                    if(amount>3) {
                        final String name3 = intent.getStringExtra("Name2");
                        final String id3 = intent.getStringExtra("ID2");
                        profileIntent.putExtra("Name3",name3);
                        profileIntent.putExtra("ID3",id3);
                    }

                    ViewOtherProfile.this.startActivity(profileIntent);


                }

            }
        });


        mAuth=FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference img = mDatabase.child(id).child("image");
        DatabaseReference stars = mStars.child(id);
        DatabaseReference cheatstars = mStars.child(id).child("abc").child("Rating");
        DatabaseReference nameDB = mDatabase.child(id).child("Name");

        nameDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nameT.setText(dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        stars.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                double count =0;
                int count2 = 0;
                for(DataSnapshot child: children){
                    double rat = child.child("Rating").getValue(double.class);
                    int i = (int) (rat/1);
                    String stars = String.valueOf(i) + " Stars";

                    if(i==1)
                        stars = String.valueOf(i) + " Star";



                    String review = child.child("Text").getValue().toString();


                    int colour;
                    if(i>=4){
                        colour = 0xff32CD32;
                    }
                    else if(i<=2){
                        colour = 0xffEC1C1C;
                    }
                    else{
                        colour = 0xffF6A34F;
                    }


                    if(count2<size)
                    {
                        tvs[count2].setAllCaps(false);

                        //tvs[count2].setText((int)rat+" Stars" + "\n"+review);
                        tvs[count2].setText(stars);
                        ets[count2].setText(review);
                        tvs[count2].setTextColor(colour);
                        tvs[count2].setVisibility(View.VISIBLE);
                        ets[count2].setVisibility(View.VISIBLE);

                    }
                    count+=rat;
                    count2++;
                }



                count/=count2;
                count = Double.parseDouble(String.format("%.1f", count));

                if(count2==0)
                    count=0;
                rating.setText(count + "/5");
                //ratbar.setMax(5);
                //ratbar.setStepSize(0.01f);
                //ratbar.invalidate();
                // ratbar.setRating(Float.parseFloat(dataSnapshot.getValue().toString()));
                ratbar.setRating(Float.parseFloat(String.valueOf(count)));
                ratbar.setIsIndicator(true);
                //  ratbar.setRating(4);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        img.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String photo = dataSnapshot.getValue().toString();
                Picasso.with(ViewOtherProfile.this).load(photo).fit().centerCrop().into(mImgView);
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
            Intent MergeIntent = new Intent(ViewOtherProfile.this, MakeJourneyRequest.class);
            ViewOtherProfile.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(ViewOtherProfile.this, JR_List.class);
            ViewOtherProfile.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(ViewOtherProfile.this, FinalisedMerges.class);
            ViewOtherProfile.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(ViewOtherProfile.this, MainActivity.class);
            ViewOtherProfile.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(ViewOtherProfile.this, Image.class);
            ViewOtherProfile.this.startActivity(MergeIntent);
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
                Picasso.with(ViewOtherProfile.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
