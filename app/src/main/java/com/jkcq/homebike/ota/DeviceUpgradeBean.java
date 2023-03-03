package com.jkcq.homebike.ota;

public class DeviceUpgradeBean {
    /**
     * "id": "1146621519413837825",
     * "createTime": null,
     * "type": "1",
     * "deviceTypeName": null,
     * "typesOf": 1,
     * "appVersionCode": "15",
     * "appVersionName": "00.00.45",
     * "url": "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/device/w516_v0045.zip",
     * "updateAble": 1,
     * "message": "123",
     * "remark": "1.test1 \\n 2.test2测试",
     * "operateUserName": "18682008982",
     * "operateUserId": "2",
     * "obsolete": 1,
     * "fileSize": 115733,
     * "remarkEn": "1.test1 \\n 2.test2"
     */

    String id;
    String createTime;
    int type;
    String deviceTypeName;
    int typesOf;
    String appVersionCode;
    String appVersionName;
    String url;
    int updateAble;
    String message;
    String remark;
    String operateUserName;
    String operateUserId;
    String obsolete;
    long fileSize;
    String remarkEn;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public int getTypesOf() {
        return typesOf;
    }

    public void setTypesOf(int typesOf) {
        this.typesOf = typesOf;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUpdateAble() {
        return updateAble;
    }

    public void setUpdateAble(int updateAble) {
        this.updateAble = updateAble;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getObsolete() {
        return obsolete;
    }

    public void setObsolete(String obsolete) {
        this.obsolete = obsolete;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getRemarkEn() {
        return remarkEn;
    }

    public void setRemarkEn(String remarkEn) {
        this.remarkEn = remarkEn;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeviceUpgradeBean{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", type=" + type +
                ", deviceTypeName='" + deviceTypeName + '\'' +
                ", typesOf=" + typesOf +
                ", appVersionCode='" + appVersionCode + '\'' +
                ", appVersionName='" + appVersionName + '\'' +
                ", url='" + url + '\'' +
                ", updateAble=" + updateAble +
                ", message='" + message + '\'' +
                ", remark='" + remark + '\'' +
                ", operateUserName='" + operateUserName + '\'' +
                ", operateUserId='" + operateUserId + '\'' +
                ", obsolete='" + obsolete + '\'' +
                ", fileSize=" + fileSize +
                ", remarkEn='" + remarkEn + '\'' +
                '}';
    }
}
