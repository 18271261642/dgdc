package com.jkcq.homebike.ride.pk.bean;

import java.io.Serializable;

public class CyclingPk implements Serializable {
    /**
     * "id": "1346385679048515585",
     * "pkName": "test",
     * "scenarioId": "1344177586919243778",
     * "participantNum": 4,
     * "playType": 0
     */
    String id;
    String pkName;
    String scenarioId;
    String participantNum;
    String playType;

    @Override
    public String toString() {
        return "CyclingPk{" +
                "id='" + id + '\'' +
                ", pkName='" + pkName + '\'' +
                ", scenarioId='" + scenarioId + '\'' +
                ", participantNum='" + participantNum + '\'' +
                ", playType='" + playType + '\'' +
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

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getParticipantNum() {
        return participantNum;
    }

    public void setParticipantNum(String participantNum) {
        this.participantNum = participantNum;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }
}
