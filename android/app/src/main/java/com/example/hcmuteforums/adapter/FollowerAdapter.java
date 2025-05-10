package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.model.modelAdapter.User;

import java.util.ArrayList;
import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    private List<FollowerResponse> followerList;
    private Context context;
    private OnFollowClickListener followClickListener;
    private OnMoreClickListener moreClickListener;
    public interface OnFollowClickListener {
        void onFollowClick(String followId, String targetUsername, int position);
    }

    public interface OnMoreClickListener {
        void onMoreClick(String followId, int position);
    }

    public FollowerAdapter(Context context, OnFollowClickListener followClickListener, OnMoreClickListener moreClickListener) {
        this.context = context;
        this.followerList = new ArrayList<>();
        this.followClickListener = followClickListener;
        this.moreClickListener = moreClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowerResponse follower = followerList.get(position);

        // Truy cập thông tin người dùng qua UserGeneral
        holder.username.setText(follower.getUserGeneral().getUsername());
        holder.displayName.setText(follower.getUserGeneral().getFullName());

        // Tải ảnh đại diện từ URL bằng Glide
        Glide.with(context)
                .load(follower.getUserGeneral().getAvt())
                .placeholder(R.drawable.avatar_boy)
                .centerCrop()
                .error(R.drawable.user_2)
                .into(holder.profileImage);

        // Lấy followId và targetUsername
        String followId = follower.getFollowId();
        String targetUsername = follower.getUserGeneral().getUsername();

        // Xử lý sự kiện nhấn nút "Theo dõi"
        holder.followButton.setOnClickListener(v -> {
            if (followClickListener != null) {
                followClickListener.onFollowClick(followId, targetUsername, position);
            }
        });

        // Xử lý sự kiện nhấn nút "More"
        holder.moreButton.setOnClickListener(v -> {
            if (moreClickListener != null) {
                moreClickListener.onMoreClick(followId, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return followerList != null ? followerList.size() : 0;
    }

    public void updateData(List<FollowerResponse> newFollowerList) {
        this.followerList.clear();
        this.followerList.addAll(newFollowerList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView username;
        TextView displayName;
        Button followButton;
        ImageView moreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            displayName = itemView.findViewById(R.id.displayName);
            followButton = itemView.findViewById(R.id.followButton);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }


}