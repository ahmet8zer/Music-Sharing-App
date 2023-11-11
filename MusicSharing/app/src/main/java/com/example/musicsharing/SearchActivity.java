package com.example.musicsharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private Data songList;

    private RecyclerView mRecyclerView;

    private SongRecyclerAdapter mSongRecyclerAdapter;

    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        songList = new Data();

        Bundle bundle = getIntent().getExtras();
        query = bundle.getString("query");

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSongRecyclerAdapter = new SongRecyclerAdapter(getApplicationContext(), songList, new SongRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Data.Song item) {
                Intent data = new Intent();
                data.putExtra("Song", item.getTitle());
                data.putExtra("Artist", item.getArtist().getName());
                data.putExtra("Url", item.getAlbum().getCover());
                setResult(RESULT_OK, data);
                finish();
            }
        });
        mRecyclerView.setAdapter(mSongRecyclerAdapter);
        RetrofitInterface apiService = RetrofitClient.getClient().create(RetrofitInterface.class);
        Call<Data> call = apiService.getSongs(query);


        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                songList = response.body();
                Log.d("TAG", "Resp="+ new GsonBuilder().setPrettyPrinting().create().toJson(response));
                mSongRecyclerAdapter.setSongs(songList);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }
}