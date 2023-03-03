package com.jkcq.homebike.ble.bike.reponsebean;

public class DailybriefBean {
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
     * }
     */
    String id;
    String deviceType;
    String exerciseType;
    String duration;
    String distance;
    String calorie;
    String powerGeneration;
    String exerciseTime;
    PkInfo pkInfo;
    Scenario scenario;
    CourseInfo course;

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "DailybriefBean{" +
                "id='" + id + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", exerciseType='" + exerciseType + '\'' +
                ", duration='" + duration + '\'' +
                ", distance='" + distance + '\'' +
                ", calorie='" + calorie + '\'' +
                ", powerGeneration='" + powerGeneration + '\'' +
                ", exerciseTime='" + exerciseTime + '\'' +
                ", pkInfo=" + pkInfo +
                ", scenario=" + scenario +
                ", course=" + course +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getPowerGeneration() {
        return powerGeneration;
    }

    public void setPowerGeneration(String powerGeneration) {
        this.powerGeneration = powerGeneration;
    }

    public String getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(String exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public PkInfo getPkInfo() {
        return pkInfo;
    }

    public void setPkInfo(PkInfo pkInfo) {
        this.pkInfo = pkInfo;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
}
