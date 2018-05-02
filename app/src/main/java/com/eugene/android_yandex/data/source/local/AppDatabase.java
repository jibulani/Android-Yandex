package com.eugene.android_yandex.data.source.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.eugene.android_yandex.data.model.Photo;

@Database(entities = {Photo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PhotoDao getPhotoDao();
}
