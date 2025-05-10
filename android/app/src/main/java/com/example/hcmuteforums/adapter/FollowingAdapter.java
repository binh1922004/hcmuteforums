package com.example.hcmuteforums.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
    private List<FollowingResponse> followingList;
    private Context context;
    private OnMoreClickListener moreClickListener;

    public interface OnMoreClickListener {
        void onMoreClick(String followId, int position);
    }

    public FollowingAdapter(Context context, OnMoreClickListener moreClickListener) {
        this.context = context;
        this.followingList = new ArrayList<>();
        this.moreClickListener = moreClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {
        FollowingResponse following = followingList.get(position);

        // Truy cập thông tin người dùng qua UserGeneral
        holder.username.setText(following.getUserGeneral().getUsername());
        holder.displayName.setText(following.getUserGeneral().getFullName());

        // Tải ảnh đại diện từ URL bằng Glide
        Glide.with(context)
                .load(following.getUserGeneral().getAvt())
                .placeholder(R.drawable.avatar_boy)
                .error(R.drawable.user_2)
                .into(holder.profileImage);

        // Lấy followId
        String followId = following.getFollowId();

        // Xử lý sự kiện nhấn nút "More"
        holder.moreButton.setOnClickListener(v -> {
            if (moreClickListener != null) {
                moreClickListener.onMoreClick(followId, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followingList != null ? followingList.size() : 0;
    }

    public void updateData(List<FollowingResponse> newFollowingList) {
        this.followingList.clear();
        this.followingList.addAll(newFollowingList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView username;
        TextView displayName;
        ImageView moreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            displayName = itemView.findViewById(R.id.displayName);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }
}