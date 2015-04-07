package com.facematch;

import android.graphics.Bitmap;

/**
 * Created by niesh on 15-4-6.
 */
public class Person {
    Bitmap photo;
    String info;

    public Person(Bitmap photo) {
        this.photo = photo;
        this.info = "";
    }

    public Person(Bitmap photo, String info) {
        this.photo = photo;
        this.info = info;

    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
