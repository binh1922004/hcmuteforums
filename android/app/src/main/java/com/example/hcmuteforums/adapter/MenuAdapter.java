package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.modelAdapter.MenuItemModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private List<MenuItemModel> menuList;
    private Context context;
    private OnItemClickListener listener;
    private String avatarUrl, fullname;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onHeaderClick();
    }
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }
    public void setAvatarProfile(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        notifyDataSetChanged();  // Cập nhật lại UI khi avatar thay đổi
    }
    public void setFullname(String fullname){
        this.fullname = fullname;
        notifyDataSetChanged();
    }


    public MenuAdapter(Context context, List<MenuItemModel> menuList,OnItemClickListener listener) {
        this.context = context;
        this.menuList = menuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_menu_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
            return new MenuViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            MenuItemModel item = menuList.get(position - 1); // Trừ 1 vì header chiếm vị trí 0
            MenuViewHolder vh = (MenuViewHolder) holder;
            vh.menuIcon.setImageResource(item.getIconResId());
            vh.menuTitle.setText(item.getTitle());
            vh.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(position - 1);
            });
        } else {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.userName.setText(fullname);
            vh.userSubText.setText("Chuyển giữa các trang cá nhân mà không phải đăng nhập lại.");
            loadImage(vh);
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size()+1;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView menuIcon, menuArrow;
        TextView menuTitle;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuIcon = itemView.findViewById(R.id.menuIcon);
            menuTitle = itemView.findViewById(R.id.menuTitle);
            menuArrow = itemView.findViewById(R.id.menuArrow);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView userName, userSubText;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatarImageView);
            userName = itemView.findViewById(R.id.userNameTextView);
            userSubText = itemView.findViewById(R.id.userSubTextView);

        }
    }
    void loadImage(HeaderViewHolder vh){
        if(avatarUrl!=null && !avatarUrl.isEmpty()){
            Log.d("MenuAdapterError", avatarUrl);
            Glide.with(context)
                    .load(avatarUrl)
                    .placeholder(R.drawable.avatar_boy)
                    .error(R.drawable.anhhoixua)
                    .into(vh.avatar);
        }
        else{
            Glide.with(context)
                    .load(R.drawable.avatar_boy)
                    .into(vh.avatar);
        }
        vh.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHeaderClick();
            }
        });
    }
}
