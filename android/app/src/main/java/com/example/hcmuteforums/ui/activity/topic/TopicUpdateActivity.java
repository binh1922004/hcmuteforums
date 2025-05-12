package com.example.hcmuteforums.ui.activity.topic;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.ImageUploadAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.ImageActionListener;
import com.example.hcmuteforums.model.dto.request.TopicUpdateRequest;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.ui.fragment.LoadingDialogFragment;
import com.example.hcmuteforums.viewmodel.TopicDetailViewModel;

public class TopicUpdateActivity extends AppCompatActivity implements ImageActionListener {
    private static final int PICK_IMAGES_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    String topicId;
    //element
    TextView tvCancel, tvPost;
    EditText edtTitle, edtContent;
    RecyclerView recyclerViewImages;
    ImageView btnAddImage;
    ImageUploadAdapter imageAdapter;
    LinearLayout layoutAddImage;
    //viewmodel
    TopicDetailViewModel topicUpdateViewModel;
    //fragment
    LoadingDialogFragment loadingDialog;
    TopicUpdateRequest topicUpdateRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topic_update);
        //mapping data
        mappingData();
        //get data from intent
        getDataFromIntent();
        //event
        cancelClickEvent();
        updateTopicEvent();
        uploadImageEvent();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getDataFromIntent() {
        Intent myIntent = getIntent();
        topicId = myIntent.getStringExtra("topicId");
        topicUpdateViewModel.getTopicDetail(topicId);
        topicUpdateViewModel.getTopicDetailLiveData().observe(this, new Observer<Event<TopicDetailResponse>>() {
            @Override
            public void onChanged(Event<TopicDetailResponse> topicDetailResponseEvent) {
                TopicDetailResponse topic = topicDetailResponseEvent.getContent();
                if (topic != null){
                    topicUpdateRequest = new TopicUpdateRequest(topic.getContent(), topic.getTitle());

                    edtContent.setText(topic.getContent());
                    edtTitle.setText(topic.getTitle());
                }
            }
        });
    }

    private void mappingData() {
        tvCancel = findViewById(R.id.tvCancel);
        tvPost = findViewById(R.id.tvPost);
        edtContent = findViewById(R.id.edtContent);
        edtTitle = findViewById(R.id.edtTitle);
        layoutAddImage = findViewById(R.id.layoutAddImage);
        //recyclerview
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 4));
        imageAdapter = new ImageUploadAdapter(this, this);
        recyclerViewImages.setAdapter(imageAdapter);
        //viewmodel
        topicUpdateViewModel = new TopicDetailViewModel();
        //fragment
        loadingDialog = new LoadingDialogFragment();
    }
    private void cancelClickEvent(){
        tvCancel.setOnClickListener(v -> {
            finish();
        });
    }
    private void updateTopicEvent(){
        tvPost.setOnClickListener(v -> {
            String title = edtTitle.getText().toString();
            String content = edtContent.getText().toString();
            topicUpdateViewModel.updateTopic(topicId, title, content);
        });

        topicUpdateViewModel.getUpdateLiveData().observe(this, new Observer<Event<TopicDetailResponse>>() {
            @Override
            public void onChanged(Event<TopicDetailResponse> topic) {
                TopicDetailResponse topicDetailResponse = topic.getContent();
                if (topicDetailResponse != null){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("topic", topicDetailResponse);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        topicUpdateViewModel.getUpdateError().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                if (booleanEvent.getContent() != null){
                    Toast.makeText(TopicUpdateActivity.this, "Da co loi trong qua trinh post - getTopicError", Toast.LENGTH_SHORT).show();
                }
            }
        });

        topicUpdateViewModel.getMessageError().observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String mess = stringEvent.getContent();
                if (mess != null){
                    Toast.makeText(TopicUpdateActivity.this, mess, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onImageRemove(int position) {
        imageAdapter.removeImage(position);
    }
    private void uploadImageEvent(){
        layoutAddImage.setOnClickListener(v -> {
            checkPermissionsAndOpenImagePicker();
        });
    }

    //permission
    private void checkPermissionsAndOpenImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission = Manifest.permission.READ_MEDIA_IMAGES;
            } else {
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            }

            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, PERMISSION_REQUEST_CODE);
            } else {
                openImagePicker();
            }
        } else {
            openImagePicker();
        }
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGES_REQUEST);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Cần quyền truy cập để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    // Xử lý khi chọn nhiều ảnh
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        imageAdapter.addImage(imageUri);
                    }
                } else if (data.getData() != null) {
                    // Xử lý khi chọn một ảnh
                    Uri imageUri = data.getData();
                    imageAdapter.addImage(imageUri);
                }

                // Hiển thị RecyclerView nếu có ảnh
                updateImageRecyclerViewVisibility();
            }
        }
    }
    private void updateImageRecyclerViewVisibility() {
        recyclerViewImages.setVisibility(imageAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
    }
}