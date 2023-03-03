package com.jkcq.homebike.ride.mvvm.repository

import com.jkcq.base.base.BaseRepository
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.homebike.net.MainRetrofitHelper
import com.jkcq.homebike.db.SceneBean

/**
 *  Created by BeyondWorlds
 *  on 2020/7/27
 */
class SceneRepository : BaseRepository() {
    suspend fun getCourseDetail(deviceType: String): BaseResponse<SceneBean> {
        return MainRetrofitHelper.mService.getCourseDetial(deviceType)
    }

    suspend fun getListByDeviceTypeId(
        deviceType: String,
        applyType: String
    ): BaseResponse<List<SceneBean>> {
        return MainRetrofitHelper.mService.getListByDeviceTypeId(deviceType, applyType)
    }

    suspend fun getBackgrounds(): BaseResponse<List<SceneBean>> {
        return MainRetrofitHelper.mService.getBackgrounds()
    }

    suspend fun getListByDeviceTypeId(
        deviceType: String
    ): BaseResponse<List<SceneBean>> {
        return MainRetrofitHelper.mService.getAllListByDeviceTypeId(deviceType)
    }

    suspend fun getCourseListByDeviceType(
        deviceType: String,
        language: String
    ): BaseResponse<List<SceneBean>> {
        return MainRetrofitHelper.mService.getCourseList(deviceType, language)
    }
}