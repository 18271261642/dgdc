package com.jkcq.base.net

import android.text.TextUtils
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import java.io.InterruptedIOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 *  Created by BeyondWorlds
 *  on 2020/7/23
 */
abstract class BaseRetrofitHelper {

    var mtoken: String by Preference(Preference.TOKEN, "")

    private val cookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(), SharedPrefsCookiePersistor(
                BaseApp.sApplicaton
            )
        )
    }

    companion object {
        val TIME_OUT = 60L
        val IS_HTTP_DEBUG = true
        val BASE_URL = "https://test.gateway.spinning.fitalent.com.cn/"
    }

    /**
     * 不同的library可能需要传入不同的ApiService
     */
    fun <S> getService(service: Class<S>, retrofit: Retrofit): S {
        return retrofit.create(service)
    }


    protected val okHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            if (IS_HTTP_DEBUG) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }
            builder.retryOnConnectionFailure(false)
//                .cookieJar(cookieJar)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(getHeaderInterceptorWithOutToken()!!)
//                .addInterceptor(CookieInterceptor())
                .addInterceptor(getTryError()!!)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            //             .retryOnConnectionFailure(true) //连接失败重连
            return builder.build()
        }
    var maxRetry = 3

    open fun getTryError(): Interceptor? {
        var retryNum = 0
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                System.out.println("retryNum=$retryNum")
                Log.e("retryNum1", "response.isSuccessful=" + retryNum);
                var response = chain.proceed(request)


                while (response == null || !response.isSuccessful && retryNum < maxRetry) {
                    Log.e("retryNum2", "response.isSuccessful=" + retryNum);
                    val nextInterval: Long = 1000
                    try {
                        Thread.sleep(nextInterval)
                    } catch (e: Exception) {
                        Thread.currentThread().interrupt()
                        throw InterruptedIOException()
                    }
                    retryNum++
                    System.out.println("retryNum=$retryNum")
                    if(response != null){
                        response.close()
                    }
                    response = chain.proceed(request)

                }
                return response

                /*  val original = chain.request()
                  Log.e("mtoken", mtoken)
                  val requestBuilder = original.newBuilder() //添加Token
                      .header("token", mtoken)
                  val request = requestBuilder.build()

                  //获取到response
                  val response = chain.proceed(request)
                  val headers = response.headers
                  if (!TextUtils.isEmpty(headers["token"])) {
                      val token = headers["token"]
                      // Logger.myLog("token == $token")
                      mtoken = token!!
                  }
                  if (!TextUtils.isEmpty(headers["Date"])) {
                      val Date = headers["Date"]
                  }
                  return response*/
            }
        }
    }

    open fun getHeaderInterceptorWithOutToken(): Interceptor? {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                Log.e("mtoken", mtoken)
                val requestBuilder = original.newBuilder() //添加Token
                    .header("token", mtoken)

                requestBuilder.addHeader("Connection", "close")
                val request = requestBuilder.build()

                //获取到response
                val response = chain.proceed(request)
                val headers = response.headers
                if (!TextUtils.isEmpty(headers["token"])) {
                    val token = headers["token"]
                    // Logger.myLog("token == $token")
                    mtoken = token!!
                }
                if (!TextUtils.isEmpty(headers["Date"])) {
                    val Date = headers["Date"]
                }
                return response
            }
        }
    }
}