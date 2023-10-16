package com.example.luggttra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    private Button SignupButton;
    private EditText emailTextInput;
    private EditText passwordTextInput;
    private TextView ErrorView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        emailTextInput = findViewById(R.id.ResetEmailTextInput);
        passwordTextInput = findViewById(R.id.LoginPasswordTextInput);//add password confirm if enough time
        SignupButton = findViewById(R.id.SignupConfirmButton);
        ErrorView = findViewById(R.id.ErrorView);

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(emailTextInput.getText().toString().contentEquals("")) {
                    ErrorView.setText("Email Cannot be Empty!");
                }
                else if (passwordTextInput.getText().toString().contentEquals("")) {
                    ErrorView.setText("Password Cannot be Empty!");
                }
                else {
                    mAuth.createUserWithEmailAndPassword(emailTextInput.getText().toString(),
                            passwordTextInput.getText().toString())
                            .addOnCompleteListener(SignupActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "createUserWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                try {
                                                    if (user != null) {
                                                        user.sendEmailVerification()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()) {
                                                                            Log.d(TAG, "Email Sent!");
                                                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
                                                                            alertDialogBuilder.setTitle("Please Verify Your Email!");

                                                                            alertDialogBuilder
                                                                                    .setMessage("A verification link has been sent to your Email, please verify your email and" +
                                                                                            "Login again!")
                                                                                    .setCancelable(false)
                                                                                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                                            Intent LoginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                                                                            startActivity(LoginIntent);
                                                                                            SignupActivity.this.finish();
                                                                                        }
                                                                                    });
                                                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                                                            alertDialog.show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                                catch (Exception e) {
                                                    ErrorView.setText(e.getMessage());
                                                }

                                            }
                                            else {
                                                //sign in failed
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(SignupActivity.this, "Authentication failed !",
                                                        Toast.LENGTH_SHORT).show();
                                                if (task.getException() != null) {
                                                    ErrorView.setText(task.getException().getMessage());
                                                }

                                            }
                                        }
                                    });
                }
            }
        });
    }

}