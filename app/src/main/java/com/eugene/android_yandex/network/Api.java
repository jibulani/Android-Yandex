package com.eugene.android_yandex.network;


import com.eugene.android_yandex.model.Photo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("photos/random")
    Single<List<Photo>> getPhotos(@Query("client_id") String clientId, @Query("count") int count);
}
