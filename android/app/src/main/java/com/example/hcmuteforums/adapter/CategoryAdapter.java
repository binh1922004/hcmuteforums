package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.entity.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context){
        this.context = context;
    }

    public void setData(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category == null)
            return;
        holder.tvCategory.setText(category.getName());
        if (category.isExpanded()){
            holder.container.setBackgroundResource(R.drawable.rounded_bg_selected);
        }
        else{
            holder.container.setBackgroundResource(R.drawable.rounded_bg);
        }
        boolean isExpanded = category.isExpanded();
        holder.rcvSubCategories.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        //set event click
        holder.container.setOnClickListener(v -> {
            for (int i = 0; i < categoryList.size(); i++){
                if (i != position && categoryList.get(i).isExpanded()) {
                    categoryList.get(i).setExpanded(false);
                    notifyItemChanged(i);
                }
            }
            category.setExpanded(!isExpanded);
            notifyItemChanged(position);
        });

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter();
        subCategoryAdapter.setData(category.getSubCategories());
        holder.rcvSubCategories.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), RecyclerView.VERTICAL, false));
        holder.rcvSubCategories.setAdapter(subCategoryAdapter);
    }

    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        RecyclerView rcvSubCategories;
        LinearLayout container;  // Thêm container để đổi màu viền
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            rcvSubCategories = itemView.findViewById(R.id.rcvSubCategories);
            container = itemView.findViewById(R.id.linearCategory);
        }
    }
}
