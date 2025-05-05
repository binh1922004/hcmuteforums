package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.request.PasswordUpdateRequest;
import com.example.hcmuteforums.viewmodel.ForgotPassWordViewModel;
import com.example.hcmuteforums.viewmodel.VerifyOTPViewModel;

public class NewPasswordActivity extends AppCompatActivity {
    EditText edtNewPassword, edtConfirmPassword;
    ImageView img_backOTP;
    Button btn_updatePass;
    TextView tv_exit;
    String newPass, confirmPass, otp, email, username;
    VerifyOTPViewModel verifyOTPViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_password);
        anhxa();
        verifyOTPViewModel = new ViewModelProvider(this).get(VerifyOTPViewModel.class);
        eventTogglePasswordVisibility(edtNewPassword,R.drawable.baseline_lock_24, R.drawable.baseline_remove_red_eye_24, R.drawable.icons8_closed_eye_50);
        eventTogglePasswordVisibility(edtConfirmPassword,R.drawable.baseline_lock_open_24, R.drawable.baseline_remove_red_eye_24, R.drawable.icons8_closed_eye_50);
        otp = getIntent().getStringExtra("OTP");
        email = getIntent().getStringExtra("Email");
        username = getIntent().getStringExtra("Username");

        eventBackOTP(img_backOTP);
        eventExit(tv_exit);
        eventUpdatePass(btn_updatePass);
    }
    void anhxa(){
        edtNewPassword = findViewById(R.id.newPassword);
        edtConfirmPassword =findViewById(R.id.confirmNewPassword);
        btn_updatePass = findViewById(R.id.btnResetPassword);
        img_backOTP = findViewById(R.id.imgBackOTP);
        tv_exit = findViewById(R.id.textExit);
    }
    void eventExit(TextView tv_exit){
        tv_exit.setOnClickListener(view -> {
            Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
    void eventBackOTP(ImageView img_backOTP){
        img_backOTP.setOnClickListener(view -> {
            Intent intent = new Intent(NewPasswordActivity.this, ForgotPasswordOTPActivity.class);
            startActivity(intent);
        });
    }
    void eventUpdatePass(Button btn_updatePass){
        btn_updatePass.setOnClickListener(view -> {
            if(validatePass()){
                newPass = edtNewPassword.getText().toString();
                confirmPass = edtConfirmPassword.getText().toString();
                PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest(email, newPass, otp, username);
                verifyOTPViewModel.updatePassword(passwordUpdateRequest);
            }
        });
        verifyOTPViewModel.getUpdatePasswordResponse().observe(this, event -> {
            Boolean isReset = event.getContent();
            if (isReset != null && isReset) {
                Toast.makeText(NewPasswordActivity.this, "Mật khẩu đã được thay đổi thành công!", Toast.LENGTH_SHORT).show();
                eventBackLogin();
            } else {
                Toast.makeText(NewPasswordActivity.this, "Không thể thay đổi mật khẩu. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
        verifyOTPViewModel.getMessageError().observe(this, event -> {
            String message = event.getContent();
            if (message != null) {
                Toast.makeText(NewPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        verifyOTPViewModel.getUpdatePasswordError().observe(this, event -> {
            Boolean errorOccurred = event.getContent();
            if(errorOccurred!=null && errorOccurred){
                Toast.makeText(NewPasswordActivity.this, "Đã có lỗi xảy ra trong quá trình cập nhật mật khẩu mới ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validatePass()
    {
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (newPassword.isEmpty()) {
            edtNewPassword.setError("Vui lòng nhập mật khẩu mới");
            edtNewPassword.requestFocus();
            return false;
        }

        if (newPassword.length() < 6) {
            edtNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edtNewPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            edtConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            edtConfirmPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu không khớp");
            edtConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }


    private void eventTogglePasswordVisibility(EditText editText, int leftIconRes,
                                               int eyeOpenIconRes,
                                               int eyeClosedIconRes){
        editText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editText.getRight()
                        - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    // Kiểm tra nếu đang ở chế độ mật khẩu
                    if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        // Hiển thị mật khẩu
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                leftIconRes,
                                0,
                                eyeOpenIconRes,
                                0
                        );
                    } else {
                        // Ẩn mật khẩu
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                leftIconRes,
                                0,
                                eyeClosedIconRes,
                                0
                        );
                    }

                    // Giữ con trỏ cuối text
                    editText.setSelection(editText.getText().length());
                    return true;
                }
            }

            return false;
        });
    }
    void eventBackLogin(){
        Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}