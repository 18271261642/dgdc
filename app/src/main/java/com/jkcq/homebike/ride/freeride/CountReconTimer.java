package com.jkcq.homebike.ride.freeride;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 作者:东芝(2018/2/26).
 * 功能:计时器
 */

public class CountReconTimer {

    private Handler mHandler;
    private long delayedTime;
    private long intervalTime;
    private int HANDLER_COUNT_TIMER = 0;
    private long millisecond;

    public CountReconTimer(final long intervalTime, final OnCountTimerListener timerListener) {
        init(-1, intervalTime, timerListener);
    }

    public CountReconTimer(long delayedTime, final long intervalTime, final OnCountTimerListener timerListener) {
        init(delayedTime, intervalTime, timerListener);
    }

    private void init(long delayedTime, final long intervalTime, final OnCountTimerListener timerListener) {
        this.delayedTime = delayedTime;
        this.intervalTime = intervalTime;
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {

                millisecond += intervalTime;
                mHandler.removeMessages(HANDLER_COUNT_TIMER);
                mHandler.sendEmptyMessageDelayed(HANDLER_COUNT_TIMER, CountReconTimer.this.intervalTime);

                if (timerListener != null) {
                    if (CountReconTimer.this.delayedTime != -1) {
                        timerListener.onCountTimerChanged(CountReconTimer.this.delayedTime + millisecond);
                    } else {
                        timerListener.onCountTimerChanged(millisecond);
                    }
                }
            }
        };
    }



    public void reStart(long millisecond) {
        this.millisecond = millisecond;
        mHandler.removeMessages(HANDLER_COUNT_TIMER);
        if (delayedTime == -1) {
            mHandler.sendEmptyMessage(HANDLER_COUNT_TIMER);
        } else {
            mHandler.sendEmptyMessageDelayed(HANDLER_COUNT_TIMER, delayedTime);
        }
    }

    public void start() {
        millisecond = 0;
        mHandler.removeMessages(HANDLER_COUNT_TIMER);
        if (delayedTime == -1) {
            mHandler.sendEmptyMessage(HANDLER_COUNT_TIMER);
        } else {
            mHandler.sendEmptyMessageDelayed(HANDLER_COUNT_TIMER, delayedTime);
        }
    }

    public void stop() {
        mHandler.removeMessages(HANDLER_COUNT_TIMER);
    }

    public interface OnCountTimerListener {
        void onCountTimerChanged(long millisecond);
    }
}
