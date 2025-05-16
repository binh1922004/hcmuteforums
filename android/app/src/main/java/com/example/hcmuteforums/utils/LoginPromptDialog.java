package com.example.hcmuteforums.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.ui.activity.user.LoginActivity;

public class LoginPromptDialog {
    public static boolean isLogged = false;
    public static void showLoginPrompt(Context context) {
        // Inflate custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_login_prompt, null);

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setCancelable(false); // Prevent dismissing by clicking outside

        // Create and show the dialog
        final AlertDialog dialog = builder.create();

        // Handle button clicks
        Button buttonLogin = dialogView.findViewById(R.id.button_login);
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);

        buttonLogin.setOnClickListener(v -> {
            // Redirect to Login Activity
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            dialog.dismiss();
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
