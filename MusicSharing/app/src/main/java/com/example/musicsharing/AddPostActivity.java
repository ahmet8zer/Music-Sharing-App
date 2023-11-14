package com.example.musicsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private AutoCompleteTextView mSongSelect;
    private EditText mCaptionText;

    private Button mPostButton;

    private Button mFindSongButton;

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

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

        mLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (GPSEnabled()){
                    LocationServices.getFusedLocationProviderClient(AddPostActivity.this).requestLocationUpdates(locationRequest, new LocationCallback(){
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);

                            LocationServices.getFusedLocationProviderClient(AddPostActivity.this).removeLocationUpdates(this);

                            if(locationResult != null && locationResult.getLocations().size() > 0){
                                double latitude = locationResult.getLocations().get(locationResult.getLocations().size() - 1).getLatitude();
                                double longitude = locationResult.getLocations().get(locationResult.getLocations().size() - 1).getLongitude();

                                mLocationButton.setText("Added Location: Columbus");
                            }
                        }

                    }, Looper.getMainLooper());

                }else{
                    mLocationButton.setText("Please enable location tracking on your device to use this feature.");
                }
            }
        });

    }

    private boolean GPSEnabled(){
        LocationManager lm = null;
        boolean isEnabled = false;

        if (lm == null){
            lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isEnabled;
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