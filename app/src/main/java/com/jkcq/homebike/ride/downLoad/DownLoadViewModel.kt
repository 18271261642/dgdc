package com.jkcq.homebike.ride.downLoad

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jkcq.base.base.BaseViewModel
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.homebike.util.FileUtil
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class DownLoadViewModel : BaseViewModel() {

    var mUserInfoLiveData = MutableLiveData<UserInfo>()
    val mLoginRepository: DownLoadRepository by lazy { DownLoadRepository() }



    fun downloadFile(url: String, name: String) {

        var mFile = File(FileUtil.getVideoDir() + File.separator + name)




        viewModelScope.launch() {
            var mCall = mLoginRepository.downLoad(url)
            mCall.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    @NonNull call: Call<ResponseBody?>,
                    @NonNull response: Response<ResponseBody?>
                ) {
                    //下载文件放在子线程
                    var mThread = object : Thread() {
                        override fun run() {
                            super.run()
                            //保存到本地
                            writeFile2Disk(response, mFile)
                        }
                    }
                    mThread.start()
                }

                override fun onFailure(
                    call: Call<ResponseBody?>,
                    t: Throwable
                ) {
                    // downloadListener.onFailure("网络错误！")
                }
            })
        }
        //设置文件名，如果不设置，会自动获取
        //setFileName = {"xxxxx.后缀"}

        //为了兼容android10,如果需要把文件下载到共享文件夹，可以调用serUri
        /* setUri = {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 val values = ContentValues().apply {
                     put(MediaStore.MediaColumns.DISPLAY_NAME,"xxxx.后缀") //文件名
                     put(MediaStore.MediaColumns.MIME_TYPE, response.body()?.contentType().toString()) //文件类型
                     put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS) //共享文件夹，固定写法
                 }
                 context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
             } else
                 Uri.fromFile(File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath+File.separator+"xxxxx.后缀"))
         }*/


        /* viewModelScope.launch {
             kotlin.runCatching {
                 withContext(Dispatchers.IO) {  mLoginRepository.downLoad(url) }
             }.onSuccess {
                 kotlin.runCatching {
                     withContext(Dispatchers.Main) {
                         executeResponse(it) {
                             onSuccess(it)
                         }
                     }
                 }.onFailure {
                     error(ExceptionHandle.handleException(it))
                 }

             }.onFailure {
                 error(ExceptionHandle.handleException(it))
             }
         }*/


    }

    private fun writeFile2Disk(
        response: Response<ResponseBody?>,
        file: File
    ) {
        //  downloadListener.onStart()
        var currentLength: Long = 0
        var os: OutputStream? = null
        if (response.body() == null) {
            // downloadListener.onFailure("资源错误！")
            return
        }
        val `is` = response.body()!!.byteStream()
        val totalLength = response.body()!!.contentLength()
        try {
            os = FileOutputStream(file)
            var len: Int
            val buff = ByteArray(1024)
            while (`is`.read(buff).also { len = it } != -1) {
                os.write(buff, 0, len)
                currentLength += len.toLong()
                Log.e("writeFile2Disk", "当前进度: $currentLength")
                //   downloadListener.onProgress((100 * currentLength / totalLength).toInt())
                if ((100 * currentLength / totalLength).toInt() == 100) {
                    //downloadListener.onFinish(mVideoPath)
                }
            }
        } catch (e: FileNotFoundException) {
            // downloadListener.onFailure("未找到文件！")
            e.printStackTrace()
        } catch (e: IOException) {
            // downloadListener.onFailure("IO错误！")
            e.printStackTrace()
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


}