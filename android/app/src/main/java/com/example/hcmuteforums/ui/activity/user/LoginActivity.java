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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.data.remote.api.AuthenticationApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.model.ApiResponse;
import com.example.hcmuteforums.model.dto.request.AuthenticationRequest;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.utils.ApiErrorHandler;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private TextView tvRegister;
    private EditText edtUsername, edtpassword;
    private Button btnLogin;

    private AuthenticationViewModel authenticationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tvRegister = findViewById(R.id.tvRegister);
        edtUsername = findViewById(R.id.edtUsername);
        edtpassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        //
        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

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
            if (validateInput()){
                String username = edtUsername.getText().toString().trim();
                String password = edtpassword.getText().toString().trim();
                authenticationViewModel.login(username, password);
            }
        });

        authenticationViewModel.getLoginResponse().observe(this, new Observer<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onChanged(ApiResponse<AuthenticationResponse> response) {
                if (response != null) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        authenticationViewModel.getLoginError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if (isError) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });    }
}