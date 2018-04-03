package com.example.gaffneca.fypfirebase;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private TextView tvWelcomeMsg;
    private EditText etUsername;
    private EditText etAge;
    private Button bMakeJR;
    private Button bViewMerges;
    private Button bfinMerge;
    private Button bImg;
    private Button blogout;
    private Button bMap;
    private Button greenRev;
    private TextView revUser;
    private TextView cancel;
    private RelativeLayout popUp;
    private RelativeLayout tint ;
    private RelativeLayout main ;
    private ImageView mImgView;

    private RelativeLayout reLay;
    private ImageView open;
    private ImageView close;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

     NotificationCompat.Builder notification;
     private static final int notifID = 232324;
    public static final String CHANEL_ID = "my_ch_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth=FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("New Users");
        mDatabase = mDatabase.child(user_id);
        DatabaseReference namedb = mDatabase.child("Name");
        DatabaseReference age = mDatabase.child("Age");


        notification = new NotificationCompat.Builder(this, CHANEL_ID);
        notification.setAutoCancel(true);

       tvWelcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);
         etUsername = (EditText) findViewById(R.id.etUsername);
        etAge = (EditText) findViewById(R.id.etAge);
        bMakeJR = (Button) findViewById(R.id.btMakeJR);
        bViewMerges = (Button) findViewById(R.id.btViewMerges);
         bfinMerge = (Button) findViewById(R.id.btFinalisedMerges);
         bImg = (Button) findViewById(R.id.btImg);
        blogout = (Button) findViewById(R.id.btLogout);
        bMap = (Button) findViewById(R.id.btMap);
        greenRev = (Button) findViewById(R.id.greenRev);
        revUser = (TextView) findViewById(R.id.revName);
        cancel = (TextView) findViewById(R.id.cancel);
         popUp = (RelativeLayout) findViewById(R.id.popUp);
        tint = (RelativeLayout) findViewById(R.id.backTint);
        main = (RelativeLayout) findViewById(R.id.main);
        mImgView = (ImageView) findViewById(R.id.img);
        reLay = findViewById(R.id.barTool);




        popUp.setVisibility(View.GONE);
        tint.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();


        //mAuth.signOut();
        //Intent logoutIntent = new Intent(HomeScreen.this, MainActivity.class);
        //HomeScreen.this.startActivity(logoutIntent);



        open = findViewById(R.id.ic_menu);
        close = findViewById(R.id.ic_back);
       mDrawerLayout = findViewById(R.id.drawerWin);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        setSupportActionBar(reLay);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);


        open.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.START);
                mDrawerLayout.setElevation(30);
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






        namedb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.getValue().toString();
                String message = name + " welcome to your user area";
                tvWelcomeMsg.setText(message);
                etUsername.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        age.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String age = dataSnapshot.getValue().toString();
                etAge.setText(age + "");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Display user details


        final DatabaseReference reviewsTab = mDatabase.child("Pending Reviews");
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
                                Picasso.with(HomeScreen.this).load(photo).fit().centerCrop().into(mImgView);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        greenRev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent revIntent = new Intent(HomeScreen.this, MakeReview.class);
                                revIntent.putExtra("name", name );
                                revIntent.putExtra("id", id );
                                HomeScreen.this.startActivity(revIntent);
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        final DatabaseReference finalised = FirebaseDatabase.getInstance().getReference().child("New Users").child(user_id).child("Finalised Merges");
        finalised.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    int i =0;
                    Iterable<DataSnapshot> merges = dataSnapshot.getChildren();
                    for(DataSnapshot merge:merges){
                        i++;
                    }

                    bfinMerge.setText("Finalised Merges" + " ("+i+")");
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



        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logoutIntent = new Intent(HomeScreen.this, MainActivity.class);
                HomeScreen.this.startActivity(logoutIntent);
            }
        });

        bMakeJR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent JRIntent = new Intent(HomeScreen.this, MakeJourneyRequest.class);
                HomeScreen.this.startActivity(JRIntent);
            }
        });

        bViewMerges.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
              Intent MergeIntent = new Intent(HomeScreen.this, JR_List.class);
               HomeScreen.this.startActivity(MergeIntent);

            }

        });
        bfinMerge.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
               Intent finMergeIntent = new Intent(HomeScreen.this, FinalisedMerges.class);
               HomeScreen.this.startActivity(finMergeIntent);


            }

        });
        bImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent MergeIntent = new Intent(HomeScreen.this, Image.class);
                HomeScreen.this.startActivity(MergeIntent);
            }

        });
        bMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DatabaseReference triggered =  FirebaseDatabase.getInstance().getReference().child("TriggeredAreas").child("23");
                triggered.setValue(23);
                Intent MergeIntent = new Intent(HomeScreen.this, MapActivity2.class);
                MergeIntent.putExtra("Plain Map", "Plain");
                HomeScreen.this.startActivity(MergeIntent);
            }

        });


    }
    public void offClick(){

        bImg.setClickable(false);
        blogout.setClickable(false);
        bMakeJR.setClickable(false);
        bfinMerge.setClickable(false);
        bMap.setClickable(false);

    }
    public void clickOn(){

        bImg.setClickable(true);
        blogout.setClickable(true);
        bMakeJR.setClickable(true);
        bfinMerge.setClickable(true);
        bMap.setClickable(true);

    }

    public void sendNotif(){

//            notification.setSmallIcon(R.mipmap.ic_launcher);
//            notification.setTicker("Ticker time");
//            notification.setWhen(System.currentTimeMillis());
//            notification.setContentTitle("New Finalised Merge");
//            notification.setContentText("All passengers have accepted your merge proposal");

            Intent intent = new Intent(this,MergeList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
      //  notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       // nm.notify(notifID,notification.build());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
           // notificationChannel.setDescription("Channel description");
           // notificationChannel.enableLights(true);
          //  notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
           // notificationChannel.enableVibration(true);
            nm.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notif = new NotificationCompat.Builder(this, CHANEL_ID)
               // .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Finalised Merge")
                .setContentText("All passengers have accepted your merge proposal")
                .setContentIntent(pendingIntent);

        nm.notify(notifID,notif.build());

    }

    public void moveData(){

        DatabaseReference caolan = FirebaseDatabase.getInstance().getReference().child("New Users").child("R7GvKIskGnWyBP91BO52BZgk4Sj1");
        caolan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference newC = FirebaseDatabase.getInstance().getReference().child("CAOLAN");
                newC.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(HomeScreen.this, MakeJourneyRequest.class);
            HomeScreen.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(HomeScreen.this, JR_List.class);
            HomeScreen.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(HomeScreen.this, FinalisedMerges.class);
            HomeScreen.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(HomeScreen.this, MainActivity.class);
            HomeScreen.this.startActivity(MergeIntent);
        }


        return false;
    }
}
