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

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {

    private Button mEditProfileButton, mDeleteProfileButton, mAddPostButton;

    private TextView mDisplayName;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView rv = findViewById(R.id.rvPosts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<Post> posts = new ArrayList<Post>();
        posts.add(new Post("Hello", "Adele, 25", "I really love this song because...", R.drawable.hello));
        posts.add(new Post("Some Song", "This is what will happen if it doesn\'t have an image", "small caption", R.drawable.blankrecord));
        posts.add(new Post("Hello", "Adele, 25", "large caption alkdjflkajsdlfkj asdkjflk asdklfj kals jdflkjalskd flj askdjflk asdjkf klajsdlkfjlksajlkdjf asjldfjk asdjk flkajsdlfjk akl sjdfjalkjs df jaslk djf kjaskldjfkljasdkjflkja sdkj fklajs dkl fjaklj sdfkj askd jfkjljasjflka sjdlkf jaskljd fkjlaskljdfaksjdfkjl asdkljfakj sd fljlkdas", R.drawable.hello));

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
