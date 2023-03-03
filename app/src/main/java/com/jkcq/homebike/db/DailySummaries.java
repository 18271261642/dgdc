package com.jkcq.homebike.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DailySummaries {
    /**
     * "exerciseDay": "2020-12-04",
     * "totalDistance": 4188,
     * "totalCalorie": 154439,
     * "times": 7
     * summaryType
     * deviceType
     * userId
     */
    @Id
    private Long id;
    String summaryType;
    String deviceType;
    String day;
    String userId;
    String exerciseDay;
    String totalDistance;
    String totalCalorie;
    String times;

    @Generated(hash = 646032396)
    public DailySummaries(Long id, String summaryType, String deviceType,
            String day, String userId, String exerciseDay, String totalDistance,
            String totalCalorie, String times) {
        this.id = id;
        this.summaryType = summaryType;
        this.deviceType = deviceType;
        this.day = day;
        this.userId = userId;
        this.exerciseDay = exerciseDay;
        this.totalDistance = totalDistance;
        this.totalCalorie = totalCalorie;
        this.times = times;
    }

    @Generated(hash = 1913683130)
    public DailySummaries() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummaryType() {
        return this.summaryType;
    }

    public void setSummaryType(String summaryType) {
        this.summaryType = summaryType;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExerciseDay() {
        return this.exerciseDay;
    }

    public void setExerciseDay(String exerciseDay) {
        this.exerciseDay = exerciseDay;
    }

    public String getTotalDistance() {
        return this.totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalCalorie() {
        return this.totalCalorie;
    }

    public void setTotalCalorie(String totalCalorie) {
        this.totalCalorie = totalCalorie;
    }

    public String getTimes() {
        return this.times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "DailySummaries{" +
                "id=" + id +
                ", summaryType='" + summaryType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", day='" + day + '\'' +
                ", userId='" + userId + '\'' +
                ", exerciseDay='" + exerciseDay + '\'' +
                ", totalDistance='" + totalDistance + '\'' +
                ", totalCalorie='" + totalCalorie + '\'' +
                ", times='" + times + '\'' +
                '}';
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
