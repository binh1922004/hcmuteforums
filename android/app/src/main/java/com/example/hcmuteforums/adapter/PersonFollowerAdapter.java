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

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersonFollowerAdapter extends RecyclerView.Adapter<PersonFollowerAdapter.ViewHolder> {

    private List<FollowerResponse> followerList;
    private Context context;
    private OnFollowClickListener followClickListener;
    private OnMoreClickListener moreClickListener;
    private Map<String, Boolean> followButtonVisibilityMap; // Theo dõi trạng thái hiển thị nút
    private Set<String> followingUsernames; // Danh sách username của những người đã follow

    public interface OnFollowClickListener {
        void onFollowClick(String followId, String targetUsername, int position, boolean isFollowing);
    }

    public interface OnMoreClickListener {
        void onMoreClick(String followId, int position);
    }

    public PersonFollowerAdapter(Context context, OnFollowClickListener followClickListener, OnMoreClickListener moreClickListener) {
        this.context = context;
        this.followerList = new ArrayList<>();
        this.followClickListener = followClickListener;
        this.moreClickListener = moreClickListener;
        this.followButtonVisibilityMap = new HashMap<>();
        this.followingUsernames = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person_follower, parent, false); // Sử dụng layout item_person_follower
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

        // Kiểm tra trạng thái hiển thị nút "Theo dõi"
        boolean isFollowing = followingUsernames.contains(targetUsername);
        // Đảm bảo trạng thái trong followButtonVisibilityMap được cập nhật từ fragment
        boolean isButtonVisible = followButtonVisibilityMap.getOrDefault(targetUsername, true);
        holder.followButton.setVisibility(isButtonVisible ? View.VISIBLE : View.GONE);
        Log.d("PersonFollowerAdapter", "Position " + position + ": Username " + targetUsername + ", Visible: " + isButtonVisible + ", Following: " + isFollowing);

        // Xử lý sự kiện nhấn nút "Theo dõi"
        holder.followButton.setOnClickListener(v -> {
            if (followClickListener != null) {
                followClickListener.onFollowClick(followId, targetUsername, position, isFollowing);
                // Cập nhật trạng thái ngay lập tức, sẽ được đồng bộ lại từ fragment
                if (!isFollowing) {
                    followButtonVisibilityMap.put(targetUsername, false);
                    holder.followButton.setVisibility(View.GONE);
                }
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

    public void updateData(List<FollowerResponse> newFollowerList, Set<String> followingUsernames) {
        this.followerList.clear();
        this.followerList.addAll(newFollowerList);
        this.followingUsernames.clear();
        this.followingUsernames.addAll(followingUsernames);
        // Không tự động cập nhật followButtonVisibilityMap ở đây, để fragment quản lý
        notifyDataSetChanged();
    }

    // Phương thức để cập nhật trạng thái nút từ bên ngoài
    public void updateButtonVisibility(String targetUsername, boolean isVisible) {
        followButtonVisibilityMap.put(targetUsername, isVisible);
        if (isVisible) {
            followingUsernames.remove(targetUsername); // Unfollow: Xóa khỏi danh sách following
        } else {
            followingUsernames.add(targetUsername); // Follow: Thêm vào danh sách following
        }
        // Cập nhật giao diện cho các item liên quan
        for (int i = 0; i < followerList.size(); i++) {
            if (followerList.get(i).getUserGeneral().getUsername().equals(targetUsername)) {
                notifyItemChanged(i);
                break;
            }
        }
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