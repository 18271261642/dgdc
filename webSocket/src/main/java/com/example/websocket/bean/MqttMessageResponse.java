package com.example.websocket.bean;

import java.io.Serializable;
import java.util.List;

public class MqttMessageResponse implements Serializable {

    private int type;
    private int duration;

    private List<PkdataList> dataList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<PkdataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<PkdataList> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "MqttMessageResponse{" +
                "type=" + type +
                ", duration=" + duration +
                ", dataList=" + dataList +
                '}';
    }
}
