package com.example.websocket.bean;

import java.io.Serializable;
import java.util.Objects;

public class PkUsers implements Serializable, Comparable {
    /**
     * "userId": 4,
     * "nickName": "186****8616",
     * "headUrl": "https://manager.fitalent.com.cn/static/2020/11/28/11-19-42-1474193.jpg",
     * "creatorFlag": true,
     * "pkRecordId": "1346385679082070017"
     */

    String userId;
    String nickName;
    String avatar;
    boolean creatorFlag;
    String pkRecordId;
    long createTimestamp;

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public PkUsers() {

    }


    public PkUsers(String userId, String nickName, String avatar, String pkRecordId, long createTimestamp, boolean creatorFlag) {
        this.userId = userId;
        this.nickName = nickName;
        this.creatorFlag = creatorFlag;
        this.avatar = avatar;
        this.pkRecordId = pkRecordId;
        this.createTimestamp = createTimestamp;

    }

    public PkUsers(String userId, String nickName, boolean creatorFlag) {
        this.userId = userId;
        this.nickName = nickName;
        this.creatorFlag = creatorFlag;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean isCreatorFlag() {
        return creatorFlag;
    }

    public void setCreatorFlag(boolean creatorFlag) {
        this.creatorFlag = creatorFlag;
    }

    public String getPkRecordId() {
        return pkRecordId;
    }

    public void setPkRecordId(String pkRecordId) {
        this.pkRecordId = pkRecordId;
    }

    @Override
    public String toString() {
        return "PkUsers{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", creatorFlag=" + creatorFlag +
                ", pkRecordId='" + pkRecordId + '\'' +
                ", createTimestamp=" + createTimestamp +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkUsers)) return false;
        PkUsers pkUsers = (PkUsers) o;
        return getUserId().equals(pkUsers.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    @Override
    public int compareTo(Object o) {
        PkUsers bean1 = (PkUsers) o;
        // return -1;
        if (this.createTimestamp == 0) {
            return 1;
        } else if (bean1.createTimestamp > this.createTimestamp) {
            return -1;
        } else {
            return 1;
        }
    }
}
