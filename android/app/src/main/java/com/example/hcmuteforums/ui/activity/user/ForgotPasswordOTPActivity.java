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
import androidx.lifecycle.ViewModelProvider;

import com.chaos.view.PinView;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.viewmodel.ForgotPassWordViewModel;
import com.example.hcmuteforums.viewmodel.OtpValidateViewModel;

public class ForgotPasswordOTPActivity extends AppCompatActivity {
    ImageView img_backEmail;
    TextView decs;
    PinView pv_otp;
    Button btn_validateOTP;
    OtpValidateViewModel otpValidateViewModel;
    String email, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_forgot_password_otpactivity);
        otpValidateViewModel = new ViewModelProvider(this).get(OtpValidateViewModel.class);
        anhxa();
        eventBackEmail(img_backEmail);
        email = getIntent().getStringExtra("Email");
        username = getIntent().getStringExtra("Username");
        decs.setText("Mã OTP đã được gửi đến Mail:\n" + email);
        eventCheckOTP(btn_validateOTP);
    }
    void anhxa()
    {
        img_backEmail = findViewById(R.id.img_backEmail);
        decs = findViewById(R.id.otp_description_pass);
        pv_otp = findViewById(R.id.password_otpCode);
        btn_validateOTP = findViewById(R.id.confirmOtp);
    }
    void eventBackEmail(ImageView img_backEmail){
        img_backEmail.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordOTPActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }


    private boolean validateOtp() {
        String otpCode = pv_otp.getText().toString().trim();

        if (otpCode.isEmpty()) {
            pv_otp.setError("Vui lòng nhập mã OTP");
            pv_otp.requestFocus();
            return false;
        }

        if (otpCode.length() < 6) {
            pv_otp.setError("Mã OTP phải gồm 6 chữ số");
            pv_otp.requestFocus();
            return false;
        }

        if (!otpCode.matches("\\d{6}")) {
            pv_otp.setError("Mã OTP không hợp lệ");
            pv_otp.requestFocus();
            return false;
        }

        return true;
    }
    String otp;
    void eventCheckOTP(Button btn_validateOTP){
        btn_validateOTP.setOnClickListener(view -> {
            if(validateOtp()){
                otp = pv_otp.getText().toString();
                otpValidateViewModel.validateOtp(email, otp);
            }
        });
        otpValidateViewModel.getOtpValidatedResponse().observe(this, event -> {
            Boolean isValid = event.getContent();
            if (isValid != null && isValid) {
                eventSwitchChangePass();
                Toast.makeText(ForgotPasswordOTPActivity.this, "OTP hợp lệ hãy nhập mật khẩu mới", Toast.LENGTH_SHORT).show();

            }
        });
        otpValidateViewModel.getOtpValidatedError().observe(this, event -> {
            Boolean isError = event.getContent();
            if (isError != null && isError) {
                Toast.makeText(ForgotPasswordOTPActivity.this, "OTP không hợp lệ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        otpValidateViewModel.getMessageError().observe(this, event -> {
            String message = event.getContent();
            if (message != null) {
                Toast.makeText(ForgotPasswordOTPActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
    void eventSwitchChangePass()
    {
        Intent intent = new Intent(ForgotPasswordOTPActivity.this, NewPasswordActivity.class);
        intent.putExtra("Email", email);
        intent.putExtra("OTP", otp);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

}