package com.example.a500pxpopularphotos.api;

import com.example.a500pxpopularphotos.BuildConfig;
import com.example.a500pxpopularphotos.pojo.PagedPhotos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FiveHundredPixel {
    String ep = "https://api.500px.com/v1/";
    String key = BuildConfig.consumer_key;

    @GET("photos?feature=popular")
    Call<PagedPhotos> getPopular(@Query("page") Integer page);
}
