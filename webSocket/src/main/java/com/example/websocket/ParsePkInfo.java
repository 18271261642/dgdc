package com.example.websocket;

import android.text.TextUtils;

import com.example.websocket.bean.JoinUser;
import com.example.websocket.bean.LeaveBean;
import com.example.websocket.bean.MqttMessageResponse;
import com.example.websocket.bean.PkdataList;
import com.example.websocket.bean.ResponeBean;
import com.example.websocket.bean.SendGiftData;
import com.example.websocket.bean.SendRealData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ParsePkInfo {


    public List<PkdataList> parResultList(String message) {
        List<PkdataList> list = new Gson().fromJson(message, new TypeToken<List<PkdataList>>() {

        }.getType());
        return list;
    }


    public JoinUser parJoinUser(String message) {
        if (!TextUtils.isEmpty(message)) {
            JoinUser bean = new Gson().fromJson(message, JoinUser.class);
            return bean;
        } else {
            return null;
        }
    }

    public MqttMessageResponse parList(String message) {
        if (!TextUtils.isEmpty(message)) {
            MqttMessageResponse bean = new Gson().fromJson(message, MqttMessageResponse.class);
            return bean;
        } else {
            return null;
        }
    }

    public LeaveBean parLeave(String message) {
        if (!TextUtils.isEmpty(message)) {
            LeaveBean bean = new Gson().fromJson(message, LeaveBean.class);
            return bean;
        } else {
            return null;
        }
    }

    public SendGiftData parGift(String message) {
        if (!TextUtils.isEmpty(message)) {
            SendGiftData bean = new Gson().fromJson(message, SendGiftData.class);
            return bean;
        } else {
            return null;
        }
    }


    public String sendRealDataBeanToString(SendRealData data) {
        return new Gson().toJson(data);
    }

    public String sendGiftbeanToStr(SendGiftData mqttSendData) {
        return new Gson().toJson(mqttSendData);
    }

    public String sendResponse(ResponeBean bean) {
        return new Gson().toJson(bean);
    }
}
