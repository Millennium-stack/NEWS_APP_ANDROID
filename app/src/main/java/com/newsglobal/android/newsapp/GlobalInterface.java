package com.newsglobal.android.newsapp;

import com.newsglobal.android.newsapp.Models.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GlobalInterface {

    @GET("top-headlines")
    Call<Headlines> getHeadlines(@Query("country") String country, @Query("category") String category, @Query("apiKey") String apiKey);
}