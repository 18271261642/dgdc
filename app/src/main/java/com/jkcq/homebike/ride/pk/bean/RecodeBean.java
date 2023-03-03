package com.jkcq.homebike.ride.pk.bean;

import android.text.TextUtils;

public class RecodeBean {
    /**
     * "userId": 86,
     * 			"nickName": "186****8615",
     * 			"avatar": "https://manager.fitalent.com.cn/static/2021/0/9/10-49-14-4486444.jpg",
     * 			"bestScore": 28.571428571428573
     */
    String userId;
    String nickName;
    String avatar;
    String bestScore;

    public String getUserId() {

        if(TextUtils.isEmpty(userId)){
            userId="";
        }

        return userId;
    }

    public String getNickName() {
        if(TextUtils.isEmpty(nickName)){
            nickName="";
        }
        return nickName;
    }

    public String getAvatar() {
        if(TextUtils.isEmpty(avatar)){
            avatar="";
        }
        return avatar;
    }

    public String getBestScore() {
        if(TextUtils.isEmpty(bestScore)){
            bestScore="0";
        }
        return bestScore;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setBestScore(String bestScore) {
        this.bestScore = bestScore;
    }

    @Override
    public String toString() {
        return "RecodeBean{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", bestScore='" + bestScore + '\'' +
                '}';
    }
}
