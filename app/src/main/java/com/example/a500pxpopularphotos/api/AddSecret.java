package com.example.a500pxpopularphotos.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddSecret implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("consumer_key", FiveHundredPixel.key)
                .build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
