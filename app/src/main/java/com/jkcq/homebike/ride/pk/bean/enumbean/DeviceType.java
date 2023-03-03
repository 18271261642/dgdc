package com.jkcq.homebike.ride.pk.bean.enumbean;

public enum DeviceType {

    /**
     * PK状态，0：未创记录，1：个人记录，2：世界记录
     */
    DEVICE_BIKE("BIKE", 120),
    DEVICE_S003("S003", 83003),
    DEVICE_S005("S005", 83005);

    private String name;
    private int value;

    private DeviceType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
