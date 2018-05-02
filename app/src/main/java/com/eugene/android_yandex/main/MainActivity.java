package com.eugene.android_yandex.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eugene.android_yandex.R;
import com.eugene.android_yandex.model.Photo;
import com.eugene.android_yandex.network.UnsplashServer;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private UnsplashServer unsplashServer;
    private Button button;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unsplashServer = new UnsplashServer();
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> getPhotos(v.getContext().getResources()
                .getString(R.string.unsplash_key)));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ImageAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    //TODO: remove
    private void getPhotos(String clientId) {
        unsplashServer.getPhotos(clientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    adapter.updatePhotos(photos);
                });
    }
}
