package com.jkcq.homebike.ride.pk.bean;

import android.text.TextUtils;

public class PropesBean {
    /**
     * "id": "1347476505233293314",
     * "code": null,
     * "name": "xd",
     * "nameEn": "dfg",
     * "thumbnailUrl": "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/161009941467454fa4efb69c42c8046f0808bdeb5afe7.jpg",
     * "preEffectUrl": "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/161009839783154fa4efb69c42c8046f0808bdeb5afe7.jpg",
     * "effectUrl": null
     */

    String id;
    String code;
    String name;
    String nameEn;
    String thumbnailUrl;
    String preEffectUrl;
    String effectUrl;
    boolean isLengque;

    public boolean isLengque() {
        return isLengque;
    }

    public void setLengque(boolean lengque) {
        isLengque = lengque;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        if (TextUtils.isEmpty(code)) {
            code = "";
        }
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPreEffectUrl() {
        return preEffectUrl;
    }

    public void setPreEffectUrl(String preEffectUrl) {
        this.preEffectUrl = preEffectUrl;
    }

    public String getEffectUrl() {
        return effectUrl;
    }

    public void setEffectUrl(String effectUrl) {
        this.effectUrl = effectUrl;
    }

    @Override
    public String toString() {
        return "PropesBean{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", preEffectUrl='" + preEffectUrl + '\'' +
                ", effectUrl='" + effectUrl + '\'' +
                '}';
    }
}
