package com.jkcq.homebike.bike.arithmetic;


import android.util.Log;

import com.jkcq.homebike.bike.BikeConfig;
import com.jkcq.homebike.bike.bean.BikeBean;
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType;

public class BikeUtil {
    public static String currentBikeName = "";

    static float cadenceCoefficient003 = 17f;

    static float wheelRotateSpeedCoefficient005 = (0.78f * 16.67f / 4.34f);
    static float wheelRotateSpeedCoefficient003 = 3.936f;

    /**
     * @param str 计算的毫秒的数据 0.05秒
     */
    public static int getZSpeed(String str) {

        String data = getSubStr(str, 15, 17);
        int powerInt = Integer.parseInt(data, 16);
        if (getSubStr(str, 19, 20).equals("1")) {
            powerInt = powerInt + 255;
        }

        return powerInt;

    }

    /**
     * 单位为秒
     *
     * @param speedInt 一秒钟的转速
     * @return 发电机的转速
     */

    public static float getFZSpeed(float speedInt) {

        return speedInt;

    }


    /**
     *
     */

    /**
     * @param fzSpeed 发电机的转速
     * @return 踏平的 每分钟的踏平
     */
    public static float getTZSpeed(float fzSpeed) {
        if (currentBikeName.contains(DeviceType.DEVICE_S005.getName())) {
            return fzSpeed / 16.67f;
        } else {
            return fzSpeed / 22.3f;

        }
        // return fzSpeed / 2.4f;

    }

    /**
     * @param tzSpeed 每分钟踏频的速度踏频的速度
     * @return 轮子每秒钟的转速
     * 踏一圈走4.4圈
     */

    public static float getWheelZSpeend(float tzSpeed) {
        if (currentBikeName.contains(DeviceType.DEVICE_S005.getName())) {
            //(16.67/4.34f)
            // return tzSpeed / wheelRotateSpeedCoefficient005 / 60;
            return tzSpeed / wheelRotateSpeedCoefficient005 / 60;
        } else {
            return tzSpeed / wheelRotateSpeedCoefficient003 / 60;

        }

        //return tzSpeed / 60.0f * 4.4f;
    }

    /**
     * @param sumSpeed     之前的总共的和
     * @param currentSpeed 当前秒钟的转速
     * @return 轮子的转数
     */
    public static float getWheelZNumber(float sumSpeed, float currentSpeed) {
        return sumSpeed + currentSpeed;
    }

    /**
     * @param speed
     * @param l     单位是米
     * @return
     */
    public static double getHourKm(double speed, double l) {
        return speed * l * 3.6;
    }

    /**
     * 获取24寸的周长，单位为 米
     * <p>
     * 1寸=3.3333333厘米(cm)
     * 1英寸(in)=2.54厘米(cm)
     *
     * @return
     */
    public static float getWheelC() {

        return 3.14f * 2.54f * cadenceCoefficient003 / 100;
        //3.14*一寸*轮股 =厘米

    }

    /**
     * @param
     * @param
     * @return 距离 单位为km
     */
    public static float getDisKm(float mDis) {
        return mDis / 1000;

    }

    /**
     * @param wheelC      轮子的周长
     * @param wheelNumber 转数
     * @return 距离 单位为km
     */
    public static float getDisM(float wheelC, float wheelNumber) {
        return wheelC * wheelNumber;

    }

    /**
     * @param dis   距离km
     * @param mills 时间是毫秒秒转换成小时
     * @return 速度
     */

    /*public static double getSpeed(double dis, long mills) {

        return dis /(mills / 1000.0 / 3600);

    }*/
    public static double getSpeed(double dis, long mills) {

        if (dis == 0) {
            return 0;
        } else {
            return (dis * 1000) / (mills / 1000.0) * 3.6;
        }

    }

    public static float getWheelSpeed(float zSpeed, long mills) {
       /* Log.d("getWheelSpeed: ", dis + "");
        if (dis == 0) {
            return 0;
        } else {
            return dis * 1000 / 1 * 3.6;
        }*/
        /**
         * 4.5 是踏频一圈轮子转4.5圈
         * getWheelC() 周长
         * 0.06 单位的转换，分钟转换成小时，米转换成千米
         */
        return zSpeed * 4.5f * 0.06f * getWheelC();

    }

    /**
     * @param str
     * @return 返回0.05秒的功率
     */

    public static int getPower(String str) {

        String data = getSubStr(str, 9, 11);
        int speedInt = Integer.parseInt(data, 16);
        if (getSubStr(str, 13, 14).equals("1")) {
            speedInt = speedInt + 255;
        }

        return speedInt;
    }

    /**
     * @param sumLight     之前的发电量
     * @param currentLight 当前秒计算出的发电量
     * @return 总共的发电量 没秒的发电量总和
     */

    public static float getLight(float sumLight, float currentLight) {
        return sumLight + currentLight;
    }

    /**
     * @param sumLight
     * @return 发电量单位的转换 1000/3600 单位为千瓦小时
     */
    public static double getSumLight(double sumLight) {
        // return sumLight / 1000 / 3600;
        return sumLight / 1000;
    }

    /**
     * @param str
     * @param start 开始下标
     * @param end   结束下标
     * @return 需要获取的字符串
     */

    // 截取字符串
    public static String getSubStr(String str, int start, int end) {
        String lastData = str.substring(start, end);
        return lastData;
    }


    public static long getAdress(byte[] address) {
        long iaddr = (address[0] << 24) & 0xff000000 |
                (address[1] << 16) & 0x00ff0000 |
                (address[2] << 8) & 0x0000ff00 |
                (address[3] << 0) & 0x000000ff;
        return iaddr;
    }


    /**
     * @param level  困难等级
     * @param values 阻力
     * @return 发送给单车的阻力值
     */
    public static int changeResistanceByLevel(int level, int values) {

        int realValues = 1;
        switch (level) {
            case 1:
                if (values == 1 || values == 2) {
                    realValues = 1;
                } else if (values == 3) {
                    realValues = 2;
                } else if (values == 4) {
                    realValues = 3;
                } else if (values == 5) {
                    realValues = 4;
                } else if (values == 6) {
                    realValues = 5;
                } else if (values == 7) {
                    realValues = 6;
                } else if (values == 8) {
                    realValues = 7;
                } else if (values == 9) {
                    realValues = 8;
                } else if (values == 10) {
                    realValues = 9;
                }
                break;
            case 2:
                realValues = values;
                break;
            case 3:
                if (values == 1) {
                    realValues = 1;
                } else if (values == 2) {
                    realValues = 4;
                } else if (values == 3) {
                    realValues = 5;
                } else if (values == 4) {
                    realValues = 6;
                } else if (values == 5) {
                    realValues = 7;
                } else if (values == 6) {
                    realValues = 8;
                } else if (values == 7) {
                    realValues = 9;
                } else if (values == 8) {
                    realValues = 10;
                } else if (values == 9) {
                    realValues = 10;
                } else if (values == 10) {
                    realValues = 10;
                }
                break;
        }

        return realValues;
    }


    private static BikeBean sPkBikeBean;
    private static BikeBean sTargetBikeBean;
    private static BikeBean mRoadBean;

   /* private static Long mCurrentTime1 = 0L;
    private static Long mCurrentTime2 = 0L;
    private static Long mCurrentTime3 = 0L;
    private static Long mCurrentTime4 = 0L;*/


    public static BikeBean getGlobalBikebean(int power, int rpm, long duration, int hrValue) {
        if (BikeConfig.sBikeBean == null) {
            BikeConfig.sBikeBean = new BikeBean();
        }
        return getBikeBean(BikeConfig.sBikeBean, power, rpm, duration, hrValue);
    }

    /*public static BikeBean getPkBikebean(int power, int rpm) {

        if (sPkBikeBean == null) {
            sPkBikeBean = new BikeBean();
        }
        return getBikeBean(sPkBikeBean, power, rpm);
    }
*/
    /*public static BikeBean getTargetBikebean(int power, int rpm) {
        if (sTargetBikeBean == null) {
            sTargetBikeBean = new BikeBean();
        }
        return getBikeBean(sTargetBikeBean, power, rpm);
    }*/



   /* public static BikeBean getUsefulBikeBean(int power, int rpm, int type) {
        switch (type) {
            case BikeConfig.GLOBAL_BIKEBEAN:
                Long time1 = System.currentTimeMillis();
                if (time1 - mCurrentTime1 < 1000) {
                    return null;
                }
                mCurrentTime1 = time1;
                if (BikeConfig.sBikeBean == null) {
                    BikeConfig.sBikeBean = new BikeBean();
                }
                return getBikeBean(BikeConfig.sBikeBean, power, rpm);
            case BikeConfig.PK_BIKEBEAN:
                Long time2 = System.currentTimeMillis();
                if (time2 - mCurrentTime2 < 1000) {
                    return null;
                }
                mCurrentTime2 = time2;
                if (sPkBikeBean == null) {
                    sPkBikeBean = new BikeBean();
                }
                return getBikeBean(sPkBikeBean, power, rpm);
            case BikeConfig.TARGET_BIKEBEAN:
                Long time3 = System.currentTimeMillis();
                if (time3 - mCurrentTime3 < 1000) {
                    return null;
                }
                mCurrentTime3 = time3;
                if (sTargetBikeBean == null) {
                    sTargetBikeBean = new BikeBean();
                }
                return getBikeBean(sTargetBikeBean, power, rpm);
            case BikeConfig.ROAD_BIKEBEAN:
                Long time4 = System.currentTimeMillis();
                if (time4 - mCurrentTime4 < 1000) {
                    return null;
                }
                mCurrentTime4 = time4;
                if (mRoadBean == null) {
                    mRoadBean = new BikeBean();
                    mRoadBean.userInfo = BikeConfig.currentUser;
                }
                return getBikeBean(mRoadBean, power, rpm);

        }
        return getBikeBean(BikeConfig.sBikeBean, power, rpm);
    }*/

    private static BikeBean getBikeBean(BikeBean bikeBean, int power, int rpm, long duration, int hrValue) {
        bikeBean.speed = rpm;
        bikeBean.power = power;
        bikeBean.endTime = System.currentTimeMillis();

        bikeBean.duration = (int) (duration / 1000);
        bikeBean.hrlist.add(hrValue);
        bikeBean.powList.add(power);

        bikeBean.minSpeed = bikeBean.speed;

        bikeBean.fzSpeed = BikeUtil.getFZSpeed(bikeBean.speed);

        bikeBean.tzSpeed = BikeUtil.getTZSpeed(bikeBean.fzSpeed);
        bikeBean.rpmList.add((int) bikeBean.tzSpeed);

        Log.e("bikeBean.tzSpeed", "bikeBean.speed=" + bikeBean.speed + "bikeBean.fzSpeed=" + bikeBean.fzSpeed + ",bikeBean.tzSpeed=" + bikeBean.tzSpeed);

        bikeBean.wheelSpeed = BikeUtil.getWheelZSpeend(bikeBean.fzSpeed);

        bikeBean.wheelNumber = BikeUtil.getWheelZNumber(bikeBean.wheelNumber, bikeBean.wheelSpeed);

        bikeBean.dis = BikeUtil.getDisM(bikeBean.wheelC, bikeBean.wheelNumber);


        bikeBean.light = BikeUtil.getLight(bikeBean.light, bikeBean.power);


        if (BikeConfig.currentUser != null) {
            bikeBean.cal = BikeUtil.calCalorie(bikeBean.light);
        }

        bikeBean.hourSpeed = BikeUtil.getWheelSpeed(bikeBean.tzSpeed, System.currentTimeMillis());

        return bikeBean;
    }

    public static void clearBikeBean(int type) {
        switch (type) {
            case BikeConfig.GLOBAL_BIKEBEAN:
                BikeConfig.sBikeBean = null;
                break;
            case BikeConfig.PK_BIKEBEAN:
                sPkBikeBean = null;
                break;
            case BikeConfig.TARGET_BIKEBEAN:
                sTargetBikeBean = null;
                break;
            case BikeConfig.ROAD_BIKEBEAN:
                mRoadBean = null;
                break;
        }
    }

    //单位是卡

    public static float calCalorie(float light) {
        float calorie = (float) (light * 242.2480620155 * 0.02);//单位是卡
        return calorie;
    }

    public static int calAvgHeart(int heart1, int heart2) {
        if (heart2 == 0) {
            return heart1;
        } else {
            return (heart1 + heart2) / 2;
        }
    }

}
