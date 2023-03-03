package com.jkcq.homebike.ride.history.bean;

public class HistoryDateBean {
    String date;
    String mdDate;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMdDate() {
        return mdDate;
    }

    public void setMdDate(String mdDate) {
        this.mdDate = mdDate;
    }

    @Override
    public String toString() {
        return "HistoryDateBean{" +
                "date='" + date + '\'' +
                ", mdDate='" + mdDate + '\'' +
                '}';
    }
}
