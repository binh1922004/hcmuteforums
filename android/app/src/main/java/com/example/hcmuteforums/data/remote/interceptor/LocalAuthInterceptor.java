package com.example.hcmuteforums.data.remote.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LocalAuthInterceptor implements Interceptor {
    private SharedPreferences sharedPreferences;
    public static LocalAuthInterceptor instance;

    public static void setInstance(Context context){
        if (context != null){
            instance = new LocalAuthInterceptor(context);
        }
    }
    public static LocalAuthInterceptor getInstance(){
        return instance;
    }
    public LocalAuthInterceptor(Context context) {
        this.sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = originalRequest.newBuilder()
                .build();
        String url = originalRequest.url().toString();

        if (url.contains("/login")){
            return chain.proceed(newRequest);
        }
        String token = sharedPreferences.getString("jwtLocal", null);
        if (token == null){
            return chain.proceed(newRequest);
        }
        Log.d("JWT", token);
        Request newRequestJWT = newRequest.newBuilder()
                .headers(newRequest.headers())
                .addHeader("authorization", "Bearer " + token)
                .build();
        return chain.proceed(newRequestJWT);
    }
}
