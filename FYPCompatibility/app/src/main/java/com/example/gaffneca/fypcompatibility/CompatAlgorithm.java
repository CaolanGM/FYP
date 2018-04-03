package com.example.gaffneca.fypcompatibility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class CompatAlgorithm extends AppCompatActivity {

    private Button popBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compat_algorithm);

        popBut = findViewById(R.id.button2);

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("TriggeredAreas");
        Toast.makeText(CompatAlgorithm.this, "Got here 1", Toast.LENGTH_LONG).show();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(CompatAlgorithm.this, "Got here 2", Toast.LENGTH_LONG).show();

                if(dataSnapshot.getValue()!=null){
                   // Toast.makeText(CompatAlgorithm.this, "Got here 3", Toast.LENGTH_LONG).show();

                    DatabaseReference md = FirebaseDatabase.getInstance().getReference().child("MergingAreas");
                    ArrayList<String> areas = new ArrayList<>();
                    Iterable<DataSnapshot> children =dataSnapshot.getChildren();
                    for(DataSnapshot child:children){
                        String key = child.getKey();
                        areas.add(key);
                    }
                    mDatabase.removeValue();



                    Toast.makeText(CompatAlgorithm.this, "We found something", Toast.LENGTH_SHORT).show();
                    Intent mergeAreas = new Intent(CompatAlgorithm.this, MergeAreas.class);
                    mergeAreas.putExtra("areas", areas);
                    CompatAlgorithm.this.startActivity(mergeAreas);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        popBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference grid = FirebaseDatabase.getInstance().getReference().child("Grid");
                grid.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot2) {
                        //Toast.makeText(CompatAlgorithm.this, "23", Toast.LENGTH_SHORT).show();


                        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("New Users");
                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Toast.makeText(CompatAlgorithm.this, "54", Toast.LENGTH_SHORT).show();
                                Iterable<DataSnapshot> children =dataSnapshot.getChildren();
                                for(DataSnapshot child:children){
                                    String userID = child.getKey();
                                    int startGrid = makeGrid();
                                    int endGrid = makeGrid();

                                   DataSnapshot sGrid = dataSnapshot2.child(String.valueOf(startGrid));
                                   DataSnapshot eGrid = dataSnapshot2.child(String.valueOf(endGrid));
                                   double[] slatlon = centrePoint(sGrid);
                                   double[] elatlon = centrePoint(eGrid);

                                   double startLat = slatlon[0];
                                   double startLon = slatlon[1];
                                   double endLat = elatlon[0];
                                   double endLon = elatlon[1];

                                   String JRID = makeID();



                                   //ADD TO JOURNEY REQUEST TABLE
                                    DatabaseReference jrs =FirebaseDatabase.getInstance().getReference().child("Journey Requests");
                                    jrs = jrs.child(String.valueOf(startGrid)).child(String.valueOf(endGrid)).child(JRID);
                                    jrs.child("Bags").setValue("1");
                                    jrs.child("Cost").setValue("12.34");
                                    jrs.child("Date").setValue("4/4/2018");
                                    jrs.child("Duration").setValue("23");
                                    jrs.child("EndLat").setValue(endLat);
                                    jrs.child("EndLong").setValue(endLon);
                                    jrs.child("StartLat").setValue(startLat);
                                    jrs.child("StartLong").setValue(startLon);
                                    jrs.child("Time").setValue("12:34");
                                    jrs.child("User ID").setValue(userID);



                                    //ADD TO USER TABLE
                                    DatabaseReference id =FirebaseDatabase.getInstance().getReference().child("New Users");
                                    id = id.child(userID).child("Journey Requests").child(JRID);
                                    id.child("EndGrid").setValue(endGrid);
                                    id.child("StartGrid").setValue(startGrid);
                                    id.child("Start").setValue("Test Address");
                                    id.child("End").setValue("Test Address");


                                    //ADD TO TRIGGERED TABLE
                                    DatabaseReference trigs =FirebaseDatabase.getInstance().getReference().child("TriggeredAreas");
                                    trigs.child(String.valueOf(startGrid)).setValue(startGrid);





                                }
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



                Toast.makeText(CompatAlgorithm.this, "Populating Database", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private int makeGrid(){

        Random r = new Random();
        int Low = 0;
        int High = 100;
        int Result = r.nextInt(High-Low) + Low;
        return Result;
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
    private double[] centrePoint(DataSnapshot grid){

        double lat1 = (double) grid.child("Lat1").getValue();
        double lat2 = (double) grid.child("Lat2").getValue();
        double lon1 = (double) grid.child("Long1").getValue();
        double lon2 = (double) grid.child("Long2").getValue();

        double lat = (lat1+lat2)/2;
        double lon = (lon1+lon2)/2;

        double[] latlon = new double[2];
        latlon[0]=lat;
        latlon[1]=lon;

        return latlon;
    }

}
