package com.example.musicsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class AddPostActivity extends AppCompatActivity {

    private AutoCompleteTextView mSongSelect;
    private EditText mCaptionText;
    private Button mPostButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);



        mAuth = FirebaseAuth.getInstance();
        mCaptionText = (EditText) findViewById(R.id.add_post_caption);
        mPostButton = (Button) findViewById(R.id.post_the_post_button);
        mSongSelect = findViewById(R.id.add_post_song);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add the song and caption to database


                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });








    }
}