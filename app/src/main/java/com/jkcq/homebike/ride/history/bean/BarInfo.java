package com.jkcq.homebike.ride.history.bean;

import java.util.Objects;

public class BarInfo {


    String date;
    String mdDate;
    boolean select;
    int maxVlaue;
    int currentValue;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getMaxVlaue() {
        return maxVlaue;
    }

    public void setMaxVlaue(int maxVlaue) {
        this.maxVlaue = maxVlaue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }


    public String getMdDate() {
        return mdDate;
    }

    public void setMdDate(String mdDate) {
        this.mdDate = mdDate;
    }

    public BarInfo(String date, int currentValue, int maxVlaue, boolean select) {
        this.date = date;
        this.currentValue = currentValue;
        this.maxVlaue = maxVlaue;
        this.select = select;
    }

    public BarInfo() {

    }

    public BarInfo(String date, String mdDate, int currentValue, int maxVlaue, boolean select) {
        this.date = date;
        this.mdDate = mdDate;
        this.currentValue = currentValue;
        this.maxVlaue = maxVlaue;
        this.select = select;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarInfo barInfo = (BarInfo) o;
        return Objects.equals(date, barInfo.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }


}
