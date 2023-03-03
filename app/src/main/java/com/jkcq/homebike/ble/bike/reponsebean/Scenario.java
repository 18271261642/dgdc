package com.jkcq.homebike.ble.bike.reponsebean;

import android.text.TextUtils;

public class Scenario {
    /**
     * "imageUrl": null,
     * "name": null,
     * "nameEn": null,
     * "length": null
     */
    String imageUrl;
    String name;
    String nameEn;
    String length;

    @Override
    public String toString() {
        return "scenario{" +
                "imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", length='" + length + '\'' +
                '}';
    }

    public String getImageUrl() {
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "";
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }

    public String getName() {
        if (TextUtils.isEmpty(name)) {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        if (TextUtils.isEmpty(nameEn)) {
            nameEn = "";
        }
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getLength() {
        if (TextUtils.isEmpty(length)) {
            length = "";
        }
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
