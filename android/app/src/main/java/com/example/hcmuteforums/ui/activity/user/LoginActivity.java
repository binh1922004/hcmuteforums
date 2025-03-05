package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.data.remote.api.AuthenticationApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.ApiResponse;
import com.example.hcmuteforums.model.dto.request.AuthenticationRequest;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.utils.ApiErrorHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private TextView tvRegister;
    private EditText edtUsername, edtpassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tvRegister = findViewById(R.id.tvRegister);
        edtUsername = findViewById(R.id.edtUsername);
        edtpassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        //function for event handler
        loginButton();

        tvRegister.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    private boolean validateInput()
    {
        String username = edtUsername.getText().toString().trim();
        String password = edtpassword.getText().toString().trim();
        if(username.isEmpty()){
            edtUsername.setError("Tên đăng nhập không được để trống");
            edtUsername.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            edtpassword.setError("Mật khẩu không được để trống");
            edtpassword.requestFocus();
            return false;
        }
        return true;
    }

    private void loginButton(){
        btnLogin.setOnClickListener(v -> {
            LocalRetrofit retrofit = new LocalRetrofit(this);
            AuthenticationApi authenticationApi = retrofit.getRetrofit().create(AuthenticationApi.class);

            String username = edtUsername.getText().toString().trim();
            String password = edtpassword.getText().toString().trim();
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
            authenticationApi.login(authenticationRequest).enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
                @Override
                public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                    ApiResponse<?> apiResp;
                    if (response.isSuccessful() && response.body() != null){
                        apiResp = response.body();
                    }
                    else{
                        apiResp = ApiErrorHandler.parseError(response);
                    }

                    if (apiResp.getCode() == 200){
                        Toast.makeText(LoginActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, apiResp.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable throwable) {
                    Log.d("Loi", throwable.getMessage());
                    Toast.makeText(LoginActivity.this, "Da co loi xay ra", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}