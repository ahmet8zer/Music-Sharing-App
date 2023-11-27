package com.example.musicsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private AutoCompleteTextView mSongSelect;
    private EditText mCaptionText;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private String mCityName;

    private List<Address> addrs;

    private Button mPostButton;

    private Button mFindSongButton, backbutton;

    private Button mLocationButton;
    private LocationRequest locationRequest;

    private String song;
    private String artist;
    private String cover;

    private FirebaseFirestore db;

    private FirebaseUser user;

    private Map<String, Object> post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mCaptionText = (EditText) findViewById(R.id.add_post_caption);
        mPostButton = (Button) findViewById(R.id.post_the_post_button);
        mLocationButton = (Button) findViewById(R.id.get_location_button);
        mSongSelect = findViewById(R.id.add_post_song);
        mFindSongButton =(Button) findViewById(R.id.search_song_button);
        backbutton = findViewById(R.id.back_button_1);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();


            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add the song and caption to database
                String caption;
                caption = mCaptionText.getText().toString();
                uploadPost(caption, user, db);
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mFindSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                String query = mSongSelect.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currLocation(view);
            }
        });

    }

    private void uploadPost(String caption, @NonNull FirebaseUser user, FirebaseFirestore db){
        post = new HashMap<>();
        post.put("uid", user.getUid());
        if(user.getDisplayName() != null  && !user.getDisplayName().isEmpty()){
            post.put("author", user.getDisplayName());
        }else{
            post.put("author", user.getEmail());
        }
        post.put("title", song);
        post.put("caption", caption);
        post.put("artist", artist);
        post.put("cover", cover);
        if(mCityName != null){
            post.put("city", mCityName);
        }

        db.collection("users").document(user.getUid()).collection("posts").add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddPostActivity.this, "Created Post!",
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                )

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPostActivity.this, "failed to create post :(.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void currLocation(View view){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }else{
            requestLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(0);

        mFusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                new LocationCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                addrs = geocoder.getFromLocation(latitude, longitude, 1);
                                mCityName = addrs.get(0).getLocality();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(
                                    AddPostActivity.this,
                                    "Location added" ,
                                    Toast.LENGTH_SHORT
                            ).show();

                            mFusedLocationProviderClient.removeLocationUpdates(this);
                        }
                    }
                },
                null
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(
                        this,
                        "Location permission denied. Cannot get location.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            if(resultCode == RESULT_OK){
                user = FirebaseAuth.getInstance().getCurrentUser();
                db = FirebaseFirestore.getInstance();
                song = data.getStringExtra("Song");
                artist = data.getStringExtra("Artist");
                cover = data.getStringExtra("Url");
                Toast.makeText(getApplicationContext(), "Clicked on:" + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}