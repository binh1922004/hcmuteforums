package com.example.hcmuteforums.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;

import java.util.List;

public class EditUserAdapter extends RecyclerView.Adapter<EditUserAdapter.ViewHolder> {
    private List<String> itemList;
    public EditUserAdapter(List<String> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public EditUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditUserAdapter.ViewHolder holder, int position) {
        String title = itemList.get(position);
        holder.tvTitle.setText(title);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgArrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgArrow = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
