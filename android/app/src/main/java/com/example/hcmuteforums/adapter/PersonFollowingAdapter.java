package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.OnSwitchFragmentProfile;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonFollowingAdapter extends RecyclerView.Adapter<PersonFollowingAdapter.ViewHolder> {

    private List<FollowingResponse> followingList;
    private Context context;
    private FollowingAdapter.OnMoreClickListener moreClickListener;

    private OnFollowClickListener followClickListener;
    private Map<String, Boolean> followButtonVisibilityMap; // Theo dõi trạng thái hiển thị nút
    private OnSwitchFragmentProfile onSwitchFragmentProfile;

    public interface OnFollowClickListener {
        void onFollowClick(String followId, String targetUsername, int position, boolean isFollowing);
    }

    public PersonFollowingAdapter(Context context, List<FollowingResponse> followingList, OnFollowClickListener followClickListener
                        ,OnSwitchFragmentProfile onSwitchFragmentProfile) {
        this.context = context;
        this.followingList = followingList;
        this.followClickListener = followClickListener;
        this.followButtonVisibilityMap = new HashMap<>();
        this.onSwitchFragmentProfile = onSwitchFragmentProfile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        String followId = following.getFollowId();
        String targetUsername = following.getUserGeneral().getUsername();

        boolean isFollowing = following.isHasFollowed();
        followButtonVisibilityMap.put(targetUsername, !isFollowing);
        boolean isButtonVisible = followButtonVisibilityMap.getOrDefault(targetUsername, true);
        holder.followButton.setVisibility(isButtonVisible ? View.VISIBLE : View.GONE);


        holder.followButton.setOnClickListener(v -> {
            if (followClickListener != null) {
                followClickListener.onFollowClick(followId, targetUsername, position, isFollowing);
                // Ẩn nút ngay sau khi nhấn "Theo dõi"
                if (!isFollowing) {
                    following.setHasFollowed(true);
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
                    following.setHasFollowed(false);
                    followButtonVisibilityMap.put(targetUsername, false);
                    holder.followButton.setVisibility(View.VISIBLE);
                    holder.unFollowButton.setVisibility(View.GONE);
                    notifyItemChanged(position);
                }
            }
        });

        if(following.isHasFollowed()){
            holder.unFollowButton.setVisibility(View.VISIBLE);
            holder.followButton.setVisibility(View.GONE);
        }else{
            holder.unFollowButton.setVisibility(View.GONE);
            holder.followButton.setVisibility(View.VISIBLE);
        }
        //Todo: Xử lí xự kiện ấn vào ảnh qua profile
        holder.profileImage.setOnClickListener(view -> {
            Log.d("Username", targetUsername);
            onSwitchFragmentProfile.onClickAnyProfile(targetUsername);
        });
        //Todo: Xử lí xự kiện ấn vào TextView Username qua profile
        holder.username.setOnClickListener(view -> {
            onSwitchFragmentProfile.onClickAnyProfile(targetUsername);
        });


    }
    public void addData(List<FollowingResponse> newList){
        int oldSize = followingList.size();
        followingList.addAll(newList);
        notifyItemRangeInserted(oldSize, newList.size());
    }
    public void clearData(){
        int size = this.followingList.size();
        if(size>0){
            this.followingList.clear();
            notifyItemRangeRemoved(0, size);
        }
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
        Button followButton, unFollowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            displayName = itemView.findViewById(R.id.displayName);
            followButton = itemView.findViewById(R.id.followButton);
            unFollowButton = itemView.findViewById(R.id.unFollowButton);
        }
    }
}
