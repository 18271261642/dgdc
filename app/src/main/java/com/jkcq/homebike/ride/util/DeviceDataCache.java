package com.jkcq.homebike.ride.util;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

public class DeviceDataCache {

    public static void saveDevcie(Context context, String key, BluetoothDevice bean) {
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

    public static void clearDevice(Context context) {
        ACache aCache = ACache.get(context);
        aCache.clear();
    }

    public static BluetoothDevice getDeviceBean(Context context, String key) {
        BluetoothDevice bean = null;
        try {
            ACache aCache = ACache.get(context);
            String strValue = aCache.getAsString(key);
            if (!TextUtils.isEmpty(strValue)) {
                bean = new Gson().fromJson(strValue, BluetoothDevice.class);
            }
        } catch (Exception e) {

        } finally {
            return bean;
        }

    }
}
