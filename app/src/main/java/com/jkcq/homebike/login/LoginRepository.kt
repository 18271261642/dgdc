package com.jkcq.homebike.login

import com.jkcq.base.base.BaseRepository
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.homebike.net.MainRetrofitHelper

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class LoginRepository : BaseRepository() {

    suspend fun loginByMobile(map: Map<String, String>): BaseResponse<UserInfo> {
        return MainRetrofitHelper.mService.loginByMobile(map)
    }

    suspend fun loginByThirdPlatform(map: Map<String, String>): BaseResponse<UserInfo> {
        return MainRetrofitHelper.mService.loginByThirdPlatform(map)
    }

    suspend fun getVerifyCodeByMobile(mobile:String,type:Int): BaseResponse<String> {
        return MainRetrofitHelper.mService.getVerifyCodeByMobile(mobile,type)
    }

    suspend fun getVerifyCodeByEmail(email:String,type:Int,language:String): BaseResponse<String> {
        return MainRetrofitHelper.mService.getVerifyCodeByEmail(email,type,language)
    }
}