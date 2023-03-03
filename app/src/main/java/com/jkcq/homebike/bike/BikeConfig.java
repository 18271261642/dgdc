package com.jkcq.homebike.bike;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.TextView;

import com.jkcq.base.net.bean.UserInfo;
import com.jkcq.homebike.R;
import com.jkcq.homebike.bike.bean.BikeBean;
import com.jkcq.homebike.ble.bike.reponsebean.DetailUrlBean;
import com.jkcq.homebike.db.Summary;

import java.util.concurrent.ConcurrentHashMap;

public class BikeConfig {


    //匹配度
    public static int CyclingMatchLeverNone = 0;
    public static int CyclingMatchLeverPerfect = 1;
    public static int CyclingMatchLeverGreat = 2;
    public static int CyclingMatchLeverGood = 3;
    public static int CyclingMatchLeverSlowDown = 4;
    public static int CyclingMatchLeverComeOn = 5;


    public static Summary summary = null;

    public static DetailUrlBean detailUrlBean = new DetailUrlBean();

    public static String METRIC_UNITS = "0";
    public static String ENGLISH_UNITS = "1";
    public static String DEVICE = "device";


    public static String userCurrentUtil = METRIC_UNITS;

    public static BluetoothDevice device;
    public static boolean isBikeScanPage = false;
    public static boolean isOpenBle = false;
    public static boolean isCanRecon = false;

    public static int BIKE_CONN_SUCCESS = 1;
    public static int BIKE_CONN_DISCONN = 0;
    public static int BIKE_CONN_IS_SCAN = 4;
    public static int BIKE_CONN_IS_SCAN_TIMEOUT = 5;
    public static int BIKE_CONN_CONNECTING = 2;
    public static int BIKE_CONN_DISCONNECTING = 3;

    public static int BIKE_TYPE = 83003;
    public static String BIKE_SPORT_DATE_ALL = "ALL";
    public static String BIKE_SPORT_DATE_WEEK = "WEEK";
    public static String BIKE_SPORT_DATE_DAY = "DAY";
    public static String BIKE_SPORT_DATE_MONTH = "MONTH";
    //骑行类型，0：自由骑行，1：线路骑行，2：PK骑行
    public static int BIKE_FREE = 0;
    public static int BIKE_LINE = 1;
    public static int BIKE_PK = 2;
    public static int BIKE_COURSE = 3;


    public static int BikeConnState = 0;
    public static int HRConnState = 0;

    public static int speed = 0;
    public static int power = 0;
    public static boolean isPause = false;
    public static boolean IS_SHOW_HIDE_NAVIGATION_BAR = false;
    //adb shell am broadcast -a android.intent.action.SHOW_NAVIGATION_BAR
    public static final String HIDE_NAVIGATION_BAR = "android.intent.action.HIDE_NAVIGATION_BAR";
    public static final String SHOW_NAVIGATION_BAR = "android.intent.action.SHOW_NAVIGATION_BAR";
    public static final String HIDE_STATUS_BAR = "android.intent.action.HIDE_STATUS_BAR";
    public static final String SHOW_STATUS_BAR = "android.intent.action.SHOW_STATUS_BAR";
    public static boolean isLoginOut = false;
    public static BikeBean sBikeBean = null;
    public static Boolean isUpgradeSuccess = false;
    public static UserInfo currentUser = new UserInfo();
    public static String currentVersion = "";
    public static String currentUserId = "";
    public static String pPUpdateId = "";


    public static boolean isConn() {
        if (BikeConnState == BIKE_CONN_SUCCESS) {
            return true;
        } else {
            return true;
        }
    }

    // public static BikeBean goalBikeBean;

    public static boolean IS_BIKE = true;
    // public static boolean is_OPEN_SCREEN=true;

    public static int CURRENT_LEVEL = 1;

    public static int LEVEL_1 = 1;//休闲
    public static int LEVEL_2 = 2;//燃脂
    public static int LEVEL_3 = 3;//挑战

    public static void setCurrentLevel(int currentLevel) {
        CURRENT_LEVEL = currentLevel;
    }

    public static String clubId = "10000";
    public static String deviceToken = "";
    public static String CurrentPkSerId = "";


    public static final int GLOBAL_BIKEBEAN = 0;
    public static final int PK_BIKEBEAN = 1;
    public static final int TARGET_BIKEBEAN = 2;
    public static final int ROAD_BIKEBEAN = 3;

    public static int heart = 0;

    public static int[] heartAvg = new int[]{0, 0};


    public static void clearBikeConfig() {
        //clubDetailInfo = null;
        currentUser = null;
        //deviceTypeBean = null;
        // clubId="";
        // deviceToke="";

    }

    public static double mTotalCarrie = 0;

    public static double mTargetCarrie = 0;

    public static void addCarrie(double cc) {
        mTotalCarrie = mTotalCarrie + cc;
    }

    public static void addTargetCar(double ct) {
        mTargetCarrie = mTargetCarrie + ct;
    }


    public static ConcurrentHashMap<String, BikeBean> oneBikeBean = new ConcurrentHashMap<>();


    public static void clearAll() {
        // BikeConfig.goalBikeBean = null;

        oneBikeBean.clear();
    }

    public static void disUnit(TextView text, Context context) {
        if (METRIC_UNITS.equals(BikeConfig.userCurrentUtil)) {
            text.setText(context.getResources().getString(R.string.sport_dis_unitl_km));
        } else {
            text.setText(context.getResources().getString(R.string.sport_dis_unitl_mile));
        }
    }

    public static String disUnit(Context context) {
        if (METRIC_UNITS.equals(BikeConfig.userCurrentUtil)) {
            return context.getResources().getString(R.string.sport_dis_unitl_km);
        } else {
            return context.getResources().getString(R.string.sport_dis_unitl_mile);
        }
    }

    public static void disUnitBike(TextView text, Context context) {
        if (METRIC_UNITS.equals(BikeConfig.userCurrentUtil)) {
            text.setText(context.getResources().getString(R.string.device_unitl_milage_km));
        } else {
            text.setText(context.getResources().getString(R.string.device_unitl_milage_mile));
        }
    }
}
