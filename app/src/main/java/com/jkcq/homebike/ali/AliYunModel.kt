package com.jkcq.homebike.ali

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseViewModel
import com.jkcq.util.ktx.ToastUtil
import java.io.File

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class AliYunModel : BaseViewModel() {

    var mOssBean = MutableLiveData<OssBean>()
    var mUpdatePhotoBean = MutableLiveData<UpdatePhotoBean>()
    var mUpdateSuccess = MutableLiveData<Boolean>()
    var mUpdatePath = MutableLiveData<String>()
    var mDownLoadBean = MutableLiveData<DownLoadBean>()
    var mDownLoadSuccess = MutableLiveData<Boolean>()

    val mLoginRepository: AliyunRepository by lazy { AliyunRepository() }

    fun updateHeadFile(file: File) {
        executeRequest({ mLoginRepository.upgradeHead(mUserId, file) }, {
            mUpdatePhotoBean.value = it
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getAliYunToken() {
        executeRequest({ mLoginRepository.getAliToken() }, {
            mOssBean.value = it
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg)
        })
    }

    private val mHandler = Handler(Looper.getMainLooper())
    var aliyunManager: AliyunManager? = null
    fun upgradePic(
        buckeName: String?,
        keyId: String?,
        secretId: String?,
        token: String?,
        imageName: String?,
        path: String?
    ) {
        aliyunManager =
            AliyunManager(
                buckeName,
                keyId,
                secretId,
                token,
                imageName,
                object : AliyunManager.callback {
                    override fun upLoadSuccess(type: Int, imgPath: String) {
                        Log.e("imgPath=", imgPath)
                        mHandler.post(Runnable {
                            mUpdateSuccess.value = true
                            mUpdatePath.value = imgPath
                        })

                    }

                    override fun upLoadFailed(error: String) {
                        //第二步失败
                        mUpdateSuccess.value = false
                    }

                    override fun upLoadProgress(
                        currentSize: Long,
                        totalSize: Long
                    ) {
                        /*if (upgradeImageView != null) {
                            upgradeImageView.upgradeProgress(currentSize, totalSize)
                        }*/
                    }
                })
        aliyunManager?.upLoadVideoFile(path)
    }

    fun cancelDownTask(){
        Log.e("cancelDownTask","cancelDownTask--------"+aliyunManager)
        if(aliyunManager!=null){
            aliyunManager?.cancelTask()
        }
    }
    fun downFileAli(
        buckeName: String?,
        keyId: String?,
        secretId: String?,
        token: String?,
        filePath: String?,
        url: String?
    ) {
             aliyunManager =
                AliyunManager(buckeName, keyId, secretId, token, filePath, object :
                    AliyunManager.callback {
                    override fun upLoadSuccess(type: Int, imgPath: String) {
                        mHandler.post {

                            mDownLoadSuccess.value=true
                        }
                        Log.e("downFileAli","imgPath="+imgPath+"，type="+type)
                    }

                    override fun upLoadFailed(error: String) {
                        mHandler.post {
                            mDownLoadSuccess.value=false
                        }
                        Log.e("downFileAli","upLoadFailed="+error)
                    }

                    override fun upLoadProgress(
                        currentSize: Long,
                        totalSize: Long
                    ) {

                        mHandler.post {

                            var mBean=DownLoadBean()
                            mBean.currentSize=currentSize
                            mBean.totalSize=totalSize
                            mDownLoadBean.value=mBean
                        }

                        Log.e("downFileAli","currentSize="+currentSize+",totalSize="+totalSize)

                    }
                })
            aliyunManager?.downFile(filePath, url)
    }
}