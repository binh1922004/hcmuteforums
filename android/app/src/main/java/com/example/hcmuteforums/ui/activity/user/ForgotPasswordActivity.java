package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.ui.fragment.LoadingDialogFragment;
import com.example.hcmuteforums.viewmodel.ForgotPassWordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edt_username,  edt_email;
    Button btn_switchOtp;
    TextView tv_backLogin;
    ImageView img_backlogin;
    ForgotPassWordViewModel forgotPassWordViewModel;
    private LoadingDialogFragment loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotPassWordViewModel = new ViewModelProvider(this).get(ForgotPassWordViewModel.class);
        anhxa();
        tv_backLogin.setOnClickListener(view -> {
            eventBackLogin();
        });
        img_backlogin.setOnClickListener(view ->{
            eventBackLogin();
        });
       eventSendOTP(btn_switchOtp);
    }
    void anhxa(){
        edt_username = findViewById(R.id.edt_username);
        edt_email = findViewById(R.id.edt_email);
        btn_switchOtp =findViewById(R.id.btn_switchOTP);
        tv_backLogin = findViewById(R.id.switchLogin);
        img_backlogin = findViewById(R.id.imgBackLogin);
    }
    void eventBackLogin(){
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    private boolean validateInputs() {
        String username = edt_username.getText().toString().trim();
        String email = edt_email.getText().toString().trim();

        if (username.isEmpty()) {
            edt_username.setError("Tên người dùng không được để trống");
            edt_username.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            edt_email.setError("Email không được để trống");
            edt_email.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError("Email không hợp lệ");
            edt_email.requestFocus();
            return false;
        }

        return true;
    }
    String email, username;
    void eventSendOTP(Button btn_switchOtp){
        btn_switchOtp.setOnClickListener(view -> {
            if(validateInputs()){
                email = edt_email.getText().toString();
                username = edt_username.getText().toString();
                forgotPassWordViewModel.sendOtpResetPassword(email, username);
            }

        });
        forgotPassWordViewModel.getSendOtpResetPWResponse().observe(this, event -> {
            Boolean isSent = event.getContent();
            if (isSent != null && isSent) {
                switchOTPActivity();
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Không thể gửi OTP. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
        forgotPassWordViewModel.getMessageError().observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String message = stringEvent.getContent();
                if(message!=null){
                    Toast.makeText(ForgotPasswordActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }
        });
        forgotPassWordViewModel.getSendOtpResetPWError().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                Boolean errorOccurred = booleanEvent.getContent();
                if(errorOccurred!=null && errorOccurred){
                    Toast.makeText(ForgotPasswordActivity.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();

                }
            }
        });
        forgotPassWordViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading){
                    if (loadingDialog == null) {
                        loadingDialog = new LoadingDialogFragment();
                    }
                    loadingDialog.show(getSupportFragmentManager(), "LoadingDiaglog");

                }
                else{
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                }
            }
        });
    }

    private void switchOTPActivity() {
        Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordOTPActivity.class);
        intent.putExtra("Email", email);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

}