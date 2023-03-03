package com.jkcq.homebike.db;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


/**
 * GSON 工具类
 */
public class JsonUtils {

    private volatile static JsonUtils instance;

    public synchronized static JsonUtils getInstance() {
        if (instance == null) {
            synchronized (JsonUtils.class) {
                if (instance == null) {
                    synchronized (JsonUtils.class) {
                        instance = new JsonUtils();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 对象解析
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */

    public <T> T parseObject(String json, Type clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
//            DebugLog.e("json数据有误：" + e.getMessage());
            return null;
        }
    }

    public <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
//            DebugLog.e("json数据有误：" + e.getMessage());
            return null;
        }
    }

    public String toJSON(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getValueByKey(JSONObject obj, String key) {
        String value = "";
        if (null != obj && !TextUtils.isEmpty(key)) {
            try {
                value = obj.has(key) ? obj.getString(key) : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static String getValueByKey(JSONObject obj, String key, String def) {
        String value = def;
        if (null != obj && !TextUtils.isEmpty(key)) {
            try {
                value = obj.has(key) ? obj.getString(key) : def;
                value = TextUtils.isEmpty(key) ? def : value;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
