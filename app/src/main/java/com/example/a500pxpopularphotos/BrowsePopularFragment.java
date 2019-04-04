package com.example.a500pxpopularphotos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.a500pxpopularphotos.event.ScollEndEvent;
import com.example.a500pxpopularphotos.pojo.PagedPhotos;
import com.example.a500pxpopularphotos.pojo.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowsePopularFragment extends BaseFragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.Adapter mAdapter;

    int GRID_SPAN = 3;

    // Pagination State
    int mCurrentPage = 0;
    List<Photo> mVerticalGallery = new ArrayList<>();

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

        mLayoutManager = new GridLayoutManager(getContext(), GRID_SPAN);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
            if (i >= (getItemCount() - GRID_SPAN - 1) && EventBus.getDefault().getStickyEvent(ScollEndEvent.class) == null){
                // request the next page if no requests exist
                EventBus.getDefault().postSticky(new ScollEndEvent(mCurrentPage + 1));
            }
            imageViewHolder.onBind(mGlide, mVerticalGallery.get(i));
        }

        @Override
        public int getItemCount() {
            return mVerticalGallery.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            View mRoot;
            ImageView mImage;

            public ImageViewHolder(View view) {
                super(view);
                mRoot = view;
                mImage = view.findViewById(R.id.gallery_item);
            }

            public void onBind(RequestManager glide, Photo photo) {
                glide.load(photo.getImage_url()[0])
                        .into(mImage);
            }
        }
    }


    @Subscribe
    public void onPagedPhotos(PagedPhotos pagedPhotos) {
        int page = pagedPhotos.getCurrent_page();
        if (page == mCurrentPage + 1) {
            // this is a response for more of the current paginated photos
            // extend list of gallery images
            mVerticalGallery.addAll(Arrays.asList(pagedPhotos.getPhotos()));
        } else if (page <= mCurrentPage) {
            // this is a refresh of new popular photos
            // delete current list of images and replace with response
            Log.d("UI", "Cleared existing gallery images");
            mVerticalGallery.clear();
            mVerticalGallery.addAll(Arrays.asList(pagedPhotos.getPhotos()));
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

}
