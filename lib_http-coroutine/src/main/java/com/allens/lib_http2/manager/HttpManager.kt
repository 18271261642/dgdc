package com.allens.lib_http2.manager

import android.content.Context
import android.os.Environment
import com.allens.lib_http2.RxHttp
import com.allens.lib_http2.config.HttpConfig
import com.allens.lib_http2.config.HttpNetWorkType
import com.allens.lib_http2.interceptor.*
import com.allens.lib_http2.interceptor.CacheInterceptor
import com.allens.lib_http2.interceptor.CacheNetworkInterceptor
import com.allens.lib_http2.tools.RxHttpLogTool
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit


/**
 *
 * @Description:
 * @Author:         Allens
 * @CreateDate:     2019-11-22 11:52
 * @Version:        1.0
 */
object HttpManager {


    lateinit var context: Context

    fun create(): HttpManager {
        createBuilder()
        return this
    }

    fun build(context: Context): HttpManager {
        HttpManager.context = context
        buildOkHttp(context)
        retrofit = createRetrofit(HttpConfig.baseUrl)
        retrofitDownLoad = createRetrofitByDownLoad()
        return this
    }


    private lateinit var okHttpBuilder: OkHttpClient.Builder
    private lateinit var retrofitBuilder: Retrofit.Builder
    lateinit var retrofit: Retrofit
    private lateinit var retrofitDownLoad: Retrofit
    val gson: Gson = Gson()

    private fun createBuilder() {
        okHttpBuilder = OkHttpClient.Builder()
        retrofitBuilder = Retrofit.Builder()
    }

    private fun buildOkHttp(context: Context) {

        val cookieJar =
            PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(context.applicationContext)
            )
        //第三方库 管理 cookie
        okHttpBuilder.cookieJar(cookieJar)
        okHttpBuilder.connectTimeout(HttpConfig.connectTime, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(HttpConfig.readTime, TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(HttpConfig.writeTime, TimeUnit.SECONDS)
        okHttpBuilder.retryOnConnectionFailure(HttpConfig.retryOnConnectionFailure)



        //动态替换BaseURL
        okHttpBuilder.addInterceptor(HostSelectionInterceptor())

        if (HttpConfig.isLog)
            okHttpBuilder.addInterceptor(LogInterceptor.register(HttpConfig.level))
        val map = HttpConfig.heardMap
        if (!map.isNullOrEmpty()) {
           okHttpBuilder.addInterceptor(HeardInterceptor.register(map))
        }

        //cookie 拦截器
        val cookiesListener = HttpConfig.cookieListener
        val onCookieInterceptor = HttpConfig.onCookieInterceptor
        if (cookiesListener != null && onCookieInterceptor != null)
            okHttpBuilder.addInterceptor(
                ReceivedCookieInterceptor.register(
                    cookiesListener,
                    onCookieInterceptor
                )
            )

        //cache 缓存
        val cacheSize = HttpConfig.cacheSize // 10 MiB
        val cache = Cache(
            File(
                if (HttpConfig.cachePath.isEmpty()) {
                    HttpConfig.cachePath = getBasePath(context) + "/cacheHttp"
                    HttpConfig.cachePath
                } else {
                    HttpConfig.cachePath
                }
            ), cacheSize.toLong()
        )
        //设置缓存
        if (HttpConfig.cacheNetWorkType != HttpNetWorkType.NONE) {
            okHttpBuilder
                .addInterceptor(CacheInterceptor(context))
                .addNetworkInterceptor(CacheNetworkInterceptor())
                .cache(cache)
        }

    }

    private fun createRetrofit(baseUrl:String): Retrofit {
        val client = retrofitBuilder
            .client(okHttpBuilder.build())

        val set = HttpConfig.onBuildClientListener?.addBuildClient()
        RxHttpLogTool.i(RxHttp.TAG, "factory size  ${set?.size}")
        set?.forEach {
            try {
                when (it) {
                    is Converter.Factory -> {
                        client.addConverterFactory(it)
                        RxHttpLogTool.i(RxHttp.TAG, "addConverterFactory $it")
                    }
                    is CallAdapter.Factory -> {
                        client.addCallAdapterFactory(it)
                        RxHttpLogTool.i(RxHttp.TAG, "addCallAdapterFactory $it")
                    }
                    else -> {
                        RxHttpLogTool.i(
                            RxHttp.TAG,
                            "factory is not Converter.Factory or CallAdapter.Factory, please check "
                        )
                    }
                }
            } catch (throwable: Throwable) {
                RxHttpLogTool.i(RxHttp.TAG, "add factory failed ${throwable.message}")
            }
        }
        return client
            .baseUrl(baseUrl)
            .build()
    }

    /***
     * 下载和网络请求使用不同的retrofit
     * 防止@Steaming 不起作用
     * @return Retrofit
     */
    private fun createRetrofitByDownLoad(): Retrofit {
        okHttpBuilder.interceptors().clear()
        return createRetrofit(HttpConfig.baseUrl)
    }


    fun <T> getService(tClass: Class<T>): T {
        return retrofit.create(tClass)
    }

    fun <T> getServiceFromDownLoadOrUpload(tClass: Class<T>): T {
        return retrofitDownLoad.create(tClass)
    }


    //获取更路径
    private fun getBasePath(context: Context): String {
        var p: String = Environment.getExternalStorageDirectory().path
        val f: File? = context.getExternalFilesDir(null)
        if (null != f) {
            p = f.absolutePath
        }
        return p
    }

}