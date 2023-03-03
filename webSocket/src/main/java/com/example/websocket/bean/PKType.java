package com.example.websocket.bean;

public enum PKType {
    JOIN("加入Pk房间", 1001),
    SOCKET_CON("是否正常连接中", 103),
    LEAVE("退出Pk房间", 1002),
    DESTROY("解散Pk房间", 1003),
    START("开始Pk", 1004),
    END("结束Pk", 1005),
    LOGOUT("结束Pk", 102),
    REJOIN("重新加入Pk房间", 1006),
    RANK("Pk排名", 1007),
    RECEIVE_GIFT("接收礼物", 1008),
    REPORT_DATA_RESPONSE("实时上报数据回复", 1009),
    PK_END_CONTDOWN("实时上报数据回复", 1010),
    PK_END_ONE("PK结束只有一个人上传数据", 1011),
    REPORT_DATA("实时上报Pk数据", 2001),
    GIVE_GIFT("赠送礼物", 2002);

    private String name;
    private int value;

    private PKType(String name, Integer value) {
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
