
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
public class NetDialogObservable extends Observable {

    private static NetDialogObservable obser;

    public static final int SHOW_SCALE_TIPS = 10;
    public static final int DISMISS_SCALE_TIPS = 11;

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private NetDialogObservable() {
        super();
    }

    public static NetDialogObservable getInstance() {
        if (obser == null) {
            synchronized (NetDialogObservable.class) {
                if (obser == null) {
                    obser = new NetDialogObservable();
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
        message.what = NetDialogObservable.SHOW_SCALE_TIPS;
        NetDialogObservable.getInstance().setChanged();
        NetDialogObservable.getInstance().notifyObservers(message);
        Log.e("show", "show------");
    }

    public void hide() {

        Log.e("show", "hide------");
        Message message = Message.obtain();
        message.what = NetDialogObservable.DISMISS_SCALE_TIPS;
        NetDialogObservable.getInstance().setChanged();
        NetDialogObservable.getInstance().notifyObservers(message);
    }


}