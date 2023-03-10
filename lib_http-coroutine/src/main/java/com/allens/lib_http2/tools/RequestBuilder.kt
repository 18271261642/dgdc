package com.allens.lib_http2.tools

import android.os.Build
import android.os.Handler
import android.util.Log
import com.allens.lib_http2.core.HttpResult
import com.allens.lib_http2.download.DownLoadManager
import com.allens.lib_http2.impl.ApiService
import com.allens.lib_http2.impl.OnDownLoadListener
import com.allens.lib_http2.impl.OnUpLoadListener
import com.allens.lib_http2.manager.HttpManager
import com.allens.lib_http2.upload.ProgressRequestBody
import com.allens.lib_http2.upload.UpLoadPool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*


class RequestBuilder {

    private val heard = HashMap<String, String>()
    private val map = HashMap<String, Any>()
    private val bodyMap = HashMap<String, ProgressRequestBody>()


    companion object {
        val CHANGE_URL = Build.BRAND + "_" + Build.MODEL + "_" + "CHANGE_URL"
    }

    private var handler: Handler? = null
    fun addHeard(key: String, value: String): RequestBuilder {
        heard[key] = value

        return this
    }

    fun addParameter(key: String, value: Any): RequestBuilder {
        map[key] = value
        return this
    }

    fun changeBaseUrl(url: String): RequestBuilder {
        addHeard(CHANGE_URL, url)
        return this
    }


    fun addFile(key: String, file: File): RequestBuilder {
        handler = Handler()
        val fileBody: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        bodyMap[key] = ProgressRequestBody(null, "", fileBody, handler)
        return this
    }


    suspend fun <T : Any> doGet(
        parameter: String,
        tClass: Class<T>
    ): HttpResult<T> {
        return executeResponse(
            {
                val baseUrl = HttpManager.retrofit.baseUrl().toUrl().toString()
                var getUrl: String = baseUrl + parameter
                if (map.size > 0) {
                    val param: String = UrlTool.prepareParam(map)
                    if (param.trim().isNotEmpty()) {
                        getUrl += "?$param"
                    }
                }
                HttpManager.getService(ApiService::class.java)
                    .doGet(heard, getUrl)
                    .body()
                    ?.string()
            }, tClass
        )
    }

    suspend fun <T : Any> doPost(parameter: String, tClass: Class<T>): HttpResult<T> {
        return executeResponse({
            Log.i("allens", "post ???????????? ?????? ${Thread.currentThread().name}")
            HttpManager.getService(ApiService::class.java)
                .doPost(parameter, heard, map).body()
                ?.string()
        }, tClass)
    }


    suspend fun <T : Any> doBody(parameter: String, tClass: Class<T>): HttpResult<T> {
        return executeResponse({
            val toJson = HttpManager.gson.toJson(map)
            val requestBody =
                toJson.toRequestBody("application/json".toMediaTypeOrNull())
            HttpManager.getService(ApiService::class.java)
                .doBody(parameter, heard, requestBody)
                .body()
                ?.string()
        }, tClass)
    }


    suspend fun <T : Any> doDelete(parameter: String, tClass: Class<T>): HttpResult<T> {
        return executeResponse({
            HttpManager.getService(ApiService::class.java)
                .doDelete(parameter, heard, map).body()
                ?.string()
        }, tClass)
    }

    suspend fun <T : Any> doPut(parameter: String, tClass: Class<T>): HttpResult<T> {
        return executeResponse({
            HttpManager.getService(ApiService::class.java)
                .doPut(parameter, heard, map).body()
                ?.string()
        }, tClass)
    }


    private suspend fun <T : Any> executeResponse(
        call: suspend () -> String?,
        tClass: Class<T>
    ): HttpResult<T> {
        return try {
            withContext(Dispatchers.IO) {
                HttpResult.Success(HttpManager.gson.fromJson(call(), tClass))
            }
        } catch (e: Throwable) {
            HttpResult.Error(e)
        }
    }


    //??????
    suspend fun doDownLoad(
        tag: String,
        url: String,
        savePath: String,
        saveName: String,
        loadListener: OnDownLoadListener
    ) {
        DownLoadManager.downLoad(
            tag, url, savePath, saveName, loadListener = loadListener
        )
    }


    //?????? cancel
    fun doDownLoadCancel(key: String) {
        DownLoadManager.cancel(key)
    }

    //????????????
    fun doDownLoadPause(key: String) {
        DownLoadManager.pause(key)
    }

    //??????????????????
    fun doDownLoadPauseAll() {
        DownLoadManager.doDownLoadPauseAll()
    }

    //??????????????????
    fun doDownLoadCancelAll() {
        DownLoadManager.doDownLoadCancelAll()
    }


    //??????
    private suspend fun <T : Any> doUpload(url: String, tClass: Class<T>): HttpResult<T> {
        return executeResponse({
            HttpManager.getServiceFromDownLoadOrUpload(ApiService::class.java)
                .upFileList(url, heard, map, bodyMap).body()
                ?.string()
        }, tClass)
    }


    suspend fun <T : Any> doUpload(
        tag: String,
        url: String,
        tClass: Class<T>,
        listener: OnUpLoadListener<T>
    ) {
        withContext(Dispatchers.IO) {
            for ((key, value) in bodyMap) {
                bodyMap[key] = ProgressRequestBody(listener, tag, value.getRequestBody(), handler)
            }
            UpLoadPool.add(tag, listener, this)
            withContext(Dispatchers.Main) {
                listener.opUploadPrepare(tag)
            }
             doUpload(url, tClass)
                .result(
                    {
                        listener.onUpLoadSuccess(tag, it)
                        UpLoadPool.remove(tag)
                    }, {
                        listener.onUpLoadFailed(tag, throwable = it)
                        UpLoadPool.remove(tag)
                    })
        }
    }

    fun doUpLoadCancel(tag: String) {
        UpLoadPool.getListener(tag)?.onUploadCancel(tag)
        UpLoadPool.remove(tag)
    }

}