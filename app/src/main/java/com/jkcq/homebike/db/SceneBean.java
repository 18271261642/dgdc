package com.jkcq.homebike.db;

import android.text.TextUtils;

import java.io.Serializable;

public class SceneBean implements Serializable {
    /**
     * "id": "1335862883784642562",
     * "difficulty": "2",
     * "length": 550,
     * "imageUrl": "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/1607329556596homePage_cyclingroute@2x.png",
     * "name": "测试而已",
     * "nameEn": "justTest",
     * "videoUrl": "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/160732947471920201203.mov",
     * audioUrl:
     * "slope": "1,20,20,20;2,40,40,40;3,50,50,50;4,60,60,60;5,70,70,70;6,80,80,80;7,90,90,90;8,100,100,100;9,10,10,10;10,30,30,30",
     * "videoSize": 67367161,
     * "videoDuration": 158,
     * "version": 1,
     * "deviceTypeId": "83003",
     * "deviceTypeName": "动感单车家庭版",
     * "participantNum": 0
     */

    String id;
    String difficulty;
    String length;
    String imageUrl;
    String name;
    String nameEn;
    String videoUrl;
    String audioUrl;
    String slope;
    String videoSize;
    String videoDuration;
    String version;
    String deviceTypeId;
    String deviceTypeName;
    String participantNum;
    int download;
    String downLoadPath;
    String createTime;
    long timeStamp;
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSlope() {
        return slope;
    }

    public void setSlope(String slope) {
        this.slope = slope;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getParticipantNum() {
        return participantNum;
    }

    public void setParticipantNum(String participantNum) {
        this.participantNum = participantNum;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getDownLoadPath() {
        return downLoadPath;
    }

    public void setDownLoadPath(String downLoadPath) {
        this.downLoadPath = downLoadPath;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAudioUrl() {
        if (TextUtils.isEmpty(audioUrl)) {
            audioUrl = "";
        }
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public String toString() {
        return "SceneBean{" +
                "id='" + id + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", length='" + length + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", slope='" + slope + '\'' +
                ", videoSize='" + videoSize + '\'' +
                ", videoDuration='" + videoDuration + '\'' +
                ", version='" + version + '\'' +
                ", deviceTypeId='" + deviceTypeId + '\'' +
                ", deviceTypeName='" + deviceTypeName + '\'' +
                ", participantNum='" + participantNum + '\'' +
                ", download=" + download +
                ", downLoadPath='" + downLoadPath + '\'' +
                ", createTime='" + createTime + '\'' +
                ", timeStamp=" + timeStamp +
                ", description='" + description + '\'' +
                '}';
    }
}
