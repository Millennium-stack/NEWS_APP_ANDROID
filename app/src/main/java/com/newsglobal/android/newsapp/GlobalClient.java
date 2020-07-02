package com.newsglobal.android.newsapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalClient {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static GlobalClient globalClient;
    private static Retrofit retrofit;

    private GlobalClient()
    {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized GlobalClient getInstance()
    {
        if(globalClient == null)
        {
            globalClient = new GlobalClient();
        }

        return globalClient;
    }

    public GlobalInterface getApi()
    {
        return retrofit.create(GlobalInterface.class);
    }
}
