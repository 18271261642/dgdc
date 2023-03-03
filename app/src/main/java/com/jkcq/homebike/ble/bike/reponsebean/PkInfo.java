package com.jkcq.homebike.ble.bike.reponsebean;

public class PkInfo {
    /**
     * "id": null,
     * "pkName": null,
     * "participantNum": null,
     * "rank": null
     */
    String id;
    String pkName;
    String participantNum;
    String rank;

    @Override
    public String toString() {
        return "pkInfo{" +
                "id='" + id + '\'' +
                ", pkName='" + pkName + '\'' +
                ", participantNum='" + participantNum + '\'' +
                ", rank='" + rank + '\'' +
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

    public String getParticipantNum() {
        return participantNum;
    }

    public void setParticipantNum(String participantNum) {
        this.participantNum = participantNum;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
