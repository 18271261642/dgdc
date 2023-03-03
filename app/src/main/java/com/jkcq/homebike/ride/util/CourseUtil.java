package com.jkcq.homebike.ride.util;

import com.jkcq.homebike.ride.sceneriding.bean.CourseLineBean;
import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean;

import java.util.ArrayList;

public class CourseUtil {


    public static String[] splitSemicolonStr(String str) {

        return str.split(";");
    }

    public static String[] splitDotStr(String str) {
        return str.split(",");
    }

    public static ArrayList<CourseLineBean> getLineBeanList(String[] strs) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < strs.length; i++) {
            list.add(getLineBean(strs[i]));
        }
        return list;
    }

    private static CourseLineBean getLineBean(String str) {

        CourseLineBean bean = new CourseLineBean();
        String[] strings = splitDotStr(str);
        try {
            bean.number = Integer.parseInt(strings[0]);
            bean.resistance = Integer.parseInt(strings[1]);
            bean.startime = Integer.parseInt(strings[2]);
            bean.interver = Integer.parseInt(strings[3]);
            bean.rpm = Integer.parseInt(strings[4]);
        } catch (Exception e) {

        } finally {
            return bean;
        }

    }

    public static ArrayList<ResistanceIntervalBean> getResisteanceList(ArrayList<CourseLineBean> values, int sum) {
        ArrayList<ResistanceIntervalBean> resistanceIntervalBeans = new ArrayList<>();
        int currentTime = 0;
        int rpm = 30;
        int resistance = 30;
        for (int i = 0; i < values.size(); i++) {
            CourseLineBean lineBean = values.get(i);
            if (lineBean.startime > currentTime) {
                ResistanceIntervalBean resistanceIntervalBean = new ResistanceIntervalBean();
                resistanceIntervalBean.setmRpm(rpm);
                resistanceIntervalBean.setmResistances(resistance);
                resistanceIntervalBean.setmIntervalStart(currentTime);
                resistanceIntervalBean.setmIntervalEnd(lineBean.startime);
                resistanceIntervalBeans.add(resistanceIntervalBean);
            }
            ResistanceIntervalBean resistanceIntervalBean = new ResistanceIntervalBean();
            resistanceIntervalBean.setmRpm(lineBean.rpm);
            resistanceIntervalBean.setmResistances(lineBean.resistance);
            resistanceIntervalBean.setmIntervalStart(lineBean.startime);
            resistanceIntervalBean.setmIntervalEnd(lineBean.startime + lineBean.interver);
            resistanceIntervalBeans.add(resistanceIntervalBean);
            currentTime = lineBean.startime + lineBean.interver;
        }
        ResistanceIntervalBean resistanceIntervalBean = resistanceIntervalBeans.get(resistanceIntervalBeans.size() - 1);
        if (resistanceIntervalBean.getmIntervalEnd() != sum) {
            ResistanceIntervalBean resistanceIntervalBean1 = new ResistanceIntervalBean();
            resistanceIntervalBean1.setmIntervalStart(resistanceIntervalBean.getmIntervalEnd());
            resistanceIntervalBean1.setmIntervalEnd(sum);
            resistanceIntervalBean1.setmRpm(rpm);
            resistanceIntervalBean1.setmResistances(resistance);
            resistanceIntervalBeans.add(resistanceIntervalBean1);
        }
        return resistanceIntervalBeans;
        //  sumDis = sum;
    }
}
