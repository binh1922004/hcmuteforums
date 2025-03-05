package com.example.hcmuteforums.data.remote.retrofit;

import android.content.Context;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocalRetrofit {
    private Retrofit retrofit;
    public LocalRetrofit(){
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        OkHttpClient client = new OkHttpClient().newBuilder()
//                .addInterceptor(new AuthInterceptor(context))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/ute/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(client)
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
