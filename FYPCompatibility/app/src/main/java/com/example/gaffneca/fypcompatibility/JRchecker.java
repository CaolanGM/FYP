package com.example.gaffneca.fypcompatibility;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JRchecker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jrchecker);


        Intent intent = getIntent();
        ArrayList<String> areas = intent.getStringArrayListExtra("areas");
        final DatabaseReference md = FirebaseDatabase.getInstance().getReference().child("MergingAreas");


        for (int i = 0; i < areas.size(); i++) {

            String key = areas.get(i);




        }
    }

}
