package com.eugene.android_yandex.data.source.remote;


import com.eugene.android_yandex.data.model.Photo;
import com.eugene.android_yandex.data.source.remote.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UnsplashServer {

    private static final String BASE_URL = "https://api.unsplash.com/";
    private static final int PHOTOS_COUNT = 10;
    private final Api api;

    public UnsplashServer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        api = retrofit.create(Api.class);
    }

    public Single<List<Photo>> getPhotos(String clientId) {
        return api.getPhotos(clientId, PHOTOS_COUNT);
    }
}
