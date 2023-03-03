package com.example.websocket.bean;

import java.io.Serializable;

public class PkdataList implements Serializable {
    /**
     * {"distance":1,"durationMillis":1,"userId":1,"online":true,"pkStatus":1}
     */
    int distance;
    long durationMillis;
    String userId;
    boolean online;
    int pkStatus;
    int rankNo;
    int subvalue;

    public PkdataList() {

    }


    public int getSubvalue() {
        return subvalue;
    }

    public void setSubvalue(int subvalue) {
        this.subvalue = subvalue;
    }

    public int getRankNo() {
        return rankNo;
    }

    public void setRankNo(int rankNo) {
        this.rankNo = rankNo;
    }

    public PkdataList(String userId, int distance, int durationMillis, boolean online, int pkStatus, int ranking) {
        this.distance = distance;
        this.durationMillis = durationMillis;
        this.userId = userId;
        this.online = online;
        this.pkStatus = pkStatus;
        this.rankNo = ranking;

    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getPkStatus() {
        return pkStatus;
    }

    public void setPkStatus(int pkStatus) {
        this.pkStatus = pkStatus;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "PkdataList{" +
                "distance=" + distance +
                ", durationMillis=" + durationMillis +
                ", userId='" + userId +
                ", online=" + online +
                ", pkStatus=" + pkStatus +
                ", rankNo=" + rankNo +
                ", subvalue=" + subvalue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PkdataList that = (PkdataList) o;
        return userId.equals(that.userId);
    }


}
