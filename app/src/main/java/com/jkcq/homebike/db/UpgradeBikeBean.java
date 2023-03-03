package com.jkcq.homebike.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "exerciseTime DESC", unique = true)
})
public class UpgradeBikeBean {

    @Id
    private Long id;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 设备类型
     */
    private int deviceType;
    /**
     * 骑行类型，0：自由骑行，1：线路骑行，2：PK骑行
     */
    private int exerciseType;
    /**
     * 骑行持续时间（秒）
     */
    private int duration;
    /**
     * 里程（米）
     */
    private int distance;
    /**
     * 消耗卡路里（卡）
     */
    private int calorie;
    /**
     * 发电量（焦）
     */
    private int powerGeneration;
    /**
     * 踏频数组
     */
    private String steppedFrequencyArray;
    /**
     * 功率数组
     */
    private String powerArray;
    /**
     * 心率数组
     */
    private String heartRateArray;
    /**
     * 线路id
     */
    private String relevanceId;
    /**
     * 骑行完成时间
     */
    @NotNull
    private Long exerciseTime;

@Generated(hash = 761458445)
public UpgradeBikeBean(Long id, String userId, int deviceType, int exerciseType,
        int duration, int distance, int calorie, int powerGeneration,
        String steppedFrequencyArray, String powerArray, String heartRateArray,
        String relevanceId, @NotNull Long exerciseTime) {
    this.id = id;
    this.userId = userId;
    this.deviceType = deviceType;
    this.exerciseType = exerciseType;
    this.duration = duration;
    this.distance = distance;
    this.calorie = calorie;
    this.powerGeneration = powerGeneration;
    this.steppedFrequencyArray = steppedFrequencyArray;
    this.powerArray = powerArray;
    this.heartRateArray = heartRateArray;
    this.relevanceId = relevanceId;
    this.exerciseTime = exerciseTime;
}
@Generated(hash = 858544772)
public UpgradeBikeBean() {
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
public int getDeviceType() {
    return this.deviceType;
}
public void setDeviceType(int deviceType) {
    this.deviceType = deviceType;
}
public int getExerciseType() {
    return this.exerciseType;
}
public void setExerciseType(int exerciseType) {
    this.exerciseType = exerciseType;
}
public int getDuration() {
    return this.duration;
}
public void setDuration(int duration) {
    this.duration = duration;
}
public int getDistance() {
    return this.distance;
}
public void setDistance(int distance) {
    this.distance = distance;
}
public int getCalorie() {
    return this.calorie;
}
public void setCalorie(int calorie) {
    this.calorie = calorie;
}
public int getPowerGeneration() {
    return this.powerGeneration;
}
public void setPowerGeneration(int powerGeneration) {
    this.powerGeneration = powerGeneration;
}
public String getSteppedFrequencyArray() {
    return this.steppedFrequencyArray;
}
public void setSteppedFrequencyArray(String steppedFrequencyArray) {
    this.steppedFrequencyArray = steppedFrequencyArray;
}
public String getPowerArray() {
    return this.powerArray;
}
public void setPowerArray(String powerArray) {
    this.powerArray = powerArray;
}
public String getHeartRateArray() {
    return this.heartRateArray;
}
public void setHeartRateArray(String heartRateArray) {
    this.heartRateArray = heartRateArray;
}
public Long getExerciseTime() {
    return this.exerciseTime;
}
public void setExerciseTime(Long exerciseTime) {
    this.exerciseTime = exerciseTime;
}

    @Override
    public String toString() {
        return "UpgradeBikeBean{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", deviceType=" + deviceType +
                ", exerciseType=" + exerciseType +
                ", duration=" + duration +
                ", distance=" + distance +
                ", calorie=" + calorie +
                ", powerGeneration=" + powerGeneration +
                ", steppedFrequencyArray='" + steppedFrequencyArray + '\'' +
                ", powerArray='" + powerArray + '\'' +
                ", heartRateArray='" + heartRateArray + '\'' +
                ", relevanceId='" + relevanceId + '\'' +
                ", exerciseTime=" + exerciseTime +
                '}';
    }
public String getRelevanceId() {
    return this.relevanceId;
}
public void setRelevanceId(String relevanceId) {
    this.relevanceId = relevanceId;
}
}
