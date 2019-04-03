package com.example.a500pxpopularphotos;

import android.support.v7.app.AppCompatActivity;

import com.example.a500pxpopularphotos.api.AddSecret;
import com.example.a500pxpopularphotos.api.FiveHundredPixel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BaseActivity extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(FiveHundredPixel.ep)
            .addConverterFactory(
                    JacksonConverterFactory.create(new ObjectMapper()
                            // if we ignore unknown fields in the received json
                            // we do not need to manually list every field in the POJO
                            // corresponding to the JSON
                            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)))
            .client(new OkHttpClient.Builder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .addInterceptor(new AddSecret())
                    .build())
            .build();
    FiveHundredPixel api = retrofit.create(FiveHundredPixel.class);

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    // DO NOT REMOVE
    @Subscribe
    public void bootstrapEventBus(BaseActivity a) {
    }
}
