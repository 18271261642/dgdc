package com.jkcq.homebike.ride.pk.bean.enumbean;

public enum PkBestState {

    /**
     * PK状态，0：未创记录，1：个人记录，2：世界记录
     */
    NO_RECORD("未创记录", 0),
    PERSONAL_RECORDS("个人记录", 1),
    WORLD_RECORD("世界记录", 2);

    private String name;
    private int value;

    private PkBestState(String name, Integer value) {
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
