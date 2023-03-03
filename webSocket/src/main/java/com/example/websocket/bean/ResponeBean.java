package com.example.websocket.bean;

public class ResponeBean {
    /**
     * {
     * "type": 1,
     * "pkId": "demoData",
     * "messageId": 1
     * }
     */
    int type;
    String pkId;
    String messageId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "ResponeBean{" +
                "type='" + type + '\'' +
                ", pkId='" + pkId + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
