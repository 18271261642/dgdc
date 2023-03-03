package com.jkcq.homebike.ride.sceneriding.bean;

import java.io.Serializable;

public class CourseLineBean implements Serializable {

    public int number;
    public int resistance;
    public int startime;
    public int interver;
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

    public int getInterver() {
        return interver;
    }

    public void setInterver(int interver) {
        this.interver = interver;
    }

    public int getStartime() {
        return startime;
    }

    public void setStartime(int startime) {
        this.startime = startime;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }
}
