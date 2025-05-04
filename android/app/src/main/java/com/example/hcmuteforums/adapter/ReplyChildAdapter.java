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
    public void setData(List<ReplyResponse> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return replyList != null ? replyList.size() : 0;
            }

            @Override
            public int getNewListSize() {
                return newList != null ? newList.size() : 0;
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return replyList.get(oldItemPosition).getId()
                        .equals(newList.get(newItemPosition).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return replyList.get(oldItemPosition).getContent().equals(newList.get(newItemPosition).getContent());
            }
        });

        this.replyList = new ArrayList<>(newList);
        diffResult.dispatchUpdatesTo(this); // Chỉ cập nhật phần thay đổi
    }

    public void addData(List<ReplyResponse> newList){
        int oldSize = replyList.size();
        replyList.addAll(newList);
        notifyItemInserted(oldSize);
    }
    public void addNewReply(ReplyResponse newReply){
        replyList.add(0, newReply);
        notifyItemInserted(0);
    }


    public void clearData() {
        replyList.clear();
        notifyItemChanged(0);
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
        holder.bind(reply);
    }

    @Override
    public int getItemCount() {
        if (replyList == null)
            return 0;
        return replyList.size();
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        ExpandableTextView tvContent;
        CircleImageView imgAvatar;
        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvContent = itemView.findViewById(R.id.tvContent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
        public void bind(ReplyResponse reply){
            tvUsername.setText(reply.getUserGeneral().getFullName());
            tvContent.setText(reply.getContent());
            Glide.with(context)
                    .load(reply.getUserGeneral().getAvt())
                    .centerCrop()
                    .into(imgAvatar);
            tvContent.setOnClickListener(v -> {
                if (listener != null)
                    listener.onReplyClick(reply);
            });

        }
    }

}

