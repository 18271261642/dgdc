package com.example.websocket.bean;

public class LeaveBean {
    /**
     * {"pkId":"1","type":1002,"userId":1}
     */

    String pkId;
    int type;
    String userId;

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LeaveBean{" +
                "pkId='" + pkId + '\'' +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                '}';
    }
}
