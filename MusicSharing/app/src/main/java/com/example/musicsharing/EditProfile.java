package com.example.musicsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {

    private Button mSaveEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mSaveEditButton = (Button) findViewById(R.id.save_edit_profile_button);

        mSaveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Saved Changes!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });






    }




}