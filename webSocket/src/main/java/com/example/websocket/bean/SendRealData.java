package com.example.websocket.bean;

import java.io.Serializable;

public class SendRealData implements Serializable {
    /**
     * {"type":2001,"pkId":"1","userId":1,"distance":1,"durationMillis":1,"pkStatus":1}
     */
    int type;
    String pkId;
    String userId;
    String distance;
    String pkStatus;

    @Override
    public String toString() {
        return "SendRealData{" +
                "type=" + type +
                ", pkId='" + pkId + '\'' +
                ", userId='" + userId + '\'' +
                ", distance='" + distance + '\'' +
                ", pkStatus='" + pkStatus + '\'' +
                '}';
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getPkStatus() {
        return pkStatus;
    }

    public void setPkStatus(String pkStatus) {
        this.pkStatus = pkStatus;
    }
}
