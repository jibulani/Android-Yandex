package com.eugene.android_yandex.image_gallery;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eugene.android_yandex.R;
import com.eugene.android_yandex.full_photo.FullPhotoActivity;
import com.eugene.android_yandex.data.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>  {

    private List<Photo> photos;
    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
        this.photos = new ArrayList<>();
    }

    public void updatePhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public void updateContext(Context context) {
        this.context = context;
    }

    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.image_elem, parent, false);
        ImageAdapter.MyViewHolder viewHolder = new ImageAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.MyViewHolder holder, int position) {
        Photo photo = photos.get(position);
        ImageView imageView = holder.mPhotoImageView;
        RequestOptions options = new RequestOptions().error(android.R.drawable.ic_menu_report_image);
        Glide.with(context)
                .load(photo.getUrls().getSmall())
                .apply(options)
                .into(imageView);

        //For caching full photo
        Glide.with(context)
                .load(photo.getUrls().getRegular()).submit();
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Photo currPhoto = photos.get(position);
                Intent intent = new Intent(context, FullPhotoActivity.class);
                intent.putExtra(FullPhotoActivity.IMAGE_URL, currPhoto.getUrls().getRegular());
                context.startActivity(intent);
            }
        }
    }
}