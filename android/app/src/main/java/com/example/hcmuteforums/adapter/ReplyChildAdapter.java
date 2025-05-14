package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.OnReplyClickListener;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.glailton.expandabletextview.ExpandableTextView;

public class ReplyChildAdapter extends RecyclerView.Adapter<ReplyChildAdapter.ReplyViewHolder> {


    private List<ReplyResponse> replyList;
    private OnReplyClickListener listener;
    private Context context;
    public ReplyChildAdapter(Context context, OnReplyClickListener listener) {
        this.listener = listener;
        this.context = context;
        replyList = new ArrayList<>();
    }
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
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ReplyChildAdapter.ReplyDiffCallback(oldList, newList));
        replyList = new ArrayList<>(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void setData(List<ReplyResponse> newList) {
        List<ReplyResponse> oldList = new ArrayList<>(replyList);
        applyDiffUtil(oldList, newList != null ? newList : new ArrayList<>());
    }

    public void addData(List<ReplyResponse> newList) {
        List<ReplyResponse> oldList = new ArrayList<>(replyList);
        List<ReplyResponse> updatedList = new ArrayList<>(replyList);
        updatedList.addAll(newList != null ? newList : new ArrayList<>());
        applyDiffUtil(oldList, updatedList);
    }


    public void addNewReply(ReplyResponse newReply) {
        List<ReplyResponse> oldList = new ArrayList<>(replyList);
        List<ReplyResponse> updatedList = new ArrayList<>(replyList);
        if (newReply != null) {
            updatedList.add(0, newReply);
        }
        applyDiffUtil(oldList, updatedList);
    }

    public void clearData() {
        List<ReplyResponse> oldList = new ArrayList<>(replyList);
        applyDiffUtil(oldList, new ArrayList<>());
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply_child, parent, false);
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
        TextView tvUsername, tvReply;
        TextView tvContent, tvToggle, tvTargetUserName;
        CircleImageView imgAvatar;
        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvToggle = itemView.findViewById(R.id.tvToggle);
            tvTargetUserName = itemView.findViewById(R.id.tvTargetUserName);
            tvReply = itemView.findViewById(R.id.tvReply);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
        public void bind(ReplyResponse reply, int pos){
            //TODO: set reply target user
            tvUsername.setText(reply.getUserGeneral().getFullName());
            if (reply.getTargetUserName() != null && !Objects.equals(reply.getTargetUserName(), "")){
                tvTargetUserName.setVisibility(View.VISIBLE);
                tvTargetUserName.setText("@" + reply.getTargetUserName() + " ");
            }
            else{
                tvTargetUserName.setVisibility(View.GONE);
            }
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
            //TODO: up load avatar
            Glide.with(context)
                    .load(reply.getUserGeneral().getAvt())
                    .centerCrop()
                    .into(imgAvatar);
            tvReply.setOnClickListener(v -> {
                if (listener != null)
                    listener.onReplyClick(reply);
            });

        }
    }

}

