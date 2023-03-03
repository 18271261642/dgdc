
package com.example.websocket.observable;

import android.util.Log;

import java.util.Observable;

/**
 * ClassName:NetProgressBar <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年4月19日 上午11:31:46 <br/>
 *
 * @author Administrator
 */
public class RealDataObservable extends Observable {

    private static RealDataObservable obser;


    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private RealDataObservable() {
        super();
    }

    public static RealDataObservable getInstance() {
        if (obser == null) {
            synchronized (RealDataObservable.class) {
                if (obser == null) {
                    obser = new RealDataObservable();
                }
            }
        }
        return obser;
    }


    public void setRealData(int speed, int power) {



    }

    public void sendHrValue(Integer hrValue) {
        Log.e("sendHrValue", hrValue + "");
        RealDataObservable.getInstance().notifyObservers(hrValue);
    }


}