package com.jkcq.homebike.ride.sceneriding.bean;

public class PraiseBean {
    /**
     * "praiseNums": 1,
     * "whetherPraise": true,
     * "dataId": "1337218396153712641"
     */
    String praiseNums;
    boolean whetherPraise;
    String dataId;
    String userId;

    @Override
    public String toString() {
        return "PraiseBean{" +
                "praiseNums='" + praiseNums + '\'' +
                ", whetherPraise=" + whetherPraise +
                ", dataId='" + dataId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getPraiseNums() {
        return praiseNums;
    }

    public void setPraiseNums(String praiseNums) {
        this.praiseNums = praiseNums;
    }

    public boolean isWhetherPraise() {
        return whetherPraise;
    }

    public void setWhetherPraise(boolean whetherPraise) {
        this.whetherPraise = whetherPraise;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
