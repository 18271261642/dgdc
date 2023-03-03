package com.jkcq.homebike.ride.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jkcq.homebike.bike.bean.BikeBean;

public class BikeDataCache {

    public static void saveBike(Context context, String key, BikeBean bean) {
        try {
            if (!TextUtils.isEmpty(key)) {
                ACache aCache = ACache.get(context);
                Gson mGson = new Gson();
                Log.e("mGson.toJson(list)", mGson.toJson(bean));
                aCache.put(key, mGson.toJson(bean));
            }
        } catch (Exception e) {

        }
    }

    public static void clearBikeBean(Context context) {
        ACache aCache = ACache.get(context);
        aCache.clear();
    }

    public static void clearBikeBean(Context context, String key) {
        ACache aCache = ACache.get(context);
        aCache.remove(key);
    }

    public static BikeBean getBikeBean(Context context, String key) {
        BikeBean bean = null;
        try {
            ACache aCache = ACache.get(context);
            String strValue = aCache.getAsString(key);
            if (!TextUtils.isEmpty(strValue)) {
                bean = new Gson().fromJson(strValue, BikeBean.class);
            }
        } catch (Exception e) {

        } finally {
            return bean;
        }

    }
}
