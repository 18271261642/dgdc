package com.jkcq.homebike.ride.pk.bean;

import com.jkcq.homebike.db.SceneBean;

public class PKListBean {
    CyclingPkDetail cyclingPkDetail;
    SceneBean scenario;

    public CyclingPkDetail getCyclingPkDetail() {
        return cyclingPkDetail;
    }

    public void setCyclingPkDetail(CyclingPkDetail cyclingPkDetail) {
        this.cyclingPkDetail = cyclingPkDetail;
    }

    public SceneBean getScenario() {
        return scenario;
    }

    public void setScenario(SceneBean scenario) {
        this.scenario = scenario;
    }

    @Override
    public String toString() {
        return "PKListBean{" +
                "cyclingPkDetail=" + cyclingPkDetail +
                ", scenario=" + scenario +
                '}';
    }
}
