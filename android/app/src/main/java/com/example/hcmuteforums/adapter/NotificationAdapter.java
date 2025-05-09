package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.NotificationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private List<NotificationDTO > notificationList;
    private Context context;
    public NotificationAdapter(Context context) {
        this.context = context;
        notificationList = new ArrayList<>();
    }

    public void addData(List<NotificationDTO> notificationList){
        int oldSize = this.notificationList.size();
        this.notificationList.addAll(notificationList);
        notifyItemInserted(oldSize);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationDTO notificationDTO = notificationList.get(position);
        if (notificationDTO != null){
            holder.bind(notificationDTO, position);
        }
    }

    @Override
    public int getItemCount() {
        if (notificationList == null)
            return 0;
        return notificationList.size();
    }

    public void clearData() {
        int size = this.notificationList.size();
        if (size > 0) {
            this.notificationList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvTime;
        CircleImageView imgAvatar;
        ImageButton imgAction;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgAction = itemView.findViewById(R.id.imgAction);
        }
        public void bind(NotificationDTO noti, int pos){
            //TODO: set up for content of notification
            tvContent.setText(noti.getContent());
            String sender = noti.getSenderName();
            String message = sender + noti.getContent();
            SpannableString spannable = new SpannableString(message);

            // In đậm tên người gửi
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.setText(spannable);

            //TODO: Up avatar
            Glide.with(context)
                    .load(noti.getSendUserAvatar())
                    .centerCrop()
                    .into(imgAvatar);

            if (!Objects.equals(noti.getType(), "LIKE")){
                imgAction.setImageResource(R.drawable.ic_comment);
            }
        }
    }

}

