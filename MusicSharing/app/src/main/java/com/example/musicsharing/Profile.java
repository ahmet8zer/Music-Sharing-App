package com.example.musicsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Profile extends AppCompatActivity {

    private Button mEditProfileButton, mDeleteProfileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mEditProfileButton = (Button) findViewById(R.id.edit_profile_button);

        mDeleteProfileButton = (Button) findViewById(R.id.delete_profile_button);

        mEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
                finish();
            }
        });








    }
}