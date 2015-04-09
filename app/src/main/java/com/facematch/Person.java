package com.facematch;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by niesh on 15-4-6.
 */
public class Person {
    Bitmap photo;
    String info;
    ArrayList<Double> similarities;
    ArrayList<Float> landmarks;

    public float[] getLandmarks() {
        float[] floatArray = new float[landmarks.size()];
        int i = 0;
        for (Float f : landmarks) {
            floatArray[i++] = f;
        }
        return floatArray;
    }

    public ArrayList<Double> getSimilarities() {
        return similarities;
    }

    public void setSimilarities(ArrayList<Double> similarities) {
        this.similarities = similarities;
    }

    public Person() {
        this.photo = null;
        this.info = "";
        this.similarities = new ArrayList<>();
        this.landmarks = new ArrayList<>();
    }

//    public Person(Bitmap photo, String info) {
//        this.photo = photo;
//        this.info = info;
//    }

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
