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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MakeReview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView homeBut;
    private ImageView logoutBut;
    private TextView homeTxt;
    private TextView logoutTxt;
    private boolean logOpen;
    private TextView title;
    private ImageView open;
    private ImageView close;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_review);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mData =  FirebaseDatabase.getInstance().getReference().child("Reviews");




        logoutBut = findViewById(R.id.logout);
        logoutTxt = findViewById(R.id.logoutTxt);
        title = findViewById(R.id.title);
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
                Intent LogoutIntent = new Intent(MakeReview.this, MainActivity.class);
                MakeReview.this.startActivity(LogoutIntent);
            }
        });

        open = findViewById(R.id.ic_menu);
        close = findViewById(R.id.ic_back);
        mDrawerLayout = findViewById(R.id.drawerWin);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);


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



        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        final String id = intent.getStringExtra("id");

        final TextView tv = (TextView) findViewById(R.id.textView4);
       final EditText etRev = (EditText) findViewById(R.id.etReview);
        final Button sendRev = (Button) findViewById(R.id.btMakeRev);
        final RatingBar ratBar = (RatingBar) findViewById(R.id.ratingBar);

        title.setText("Reviewing: " + name);

        sendRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int stars = ((int) ratBar.getRating());
                String Review = etRev.getText().toString().trim();

                String revID = makeID();
                String userID = makeID();
                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference revData = mData.child(id);
                revData = revData.child(revID);
                revData.child("Rating").setValue(stars);
                revData.child("UserID").setValue(user_id);
                revData.child("Text").setValue(Review);


                //tv.setText("Your Rating is " + stars + ", your ID is " + random );

                Toast.makeText(MakeReview.this, "Review Sent", Toast.LENGTH_LONG).show();


                Intent JRIntent = new Intent(MakeReview.this, MapActivity2.class);
               MakeReview.this.startActivity(JRIntent);
            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(MakeReview.this, MakeJourneyRequest.class);
            MakeReview.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(MakeReview.this, JR_List.class);
            MakeReview.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(MakeReview.this, FinalisedMerges.class);
            MakeReview.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(MakeReview.this, MainActivity.class);
            MakeReview.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(MakeReview.this, Image.class);
            MakeReview.this.startActivity(MergeIntent);
        }
        return false;
    }
}
