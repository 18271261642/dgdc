package com.jkcq.homebike.bike.bean;


import com.jkcq.base.net.bean.UserInfo;

import java.util.ArrayList;

public class HeartBeanBike {
    public UserInfo user;
    public double sn;
    public int heartValue;
    public double cal;
    public int hrCount;
    public int hrStreng;
    public int maxHr;
    public Integer sumMinHrValue;
    public ArrayList<Integer> minList;
    public ArrayList<Integer> hrList2;
    public ArrayList<Integer> hrList3;
    public ArrayList<Integer> sumHrList;
    public boolean isCacheFull;
    public boolean isDrops;



    public int mTaskGrey;//课程内的该段时间数
    public int mTaskBlue;
    public int mTaskGreen;
    public int mTaskYellow;
    public int mTaskRed;



    public HeartBeanBike() {
        minList = new ArrayList<>();
        hrList2 = new ArrayList<>();
        hrList3 = new ArrayList<>();
        sumHrList = new ArrayList<>();
        isCacheFull=false;
        sumMinHrValue = 0;
        hrCount = 0;
        mTaskGreen=0;
        mTaskBlue=0;
        mTaskGrey=0;
        mTaskRed=0;
        mTaskYellow=0;
    }

    public void clearBikeValue(){
        minList .clear();
        hrList2 .clear();
        hrList3 .clear();
        sumHrList.clear();
        sumMinHrValue = 0;
        isCacheFull=false;
        hrCount = 0;
        mTaskGreen=0;
        mTaskBlue=0;
        mTaskGrey=0;
        mTaskRed=0;
        mTaskYellow=0;
    }

    public int getmTaskGrey() {
        return mTaskGrey;
    }

    public void setmTaskGrey() {
        this.mTaskGrey ++;
    }

    public int getmTaskBlue() {
        return mTaskBlue;
    }

    public void setmTaskBlue() {
        this.mTaskBlue ++;
    }

    public int getmTaskGreen() {
        return mTaskGreen;
    }

    public void setmTaskGreen() {
        this.mTaskGreen ++;
    }

    public int getmTaskYellow() {
        return mTaskYellow;
    }

    public void setmTaskYellow() {
        this.mTaskYellow ++;
    }

    public int getmTaskRed() {
        return mTaskRed;
    }

    public void setmTaskRed() {
        this.mTaskRed ++;
    }
}
