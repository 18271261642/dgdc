package com.jkcq.base.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  Created by BeyondWorlds
 *  on 2020/7/23
 */
object  CommonRetrofitHelper:BaseRetrofitHelper() {
    private var retorfit: Retrofit? = null

    val mService by lazy { getService(CommonApiService::class.java, getRetrofit()!!) }

    private fun getRetrofit(): Retrofit? {
        if (retorfit == null) {
            synchronized(CommonRetrofitHelper::class.java) {
                if (retorfit == null) {
                    retorfit = Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
        }
        return retorfit

    }
}