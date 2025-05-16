package com.example.hcmuteforums.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.listeners.ImageActionListener;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ImageUploadViewHolder> {
    private Context context;
    private List<Object> imageList;
    private ImageActionListener imageActionListener;

    public ImageUploadAdapter(Context context, ImageActionListener imageActionListener) {
        this.context = context;
        this.imageActionListener = imageActionListener;
        this.imageList = new ArrayList<>();
        Log.d("ImageUploadAdapter", "Adapter initialized with empty imageList");
    }

    public List<Uri> getImageList() {
        List<Uri> listUri = new ArrayList<>();
        for (Object item : imageList) {
            if (item instanceof Uri) {
                listUri.add((Uri) item);
            }
        }
        Log.d("ImageUploadAdapter", "getImageList: " + listUri.size() + " URIs");
        return listUri;
    }

    public void addImage(Uri uri) {
        if (uri != null) {
            imageList.add(uri);
            notifyItemInserted(imageList.size() - 1);
            Log.d("ImageUploadAdapter", "Added Uri: " + uri);
        }
    }

    public void addImagesFromUrls(List<String> imageUrls) {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            Log.d("ImageUploadAdapter", "Adding URLs: " + imageUrls.size() + " - " + imageUrls);
            imageList.addAll(imageUrls);
            notifyDataSetChanged();
        } else {
            Log.d("ImageUploadAdapter", "imageUrls is null or empty");
        }
    }

    public void removeImage(int pos) {
        if (pos >= 0 && pos < imageList.size()) {
            Object removedItem = imageList.remove(pos);
            notifyItemRemoved(pos);
            Log.d("ImageUploadAdapter", "Removed image at position: " + pos + ", Item: " + removedItem);
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
        if (imageList.isEmpty()) {
            Log.d("ImageUploadAdapter", "imageList is empty in onBindViewHolder");
            return;
        }
        Object imageUri = imageList.get(position);
        Log.d("ImageUploadAdapter", "Binding position: " + position + ", Image: " + imageUri);
        holder.bind(imageUri, position);
    }

    @Override
    public int getItemCount() {
        int size = imageList != null ? imageList.size() : 0;
        Log.d("ImageUploadAdapter", "getItemCount: " + size);
        return size;
    }

    public List<Object> getAll() {
        return imageList;
    }

    public class ImageUploadViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagePreview, btnRemoveImage;

        public ImageUploadViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePreview = itemView.findViewById(R.id.imagePreview);
            btnRemoveImage = itemView.findViewById(R.id.btnRemoveImage);
        }

        public void bind(Object imageUri, final int pos) {
            Log.d("ImageUploadAdapter", "Binding image: " + imageUri);
            if (imageUri instanceof Uri) {
                Glide.with(context)
                        .load((Uri) imageUri)
                        .centerCrop()
                        .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Glide", "Failed to load Uri: " + imageUri, e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                Log.d("Glide", "Loaded Uri: " + imageUri);
                                return false;
                            }
                        })
                        .into(imagePreview);
            } else if (imageUri instanceof String) {
                Glide.with(context)
                        .load((String) imageUri)
                        .centerCrop()
                        .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Glide", "Failed to load URL: " + imageUri, e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                Log.d("Glide", "Loaded URL: " + imageUri);
                                return false;
                            }
                        })
                        .into(imagePreview);
            }
            btnRemoveImage.setOnClickListener(v -> {
                if (imageActionListener != null) {
                    imageActionListener.onImageRemove(pos);
                }
            });
        }
    }
}