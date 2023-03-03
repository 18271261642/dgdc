package com.jkcq.homebike.ble.bean;

public class DeviceBean {
    String deviceTypeName;
    String deviceName;


    public DeviceBean() {

    }

    public DeviceBean(String deviceTypeName, String deviceName) {
        this.deviceTypeName = deviceTypeName;
        this.deviceName = deviceName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
