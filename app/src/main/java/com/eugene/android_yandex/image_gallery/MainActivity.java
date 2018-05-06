package com.eugene.android_yandex.image_gallery;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eugene.android_yandex.R;

public class MainActivity extends AppCompatActivity {

    private static final String GALLERY_FRAGMENT_TAG = "gallery_fragment";

    private FloatingActionButton updateButton;
    private ImageGalleryFragment imageGalleryFragment;
    private ProgressBar progressBar;

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
        init();
    }

    public static ImageGalleryViewModel obtainViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(ImageGalleryViewModel.class);
    }

    private void init() {
        ImageGalleryViewModel imageGalleryViewModel = obtainViewModel(this);
        updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(v -> imageGalleryViewModel.loadPhotos(false));
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        setObservers(imageGalleryViewModel);
    }

    private void setObservers(ImageGalleryViewModel imageGalleryViewModel) {
        LiveData<Boolean> errorData = imageGalleryViewModel.getLoadingErrorLiveData();
        errorData.observe(this, isDataLoadingError -> {
            if (isDataLoadingError != null && isDataLoadingError) {
                Toast.makeText(this, "Error while loading photos", Toast.LENGTH_LONG).show();
            }
        });

        LiveData<Boolean> loadingData = imageGalleryViewModel.getIsLoadingLiveData();
        loadingData.observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
