package com.example.hcmuteforums.data.remote.retrofit;

import android.content.Context;

import com.example.hcmuteforums.data.remote.interceptor.LocalAuthInterceptor;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocalRetrofit {
    private static Retrofit retrofit;
    public static void setInterceptor(){
        OkHttpClient client;
        if (LocalAuthInterceptor.getInstance() != null){
            client = new OkHttpClient().newBuilder()
                    .addInterceptor(LocalAuthInterceptor.getInstance())
                    .build();
        }
        else{
            client = new OkHttpClient().newBuilder()
                    .build();
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("https://ball.io.vn/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(client)
                .build();
    }
    public static Retrofit getRetrofit() {
        if (retrofit == null){
            setInterceptor();
        }
        return retrofit;
    }
}
