package com.example.gaffneca.fypfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginRegister extends AppCompatActivity {

    private Button register;
    private Button login;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){


                    Intent intent = new Intent(LoginRegister.this,MapActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    LoginRegister.this.startActivity(intent);
                }

            }
        };




        register = findViewById(R.id.register);
        login = findViewById(R.id.login);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent JRIntent = new Intent(LoginRegister.this, Register.class);
                LoginRegister.this.startActivity(JRIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent JRIntent = new Intent(LoginRegister.this, MainActivity.class);
                LoginRegister.this.startActivity(JRIntent);
            }
        });
    }
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }
}
