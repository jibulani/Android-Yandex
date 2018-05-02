package com.eugene.android_yandex.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eugene.android_yandex.R;
import com.eugene.android_yandex.model.Photo;
import com.eugene.android_yandex.network.UnsplashServer;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String GALLERY_FRAGMENT_TAG = "gallery_fragment";

    private Button button;
    private ImageGalleryFragment imageGalleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            imageGalleryFragment = ImageGalleryFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.image_gallery, imageGalleryFragment, GALLERY_FRAGMENT_TAG)
                    .commit();
        } else {
            imageGalleryFragment = (ImageGalleryFragment) getSupportFragmentManager()
                    .findFragmentByTag(GALLERY_FRAGMENT_TAG);
        }
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> getPhotos(v.getContext().getResources()
                .getString(R.string.unsplash_key)));
    }

    private void getPhotos(String clientId) {
            imageGalleryFragment.updateAdapterData(clientId);
    }
}
