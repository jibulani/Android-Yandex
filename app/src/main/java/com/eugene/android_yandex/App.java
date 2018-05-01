package com.eugene.android_yandex;


import android.app.Application;

import com.eugene.android_yandex.network.UnsplashServer;

public class App extends Application {

    public static App instance;
    private UnsplashServer unsplashServer;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

}
