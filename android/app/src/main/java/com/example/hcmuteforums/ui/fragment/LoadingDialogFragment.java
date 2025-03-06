package com.example.hcmuteforums.ui.fragment;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class LoadingDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Tạo ProgressBar
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(150, 150, Gravity.CENTER)); // Định kích thước nhỏ lại

        // Đặt màu trắng cho ProgressBar
        Drawable indeterminateDrawable = progressBar.getIndeterminateDrawable().mutate();
        indeterminateDrawable.setTint(ContextCompat.getColor(requireContext(), android.R.color.white));
        progressBar.setIndeterminateDrawable(indeterminateDrawable);

        // Tạo nền trắng bo tròn cho ProgressBar
        FrameLayout container = new FrameLayout(requireContext());
        container.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));
        container.setPadding(40, 40, 40, 40);

        container.addView(progressBar);

        dialog.setContentView(container);

        // Thiết lập kích thước Dialog
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setDimAmount(0.5f); // Làm mờ nền
            window.setGravity(Gravity.CENTER);
        }

        dialog.setCancelable(false);
        return dialog;
    }
}