package com.example.musicsharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageview.setImageResource(posts.get(position).getImage());

        holder.nameview.setText(posts.get(position).getSongname());
        holder.artistalbumview.setText(posts.get(position).getAlbum_artist());
        holder.captionview.setText(posts.get(position).getCaption());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
