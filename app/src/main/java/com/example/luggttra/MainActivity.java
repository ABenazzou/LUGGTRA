package com.example.luggttra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private FirebaseAuth mAuth;
    private String UserId;
    private ListView Luggage;
    private Button Logout;
    private Button addLuggage;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Luggage = (ListView) findViewById(R.id.ListView);

        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getUid();
        email = findViewById(R.id.PlaceHolder);
        email.setText(mAuth.getCurrentUser().getEmail());


        Logout = (Button) findViewById(R.id.Logout);
        addLuggage = (Button) findViewById(R.id.AddLuggageButton);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("items/" + UserId);
       // String[] items = {"Luggage1", "Luggage2", "Luggage3"};
        // set instead of add directly
        addLuggage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrActivity = new Intent(MainActivity.this, QRScannerActivity.class);
                startActivity(qrActivity);
            }
        });
      //  myRef.setValue(Arrays.asList(items));
        //HashMap<String, String> hashMap = new HashMap<>();
        //hashMap.put("location", "Loc2");
       // myRef.push().setValue(hashMap);

        // Read from the database
        ArrayList<String> Locations = new ArrayList<>();
        ArrayList<String> Longitudes = new ArrayList<>();
        ArrayList<String> Latitudes = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {//reg of msg
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //  String value = dataSnapshot.getValue(String.class);
                //for (DataSnapshot data: dataSnapshot.getChildren()) {//each luggage get the info
                //  String Location = data.getValue().toString();
                // Locations.add(Location);
                //
                //
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String location = ds.child("location").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    if (!Locations.contains(name + " at: " + location)) {
                        Locations.add(name + " at: " + location);
                        Longitudes.add(ds.child("lon").getValue(String.class));
                        Latitudes.add(ds.child("lat").getValue(String.class));
                    }
                    //hashMap.put("name", "Luggage ID:" + String.valueOf(new Random(100000000)));
                    // hashMap.put("lat", lat);
                    // hashMap.put("lon", lon);
                    //Log.d("TAG", msg);
                }
                // Locations.add(dataSnapshot.toString());
                // Locations.add(dataSnapshot.getValue().toString());
                //  String[] locs = Locations.toArray(new String[0]);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, Locations);
                Luggage.setAdapter(adapter);
                Log.d(TAG, "Value is: " + Locations.toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Luggage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent map = new Intent(MainActivity.this, MapsActivity.class);
                map.putExtra("lat", Latitudes.get(i));
                map.putExtra("lon", Longitudes.get(i));
                startActivity(map);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

    }
}