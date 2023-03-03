package com.example.websocket.bean;

import java.io.Serializable;

public class JoinUser implements Serializable {
    /**
     * {"pkId":"1","nickName":"demoData","avatar":"demoData","type":1001,"userId":1}
     */
    String pkId;
    String nickName;
    String avatar;
    String userId;
    Long durationMillis;
    Long createTimestamp;
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(Long durationMillis) {
        this.durationMillis = durationMillis;
    }


    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    @Override
    public String toString() {
        return "JoinUser{" +
                "pkId='" + pkId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userId='" + userId + '\'' +
                ", durationMillis=" + durationMillis +
                ", createTimestamp=" + createTimestamp +
                ", type=" + type +
                '}';
    }
}
