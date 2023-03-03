package com.jkcq.homebike.mine.mvvm.repository

import com.jkcq.base.base.BaseRepository
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.homebike.net.MainRetrofitHelper

/**
 *  Created by BeyondWorlds
 *  on 2020/7/31
 */
class UserRepository : BaseRepository() {

    /**
     * 编辑用户信息
     */
    suspend fun editUserInfo(map: Map<String, String>): BaseResponse<String> {
        return MainRetrofitHelper.mService.editUserInfo(map)
    }

    suspend fun getBasicInfo(map: Map<String, String>): BaseResponse<UserInfo> {

        return MainRetrofitHelper.mService.getBasicInfo(map)

    }

    suspend fun getmeasurement(map: Map<String, String>): BaseResponse<String> {
        return MainRetrofitHelper.mService.measurement(map)
    }

    suspend fun getFeedBackUrl(): BaseResponse<String> {
        return MainRetrofitHelper.mService.getFeedBackUrl()
    }

    suspend fun getQuestionUrl(): BaseResponse<String> {
        return MainRetrofitHelper.mService.getQuestionUrl()
    }

    suspend fun editBgUrl(url: String): BaseResponse<String> {
        return MainRetrofitHelper.mService.editBackround(url)
    }


}