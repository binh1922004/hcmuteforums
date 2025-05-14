package com.example.hcmuteforums.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.data.remote.api.AuthenticationApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.ui.activity.user.UserMainActivity;
import com.example.hcmuteforums.utils.LoginPromptDialog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwtLocal", null);
        // Hiển thị ProgressBar
        showLoading();
        if (jwt != null) {
            // Sử dụng ExecutorService để kiểm tra token trong nền
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                if (!isTokenValid(jwt)) {
                    removeJwt();
                }
                else{
                    LoginPromptDialog.isLogged = true;
                }
                // Gọi callback hoặc cập nhật UI ở đây
                onTokenVerificationCompleted();
            });
        }
        else{
            onTokenVerificationCompleted();
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
//        loadingProgressBar.setVisibility(View.GONE);
    }

    private void onTokenVerificationCompleted() {
        // Ẩn ProgressBar
        hideLoading();
        // Chuyển sang Activity tiếp theo hoặc thực hiện các tác vụ khác
        Log.d("TokenCheck", "Token verification completed.");
        // Ví dụ:
         Intent intent = new Intent(this, UserMainActivity.class);
         startActivity(intent);
         finish();
    }
}