package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.modelAdapter.MenuItemModel;

import java.util.List;

public class MenuAdapter  extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder>{
    private List<MenuItemModel> menuList;
    private Context context;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MenuAdapter(Context context, List<MenuItemModel> menuList,OnItemClickListener listener) {
        this.context = context;
        this.menuList = menuList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItemModel item = menuList.get(position);
        holder.menuIcon.setImageResource(item.getIconResId());
        holder.menuTitle.setText(item.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
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
}
