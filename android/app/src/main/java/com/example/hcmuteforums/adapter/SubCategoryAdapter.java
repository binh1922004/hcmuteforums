package com.example.hcmuteforums.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.entity.SubCategory;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {
    private List<SubCategory> subCategories;

    public void setData(List<SubCategory> subCategoryList){
        this.subCategories = subCategoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_category, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubCategory subCategory = subCategories.get(position);
        if (subCategory == null)
            return;
        holder.tvSubCategoryName.setText(subCategory.getName());
    }

    @Override
    public int getItemCount() {
        if (subCategories != null)
            return subCategories.size();

        return 0;
    }

    public static class SubCategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubCategoryName;
        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubCategoryName = itemView.findViewById(R.id.tvSubCategoryName);
        }
    }
}
