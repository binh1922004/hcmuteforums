package com.example.hcmuteforums.ui.activity.topic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.ImagePagerAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.OnReplyAddedListener;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.ui.fragment.ReplyBottomSheetFragment;
import com.example.hcmuteforums.ui.fragment.ReplyFragment;
import com.example.hcmuteforums.viewmodel.TopicDetailViewModel;
import com.example.hcmuteforums.viewmodel.TopicViewModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicDetailActivity extends AppCompatActivity {
    ImageView imgBack;
    TextView tvName, tvTime, tvTitle, tvContent, tvReplyCount, tvLikeCount, tvToggle;
    ImageView btnLike, btnReply;
    CircleImageView imgAvatar;
    ViewPager2 viewPagerImages;

    //view model
    TopicDetailViewModel topicDetailViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topic_detail);
        //TODO: Get data from before activity
        Intent receivedIntent = getIntent();
        String topicId = receivedIntent.getStringExtra("topicId");
        String replyId = receivedIntent.getStringExtra("replyId");
        //mapping data
        mappingData();

        //TODO: set topic data to element
        setTopicData(topicId);

        // Thêm CommentFragment vào Activity
        ReplyFragment replyFragment;
        if (replyId == null){
            replyFragment = ReplyFragment.newInstance(topicId);
        }
        else{
            replyFragment = ReplyFragment.newInstance(topicId, replyId);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, replyFragment)
                .commit();

        //event handler
        backEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setTopicData(String topicId) {
        topicDetailViewModel.getTopicDetail(topicId);
        topicDetailViewModel.getTopicDetailLiveData().observe(this, new Observer<Event<TopicDetailResponse>>() {
            @Override
            public void onChanged(Event<TopicDetailResponse> topicDetailResponseEvent) {
                TopicDetailResponse topic = topicDetailResponseEvent.getContent();
                if (topic != null){
                    tvName.setText(topic.getUserGeneral().getFullName());
                    tvTime.setText(topic.getCreatedAt().toString());
                    tvTitle.setText(topic.getTitle());
                    //TODO: Expand text view
                    String content = topic.getContent();
                    tvContent.setText(content);
                    //setup for content and max lines
                    tvContent.setMaxLines(topic.isExpanded() ? Integer.MAX_VALUE : 3);
                    tvToggle.setText(topic.isExpanded() ? "Thu gọn" : "Xem thêm");
                    tvToggle.setVisibility(content.length() > 100 ? View.VISIBLE : View.GONE);
                    //content and toggle use both event
                    // Toggle xử lý khi nhấn
                    View.OnClickListener toggleListener = v -> {
                        topic.setExpanded(!topic.isExpanded());
                    };
                    tvToggle.setOnClickListener(toggleListener);
                    tvContent.setOnClickListener(toggleListener);
                    tvContent.setText(topic.getContent());

                    List<String> imageUrls = topic.getImgUrls();
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        viewPagerImages.setVisibility(View.VISIBLE);
                        ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(TopicDetailActivity.this, imageUrls);
                        viewPagerImages.setAdapter(pagerAdapter);
                    } else {
                        viewPagerImages.setVisibility(View.GONE);
                    }

                    //TODO: Up avatar
                    Glide.with(TopicDetailActivity.this)
                            .load(topic.getUserGeneral().getAvt())
                            .centerCrop()
                            .into(imgAvatar);

                    //biding for like
                    tvLikeCount.setText(String.valueOf(topic.getLikeCount()));
                    boolean like = topic.isLiked();
                    Log.d("Like: ", like ? "isLike": "unlike");
                    if (like){
                        btnLike.setSelected(true);
                        btnLike.setImageResource(R.drawable.love_click);
                    }
                    else{
                        btnLike.setSelected(false);
                        btnLike.setImageResource(R.drawable.love_unclick);
                    }
                }
            }
        });
    }

    private void mappingData() {
        imgBack = findViewById(R.id.imgBack);
        tvName = findViewById(R.id.tvName);
        tvTime = findViewById(R.id.tvTime);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnLike = findViewById(R.id.btnLike);
        btnReply = findViewById(R.id.btnReply);
        viewPagerImages = findViewById(R.id.viewPagerImages);
        tvReplyCount = findViewById(R.id.tvReplyCount);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        tvToggle = findViewById(R.id.tvToggle);

        topicDetailViewModel = new TopicDetailViewModel();
    }

    private void backEvent(){
        imgBack.setOnClickListener(v -> {
            finish();
        });
    }
}