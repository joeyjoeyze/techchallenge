package com.example.a500pxpopularphotos.pojo;

import java.util.Arrays;

public class Photo {
    int id;
    String url;
    String[] image_url;
    String name;
    int positive_votes_count;
    User user;

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", image_url=" + Arrays.toString(image_url) +
                ", name='" + name + '\'' +
                ", positive_votes_count=" + positive_votes_count +
                ", user=" + user +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPositive_votes_count() {
        return positive_votes_count;
    }

    public void setPositive_votes_count(int positive_votes_count) {
        this.positive_votes_count = positive_votes_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
