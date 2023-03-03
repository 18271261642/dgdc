package com.jkcq.homebike.ble.bean;

import androidx.annotation.NonNull;

import java.util.Objects;

public class RankingBean implements Comparable {
    /**
     * "rankingNo": "1",
     * "userId": 6,
     * "headUrl": "https://manager.fitalent.com.cn/static/2018/9/19/9-54-56-835426.png",
     * "nickName": "177****4396",
     * "totalCalorie": 186784,
     * "praiseNums": 1,
     * "whetherPraise": false
     */
    int rankingNo;
    String userId;
    String headUrl;
    String nickName;
    int totalCalorie;
    String praiseNums;
    boolean whetherPraise;
    String dataId;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public int getRankingNo() {
        return rankingNo;
    }

    public void setRankingNo(int rankingNo) {
        this.rankingNo = rankingNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getTotalCalorie() {
        return totalCalorie;
    }

    public void setTotalCalorie(int totalCalorie) {
        this.totalCalorie = totalCalorie;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankingBean that = (RankingBean) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "RankingBean{" +
                "rankingNo=" + rankingNo +
                ", userId='" + userId + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", totalCalorie=" + totalCalorie +
                ", praiseNums='" + praiseNums + '\'' +
                ", whetherPraise=" + whetherPraise +
                ", dataId='" + dataId + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object bean) {


        RankingBean bean1 = (RankingBean) bean;
        if (this.totalCalorie < bean1.totalCalorie) {
            return 1;
        } else {
            return -1;
        }

    }


}
