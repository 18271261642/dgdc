package com.jkcq.homebike.ride.history.bean;


import com.jkcq.homebike.ble.bike.reponsebean.DailybriefBean;

import java.util.ArrayList;

public class WeekBean {

    int sumCount;
    String cal;
    String date;
    ArrayList<DailybriefBean> list;
    boolean isOpen;


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


    public int getSumCount() {
        return sumCount;
    }

    public void setSumCount(int sumCount) {
        this.sumCount = sumCount;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<DailybriefBean> getList() {
        return list;
    }

    public void setList(ArrayList<DailybriefBean> list) {
        this.list = list;
    }
}
