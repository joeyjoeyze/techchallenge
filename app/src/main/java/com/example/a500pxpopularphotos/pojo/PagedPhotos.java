package com.example.a500pxpopularphotos.pojo;

import java.util.Arrays;

public class PagedPhotos {
    int current_page;
    int total_pages;
    Photo[] photos;

    @Override
    public String toString() {
        return "PagedPhotos{" +
                "current_page=" + current_page +
                ", total_pages=" + total_pages +
                ", photos=" + Arrays.toString(photos) +
                '}';
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photo[] photos) {
        this.photos = photos;
    }

}
