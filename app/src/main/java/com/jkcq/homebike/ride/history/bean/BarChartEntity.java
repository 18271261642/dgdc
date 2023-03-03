package com.jkcq.homebike.ride.history.bean;

/**
 * 作者：chs on 2016/9/6 15:14
 * 邮箱：657083984@qq.com
 */
public class BarChartEntity {
    private String xLabel;
    private Float[] yValue;
    private float sum;
    private String date;

    public BarChartEntity(String xLabel, String date, Float[] yValue) {
        this.xLabel = xLabel;
        this.date = date;
        this.yValue = yValue;
        for (float y : yValue) {
            sum += y;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public String getxLabel() {
        return xLabel;
    }

    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public BarChartEntity(Float[] yValue) {
        this.yValue = yValue;
    }

    public Float[] getyValue() {
        return yValue;
    }

    public void setyValue(Float[] yValue) {
        this.yValue = yValue;
        for (float y : yValue) {
            sum += y;
        }
    }

    public float getSum() {
        return sum;
    }
}
