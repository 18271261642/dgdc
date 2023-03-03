package com.jkcq.homebike.ride.sceneriding.bean;

import java.io.Serializable;

/*
 *
 *
 * @author mhj
 * Create at 2019/4/1 10:46
 */public class SenceBeans implements Serializable {


    /**
     * uid : 1048
     * createTime : null
     * page : 1
     * size : 10
     * difficulty : 中
     * lastOperationTime : 2019-01-18 17:02:41
     * length : 3.2
     * imageUrl : https://manager.fitalent.com.cn/static/2018/8/26/17-38-31-9376037.png
     * name : 中国高速公路
     * videoUrl : https://isportcloud.oss-cn-shenzhen.aliyuncs.com/%E4%B8%AD%E5%9B%BD%E9%AB%98%E9%80%9F%E5%85%AC%E8%B7%AF.mp4
     * slope : 1,4,2;2,8,4;3,10,4;4,8,4;5,5,2;6,10,8;7,8,4;8,4,2;9,2,2
     * status : 1
     * videoSize : 16586180
     */

    private String uid;
    private Object createTime;
    private int page;
    private int size;
    private String difficulty;
    private String lastOperationTime;
    private double length;
    private String imageUrl;
    private String name;
    private String videoUrl;
    private String slope;
    private int status;
    private int videoSize;
    private int version;

    public String videoPath;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLastOperationTime() {
        return lastOperationTime;
    }

    public void setLastOperationTime(String lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(int videoSize) {
        this.videoSize = videoSize;
    }


    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }



    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SenceBeans) {
            return ((SenceBeans) (obj)).getUid().equals(this.uid) && ((SenceBeans) (obj)).getVersion() == this.version;
        }
        return false;
    }

    @Override
    public String toString() {
        return "SenceBeans{" +
                "uid='" + uid + '\'' +
                ", createTime=" + createTime +
                ", page=" + page +
                ", size=" + size +
                ", difficulty='" + difficulty + '\'' +
                ", lastOperationTime='" + lastOperationTime + '\'' +
                ", length=" + length +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", slope='" + slope + '\'' +
                ", status=" + status +
                ", videoSize=" + videoSize +
                ", version=" + version +
                ", videoPath='" + videoPath + '\'' +
                '}';
    }
}
