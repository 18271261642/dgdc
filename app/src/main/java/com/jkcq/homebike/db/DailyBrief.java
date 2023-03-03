package com.jkcq.homebike.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "exerciseTime DESC", unique = true)
})
public class DailyBrief {
    /**
     * "id": "1334339648601518081",
     * "deviceType": 83003,
     * "exerciseType": 0,
     * "duration": 155,
     * "distance": 6467,
     * "calorie": 238323,
     * "powerGeneration": 436,
     * "exerciseTime": 1606966338000,
     * "pkInfo": {
     * "id": null,
     * "pkName": null,
     * "participantNum": null,
     * "rank": null
     * },
     * "scenario": {
     * "imageUrl": null,
     * "name": null,
     * "nameEn": null,
     * "length": null
     * },
     * "course": {
     * "name": null,
     * "imageUrl": null,
     * "difficulty": null,
     * "length": null
     * }
     */

    @Id
    private Long id;
    String reportId;
    String userId;
    String deviceType;
    String exerciseType;
    String duration;
    String distance;
    String calorie;
    String powerGeneration;
    String exerciseTime;
    String strDate;
    String pkInfo;
    String scenario;
    String course;


    @Generated(hash = 1245711445)
    public DailyBrief(Long id, String reportId, String userId, String deviceType,
                      String exerciseType, String duration, String distance, String calorie,
                      String powerGeneration, String exerciseTime, String strDate,
                      String pkInfo, String scenario, String course) {
        this.id = id;
        this.reportId = reportId;
        this.userId = userId;
        this.deviceType = deviceType;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.distance = distance;
        this.calorie = calorie;
        this.powerGeneration = powerGeneration;
        this.exerciseTime = exerciseTime;
        this.strDate = strDate;
        this.pkInfo = pkInfo;
        this.scenario = scenario;
        this.course = course;
    }

    @Generated(hash = 2079952672)
    public DailyBrief() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportId() {
        return this.reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
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

    public String getExerciseType() {
        return this.exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalorie() {
        return this.calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getPowerGeneration() {
        return this.powerGeneration;
    }

    public void setPowerGeneration(String powerGeneration) {
        this.powerGeneration = powerGeneration;
    }


    public String getStrDate() {
        return this.strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getPkInfo() {
        return this.pkInfo;
    }

    public void setPkInfo(String pkInfo) {
        this.pkInfo = pkInfo;
    }

    public String getScenario() {
        return this.scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getCourse() {
        return this.course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "DailyBrief{" +
                "id=" + id +
                ", reportId='" + reportId + '\'' +
                ", userId='" + userId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", exerciseType='" + exerciseType + '\'' +
                ", duration='" + duration + '\'' +
                ", distance='" + distance + '\'' +
                ", calorie='" + calorie + '\'' +
                ", powerGeneration='" + powerGeneration + '\'' +
                ", exerciseTime='" + exerciseTime + '\'' +
                ", strDate='" + strDate + '\'' +
                ", pkInfo='" + pkInfo + '\'' +
                ", scenario='" + scenario + '\'' +
                ", course='" + course + '\'' +
                '}';
    }

    public String getExerciseTime() {
        return this.exerciseTime;
    }

    public void setExerciseTime(String exerciseTime) {
        this.exerciseTime = exerciseTime;
    }
}
