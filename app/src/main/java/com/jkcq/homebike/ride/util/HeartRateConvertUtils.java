package com.jkcq.homebike.ride.util;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

/*
 *
 *
 * @author mhj
 * Create at 2018/4/20 10:26
 */
public class HeartRateConvertUtils {

    //小数点后保留几位
    private static int scale = 2;


    //计算心率数据
    public static ArrayList<String> pointToheartRate(int age, String sex) {

        int h1 = (int) (0.5f * getMaxHeartRate(age, sex));
        int h2 = (int) (0.595f * getMaxHeartRate(age, sex));
        int h3 = (int) (0.6f * getMaxHeartRate(age, sex));
        int h4 = (int) (0.695f * getMaxHeartRate(age, sex));
        int h5 = (int) (0.7f * getMaxHeartRate(age, sex));
        int h6 = (int) (0.795f * getMaxHeartRate(age, sex));
        int h7 = (int) (0.8f * getMaxHeartRate(age, sex));
        int h8 = (int) (0.895f * getMaxHeartRate(age, sex));
        ArrayList<String> hrList = new ArrayList<>(6);
        hrList.add(">" + h8);
        hrList.add(h7 + "~" + h8);
        hrList.add(h5 + "~" + h6);
        hrList.add(h3 + "~" + h4);
        hrList.add(h1 + "~" + h2);
        hrList.add("<" + h1);
        return hrList;

    }


    public static boolean hrValueColor(int heartRate, int maxHearRate, TextView iv, TextView iv2, TextView iv3) {
        int point = 0;
        //心率强度
        double hearStrength = hearRate2Percent(heartRate, maxHearRate);
        if (hearStrength < 50) {
            iv.setTextColor(Color.parseColor("#BDC1C7"));
            iv2.setTextColor(Color.parseColor("#BDC1C7"));
            iv3.setTextColor(Color.parseColor("#BDC1C7"));
            return false;
            //point = R.color.color_leisure;
        } else if (hearStrength >= 50 && hearStrength < 60) {
            iv.setTextColor(Color.parseColor("#9399A5"));
            iv2.setTextColor(Color.parseColor("#9399A5"));
            iv3.setTextColor(Color.parseColor("#9399A5"));
            return false;
            //  point = R.color.color_warm_up;
        } else if (hearStrength >= 60 && hearStrength < 70) {
            iv.setTextColor(Color.parseColor("#3FA6F2"));
            iv2.setTextColor(Color.parseColor("#3FA6F2"));
            iv3.setTextColor(Color.parseColor("#3FA6F2"));
            return false;
            // point = R.color.color_fat_burning_exercise;
        } else if (hearStrength >= 70 && hearStrength < 80) {
            iv.setTextColor(Color.parseColor("#14D36B"));
            iv2.setTextColor(Color.parseColor("#14D36B"));
            iv3.setTextColor(Color.parseColor("#14D36B"));
            return false;
            // point = R.color.color_aerobic_exercise;
        } else if (hearStrength >= 80 && hearStrength < 90) {
            iv.setTextColor(Color.parseColor("#FFCB14"));
            iv2.setTextColor(Color.parseColor("#FFCB14"));
            iv3.setTextColor(Color.parseColor("#FFCB14"));
            return true;
            //  point = R.color.color_anaerobic_exercise;
        } else if (hearStrength >= 90) {
            iv.setTextColor(Color.parseColor("#F85842"));
            iv2.setTextColor(Color.parseColor("#F85842"));
            iv3.setTextColor(Color.parseColor("#F85842"));
            return true;
            //  point = R.color.color_limit;
        }
        return false;
      /*  if (Constant.UNIT_MILLS.equals(util)) {
            return Arith.div(point,60/Constant.REFRESH_RATE);
        }else{

        }*/
    }

    public static int hrValueColor(int heartRate, int maxHearRate) {
        int point = 0;
        //心率强度
        double hearStrength = hearRate2Percent(heartRate, maxHearRate);
        if (hearStrength < 50) {
            return (Color.parseColor("#BDC1C7"));
            //point = R.color.color_leisure;
        } else if (hearStrength >= 50 && hearStrength < 60) {
            return (Color.parseColor("#9399A5"));
            //  point = R.color.color_warm_up;
        } else if (hearStrength >= 60 && hearStrength < 70) {
            return (Color.parseColor("#3FA6F2"));
            // point = R.color.color_fat_burning_exercise;
        } else if (hearStrength >= 70 && hearStrength < 80) {
            return (Color.parseColor("#14D36B"));
            // point = R.color.color_aerobic_exercise;
        } else if (hearStrength >= 80 && hearStrength < 90) {
            return (Color.parseColor("#FFCB14"));
            //  point = R.color.color_anaerobic_exercise;
        } else if (hearStrength >= 90) {
            return (Color.parseColor("#F85842"));
            //  point = R.color.color_limit;
        }
        return (Color.parseColor("#BDC1C7"));
    }


    //点数,单次心率点数
    public static int hearRate2Point(int heartRate, int maxHearRate) {
        int point = 0;
        //心率强度
        double hearStrength = hearRate2Percent(heartRate, maxHearRate);
        if (hearStrength < 50) {
            point = 0;
        } else if (hearStrength >= 50 && hearStrength < 60) {
            point = 1;
        } else if (hearStrength >= 60 && hearStrength < 70) {
            point = 2;
        } else if (hearStrength >= 70 && hearStrength < 80) {
            point = 3;
        } else if (hearStrength >= 80 && hearStrength < 90) {
            point = 4;
        } else if (hearStrength >= 90) {
            point = 5;
        }
        return point;
      /*  if (Constant.UNIT_MILLS.equals(util)) {
            return Arith.div(point,60/Constant.REFRESH_RATE);
        }else{

        }*/
    }

    //心率强度
    public static double hearRate2Percent(int heartRate, int maxHeartRate) {
        double percent = 0;
        //心率强度
//        percent = Arith.div(heartRate,maxHeartRate,0)*100;
        percent = (heartRate * 1.0f / maxHeartRate) * 100;
        //  percent = Arith.div(percent, 1, 0);
        return percent;
    }

    //最大心率
    public static int getMaxHeartRate(int age, String gender) {
        //女的226
        //男的220
        int maxHeartRate = 220 - age;
        if (!TextUtils.isEmpty(gender)) {
            if (gender.equals("Female")) {
                maxHeartRate = 226 - age;
            } else {
                maxHeartRate = 220 - age;
            }
        }
        return maxHeartRate;
    }


    //
    public static int hearRate2MatchRate(int heartRate) {

        int matchRate = 0;

        return matchRate;
    }

    public static String doubleParseStr(double data) {
        int num = (int) data;
        return String.valueOf(num);
    }

}
