package com.example.luggttra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button SignupButton;
    private Button LoginButton;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        SignupButton = findViewById(R.id.SignupButton);
        LoginButton = findViewById(R.id.LoginButton);

        SignupButton.setVisibility(View.VISIBLE);
        LoginButton.setVisibility(View.VISIBLE);

        if (mAuth.getCurrentUser() != null ) {
            mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>(){

                @Override
                public void onSuccess(Void unused) {
                    currentUser = mAuth.getCurrentUser();
                    if (currentUser != null && currentUser.isEmailVerified()) {
                        System.out.println("Email Verified : " + currentUser.isEmailVerified());

                        Intent MainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(MainActivity);
                        WelcomeActivity.this.finish();
                    }
                }
            });
        }
        else {
            SignupButton.setVisibility(View.VISIBLE);
            LoginButton.setVisibility(View.VISIBLE);
            System.out.println("User is not available");
        }

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignupIntent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(SignupIntent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });
    }
}