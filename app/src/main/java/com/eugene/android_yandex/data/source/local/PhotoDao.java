package com.eugene.android_yandex.data.source.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.eugene.android_yandex.data.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhotos(Photo... photos);

    @Delete
    void deletePhotos(Photo... photos);

    @Query("SELECT * FROM photo")
    List<Photo> getPhotos();
}
