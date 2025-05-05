package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.OnReplyClickListener;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.glailton.expandabletextview.ExpandableTextView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {


    private List<ReplyResponse> replyList;
    private OnReplyClickListener listener;
    private Context context;
    public ReplyAdapter(Context context, List<ReplyResponse> replyList, OnReplyClickListener listener) {
        this.replyList = replyList;
        this.listener = listener;
        this.context = context;
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
    public void addNewReply(ReplyResponse newReply){
        replyList.add(0, newReply);
        notifyItemInserted(0);
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

