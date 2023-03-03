package com.jkcq.homebike.net

import com.jkcq.base.net.BaseRetrofitHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
object MainRetrofitHelper : BaseRetrofitHelper() {
    private var retorfit: Retrofit? = null

    val mService by lazy {
        getService(
            MainApiService::class.java,
            getRetrofit()!!
        )
    }

    private fun getRetrofit(): Retrofit? {
        if (retorfit == null) {
            synchronized(MainRetrofitHelper::class.java) {
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