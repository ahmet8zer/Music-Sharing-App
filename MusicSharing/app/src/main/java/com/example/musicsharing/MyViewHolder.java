package com.example.musicsharing;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageview;
    TextView nameview, artistalbumview, captionview;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageview = itemView.findViewById(R.id.imageview);
        nameview = itemView.findViewById(R.id.songname);
        artistalbumview = itemView.findViewById(R.id.album_artist);
        captionview = itemView.findViewById(R.id.caption);
    }
}
