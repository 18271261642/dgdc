package com.jkcq.homebike.bike.bean;


import androidx.annotation.NonNull;

import com.jkcq.homebike.bike.arithmetic.BikeUtil;

import java.util.LinkedList;

public class BikeBean implements Comparable {


    public LinkedList<Integer> hrlist;
    public LinkedList<Integer> powList;
    public LinkedList<Integer> rpmList;
    public float fzSpeed;//发电机的转速
    public float tzSpeed;//踏频
    public float wheelSpeed;
    public float wheelNumber;
    public float wheelC;
    public float dis;
    public float light;
    public float power;
    public String sceneId;

    public float sumSpeed;
    public float sumPower;
    public float intSpeed;
    public String adress;
    public long longAdress;
    //单位是大卡
    public float cal;

    public float minSpeed;

    public long connTime;
    public long endTime;
    public long finishTime;
    public long finishDuration;
    public int duration;
    public String strFinishTime;


    public long pauseTime;
    public long startPauseTime;
    public long endParuseTime;


    public float speed;
    public float hourSpeed;

    public int currentResistance;

    public boolean isFinish;

    public BikeBean() {
        hrlist = new LinkedList<>();
        powList = new LinkedList<>();
        rpmList = new LinkedList<>();
        connTime = System.currentTimeMillis();
        wheelC = BikeUtil.getWheelC();
        this.adress = adress;
        this.longAdress = longAdress;
        minSpeed = 0;
        currentResistance = 0;
        pauseTime = 0;
        isFinish = false;
        finishTime = 0;
        finishDuration = 0;
        strFinishTime = "";
        cal = 0;
    }


    public void clearBikeValue() {
        hrlist.clear();
        powList.clear();
        rpmList.clear();
        fzSpeed = 0;//发电机的转速
        tzSpeed = 0;//踏频
        wheelSpeed = 0;
        wheelNumber = 0;
        dis = 0;
        light = 0;
        power = 0;
        pauseTime = 0;
        isFinish = false;

        sumSpeed = 0;
        sumPower = 0;
        intSpeed = 0;

        minSpeed = 0;


        connTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
        finishTime = System.currentTimeMillis();
        finishDuration = 0;
        strFinishTime = "";

        startPauseTime = 0;
        endParuseTime = 0;


        speed = 0;
        hourSpeed = 0;
    }

    public void setdefult() {
        fzSpeed = 0;//发电机的转速
        tzSpeed = 0;//踏频
        wheelSpeed = 0;
        wheelNumber = 0;
        dis = 0;
        light = 0;
        power = 0;
        pauseTime = 0;
        cal = 0;

        sumSpeed = 0;
        sumPower = 0;
        intSpeed = 0;

        minSpeed = 0;
        isFinish = false;

        connTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
        finishTime = System.currentTimeMillis();
        finishDuration = 0;
        strFinishTime = "";

        startPauseTime = 0;
        endParuseTime = 0;


        speed = 0;
        hourSpeed = 0;

    }


    @Override
    public int compareTo(@NonNull Object o) {
        BikeBean bikeBean = (BikeBean) o;

        if (this.isFinish) {
            if (this.finishDuration < bikeBean.finishDuration) {
                return -1;
            } else {
                return 1;
            }

        } else {
//            if (this.light > bikeBean.light) {
//                return -1;
//            } else {
//                return 1;
//            }
            if (this.dis > bikeBean.dis) {
                return -1;
            } else {
                return 1;
            }
        }


    }


}
