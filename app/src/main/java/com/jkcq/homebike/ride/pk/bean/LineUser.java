package com.jkcq.homebike.ride.pk.bean;

public class LineUser {

    String userId;
    int dis;


    public LineUser() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LineUser(String userId, int dis) {
        this.userId = userId;
        this.dis = dis;
    }

    public int getDis() {
        return dis;
    }

    public void setDis(int dis) {
        this.dis = dis;
    }

    @Override
    public String toString() {
        return "LineUser{" +
                "avatar='" + userId + '\'' +
                ", dis=" + dis +
                '}';
    }
}
