package com.example.a500pxpopularphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ortiz.touchview.TouchImageView;

public class FullscreenImageActivity extends BaseActivity {
    TouchImageView mFullscreenImage;
    public static String IMAGE_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        mFullscreenImage = (TouchImageView) findViewById(R.id.fullscreen_image);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(IMAGE_URL);

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mFullscreenImage.setImageBitmap(resource);
                    }
                });
    }
}
