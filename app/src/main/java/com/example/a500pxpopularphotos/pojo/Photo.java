package com.example.a500pxpopularphotos.pojo;

import java.util.Arrays;

public class Photo {
    int id;
    String url;
    String[] image_url;

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", image_url=" + Arrays.toString(image_url) +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getImage_url() {
        return image_url;
    }

    public void setImage_url(String[] image_url) {
        this.image_url = image_url;
    }
}
