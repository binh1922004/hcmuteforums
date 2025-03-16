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
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(LocalAuthInterceptor.getInstance())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/ute/")
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
