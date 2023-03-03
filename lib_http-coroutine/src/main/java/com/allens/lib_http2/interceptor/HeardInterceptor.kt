package com.allens.lib_http2.interceptor

import android.text.TextUtils
import android.util.Log
import com.allens.lib_http2.RxHttp
import com.allens.lib_http2.config.HttpConfig
import com.allens.lib_http2.tools.RxHttpLogTool
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

//请求头
object HeardInterceptor {
    fun register(map: Map<String, String>): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val builder: Request.Builder = request.newBuilder()
                for ((key, value) in map.entries) {
                    RxHttpLogTool.i(RxHttp.TAG, "http----> add heard [key]:$key [value]:$value ")
                    builder.addHeader(key, value)
                }
                //chain.proceed(builder.build())
                val response = chain.proceed(request)
                return response

            }
        }


    }
}

