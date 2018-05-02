package com.eugene.android_yandex.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eugene.android_yandex.R;
import com.eugene.android_yandex.model.Photo;

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
        Glide.with(context)
                .load(photo.getUrls().getSmall())
                .into(imageView);
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
//            if(position != RecyclerView.NO_POSITION) {
//                SpacePhoto spacePhoto = mSpacePhotos[position];
//                Intent intent = new Intent(mContext, SpacePhotoActivity.class);
//                intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
//                startActivity(intent);
//            }
        }
    }
}