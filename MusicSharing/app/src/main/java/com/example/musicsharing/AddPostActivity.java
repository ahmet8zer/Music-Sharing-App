package com.example.musicsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        mSongSelect = findViewById(R.id.add_post_song);
        mFindSongButton =(Button) findViewById(R.id.search_song_button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
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