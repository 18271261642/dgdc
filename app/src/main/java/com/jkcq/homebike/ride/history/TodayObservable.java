
package com.jkcq.homebike.ride.history;

import java.util.Observable;

/**
 * ClassName:NetProgressBar <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年4月19日 上午11:31:46 <br/>
 *
 * @author Administrator
 */
public class TodayObservable extends Observable {

    private static TodayObservable obser;

    public static final int SHOW_PROGRESS_BAR = 0;
    public static final int DISMISS_PORGRESS_BAR = 1;

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private TodayObservable() {
        super();
    }

    public static TodayObservable getInstance() {
        if (obser == null) {
            synchronized (TodayObservable.class) {
                if (obser == null) {
                    obser = new TodayObservable();
                }
            }
        }
        return obser;
    }


    public void cheackType(Integer integer) {
        TodayObservable.getInstance().setChanged();
        TodayObservable.getInstance().notifyObservers(integer);
    }

}