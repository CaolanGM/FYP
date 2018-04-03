package com.example.gaffneca.fypfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class Image extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button mImage;
    private ImageView mImgView;
    private StorageReference mStore;
    private static final int GALLERY_INTENT = 2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mStars;
    private EditText rating;
    private EditText name;
    private RatingBar ratbar;
    private TextView testtv;
    private EditText[] ets;
    private TextView[]tvs;
    private View[] views;
    private ImageView barImg;


    private ImageView homeBut;
    private ImageView logoutBut;
    private TextView homeTxt;
    private TextView logoutTxt;
    private boolean logOpen;
    private TextView title;
    private ImageView sideimg;

    private ImageView open;
    private ImageView close;
    private DrawerLayout mDrawerLayout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mStore = FirebaseStorage.getInstance().getReference();
        mImage = (Button) findViewById(R.id.bimg);
        mImgView = (ImageView) findViewById(R.id.imageView);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("New Users");
        mStars = FirebaseDatabase.getInstance().getReference().child("Reviews");
        ratbar = (RatingBar) findViewById(R.id.ratBar);
        name = (EditText) findViewById(R.id.etName);
        rating = (EditText) findViewById(R.id.etRat);
       LinearLayout mylinlay = (LinearLayout) findViewById(R.id.revLinLay);
        mAuth=FirebaseAuth.getInstance();

//        setContentView(R.layout.header);
//        barImg = findViewById(R.id.hamimg);
//        setContentView(R.layout.activity_image);


        final LayoutInflater factory = getLayoutInflater();
        final View textEntryView = factory.inflate(R.layout.header, null);
         sideimg = (ImageView) textEntryView.findViewById(R.id.hamimg);



         NavigationView naview = findViewById(R.id.navView);
        View header = naview.getHeaderView(0);
        final ImageView imview = (ImageView) header.findViewById(R.id.hamimg);

        logoutBut = findViewById(R.id.logout);
        logoutTxt = findViewById(R.id.logoutTxt);
        title = findViewById(R.id.title);
        logOpen = false;

        final ImageView sidebar = findViewById(R.id.navView).findViewById(R.id.hamimg);

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
                Intent LogoutIntent = new Intent(Image.this, MainActivity.class);
                Image.this.startActivity(LogoutIntent);
            }
        });




        String datetest = "23/2/2018";
        String datetest2 = "23/4/2018";
        String datetest3 = "29/3/2018";
        String timetest = "23:34";
        String timetest2 = "13:24";
        String timetest3 = "03:34";
        String timetest4 = "03:24";
        String timetest5 = "03:13";
        String timetest6 = "02:59";

       testTime(timetest2,timetest3);
       testTime(timetest4,timetest3);
       testTime(timetest5,timetest3);
       testTime(timetest5,timetest6);
       testTime(timetest6,timetest5);


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

                while(!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

                }
                mDrawerLayout.setElevation(0);


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



        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference img = mDatabase.child(user_id).child("image");
        DatabaseReference stars = mStars.child(user_id);
        DatabaseReference cheatstars = mStars.child(user_id).child("abc").child("Rating");
        DatabaseReference nameDB = mDatabase.child(user_id).child("Name");

        nameDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name.setText(dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        stars.addListenerForSingleValueEvent(new ValueEventListener() {
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

//                    SpannableString span1 = new SpannableString(stars);
//                    span1.setSpan(new RelativeSizeSpan(2f), 0, 6, 0);
//                    span1.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, 0);
//
//                    SpannableString span2 = new SpannableString(review);
//                    span2.setSpan(new RelativeSizeSpan(400), 0, review.length(), SPAN_INCLUSIVE_INCLUSIVE);


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
                Picasso.with(Image.this).load(photo).fit().centerCrop().into(mImgView);




                Picasso.with(Image.this).load(photo).fit().centerCrop().into(imview);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode ==RESULT_OK){

            Uri uri = data.getData();
            StorageReference filepath = mStore.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Image.this, "Updated", Toast.LENGTH_LONG).show();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    //Picasso.with(Image.this).load(downloadUri).fit().centerCrop().into(mImgView);

                    mAuth=FirebaseAuth.getInstance();
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference img = mDatabase.child(user_id);
                    String photo = downloadUri.toString();
                    img.child("image").setValue(photo);
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(Image.this, MakeJourneyRequest.class);
            Image.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(Image.this, JR_List.class);
            Image.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(Image.this, FinalisedMerges.class);
            Image.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(Image.this, MainActivity.class);
            Image.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(Image.this, Image.class);
            Image.this.startActivity(MergeIntent);
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
                Picasso.with(Image.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public boolean oldTime(String date, String time){

        int curhour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1;
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

        String cur = curday+"/"+curmonth+"/"+curYEAR + "   " + curhour+":"+curminute;
       // Log.e("Current Date", cur);

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
                    if(hour<curhour)
                    {
                        return true;
                    }
                    else if(hour==curhour){
                        if(minute<curminute)
                        {
                            return true;
                        }

                    }

                }

            }
        }

        return false;
    }

    public void testTime(String time2, String time){

        if(closeTimes(time2,time)){
            Log.e("CLOSE", "Time: "+time2 + "Time: "+ time);
        }
        else{
            Log.e("FAR", "Time: "+time2 + "Time: "+ time);

        }

    }

    private boolean closeTimes(String time, String time2){

        final String[] times = time.split(Pattern.quote(":"));
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);

        final String[] times2 = time2.split(Pattern.quote(":"));
        int hour2 = Integer.parseInt(times2[0]);
        int minute2 = Integer.parseInt(times2[1]);

        if(hour==hour2){
            if(minute>=minute2){
                if((minute-minute2)<15)
                    return true;
            }
            else{
                if((minute2-minute)<15)
                    return true;
            }
        }
        else if(hour==hour2-1)
        {
            if((minute-minute2)>=45)
                return true;
            else{
                Log.e("Difference", minute2-minute+"");
            }
        }
        else if(hour==hour2+1){
            if((minute2-minute)>=45)
                return true;
        }


        return false;
    }


}
