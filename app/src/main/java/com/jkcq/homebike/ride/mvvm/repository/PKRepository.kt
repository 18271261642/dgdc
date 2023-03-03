package com.jkcq.homebike.ride.mvvm.repository

import com.jkcq.base.base.BaseRepository
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.homebike.net.MainRetrofitHelper
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.pk.bean.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/27
 */
class PKRepository : BaseRepository() {
    suspend fun getListByDeviceTypeId(
        deviceType: String,
        applyType: String
    ): BaseResponse<List<SceneBean>> {
        return MainRetrofitHelper.mService.getListByDeviceTypeId(deviceType, applyType)
    }

    suspend fun createPkRoom(
        maps: Map<String, String>
    ): BaseResponse<PKRoomBean> {
        return MainRetrofitHelper.mService.createPks(maps)
    }

    suspend fun getPkList(
    ): BaseResponse<List<PKListBean>> {
        return MainRetrofitHelper.mService.getPkList()
    }

    suspend fun addPk(
        maps: Map<String, String>
    ): BaseResponse<PKRoomBean> {
        return MainRetrofitHelper.mService.joinPk(maps)
    }

    suspend fun startPk(maps: Map<String, String>): BaseResponse<String> {
        return MainRetrofitHelper.mService.startPk(maps)
    }

    suspend fun findJoinPk(
        pkId: String,
        userId: String
    ): BaseResponse<PKStateBean> {
        return MainRetrofitHelper.mService.findJoinPk(pkId, userId)
    }

    suspend fun reJoinPk(
        maps: Map<String, String>
    ): BaseResponse<PKRoomBean> {
        return MainRetrofitHelper.mService.reJoinPk(maps)
    }

    suspend fun leavePk(
        maps: Map<String, String>, isCreate: Boolean
    ): BaseResponse<String> {
        if (isCreate) {
            return MainRetrofitHelper.mService.destroyPk(maps)
        } else {
            return MainRetrofitHelper.mService.leavePk(maps)
        }

    }

    suspend fun updatePKData(map: Map<String, String>): BaseResponse<String> {
        return MainRetrofitHelper.mService.cyclingPkRecords(map)
    }

    suspend fun getPKResult(pkId: String): BaseResponse<List<PKResultBean>> {
        return MainRetrofitHelper.mService.getCyclingPkRecords(pkId)
    }

    suspend fun getPkParticipantNum(): BaseResponse<ResultPKNumberBean> {
        return MainRetrofitHelper.mService.getPkParticipantNum()
    }

    suspend fun getBestRecodes(scenarioId: String, userId: String): BaseResponse<BestRecodeBean> {
        return MainRetrofitHelper.mService.getBestRecodes(scenarioId, userId)
    }

    suspend fun getPropsAll(): BaseResponse<List<PropesBean>> {
        return MainRetrofitHelper.mService.getPropsAll()
    }

    suspend fun getJoinPkState(pkId: String, userId: String): BaseResponse<PKStateBean> {
        return MainRetrofitHelper.mService.findJoinPk(pkId, userId)
    }

    suspend fun getPKResultUrl(pkId: String, userId: String): BaseResponse<String> {


        return MainRetrofitHelper.mService.getH5Urls("rank")
    }
}