package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.OnMenuActionListener;
import com.example.hcmuteforums.listeners.OnReplyClickListener;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {


    private List<ReplyResponse> replyList;
    private Set<String> replyIds; // Lưu trữ id của các reply
    //listener
    private OnReplyClickListener onReplyClickListener;
    private OnMenuActionListener onMenuActionListener;
    private Context context;
    //adapter
    public ReplyAdapter(Context context, List<ReplyResponse> replyList, OnReplyClickListener onReplyClickListener,
                        OnMenuActionListener onMenuActionListener) {
        this.replyList = replyList;
        this.onReplyClickListener = onReplyClickListener;
        this.context = context;
        this.onMenuActionListener = onMenuActionListener;
        replyIds = new HashSet<>();
    }
    // Lớp DiffUtil.Callback chung
    private static class ReplyDiffCallback extends DiffUtil.Callback {
        private final List<ReplyResponse> oldList;
        private final List<ReplyResponse> newList;

        public ReplyDiffCallback(List<ReplyResponse> oldList, List<ReplyResponse> newList) {
            this.oldList = oldList != null ? oldList : new ArrayList<>();
            this.newList = newList != null ? newList : new ArrayList<>();
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ReplyResponse oldReply = oldList.get(oldItemPosition);
            ReplyResponse newReply = newList.get(newItemPosition);
            return oldReply.getContent().equals(newReply.getContent());
        }
    }

    private void applyDiffUtil(List<ReplyResponse> oldList, List<ReplyResponse> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ReplyDiffCallback(oldList, newList));
        replyList = new ArrayList<>(newList);
        diffResult.dispatchUpdatesTo(this);
    }
    public void addData(List<ReplyResponse> newList) {
        if (newList == null || newList.isEmpty()) return;
        List<ReplyResponse> oldList = new ArrayList<>(replyList);
        List<ReplyResponse> updatedList = new ArrayList<>(oldList);
        // Chỉ thêm reply chưa tồn tại
        for (ReplyResponse newReply : newList) {
            if (!replyIds.contains(newReply.getId())) {
                updatedList.add(newReply);
            }
        }
        applyDiffUtil(oldList, updatedList);
    }

    public void addNewReply(ReplyResponse newReply) {
        if (newReply == null) return;
        List<ReplyResponse> oldList = new ArrayList<>(replyList);
        List<ReplyResponse> updatedList = new ArrayList<>(oldList);
        updatedList.add(0, newReply);
        applyDiffUtil(oldList, updatedList);

        //add item to set
        replyIds.add(newReply.getId());
    }
    public void addNewReplyChildList(List<ReplyResponse> childList, boolean isLast){
        //child list empty so not do any thing
        if (childList.isEmpty())
            return;
        String parentId = childList.get(0).getParentReplyId();
        for (int pos = 0; pos < replyList.size(); pos++){
            var reply = replyList.get(pos);
            if (reply.getId().equals(parentId)){
                if (replyList.get(pos).getListChild() == null){
                    replyList.get(pos).setListChild(new ArrayList<>());
                }
                replyList.get(pos).getListChild().addAll(childList);
                replyList.get(pos).setLast(isLast);
                notifyItemChanged(pos);
                break;
            }
        }
    }

    public void addNewReplyChild(ReplyResponse childReply){
        String parentId = childReply.getParentReplyId();
        for (int pos = 0; pos < replyList.size(); pos++){
            var reply = replyList.get(pos);
            if (reply.getId().equals(parentId)){
                if (replyList.get(pos).getListChild() == null){
                    replyList.get(pos).setListChild(new ArrayList<>());
                }
                replyList.get(pos).getListChild().add(0, childReply);
                notifyItemChanged(pos);
                break;
            }
        }
    }


    public void deleteReply(int position){
        if (position >= 0 && position < replyList.size()) {
            replyList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, replyList.size());
        }
    }

    // Method to update reply at specific position
    public void updateReply(int position, String newContent) {
        if (position >= 0 && position < replyList.size()) {
            ReplyResponse reply = replyList.get(position);
            reply.setContent(newContent); // Assuming Reply has a setter for content
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        ReplyResponse reply = replyList.get(position);
        holder.bind(reply, position);
    }

    @Override
    public int getItemCount() {
        if (replyList == null)
            return 0;
        return replyList.size();
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvReply, tvShowChildReply;
        TextView tvContent, tvToggle, tvShowMoreChildReply, tvHideChildReply;
        CircleImageView imgAvatar;
        ImageView imgMoreActions;
        RecyclerView rcvChildReplies;
        ReplyChildAdapter replyChildAdapter;
        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvContent = itemView.findViewById(R.id.tvContent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvReply = itemView.findViewById(R.id.tvReply);
            tvShowChildReply = itemView.findViewById(R.id.tvShowChildReply);
            rcvChildReplies = itemView.findViewById(R.id.rcvChildReplies);
            tvToggle = itemView.findViewById(R.id.tvToggle);
            tvShowMoreChildReply = itemView.findViewById(R.id.tvShowMoreChildReply);
            tvHideChildReply = itemView.findViewById(R.id.tvHideChildReply);
            imgMoreActions = itemView.findViewById(R.id.imgMoreActions);
            //config for reply child adapter
            rcvChildReplies.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.VERTICAL, false));
            replyChildAdapter = new ReplyChildAdapter(context, onReplyClickListener);
            rcvChildReplies.setAdapter(replyChildAdapter);
            rcvChildReplies.setNestedScrollingEnabled(false);
        }
        public void bind(ReplyResponse reply, int pos){
            tvShowMoreChildReply.setVisibility(View.GONE);
            tvShowChildReply.setVisibility(View.GONE);
            tvHideChildReply.setVisibility(View.GONE);

            tvUsername.setText(reply.getUserGeneral().getFullName());
            //TODO: Expand text view
            String content = reply.getContent();
            tvContent.setText(content);
            //setup for content and max lines
            tvContent.setMaxLines(reply.isExpanded() ? Integer.MAX_VALUE : 3);
            tvToggle.setText(reply.isExpanded() ? "Thu gọn" : "Xem thêm");
            tvToggle.setVisibility(content.length() > 100 ? View.VISIBLE : View.GONE);
            //content and toggle use both event
            // Toggle xử lý khi nhấn
            View.OnClickListener toggleListener = v -> {
                reply.setExpanded(!reply.isExpanded());
                notifyItemChanged(pos); // Dùng notify để đảm bảo UI được update
            };
            tvToggle.setOnClickListener(toggleListener);
            tvContent.setOnClickListener(toggleListener);


            //TODO: Up avatar
            Glide.with(context)
                    .load(reply.getUserGeneral().getAvt())
                    .centerCrop()
                    .into(imgAvatar);

            //TODO: set click event to reply
            tvReply.setOnClickListener(v -> {
                if (onReplyClickListener != null)
                    onReplyClickListener.onReplyClick(reply);
            });

            //TODO: show more reply
            if (reply.isShowChild() ){
                if (reply.isLast()){
                    tvShowMoreChildReply.setVisibility(View.GONE);
                    tvHideChildReply.setVisibility(View.VISIBLE);

                    tvHideChildReply.setOnClickListener(v -> {
                        rcvChildReplies.setVisibility(View.GONE);
                        tvHideChildReply.setVisibility(View.GONE);
                        reply.getListChild().clear();
                        replyChildAdapter.clearData();
                        reply.setShowChild(false);
                        onReplyClickListener.onHideChildReply(reply);
                        notifyItemChanged(pos);
                    });
                }
                else{
                    tvShowMoreChildReply.setVisibility(View.VISIBLE);
                    tvHideChildReply.setVisibility(View.GONE);

                    tvShowMoreChildReply.setOnClickListener(v -> {
                        onReplyClickListener.onShowChildReply(reply);
                    });
                }

            }
            else{
                tvShowMoreChildReply.setVisibility(View.GONE);
                tvHideChildReply.setVisibility(View.GONE);
            }

            //TODO: show reply child
            if (reply.isHasChild()) {
                if (!reply.isShowChild()) {
                    reply.setShowChild(true);
                    tvShowChildReply.setVisibility(View.VISIBLE);
                }
                else{
                    tvShowChildReply.setVisibility(View.GONE);
                }
                //recyclerview set up and add data
                List<ReplyResponse> children = reply.getListChild();
                replyChildAdapter.setData(children != null ? children : new ArrayList<>());

                rcvChildReplies.setVisibility(
                        reply.isHasChild() && reply.isShowChild() && children != null && !children.isEmpty()
                                ? View.VISIBLE
                                : View.GONE
                );

                //show child
                tvShowChildReply.setOnClickListener(v -> {
                    tvShowChildReply.setVisibility(View.GONE);
                    onReplyClickListener.onShowChildReply(reply);
                });
            }
            else{
                tvShowChildReply.setVisibility(View.GONE);
                rcvChildReplies.setVisibility(View.GONE);
            }
            //TODO: menu actions
            imgMoreActions.setOnClickListener(v ->{
                // Tạo PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, v);

                //if is your reply have 3 selections
                if (reply.isOwner()) {
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
                            onMenuActionListener.onCopy(reply.getContent());
                        }
                        else if (itemId == R.id.actionEdit){
                            onMenuActionListener.onUpdate(reply.getId(), reply.getContent(), pos);
                        }
                        else if (itemId == R.id.actionDelete){
                            onMenuActionListener.onDelete(reply.getId(), pos);
                        }
                        return true;
                    }
                });

                // Hiển thị menu
                popupMenu.show();
            });
        }
    }
}

