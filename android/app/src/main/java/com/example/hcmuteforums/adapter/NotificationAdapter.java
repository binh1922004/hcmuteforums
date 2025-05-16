package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.OnNotificationClickListener;
import com.example.hcmuteforums.model.dto.NotificationDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private List<NotificationDTO > notificationList;
    private OnNotificationClickListener onNotificationClickListener;
    private Context context;
    public NotificationAdapter(Context context, OnNotificationClickListener onNotificationClickListener) {
        this.context = context;
        this.onNotificationClickListener = onNotificationClickListener;
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
        public void bind(NotificationDTO notification, int pos){
            Log.d("NoticationAdapter", notification.getCreatedAt()+"");
            //TODO: set up for content of notification
            tvContent.setText(notification.getContent());
            String sender = notification.getSenderName();
            String message = sender + notification.getContent();
            SpannableString spannable = new SpannableString(message);

            // In đậm tên người gửi
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.setText(spannable);

            //TODO: Up avatar
            Glide.with(context)
                    .load(notification.getSendUserAvatar())
                    .centerCrop()
                    .into(imgAvatar);

            if (!Objects.equals(notification.getType(), "LIKE")){
                imgAction.setImageResource(R.drawable.ic_comment);
            }
            else {
                imgAction.setImageResource(R.drawable.love_click);
            }

            itemView.setOnClickListener(v -> {
                if (Objects.equals(notification.getType(), "LIKE")) {
                    onNotificationClickListener.onClickLike(notification.getTopicId());
                }
                else if (Objects.equals(notification.getType(), "FOLLOW")){
                    onNotificationClickListener.onClickFollow(notification.getSenderName());
                }
                else{
                    onNotificationClickListener.onClickReply(notification.getTopicId(), notification.getActionId());
                }
            });

            Log.d("NoticationAdapter", notification.getCreatedAt()+"");
            tvTime.setText(timeAgo(notification.getCreatedAt()));
        }
        public String timeAgo(Date createdAt) {
            // Lấy thời gian hiện tại
            // Sử dụng Calendar
            Calendar nowCal = Calendar.getInstance();
            Calendar createdCal = Calendar.getInstance();
            createdCal.setTime(createdAt);

            // Tính khoảng cách thời gian
            long nowMillis = nowCal.getTimeInMillis();
            long createdMillis = createdCal.getTimeInMillis();
            long diffMillis = nowMillis - createdMillis;
            long seconds = diffMillis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            // Xác định thời gian hiển thị
            if (seconds < 60) {
                return seconds + " giây trước";
            } else if (minutes < 60) {
                return minutes + " phút trước";
            } else if (hours < 24) {
                return hours + " tiếng trước";
            } else {
                return days + " ngày trước";
            }
        }
    }

}

