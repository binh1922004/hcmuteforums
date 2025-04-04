package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicDetailAdapter extends RecyclerView.Adapter<TopicDetailAdapter.TopicDetailViewHolder>{
    Context context;
    List<TopicDetailResponse> topicDetailResponsesList;
    public TopicDetailAdapter(Context context){
        this.context = context;
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
        holder.tvName.setText(topicDetailResponse.getUserGeneral().getFullName());
        holder.tvTime.setText(topicDetailResponse.getCreatedAt().toString());
        holder.tvTitle.setText(topicDetailResponse.getTitle());
        holder.tvContent.setText(topicDetailResponse.getContent());

        boolean like = topicDetailResponse.isLiked();
        Log.d("Like: ", like ? "isLike": "unlike");
        if (like){
            holder.btnLike.setImageResource(R.drawable.love_click);
        }
//
//        holder.btnLike.setOnClickListener(v -> {
//            v.setSelected(!v.isSelected());
//        });
    }


    @Override
    public int getItemCount() {
        if (topicDetailResponsesList != null)
            return topicDetailResponsesList.size();
        return 0;
    }

    public static class TopicDetailViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvTime, tvTitle, tvContent;
        ImageView btnLike;
        CircleImageView imgAvatar;
        public TopicDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnLike = itemView.findViewById(R.id.btnLike);
        }
    }
}
