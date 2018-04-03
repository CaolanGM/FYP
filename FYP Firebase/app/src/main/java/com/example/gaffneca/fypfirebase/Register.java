package com.example.gaffneca.fypfirebase;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProg;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etAge;
    private EditText etName;
    private EditText etUsername;
    private EditText etPassword;
    private Button bRegister;
    private TextView loginLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etAge = (EditText) findViewById(R.id.etAge);
        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        loginLink = findViewById(R.id.loginlink);

        mAuth = FirebaseAuth.getInstance();
        mProg = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("New Users");

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){



                startRegister();



            }


        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Register.this, MainActivity.class);
                Register.this.startActivity(loginIntent);
            }
        });

    }

    private void startRegister() {

        String username = etUsername.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        final String age = etAge.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){

            mProg.setMessage("Signing Up...");
            mProg.show();

            mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
               @Override
                public void onComplete(@NonNull Task<AuthResult> task){

                   if(task.isSuccessful()){

                       String user_id = mAuth.getCurrentUser().getUid();
                       DatabaseReference currentUser = mDatabase.child(user_id);
                      currentUser.child("Name").setValue(name);
                      currentUser.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/fyp-firebase-e6552.appspot.com/o/Photos%2Fno%20profile.png?alt=media&token=ed4e855f-5830-4131-bac5-14655fa4b8b2");
                      currentUser.child("Age").setValue(age);

                       mProg.dismiss();

                       Intent intent = new Intent(Register.this,HomeScreen.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                   }
                   else
                   {
                       Toast.makeText(Register.this, "Password is too short", Toast.LENGTH_LONG).show();

                   }

               }


            });
        }
    }


}
