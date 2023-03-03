package com.jkcq.homebike.ride.pk.bean;

import com.example.websocket.bean.PkUsers;
import com.jkcq.homebike.db.SceneBean;

import java.io.Serializable;
import java.util.List;

public class PKRoomBean implements Serializable {
    CyclingPk cyclingPk;
    List<PkUsers> pkUsers;
    SceneBean scenario;

    public SceneBean getScenario() {
        return scenario;
    }

    public void setScenario(SceneBean scenario) {
        this.scenario = scenario;
    }

    public CyclingPk getCyclingPk() {
        return cyclingPk;
    }

    public void setCyclingPk(CyclingPk cyclingPk) {
        this.cyclingPk = cyclingPk;
    }

    public List<PkUsers> getPkUsers() {
        return pkUsers;
    }

    public void setPkUsers(List<PkUsers> pkUsers) {
        this.pkUsers = pkUsers;
    }

    @Override
    public String toString() {
        return "PKRoomBean{" +
                "cyclingPk=" + cyclingPk +
                ", pkUsers=" + pkUsers +
                ", scenario=" + scenario +
                '}';
    }



}
