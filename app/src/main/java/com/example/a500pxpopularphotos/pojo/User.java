package com.example.a500pxpopularphotos.pojo;

public class User {
    String fullname;
    String userpic_url;

    @Override
    public String toString() {
        return "User{" +
                "fullname='" + fullname + '\'' +
                ", userpic_url='" + userpic_url + '\'' +
                '}';
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserpic_url() {
        return userpic_url;
    }

    public void setUserpic_url(String userpic_url) {
        this.userpic_url = userpic_url;
    }
}
