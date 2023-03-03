package com.jkcq.base.net.bean;

import android.text.TextUtils;

/**
 * Created by BeyondWorlds
 * on 2020/7/30
 */
public class UserInfo {

    /**
     * userId : 170
     * mobile : 18617111608
     * nickName : 186****1608
     * gender : Male
     * height : 170
     * isRegidit : true
     * weight : 60
     * birthday : 1990-01-01
     * headUrl : https://manager.fitalent.com.cn/static/2020/3/23/14-24-46-2863100.PNG
     * headUrlTiny : https://manager.fitalent.com.cn/static/2020/3/23/14-24-46-519578.PNG
     * backgroundUrl : null
     * myProfile :
     * qrString : e5413b8d249b45a5a45d098842b71121
     * enableRanking : true
     */

    private String userId;
    private String measurement;
    private String mobile;
    private String nickName;
    private String gender;
    private float height;
    private boolean isRegidit;
    private float weight;
    private String birthday;
    private String headUrl;
    private String headUrlTiny;
    private String backgroundUrl;
    private String myProfile;
    private String qrString;
    private boolean enableRanking;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isIsRegidit() {
        return isRegidit;
    }

    public void setIsRegidit(boolean isRegidit) {
        this.isRegidit = isRegidit;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHeadUrlTiny() {
        return headUrlTiny;
    }

    public void setHeadUrlTiny(String headUrlTiny) {
        this.headUrlTiny = headUrlTiny;
    }

    public String getBackgroundUrl() {
        if (TextUtils.isEmpty(backgroundUrl)) {
            backgroundUrl = "";
        }
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getMyProfile() {
        if (TextUtils.isEmpty(myProfile)) {
            myProfile = "";
        }
        return myProfile;
    }

    public void setMyProfile(String myProfile) {
        this.myProfile = myProfile;
    }

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    public boolean isEnableRanking() {
        return enableRanking;
    }

    public void setEnableRanking(boolean enableRanking) {
        this.enableRanking = enableRanking;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public boolean isRegidit() {
        return isRegidit;
    }

    public void setRegidit(boolean regidit) {
        isRegidit = regidit;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", measurement='" + measurement + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", isRegidit=" + isRegidit +
                ", weight=" + weight +
                ", birthday='" + birthday + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", headUrlTiny='" + headUrlTiny + '\'' +
                ", backgroundUrl='" + backgroundUrl + '\'' +
                ", myProfile='" + myProfile + '\'' +
                ", qrString='" + qrString + '\'' +
                ", enableRanking=" + enableRanking +
                '}';
    }
}
