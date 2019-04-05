package com.example.a500pxpopularphotos.api;

import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

// Class to calculate image_size ID for 500px API from image sizes
public class ImageSize {
    SortedMap<Integer, Integer> mSquareImageSize = new TreeMap<>();
    SortedMap<Integer, Integer> mUncroppedImageSize = new TreeMap<>();
    SortedMap<Integer, Integer> mHighImageSize = new TreeMap<>();

    public ImageSize() {
        mSquareImageSize.put(70, 1);
        mSquareImageSize.put(140, 2);
        mSquareImageSize.put(280, 3);
        mSquareImageSize.put(100, 100);
        mSquareImageSize.put(200, 200);
        mSquareImageSize.put(440, 440);
        mSquareImageSize.put(600, 600);

        mUncroppedImageSize.put(900, 4);
        mUncroppedImageSize.put(1170, 5);
        mUncroppedImageSize.put(256, 30);
        mUncroppedImageSize.put(1080, 1080);
        mUncroppedImageSize.put(1600, 1600);
        mUncroppedImageSize.put(2048, 2048);

        mHighImageSize.put(1080, 6);
        mHighImageSize.put(300, 20);
        mHighImageSize.put(600, 21);
        mHighImageSize.put(450, 31);
    }

    public int CalculateSquareSize(int imageLength) {
        for(Integer squareSize : mSquareImageSize.keySet()) {
            if (squareSize >= imageLength) {
                Log.d("NET", "Square image size " + squareSize);
                return mSquareImageSize.get(squareSize);
            }
        }
        Log.d("NET", "Square image size " + mSquareImageSize.lastKey());
        return mSquareImageSize.get(mSquareImageSize.lastKey());
    }

    public int CalculateLongestEdge(int longestEdge) {
        for(Integer uncroppedSize : mUncroppedImageSize.keySet()) {
            if (uncroppedSize >= longestEdge) {
                Log.d("NET", "Uncropped image size " + uncroppedSize);
                return mUncroppedImageSize.get(uncroppedSize);
            }
        }
        Log.d("NET", "Uncropped image size " + mUncroppedImageSize.lastKey());
        return mUncroppedImageSize.get(mUncroppedImageSize.lastKey());
    }
}
