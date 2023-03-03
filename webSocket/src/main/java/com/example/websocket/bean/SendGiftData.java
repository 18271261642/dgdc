package com.example.websocket.bean;

import java.io.Serializable;

public class SendGiftData  implements Serializable {
    /**
     * {"type":2002,"pkId":"1","giftCode":"demoData","fromUserId":1,"toUserId":2}
     *
     * @return
     */
    int type;
    String pkId;
    String giftCode;
    String fromUserId;
    String toUserId;

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

    public String getGiftCode() {
        return giftCode;
    }

    public void setGiftCode(String giftCode) {
        this.giftCode = giftCode;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public String toString() {
        return "SendGiftData{" +
                "type=" + type +
                ", pkId='" + pkId + '\'' +
                ", giftCode='" + giftCode + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                '}';
    }
}
