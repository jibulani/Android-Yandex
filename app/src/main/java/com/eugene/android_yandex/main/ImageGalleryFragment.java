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
import android.widget.Toast;

import com.eugene.android_yandex.App;
import com.eugene.android_yandex.R;
import com.eugene.android_yandex.data.model.Photo;
import com.eugene.android_yandex.data.source.local.AppDatabase;
import com.eugene.android_yandex.data.source.remote.UnsplashServer;


import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImageGalleryFragment extends Fragment {

    private static UnsplashServer unsplashServer;
    private static AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    public static ImageGalleryFragment newInstance() {
        ImageGalleryFragment imageGalleryFragment = new ImageGalleryFragment();
        unsplashServer = App.getInstance().getUnsplashServer();
        appDatabase = App.getInstance().getAppDatabase();
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
        if (imageAdapter.getItemCount() == 0) {
            updateAdapterData();
        }
        recyclerView.setAdapter(imageAdapter);
    }

    public void updateAdapterDataFromNetwork() {
        unsplashServer.getPhotos(getResources().getString(R.string.unsplash_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photos -> {
                            if (photos.size() > 0) {
                                getImageAdapter().updatePhotos(photos);
                                updateDbAsync(photos);
                            }
                        },
                        err -> Toast.makeText(getContext(), "Error while getting new images",
                                Toast.LENGTH_LONG).show()
                );
    }

    private void updateDbAsync(List<Photo> photos) {
        Single.fromCallable(() -> updateDb(photos))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private int updateDb(List<Photo> photos) {
        Photo[] oldPhotos = {};
        Photo[] newPhotos = {};
        appDatabase.getPhotoDao().deletePhotos(appDatabase.getPhotoDao()
                .getPhotos().toArray(oldPhotos));
        appDatabase.getPhotoDao().insertPhotos(photos.toArray(newPhotos));
        return 0;
    }

    private void updateAdapterData() {
        Single.fromCallable(this::getPhotosFromDb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    if (photos.size() > 0) {
                        getImageAdapter().updatePhotos(photos);
                    } else {
                        updateAdapterDataFromNetwork();
                    }
                });
    }

    private List<Photo> getPhotosFromDb() {
        return appDatabase.getPhotoDao().getPhotos();
    }

}
