package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hcmuteforums.R;

public class VerifyOTPActivity extends AppCompatActivity {

    private ImageView imgClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactivity);
        imgClose = findViewById(R.id.img_Iconclose);
        imgClose.setOnClickListener(view -> {
            Intent intent = new Intent(VerifyOTPActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }
}