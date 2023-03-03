package com.jkcq.homebike.ride.mvvm.viewmodel

import com.jkcq.base.base.BaseViewModel
import com.jkcq.homebike.ride.mvvm.repository.SceneRepository

/**
 *  Created by BeyondWorlds
 *  on 2020/7/27
 */
class RideViewModel : BaseViewModel() {
    val mRideRepository by lazy { SceneRepository() }
}