
package com.jkcq.base.observable;

import android.os.Message;
import android.util.Log;

import java.util.Observable;

/**
 * ClassName:NetProgressBar <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年4月19日 上午11:31:46 <br/>
 *
 * @author Administrator
 */
public class LoginOutObservable extends Observable {

    private static LoginOutObservable obser;

    public static final int SHOW_SCALE_TIPS = 10;
    public static final int DISMISS_SCALE_TIPS = 11;

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private LoginOutObservable() {
        super();
    }

    public static LoginOutObservable getInstance() {
        if (obser == null) {
            synchronized (LoginOutObservable.class) {
                if (obser == null) {
                    obser = new LoginOutObservable();
                }
            }
        }
        return obser;
    }

    public void show() {
        show(null);
    }

    /**
     * @param isCancelable 是否允许返回键关闭Dialog true允许
     */
    public void show(boolean isCancelable) {
        show(null, isCancelable);
    }

    public void show(String msg) {
        show(msg, true);
    }

    public void show(String msg, boolean isCancelable) {

        Log.e("show", "show------");
        Message message = Message.obtain();
        message.what = LoginOutObservable.SHOW_SCALE_TIPS;
        LoginOutObservable.getInstance().setChanged();
        LoginOutObservable.getInstance().notifyObservers(message);
        Log.e("show", "show------");
    }


    public void hid() {
        Message message = Message.obtain();
        message.what = LoginOutObservable.DISMISS_SCALE_TIPS;
        LoginOutObservable.getInstance().setChanged();
        LoginOutObservable.getInstance().notifyObservers(message);
        Log.e("show", "show------");
    }


}