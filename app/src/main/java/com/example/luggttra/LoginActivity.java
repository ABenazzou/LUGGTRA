package com.example.luggttra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private EditText EmailTextInput;
    private EditText PasswordTextInput;
    private Button LoginButton;
    private Button ForgotPasswordButton;
    private TextView ErrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EmailTextInput = findViewById(R.id.ResetEmailTextInput);
        PasswordTextInput = findViewById(R.id.LoginPasswordTextInput);
        LoginButton = findViewById(R.id.LoginConfirmButton);
        ForgotPasswordButton = findViewById(R.id.forgotPassword);
        ErrorView = findViewById(R.id.errorView2);

        mAuth = FirebaseAuth.getInstance();

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EmailTextInput.getText().toString().contentEquals("")) {
                    ErrorView.setText("Email cannot be Empty !");
                }
                else if (PasswordTextInput.getText().toString().contentEquals("")) {
                    ErrorView.setText("Password cannot be Empty !");
                }
                else {
                    mAuth.signInWithEmailAndPassword(EmailTextInput.getText().toString(), PasswordTextInput.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "loginWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            if (user.isEmailVerified()) {
                                                System.out.println("Email Verified : " + user.isEmailVerified());
                                                Intent HomeActivity = new Intent(LoginActivity.this, MainActivity.class);
                                                setResult(RESULT_OK, null);
                                                startActivity(HomeActivity);
                                                LoginActivity.this.finish();
                                            } else {
                                                ErrorView.setText("Please Verify your Email before trying to Login");

                                            }
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "loginWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed!",
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

        ForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPwActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPwActivity);
                LoginActivity.this.finish();
            }
        });
    }
}