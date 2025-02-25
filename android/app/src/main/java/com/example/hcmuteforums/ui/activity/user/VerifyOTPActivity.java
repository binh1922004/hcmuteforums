package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.example.hcmuteforums.R;

public class VerifyOTPActivity extends AppCompatActivity {

    private ImageView imgClose;
    private PinView pinView;
    private TextView tvOtpdescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactivity);
        imgClose = findViewById(R.id.img_Iconclose);
        pinView = findViewById(R.id.pinv_Otpcode);
        tvOtpdescription = findViewById(R.id.otp_description_text);
        String email = getIntent().getStringExtra("Email");
        if(email != null){
            tvOtpdescription.setText("Mã OTP đã được gửi đến Mail:\n" + email);
        }
        imgClose.setOnClickListener(view -> {
            pinView.requestFocus();
            Intent intent = new Intent(VerifyOTPActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        });
    }
}