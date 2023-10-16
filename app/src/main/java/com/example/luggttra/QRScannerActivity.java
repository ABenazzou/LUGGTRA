package com.example.luggttra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Random;

import okhttp3.HttpUrl;

public class QRScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private final static String TAG = "QRActivity";
    private FirebaseAuth mAuth;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        mAuth = FirebaseAuth.getInstance();
        String UserId = mAuth.getUid();
        back = findViewById(R.id.goBack);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("items/" + UserId);



        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, result.getText());
                        HttpUrl url = HttpUrl.parse(result.getText());
                        String name = url.queryParameter("name");
                        String lat = url.queryParameter("lat");
                        String lon = url.queryParameter("lon");
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("location", name);
                        Random rand = new Random();
                        hashMap.put("name", "Luggage ID:" + String.valueOf(rand.nextInt(1000000) + 1));
                        hashMap.put("lat", lat);
                        hashMap.put("lon", lon);
                        myRef.push().setValue(hashMap);

                        Intent main = new Intent(QRScannerActivity.this, MapsActivity.class);
                        main.putExtra("lat", lat);
                        main.putExtra("lon", lon);
                        startActivity(main);
                        Log.d(TAG, name);
                        //Toast.makeText(QRScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRScannerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();


    }
}