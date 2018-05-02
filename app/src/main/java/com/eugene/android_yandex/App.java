package com.eugene.android_yandex;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.eugene.android_yandex.data.source.local.AppDatabase;
import com.eugene.android_yandex.data.source.remote.UnsplashServer;

public class App extends Application {

    public static App instance;
    private AppDatabase appDatabase;
    private UnsplashServer unsplashServer;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
        unsplashServer = new UnsplashServer();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public UnsplashServer getUnsplashServer() {
        return unsplashServer;
    }
}
