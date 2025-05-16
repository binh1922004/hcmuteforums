package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.data.remote.interceptor.LocalAuthInterceptor;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.response.AuthenticationResponse;
import com.example.hcmuteforums.utils.LoginPromptDialog;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;

public class LoginActivity extends AppCompatActivity {


    private TextView tvRegister, tvForgotPass;
    private EditText edtUsername, edtpassword;
    private Button btnLogin;

    private AuthenticationViewModel authenticationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        anhxa();
        //
        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        //function for event handler
        loginButton();
        eventTogglePasswordVisibility(edtpassword,R.drawable.baseline_lock_24, R.drawable.baseline_remove_red_eye_24, R.drawable.icons8_closed_eye_50);

        tvRegister.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        eventForgotPass(tvForgotPass);
    }
    void anhxa()
    {
        tvRegister = findViewById(R.id.tvRegister);
        edtUsername = findViewById(R.id.edtUsername);
        edtpassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPass = findViewById(R.id.tvForgotPass);
    }
    void eventForgotPass(TextView tvForgotPass)
    {
        tvForgotPass.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
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

    private void loginButton(){
        btnLogin.setOnClickListener(v -> {
            if (validateInput()){
                String username = edtUsername.getText().toString().trim();
                String password = edtpassword.getText().toString().trim();
                authenticationViewModel.login(username, password);
            }
        });

        authenticationViewModel.getLoginResponse().observe(this, new Observer<Event<AuthenticationResponse>>() {
            @Override
            public void onChanged(Event<AuthenticationResponse> event) {
                AuthenticationResponse response = event.getContent(); // Lấy giá trị chưa được xử lý
                if (response != null) {
                    SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE); //Set danh dau dang nhap
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", response.getUsername()); // Lưu username
                    editor.putString("userId", response.getId()); // Lưu username
                    editor.putString("email", response.getEmail());
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    //Gán JWT sau khi login vào interceptor
                    editor.putString("jwtLocal", response.getToken());
                    editor.apply();
                    LocalAuthInterceptor.setInstance(LoginActivity.this);
                    LocalRetrofit.setInterceptor();
                    LoginPromptDialog.isLogged = true;

                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    startActivity(intent);
                }
            }
        });

        authenticationViewModel.getLoginError().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean isError = event.getContent(); // Lấy giá trị chưa được xử lý
                if (isError != null && isError) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}