package com.example.musicsharing;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitInterface{

    @GET("track")
    Call<Data> getSongs(@Query("q") String query);
}
