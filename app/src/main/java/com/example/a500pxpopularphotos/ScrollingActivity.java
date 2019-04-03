package com.example.a500pxpopularphotos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.a500pxpopularphotos.pojo.PagedPhotos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingActivity extends BaseActivity {
    TextView mDebugText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDebugText = (TextView) findViewById(R.id.secret_load);
        mDebugText.setText(BuildConfig.consumer_key);

        api.getPopular().enqueue(new Callback<PagedPhotos>() {
            @Override
            public void onResponse(Call<PagedPhotos> call, Response<PagedPhotos> response) {
                mDebugText.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<PagedPhotos> call, Throwable t) {
                mDebugText.setText("popular failed");
            }
        });
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
}
