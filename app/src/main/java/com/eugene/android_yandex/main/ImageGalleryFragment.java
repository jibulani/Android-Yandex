package com.eugene.android_yandex.main;


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
import com.eugene.android_yandex.network.UnsplashServer;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImageGalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private static UnsplashServer unsplashServer;

    public static ImageGalleryFragment newInstance() {
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        unsplashServer = new UnsplashServer();
        return imageGalleryFragment;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getActivity(), 2);
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
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.getItemAnimator().setAddDuration(700);
        recyclerView.getItemAnimator().setChangeDuration(700);
        recyclerView.getItemAnimator().setMoveDuration(700);
        recyclerView.getItemAnimator().setRemoveDuration(700);
        recyclerView.setHasFixedSize(true);

        imageAdapter = getImageAdapter();
        imageAdapter.updateContext(getContext());
        recyclerView.setAdapter(imageAdapter);
    }

    public void updateAdapterData(String clientId) {
        unsplashServer.getPhotos(clientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    getImageAdapter().updatePhotos(photos);
                });
    }

}
