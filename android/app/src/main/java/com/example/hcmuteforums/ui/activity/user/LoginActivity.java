package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.data.remote.interceptor.LocalAuthInterceptor;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.utils.LoginPromptDialog;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;

public class LoginActivity extends AppCompatActivity {


    private TextView tvRegister, tvForgotPass;
    private EditText edtUsername, edtpassword;
    private Button btnLogin;

    private AuthenticationViewModel authenticationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        anhxa();
        //
        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        //function for event handler
        loginButton();

        tvRegister.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        eventForgotPass(tvForgotPass);
    }
    void anhxa()
    {
        tvRegister = findViewById(R.id.tvRegister);
        edtUsername = findViewById(R.id.edtUsername);
        edtpassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPass = findViewById(R.id.tvForgotPass);
    }
    void eventForgotPass(TextView tvForgotPass)
    {
        tvForgotPass.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
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

        authenticationViewModel.getLoginResponse().observe(this, new Observer<Event<AuthenticationResponse>>() {
            @Override
            public void onChanged(Event<AuthenticationResponse> event) {
                AuthenticationResponse response = event.getContent(); // Lấy giá trị chưa được xử lý
                if (response != null) {
                    SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE); //Set danh dau dang nhap
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", response.getUsername()); // Lưu username
                    editor.putString("userId", response.getId()); // Lưu username
                    editor.putString("email", response.getEmail());
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    //Gán JWT sau khi login vào interceptor
                    editor.putString("jwtLocal", response.getToken());
                    editor.apply();
                    LocalAuthInterceptor.setInstance(LoginActivity.this);
                    LocalRetrofit.setInterceptor();
                    LoginPromptDialog.isLogged = true;

                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    startActivity(intent);
                }
            }
        });

        authenticationViewModel.getLoginError().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean isError = event.getContent(); // Lấy giá trị chưa được xử lý
                if (isError != null && isError) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}