package com.example.gaffneca.fypcompatibility;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gaffneca.fypcompatibility.route.GetDirectionsData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MergeAreas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_areas);
        Toast.makeText(MergeAreas.this, "Got here 645645645: " , Toast.LENGTH_LONG).show();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);




//
//        Object dataTransfer1[];
//        dataTransfer1 = new Object[1];
//        String url1 = getDirectionsUrl2(53.310622070608424, -6.280633471906184, 53.32474687276647, -6.324204616248608);
//        GetDirectionsData getDirectionsData1 = new GetDirectionsData();
//        dataTransfer1[0] = url1;
//        getDirectionsData1.execute(dataTransfer1);
//        String[] infoT = getDirectionsData1.getInfo(url1);
//        Log.e("TESTE", infoT[0]);








        Intent intent = getIntent();
        final ArrayList<String> areas = intent.getStringArrayListExtra("areas");

        Toast.makeText(MergeAreas.this, "Got here 44444Sfr5: " , Toast.LENGTH_LONG).show();


            final DatabaseReference jrs = FirebaseDatabase.getInstance().getReference().child("Journey Requests");
            ValueEventListener stop;
            jrs.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot3) {

                    Toast.makeText(MergeAreas.this, "Got here wefr5: " , Toast.LENGTH_LONG).show();

                    for (int i = 0; i < areas.size(); i++) {

                        final String key = areas.get(i);

                        DataSnapshot dataSnapshot = dataSnapshot3.child(key);
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            final String end = child.getKey().toString();


                            Toast.makeText(MergeAreas.this, "Got here 6: " + end, Toast.LENGTH_LONG).show();

                            final DataSnapshot dataSnapshot2 = dataSnapshot.child(end);
                            ArrayList<DataSnapshot> JRS = new ArrayList<>();
                            final Iterable<DataSnapshot> JRs = dataSnapshot2.getChildren();

                            //GOING THROUGH ALL THE JOURNEY REQUESTS
                            for (DataSnapshot jr : JRs) {
                                JRS.add(jr);
                            }

                            int length;
                            if(JRS.size()>3)
                                length=3;
                            else if(JRS.size()>2)
                                length=2;
                            else
                                length=1;

                            for(int x =0; x<JRS.size()-1;x++ ){
                                Toast.makeText(MergeAreas.this, "Got here 7", Toast.LENGTH_LONG).show();

                                final DataSnapshot jr = JRS.get(x);

                                String JRID = jr.getKey().toString();
                                final DatabaseReference jr1 = jrs.child(key).child(end).child(JRID);
                                final DatabaseReference existingMerges = jr1.child("Proposed Merges");



                                int baggage1 = Integer.parseInt(dataSnapshot2.child(JRID).child("Bags").getValue().toString());
                                int passengers1 = Integer.parseInt(dataSnapshot2.child(JRID).child("Passengers").getValue().toString());
                                String user1ID = dataSnapshot2.child(JRID).child("User ID").getValue().toString();
                                double startLat = (double) dataSnapshot2.child(JRID).child("StartLat").getValue();
                                double startLon = (double) dataSnapshot2.child(JRID).child("StartLong").getValue();
                                double endLat = (double) dataSnapshot2.child(JRID).child("EndLat").getValue();
                                double endLon = (double) dataSnapshot2.child(JRID).child("EndLong").getValue();
                                String unsharedCost1 = dataSnapshot2.child(JRID).child("UnsharedCost").getValue().toString();
                                String cost1 = dataSnapshot2.child(JRID).child("Cost").getValue().toString();
                                String date1 = dataSnapshot2.child(JRID).child("Date").getValue().toString();
                                String time1 = dataSnapshot2.child(JRID).child("Time").getValue().toString();
                                String unsharedDur1 = dataSnapshot2.child(JRID).child("UnsharedDuration").getValue().toString();
                                String Duration1 = dataSnapshot2.child(JRID).child("Duration").getValue().toString();





                                int y = x+1;
                                while(y<JRS.size()) {

                                    ArrayList<String> inputStr = new ArrayList<String>();
                                    List<Double> inputD = new ArrayList<Double>();

                                    DataSnapshot ojr = JRS.get(y);
                                    final String JRID2 = ojr.getKey().toString();
                                    final DatabaseReference jr2 = jrs.child(key).child(end).child(JRID2);
                                    final DatabaseReference existingMerges2 = jr2.child("Proposed Merges");



                                    //ENSURING THAT YOU'RE NOT PAIRED WITH YOURSELF
                                    if ((!(JRID.equals(JRID2)))) {


                                        int baggage2 = Integer.parseInt(dataSnapshot2.child(JRID2).child("Bags").getValue().toString());
                                        int passengers2 = Integer.parseInt(dataSnapshot2.child(JRID2).child("Passengers").getValue().toString());
                                        String user2ID = dataSnapshot2.child(JRID2).child("User ID").getValue().toString();
                                        double startLat2 = (double) dataSnapshot2.child(JRID2).child("StartLat").getValue();
                                        double startLon2 = (double) dataSnapshot2.child(JRID2).child("StartLong").getValue();
                                        double endLat2 = (double) dataSnapshot2.child(JRID2).child("EndLat").getValue();
                                        double endLon2 = (double) dataSnapshot2.child(JRID2).child("EndLong").getValue();
                                        String unsharedCost2 = dataSnapshot2.child(JRID2).child("UnsharedCost").getValue().toString();
                                        String cost2 = dataSnapshot2.child(JRID2).child("Cost").getValue().toString();
                                        String date2 = dataSnapshot2.child(JRID2).child("Date").getValue().toString();
                                        String time2 = dataSnapshot2.child(JRID2).child("Time").getValue().toString();
                                        String unsharedDur2 = dataSnapshot2.child(JRID2).child("UnsharedDuration").getValue().toString();
                                        String Duration2 = dataSnapshot2.child(JRID2).child("Duration").getValue().toString();

                                        int passengerTot = passengers1 + passengers2;
                                        int baggageTot = baggage1 + baggage2;

                                        if (passengerTot<5&&baggageTot<5&& date1.equals(date2)&&closeTimes(time1,time2)){

                                        if (passengerTot < 4) {

                                            int z = y + 1;
                                            while (z < JRS.size()) {
                                                DataSnapshot jr3 = JRS.get(z);

                                                ArrayList<String> inputStr2 = new ArrayList<String>();
                                                List<Double> inputD2 = new ArrayList<Double>();

                                                final String JRID3 = jr3.getKey().toString();
                                                final DatabaseReference jR3 = jrs.child(key).child(end).child(JRID3);
                                                final DatabaseReference existingMerges3 = jR3.child("Proposed Merges");


                                                if (!(JRID.equals(JRID3)) && !(JRID2.equals(JRID3))) {


                                                    int baggage3 = Integer.parseInt(dataSnapshot2.child(JRID3).child("Bags").getValue().toString());
                                                    int passengers3 = Integer.parseInt(dataSnapshot2.child(JRID3).child("Passengers").getValue().toString());
                                                    String user3ID = dataSnapshot2.child(JRID3).child("User ID").getValue().toString();
                                                    double startLat3 = (double) dataSnapshot2.child(JRID3).child("StartLat").getValue();
                                                    double startLon3 = (double) dataSnapshot2.child(JRID3).child("StartLong").getValue();
                                                    double endLat3 = (double) dataSnapshot2.child(JRID3).child("EndLat").getValue();
                                                    double endLon3 = (double) dataSnapshot2.child(JRID3).child("EndLong").getValue();
                                                    String unsharedCost3 = dataSnapshot2.child(JRID3).child("UnsharedCost").getValue().toString();
                                                    String cost3 = dataSnapshot2.child(JRID3).child("Cost").getValue().toString();
                                                    String date3 = dataSnapshot2.child(JRID3).child("Date").getValue().toString();
                                                    String time3 = dataSnapshot2.child(JRID3).child("Time").getValue().toString();
                                                    String unsharedDur3 = dataSnapshot2.child(JRID3).child("UnsharedDuration").getValue().toString();
                                                    String Duration3 = dataSnapshot2.child(JRID3).child("Duration").getValue().toString();
                                                    int passengerTot2 = passengerTot + passengers3;
                                                    int baggageTot2 = baggageTot + baggage3;


                                                    if(passengerTot2<5&&baggageTot2<5&& date1.equals(date3)&&closeTimes(time1,time3)&&closeTimes(time2,time3)){

                                                    if (passengerTot2 < 4) {
                                                        int a = z + 1;
                                                        Log.e("ADD 4: ", "GOING FOR 4");
                                                        while (a < JRS.size()) {
                                                            DataSnapshot jr4 = JRS.get(a);

                                                            ArrayList<String> inputStr3 = new ArrayList<String>();
                                                            List<Double> inputD3 = new ArrayList<Double>();

                                                            final String JRID4 = jr4.getKey().toString();
                                                            final DatabaseReference jR4 = jrs.child(key).child(end).child(JRID4);
                                                            final DatabaseReference existingMerges4 = jR4.child("Proposed Merges");

                                                            if (!(JRID.equals(JRID4)) && !(JRID2.equals(JRID4)) && !(JRID3.equals(JRID4))) {

                                                                int baggage4 = Integer.parseInt(dataSnapshot2.child(JRID4).child("Bags").getValue().toString());
                                                                int passengers4 = Integer.parseInt(dataSnapshot2.child(JRID4).child("Passengers").getValue().toString());
                                                                String user4ID = dataSnapshot2.child(JRID4).child("User ID").getValue().toString();
                                                                double startLat4 = (double) dataSnapshot2.child(JRID4).child("StartLat").getValue();
                                                                double startLon4 = (double) dataSnapshot2.child(JRID4).child("StartLong").getValue();
                                                                double endLat4 = (double) dataSnapshot2.child(JRID4).child("EndLat").getValue();
                                                                double endLon4 = (double) dataSnapshot2.child(JRID4).child("EndLong").getValue();
                                                                String unsharedCost4 = dataSnapshot2.child(JRID4).child("UnsharedCost").getValue().toString();
                                                                String cost4 = dataSnapshot2.child(JRID4).child("Cost").getValue().toString();
                                                                String date4 = dataSnapshot2.child(JRID4).child("Date").getValue().toString();
                                                                String time4 = dataSnapshot2.child(JRID4).child("Time").getValue().toString();
                                                                String unsharedDur4 = dataSnapshot2.child(JRID4).child("UnsharedDuration").getValue().toString();
                                                                String Duration4 = dataSnapshot2.child(JRID4).child("Duration").getValue().toString();


                                                                if ((passengerTot2 + passengers4) < 5 && (baggageTot2 + baggage4) < 5) {

                                                                    if (date4.equals(date1)&&closeTimes(time1,time4)&&closeTimes(time2,time4)&&closeTimes(time3,time4)) {

                                                                        inputStr3.add(user1ID);
                                                                        inputStr3.add(JRID);
                                                                        inputStr3.add(unsharedCost1);
                                                                        inputStr3.add(cost1);
                                                                        inputStr3.add(unsharedDur1);
                                                                        inputStr3.add(Duration1);

                                                                        inputStr3.add(user2ID);
                                                                        inputStr3.add(JRID2);
                                                                        inputStr3.add(unsharedCost2);
                                                                        inputStr3.add(cost2);
                                                                        inputStr3.add(unsharedDur2);
                                                                        inputStr3.add(Duration2);

                                                                        inputStr3.add(user3ID);
                                                                        inputStr3.add(JRID3);
                                                                        inputStr3.add(unsharedCost3);
                                                                        inputStr3.add(cost3);
                                                                        inputStr3.add(unsharedDur3);
                                                                        inputStr3.add(Duration3);

                                                                        inputStr3.add(user4ID);
                                                                        inputStr3.add(JRID4);
                                                                        inputStr3.add(unsharedCost4);
                                                                        inputStr3.add(cost4);
                                                                        inputStr3.add(unsharedDur4);
                                                                        inputStr3.add(Duration4);

                                                                        inputStr3.add(time1);
                                                                        inputStr3.add(date1);


                                                                        inputD3.add(startLat);
                                                                        inputD3.add(startLon);
                                                                        inputD3.add(endLat);
                                                                        inputD3.add(endLon);
                                                                        inputD3.add(startLat2);
                                                                        inputD3.add(startLon2);
                                                                        inputD3.add(endLat2);
                                                                        inputD3.add(endLon2);
                                                                        inputD3.add(startLat3);
                                                                        inputD3.add(startLon3);
                                                                        inputD3.add(endLat3);
                                                                        inputD3.add(endLon3);
                                                                        inputD3.add(startLat4);
                                                                        inputD3.add(startLon4);
                                                                        inputD3.add(endLat4);
                                                                        inputD3.add(endLon4);

                                                                        Log.e("ADD 4: ", "GOING TO FUNCTION");

                                                                        addMerges(4, existingMerges, existingMerges2,existingMerges3,existingMerges4,inputStr3,inputD3);
                                                                        final Handler handler = new Handler();
                                                                        handler.postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                deleteDuplicates(existingMerges);
                                                                                deleteDuplicates(existingMerges2);
                                                                                deleteDuplicates(existingMerges3);
                                                                                deleteDuplicates(existingMerges4);
                                                                            }
                                                                        }, 1000);

                                                                    }
                                                                    else
                                                                        Log.e("Failed 4 date: ", "here");

                                                                }
                                                                else
                                                                    Log.e("Failed 4 passbags: ", "here");



                                                            }
                                                            else
                                                                Log.e("Failed 4 equals: ", "JRID1 "+JRID+" JRID2 "+JRID2+" JRID3 "+JRID3+" JRID4: "+JRID4);

                                                            a++;

                                                        }

                                                    }


                                                    //add3
                                                        inputStr2.add(user1ID);
                                                        inputStr2.add(JRID);
                                                        inputStr2.add(unsharedCost1);
                                                        inputStr2.add(cost1);
                                                        inputStr2.add(unsharedDur1);
                                                        inputStr2.add(Duration1);

                                                        inputStr2.add(user2ID);
                                                        inputStr2.add(JRID2);
                                                        inputStr2.add(unsharedCost2);
                                                        inputStr2.add(cost2);
                                                        inputStr2.add(unsharedDur2);
                                                        inputStr2.add(Duration2);

                                                        inputStr2.add(user3ID);
                                                        inputStr2.add(JRID3);
                                                        inputStr2.add(unsharedCost3);
                                                        inputStr2.add(cost3);
                                                        inputStr2.add(unsharedDur3);
                                                        inputStr2.add(Duration3);

                                                        inputStr2.add(time1);
                                                        inputStr2.add(date1);


                                                        inputD2.add(startLat);
                                                        inputD2.add(startLon);
                                                        inputD2.add(endLat);
                                                        inputD2.add(endLon);
                                                        inputD2.add(startLat2);
                                                        inputD2.add(startLon2);
                                                        inputD2.add(endLat2);
                                                        inputD2.add(endLon2);
                                                        inputD2.add(startLat3);
                                                        inputD2.add(startLon3);
                                                        inputD2.add(endLat3);
                                                        inputD2.add(endLon3);

                                                        Log.e("ADD 3: ", "GOING TO FUNCTION");

                                                        addMerges(3, existingMerges, existingMerges2,existingMerges3,null,inputStr2,inputD2);
                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                deleteDuplicates(existingMerges);
                                                                deleteDuplicates(existingMerges2);
                                                                deleteDuplicates(existingMerges3);
                                                            }
                                                        }, 1000);
                                                    }
                                                }

                                                z++;
                                            }



                                        }


                                        //Toast.makeText(MergeAreas.this, "JR: " + JRID + "   USer:  " + user1ID, Toast.LENGTH_LONG).show();
                                        // Toast.makeText(MergeAreas.this, "JR2: " + JRID2 + "   USer2:  " + user2ID, Toast.LENGTH_LONG).show();


                                            inputStr.add(user1ID);
                                            inputStr.add(JRID);
                                            inputStr.add(unsharedCost1);
                                            inputStr.add(cost1);
                                            inputStr.add(unsharedDur1);
                                            inputStr.add(Duration1);

                                            inputStr.add(user2ID);
                                            inputStr.add(JRID2);
                                            inputStr.add(unsharedCost2);
                                            inputStr.add(cost2);
                                            inputStr.add(unsharedDur2);
                                            inputStr.add(Duration2);

                                            inputStr.add(time1);
                                            inputStr.add(date1);


                                            inputD.add(startLat);
                                            inputD.add(startLon);
                                            inputD.add(endLat);
                                            inputD.add(endLon);
                                            inputD.add(startLat2);
                                            inputD.add(startLon2);
                                            inputD.add(endLat2);
                                            inputD.add(endLon2);

                                            Toast.makeText(MergeAreas.this, "Testing INDEX" + inputD.indexOf(startLat2), Toast.LENGTH_SHORT).show();

                                            Log.e("ADD 2: ", "GOING TO FUNCTION");


                                            addMerges(2, existingMerges, existingMerges2,null,null,inputStr,inputD);
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    deleteDuplicates(existingMerges);
                                                    deleteDuplicates(existingMerges2);
                                                }
                                            }, 1000);
                                        }else {
                                          //  Log.d("NO MERGE", " Date1: "+date1+" Date2: "+date2 + " Bag Tot: "+ baggageTot + " Passenger: "+passengerTot + " JRID: "+JRID + " JRID2: "+JRID2);
                                        }

                                    }

                                      y++;
                                    }

                                }

                            }


                        }

                    }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        //jrs.removeEventListener(stop);


        Intent mergeAreas = new Intent(MergeAreas.this, CompatAlgorithm.class);
        mergeAreas.putExtra("areas", areas);
        MergeAreas.this.startActivity(mergeAreas);
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


    private void addMerges(final int amount, final DatabaseReference existingMerges, final DatabaseReference existingMerges2,final DatabaseReference existingMerges3, final DatabaseReference existingMerges4,
                           final ArrayList<String> info, final List<Double> coords){

        //ENSURING THAT YOU'RE NOT PAIRED WITH A DIFFERENT JOURNEY REQUEST YOU HAVE MADE
        String user3ID="x";
        String JRID3="";
        String user4ID="y";
        String JRID4="";

        final String user1ID =info.get(0);
        final String JRID = info.get(1);
        final String user2ID =info.get(6);
        final String JRID2 = info.get(7);
        if(amount>2) {
             user3ID = info.get(4);
            JRID3 = info.get(5);

            if (amount > 3) {
                user4ID = info.get(6);
                 JRID4 = info.get(7);
            }
        }

         String withCheck = JRID2;
        if(amount>2){
            withCheck = JRID2+","+JRID3;
            if(amount>3){
                withCheck = JRID2+","+JRID3+","+JRID4;
            }
        }

        final String with = withCheck;

        boolean check1 =(!user1ID.equals(user2ID));
        boolean check2 = (!user1ID.equals(user3ID)&&!user2ID.equals(user3ID));
        boolean check3 = (!user1ID.equals(user4ID)&&!user2ID.equals(user4ID)&&!user3ID.equals(user4ID));

        if(check1&&check2&&check3){

            existingMerges.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue()!=null) {

                        final Iterable<DataSnapshot> merges = dataSnapshot.getChildren();
                        boolean notThere = true;
                        for (DataSnapshot merge : merges) {
                            String dbwith = merge.child("With").getValue().toString();

                            if (with.equals(dbwith)) {
                                notThere=false;
                            }
                        }
                        if(notThere){
                                Toast.makeText(MergeAreas.this, "Got here 11", Toast.LENGTH_LONG).show();
                                //Log.d("Second Merge", " JRID: "+JRID + " JRID2: "+JRID2 + " New With "+with+" Old With "+dbwith);

                            if(amount==2)
                            {
                                Log.e("ADDING 2","f");
                                add2(existingMerges,existingMerges2,info,coords);
                            }
                            else if(amount==3)
                            {
                                Log.e("ADDING 3","f");
                                add3(existingMerges,existingMerges2,existingMerges3,info,coords);
                            }
                            else
                            {
                                Log.e("ADDING 4","f");

                                add4(existingMerges,existingMerges2,existingMerges3,existingMerges4,info,coords);
                            }



                        }
                    }
                    else
                    {
                        Log.d("First Merge", " JRID: "+JRID + " JRID2: "+JRID2);

                        if(amount==2)
                        {
                            Log.e("ADDING 2","f");
                            add2(existingMerges,existingMerges2,info,coords);
                        }
                        else if(amount==3)
                        {
                            Log.e("ADDING 3","f");

                            add3(existingMerges,existingMerges2,existingMerges3,info,coords);
                        }
                        else
                        {
                            Log.e("ADDING 4","f");
                            add4(existingMerges,existingMerges2,existingMerges3,existingMerges4,info,coords);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



//





        }


    }

    public void deleteDuplicates(final DatabaseReference existM1){

        existM1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DataSnapshot> mergeList = new ArrayList<>();
                final Iterable<DataSnapshot> merges = dataSnapshot.getChildren();
                //GOING THROUGH ALL THE JOURNEY REQUESTS
                for (DataSnapshot merge : merges) {
                    mergeList.add(merge);
                }

                for(int i=0;i<mergeList.size()-1;i++){
                    DataSnapshot mergeDS = mergeList.get(i);
                    String key1 = mergeDS.getKey().toString();
                    String with1 = mergeDS.child("With").getValue().toString();

                    for(int j = i+1;j<mergeList.size();j++){
                        DataSnapshot mergeDS2 = mergeList.get(j);
                        String key2 = mergeDS2.getKey().toString();
                        String with2 = mergeDS2.child("With").getValue().toString();

                        if(with1.equals(with2) &&!key1.equals(key2)){
                            DatabaseReference duplicate = existM1.child(key2);
                            duplicate.removeValue();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public boolean check2(){
        return true;
    }




    public void add2(final DatabaseReference existingMerges, final DatabaseReference existingMerges2, final ArrayList<String> info, final List<Double> coords){

        final String user1ID =info.get(0);
        final String JRID = info.get(1);
        final double unsCost1 = Double.parseDouble(info.get(2));
        final double desCost1 = Double.parseDouble(info.get(3));
        final double unsDur1 = Double.parseDouble(info.get(4));
        final double desDur1 = Double.parseDouble(info.get(5));


        final String user2ID =info.get(6);
        final String JRID2 = info.get(7);
        final double unsCost2 = Double.parseDouble(info.get(8));
        final double desCost2 = Double.parseDouble(info.get(9));
        final double unsDur2 = Double.parseDouble(info.get(10));
        final double desDur2 = Double.parseDouble(info.get(11));

        final String time = info.get(12);
        final String date = info.get(13);

        final double startLat = coords.get(0);
        final double startLon = coords.get(1);
        final double endLat = coords.get(2);
        final double endLon = coords.get(3);
        final double startLat2 = coords.get(4);
        final double startLon2 = coords.get(5);
        final double endLat2 = coords.get(6);
        final double endLon2 = coords.get(7);



        double distance=1000000;
        double FstartLat = 0;
        double FstartLon = 0;
        double FendLat = 0;
        double FendLon = 0;
        double FstartLat2 = 0;
        double FstartLon2 = 0;
        double FendLat2 = 0;
        double FendLon2 = 0;




        List<Double> startLats = new ArrayList<Double>();
        List<Double> startLons = new ArrayList<Double>();
        List<Double> endLats = new ArrayList<Double>();
        List<Double> endLons = new ArrayList<Double>();

        startLats.add(startLat);
        startLats.add(startLat2);
        startLons.add(startLon);
        startLons.add(startLon2);

        endLats.add(endLat);
        endLats.add(endLat2);
        endLons.add(endLon);
        endLons.add(endLon2);

        int currentTotal = 10000000;

        for(int a=0;a<2;a++){
            List<Double> startLats1 = new ArrayList<Double>(startLats);
            List<Double> startLons1 =new ArrayList<Double>(startLons);
            double sLat1 = startLats1.get(a);
            double sLon1 = startLons1.get(a);
            startLats1.remove(a);
            startLons1.remove(a);
                double sLat2 = startLats1.get(0);
                double sLon2 = startLons1.get(0);

                for(int x=0;x<2;x++) {
                    List<Double> endLats1 = new ArrayList<Double>(endLats);
                    List<Double> endLons1 = new ArrayList<Double>(endLons);
                    double eLat1 = endLats1.get(x);
                    double eLon1 = endLons1.get(x);
                    endLats1.remove(x);
                    endLons1.remove(x);
                        double eLat2 = endLats1.get(0);
                        double eLon2 = endLons1.get(0);


                        double newDistance = 0;
                        newDistance+=distance(sLat1,sLon1,sLat2,sLon2);
                        newDistance+=distance(sLat2,sLon2,eLat1,sLon1);
                        newDistance+=distance(eLat1,eLon1,eLat2,sLon2);


                        Log.e("Lat1", " "+sLat1);
                        Log.e("Lat2", " "+sLat2);
                        Log.e("End1", " "+eLat1);
                        Log.e("End2", " "+eLat2);

                        if(newDistance<distance){

                            distance=newDistance;
                             FstartLat = sLat1;
                             FstartLon = sLon1;
                             FendLat = eLat1;
                             FendLon = eLon1;
                             FstartLat2 = sLat2;
                             FstartLon2 = sLon2;
                             FendLat2 = eLat2;
                             FendLon2 = eLon2;

                        }









            }

        }

//
//        double dist1 = distance(FstartLat, FstartLon, FstartLat2, FstartLon2);
//        double dist2 = distance(FstartLat2, FstartLon2, FendLat, FendLon);
//        double dist3 = distance(FendLat, FendLon, FendLat2, FendLon2);




                            Object dataTransfer1[];



                                dataTransfer1 = new Object[1];
                                String url1 = getDirectionsUrl2(FstartLat, FstartLon, FstartLat2, FstartLon2);
                                GetDirectionsData getDirectionsData1 = new GetDirectionsData();
                                dataTransfer1[0] = url1;
                                getDirectionsData1.execute(dataTransfer1);
                                final Handler handler = new Handler();
                                final GetDirectionsData finalGetDirectionsData = getDirectionsData1;
                                final String finalUrl = url1;
                                String[] info1 = finalGetDirectionsData.getInfo(finalUrl);



                            dataTransfer1 = new Object[1];
                                String url2 = getDirectionsUrl2(FstartLat2, FstartLon2, FendLat, FendLon);
                                getDirectionsData1 = new GetDirectionsData();
                                dataTransfer1[0] = url2;
                                getDirectionsData1.execute(dataTransfer1);
                                String[] info2 = getDirectionsData1.getInfo(url2);

                                dataTransfer1 = new Object[1];
                                String url3 = getDirectionsUrl2(FendLat, FendLon, FendLat2, FendLon2);
                                getDirectionsData1 = new GetDirectionsData();
                                dataTransfer1[0] = url3;
                                getDirectionsData1.execute(dataTransfer1);
                                String[] info3 = getDirectionsData1.getInfo(url3);



        double dist1 = getTimeDistance(info1);
        double dist2 = getTimeDistance(info2);
        double dist3 = getTimeDistance(info3);




        double OverallDist = dist1+dist2+dist3;
        double OverallCost = getFare(OverallDist);
        double actDur1=1000000;
        double actCost1=100000;
        double actDur2=1000000;
        double actCost2=100000;
        int compat1;
        int compat2;



        for(int i =1;i<3;i++){


            double stlat,stlon,enLat,enLon;
            double yourCost;
            double yourDur;

            if(i==1){
                 stlat = startLat;  stlon = startLon;  enLat = endLat;  enLon = endLon;
            }
            else{
                 stlat = startLat2;  stlon = startLon2;  enLat = endLat2;  enLon = endLon2;
            }

            if(stlat==FstartLat&&stlon==FstartLon)
            {
                if(enLat==FendLat && enLon==FendLon)
                {

                    yourDur = dist1+dist2;
                    double percentage = dist2/2;
                    percentage+=dist1;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else
                {

                    yourDur = dist1+dist2+dist3;


                    double percentage = dist2/2;
                    percentage+=dist1+dist3;
                    percentage = percentage/OverallDist;


                    yourCost= OverallCost*percentage;
                }

            }
            else
            {
                if(enLat==FendLat && enLon==FendLon)
                {
                    yourDur = dist2;
                    double percentage = dist2/2;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else
                {
                    yourDur = dist2+dist3;
                    double percentage = dist2/2;
                    percentage+=dist3;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }


            }

            if(i==1){
                actCost1 = yourCost;  actDur1 = yourDur;
            }
            else{
                actCost2 = yourCost;  actDur2 = yourDur;
            }


        }


        if(actCost1<unsCost1 && actCost2<unsCost2 && actDur1<(desDur1*1.25)&& actDur2<(desDur2*1.25)){

            double costCompat1;
            double costCompat2;
            double durCompat1;
            double durCompat2;

            //PASSENGER 1
            if(actCost1<=desCost1){
                costCompat1 = 1;
            }
            else{
                costCompat1 = (unsCost1-actCost1)/(unsCost1-desCost1);
            }
            if(actDur1<=desDur1){
                durCompat1 = 1;
            }
            else{
                durCompat1 = ((desDur1*1.25)-actDur1)/((desDur1*1.25)-unsDur1);
            }

            compat1 = (int)(((costCompat1+durCompat1)/2)*100);

            //PASSENGER 2
            if(actCost2<=desCost2){
                costCompat2 = 1;
            }
            else{
                costCompat2 = (unsCost2-actCost2)/(unsCost2-desCost2);;
            }
            if(actDur2<=desDur2){
                durCompat2 = 1;
            }
            else{
                durCompat2 = ((desDur2*1.25)-actDur2)/((desDur2*1.25)-unsDur2);;
            }

            compat2 = (int) (((costCompat2+durCompat2)/2)*100);




            final String mergeID = makeID();
            existingMerges.child(mergeID).child("With").setValue(JRID2);
            existingMerges.child(mergeID).child("Compat").setValue(compat1);
            existingMerges.child(mergeID).child("Accepted").setValue("NO");
            existingMerges.child(mergeID).child("StartLat").setValue(startLat2);
            existingMerges.child(mergeID).child("StartLon").setValue(startLon2);
            existingMerges.child(mergeID).child("EndLat").setValue(endLat);
            existingMerges.child(mergeID).child("EndLon").setValue(endLon);

            existingMerges2.child(mergeID).child("With").setValue(JRID);
            existingMerges2.child(mergeID).child("Compat").setValue(compat2);
            existingMerges2.child(mergeID).child("Accepted").setValue("NO");
            existingMerges2.child(mergeID).child("StartLat").setValue(startLat);
            existingMerges2.child(mergeID).child("StartLon").setValue(startLon);
            existingMerges2.child(mergeID).child("EndLat").setValue(endLat2);
            existingMerges2.child(mergeID).child("EndLon").setValue(endLon2);


            //PLACING IT IN MERGE PROPOSAL TABLE
            DatabaseReference mergeTab = FirebaseDatabase.getInstance().getReference().child("Merge Proposals");

//        mergeTab.child(mergeID).child("Compat1").setValue("92");
//        mergeTab.child(mergeID).child("Compat2").setValue("78");
//        mergeTab.child(mergeID).child("UserID1").setValue(user1ID);
//        mergeTab.child(mergeID).child("UserID2").setValue(user2ID);
//        mergeTab.child(mergeID).child("JRID1").setValue(JRID);
//        mergeTab.child(mergeID).child("JRID2").setValue(JRID2);
//
//
            mergeTab.child(mergeID).child("StartLat1").setValue(FstartLat);
            mergeTab.child(mergeID).child("StartLon1").setValue(FstartLon);
            mergeTab.child(mergeID).child("StartLat2").setValue(FstartLat2);
            mergeTab.child(mergeID).child("StartLon2").setValue(FstartLon2);
            mergeTab.child(mergeID).child("EndLat1").setValue(FendLat);
            mergeTab.child(mergeID).child("EndLon1").setValue(FendLon);
            mergeTab.child(mergeID).child("EndLat2").setValue(FendLat2);
            mergeTab.child(mergeID).child("EndLon2").setValue(FendLon2);

            mergeTab.child(mergeID).child("Overall Cost").setValue(OverallCost);
            mergeTab.child(mergeID).child("Overall Distance").setValue(OverallDist);
            mergeTab.child(mergeID).child("Time").setValue(time);
            mergeTab.child(mergeID).child("Date").setValue(date);




            mergeTab.child(mergeID).child(user1ID).child("Compat").setValue(compat1);
            mergeTab.child(mergeID).child(user1ID).child("JRID").setValue(JRID);
            mergeTab.child(mergeID).child(user1ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user1ID).child("Cost").setValue(actCost1);
            mergeTab.child(mergeID).child(user1ID).child("Desired Cost").setValue(desCost1);
            mergeTab.child(mergeID).child(user1ID).child("Unshared Cost").setValue(unsCost1);
            mergeTab.child(mergeID).child(user1ID).child("Duration").setValue(actDur1);
            mergeTab.child(mergeID).child(user1ID).child("Desired Duration").setValue(desDur1);
            mergeTab.child(mergeID).child(user1ID).child("Unshared Duration").setValue(unsDur1);
            mergeTab.child(mergeID).child(user1ID).child("StartLat").setValue(startLat);
            mergeTab.child(mergeID).child(user1ID).child("StartLon").setValue(startLon);
            mergeTab.child(mergeID).child(user1ID).child("EndLat").setValue(endLat);
            mergeTab.child(mergeID).child(user1ID).child("EndLon").setValue(endLon);

            mergeTab.child(mergeID).child(user2ID).child("Compat").setValue(compat2);
            mergeTab.child(mergeID).child(user2ID).child("JRID").setValue(JRID2);
            mergeTab.child(mergeID).child(user2ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user2ID).child("Cost").setValue(actCost2);
            mergeTab.child(mergeID).child(user2ID).child("Desired Cost").setValue(desCost2);
            mergeTab.child(mergeID).child(user2ID).child("Unshared Cost").setValue(unsCost2);
            mergeTab.child(mergeID).child(user2ID).child("Duration").setValue(actDur2);
            mergeTab.child(mergeID).child(user2ID).child("Desired Duration").setValue(desDur2);
            mergeTab.child(mergeID).child(user2ID).child("Unshared Duration").setValue(unsDur2);
            mergeTab.child(mergeID).child(user2ID).child("StartLat").setValue(startLat2);
            mergeTab.child(mergeID).child(user2ID).child("StartLon").setValue(startLon2);
            mergeTab.child(mergeID).child(user2ID).child("EndLat").setValue(endLat2);
            mergeTab.child(mergeID).child(user2ID).child("EndLon").setValue(endLon2);

            mergeTab.child(mergeID).child("Amount").setValue(2);
            mergeTab.child(mergeID).child("AmountAccepted").setValue(0);

        }

        //PLACING IT IN JOURNEY REQUEST PROPOSED MERGES




    }


    public void add3(final DatabaseReference existingMerges, final DatabaseReference existingMerges2, final DatabaseReference existingMerges3,
                     final ArrayList<String> info, final List<Double> coords){

        final String user1ID =info.get(0);
        final String JRID = info.get(1);
        final double unsCost1 = Double.parseDouble(info.get(2));
        final double desCost1 = Double.parseDouble(info.get(3));
        final double unsDur1 = Double.parseDouble(info.get(4));
        final double desDur1 = Double.parseDouble(info.get(5));


        final String user2ID =info.get(6);
        final String JRID2 = info.get(7);
        final double unsCost2 = Double.parseDouble(info.get(8));
        final double desCost2 = Double.parseDouble(info.get(9));
        final double unsDur2 = Double.parseDouble(info.get(10));
        final double desDur2 = Double.parseDouble(info.get(11));

        final String user3ID =info.get(12);
        final String JRID3 = info.get(13);
        final double unsCost3 = Double.parseDouble(info.get(14));
        final double desCost3 = Double.parseDouble(info.get(15));
        final double unsDur3 = Double.parseDouble(info.get(16));
        final double desDur3 = Double.parseDouble(info.get(17));

        String time = info.get(18);
        String date = info.get(19);

        final double startLat = coords.get(0);
        final double startLon = coords.get(1);
        final double endLat = coords.get(2);
        final double endLon = coords.get(3);
        final double startLat2 = coords.get(4);
        final double startLon2 = coords.get(5);
        final double endLat2 = coords.get(6);
        final double endLon2 = coords.get(7);
        final double startLat3 = coords.get(8);
        final double startLon3 = coords.get(9);
        final double endLat3 = coords.get(10);
        final double endLon3 = coords.get(11);



        double distance=1000000;
        double FstartLat = 0;
        double FstartLon = 0;
        double FendLat = 0;
        double FendLon = 0;
        double FstartLat2 = 0;
        double FstartLon2 = 0;
        double FendLat2 = 0;
        double FendLon2 = 0;
        double FstartLat3 = 0;
        double FstartLon3 = 0;
        double FendLat3 = 0;
        double FendLon3 = 0;


        List<Double> startLats = new ArrayList<Double>();
        List<Double> startLons = new ArrayList<Double>();
        List<Double> endLats = new ArrayList<Double>();
        List<Double> endLons = new ArrayList<Double>();

        startLats.add(startLat);
        startLats.add(startLat2);
        startLats.add(startLat3);
        startLons.add(startLon);
        startLons.add(startLon2);
        startLons.add(startLon3);

        endLats.add(endLat);
        endLats.add(endLat2);
        endLats.add(endLat3);
        endLons.add(endLon);
        endLons.add(endLon2);
        endLons.add(endLon3);

        int currentTotal = 10000000;

        for(int a=0;a<3;a++){
            List<Double> startLats1 = new ArrayList<Double>(startLats);
            List<Double> startLons1 =new ArrayList<Double>(startLons);
            double sLat1 = startLats1.get(a);
            double sLon1 = startLons1.get(a);
            startLats1.remove(a);
            startLons1.remove(a);
            for(int b=0;b<2;b++){
                List<Double> startLats2 = new ArrayList<Double>(startLats1);
                List<Double> startLons2 =new ArrayList<Double>(startLons1);
                double sLat2 = startLats2.get(b);
                double sLon2 = startLons2.get(b);
                startLats2.remove(b);
                startLons2.remove(b);
                double sLat3 = startLats2.get(0);
                double sLon3 = startLons2.get(0);

                for(int x=0;x<3;x++) {
                    List<Double> endLats1 = new ArrayList<Double>(endLats);
                    List<Double> endLons1 = new ArrayList<Double>(endLons);
                    double eLat1 = endLats1.get(x);
                    double eLon1 = endLons1.get(x);
                    endLats1.remove(x);
                    endLons1.remove(x);
                    for (int y = 0; y < 2; y++) {
                        List<Double> endLats2 = new ArrayList<Double>(endLats1);
                        List<Double> endLons2 = new ArrayList<Double>(endLons1);
                        double eLat2 = endLats2.get(y);
                        double eLon2 = endLons2.get(y);
                        endLats2.remove(y);
                        endLons2.remove(y);
                        double eLat3 = endLats2.get(0);
                        double eLon3 = endLons2.get(0);



                        double newDistance = 0;
                        newDistance+=distance(sLat1,sLon1,sLat2,sLon2);
                        newDistance+=distance(sLat2,sLon2,eLat1,sLon1);
                        newDistance+=distance(eLat1,eLon1,eLat2,sLon2);


                        Log.e("Lat1", " "+sLat1);
                        Log.e("Lat2", " "+sLat2);
                        Log.e("End1", " "+eLat1);
                        Log.e("End2", " "+eLat2);

                        if(newDistance<distance){

                            distance=newDistance;
                            FstartLat = sLat1;
                            FstartLon = sLon1;
                            FendLat = eLat1;
                            FendLon = eLon1;
                            FstartLat2 = sLat2;
                            FstartLon2 = sLon2;
                            FendLat2 = eLat2;
                            FendLon2 = eLon2;
                            FstartLat3 = sLat3;
                            FstartLon3 = sLon3;
                            FendLat3 = eLat3;
                            FendLon3 = eLon3;

                        }



                 }

                }


            }

        }




//
//        double dist1 = distance(FstartLat, FstartLon, FstartLat2, FstartLon2);
//        double dist2 = distance(FstartLat2, FstartLon2, FendLat, FendLon);
//        double dist3 = distance(FendLat, FendLon, FendLat2, FendLon2);




        Object dataTransfer1[];



        dataTransfer1 = new Object[1];
        String url1 = getDirectionsUrl2(FstartLat, FstartLon, FstartLat2, FstartLon2);
        GetDirectionsData getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url1;
        getDirectionsData1.execute(dataTransfer1);
        final Handler handler = new Handler();
        final GetDirectionsData finalGetDirectionsData = getDirectionsData1;
        final String finalUrl = url1;
        String[] info1 = finalGetDirectionsData.getInfo(finalUrl);



        dataTransfer1 = new Object[1];
        String url2 = getDirectionsUrl2(FstartLat2, FstartLon2, FstartLat3, FstartLon3);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url2;
        getDirectionsData1.execute(dataTransfer1);
        String[] info2 = getDirectionsData1.getInfo(url2);

        dataTransfer1 = new Object[1];
        String url3 = getDirectionsUrl2(FstartLat3, FstartLon3, FendLat, FendLon);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url3;
        getDirectionsData1.execute(dataTransfer1);
        String[] info3 = getDirectionsData1.getInfo(url3);

        dataTransfer1 = new Object[1];
        String url4 = getDirectionsUrl2(FendLat, FendLon, FendLat2, FendLon2);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url4;
        getDirectionsData1.execute(dataTransfer1);
        String[] info4 = getDirectionsData1.getInfo(url4);

        dataTransfer1 = new Object[1];
        String url5 = getDirectionsUrl2(FendLat2, FendLon2, FendLat3, FendLon3);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url5;
        getDirectionsData1.execute(dataTransfer1);
        String[] info5 = getDirectionsData1.getInfo(url5);


        double dist1 = getTimeDistance(info1);
        double dist2 = getTimeDistance(info2);
        double dist3 = getTimeDistance(info3);
        double dist4 = getTimeDistance(info4);
        double dist5 = getTimeDistance(info5);




        double OverallDist = dist1+dist2+dist3+dist4+dist5;
        double OverallCost = getFare(OverallDist);
        double actDur1=1000000;
        double actCost1=100000;
        double actDur2=1000000;
        double actCost2=100000;
        double actDur3=1000000;
        double actCost3=100000;
        int compat1;
        int compat2;
        int compat3;



        for(int i =1;i<4;i++){


            double stlat,stlon,enLat,enLon;
            double yourCost;
            double yourDur;

            if(i==1){
                stlat = startLat;  stlon = startLon;  enLat = endLat;  enLon = endLon;
            }
            else if(i==2){
                stlat = startLat2;  stlon = startLon2;  enLat = endLat2;  enLon = endLon2;
            }
            else {
                stlat = startLat3;  stlon = startLon3;  enLat = endLat3;  enLon = endLon3;

            }

            if(stlat==FstartLat&&stlon==FstartLon)
            {
                if(enLat==FendLat && enLon==FendLon)
                {

                    yourDur = dist1+dist2+dist3;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage+=dist1;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat2 && enLon==FendLon2)
                {

                    yourDur = dist1+dist2+dist3+dist4;


                    double percentage = dist2/2;
                    percentage+= (dist3/3);
                    percentage+= (dist4/2);
                    percentage+=dist1;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else {
                    yourDur = OverallDist;
                    double percentage = dist2/2;
                    percentage+= (dist3/3);
                    percentage+= (dist4/2);
                    percentage+=dist1+dist5;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }

            }
            else if(stlat==FstartLat2&&stlon==FstartLon2)
            {
                if(enLat==FendLat && enLon==FendLon)
                {
                    yourDur = dist2+dist3;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat2 && enLon==FendLon2)
                {
                    yourDur = dist2+dist3+dist4;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage+=(dist4/2);
                    percentage = percentage/OverallDist;

                    yourCost= OverallCost*percentage;
                }
                else {
                    yourDur = dist2+dist3+dist4+dist5;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage+=(dist4/2);
                    percentage+=dist5;
                    percentage = percentage/OverallDist;

                    yourCost= OverallCost*percentage;
                }


            }
            else {
                if(enLat==FendLat && enLon==FendLon)
                {
                    yourDur = dist3;
                    double percentage =(dist3/3);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat2 && enLon==FendLon2)
                {
                    yourDur = dist3+dist4;
                    double percentage =(dist3/3);
                    percentage+=(dist4/2);
                    percentage = percentage/OverallDist;

                    yourCost= OverallCost*percentage;
                }
                else {
                    yourDur = dist3+dist4+dist5;
                    double percentage =(dist3/3);
                    percentage+=(dist4/2);
                    percentage+=dist5;
                    percentage = percentage/OverallDist;

                    yourCost= OverallCost*percentage;
                }
            }





            if(i==1){
                actCost1 = yourCost;  actDur1 = yourDur;
            }
            else if(i==2){
                actCost2 = yourCost;  actDur2 = yourDur;
            }
            else {
                actCost3 = yourCost;  actDur3 = yourDur;
            }


        }

        Log.e("if 3: ", "CHECKING COMPATS");

        if((actCost1<unsCost1) && (actCost2<unsCost2)&& (actCost3<unsCost3) && (actDur1<(desDur1*1.25))&& (actDur2<(desDur2*1.25))&& (actDur3<(desDur3*1.25))) {

            Log.e("was 3: ", "was COMPATS");
            double costCompat1;
            double costCompat2;
            double costCompat3;
            double durCompat1;
            double durCompat2;
            double durCompat3;

            //PASSENGER 1
            if(actCost1<=desCost1){
                costCompat1 = 1;
            }
            else{
                costCompat1 = (unsCost1-actCost1)/(unsCost1-desCost1);
            }
            if(actDur1<=desDur1){
                durCompat1 = 1;
            }
            else{
                durCompat1 = ((desDur1*1.25)-actDur1)/((desDur1*1.25)-desDur1);
            }

            compat1 = (int)(((costCompat1+durCompat1)/2)*100);

            //PASSENGER 2
            if(actCost2<=desCost2){
                costCompat2 = 1;
            }
            else{
                costCompat2 = (unsCost2-actCost2)/(unsCost2-desCost2);
            }
            if(actDur2<=desDur2){
                durCompat2 = 1;
            }
            else{
                durCompat2 = ((desDur2*1.25)-actDur2)/((desDur2*1.25)-desDur2);
            }

            compat2 = (int) (((costCompat2+durCompat2)/2)*100);


            //PASSENGER 3
            if (actCost3 <= desCost3) {
                costCompat3 = 1;
            } else {
                costCompat3 = (unsCost3 - actCost3) / (unsCost3-desCost3);
            }
            if (actDur3 <= desDur3) {
                durCompat3 = 1;
            } else {
                durCompat3 = ((desDur3 * 1.25) - actDur3) / ((desDur3 * 1.25)-desDur3);
            }

            compat3 = (int) (((costCompat3 + durCompat3) / 2) * 100);







            final String mergeID = makeID();
            existingMerges.child(mergeID).child("With").setValue(JRID2 +"," + JRID3);
            existingMerges.child(mergeID).child("Compat").setValue(compat1);
            existingMerges.child(mergeID).child("Accepted").setValue("NO");
            existingMerges.child(mergeID).child("StartLat").setValue(startLat);
            existingMerges.child(mergeID).child("StartLon").setValue(startLon);
            existingMerges.child(mergeID).child("EndLat").setValue(endLat);
            existingMerges.child(mergeID).child("EndLon").setValue(endLon);

            existingMerges2.child(mergeID).child("With").setValue(JRID + "," + JRID3);
            existingMerges2.child(mergeID).child("Compat").setValue(compat2);
            existingMerges2.child(mergeID).child("Accepted").setValue("NO");
            existingMerges2.child(mergeID).child("StartLat").setValue(startLat2);
            existingMerges2.child(mergeID).child("StartLon").setValue(startLon2);
            existingMerges2.child(mergeID).child("EndLat").setValue(endLat2);
            existingMerges2.child(mergeID).child("EndLon").setValue(endLon2);

            existingMerges3.child(mergeID).child("With").setValue(JRID + "," + JRID2);
            existingMerges3.child(mergeID).child("Compat").setValue(compat3);
            existingMerges3.child(mergeID).child("Accepted").setValue("NO");
            existingMerges3.child(mergeID).child("StartLat").setValue(startLat3);
            existingMerges3.child(mergeID).child("StartLon").setValue(startLon3);
            existingMerges3.child(mergeID).child("EndLat").setValue(endLat3);
            existingMerges3.child(mergeID).child("EndLon").setValue(endLon3);


            //PLACING IT IN MERGE PROPOSAL TABLE
            DatabaseReference mergeTab = FirebaseDatabase.getInstance().getReference().child("Merge Proposals");


//
//
            mergeTab.child(mergeID).child("StartLat1").setValue(FstartLat);
            mergeTab.child(mergeID).child("StartLon1").setValue(FstartLon);
            mergeTab.child(mergeID).child("StartLat2").setValue(FstartLat2);
            mergeTab.child(mergeID).child("StartLon2").setValue(FstartLon2);
            mergeTab.child(mergeID).child("StartLat3").setValue(FstartLat3);
            mergeTab.child(mergeID).child("StartLon3").setValue(FstartLon3);
            mergeTab.child(mergeID).child("EndLat1").setValue(FendLat);
            mergeTab.child(mergeID).child("EndLon1").setValue(FendLon);
            mergeTab.child(mergeID).child("EndLat2").setValue(FendLat2);
            mergeTab.child(mergeID).child("EndLon2").setValue(FendLon2);
            mergeTab.child(mergeID).child("EndLat3").setValue(FendLat3);
            mergeTab.child(mergeID).child("EndLon3").setValue(FendLon3);

            mergeTab.child(mergeID).child("Overall Cost").setValue(OverallCost);
            mergeTab.child(mergeID).child("Overall Distance").setValue(OverallDist);
            mergeTab.child(mergeID).child("Time").setValue(time);
            mergeTab.child(mergeID).child("Date").setValue(date);



            mergeTab.child(mergeID).child(user1ID).child("Compat").setValue(compat1);
            mergeTab.child(mergeID).child(user1ID).child("JRID").setValue(JRID);
            mergeTab.child(mergeID).child(user1ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user1ID).child("Cost").setValue(actCost1);
            mergeTab.child(mergeID).child(user1ID).child("Desired Cost").setValue(desCost1);
            mergeTab.child(mergeID).child(user1ID).child("Unshared Cost").setValue(unsCost1);
            mergeTab.child(mergeID).child(user1ID).child("Duration").setValue(actDur1);
            mergeTab.child(mergeID).child(user1ID).child("Desired Duration").setValue(desDur1);
            mergeTab.child(mergeID).child(user1ID).child("Unshared Duration").setValue(unsDur1);
            mergeTab.child(mergeID).child(user1ID).child("StartLat").setValue(startLat);
            mergeTab.child(mergeID).child(user1ID).child("StartLon").setValue(startLon);
            mergeTab.child(mergeID).child(user1ID).child("EndLat").setValue(endLat);
            mergeTab.child(mergeID).child(user1ID).child("EndLon").setValue(endLon);

            mergeTab.child(mergeID).child(user2ID).child("Compat").setValue(compat2);
            mergeTab.child(mergeID).child(user2ID).child("JRID").setValue(JRID2);
            mergeTab.child(mergeID).child(user2ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user2ID).child("Cost").setValue(actCost2);
            mergeTab.child(mergeID).child(user2ID).child("Desired Cost").setValue(desCost2);
            mergeTab.child(mergeID).child(user2ID).child("Unshared Cost").setValue(unsCost2);
            mergeTab.child(mergeID).child(user2ID).child("Duration").setValue(actDur2);
            mergeTab.child(mergeID).child(user2ID).child("Desired Duration").setValue(desDur2);
            mergeTab.child(mergeID).child(user2ID).child("Unshared Duration").setValue(unsDur2);
            mergeTab.child(mergeID).child(user2ID).child("StartLat").setValue(startLat2);
            mergeTab.child(mergeID).child(user2ID).child("StartLon").setValue(startLon2);
            mergeTab.child(mergeID).child(user2ID).child("EndLat").setValue(endLat2);
            mergeTab.child(mergeID).child(user2ID).child("EndLon").setValue(endLon2);

            mergeTab.child(mergeID).child(user3ID).child("Compat").setValue(compat3);
            mergeTab.child(mergeID).child(user3ID).child("JRID").setValue(JRID3);
            mergeTab.child(mergeID).child(user3ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user3ID).child("Cost").setValue(actCost3);
            mergeTab.child(mergeID).child(user3ID).child("Desired Cost").setValue(desCost3);
            mergeTab.child(mergeID).child(user3ID).child("Unshared Cost").setValue(unsCost3);
            mergeTab.child(mergeID).child(user3ID).child("Duration").setValue(actDur3);
            mergeTab.child(mergeID).child(user3ID).child("Desired Duration").setValue(desDur3);
            mergeTab.child(mergeID).child(user3ID).child("Unshared Duration").setValue(unsDur3);
            mergeTab.child(mergeID).child(user3ID).child("StartLat").setValue(startLat3);
            mergeTab.child(mergeID).child(user3ID).child("StartLon").setValue(startLon3);
            mergeTab.child(mergeID).child(user3ID).child("EndLat").setValue(endLat3);
            mergeTab.child(mergeID).child(user3ID).child("EndLon").setValue(endLon3);

            mergeTab.child(mergeID).child("Amount").setValue(3);
            mergeTab.child(mergeID).child("AmountAccepted").setValue(0);

        }
        else{
            Log.e("3 FAILED",actCost1+" "+ unsCost1 +" "+  actCost2+" "+ unsCost2+" "+  actCost3+" "+ unsCost3 +"\n "+  actDur1+" "+ (desDur1*1.25)+" "+  actDur2+" "+ (desDur2*1.25)+" "+ actDur3+" "+(desDur3*1.25) );
        }





    }


    public void add4(final DatabaseReference existingMerges, final DatabaseReference existingMerges2, final DatabaseReference existingMerges3,final DatabaseReference existingMerges4,
                     final ArrayList<String> info, final List<Double> coords){
        Log.e("ADD 4: ", "ENTERED FUNCTION");

        final String user1ID =info.get(0);
        final String JRID = info.get(1);
        final double unsCost1 = Double.parseDouble(info.get(2));
        final double desCost1 = Double.parseDouble(info.get(3));
        final double unsDur1 = Double.parseDouble(info.get(4));
        final double desDur1 = Double.parseDouble(info.get(5));


        final String user2ID =info.get(6);
        final String JRID2 = info.get(7);
        final double unsCost2 = Double.parseDouble(info.get(8));
        final double desCost2 = Double.parseDouble(info.get(9));
        final double unsDur2 = Double.parseDouble(info.get(10));
        final double desDur2 = Double.parseDouble(info.get(11));

        final String user3ID =info.get(12);
        final String JRID3 = info.get(13);
        final double unsCost3 = Double.parseDouble(info.get(14));
        final double desCost3 = Double.parseDouble(info.get(15));
        final double unsDur3 = Double.parseDouble(info.get(16));
        final double desDur3 = Double.parseDouble(info.get(17));

        final String user4ID =info.get(18);
        final String JRID4 = info.get(19);
        final double unsCost4 = Double.parseDouble(info.get(20));
        final double desCost4 = Double.parseDouble(info.get(21));
        final double unsDur4 = Double.parseDouble(info.get(22));
        final double desDur4 = Double.parseDouble(info.get(23));

        String time = info.get(24);
        String date = info.get(25);

        final double startLat = coords.get(0);
        final double startLon = coords.get(1);
        final double endLat = coords.get(2);
        final double endLon = coords.get(3);
        final double startLat2 = coords.get(4);
        final double startLon2 = coords.get(5);
        final double endLat2 = coords.get(6);
        final double endLon2 = coords.get(7);
        final double startLat3 = coords.get(8);
        final double startLon3 = coords.get(9);
        final double endLat3 = coords.get(10);
        final double endLon3 = coords.get(11);
        final double startLat4 = coords.get(12);
        final double startLon4 = coords.get(13);
        final double endLat4 = coords.get(14);
        final double endLon4 = coords.get(15);


        double distance=1000000;
         double FstartLat = 0;
         double FstartLon = 0;
         double FendLat = 0;
         double FendLon = 0;
         double FstartLat2 = 0;
         double FstartLon2 = 0;
         double FendLat2 = 0;
         double FendLon2 = 0;
         double FstartLat3 = 0;
         double FstartLon3 = 0;
         double FendLat3 =0;
         double FendLon3 =0;
         double FstartLat4 = 0;
         double FstartLon4 = 0;
         double FendLat4 =0;
         double FendLon4 = 0;


        List<Double> startLats = new ArrayList<Double>();
        List<Double> startLons = new ArrayList<Double>();
        List<Double> endLats = new ArrayList<Double>();
        List<Double> endLons = new ArrayList<Double>();

        startLats.add(startLat);
        startLats.add(startLat2);
        startLats.add(startLat3);
        startLats.add(startLat4);
        startLons.add(startLon);
        startLons.add(startLon2);
        startLons.add(startLon3);
        startLons.add(startLon4);

        endLats.add(endLat);
        endLats.add(endLat2);
        endLats.add(endLat3);
        endLats.add(endLat4);
        endLons.add(endLon);
        endLons.add(endLon2);
        endLons.add(endLon3);
        endLons.add(endLon4);

        int currentTotal = 10000000;

        for(int a=0;a<4;a++){
            List<Double> startLats1 = new ArrayList<Double>(startLats);
            List<Double> startLons1 =new ArrayList<Double>(startLons);
            double sLat1 = startLats1.get(a);
            double sLon1 = startLons1.get(a);
            startLats1.remove(a);
            startLons1.remove(a);
            for(int b=0;b<3;b++){
                List<Double> startLats2 = new ArrayList<Double>(startLats1);
                List<Double> startLons2 =new ArrayList<Double>(startLons1);
                double sLat2 = startLats2.get(b);
                double sLon2 = startLons2.get(b);
                startLats2.remove(b);
                startLons2.remove(b);
                for(int c=0;c<2;c++){
                    List<Double> startLats3 = new ArrayList<Double>(startLats2);
                    List<Double> startLons3 =new ArrayList<Double>(startLons2);
                    double sLat3 = startLats3.get(c);
                    double sLon3 = startLons3.get(c);
                    startLats3.remove(c);
                    startLons3.remove(c);
                    double sLat4 = startLats3.get(0);
                    double sLon4 = startLons3.get(0);



                for(int x=0;x<4;x++) {
                    List<Double> endLats1 = new ArrayList<Double>(endLats);
                    List<Double> endLons1 = new ArrayList<Double>(endLons);
                    Log.e("ENDLATs1 SIZE", String.valueOf(endLats1.size()));
                    double eLat1 = endLats1.get(x);
                    double eLon1 = endLons1.get(x);
                    endLats1.remove(x);
                    endLons1.remove(x);
                    for (int y = 0; y < 3; y++) {
                        List<Double> endLats2 = new ArrayList<Double>(endLats1);
                        List<Double> endLons2 = new ArrayList<Double>(endLons1);
                        Log.e("ENDLATs2 SIZE", String.valueOf(endLats2.size()));
                        double eLat2 = endLats2.get(y);
                        double eLon2 = endLons2.get(y);
                        endLats2.remove(y);
                        endLons2.remove(y);

                        for (int z = 0; z < 2; z++) {
                            List<Double> endLats3 = new ArrayList<Double>(endLats2);
                            List<Double> endLons3 = new ArrayList<Double>(endLons2);
                            Log.e("ENDLATs3 SIZE", String.valueOf(endLats3.size()));
                            double eLat3 = endLats3.get(z);
                            double eLon3 = endLons3.get(z);
                            endLats3.remove(z);
                            endLons3.remove(z);
                            double eLat4 = endLats3.get(0);
                            double eLon4 = endLons3.get(0);






                            double newDistance = 0;
                            newDistance+=distance(sLat1,sLon1,sLat2,sLon2);
                            newDistance+=distance(sLat2,sLon2,eLat1,sLon1);
                            newDistance+=distance(eLat1,eLon1,eLat2,sLon2);


                            Log.e("Lat1", " "+sLat1);
                            Log.e("Lat2", " "+sLat2);
                            Log.e("End1", " "+eLat1);
                            Log.e("End2", " "+eLat2);

                            if(newDistance<distance){

                                distance=newDistance;
                                FstartLat = sLat1;
                                FstartLon = sLon1;
                                FendLat = eLat1;
                                FendLon = eLon1;
                                FstartLat2 = sLat2;
                                FstartLon2 = sLon2;
                                FendLat2 = eLat2;
                                FendLon2 = eLon2;
                                FstartLat3 = sLat3;
                                FstartLon3 = sLon3;
                                FendLat3 = eLat3;
                                FendLon3 = eLon3;

                            }





                        }
                        }
                    }

                }


            }

        }






//
//        double dist1 = distance(FstartLat, FstartLon, FstartLat2, FstartLon2);
//        double dist2 = distance(FstartLat2, FstartLon2, FendLat, FendLon);
//        double dist3 = distance(FendLat, FendLon, FendLat2, FendLon2);




        Object dataTransfer1[];



        dataTransfer1 = new Object[1];
        String url1 = getDirectionsUrl2(FstartLat, FstartLon, FstartLat2, FstartLon2);
        GetDirectionsData getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url1;
        getDirectionsData1.execute(dataTransfer1);
        final Handler handler = new Handler();
        final GetDirectionsData finalGetDirectionsData = getDirectionsData1;
        final String finalUrl = url1;
        String[] info1 = finalGetDirectionsData.getInfo(finalUrl);



        dataTransfer1 = new Object[1];
        String url2 = getDirectionsUrl2(FstartLat2, FstartLon2, FstartLat3, FstartLon3);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url2;
        getDirectionsData1.execute(dataTransfer1);
        String[] info2 = getDirectionsData1.getInfo(url2);

        dataTransfer1 = new Object[1];
        String url3 = getDirectionsUrl2(FstartLat3, FstartLon3, FendLat, FendLon);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url3;
        getDirectionsData1.execute(dataTransfer1);
        String[] info3 = getDirectionsData1.getInfo(url3);

        dataTransfer1 = new Object[1];
        String url4 = getDirectionsUrl2(FendLat, FendLon, FendLat2, FendLon2);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url4;
        getDirectionsData1.execute(dataTransfer1);
        String[] info4 = getDirectionsData1.getInfo(url4);

        dataTransfer1 = new Object[1];
        String url5 = getDirectionsUrl2(FendLat2, FendLon2, FendLat3, FendLon3);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url5;
        getDirectionsData1.execute(dataTransfer1);
        String[] info5 = getDirectionsData1.getInfo(url5);

        dataTransfer1 = new Object[1];
        String url6 = getDirectionsUrl2(FendLat, FendLon, FendLat2, FendLon2);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url6;
        getDirectionsData1.execute(dataTransfer1);
        String[] info6 = getDirectionsData1.getInfo(url6);

        dataTransfer1 = new Object[1];
        String url7 = getDirectionsUrl2(FendLat2, FendLon2, FendLat3, FendLon3);
        getDirectionsData1 = new GetDirectionsData();
        dataTransfer1[0] = url7;
        getDirectionsData1.execute(dataTransfer1);
        String[] info7 = getDirectionsData1.getInfo(url7);


        double dist1 = getTimeDistance(info1);
        double dist2 = getTimeDistance(info2);
        double dist3 = getTimeDistance(info3);
        double dist4 = getTimeDistance(info4);
        double dist5 = getTimeDistance(info5);
        double dist6 = getTimeDistance(info6);
        double dist7 = getTimeDistance(info7);




        double OverallDist = dist1+dist2+dist3+dist4+dist5+dist6+dist7;
        double OverallCost = getFare(OverallDist);
        double actDur1=1000000;
        double actCost1=100000;
        double actDur2=1000000;
        double actCost2=100000;
        double actDur3=1000000;
        double actCost3=100000;
        double actDur4=1000000;
        double actCost4=100000;
        int compat1;
        int compat2;
        int compat3;
        int compat4;



        for(int i =1;i<5;i++){


            double stlat,stlon,enLat,enLon;
            double yourCost;
            double yourDur;

            if(i==1){
                stlat = startLat;  stlon = startLon;  enLat = endLat;  enLon = endLon;
            }
            else if(i==2){
                stlat = startLat2;  stlon = startLon2;  enLat = endLat2;  enLon = endLon2;
            }
            else if(i==3) {
                stlat = startLat3;  stlon = startLon3;  enLat = endLat3;  enLon = endLon3;

            }
            else {
                stlat = startLat4;  stlon = startLon4;  enLat = endLat4;  enLon = endLon4;
            }

            if(stlat==FstartLat&&stlon==FstartLon)
            {
                if(enLat==FendLat && enLon==FendLon)
                {

                    yourDur = dist1+dist2+dist3+dist4;
                    double percentage = dist2/2;
                    percentage+=dist1;
                    percentage+=(dist3/3);
                    percentage+=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat2 && enLon==FendLon2)
                {

                    yourDur = dist1+dist2+dist3+dist4+dist5;
                    double percentage = dist2/2;
                    percentage+=dist1;
                    percentage+=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat3 && enLon==FendLon3)
                {
                    yourDur = dist1+dist2+dist3+dist4+dist5+dist6;
                    double percentage = dist2/2;
                    percentage+=dist1;
                    percentage+=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else{
                    yourDur = OverallDist;
                    double percentage = dist2/2;
                    percentage+=dist1;
                    percentage+=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage+=dist7;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }

            }
            else if(enLat==FendLat2 && enLon==FendLon2)
            {
                if(enLat==FendLat && enLon==FendLon)
                {

                    yourDur = dist2+dist3+dist4;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage+=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat2 && enLon==FendLon2)
                {

                    yourDur = dist2+dist3+dist4+dist5;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat3 && enLon==FendLon3)
                {
                    yourDur = dist2+dist3+dist4+dist5+dist6;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else{
                    yourDur = dist2+dist3+dist4+dist5+dist6+dist7;
                    double percentage = dist2/2;
                    percentage+=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage+=dist7;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }


            }
            else if(enLat==FendLat3 && enLon==FendLon3)
            {

                if(enLat==FendLat && enLon==FendLon)
                {

                    yourDur = dist3+dist4;
                    double percentage=(dist3/3);
                    percentage+=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat2 && enLon==FendLon2)
                {

                    yourDur = dist3+dist4+dist5;
                    double percentage=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat3 && enLon==FendLon3)
                {
                    yourDur = dist3+dist4+dist5+dist6;
                    double percentage=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else{
                    yourDur = dist3+dist4+dist5+dist6+dist7;
                    double percentage=(dist3/3);
                    percentage+=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage+=dist7;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }

            }
            else {

                if(enLat==FendLat && enLon==FendLon)
                {

                    yourDur = dist4;
                    double percentage=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat2 && enLon==FendLon2)
                {

                    yourDur = dist4+dist5;
                    double percentage =(dist5/3);
                    percentage+=(dist4/4);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else if(enLat==FendLat3 && enLon==FendLon3)
                {
                    yourDur = dist4+dist5+dist6;
                    double percentage=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }
                else{
                    yourDur = dist4+dist5+dist6+dist7;
                    double percentage=(dist5/3);
                    percentage+=(dist4/4);
                    percentage+=(dist6/2);
                    percentage+=dist7;
                    percentage = percentage/OverallDist;
                    yourCost= OverallCost*percentage;
                }

            }






            if(i==1){
                actCost1 = yourCost;  actDur1 = yourDur;
            }
            else if(i==2){
                actCost2 = yourCost;  actDur2 = yourDur;
            }
            else if(i==3){
                actCost3 = yourCost;  actDur3 = yourDur;
            }
            else{
                actCost4 = yourCost;  actDur4 = yourDur;
            }


        }


        if((actCost1<unsCost1) && (actCost2<unsCost2)&& (actCost3<unsCost3) && (actCost4<unsCost4) &&
                (actDur1<(desDur1*1.25))&& (actDur2<(desDur2*1.25))&& (actDur3<(desDur3*1.25))&& (actDur4<(desDur4*1.25))) {

            double costCompat1;
            double costCompat2;
            double costCompat3;
            double costCompat4;
            double durCompat1;
            double durCompat2;
            double durCompat3;
            double durCompat4;

            //PASSENGER 1
            if(actCost1<=desCost1){
                costCompat1 = 1;
            }
            else{
                costCompat1 = (unsCost1-actCost1)/(unsCost1-desCost1);
            }
            if(actDur1<=desDur1){
                durCompat1 = 1;
            }
            else{
                durCompat1 = ((desDur1*1.25)-actDur1)/((desDur1*1.25)-desDur1);
            }

            compat1 = (int)(((costCompat1+durCompat1)/2)*100);

            //PASSENGER 2
            if(actCost2<=desCost2){
                costCompat2 = 1;
            }
            else{
                costCompat2 = (unsCost2-actCost2)/(unsCost2-desCost2);
            }
            if(actDur2<=desDur2){
                durCompat2 = 1;
            }
            else{
                durCompat2 = ((desDur2*1.25)-actDur2)/((desDur2*1.25)-desDur2);
            }

            compat2 = (int) (((costCompat2+durCompat2)/2)*100);


            //PASSENGER 3
            if (actCost3 <= desCost3) {
                costCompat3 = 1;
            } else {
                costCompat3 = (unsCost3 - actCost3) / (unsCost3-desCost3);
            }
            if (actDur3 <= desDur3) {
                durCompat3 = 1;
            } else {
                durCompat3 = ((desDur3 * 1.25) - actDur3) / ((desDur3 * 1.25) -desDur3);
            }

            compat3 = (int) (((costCompat3 + durCompat3) / 2) * 100);


            //PASSENGER 4
            if (actCost4 <= desCost4) {
                costCompat4 = 1;
            } else {
                costCompat4 = (unsCost4 - actCost4) / (unsCost4-desCost4);
            }
            if (actDur4 <= desDur4) {
                durCompat4 = 1;
            } else {
                durCompat4 = ((desDur4 * 1.25) - actDur4) /((desDur4 * 1.25)-desDur4);
            }

            compat4 = (int) (((costCompat4 + durCompat4) / 2) * 100);








            final String mergeID = makeID();
            existingMerges.child(mergeID).child("With").setValue(JRID2 +"," + JRID3+","+JRID4);
            existingMerges.child(mergeID).child("Compat").setValue(compat1);
            existingMerges.child(mergeID).child("Accepted").setValue("NO");
            existingMerges.child(mergeID).child("StartLat").setValue(startLat);
            existingMerges.child(mergeID).child("StartLon").setValue(startLon);
            existingMerges.child(mergeID).child("EndLat").setValue(endLat);
            existingMerges.child(mergeID).child("EndLon").setValue(endLon);

            existingMerges2.child(mergeID).child("With").setValue(JRID + "," + JRID3+","+JRID4);
            existingMerges2.child(mergeID).child("Compat").setValue(compat2);
            existingMerges2.child(mergeID).child("Accepted").setValue("NO");
            existingMerges2.child(mergeID).child("StartLat").setValue(startLat2);
            existingMerges2.child(mergeID).child("StartLon").setValue(startLon2);
            existingMerges2.child(mergeID).child("EndLat").setValue(endLat2);
            existingMerges2.child(mergeID).child("EndLon").setValue(endLon2);

            existingMerges3.child(mergeID).child("With").setValue(JRID + "," + JRID2+","+JRID4);
            existingMerges3.child(mergeID).child("Compat").setValue(compat3);
            existingMerges3.child(mergeID).child("Accepted").setValue("NO");
            existingMerges3.child(mergeID).child("StartLat").setValue(startLat3);
            existingMerges3.child(mergeID).child("StartLon").setValue(startLon3);
            existingMerges3.child(mergeID).child("EndLat").setValue(endLat3);
            existingMerges3.child(mergeID).child("EndLon").setValue(endLon3);

            existingMerges4.child(mergeID).child("With").setValue(JRID + "," + JRID2+","+JRID3);
            existingMerges4.child(mergeID).child("Compat").setValue(compat4);
            existingMerges4.child(mergeID).child("Accepted").setValue("NO");
            existingMerges4.child(mergeID).child("StartLat").setValue(startLat4);
            existingMerges4.child(mergeID).child("StartLon").setValue(startLon4);
            existingMerges4.child(mergeID).child("EndLat").setValue(endLat4);
            existingMerges4.child(mergeID).child("EndLon").setValue(endLon4);


            //PLACING IT IN MERGE PROPOSAL TABLE
            DatabaseReference mergeTab = FirebaseDatabase.getInstance().getReference().child("Merge Proposals");


//
//
            mergeTab.child(mergeID).child("StartLat1").setValue(FstartLat);
            mergeTab.child(mergeID).child("StartLon1").setValue(FstartLon);
            mergeTab.child(mergeID).child("StartLat2").setValue(FstartLat2);
            mergeTab.child(mergeID).child("StartLon2").setValue(FstartLon2);
            mergeTab.child(mergeID).child("StartLat3").setValue(FstartLat3);
            mergeTab.child(mergeID).child("StartLon3").setValue(FstartLon3);
            mergeTab.child(mergeID).child("StartLat4").setValue(FstartLat4);
            mergeTab.child(mergeID).child("StartLon4").setValue(FstartLon4);
            mergeTab.child(mergeID).child("EndLat1").setValue(FendLat);
            mergeTab.child(mergeID).child("EndLon1").setValue(FendLon);
            mergeTab.child(mergeID).child("EndLat2").setValue(FendLat2);
            mergeTab.child(mergeID).child("EndLon2").setValue(FendLon2);
            mergeTab.child(mergeID).child("EndLat3").setValue(FendLat3);
            mergeTab.child(mergeID).child("EndLon3").setValue(FendLon3);
            mergeTab.child(mergeID).child("EndLat4").setValue(FendLat4);
            mergeTab.child(mergeID).child("EndLon4").setValue(FendLon4);

            mergeTab.child(mergeID).child("Overall Cost").setValue(OverallCost);
            mergeTab.child(mergeID).child("Overall Distance").setValue(OverallDist);



            mergeTab.child(mergeID).child(user1ID).child("Compat").setValue(compat1);
            mergeTab.child(mergeID).child(user1ID).child("JRID").setValue(JRID);
            mergeTab.child(mergeID).child(user1ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user1ID).child("Cost").setValue(actCost1);
            mergeTab.child(mergeID).child(user1ID).child("Desired Cost").setValue(desCost1);
            mergeTab.child(mergeID).child(user1ID).child("Unshared Cost").setValue(unsCost1);
            mergeTab.child(mergeID).child(user1ID).child("Duration").setValue(actDur1);
            mergeTab.child(mergeID).child(user1ID).child("Desired Duration").setValue(desDur1);
            mergeTab.child(mergeID).child(user1ID).child("Unshared Duration").setValue(unsDur1);
            mergeTab.child(mergeID).child(user1ID).child("StartLat").setValue(startLat);
            mergeTab.child(mergeID).child(user1ID).child("StartLon").setValue(startLon);
            mergeTab.child(mergeID).child(user1ID).child("EndLat").setValue(endLat);
            mergeTab.child(mergeID).child(user1ID).child("EndLon").setValue(endLon);

            mergeTab.child(mergeID).child(user2ID).child("Compat").setValue(compat2);
            mergeTab.child(mergeID).child(user2ID).child("JRID").setValue(JRID2);
            mergeTab.child(mergeID).child(user2ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user2ID).child("Cost").setValue(actCost2);
            mergeTab.child(mergeID).child(user2ID).child("Desired Cost").setValue(desCost2);
            mergeTab.child(mergeID).child(user2ID).child("Unshared Cost").setValue(unsCost2);
            mergeTab.child(mergeID).child(user2ID).child("Duration").setValue(actDur2);
            mergeTab.child(mergeID).child(user2ID).child("Desired Duration").setValue(desDur2);
            mergeTab.child(mergeID).child(user2ID).child("Unshared Duration").setValue(unsDur2);
            mergeTab.child(mergeID).child(user2ID).child("StartLat").setValue(startLat2);
            mergeTab.child(mergeID).child(user2ID).child("StartLon").setValue(startLon2);
            mergeTab.child(mergeID).child(user2ID).child("EndLat").setValue(endLat2);
            mergeTab.child(mergeID).child(user2ID).child("EndLon").setValue(endLon2);

            mergeTab.child(mergeID).child(user3ID).child("Compat").setValue(compat3);
            mergeTab.child(mergeID).child(user3ID).child("JRID").setValue(JRID3);
            mergeTab.child(mergeID).child(user3ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user3ID).child("Cost").setValue(actCost3);
            mergeTab.child(mergeID).child(user3ID).child("Desired Cost").setValue(desCost3);
            mergeTab.child(mergeID).child(user3ID).child("Unshared Cost").setValue(unsCost3);
            mergeTab.child(mergeID).child(user3ID).child("Duration").setValue(actDur3);
            mergeTab.child(mergeID).child(user3ID).child("Desired Duration").setValue(desDur3);
            mergeTab.child(mergeID).child(user3ID).child("Unshared Duration").setValue(unsDur3);
            mergeTab.child(mergeID).child(user3ID).child("StartLat").setValue(startLat3);
            mergeTab.child(mergeID).child(user3ID).child("StartLon").setValue(startLon3);
            mergeTab.child(mergeID).child(user3ID).child("EndLat").setValue(endLat3);
            mergeTab.child(mergeID).child(user3ID).child("EndLon").setValue(endLon3);

            mergeTab.child(mergeID).child(user4ID).child("Compat").setValue(compat4);
            mergeTab.child(mergeID).child(user4ID).child("JRID").setValue(JRID4);
            mergeTab.child(mergeID).child(user4ID).child("Accepted").setValue("NO");
            mergeTab.child(mergeID).child(user4ID).child("Cost").setValue(actCost4);
            mergeTab.child(mergeID).child(user4ID).child("Desired Cost").setValue(desCost4);
            mergeTab.child(mergeID).child(user4ID).child("Unshared Cost").setValue(unsCost4);
            mergeTab.child(mergeID).child(user4ID).child("Duration").setValue(actDur4);
            mergeTab.child(mergeID).child(user4ID).child("Desired Duration").setValue(desDur4);
            mergeTab.child(mergeID).child(user4ID).child("Unshared Duration").setValue(unsDur4);
            mergeTab.child(mergeID).child(user4ID).child("StartLat").setValue(startLat4);
            mergeTab.child(mergeID).child(user4ID).child("StartLon").setValue(startLon4);
            mergeTab.child(mergeID).child(user4ID).child("EndLat").setValue(endLat4);
            mergeTab.child(mergeID).child(user4ID).child("EndLon").setValue(endLon4);

            mergeTab.child(mergeID).child("Amount").setValue(4);
            mergeTab.child(mergeID).child("AmountAccepted").setValue(0);

        }








    }



    private String getDirectionsUrl2(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
    {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+startLatitude+","+startLongitude);
        googleDirectionsUrl.append("&destination="+endLatitude+","+endLongitude);
        googleDirectionsUrl.append("&key="+"AIzaSyDlMqMklLecEjG2-3MCzPYIkHtAd4X_ZLs");

        return googleDirectionsUrl.toString();
    }



    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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


    public String formatDecimal(float number) {
        float epsilon = 0.004f; // 4 tenths of a cent
        if (Math.abs(Math.round(number) - number) < epsilon) {
            return String.format("%10.0f", number); // sdb
        } else {
            return String.format("%10.2f", number); // dj_segfault
        }
    }

    private int getTimeDistance(String[] info){


        double priceNum = timeToMinutes(info[0]);
        int duration = timeToMinutes(info[0]);

        return duration;
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
