package com.example.websocket.observable;

import java.util.Observable;


public class TimeOutObservable extends Observable {
    private static TimeOutObservable obser;


    private TimeOutObservable() {
        super();
    }

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    public static TimeOutObservable getInstance() {
        if (obser == null) {
            synchronized (TimeOutObservable.class) {
                if (obser == null) {
                    obser = new TimeOutObservable();
                }
            }
        }
        return obser;
    }


    public void sendTimeOut() {
        TimeOutObservable.getInstance().setChanged();
        TimeOutObservable.getInstance().notifyObservers(0);
    }

    public void sendConnTime() {
        TimeOutObservable.getInstance().setChanged();
        TimeOutObservable.getInstance().notifyObservers(1);
    }


}
