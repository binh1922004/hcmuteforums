package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hcmuteforums.R;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_next;
    private ImageView imgBackLogin;
    private TextView tvBackLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        btn_next = findViewById(R.id.btnNextOTP);
        btn_next.setOnClickListener(v->{
            Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class);
            startActivity(intent);
        });

        imgBackLogin = findViewById(R.id.imgBackLogin);
        imgBackLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        tvBackLogin = findViewById(R.id.register_tvBackLogin);
        tvBackLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}