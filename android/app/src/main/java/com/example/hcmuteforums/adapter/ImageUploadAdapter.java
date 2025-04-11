package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.ImageActionListener;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ImageUploadViewHolder> {
    private Context context;
    private List<Uri> imageList;
    private ImageActionListener imageActionListener;

    public ImageUploadAdapter(Context context, ImageActionListener imageActionListener) {
        this.context = context;
        this.imageActionListener = imageActionListener;
        imageList = new ArrayList<>();
    }

    public void addImage(Uri uri){
        imageList.add(uri);
        notifyItemInserted(imageList.size() - 1);
    }

    public void removeImage(int pos){
        if (pos >= 0 && pos < imageList.size()){
            imageList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    @NonNull
    @Override
    public ImageUploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_upload, parent, false);
        return new ImageUploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageUploadViewHolder holder, int position) {
        Uri imageUri = imageList.get(position);
        holder.bind(imageUri, position);
    }

    @Override
    public int getItemCount() {
        if (imageList != null)
            return imageList.size();
        return 0;
    }

    public class ImageUploadViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagePreview, btnRemoveImage;
        public ImageUploadViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePreview = itemView.findViewById(R.id.imagePreview);
            btnRemoveImage = itemView.findViewById(R.id.btnRemoveImage);
        }

        public void bind(Uri imageUri, final int pos){
            Glide.with(context)
                    .load(imageUri)
                    .centerCrop()
                    .into(imagePreview);
            btnRemoveImage.setOnClickListener(v -> {
                if (imageActionListener != null){
                    imageActionListener.onImageRemove(pos);
                }
            });
        }
    }
}
