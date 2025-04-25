package com.example.hcmuteforums.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    public interface OnReplyClickListener {
        void onReplyClick(ReplyResponse reply);
    }

    private List<ReplyResponse> replyList;
    private OnReplyClickListener listener;
    public ReplyAdapter(List<ReplyResponse> replyList, OnReplyClickListener listener) {
        this.replyList = replyList;
        this.listener = listener;
    }

    public void setData(List<ReplyResponse> replyList){
        this.replyList = replyList;
        notifyDataSetChanged();
    }
    public void addData(List<ReplyResponse> newList){
        int oldSize = replyList.size();
        replyList.addAll(newList);
        notifyItemInserted(oldSize);
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
        holder.tvUsername.setText(reply.getUserGeneral().getFullName());
        holder.tvContent.setText(reply.getContent());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onReplyClick(reply);
        });
    }

    @Override
    public int getItemCount() {
        if (replyList == null)
            return 0;
        return replyList.size();
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvContent;
        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}

