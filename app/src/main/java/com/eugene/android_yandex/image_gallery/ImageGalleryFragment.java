package com.eugene.android_yandex.image_gallery;


import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eugene.android_yandex.R;
import com.eugene.android_yandex.data.model.Photo;


import java.util.List;


public class ImageGalleryFragment extends Fragment {

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ImageGalleryViewModel imageGalleryViewModel;

    public static ImageGalleryFragment newInstance() {
        return new ImageGalleryFragment();
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getActivity(), NUMBER_OF_COLUMNS);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    protected ImageAdapter getImageAdapter() {
        return imageAdapter != null ? imageAdapter : new ImageAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_gallery_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.images);
        imageGalleryViewModel = MainActivity.obtainViewModel(getActivity());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setHasFixedSize(true);

        imageAdapter = getImageAdapter();
        imageAdapter.updateContext(getContext());

        LiveData<List<Photo>> photosData = imageGalleryViewModel.getPhotoLiveData();
        photosData.observe(this, photos -> {
            if (photos != null && !photos.isEmpty()) {
                imageAdapter.updatePhotos(photos);
            }
        });
        LiveData<Boolean> loadingData = imageGalleryViewModel.getIsLoadingLiveData();
        loadingData.observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                recyclerView.setVisibility(View.INVISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        if (imageAdapter.getItemCount() == 0) {
            imageGalleryViewModel.loadPhotos(true);
        }
        recyclerView.setAdapter(imageAdapter);
    }
}
