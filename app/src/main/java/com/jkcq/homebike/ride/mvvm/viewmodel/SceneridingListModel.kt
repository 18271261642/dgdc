package com.jkcq.homebike.ride.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseViewModel
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.BikeDbModel
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.mvvm.repository.SceneRepository
import com.jkcq.util.ktx.ToastUtil
import rx.android.schedulers.AndroidSchedulers

/**
 *  Created by BeyondWorlds
 *  on 2020/7/27
 */
class SceneridingListModel : BaseViewModel() {
    val mRideRepository by lazy { SceneRepository() }
    var mSceneBean = MutableLiveData<List<SceneBean>>()
    var mSingeSceneBean = MutableLiveData<SceneBean>()
    fun getAllSceneList(deviceType: String) {
        executeRequest({ mRideRepository.getListByDeviceTypeId(deviceType) }, {
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getSceneList(deviceType: String, applyType: String) {
        executeRequest({ mRideRepository.getListByDeviceTypeId(deviceType, applyType) }, {
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getFreeBackgroundList() {
        executeRequest({ mRideRepository.getBackgrounds() }, {
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getCourseList(deviceType: String, language: String) {
        executeRequest({ mRideRepository.getCourseListByDeviceType(deviceType, language) }, {


            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getCourseList(deviceType: String) {
        executeRequest({ mRideRepository.getCourseDetail(deviceType) }, {

            mSingeSceneBean.value = it
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun updateScene(sceneId: String, path: String) {
        // BikeDbModel.getInstance().updateSenceBean(sceneId, 1, path)

    }

}