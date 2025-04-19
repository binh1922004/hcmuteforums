package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.TopicLikeListener;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicDetailAdapter extends RecyclerView.Adapter<TopicDetailAdapter.TopicDetailViewHolder>{
    private Context context;
    private List<TopicDetailResponse> topicDetailResponsesList;
    private TopicLikeListener topicLikeListener;
    public TopicDetailAdapter(Context context, TopicLikeListener topicLikeListener){
        this.context = context;
        this.topicLikeListener = topicLikeListener;
    }

    public void setData(List<TopicDetailResponse> topicDetailResponses){
        this.topicDetailResponsesList = topicDetailResponses;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TopicDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new TopicDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicDetailViewHolder holder, int position) {
        TopicDetailResponse topicDetailResponse = topicDetailResponsesList.get(position);
        if (topicDetailResponse == null)
            return;
        holder.bind(topicDetailResponse);

    }


    @Override
    public int getItemCount() {
        if (topicDetailResponsesList != null)
            return topicDetailResponsesList.size();
        return 0;
    }

    public class TopicDetailViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvTime, tvTitle, tvContent;
        ImageView btnLike;
        CircleImageView imgAvatar;
        ViewPager2 viewPagerImages;
        public TopicDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnLike = itemView.findViewById(R.id.btnLike);
            viewPagerImages = itemView.findViewById(R.id.viewPagerImages);
        }

        public void bind(TopicDetailResponse topic) {
            tvName.setText(topic.getUserGeneral().getFullName());
            tvTime.setText(topic.getCreatedAt().toString());
            tvTitle.setText(topic.getTitle());
            tvContent.setText(topic.getContent());
            List<String> imageUrls = topic.getImgUrls();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                viewPagerImages.setVisibility(View.VISIBLE);
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(context, imageUrls);
                viewPagerImages.setAdapter(pagerAdapter);
            } else {
                viewPagerImages.setVisibility(View.GONE);
            }
            boolean like = topic.isLiked();
            Log.d("Like: ", like ? "isLike": "unlike");
            if (like){
                btnLike.setImageResource(R.drawable.love_click);
            }

            btnLike.setOnClickListener(v -> {
                if (v.isSelected()){
                    btnLike.setImageResource(R.drawable.love_unclick);
                }
                else{
                    btnLike.setImageResource(R.drawable.love_click);
                }
                v.setSelected(!v.isSelected());

                if (topicLikeListener != null){
                    topicLikeListener.likeTopic(topic.getId());
                }
            });
        }
    }
}
