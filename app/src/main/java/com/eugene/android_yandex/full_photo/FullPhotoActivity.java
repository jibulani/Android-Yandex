package com.eugene.android_yandex.full_photo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eugene.android_yandex.R;

public class FullPhotoActivity extends AppCompatActivity {

    public static final String IMAGE_URL = "image_url";
    private ImageView imageView;
    private String imageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_photo_activity);
        imageView = findViewById(R.id.full_image);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            imageUrl = intent.getExtras().getString(IMAGE_URL);
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }

    }
}
