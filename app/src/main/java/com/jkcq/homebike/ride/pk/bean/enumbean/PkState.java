package com.jkcq.homebike.ride.pk.bean.enumbean;

public enum PkState {

    /**
     * PK状态，0：未开始，1：进行中，2：已结束, 3:已解散, 4:收集数据
     */
    UNSTART("未开始", 0),
    START("进行中", 1),
    END("结束", 2),
    DESTROY("已解散", 3),
    COLLECT_DATA("收集数据", 4);

    private String name;
    private int value;

    private PkState(String name, Integer value) {
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
