package com.jkcq.homebike.ride.sceneriding.bean;

/*
 *
 *
 * @author mhj
 * Create at 2019/1/9 12:03
 */
public class ResistanceIntervalBean {

    private int mIntervalStart;//阻力开始位置
    private int mIntervalEnd;//阻力结束位置
    private int mResistances;//阻力值
    private int mstartDis;
    private int mendDis;
    private int mRpm;


    public int getmIntervalStart() {
        return mIntervalStart;
    }

    public void setmIntervalStart(int mIntervalStart) {
        this.mIntervalStart = mIntervalStart;
    }

    public int getmIntervalEnd() {
        return mIntervalEnd;
    }

    public void setmIntervalEnd(int mIntervalEnd) {
        this.mIntervalEnd = mIntervalEnd;
    }

    public int getmResistances() {
        return mResistances;
    }

    public void setmResistances(int mResistances) {
        this.mResistances = mResistances;
    }

    public int getMstartDis() {
        return mstartDis;
    }

    public void setMstartDis(int mstartDis) {
        this.mstartDis = mstartDis;
    }

    public int getMendDis() {
        return mendDis;
    }

    public void setMendDis(int mendDis) {
        this.mendDis = mendDis;
    }

    public int getmRpm() {
        return mRpm;
    }

    public void setmRpm(int mRpm) {
        this.mRpm = mRpm;
    }

    @Override
    public String toString() {
        return "ResistanceIntervalBean{" +
                "mIntervalStart=" + mIntervalStart +
                ", mIntervalEnd=" + mIntervalEnd +
                ", mResistances=" + mResistances +
                ", mstartDis=" + mstartDis +
                ", mendDis=" + mendDis +
                ", mRpm=" + mRpm +
                '}';
    }
}
