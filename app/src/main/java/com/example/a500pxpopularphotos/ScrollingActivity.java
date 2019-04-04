package com.example.a500pxpopularphotos;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a500pxpopularphotos.event.ScollEndEvent;
import com.example.a500pxpopularphotos.pojo.PagedPhotos;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingActivity extends BaseActivity {
    SwipeRefreshLayout mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRefresh = findViewById(R.id.refresh);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // stop all ongoing requests for new pages
                okHttpClient.dispatcher().cancelAll();

                // request for page 1 again
                requestPopular(1);
            }
        });

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

        // send request to api for popular photos
        api.getPopular(page).enqueue(new Callback<PagedPhotos>() {
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
            }

            @Override
            public void onFailure(Call<PagedPhotos> call, Throwable t) {
                // remove outstanding sticky events for page requests, if any
                EventBus.getDefault().removeStickyEvent(ScollEndEvent.class);

                // dismiss pull down refresh circle
                mRefresh.setRefreshing(false);

                // TODO
                Log.e("NET", "Popular page request failed");
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
