package com.jkcq.homebike.ride.freeride;

import com.jkcq.homebike.bike.BikeConfig;

public class ReconDeviceUtil implements CountReconTimer.OnCountTimerListener {
    private static ReconDeviceUtil managerInstance;
    static CountReconTimer timer;

    public static synchronized ReconDeviceUtil getInstance() {
        if (managerInstance == null) {
            managerInstance = new ReconDeviceUtil();
        }
        return managerInstance;
    }

    @Override
    public void onCountTimerChanged(long millisecond) {
        if (lister != null) {
            lister.onCountTimeChange();
        }

    }

    public void init(OnCountTimeChangeLister lister) {
        this.lister = lister;
        if (timer == null) {
            timer = new CountReconTimer(10000, this);
        }
    }

    public OnCountTimeChangeLister lister;


    public interface OnCountTimeChangeLister {


        void onCountTimeChange();
    }

    public void startReconDevice() {


        if (timer != null && BikeConfig.isOpenBle && BikeConfig.isCanRecon) {
            timer.start();
        }

    }

    public void endReconDevice() {
        if (timer != null) {
            timer.stop();
        }
    }

}
