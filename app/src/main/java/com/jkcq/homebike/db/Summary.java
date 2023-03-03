package com.jkcq.homebike.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Summary {
    /**
     * "totalDuration": 1023,
     * "totalDistance": 29618,
     * "totalCalorie": 884343,
     * "totalPowerGeneration": 2762,
     * "totalTimes": 15
     * summaryType
     * deviceType
     * userId
     */

    @Id
    private Long id;
    private String day;
    private String userId;
    private String deviceType;
    private String summaryType;
    private String totalDuration;
    private String totalDistance;
    private String totalCalorie;
    private String totalPowerGeneration;
    private String totalTimes;
    private String upgradeId;

    @Generated(hash = 507602924)
    public Summary(Long id, String day, String userId, String deviceType, String summaryType,
            String totalDuration, String totalDistance, String totalCalorie,
            String totalPowerGeneration, String totalTimes, String upgradeId) {
        this.id = id;
        this.day = day;
        this.userId = userId;
        this.deviceType = deviceType;
        this.summaryType = summaryType;
        this.totalDuration = totalDuration;
        this.totalDistance = totalDistance;
        this.totalCalorie = totalCalorie;
        this.totalPowerGeneration = totalPowerGeneration;
        this.totalTimes = totalTimes;
        this.upgradeId = upgradeId;
    }

    @Generated(hash = 1461598545)
    public Summary() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSummaryType() {
        return this.summaryType;
    }

    public void setSummaryType(String summaryType) {
        this.summaryType = summaryType;
    }

    public String getTotalDuration() {
        return this.totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
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

    public String getTotalPowerGeneration() {
        return this.totalPowerGeneration;
    }

    public void setTotalPowerGeneration(String totalPowerGeneration) {
        this.totalPowerGeneration = totalPowerGeneration;
    }

    public String getTotalTimes() {
        return this.totalTimes;
    }

    public void setTotalTimes(String totalTimes) {
        this.totalTimes = totalTimes;
    }

    public String getUpgradeId() {
        return this.upgradeId;
    }

    public void setUpgradeId(String upgradeId) {
        this.upgradeId = upgradeId;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "id=" + id +
                ", day='" + day + '\'' +
                ", userId='" + userId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", summaryType='" + summaryType + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", totalDistance='" + totalDistance + '\'' +
                ", totalCalorie='" + totalCalorie + '\'' +
                ", totalPowerGeneration='" + totalPowerGeneration + '\'' +
                ", totalTimes='" + totalTimes + '\'' +
                ", upgradeId='" + upgradeId + '\'' +
                '}';
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
