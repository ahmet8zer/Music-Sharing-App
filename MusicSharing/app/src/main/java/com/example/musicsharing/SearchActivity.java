package com.example.musicsharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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

        mSongRecyclerAdapter = new SongRecyclerAdapter(getApplicationContext(),songList);
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