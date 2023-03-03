package com.jkcq.homebike.ride.freeride

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseViewModel

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class FreeRideViewModel : BaseViewModel(), CountTimer.OnCountTimerListener {

    private val countTimer = CountTimer(1000, this)
    private var millisecond: Long = 0

    var mBikeName: String by Preference(Preference.BIKENAME, "")


    val mTimer: MutableLiveData<Long> =
        MutableLiveData<Long>()
    val mDeviceConnStateTips: MutableLiveData<String> =
        MutableLiveData<String>()

    override fun onCountTimerChanged(millisecond: Long) {


        Log.e("onCountTimerChanged", "onCountTimerChanged")

        this.millisecond = millisecond

        mTimer.value = millisecond
    }

    fun reStartFreeRide() {
        countTimer.reStart(this.millisecond)
    }

    fun startFreeRide() {
        countTimer.start()
    }

    fun endFreeRide() {
        countTimer.stop()
    }


}