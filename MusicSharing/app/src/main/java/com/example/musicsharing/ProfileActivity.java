package com.example.musicsharing;

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


public class ProfileActivity extends AppCompatActivity {

    private Button mEditProfileButton, mDeleteProfileButton, mAddPostButton;

    private TextView mDisplayName;


    private String song;
    private String artist;
    private String cover;

    private FirebaseFirestore db;

    private FirebaseUser user;

    private Map<String, Object> post;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        RecyclerView rv = findViewById(R.id.rvPosts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<Post> posts = new ArrayList<Post>();

        //get the users posts
        db.collection("users").document(user.getUid()).collection("posts").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int size = queryDocumentSnapshots.size();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String uid = document.getString("uid");
                            String author = document.getString("author");
                            String title = document.getString("title");
                            String caption = document.getString("caption");
                            String artist = document.getString("artist");
                            String cover = document.getString("cover");

                            posts.add(new Post(title, artist, caption, cover));
                        }

                        rv.setAdapter(new MyAdapter(getApplicationContext(), posts));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error getting documents.", e);
                    }
                });



        //posts.add(new Post("Hello", "Adele, 25", "I really love this song because...", R.drawable.hello));
        //posts.add(new Post("Some Song", "This is what will happen if it doesn\'t have an image", "small caption", R.drawable.blankrecord));
        //posts.add(new Post("Hello", "Adele, 25", "large caption alkdjflkajsdlfkj asdkjflk asdklfj kals jdflkjalskd flj askdjflk asdjkf klajsdlkfjlksajlkdjf asjldfjk asdjk flkajsdlfjk akl sjdfjalkjs df jaslk djf kjaskldjfkljasdkjflkja sdkj fklajs dkl fjaklj sdfkj askd jfkjljasjflka sjdlkf jaskljd fkjlaskljdfaksjdfkjl asdkljfakj sd fljlkdas", R.drawable.hello));

        //idk why but I think this line doesn't execute
        rv.setAdapter(new MyAdapter(getApplicationContext(), posts));

        mDisplayName = (TextView) findViewById(R.id.display_name);
        if (user != null) {
            mDisplayName.setText(user.getDisplayName());
        }
        mEditProfileButton = (Button) findViewById(R.id.edit_profile_button);

        mDeleteProfileButton = (Button) findViewById(R.id.delete_profile_button);

        mAddPostButton = (Button) findViewById(R.id.add_post_button);


        mEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mDeleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Your account has been deleted.");

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
            }
        });

    }
}
