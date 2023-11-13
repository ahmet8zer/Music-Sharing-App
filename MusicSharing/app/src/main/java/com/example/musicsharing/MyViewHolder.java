package com.example.musicsharing;

import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageview;
    TextView nameview, artistalbumview, captionview;

    private FirebaseFirestore db;

    private FirebaseUser user;

    private ProfileActivity profileActivity;
    Button threedots;
    Context context;

    public MyViewHolder(@NonNull View itemView, ProfileActivity profileActivity) {
        super(itemView);
        this.profileActivity = profileActivity;
        this.context = itemView.getContext();
        imageview = itemView.findViewById(R.id.imageview);
        nameview = itemView.findViewById(R.id.songname);
        artistalbumview = itemView.findViewById(R.id.album_artist);
        captionview = itemView.findViewById(R.id.caption);
        threedots = itemView.findViewById(R.id.three_dots_button);

        threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showPopupMenu(threedots, position);
                }

            }
        });

    }


    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.edit_or_delete, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_edit) {
                    startEditActivity(position);
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    showDeleteConfirmationDialog(position);
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();
    }

    private void startEditActivity(int position) {
        // go to the new activity and pass the current caption to it
        String caption = (String) captionview.getText();
        profileActivity.onEditClick( position, caption);

    }

    private void showDeleteConfirmationDialog(int position) {
        // delete the post and reload the activity
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
                                    document.getReference().delete();
                                }
                                i+=1;
                            }
                            profileActivity.onDeleteClick();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error deleting document", e);
                        }
                    });

        }


    }



    public interface OnItemClickListener {
        void onEditClick(int position, String caption);
        void onDeleteClick();
    }




}



