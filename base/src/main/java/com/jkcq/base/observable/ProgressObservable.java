
package com.jkcq.base.observable;

import android.util.Log;

import java.util.Observable;

/**
 * ClassName:NetProgressBar <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年4月19日 上午11:31:46 <br/>
 *
 * @author Administrator
 */
public class ProgressObservable extends Observable {

    private static ProgressObservable obser;

    public static final int SHOW_SCALE_TIPS = 10;
    public static final int DISMISS_SCALE_TIPS = 11;

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private ProgressObservable() {
        super();
    }

    public static ProgressObservable getInstance() {
        if (obser == null) {
            synchronized (ProgressObservable.class) {
                if (obser == null) {
                    obser = new ProgressObservable();
                }
            }
        }
        return obser;
    }


    public void connBikeState(int state) {
        ProgressObservable.getInstance().setChanged();
        ProgressObservable.getInstance().notifyObservers(state);
        Log.e("show", "show------" + state);
    }

    public void updateProgress(float progress) {
        ProgressObservable.getInstance().setChanged();
        ProgressObservable.getInstance().notifyObservers(progress);
    }


}