package com.jkcq.homebike.bike.bean;

public class RealDataBean {
    int speed;
    int power;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "RealDataBean{" +
                "speed=" + speed +
                ", power=" + power +
                '}';
    }
}
