package com.example.musicsharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Post> posts;


    public MyAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_post, parent, false);
        return new MyViewHolder(view, (ProfileActivity) parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(posts.get(position).getImage()).into(holder.imageview);

        holder.nameview.setText(posts.get(position).getSongname());
        holder.artistalbumview.setText(posts.get(position).getAlbum_artist());
        holder.captionview.setText(posts.get(position).getCaption());
        holder.locationview.setText(posts.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
