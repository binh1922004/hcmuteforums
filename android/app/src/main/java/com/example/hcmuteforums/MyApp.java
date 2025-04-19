package com.example.hcmuteforums;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hcmuteforums.data.remote.api.AuthenticationApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiResponse;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {
    private ProgressBar loadingProgressBar;
    @Override
    public void onCreate() {
        super.onCreate();

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwtLocal", null);

        if (jwt != null) {
            // Hiển thị ProgressBar
            showLoading();

            // Sử dụng ExecutorService để kiểm tra token trong nền
            ExecutorService executor = Executors.newSingleThreadExecutor();

            executor.execute(() -> {
                if (!isTokenValid(jwt)) {
                    removeJwt();
                }
                // Gọi callback hoặc cập nhật UI ở đây
                onTokenVerificationCompleted();
            });
        }
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
    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void onTokenVerificationCompleted() {
        // Ẩn ProgressBar
        hideLoading();
        // Chuyển sang Activity tiếp theo hoặc thực hiện các tác vụ khác
        Log.d("TokenCheck", "Token verification completed.");
        // Ví dụ:
        // Intent intent = new Intent(this, NextActivity.class);
        // startActivity(intent);
        // finish();
    }
}
