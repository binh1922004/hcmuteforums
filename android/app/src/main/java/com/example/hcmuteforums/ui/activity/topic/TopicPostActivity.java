package com.example.hcmuteforums.ui.activity.topic;

import android.os.Bundle;
import android.widget.EditText;
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
import com.example.hcmuteforums.viewmodel.TopicPostViewModel;

public class TopicPostActivity extends AppCompatActivity {
    //element
    TextView tvCancel, tvPost;
    EditText edtTitle, edtContent;
    //viewmodel
    TopicPostViewModel topicPostViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topic_post);
        //mapping data
        mappingData();

        //event
        cancelClickEvent();
        postTopicEvent();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void mappingData() {
        tvCancel = findViewById(R.id.tvCancel);
        tvPost = findViewById(R.id.tvPost);
        edtContent = findViewById(R.id.edtContent);
        edtTitle = findViewById(R.id.edtTitle);

        //viewmodel
        topicPostViewModel = new ViewModelProvider(this).get(TopicPostViewModel.class);
    }
    private void cancelClickEvent(){
        tvCancel.setOnClickListener(v -> {
            finish();
        });
    }

    private void postTopicEvent(){
        tvPost.setOnClickListener(v -> {
            String title = edtTitle.getText().toString();
            String content = edtContent.getText().toString();
            String subCategoryId = "8dd3ad6b-ff4a-11ef-ae63-0242ac120003";
            Toast.makeText(this, "Go", Toast.LENGTH_SHORT).show();
            topicPostViewModel.postTopic(title, content, subCategoryId);
        });

        topicPostViewModel.getTopicPostSuccess().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                if (event.getContent() != null){
                    Toast.makeText(TopicPostActivity.this, "Post thanhg cong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        topicPostViewModel.getTopicPostError().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                if (booleanEvent.getContent() != null){
                    Toast.makeText(TopicPostActivity.this, "Da co loi trong qua trinh post - getTopicError", Toast.LENGTH_SHORT).show();
                }
            }
        });

        topicPostViewModel.getMessageError().observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String mess = stringEvent.getContent();
                if (mess != null){
                    Toast.makeText(TopicPostActivity.this, mess, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}