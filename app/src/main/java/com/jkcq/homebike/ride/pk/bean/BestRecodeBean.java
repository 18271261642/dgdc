package com.jkcq.homebike.ride.pk.bean;

public class BestRecodeBean {
    RecodeBean worldRecord;
    RecodeBean myRecord;

    @Override
    public String toString() {
        return "BestRecodeBean{" +
                "worldRecord=" + worldRecord +
                ", myRecord=" + myRecord +
                '}';
    }

    public RecodeBean getWorldRecord() {
        return worldRecord;
    }

    public void setWorldRecord(RecodeBean worldRecord) {
        this.worldRecord = worldRecord;
    }

    public RecodeBean getMyRecord() {
        return myRecord;
    }

    public void setMyRecord(RecodeBean myRecord) {
        this.myRecord = myRecord;
    }
}
