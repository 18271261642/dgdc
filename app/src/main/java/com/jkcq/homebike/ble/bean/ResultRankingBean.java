package com.jkcq.homebike.ble.bean;

import java.util.List;

public class ResultRankingBean {

    List<RankingBean> list;
    int totalNum;
    RankingBean myRanking;

    public List<RankingBean> getList() {
        return list;
    }

    public void setList(List<RankingBean> list) {
        this.list = list;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public RankingBean getMyRanking() {
        return myRanking;
    }

    public void setMyRanking(RankingBean myRanking) {
        this.myRanking = myRanking;
    }
}
