package com.eugene.android_yandex.image_gallery;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.eugene.android_yandex.App;
import com.eugene.android_yandex.R;
import com.eugene.android_yandex.data.model.Photo;
import com.eugene.android_yandex.data.source.local.AppDatabase;
import com.eugene.android_yandex.data.source.remote.UnsplashServer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImageGalleryViewModel extends AndroidViewModel {

    private MutableLiveData<List<Photo>> photos;
    private MutableLiveData<Boolean> isDataLoadingError;
    private final Context context;
    private final String unsplashKey;
    private static UnsplashServer unsplashServer;
    private static AppDatabase appDatabase;

    public ImageGalleryViewModel(@NonNull Application application) {
        super(application);
        photos = new MutableLiveData<>();
        isDataLoadingError = new MutableLiveData<>();
        photos.setValue(new ArrayList<>());
        unsplashKey = application.getApplicationContext().getResources()
                .getString(R.string.unsplash_key);
        context = application.getApplicationContext();
        unsplashServer = App.getInstance().getUnsplashServer();
        appDatabase = App.getInstance().getAppDatabase();
    }

    public LiveData<List<Photo>> getPhotoLiveData() {
        return photos;
    }

    public LiveData<Boolean> getLoadingErrorLiveData() {
        return isDataLoadingError;
    }

    public void loadPhotos(boolean isCacheFirst) {
        if (isCacheFirst) {
            getPhotosFromDbAsync();
        } else {
            getPhotosFromNetwork();
        }
    }

    private void getPhotosFromNetwork() {
        unsplashServer.getPhotos(unsplashKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photos -> {
                            if (photos.size() > 0) {
                                cleanGlideCacheAndUpdatePhotos(photos);
                            }
                            isDataLoadingError.setValue(false);
                        },
                        throwable -> isDataLoadingError.setValue(true)
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

    private void getPhotosFromDbAsync() {
        if (photos.getValue() != null) {
            photos.getValue().clear();
        }
        Single.fromCallable(this::getPhotosFromDb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    if (photos.size() > 0) {
                        this.photos.setValue(photos);
                    } else {
                        getPhotosFromNetwork();
                    }
                });
    }

    private List<Photo> getPhotosFromDb() {
        return appDatabase.getPhotoDao().getPhotos();
    }

    private void cleanGlideCacheAndUpdatePhotos(List<Photo> photos) {
        Glide.get(context).clearMemory();
        Single.fromCallable(this::cleanGlideCache)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (this.photos.getValue() != null) {
                        this.photos.getValue().clear();
                    }
                    this.photos.setValue(photos);
                    updateDbAsync(photos);
                });
    }

    private int cleanGlideCache() {
        Glide.get(context).clearDiskCache();
        return 0;
    }
}