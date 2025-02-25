package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hcmuteforums.R;

public class LoginActivity extends AppCompatActivity {


    private TextView tvRegister;
    private EditText edtUsername, edtpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        tvRegister = findViewById(R.id.tvRegister);
        edtUsername = findViewById(R.id.edtUsername);
        edtpassword = findViewById(R.id.edtPassword);
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
}