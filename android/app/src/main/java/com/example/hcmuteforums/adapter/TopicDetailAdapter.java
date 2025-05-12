package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.OnMenuActionListener;
import com.example.hcmuteforums.listeners.OnSwitchActivityActionListener;
import com.example.hcmuteforums.listeners.OnReplyShowListener;
import com.example.hcmuteforums.listeners.TopicLikeListener;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    //data
    private List<TopicDetailResponse> topicDetailResponsesList;
    //listener interface
    private TopicLikeListener topicLikeListener;
    private OnReplyShowListener onReplyShowListener;
    private OnSwitchActivityActionListener onSwitchActivityActionListener;

    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_LOADING = 1;

    private OnMenuActionListener onMenuActionListener;
    private boolean isLoadingAdded = false;


    public TopicDetailAdapter(Context context, OnReplyShowListener onReplyShowListener, TopicLikeListener topicLikeListener, OnSwitchActivityActionListener onSwitchActivityActionListener) {
        this.context = context;
        this.onReplyShowListener = onReplyShowListener;
        this.topicLikeListener = topicLikeListener;
        this.onSwitchActivityActionListener = onSwitchActivityActionListener;
        topicDetailResponsesList = new ArrayList<>();
    }
    public void setOnMenuActionListener(OnMenuActionListener onMenuActionListener) {
        this.onMenuActionListener = onMenuActionListener;
    }

    public void setData(List<TopicDetailResponse> topicDetailResponses){
        this.topicDetailResponsesList = topicDetailResponses;
        notifyDataSetChanged();
    }
    public void addData(List<TopicDetailResponse> newList){
        int oldSize = topicDetailResponsesList.size();
        topicDetailResponsesList.addAll(newList);
        notifyItemRangeInserted(oldSize, newList.size());
    }

    public void deleteTopic(int position){
        if (position >= 0 && position < topicDetailResponsesList.size()) {
            topicDetailResponsesList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, topicDetailResponsesList.size());
        }
    }

    public void clearData(){
        int size = this.topicDetailResponsesList.size();
        if (size > 0) {
            this.topicDetailResponsesList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public List<TopicDetailResponse> getData() {
        return topicDetailResponsesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOADING){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new TopicDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopicDetailViewHolder){
            var topicHolder = (TopicDetailViewHolder) holder;
            TopicDetailResponse topicDetailResponse = topicDetailResponsesList.get(position);
            if (topicDetailResponse == null)
                return;
            topicHolder.bind(topicDetailResponse, position);
        }
    }


    @Override
    public int getItemCount() {
        if (topicDetailResponsesList != null)
            return topicDetailResponsesList.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == topicDetailResponsesList.size() - 1 && isLoadingAdded)
            return ITEM_TYPE_LOADING;
        else
            return ITEM_TYPE_NORMAL;
    }


    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


    public class TopicDetailViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvTime, tvTitle, tvContent, tvReplyCount, tvLikeCount, tvToggle;
        ImageView btnLike, btnReply, imgMoreActions;
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
            btnReply = itemView.findViewById(R.id.btnReply);
            viewPagerImages = itemView.findViewById(R.id.viewPagerImages);
            tvReplyCount = itemView.findViewById(R.id.tvReplyCount);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvToggle = itemView.findViewById(R.id.tvToggle);
            imgMoreActions = itemView.findViewById(R.id.imgMoreActions);
        }

        public void bind(TopicDetailResponse topic, int position) {
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
                notifyItemChanged(position); // Dùng notify để đảm bảo UI được update
            };
            tvToggle.setOnClickListener(toggleListener);
            tvContent.setOnClickListener(toggleListener);
            tvContent.setText(topic.getContent());

            List<String> imageUrls = topic.getImgUrls();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                viewPagerImages.setVisibility(View.VISIBLE);
                ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(context, imageUrls);
                viewPagerImages.setAdapter(pagerAdapter);
            } else {
                viewPagerImages.setVisibility(View.GONE);
            }

            //TODO: Up avatar
            Glide.with(context)
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
                int currentLike = topic.getLikeCount();

                currentLike += (v.isSelected() ? 1 : -1);
                topic.setLikeCount(currentLike);
                tvLikeCount.setText(String.valueOf(currentLike));
            });


            //biding for reply
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
            btnReply.setOnClickListener(v -> {
                if (onReplyShowListener != null)
                    onReplyShowListener.onReply(topic.getId(), position);
            });


            //TODO: log to user profile
            imgAvatar.setOnClickListener(v -> {
                onSwitchActivityActionListener.onClickProfile(topic.getUserGeneral().getUsername());
            });
            tvName.setOnClickListener(v -> {
                onSwitchActivityActionListener.onClickProfile(topic.getUserGeneral().getUsername());
            });
            //TODO: log to topic detail
            tvTitle.setOnClickListener(v -> {
                onSwitchActivityActionListener.onClickTopicDetail(topic.getId(), topic.isOwner());
            });

            //TODO: if a topic of your topic, display a menu action
            if (onMenuActionListener != null){
                imgMoreActions.setVisibility(View.VISIBLE);
                imgMoreActions.setOnClickListener(v -> {
                    // Tạo PopupMenu
                    PopupMenu popupMenu = new PopupMenu(context, v);

                    //if is your reply have 3 selections
                    if (topic.isOwner()){
                        popupMenu.getMenuInflater().inflate(R.menu.reply_actions_menu_user, popupMenu.getMenu());
                    }
                    else{
                        popupMenu.getMenuInflater().inflate(R.menu.reply_actions_menu_guest, popupMenu.getMenu());
                    }

                    // Xử lý sự kiện khi chọn mục trong menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(android.view.MenuItem item) {
                            int itemId = item.getItemId();
                            if (itemId == R.id.actionCopy){
                                onMenuActionListener.onCopy(topic.getContent());
                            }
                            else if (itemId == R.id.actionEdit){
                                onMenuActionListener.onUpdate(topic.getId(), topic.getContent(), position);
                            }
                            else if (itemId == R.id.actionDelete){
                                onMenuActionListener.onDelete(topic.getId(), position);
                            }
                            return true;
                        }
                    });

                    // Hiển thị menu
                    popupMenu.show();
                });
            }
            else {
                imgMoreActions.setVisibility(View.GONE);
            }
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        notifyItemInserted(topicDetailResponsesList.size());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        notifyItemRemoved(topicDetailResponsesList.size());
    }

}
