package com.example.gaffneca.fypfirebase;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

import static android.media.CamcorderProfile.get;

public class MakeJourneyRequest extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,NavigationView.OnNavigationItemSelectedListener{

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    private DatabaseReference mDatabase;
    private DatabaseReference newArea;
    private FirebaseAuth mAuth;
    private ProgressDialog mProg;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView StartPoint;
    private TextView EndPoint;
    private NumberPicker TimeSpent;
    private NumberPicker Cost;
    private Button bSend;
    private Button bCal;
    private Button bTime;
    private TextView tvDate;
    private TextView tvTime;
    private TextView pCostTV;
    private TextView pDurationTV;
    private Button route;
    private RadioGroup RadGroup;
    private RadioButton RadBut;
    private RadioGroup passRadG;
    private RadioButton passRadB;
    private int numBags;
    private int numPass;
    private Button bData;

    //mapData
    private String cost;
    private String pickUpAddress;
    private String dropOffAddress;
    private String duration;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private String gridRef;
    private boolean startFound;
    private boolean endFound;
    private String startG;
    private String endG;


    //enteredData
    private int myCost;
    private int myDuration;


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
        setContentView(R.layout.activity_make_journey_request);
        hideKeyboard();

        StartPoint = findViewById(R.id.etStart);
        EndPoint = findViewById(R.id.etEnd);
        TimeSpent = findViewById(R.id.etTime);
        Cost = findViewById(R.id.etCost);
        bSend = findViewById(R.id.bSend);
        bCal = findViewById(R.id.bdate);
        tvDate = findViewById(R.id.tvdate);
        mAuth = FirebaseAuth.getInstance();
        pCostTV = findViewById(R.id.proposedCost);
        pDurationTV = findViewById(R.id.proposedDuration);
        route =findViewById(R.id.routeBut);
        RadGroup = findViewById(R.id.bagSel);
        passRadG = findViewById(R.id.passSel);
        myCost=0;
        myDuration=0;



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
                Intent LogoutIntent = new Intent(MakeJourneyRequest.this, MainActivity.class);
                MakeJourneyRequest.this.startActivity(LogoutIntent);
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

        pCostTV.setText("Your Max Cost: €"+myCost+"\nUnshared Taxi Cost: (No route chosen)");
        pDurationTV.setText("Your Max Duration: "+myDuration+" mins"+"\nUnshared Taxi Duration:(No route chosen)");

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            if(extras.containsKey("date")){
                String dateInt = extras.getString("date");
                tvDate.setText(dateInt);
                dayFinal = extras.getInt("dayFinal");
                monthFinal = extras.getInt("monthFinal");
                yearFinal = extras.getInt("yearFinal");
                hourFinal = extras.getInt("hourFinal");
                minuteFinal = extras.getInt("minuteFinal");


            }

                cost = extras.getString("Cost");
                pickUpAddress = extras.getString("Start");
                dropOffAddress = extras.getString("End");
                duration = extras.getString("Duration");
                startLatitude = extras.getDouble("SLat");
                startLongitude = extras.getDouble("SLong");
                endLatitude = extras.getDouble("ELat");
                endLongitude = extras.getDouble("ELong");

                cost = cost.replace(" ","");
                duration = duration.replace(" mins","");
                pCostTV.setText("Your Max Cost: €"+myCost+"\nUnshared Taxi Cost: €" + cost);
                pDurationTV.setText("Your Max Duration: "+myDuration+" mins"+"\nUnshared Taxi Duration: " + duration+ " mins");
                StartPoint.setText("PickUp: "+pickUpAddress);
                EndPoint.setText("DropOff: "+dropOffAddress );
                StartPoint.setVisibility(View.VISIBLE);
                route.setText("Change Route");



        }

        hideKeyboard();




        TimeSpent.setMaxValue(120);
        TimeSpent.setMinValue(1);
        TimeSpent.setWrapSelectorWheel(true);
        Cost.setMaxValue(100);
        Cost.setMinValue(1);
        Cost.setWrapSelectorWheel(true);

        TimeSpent.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old, int newV) {
                hideKeyboard();
                myDuration = newV;
                pDurationTV.setText("Your Max Duration: "+myDuration +" mins"+"\nUnshared Taxi Duration: " + duration+" mins");

            }
        });
        TimeSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                //Toast.makeText(MakeJourneyRequest.this, "Cliked", Toast.LENGTH_SHORT).show();
            }
        });  Cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                //Toast.makeText(MakeJourneyRequest.this, "Click click", Toast.LENGTH_SHORT).show();
            }
        });

        Cost.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int old, int newV) {
                hideKeyboard();
                myCost = newV;
                pCostTV.setText("Your Max Cost: €"+myCost+"\nUnshared Taxi Cost: €" + cost);

            }
        });

        route.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent MergeIntent = new Intent(MakeJourneyRequest.this, MapActivity2.class);
                String dt = tvDate.getText().toString();
                MergeIntent.putExtra("date/time",dt);
                MergeIntent.putExtra("dayFinal", dayFinal);
                MergeIntent.putExtra("monthFinal", monthFinal);
                MergeIntent.putExtra("yearFinal", yearFinal);
                MergeIntent.putExtra("hourFinal", hourFinal);
                MergeIntent.putExtra("minuteFinal", minuteFinal);
                MakeJourneyRequest.this.startActivity(MergeIntent);
            }

        });

        bCal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(MakeJourneyRequest.this, MakeJourneyRequest.this,year,month,day);
                dpd.show();
            }

        });

        bSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SendRequest();
            }

        });



    }

    private void SendRequest(){

        final String start = pickUpAddress;
        final String end = dropOffAddress;
        //final String duration = TimeSpent.getText().toString().trim();
        //String cost = Cost.getText().toString().trim();

        if(TextUtils.isEmpty(start)||TextUtils.isEmpty(end)||myDuration==0||myCost==0){


            Toast.makeText(MakeJourneyRequest.this, "Fill in all Fields", Toast.LENGTH_SHORT).show();


        }
        else if(myCost>Double.parseDouble(cost)){
            Toast.makeText(MakeJourneyRequest.this, "Desired shared cost must be lower than Unshared Cost ", Toast.LENGTH_SHORT).show();

        }
        else if(myDuration<=Integer.parseInt(duration))
        {
            Toast.makeText(MakeJourneyRequest.this, "Desired shared duration must be higher than Unshared Duration ", Toast.LENGTH_SHORT).show();

        }
        else if(year==0){
            Toast.makeText(MakeJourneyRequest.this, "Enter Date/Time", Toast.LENGTH_SHORT).show();

        }
                else{




            mDatabase = FirebaseDatabase.getInstance().getReference().child("Journey Requests");

            final String user_id = mAuth.getCurrentUser().getUid();
            final String JR_id = makeID();
//                        setGrid(startLatitude,startLongitude, endLatitude,endLongitude, JR_id);
            final double slat = startLatitude;
            final double slon = startLongitude;
            final double elat = endLatitude;
            final double elon = endLongitude;

            //fillValues(getGridsS,getGridsE,mDatabase,JR_id,user_id);



            final DatabaseReference gridDB = FirebaseDatabase.getInstance().getReference().child("Grid");
            final boolean startFound = false;
            final boolean endFound = false;

            gridDB.addValueEventListener(new ValueEventListener() {

                public void onDataChange(final DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        final String grid = child.getKey().toString();
                        //Toast.makeText(MakeJourneyRequest.this, "Grid TEST: "+grid+" " +lat+ "" +lon, Toast.LENGTH_SHORT).show();




                        final DatabaseReference glat1 = gridDB.child(grid).child("Lat1");
                        final DatabaseReference glat2 =  gridDB.child(grid).child("Lat2");
                        final DatabaseReference glon1 =  gridDB.child(grid).child("Long1");
                        final DatabaseReference glon2 =  gridDB.child(grid).child("Long2");

                        glat1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot1) {
                                glat2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot2) {
                                        glon1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot3) {
                                                glon2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot4) {
                                                        double lat1=(double) dataSnapshot1.getValue();
                                                        double lat2=(double) dataSnapshot2.getValue();
                                                        double lon1=(double) dataSnapshot3.getValue();
                                                        double lon2=(double) dataSnapshot4.getValue();

                                                        DatabaseReference jrg = FirebaseDatabase.getInstance().getReference().child("JR GRid").child(JR_id);

                                                        if(!startFound) {
                                                            if ((lat1 <= slat && slat <= lat2) || (lat2 <= slat && slat <= lat1)) {
                                                                if ((lon1 <= slon && slon <= lon2) || (lon2 <= slon && slon <= lon1)) {
                                                                    int gridRef2 = Integer.parseInt(grid);
                                                                    Toast.makeText(MakeJourneyRequest.this, "GOT HERE START " + gridRef2, Toast.LENGTH_SHORT).show();
                                                                    jrg.child("Start").setValue(gridRef2);

                                                                }
                                                            }
                                                        }
                                                        if(!endFound) {
                                                            if ((lat1 <= elat && elat <= lat2) || (lat2 <= elat && elat <= lat1)) {
                                                                if ((lon1 <= elon && elon <= lon2) || (lon2 <= elon && elon <= lon1)) {
                                                                    int gridRef2 = Integer.parseInt(grid);
                                                                    Toast.makeText(MakeJourneyRequest.this, "GOT HERE END  " + gridRef2, Toast.LENGTH_SHORT).show();
                                                                    jrg.child("End").setValue(gridRef2);
                                                                }
                                                            }
                                                        }
                                                        if(endFound&&startFound)
                                                            return;
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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });



            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms

                    final DatabaseReference getGridsS = FirebaseDatabase.getInstance().getReference().child("JR GRid").child(JR_id).child("Start");
                    final DatabaseReference getGridsE = FirebaseDatabase.getInstance().getReference().child("JR GRid").child(JR_id).child("End");


                    getGridsS.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            Toast.makeText(MakeJourneyRequest.this, "SnapTEST "+dataSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();

                            startG = dataSnapshot.getValue().toString();
                            mDatabase = mDatabase.child(startG);

                            getGridsE.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    endG = dataSnapshot.getValue().toString();
                                    mDatabase = mDatabase.child(endG);

                                    int radID = RadGroup.getCheckedRadioButtonId();
                                    RadBut = findViewById(radID);
                                    numBags = Integer.parseInt(RadBut.getText().toString());

                                    int rad2ID = passRadG.getCheckedRadioButtonId();
                                    passRadB = findViewById(rad2ID);
                                    numPass = Integer.parseInt(passRadB.getText().toString());


                                    //PLACING REQUEST IN JOURNEY REQUEST TABLE
                                    mDatabase = mDatabase.child(JR_id);
                                    mDatabase.child("User ID").setValue(user_id);
                                    mDatabase.child("Cost").setValue(myCost);
                                    mDatabase.child("Duration").setValue(myDuration);
                                    mDatabase.child("Date").setValue( dayFinal + "/" + monthFinal + "/" + yearFinal);
                                    mDatabase.child("Time").setValue(hourFinal + ":" + minuteFinal);
                                    if(minuteFinal<10)
                                        mDatabase.child("Time").setValue(hourFinal + ":0" + minuteFinal);
                                    mDatabase.child("StartLat").setValue(startLatitude);
                                    mDatabase.child("StartLong").setValue(startLongitude);
                                    mDatabase.child("EndLat").setValue(endLatitude);
                                    mDatabase.child("EndLong").setValue(endLongitude);
                                    mDatabase.child("Bags").setValue(numBags);
                                    mDatabase.child("Passengers").setValue(numPass);
                                    mDatabase.child("UnsharedCost").setValue(cost);
                                    mDatabase.child("UnsharedDuration").setValue(duration);


                                    //PLACING REQUEST IN USER'S TABLE
                                    DatabaseReference userJr = FirebaseDatabase.getInstance().getReference().child("New Users");
                                    userJr = userJr.child(user_id).child("Journey Requests").child(JR_id);
                                    userJr.child("Start").setValue(start);
                                    userJr.child("End").setValue(end);
                                    userJr.child("StartGrid").setValue(startG);
                                    userJr.child("EndGrid").setValue(endG);

                                    //PLACING REQUEST INTO TRIGGERED AREA TABLE
                                    newArea = FirebaseDatabase.getInstance().getReference().child("TriggeredAreas");
                                    newArea.child(startG).setValue(startG);

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
            }, 1000);












            // populateDB();





            Intent MergeIntent = new Intent(MakeJourneyRequest.this, MapActivity2.class);
            startActivity(MergeIntent);
        }


        }
        public void checkButton(View v){
            int radID = RadGroup.getCheckedRadioButtonId();
            RadBut = findViewById(radID);
            numBags = Integer.parseInt(RadBut.getText().toString());

            int rad2ID = passRadG.getCheckedRadioButtonId();
            passRadB = findViewById(rad2ID);
            numPass = Integer.parseInt(passRadB.getText().toString());
        }



    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(MakeJourneyRequest.this,MakeJourneyRequest.this,
                hour,minute, true);


        TimePickerDialog tpd4 = new TimePickerDialog(MakeJourneyRequest.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
        MakeJourneyRequest.this, hour, minute, true);

        tpd4.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        hourFinal = i;
        minuteFinal = i1;

        tvDate.setText("Date: " + dayFinal + "/" + monthFinal + "/" + yearFinal
        + "\n Time: " + hourFinal + ":" + minuteFinal);

        if(minuteFinal<10)
            tvDate.setText("Date: " + dayFinal + "/" + monthFinal + "/" + yearFinal
                    + "\n Time: " + hourFinal + ":0" + minuteFinal);    }

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

    private void populateDB(){

        DatabaseReference fillGrid = FirebaseDatabase.getInstance().getReference().child("Grid");
        double blLat = 53.27;


        for(int j=0;j<100;j+=10){

            double blLong = -6.4;
            for(int i=0;i<10;i++){
                DatabaseReference newGrid= fillGrid.child(j+i+"");
                newGrid.child("Long1").setValue(blLong);
                newGrid.child("Long2").setValue(blLong+.032);
                newGrid.child("Lat1").setValue(blLat);
                newGrid.child("Lat2").setValue(blLat+.018);
                blLong+=.032;
            }
            blLat+=.018;
        }

    }

    public void setGrid(final double slat, final double slon, final double elat, final double elon, final String id){
        final DatabaseReference gridDB = FirebaseDatabase.getInstance().getReference().child("Grid");


        gridDB.addValueEventListener(new ValueEventListener() {

            public void onDataChange(final DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
               // boolean startFound;
                startFound= false;
                endFound = false;
                for (DataSnapshot child : children) {
                    final String grid = child.getKey().toString();
                    //Toast.makeText(MakeJourneyRequest.this, "Grid TEST: "+grid+" " +lat+ "" +lon, Toast.LENGTH_SHORT).show();




                    final DatabaseReference glat1 = gridDB.child(grid).child("Lat1");
                    final DatabaseReference glat2 =  gridDB.child(grid).child("Lat2");
                    final DatabaseReference glon1 =  gridDB.child(grid).child("Long1");
                    final DatabaseReference glon2 =  gridDB.child(grid).child("Long2");

                    glat1.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(final DataSnapshot dataSnapshot1) {
                            glat2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot2) {
                                    glon1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot3) {
                                            glon2.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot4) {
                                                    double lat1=(double) dataSnapshot1.getValue();
                                                    double lat2=(double) dataSnapshot2.getValue();
                                                    double lon1=(double) dataSnapshot3.getValue();
                                                    double lon2=(double) dataSnapshot4.getValue();

                                                    DatabaseReference jrg = FirebaseDatabase.getInstance().getReference().child("JR GRid").child(id);

                                                    if(!startFound) {
                                                        if ((lat1 <= slat && slat <= lat2) || (lat2 <= slat && slat <= lat1)) {
                                                            if ((lon1 <= slon && slon <= lon2) || (lon2 <= slon && slon <= lon1)) {
                                                                int gridRef2 = Integer.parseInt(grid);
                                                                startFound=true;
                                                                //Toast.makeText(MakeJourneyRequest.this, "GOT HERE START " + gridRef2, Toast.LENGTH_SHORT).show();
                                                                jrg.child("Start").setValue(gridRef2);

                                                            }
                                                        }
                                                    }
                                                    if(!endFound) {
                                                        if ((lat1 <= elat && elat <= lat2) || (lat2 <= elat && elat <= lat1)) {
                                                            if ((lon1 <= elon && elon <= lon2) || (lon2 <= elon && elon <= lon1)) {
                                                                int gridRef2 = Integer.parseInt(grid);
                                                                endFound=true;
                                                                //Toast.makeText(MakeJourneyRequest.this, "GOT HERE END  " + gridRef2, Toast.LENGTH_SHORT).show();
                                                                jrg.child("End").setValue(gridRef2);
                                                            }
                                                        }
                                                    }
                                                    if(endFound&&startFound)
                                                        //Toast.makeText(MakeJourneyRequest.this, "GOT HERE SOMEHOW  ", Toast.LENGTH_SHORT).show();

                                                    return;
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.makeJR){
            Intent MergeIntent = new Intent(MakeJourneyRequest.this, MakeJourneyRequest.class);
            MakeJourneyRequest.this.startActivity(MergeIntent);        }
        else if(id==R.id.mergeProp){
            Intent MergeIntent = new Intent(MakeJourneyRequest.this, JR_List.class);
            MakeJourneyRequest.this.startActivity(MergeIntent);        }
        else if (id == R.id.finalMerge) {
            Intent MergeIntent = new Intent(MakeJourneyRequest.this, FinalisedMerges.class);
            MakeJourneyRequest.this.startActivity(MergeIntent);

        }
        else if(id==R.id.logout){
            mAuth.signOut();
            Intent MergeIntent = new Intent(MakeJourneyRequest.this, MainActivity.class);
            MakeJourneyRequest.this.startActivity(MergeIntent);
        }
        else if(id==R.id.profile){
            Intent MergeIntent = new Intent(MakeJourneyRequest.this, Image.class);
            MakeJourneyRequest.this.startActivity(MergeIntent);
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
                Picasso.with(MakeJourneyRequest.this).load(photo).fit().centerCrop().into(sideImg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
