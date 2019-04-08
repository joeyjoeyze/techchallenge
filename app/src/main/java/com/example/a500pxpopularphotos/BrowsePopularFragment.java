package com.example.a500pxpopularphotos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.a500pxpopularphotos.api.FiveHundredPixel;
import com.example.a500pxpopularphotos.event.ScollEndEvent;
import com.example.a500pxpopularphotos.pojo.PagedPhotos;
import com.example.a500pxpopularphotos.pojo.Photo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BrowsePopularFragment extends BaseFragment {

    protected RecyclerView mRecyclerView;
    protected FlexboxLayoutManager mLayoutManager;
    protected RecyclerView.Adapter mAdapter;

    public static int NUM_COLUMNS = 2;

    // Pagination State
    int mCurrentPage = 0;
    List<Photo> mVerticalGallery = new ArrayList<>();
    HashSet<Integer> mExistingPhotoIds = new HashSet<>();

    int mDisplayWidth;

    public static BrowsePopularFragment newInstance() {

        Bundle args = new Bundle();

        BrowsePopularFragment fragment = new BrowsePopularFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gallery_recycler);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new GridAdapter(R.layout.vertical_gallery_item, Glide.with(this));

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new FlexboxLayoutManager(getContext());
        mLayoutManager.setFlexDirection(FlexDirection.ROW);
        mLayoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);
        mLayoutManager.setFlexWrap(FlexWrap.WRAP);
        mLayoutManager.setAlignItems(AlignItems.STRETCH);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        mDisplayWidth = width;
    }

    public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageViewHolder> {
        int mLayout;
        RequestManager mGlide;
        public GridAdapter(int layout, RequestManager glide) {
            mLayout = layout;
            mGlide = glide;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(mLayout, viewGroup, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
            // displaying final row or final two rows
            if (i >= (getItemCount() - NUM_COLUMNS - 1) && EventBus.getDefault().getStickyEvent(ScollEndEvent.class) == null){
                // request the next page if no requests exist
                EventBus.getDefault().postSticky(new ScollEndEvent(mCurrentPage + 1));
            }

            imageViewHolder.mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent openFullscreen = new Intent(getContext(), FullscreenImageActivity.class);
                        String ser = new ObjectMapper().writeValueAsString(mVerticalGallery.get(i));
                        openFullscreen.putExtra(FullscreenImageActivity.PHOTO_INFO, ser);
                        startActivity(openFullscreen);
                    } catch (IOException e) {
                        Log.e("EX", e.getLocalizedMessage());
                    }
                }
            });

            // calculate height from image aspect ratio
            Photo thisPhoto = mVerticalGallery.get(i);
            int rowStartIndex = (i / NUM_COLUMNS) * NUM_COLUMNS;
            List<Point> rowImages = new ArrayList<>();
            for (int imgIndex = rowStartIndex; imgIndex < rowStartIndex + NUM_COLUMNS; imgIndex++) {
                Photo image = mVerticalGallery.get(imgIndex);
                rowImages.add(new Point(image.getWidth(), image.getHeight()));
            }

            // height and width in pixels
            double height = calculateHeight(rowImages);
            int heightPixel = (int)Math.floor(height);
            int widthPixel = (int)Math.floor(height * ((double)thisPhoto.getWidth()/thisPhoto.getHeight()));

            Log.v("UI", "Display dims W:" + mDisplayWidth);
            Log.v("UI", "Name:" + thisPhoto.getName());
            Log.v("UI", "Original dims and aspect ratio W:" + thisPhoto.getWidth() + " H:" + thisPhoto.getHeight() + " AR:" + (double)thisPhoto.getWidth() / thisPhoto.getHeight());
            Log.v("UI", "Display dims and aspect ratio W:" + widthPixel + " H:" + heightPixel + " AR:" + (double)widthPixel / heightPixel);

            imageViewHolder.onBind(mGlide, mVerticalGallery.get(i), widthPixel, heightPixel);
        }

        @Override
        public int getItemCount() {
            // always return count as a multiple of NUM_COLUMNS
            // the remaining possible undisplayed items will be shown after the network request for the next page finishes
            // This removes cases where we need to calculate the height and width of x elements in a row where x is less than NUM_COLUMNS
            return mVerticalGallery.size() - (mVerticalGallery.size() % NUM_COLUMNS);
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            View mRoot;
            ImageView mImage;

            public ImageViewHolder(View view) {
                super(view);
                mRoot = view;
                mImage = view.findViewById(R.id.gallery_item);
            }

            public void onBind(RequestManager glide, Photo photo, int width, int height) {
                // predetermine imageview size before loading image
                // resizing imageview after glide loads the image from networks results in incorrect width
                mImage.setLayoutParams(new FlexboxLayoutManager.LayoutParams(width, height));
                glide.load(photo.getImage_url()[FiveHundredPixel.SMALL_IMG_INDEX])
                        .override(width,height)
                        .into(mImage);
            }
        }
    }


    @Subscribe
    public void onPagedPhotos(PagedPhotos pagedPhotos) {
        int page = pagedPhotos.getCurrent_page();
        List<Photo> photos = Arrays.asList(pagedPhotos.getPhotos());
        if (page == mCurrentPage + 1) {
            // this is a response for more of the current paginated photos
            // extend list of gallery images
            for (Photo p : photos) {
                if (!mExistingPhotoIds.contains(p.getId())) {
                    mVerticalGallery.add(p);
                    mExistingPhotoIds.add(p.getId());
                }
            }
        } else if (page <= mCurrentPage) {
            // this is a refresh of new popular photos
            // delete current list of images and replace with response
            Log.d("UI", "Cleared existing gallery images");
            mVerticalGallery.clear();
            mVerticalGallery.addAll(photos);

            mExistingPhotoIds.clear();
            for (Photo p : photos) {
                mExistingPhotoIds.add(p.getId());
            }
        } else {
            // we have received a page greater than current page + 1
            // this indicates a response to a request made in the past that is no longer valid
            // ignore this request
            return;
        }
        mCurrentPage = page;

        // ask adapter to display new items
        Log.d("UI", "vertical gallery updated. Page " + mCurrentPage);
        mAdapter.notifyDataSetChanged();
    }

    private double calculateHeight(List<Point> images) {
        double ratioSum = 0;
        for (Point image : images) {
            ratioSum += (double)image.x / image.y;
        }

        return (double)mDisplayWidth / ratioSum;
    }

}
