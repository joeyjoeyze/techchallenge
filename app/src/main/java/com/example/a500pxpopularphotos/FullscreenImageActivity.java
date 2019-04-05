package com.example.a500pxpopularphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.a500pxpopularphotos.api.CategoryLookup;
import com.example.a500pxpopularphotos.api.FiveHundredPixel;
import com.example.a500pxpopularphotos.pojo.Photo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiz.touchview.TouchImageView;

import org.threeten.bp.Instant;
import org.threeten.bp.ZonedDateTime;

import java.io.IOException;

public class FullscreenImageActivity extends BaseActivity {
    public enum CardStatus {
        RAISED,
        HIDDEN,
        HALF,
    }
    public static String PHOTO_INFO = "PHOTO_INFO";
    public static String TAG = "FullscreenImageActivity";

    TouchImageView mFullscreenImage;
    GestureDetectorCompat mFling;
    CardView mInfoCard;
    TextView mFullname;
    TextView mTitle;
    TextView mCategory;
    TextView mTime;
    TextView mLocation;
    TextView mDescription;


    Photo mPhoto;
    CardStatus mCardRaised = CardStatus.HALF;
    int mDisplayHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        mFullscreenImage = (TouchImageView) findViewById(R.id.fullscreen_image);
        mInfoCard = (CardView) findViewById(R.id.fullscreen_card);
        mFullname = (TextView) findViewById(R.id.fullname);
        mTitle = (TextView) findViewById(R.id.title);
        mCategory = (TextView) findViewById(R.id.category);
        mTime = (TextView) findViewById(R.id.time);
        mLocation = (TextView) findViewById(R.id.location);
        mDescription = (TextView) findViewById(R.id.description);

        try {
            Intent intent = getIntent();
            String photoSer = intent.getStringExtra(PHOTO_INFO);
            mPhoto = new ObjectMapper().readValue(photoSer, Photo.class);
        } catch (IOException e) {
            Log.e("EX", e.getLocalizedMessage());
            finish();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mDisplayHeight = displayMetrics.heightPixels;

        mFullscreenImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("UI", "on touch event root");
                mFling.onTouchEvent(event);
                return true;
            }
        });

        mFling = new GestureDetectorCompat(this, new FlingGestureListener());

        Glide.with(this)
                .asBitmap()
                .load(mPhoto.getImage_url()[FiveHundredPixel.LARGE_IMG_INDEX])
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mFullscreenImage.setImageBitmap(resource);
                    }
                });

        mFullname.setText(mPhoto.getUser().getFullname());
        mTitle.setText(mPhoto.getName());
        mCategory.setText(new CategoryLookup().getCategory(mPhoto.getCategory()));
        mTime.setText(
                DateUtils.getRelativeTimeSpanString(
                        ZonedDateTime.parse(mPhoto.getCreated_at())
                                .toInstant()
                                .toEpochMilli()));

        String location = mPhoto.getLocation();
        Log.d(TAG, "" + location);
        if (location == null || location.equals("")) {
            ((View)mLocation.getParent()).setVisibility(View.GONE);
        } else {
            mLocation.setText(location);
        }

        String description = mPhoto.getDescription();
        if (description == null || description.equals("")) {
            mDescription.setVisibility(View.GONE);
        } else {
            mDescription.setText(description);
            mDescription.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    public class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getY() > e1.getY()) {
                Log.d("UI", "fling down");
                if (mCardRaised != CardStatus.HIDDEN) {
                    // just hide the entire card all together
                    mInfoCard.animate().translationY(mDisplayHeight);
                    mCardRaised = CardStatus.HIDDEN;
                }
            } else if (e1.getY() > e2.getY()) {
                Log.d("UI", "fling up");
                if (mCardRaised != CardStatus.RAISED) {
                    mInfoCard.animate().translationY(0);
                    mCardRaised = CardStatus.RAISED;
                }
            }
            return true;
        }
    }
}
