package com.example.hcmuteforums;

import android.app.Application;

import com.example.hcmuteforums.data.remote.interceptor.LocalAuthInterceptor;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo Interceptor với Application Context
        LocalAuthInterceptor.setInstance(this);

        // Cấu hình Retrofit với Interceptor đã khởi tạo
        LocalRetrofit.setInterceptor();
    }
}
