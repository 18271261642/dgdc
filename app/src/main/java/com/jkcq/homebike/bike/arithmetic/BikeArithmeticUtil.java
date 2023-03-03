package com.jkcq.homebike.bike.arithmetic;

public class BikeArithmeticUtil {

    public static double getGEnergy(double cal) {
        /**
         * 阻力不变时：G =（RS*17.3 *阻力）*T
         或者：发电量=卡路里/0.2389*转化率（0.6~0.7）
         */
        return cal / 0.2389 * 0.6;
    }

    public static double getHourDis(double sumDis, long mill) {
        long mills = System.currentTimeMillis() - mill;
        return sumDis / (mills * 1.0 / 1000 / 60 / 60);
    }

    /**
     * @param wheelC
     * @param revolutionsSpeed 每分钟的转速
     * @return 每分钟的距离
     */
    public static double getDisKm(double wheelC, double revolutionsSpeed) {
/**
 * C*Z/100/1000（将cm换算成km）
 */
        return wheelC * (revolutionsSpeed / 60) / 100000;
    }

//    public static double getRote(long videomill) {
//
//
//        /**
//         * 视频播放的时长，单位为秒
//         */
//
//        long videoSencond = videomill / 1000;
//        /**
//         * 80公里米小时转化成分钟
//         */
//        double videoSpeed = 60 / 3.6;
//
//
//        //转换成米 将千米转换了 米  1000米=1km
//
//
//        return videoSencond / ((videoSpeed * videoSencond) / (BikeEquipmentBase.getInstance().revolutionsSpeed / 60 * BikeEquipmentBase.getInstance().wheelC / 100));
//
//
//    }
}
