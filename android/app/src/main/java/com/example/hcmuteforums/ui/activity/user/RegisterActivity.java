package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText edt_fullname, edt_email, edt_username, edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        btn_next = findViewById(R.id.btnNextOTP);
        btn_next.setOnClickListener(v->{
            if(validateInput()) {
                String email = edt_email.getText().toString();
                Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        imgBackLogin = findViewById(R.id.imgBackLogin);
        imgBackLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        tvBackLogin = findViewById(R.id.register_tvBackLogin);
        tvBackLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        edt_fullname = findViewById(R.id.register_edtFullname);
        edt_email = findViewById(R.id.register_edtEmail);
        edt_username = findViewById(R.id.register_edtUsername);
        edt_password = findViewById(R.id.register_edtPassword);

    }
    private boolean validateInput() {
        String fullname = edt_fullname.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String username = edt_username.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        if (fullname.isEmpty()) {
            edt_fullname.setError("Họ tên không được để trống");
            edt_fullname.requestFocus();
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError("Email không hợp lệ");
            edt_email.requestFocus();
            return false;
        }

        if (username.isEmpty()) {
            edt_username.setError("Tên người dùng không được để trống");
            edt_username.requestFocus();
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            edt_password.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edt_password.requestFocus();
            return false;
        }

        return true;
    }
}