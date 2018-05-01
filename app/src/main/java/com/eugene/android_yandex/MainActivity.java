package com.eugene.android_yandex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.eugene.android_yandex.model.Photo;
import com.eugene.android_yandex.network.UnsplashServer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private UnsplashServer unsplashServer;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unsplashServer = new UnsplashServer();
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> getPhotos(v.getContext().getResources().getString(R.string.unsplash_key)));
    }

    //TODO: remove
    private void getPhotos(String clientId) {
        unsplashServer.getPhotos(clientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    for (Photo photo : photos) {
                        System.out.println(photo.getId() + " " + photo.getUrls().getFull());
                    }
                });
    }
}
