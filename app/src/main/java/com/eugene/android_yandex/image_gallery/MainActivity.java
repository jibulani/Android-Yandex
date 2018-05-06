package com.eugene.android_yandex.image_gallery;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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
        ImageGalleryViewModel imageGalleryViewModel = obtainViewModel(this);
        LiveData<Boolean> errorData = imageGalleryViewModel.getLoadingErrorLiveData();
        errorData.observe(this, isDataLoadingError -> {
            if (isDataLoadingError != null && isDataLoadingError) {
                Toast.makeText(this, "Error while loading photos", Toast.LENGTH_LONG).show();
            }
        });
        updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(v -> imageGalleryViewModel.loadPhotos(false));
    }

    public static ImageGalleryViewModel obtainViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(ImageGalleryViewModel.class);
    }
}
