package com.example.hcmuteforums;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.hcmuteforums.data.remote.api.AuthenticationApi;
import com.example.hcmuteforums.data.remote.interceptor.LocalAuthInterceptor;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwtLocal", null);

        if (jwt != null && !isTokenValid(jwt)) {
            removeJwt();
        }

        // Khởi tạo Interceptor với Application Context
        LocalAuthInterceptor.setInstance(this);
        // Cấu hình Retrofit với Interceptor đã khởi tạo
        LocalRetrofit.setInterceptor();
    }

    private boolean isTokenValid(String jwt) {
        try {
            AuthenticationApi authenticationApi = LocalRetrofit.getRetrofit().create(AuthenticationApi.class);
            Response<ApiResponse<Boolean>> response = authenticationApi.introspect(jwt).execute(); // Gọi đồng bộ
            if (response.isSuccessful() && response.body() != null) {
                return Boolean.TRUE.equals(response.body().getResult());
            }
        } catch (Exception e) {
            Log.e("CheckJWT", "Lỗi khi kiểm tra JWT", e);
        }
        return false;
    }

    private void removeJwt() {
        Log.d("RemoveJWT", "JWT đã bị xóa");
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("jwtLocal");
        editor.remove("isLoggedIn");
        editor.apply();
    }
}
