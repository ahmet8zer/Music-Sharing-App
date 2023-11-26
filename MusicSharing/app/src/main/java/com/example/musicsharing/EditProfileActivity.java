package com.example.musicsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity {

    private Button mSaveEditButton, backbutton;

    private EditText mEditText;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mSaveEditButton = (Button) findViewById(R.id.save_edit_profile_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mEditText = (EditText) findViewById(R.id.edit_bio_field);
        backbutton = findViewById(R.id.back_button);

        if(user != null){
            mEditText.setText(user.getDisplayName());
        }

        mSaveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = "";

                name = String.valueOf(mEditText.getText());

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Saved Changes!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });


            }
        });





        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();


            }
        });






    }




}