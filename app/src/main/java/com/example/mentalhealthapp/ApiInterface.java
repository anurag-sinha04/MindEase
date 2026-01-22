package com.example.mentalhealthapp;

import com.example.mentalhealthapp.model.Headlines;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("everything")
    Call<Headlines> getSpecificData(
            @Query("q") String query,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey
    );
}
