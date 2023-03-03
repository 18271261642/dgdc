package com.jkcq.homebike.ride.util;

import com.jkcq.homebike.ride.sceneriding.bean.LineBean;
import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean;

import java.util.ArrayList;

public class SceneUtil {


    public static String[] splitSemicolonStr(String str) {

        return str.split(";");
    }

    public static String[] splitDotStr(String str) {
        return str.split(",");
    }

    public static ArrayList<LineBean> getLineBeanList(String[] strs) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < strs.length; i++) {
            list.add(getLineBean(strs[i]));
        }
        return list;
    }

    private static LineBean getLineBean(String str) {

        LineBean bean = new LineBean();
        String[] strings = splitDotStr(str);
        try {
            bean.number = Integer.parseInt(strings[0]);
            bean.resistance = Integer.parseInt(strings[1]);
            bean.zoneLen = Integer.parseInt(strings[2]);
            bean.rpm = Integer.parseInt(strings[3]);
        } catch (Exception e) {

        } finally {
            return bean;
        }

    }


    public static String calculateMatchLeverWithCurrentCadence(int currentCadence, int targetCadence) {
        if ((targetCadence <= 0) || (currentCadence < 0)) {
            // return BikeConfig.CyclingMatchLeverNone;
            //return "perfect.svga";
            return "";
        }

        int differCadence = (int) (currentCadence - targetCadence);

        if ((Math.abs(differCadence) * 1.0 / targetCadence) <= 0.05) {
            // ±5% 完美
            return "perfect.svga";
            //  return BikeConfig.CyclingMatchLeverPerfect;

        } else if ((Math.abs(differCadence) * 1.0 / targetCadence) <= 0.1) {
            // ±10% 太棒了
            return "great.svga";
            // return BikeConfig.CyclingMatchLeverGreat;

        } else if ((Math.abs(differCadence) * 1.0 / targetCadence) <= 0.15) {
            // ±15% 还不错
            return "good.svga";
            //return BikeConfig.CyclingMatchLeverGood;

        } else if (differCadence > 0) {
            // +15%以上 慢一点
            return "slow_down.svga";
            // return BikeConfig.CyclingMatchLeverSlowDown;

        } else {
            // -15%以下 加油
            return "come_on.svga";
            //return BikeConfig.CyclingMatchLeverComeOn;
        }
    }

    public static String calculatePoint(int currentCadence, int targetCadence) {
        if ((targetCadence <= 0) || (currentCadence <= 0)) {
            // return BikeConfig.CyclingMatchLeverNone;
            return "";
            // return "";
        }

        int differCadence = (int) (currentCadence - targetCadence);

        if ((Math.abs(differCadence) * 1.0 / targetCadence) <= 0.05) {
            // ±5% 完美
            return "point.svga";
            //  return BikeConfig.CyclingMatchLeverPerfect;

        } else if ((Math.abs(differCadence) * 1.0 / targetCadence) <= 0.1) {
            // ±10% 太棒了
            return "";
            // return BikeConfig.CyclingMatchLeverGreat;

        } else if ((Math.abs(differCadence) * 1.0 / targetCadence) <= 0.15) {
            // ±15% 还不错
            return "";
            //return BikeConfig.CyclingMatchLeverGood;

        } else if (differCadence > 0) {
            // +15%以上 慢一点
            return "";
            // return BikeConfig.CyclingMatchLeverSlowDown;

        } else {
            // -15%以下 加油
            return "";
            //return BikeConfig.CyclingMatchLeverComeOn;
        }
    }

    public static ArrayList<ResistanceIntervalBean> getResisteanceList(ArrayList<LineBean> values) {
        ArrayList<ResistanceIntervalBean> resistanceIntervalBeans = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < values.size(); i++) {
            LineBean lineBean = values.get(i);

            ResistanceIntervalBean resistanceIntervalBean = new ResistanceIntervalBean();
            //开始位置
            resistanceIntervalBean.setmIntervalStart(sum);
            //阻力值
            resistanceIntervalBean.setmResistances(lineBean.resistance);
            resistanceIntervalBean.setmRpm(lineBean.rpm);

            sum = sum + lineBean.zoneLen;

            //阻力结束位置
            resistanceIntervalBean.setmIntervalEnd(sum);
            resistanceIntervalBeans.add(resistanceIntervalBean);
        }
        return resistanceIntervalBeans;
        //  sumDis = sum;
    }
}
