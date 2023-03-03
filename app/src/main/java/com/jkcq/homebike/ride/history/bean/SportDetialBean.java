package com.jkcq.homebike.ride.history.bean;

import java.util.ArrayList;

public class SportDetialBean {


    String strDate;
    String date;
    String sportName;
    String sportNameEn;
    String sportUrl;
    String sportDis;
    String sportTime;
    String sportCal;
    String sportId;
    String times;
    boolean isOpen;
    int sportCount;



    ArrayList<SportDetialBean> list;
    ArrayList<SportDetialBean> currentShowList;


    public ArrayList<SportDetialBean> getList() {
        return list;
    }


    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setList(ArrayList<SportDetialBean> list) {

        this.list = list;

     /*   this.list = new ArrayList<>();
        this.list.addAll(list);*/

    }

    public ArrayList<SportDetialBean> getCurrentShowList() {
        if (currentShowList == null || currentShowList.size() == 0) {
            currentShowList = new ArrayList<>();
        }
        return currentShowList;
    }

    public void setCurrentShowList(ArrayList<SportDetialBean> currentShowList) {
        this.currentShowList = currentShowList;
    }

    public SportDetialBean() {

    }

    public SportDetialBean(String date, String sportCal, String sporDis, String times) {
        this.date = date;
        this.sportCal = sportCal;
        this.sportDis = sporDis;
        this.times = times;
    }



    public SportDetialBean(String sportId, String date, String sportName, String sportUrl, String sportDis, String sportTime, String sportCal) {
        this.sportId = sportId;
        this.date = date;
        this.sportName = sportName;
        this.sportUrl = sportUrl;
        this.sportDis = sportDis;
        this.sportTime = sportTime;
        this.sportCal = sportCal;
    }

    public SportDetialBean(String sportId, String strdate, String date, String sportName,String sportEn, String sportUrl, String sportDis, String sportTime, String sportCal) {
        this.sportId = sportId;
        this.strDate = strdate;
        this.date = date;
        this.sportNameEn=sportEn;
        this.sportName = sportName;
        this.sportUrl = sportUrl;
        this.sportDis = sportDis;
        this.sportTime = sportTime;
        this.sportCal = sportCal;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
    public String getSportNameEn() {
        return sportNameEn;
    }

    public void setSportNameEn(String sportName) {
        this.sportNameEn = sportName;
    }

    public String getSportUrl() {
        return sportUrl;
    }

    public void setSportUrl(String sportUrl) {
        this.sportUrl = sportUrl;
    }

    public String getSportDis() {
        return sportDis;
    }

    public void setSportDis(String sportDis) {
        this.sportDis = sportDis;
    }

    public String getSportTime() {
        return sportTime;
    }

    public void setSportTime(String sportTime) {
        this.sportTime = sportTime;
    }

    public String getSportCal() {
        return sportCal;
    }

    public void setSportCal(String sportCal) {
        this.sportCal = sportCal;
    }

    public int getSportCount() {
        return sportCount;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public void setSportCount(int sportCount) {
        this.sportCount = sportCount;
    }

    @Override
    public String toString() {
        return "SportDetialBean{" +
                "date='" + date + '\'' +
                ", sportName='" + sportName + '\'' +
                ", sportUrl='" + sportUrl + '\'' +
                ", sportDis='" + sportDis + '\'' +
                ", sportTime='" + sportTime + '\'' +
                ", sportCal='" + sportCal + '\'' +
                ", sportId='" + sportId + '\'' +
                ", isOpen=" + isOpen +
                ", list=" + list +
                ", currentShowList=" + currentShowList +
                '}';
    }
}
