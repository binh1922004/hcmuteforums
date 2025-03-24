package com.example.hcmuteforums.ui.activity.topic;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hcmuteforums.R;

public class TopicPostActivity extends AppCompatActivity {
    TextView tvCancel, tvPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topic_post);
        //mapping data
        mappingData();

        //event
        cancelClickEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void mappingData() {
        tvCancel = findViewById(R.id.tvCancel);
    }
    private void cancelClickEvent(){
        tvCancel.setOnClickListener(v -> {
            finish();
        });
    }
}