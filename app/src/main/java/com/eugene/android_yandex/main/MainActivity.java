package com.eugene.android_yandex.main;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eugene.android_yandex.R;

public class MainActivity extends AppCompatActivity {

    private static final String GALLERY_FRAGMENT_TAG = "gallery_fragment";

    private FloatingActionButton updateButton;
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
        updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(v -> getPhotos());
    }

    private void getPhotos() {
            imageGalleryFragment.updateAdapterDataFromNetwork();
    }
}
