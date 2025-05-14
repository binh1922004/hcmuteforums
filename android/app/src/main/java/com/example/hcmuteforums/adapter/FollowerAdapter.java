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
import com.example.hcmuteforums.listeners.OnSwitchFragmentProfile;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    private List<FollowerResponse> followerList;
    private Context context;
    private OnFollowClickListener followClickListener;
    private OnMoreClickListener moreClickListener;
    private Map<String, Boolean> followButtonVisibilityMap; // Theo dõi trạng thái hiển thị nút
    private OnSwitchFragmentProfile onSwitchFragmentProfile;
    String currentUsername;     //Lấy biến currentUsername để check có phải chính mình trong list Follow không
    public interface OnFollowClickListener {
        void onFollowClick(String followId, String targetUsername, int position, boolean isFollowing);
    }

    public interface OnMoreClickListener {
        void onMoreClick(String followId, int position);
    }

    public FollowerAdapter(Context context, OnFollowClickListener followClickListener,
                           OnMoreClickListener moreClickListener, OnSwitchFragmentProfile onSwitchFragmentProfile) {
        this.context = context;
        this.followerList = new ArrayList<>();
        this.followClickListener = followClickListener;
        this.moreClickListener = moreClickListener;
        this.followButtonVisibilityMap = new HashMap<>();
        this.onSwitchFragmentProfile = onSwitchFragmentProfile;

    }
    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
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

        //Todo: Truy cập thông tin người dùng qua UserGeneral
        holder.username.setText(follower.getUserGeneral().getUsername());
        holder.displayName.setText(follower.getUserGeneral().getFullName());

        //Todo: Tải ảnh đại diện từ URL bằng Glide
        Glide.with(context)
                .load(follower.getUserGeneral().getAvt())
                .placeholder(R.drawable.avatar_boy)
                .centerCrop()
                .error(R.drawable.user_2)
                .into(holder.profileImage);

        //Todo: Lấy followId và targetUsername
        String followId = follower.getFollowId();
        String targetUsername = follower.getUserGeneral().getUsername();

        //Todo: Check có phải là bản thân không
        boolean isCurrentUser = Boolean.TRUE.equals(follower.getCurrentMe());
        boolean isFollowing= follower.getHasFollowed();


        /*//Todo: Kiểm tra trạng thái hiển thị nút "Theo dõi"
        boolean isFollowing= follower.getHasFollowed();
        followButtonVisibilityMap.put(targetUsername, !isFollowing);
        boolean isButtonVisible = followButtonVisibilityMap.getOrDefault(targetUsername, true);
        holder.followButton.setVisibility(isButtonVisible ? View.VISIBLE : View.GONE);
        //Todo: Xử lý sự kiện nhấn nút "Theo dõi"
        holder.followButton.setOnClickListener(v -> {
            if (followClickListener != null) {
                followClickListener.onFollowClick(followId, targetUsername, position, isFollowing);
                // Ẩn nút ngay sau khi nhấn "Theo dõi"
                if (!isFollowing) {

                    follower.setHasFollowed(true);
                    followButtonVisibilityMap.put(targetUsername, false);
                    holder.followButton.setVisibility(View.GONE);
                    holder.unFollowButton.setVisibility(View.VISIBLE);
                    notifyItemChanged(position);
                }
            }
        });
        holder.unFollowButton.setOnClickListener(view -> {
            if (followClickListener != null) {
                followClickListener.onFollowClick(followId, targetUsername, position, isFollowing);
                // Ẩn nút ngay sau khi nhấn "Theo dõi"
                if (isFollowing) {
                    follower.setHasFollowed(false);
                    followButtonVisibilityMap.put(targetUsername, false);
                    holder.followButton.setVisibility(View.VISIBLE);
                    holder.unFollowButton.setVisibility(View.GONE);
                    notifyItemChanged(position);
                }
            }
        });

        //Todo: Xử lý sự kiện nhấn nút "More"
        holder.moreButton.setOnClickListener(v -> {
            if (moreClickListener != null) {
                moreClickListener.onMoreClick(followId, position);
            }
        });

        if(follower.getHasFollowed()){
            holder.unFollowButton.setVisibility(View.VISIBLE);
            holder.followButton.setVisibility(View.GONE);
        }else{
            holder.unFollowButton.setVisibility(View.GONE);
            holder.followButton.setVisibility(View.VISIBLE);
        }
        //Todo: Xử lí xự kiện ấn vào ảnh qua profile
        holder.profileImage.setOnClickListener(view -> {
            onSwitchFragmentProfile.onClickAnyProfile(targetUsername);
        });
        //Todo: Xử lí xự kiện ấn vào TextView Username qua profile
        holder.username.setOnClickListener(view -> {
            onSwitchFragmentProfile.onClickAnyProfile(targetUsername);
        });*/
        // Ẩn nút nếu là chính mình
        if (isCurrentUser) {
            holder.followButton.setVisibility(View.GONE);
            holder.unFollowButton.setVisibility(View.GONE);
        } else {
            // Nếu không phải chính mình thì mới xử lý follow/unfollow
            holder.followButton.setVisibility(isFollowing ? View.GONE : View.VISIBLE);
            holder.unFollowButton.setVisibility(isFollowing ? View.VISIBLE : View.GONE);

            holder.followButton.setOnClickListener(v -> {
                if (followClickListener != null) {
                    followClickListener.onFollowClick(followId, targetUsername, position, isFollowing);
                    follower.setHasFollowed(true);
                    notifyItemChanged(position);
                }
            });

            holder.unFollowButton.setOnClickListener(v -> {
                if (followClickListener != null) {
                    followClickListener.onFollowClick(followId, targetUsername, position, isFollowing);
                    follower.setHasFollowed(false);
                    notifyItemChanged(position);
                }
            });
        }


        holder.profileImage.setOnClickListener(view -> {
            onSwitchFragmentProfile.onClickAnyProfile(targetUsername);
        });

        holder.username.setOnClickListener(view -> {
            onSwitchFragmentProfile.onClickAnyProfile(targetUsername);
        });

    }

    @Override
    public int getItemCount() {
        return followerList != null ? followerList.size() : 0;
    }

    public void updateData(List<FollowerResponse> newFollowerList, Set<String> followingUsernames) {

        this.followerList.addAll(newFollowerList);
        // Cập nhật trạng thái nút dựa trên danh sách following
        followButtonVisibilityMap.clear();
        for (FollowerResponse follower : newFollowerList) {
            String targetUsername = follower.getUserGeneral().getUsername();
            boolean isFollowing = followingUsernames.contains(targetUsername);
            followButtonVisibilityMap.put(targetUsername, !isFollowing);
        }
        notifyDataSetChanged();
    }

    public void addData(List<FollowerResponse> newList){
        int oldSize = followerList.size();
        followerList.addAll(newList);
        notifyItemRangeInserted(oldSize, newList.size());
    }
    public void clearData(){
        int size = this.followerList.size();
        if(size>0){
            this.followerList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView username;
        TextView displayName;
        Button followButton, unFollowButton;
        ImageView moreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            displayName = itemView.findViewById(R.id.displayName);
            followButton = itemView.findViewById(R.id.followButton);
            moreButton = itemView.findViewById(R.id.moreButton);
            unFollowButton = itemView.findViewById(R.id.unFollowButton);
        }
    }
}