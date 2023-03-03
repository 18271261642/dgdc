package com.jkcq.homebike.ride.pk.bean;

public class PKStateBean {
    /**
     * "id": "1348577734621433857",
     * "pkName": "tggg",
     * "participantNum": 2,
     * "joinNum": 0,
     * "hasPassword": true,
     * "playType": 0,
     * "pkStatus": 1
     */
    String id;
    String pkName;
    String participantNum;
    String joinNum;
    boolean hasPassword;
    int playType;
    int pkStatus;

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

    public String getParticipantNum() {
        return participantNum;
    }

    public void setParticipantNum(String participantNum) {
        this.participantNum = participantNum;
    }

    public String getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(String joinNum) {
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

    @Override
    public String toString() {
        return "PKStateBean{" +
                "id='" + id + '\'' +
                ", pkName='" + pkName + '\'' +
                ", participantNum='" + participantNum + '\'' +
                ", joinNum='" + joinNum + '\'' +
                ", hasPassword=" + hasPassword +
                ", playType=" + playType +
                ", pkStatus=" + pkStatus +
                '}';
    }
}
