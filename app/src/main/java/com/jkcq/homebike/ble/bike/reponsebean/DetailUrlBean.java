package com.jkcq.homebike.ble.bike.reponsebean;

public class DetailUrlBean {
    /**
     * "dark": "string",
     * "light": "string"
     */
    String dark;
    String light;

    public String getDark() {
        return dark;
    }

    public void setDark(String dark) {
        this.dark = dark;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    @Override
    public String toString() {
        return "DetailUrlBean{" +
                "dark='" + dark + '\'' +
                ", light='" + light + '\'' +
                '}';
    }
}
