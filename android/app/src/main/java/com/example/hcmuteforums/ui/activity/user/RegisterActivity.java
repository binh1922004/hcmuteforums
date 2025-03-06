package com.example.hcmuteforums.ui.activity.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.hcmuteforums.viewmodel.RegisterViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_next;
    private ImageView imgBackLogin;
    private TextView tvBackLogin;
    private EditText edt_fullname, edt_email, edt_username, edt_password, etDateOfBirth;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale ,rbOther;


    RegisterViewModel registerViewModel;

    private final Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //mapping
        setContentView(R.layout.activity_register);
        btn_next = findViewById(R.id.btnNextOTP);
        imgBackLogin = findViewById(R.id.imgBackLogin);
        edt_fullname = findViewById(R.id.register_edtFullname);
        edt_email = findViewById(R.id.register_edtEmail);
        edt_username = findViewById(R.id.register_edtUsername);
        edt_password = findViewById(R.id.register_edtPassword);
        etDateOfBirth = findViewById(R.id.register_etDOB);
        etDateOfBirth.setOnClickListener(v -> showDatePickerDialog());
        rgGender = findViewById(R.id.rg_gender);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        //viewmodel
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        //event handler
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

        registerEvent();

    }

    private void registerEvent() {
        btn_next.setOnClickListener(v->{
            if(validateInput()) {
                String email = edt_email.getText().toString();
                String username = edt_username.getText().toString();
                registerViewModel.sendOtp(email, username);
            }
        });

        registerViewModel.getSendOtpResponse().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSent) {
                Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class);
                intent.putExtra("Email", "binh123");
                startActivity(intent);
            }
        });

        registerViewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.getSendOtpError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(RegisterActivity.this, "Da co loi xay ra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput() {
        String fullname = edt_fullname.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String username = edt_username.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String gender = getSelectedGender();

        if (fullname.isEmpty()) {
            edt_fullname.setError("Họ tên không được để trống");
            edt_fullname.requestFocus();
            return false;
        }if (dateOfBirth.isEmpty()) {
            etDateOfBirth.setError("Ngày sinh không được để trống");
            etDateOfBirth.requestFocus();
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
        if (gender.isEmpty()) {
            return false;
        }

        return true;
    }
    private void showDatePickerDialog() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            etDateOfBirth.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    // Hàm lấy giới tính từ RadioGroup
    private String getSelectedGender() {
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId == R.id.rb_male) {
            return "Nam";
        } else if (selectedId == R.id.rb_female) {
            return "Nữ";
        }
        return "Khác";
    }
}