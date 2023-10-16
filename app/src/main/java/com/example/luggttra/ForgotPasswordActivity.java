package com.example.luggttra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private final static String TAG = "ForgotPasswordActivity";
    private FirebaseAuth mAuth;
    private Button resetPasswordButton;
    private EditText EmailTextInput;
    private TextView ErrorView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EmailTextInput = findViewById(R.id.ResetEmailTextInput);
        resetPasswordButton = findViewById(R.id.ResetPasswordButton);
        mAuth = FirebaseAuth.getInstance();
        ErrorView3 = findViewById(R.id.ErrorView3);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EmailTextInput.getText().toString().contentEquals("")) {
                    ErrorView3.setText("Email cannot be empty !");
                }
                else {
                    mAuth.sendPasswordResetEmail(EmailTextInput.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) Log.d(TAG, "Email sent !");
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            ForgotPasswordActivity.this);

                                    alertDialogBuilder.setTitle("Reset Password");

                                    alertDialogBuilder
                                            .setMessage("A Reset password link has been sent to your email!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    ForgotPasswordActivity.this.finish();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


                                }
                            });
                }

            }
        });
    }
}