package com.example.a500pxpopularphotos;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
