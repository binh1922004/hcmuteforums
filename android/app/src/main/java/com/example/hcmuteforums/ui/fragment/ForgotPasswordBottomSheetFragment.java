package com.example.hcmuteforums.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.request.PasswordUpdateRequest;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.example.hcmuteforums.viewmodel.ForgotPassWordViewModel;
import com.example.hcmuteforums.viewmodel.OtpValidateViewModel;
import com.example.hcmuteforums.viewmodel.VerifyOTPViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public static final String ARG_USER = "user";

    public static ForgotPasswordBottomSheetFragment newInstance(UserResponse user)
    {
        ForgotPasswordBottomSheetFragment fragment = new ForgotPasswordBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public ForgotPasswordBottomSheetFragment() {
        // Required empty public constructor
    }

    ImageView imgClose;
    EditText edt_mail;
    Button btn_switchOtp;
    private UserResponse user;
    private LoadingDialogFragment loadingDialog;
    PasswordUpdateRequest passwordUpdateRequest;

    ForgotPassWordViewModel forgotPassWordViewModel;
    VerifyOTPViewModel verifyOTPViewModel;
    OtpValidateViewModel otpValidateViewModel;

    String email, newPassword, otp, username;

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);

                // Đặt chiều cao tối đa là 80% màn hình
                parent.getLayoutParams().height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
                parent.requestLayout();

                // Đặt BottomSheetDialog ở trạng thái mở rộng (EXPANDED)
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    public static ForgotPasswordBottomSheetFragment newInstance(String param1, String param2) {
        ForgotPasswordBottomSheetFragment fragment = new ForgotPasswordBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            user = (UserResponse) getArguments().getSerializable(ARG_USER); // Lấy user
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password_bottom_sheet, container, false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ForgotPassWordViewModel
        forgotPassWordViewModel = new ViewModelProvider(this).get(ForgotPassWordViewModel.class);
        verifyOTPViewModel = new ViewModelProvider(this).get(VerifyOTPViewModel.class);
        otpValidateViewModel = new ViewModelProvider(this).get(OtpValidateViewModel.class);
        switchToEmailLayout();
    }


    private void switchToEmailLayout() {
        View view = getView();
        if (view == null) return;

        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        if (parent == null) return;

        parent.removeAllViews();

        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_change_password_bottom_sheet, parent, true);
        edt_mail = emailView.findViewById(R.id.edtEmail);
        btn_switchOtp = emailView.findViewById(R.id.getOtp);

        ImageView imageClose = view.findViewById(R.id.imgClose);
        imageClose.setOnClickListener(v ->{
            dismiss();
        });

        btn_switchOtp.setOnClickListener(v -> {
            email = edt_mail.getText().toString();
            username = user.getUsername();
            forgotPassWordViewModel.sendOtpResetPassword(email, username);
        });

        forgotPassWordViewModel.getSendOtpResetPWResponse().observe(getViewLifecycleOwner(), event -> {
            Boolean isSent = event.getContent();
            if (isSent != null && isSent) {
                switchToOtpLayout(); // Chuyển sang màn hình OTP
            } else {
                Toast.makeText(getContext(), "Không thể gửi OTP. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
        forgotPassWordViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String message = stringEvent.getContent();
                if(message!=null){
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                }
            }
        });
        forgotPassWordViewModel.getSendOtpResetPWError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                Boolean errorOccurred = booleanEvent.getContent();
                if(errorOccurred!=null && errorOccurred){
                    Toast.makeText(getContext(), "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();

                }
            }
        });
        forgotPassWordViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading){
                    if (loadingDialog == null) {
                        loadingDialog = new LoadingDialogFragment();
                    }
                    loadingDialog.show(requireActivity().getSupportFragmentManager(), "LoadingDiaglog");

                }
                else{
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                }
            }
        });


    }

    private void switchToOtpLayout() {
        View view = getView();
        if (view == null) return;

        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        parent.removeAllViews();

        View otpView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_otp_password_bottom_sheet, parent, true);
        PinView edtOtp = otpView.findViewById(R.id.password_otpCode);
        Button btn_verifyOtp = otpView.findViewById(R.id.confirmOtp);
        ImageView imgBack = view.findViewById(R.id.img_backEmail);
        imgBack.setOnClickListener(v->{
            switchToEmailLayout();
        });
        TextView desOTP = otpView.findViewById(R.id.otp_description_pass);
        desOTP.setText("Mã OTP đã được gửi đến Mail:\n" + email);


        btn_verifyOtp.setOnClickListener(v -> {
            if(edtOtp.getText()!=null){
                otp = edtOtp.getText().toString();
                Log.d("Email", email);
                Log.d("Otp", otp);
                otpValidateViewModel.validateOtp(email, otp);
                edtOtp.requestFocus();

            }
        });
        otpValidateViewModel.getOtpValidatedResponse().observe(getViewLifecycleOwner(), event -> {
            Boolean isValid = event.getContent();
            if (isValid != null && isValid) {
                Toast.makeText(getContext(), "OTP hợp lệ hãy nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                switchToNewPasswordLayout();
            }
        });
        otpValidateViewModel.getOtpValidatedError().observe(getViewLifecycleOwner(), event -> {
            Boolean isError = event.getContent();
            if (isError != null && isError) {
                Toast.makeText(getContext(), "OTP không hợp lệ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        otpValidateViewModel.getMessageError().observe(getViewLifecycleOwner(), event -> {
            String message = event.getContent();
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void switchToNewPasswordLayout() {
        View view = getView();
        if (view == null) return;

        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        parent.removeAllViews();

        View newPasswordView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_change_password, parent, true);
        EditText edtNewPassword = newPasswordView.findViewById(R.id.newPassword);
        EditText edtConfirmPassword = newPasswordView.findViewById(R.id.confirmNewPassword);
        Button btn_resetPassword = newPasswordView.findViewById(R.id.btnResetPassword);
        eventTogglePasswordVisibility(edtNewPassword,R.drawable.baseline_lock_24, R.drawable.baseline_remove_red_eye_24, R.drawable.icons8_closed_eye_50);
        eventTogglePasswordVisibility(edtConfirmPassword,R.drawable.baseline_lock_open_24, R.drawable.baseline_remove_red_eye_24, R.drawable.icons8_closed_eye_50);

        ImageView img_backOTP = view.findViewById(R.id.imgBackOTP);
        img_backOTP.setOnClickListener(v->{
            switchToOtpLayout();
        });
        btn_resetPassword.setOnClickListener(v -> {
            newPassword = edtNewPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if (newPassword.equals(confirmPassword)) {
                PasswordUpdateRequest request = new PasswordUpdateRequest(email, newPassword, otp, username);
                Log.d("ForgotPassword", "Đang gọi updatePassword");
                verifyOTPViewModel.updatePassword(request);
            } else {
                Toast.makeText(getContext(), "Mật khẩu xác nhận không khớp. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        verifyOTPViewModel.getUpdatePasswordResponse().observe(getViewLifecycleOwner(), event -> {
            Boolean isReset = event.getContent();
            if (isReset != null && isReset) {
                Toast.makeText(getContext(), "Mật khẩu đã được thay đổi thành công!", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(getContext(), "Không thể thay đổi mật khẩu. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
        verifyOTPViewModel.getMessageError().observe(getViewLifecycleOwner(), event -> {
            String message = event.getContent();
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        verifyOTPViewModel.getUpdatePasswordError().observe(getViewLifecycleOwner(), event -> {
            Boolean errorOccurred = event.getContent();
            if(errorOccurred!=null && errorOccurred){
                Toast.makeText(getContext(), "Đã có lỗi xảy ra trong quá trình cập nhật mật khẩu mới ", Toast.LENGTH_SHORT).show();
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