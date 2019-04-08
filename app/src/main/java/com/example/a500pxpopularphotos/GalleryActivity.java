package com.example.a500pxpopularphotos;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.a500pxpopularphotos.api.ImageSize;
import com.example.a500pxpopularphotos.event.ScollEndEvent;
import com.example.a500pxpopularphotos.pojo.PagedPhotos;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryActivity extends BaseActivity {
    SwipeRefreshLayout mRefresh;
    int mGalleryItemWidth;
    int mScreenHeight;
    ImageSize mImageSize = new ImageSize();

    View mErrorView;
    View mGalleryFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRefresh = findViewById(R.id.refresh);
        mGalleryFrame = findViewById(R.id.gallery_frame);
        mErrorView = findViewById(R.id.error_view);

        mErrorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // prevent any click events from propogating to gallery
                return true;
            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // stop all ongoing requests for new pages
                okHttpClient.dispatcher().cancelAll();

                // request for page 1 again
                requestPopular(1);
            }
        });

        // fetch horizontal length for width of each gallery item
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mGalleryItemWidth = width / BrowsePopularFragment.NUM_COLUMNS;
        mScreenHeight = height;

        // Load recyclerview into framelayout
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.gallery_frame, BrowsePopularFragment.newInstance())
                .commit();

        requestPopular(1);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPopular(int page) {
        // Calculate image sizes
        // fetch uncropped image for thumbnails
        int thumbnailId = 20;
        int fullscreenId = mImageSize.CalculateLongestEdge(mScreenHeight);
        String imageIds = Integer.toString(thumbnailId) + ',' + Integer.toString(fullscreenId);
        Log.d("NET", "item width " + mGalleryItemWidth + " screen heigh " + mScreenHeight  + " image_size IDs " + imageIds);

        // send request to api for popular photos
        api.getPopular(page, imageIds)
                .enqueue(new Callback<PagedPhotos>() {
            @Override
            public void onResponse(Call<PagedPhotos> call, Response<PagedPhotos> response) {
                // remove outstanding sticky events for page requests, if any
                EventBus.getDefault().removeStickyEvent(ScollEndEvent.class);

                if (mRefresh.isRefreshing()) {
                    // Display toast to indicate refresh completed
                    Toast.makeText(getApplicationContext(),
                            "Displaying newest popular photos",
                            Toast.LENGTH_SHORT).show();
                }

                // dismiss pull down refresh circle
                mRefresh.setRefreshing(false);

                Log.v("NET", "Popular page request success " + response.body().getCurrent_page());
                EventBus.getDefault().post(response.body());

                mGalleryFrame.bringToFront();
            }

            @Override
            public void onFailure(Call<PagedPhotos> call, Throwable t) {
                // remove outstanding sticky events for page requests, if any
                EventBus.getDefault().removeStickyEvent(ScollEndEvent.class);

                // dismiss pull down refresh circle
                mRefresh.setRefreshing(false);

                Log.e("NET", "Popular page request failed");
                mErrorView.bringToFront();
            }
        });
    }

    @Subscribe
    public void onScrollEndEvent(ScollEndEvent e) {
        // fetch more pages
        Log.d("NET", "Requesting popular page " + e.page);
        requestPopular(e.page);
    }
}
