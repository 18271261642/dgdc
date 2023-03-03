package com.jkcq.homebike.ride.sceneriding.bean;

import java.io.Serializable;

public class LineBean implements Serializable {

    public int number;
    public int resistance;
    public int zoneLen;
    public int rpm;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public int getZoneLen() {
        return zoneLen;
    }

    public void setZoneLen(int zoneLen) {
        this.zoneLen = zoneLen;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }
}
