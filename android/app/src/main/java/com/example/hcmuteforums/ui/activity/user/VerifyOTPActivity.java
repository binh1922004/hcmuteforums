package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.chaos.view.PinView;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.request.UserCreationRequest;
import com.example.hcmuteforums.viewmodel.VerifyOTPViewModel;
import com.google.gson.Gson;

public class VerifyOTPActivity extends AppCompatActivity {

    private ImageView imgClose;
    private PinView pinView;
    private TextView tvOtpdescription;
    private Button btnVerifyOtp;

    private VerifyOTPViewModel verifyOTPViewModel;
    UserCreationRequest userRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactivity);
        imgClose = findViewById(R.id.img_Iconclose);
        pinView = findViewById(R.id.pinv_Otpcode);
        tvOtpdescription = findViewById(R.id.otp_description_text);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        //viewmodel
        verifyOTPViewModel = new ViewModelProvider(this).get(VerifyOTPViewModel.class);

        //get data from intent
        String userJson = getIntent().getStringExtra("user_request_json");
        userRequest = new Gson().fromJson(userJson, UserCreationRequest.class);


        if(userRequest.getEmail() != null){
            tvOtpdescription.setText("Mã OTP đã được gửi đến Mail:\n" + userRequest.getEmail());
        }

        //Event
        verifyOtpEvent();
        imgClose.setOnClickListener(view -> {
            pinView.requestFocus();
            Intent intent = new Intent(VerifyOTPActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        });
    }

    private void verifyOtpEvent() {
        btnVerifyOtp.setOnClickListener(v -> {
            if (pinView.getText() != null) {
                String otp = pinView.getText().toString();
                userRequest.setOtp(otp);
                verifyOTPViewModel.register(userRequest);
            }
            else{
                Toast.makeText(this, "Vui lòng nhập đủ OTP", Toast.LENGTH_SHORT).show();
            }
        });

        verifyOTPViewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(VerifyOTPActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        verifyOTPViewModel.getRegisterError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(VerifyOTPActivity.this, "Đã xảy ra lỗi trong quá trình xác thực", Toast.LENGTH_SHORT).show();
            }
        });
        verifyOTPViewModel.getRegisterResponse().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(VerifyOTPActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }
}