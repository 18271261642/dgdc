package com.jkcq.homebike.ble.bike

import com.jkcq.base.base.BaseRepository
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.homebike.ble.bean.RankingBean
import com.jkcq.homebike.ble.bean.ResultRankingBean
import com.jkcq.homebike.ble.bike.reponsebean.DailybriefBean
import com.jkcq.homebike.ble.bike.reponsebean.DetailUrlBean
import com.jkcq.homebike.db.DailySummaries
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.net.MainRetrofitHelper
import com.jkcq.homebike.ota.DeviceUpgradeBean
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.sceneriding.bean.PraiseBean

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class BikeDataRepository : BaseRepository() {

    suspend fun updateBikeData(map: Map<String, String>): BaseResponse<String> {
        return MainRetrofitHelper.mService.cyclingrecords(map)
    }

    suspend fun getSummary(
        day: String,
        deviceType: String,
        summaryType: String,
        userId: String
    ): BaseResponse<Summary> {
        return MainRetrofitHelper.mService.summary(
            day,
            deviceType,
            summaryType,
            userId,
            "" + DeviceType.DEVICE_BIKE.value
        )
    }

    suspend fun getExerciseDaysInMonth(
        deviceType: String,
        month: String,
        userId: String
    ): BaseResponse<List<String>> {
        return MainRetrofitHelper.mService.exercise_days_in_month(
            deviceType,
            month,
            userId,
            "" + DeviceType.DEVICE_BIKE.value
        )
    }

    suspend fun getDailySummaries(
        day: String,
        deviceType: String,
        summaryType: String,
        userId: String
    ): BaseResponse<List<DailySummaries>> {
        return MainRetrofitHelper.mService.daily_summaries(
            day,
            deviceType,
            summaryType,
            userId,
            "" + DeviceType.DEVICE_BIKE.value
        )
    }

    suspend fun getDailyBrief(
        day: String,
        deviceType: String,
        userId: String
    ): BaseResponse<List<DailybriefBean>> {
        return MainRetrofitHelper.mService.daily_brief(
            day,
            deviceType,
            userId,
            "" + DeviceType.DEVICE_BIKE.value
        )
    }

    suspend fun getDetailUrl(
    ): BaseResponse<DetailUrlBean> {
        return MainRetrofitHelper.mService.getUrl()
    }

    suspend fun getRankings(
        relevanceId: String,
        praiseType: String
    ): BaseResponse<ResultRankingBean> {
        return MainRetrofitHelper.mService.getrankings(relevanceId, praiseType)
    }

    suspend fun praiseToOther(
        map: Map<String, String>
    ): BaseResponse<PraiseBean> {

        return MainRetrofitHelper.mService.praisesToOther(map)
    }

    suspend fun unPraiseToOther(
        praiseId: String
    ): BaseResponse<PraiseBean> {
        return MainRetrofitHelper.mService.unPraiseToOTher(praiseId)
    }

    suspend fun deviceVersion(
        praiseId: String
    ): BaseResponse<DeviceUpgradeBean> {
        return MainRetrofitHelper.mService.getDeviceVersion(praiseId)
    }


}