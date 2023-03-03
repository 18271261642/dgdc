package com.jkcq.homebike.ride.pk.bean;

/**
 * "id": "1346355021190275074",
 * "pkName": "这么",
 * "participantNum": 6,
 * "joinNum": 0,
 * "hasPassword": false,
 * "playType": 0,
 * "pkStatus": 0
 */
public class CyclingPkDetail {
    String id;
    String pkName;
    int participantNum;
    int joinNum;
    boolean hasPassword;
    int playType;
    int pkStatus;

    @Override
    public String toString() {
        return "CyclingPkDetail{" +
                "id='" + id + '\'' +
                ", pkName='" + pkName + '\'' +
                ", participantNum=" + participantNum +
                ", joinNum=" + joinNum +
                ", hasPassword=" + hasPassword +
                ", playType=" + playType +
                ", pkStatus=" + pkStatus +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public int getParticipantNum() {
        return participantNum;
    }

    public void setParticipantNum(int participantNum) {
        this.participantNum = participantNum;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public int getPkStatus() {
        return pkStatus;
    }

    public void setPkStatus(int pkStatus) {
        this.pkStatus = pkStatus;
    }
}
