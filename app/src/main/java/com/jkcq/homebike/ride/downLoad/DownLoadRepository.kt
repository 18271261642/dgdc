package com.jkcq.homebike.ride.downLoad

import com.jkcq.base.base.BaseRepository
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.homebike.net.MainRetrofitHelper
import okhttp3.ResponseBody
import retrofit2.Call

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class DownLoadRepository : BaseRepository() {


    suspend fun downLoad(url: String): Call<ResponseBody> {
        return MainRetrofitHelper.mService.download(url)
    }
}