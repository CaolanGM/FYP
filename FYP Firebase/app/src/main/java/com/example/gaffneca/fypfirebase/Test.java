package com.example.gaffneca.fypfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Test extends AppCompatActivity {

    private Button ViewMerges;
    private TextView tvMerge;
    private TextView tvYes;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String testS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //testS ="Hola";
        setContentView(R.layout.activity_jr__list);
        tvMerge = (TextView) findViewById(R.id.textView11);
        //tvYes = (TextView) findViewById(R.id.tvYes);
        ViewMerges = (Button) findViewById(R.id.bttest);
        final LinearLayout mylinlay = (LinearLayout) findViewById(R.id.linlay);
        final String test = null;



//        mAuth= FirebaseAuth.getInstance();
//        String user_id = mAuth.getCurrentUser().getUid();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("New Users").child(user_id).child("Name");
//        final Button[] buts = new Button[5];
//        final Button temp;
//
//        //doSomething();
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                tvMerge.setText("no way jose");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//        tvMerge.setText("no way");





        DatabaseReference testDB = FirebaseDatabase.getInstance().getReference().child("Merge Proposals").child("ODICNVSDIVBH");
        testDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String testS = dataSnapshot.getValue().toString();
                if(testS.contains("sdfgrger"))
                {
                    Toast.makeText(Test.this, "Whammy!", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(Test.this, testS, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        
    }

}
