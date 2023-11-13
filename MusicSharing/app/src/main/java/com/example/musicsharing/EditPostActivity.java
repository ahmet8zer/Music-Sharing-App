package com.example.musicsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {

    private EditText caption;

    private Button save_button;

    private FirebaseFirestore db;

    private FirebaseUser user;

    private String caption_string;

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        caption_string = bundle.getString("caption");
        position = bundle.getInt("position");

        caption = findViewById(R.id.edit_post_caption);
        caption.setText(caption_string);

        save_button = findViewById(R.id.update_the_post_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update firebase with the new caption

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                    // Delete from Firestore based on position that it loaded them in in
                    db.collection("users").document(user.getUid())
                            .collection("posts")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int i = 0;

                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        if (i==position) {
                                            document.getReference().update("caption", caption.getText().toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // now we know that the operation is done
                                                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            caption_string = caption.getText().toString();
                                                        }
                                                    });
                                        }
                                        i+=1;
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error deleting document", e);
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                }




                //Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                //startActivity(intent);
                //finish();
            }
        });






    }
}