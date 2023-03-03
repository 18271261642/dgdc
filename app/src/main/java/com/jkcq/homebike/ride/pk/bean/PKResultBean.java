package com.jkcq.homebike.ride.pk.bean;

public class PKResultBean {
    /**
     * "id": "1347441153198092290",
     * "cyclingPkId": "1347440729661468674",
     * "userId": 86,
     * "nickName": "186****8615",
     * "avatar": "https://manager.fitalent.com.cn/static/2018/9/19/9-54-56-835426.png",
     * "cyclingRecordId": "1347441153135177730",
     * "creatorFlag": false,
     * "finishFlag": true,
     * "durationMillis": 298,
     * "distance": 0,
     * "recordBreakingStatus": 0
     *
     */

    String id;
    String cyclingPkId;
    String userId;
    String nickName;
    String avatar;
    String cyclingRecordId;
    boolean creatorFlag;
    boolean finishFlag;
    String durationMillis;
    String distance;
    int recordBreakingStatus;
    int ranking;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCyclingPkId() {
        return cyclingPkId;
    }

    public void setCyclingPkId(String cyclingPkId) {
        this.cyclingPkId = cyclingPkId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCyclingRecordId() {
        return cyclingRecordId;
    }

    public void setCyclingRecordId(String cyclingRecordId) {
        this.cyclingRecordId = cyclingRecordId;
    }

    public boolean isCreatorFlag() {
        return creatorFlag;
    }

    public void setCreatorFlag(boolean creatorFlag) {
        this.creatorFlag = creatorFlag;
    }

    public boolean isFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(boolean finishFlag) {
        this.finishFlag = finishFlag;
    }

    public String getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(String durationMillis) {
        this.durationMillis = durationMillis;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getRecordBreakingStatus() {
        return recordBreakingStatus;
    }

    public void setRecordBreakingStatus(int recordBreakingStatus) {
        this.recordBreakingStatus = recordBreakingStatus;
    }

    @Override
    public String toString() {
        return "PKResultBean{" +
                "id='" + id + '\'' +
                ", cyclingPkId='" + cyclingPkId + '\'' +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cyclingRecordId='" + cyclingRecordId + '\'' +
                ", creatorFlag=" + creatorFlag +
                ", finishFlag=" + finishFlag +
                ", durationMillis='" + durationMillis + '\'' +
                ", distance='" + distance + '\'' +
                ", recordBreakingStatus=" + recordBreakingStatus +
                ", ranking=" + ranking +
                '}';
    }
}
