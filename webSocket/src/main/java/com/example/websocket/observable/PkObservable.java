package com.example.websocket.observable;

import com.example.websocket.bean.JoinUser;
import com.example.websocket.bean.LeaveBean;
import com.example.websocket.bean.MqttMessageResponse;
import com.example.websocket.bean.SendGiftData;

import java.util.Observable;


public class PkObservable extends Observable {
    private static PkObservable obser;


    private PkObservable() {
        super();
    }

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    public static PkObservable getInstance() {
        if (obser == null) {
            synchronized (PkObservable.class) {
                if (obser == null) {
                    obser = new PkObservable();
                }
            }
        }
        return obser;
    }


    public void sendRank(MqttMessageResponse list) {
        PkObservable.getInstance().setChanged();
        PkObservable.getInstance().notifyObservers(list);
    }

    public void sendPkState(int type) {
        PkObservable.getInstance().setChanged();
        PkObservable.getInstance().notifyObservers(type);
    }

    public void sendJoinPk(JoinUser user) {
        PkObservable.getInstance().setChanged();
        PkObservable.getInstance().notifyObservers(user);
    }

    public void sendLeavePk(LeaveBean user) {
        PkObservable.getInstance().setChanged();
        PkObservable.getInstance().notifyObservers(user);
    }
    public void sendGiftBean(SendGiftData user) {
        PkObservable.getInstance().setChanged();
        PkObservable.getInstance().notifyObservers(user);
    }


}
