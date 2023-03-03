package com.jkcq.homebike.ble.bike.reponsebean;

import android.text.TextUtils;

public class CourseInfo {
    String name;
    String imageUrl;
    String difficulty;
    String length;

    public String getName() {
        if(TextUtils.isEmpty(name)){
           name="";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        if(TextUtils.isEmpty(imageUrl)){
            imageUrl="";
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", length='" + length + '\'' +
                '}';
    }
}
