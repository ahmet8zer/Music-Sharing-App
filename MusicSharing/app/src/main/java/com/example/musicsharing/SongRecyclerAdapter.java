package com.example.musicsharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.ViewHolder> {
    Context mContext;

    private Data songs;



    public SongRecyclerAdapter(Context context, Data songs){
        this.mContext = context;
        this.songs = songs;
    }
    public void setSongs(Data songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongRecyclerAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(songs.getSongs().get(position).getTitle().toString());
        holder.tvArtist.setText(songs.getSongs().get(position).getArtist().getName().toString());
        Glide.with(mContext).load(songs.getSongs().get(position).getAlbum().getCover()).apply(RequestOptions.centerCropTransform()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if(songs != null){
            return songs.getSongs().size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        ImageView mImageView;

        TextView tvArtist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            mImageView = (ImageView) itemView.findViewById(R.id.cover);
            tvArtist = (TextView) itemView.findViewById(R.id.artist);
        }
    }
}
