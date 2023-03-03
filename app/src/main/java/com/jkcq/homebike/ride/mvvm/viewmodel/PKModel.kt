package com.jkcq.homebike.ride.mvvm.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.websocket.bean.PKType
import com.google.gson.Gson
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseViewModel
import com.jkcq.base.observable.NetDialogObservable
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.db.UpgradeBikeBean
import com.jkcq.homebike.ride.mvvm.repository.PKRepository
import com.jkcq.homebike.ride.pk.bean.*
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.pk.bean.enumbean.PkState
import com.jkcq.homebike.ride.util.BikeDataCache
import com.jkcq.util.ktx.ToastUtil
import java.util.*
import kotlin.collections.HashMap

/**
 *  Created by BeyondWorlds
 *  on 2020/7/27
 */
class PKModel : BaseViewModel() {
    var mBikeName: String by Preference(Preference.BIKENAME, "")
    var mpkId: String by Preference(Preference.PK_ID, "")
    val mRideRepository by lazy { PKRepository() }
    var mPKResultList = MutableLiveData<List<PKResultBean>>()
    var mSceneBean = MutableLiveData<List<SceneBean>>()
    var mPkList = MutableLiveData<List<PKListBean>>()
    var mPkRoomBean = MutableLiveData<PKRoomBean>()
    var mReJoinRoom = MutableLiveData<PKRoomBean>()
    var mLeavePk = MutableLiveData<Boolean>()
    var mStartPk = MutableLiveData<Boolean>()
    var mPkNumberBean = MutableLiveData<ResultPKNumberBean>()
    var mBestRecodeBean = MutableLiveData<BestRecodeBean>()
    var mPropesList = MutableLiveData<List<PropesBean>>()
    var mCanJoin = MutableLiveData<PKStateBean>()
    var mPKDestory = MutableLiveData<Boolean>()
    var shareUrl = MutableLiveData<String>()

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

    fun getPKResultList(pkId: String) {
        executeRequest({ mRideRepository.getPKResult(pkId) }, {
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            mPKResultList.value = it

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getPkParticipantNum() {
        executeRequest({ mRideRepository.getPkParticipantNum() }, {
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            mPkNumberBean.value = it
            // mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getBestRecodes(scenarioId: String, userId: String) {
        executeRequest({ mRideRepository.getBestRecodes(scenarioId, userId) }, {

            mBestRecodeBean.value = it
            Log.e("getBestRecodes", "" + it)
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            //mPkNumberBean.value = it
            // mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            if (it.errCode == 2000) {
                return@executeRequest
            }
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun getPropsAll() {
        executeRequest({ mRideRepository.getPropsAll() }, {

            mPropesList.value = it
            Log.e("getBestRecodes", "" + it)
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            //mPkNumberBean.value = it
            // mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }


    fun getPKRestutUrl() {

        executeRequest({ mRideRepository.getPKResultUrl(mpkId, mUserId) }, {

            shareUrl.value = it
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            // mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }


    fun getPkList() {
        executeRequest({ mRideRepository.getPkList() }, {


            var mDataList = mutableListOf<PKListBean>()

            it.forEach {
                if (!TextUtils.isEmpty(it.scenario.videoUrl)) {
                    mDataList.add(it)
                }
            }

            mPkList.value = mDataList
            //需要把数据库的数据取出来进行对比，把已经下线的数据删除,包括删除本地的视频文件
            // mSceneBean.value = it
            /* it.forEach {
                 BikeDbModel.getInstance().addSenceBean(it)
             }
             getAllSenceBean("" + BikeConfig.BIKE_TYPE)*/

        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun createPkRoom(
        participantNum: String,
        pkName: String,
        pkPassword: String,
        scenarioId: String
    ) {

        //PK类型，0：单人，1：组队
        /**
         * {
        "participantNum": 0,
        "pkName": "string",
        "pkPassword": "string",
        "playType": 0,
        "scenarioId": "string",
        "userId": 0
        }
         */
        val map = HashMap<String, String>()
        map.put("participantNum", participantNum)
        map.put("pkName", pkName)
        map.put("userId", mUserId)
        map.put("scenarioId", scenarioId)
        map.put("playType", "0")
        if (!TextUtils.isEmpty(pkPassword)) {
            map.put("pkPassword", pkPassword)
            if (pkPassword.length < 4) {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton, R.string.pk_hide_enter_pwd
                )
                return
            }
        }
        NetDialogObservable.getInstance().show()
        executeRequest({ mRideRepository.createPkRoom(map) }, {
            mpkId = it.cyclingPk.id
            mPkRoomBean.value = it
        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    fun addPkRoom(
        pkId: String,
        pkPassword: String
    ) {

        //PK类型，0：单人，1：组队
        /**
        {
        "joinType": 0,
        "pkId": "string",
        "pkPassword": "string",
        "userId": 0
        }
         */
        val map = HashMap<String, String>()
        map.put("pkId", pkId)
        map.put("userId", mUserId)
        if (!TextUtils.isEmpty(pkPassword)) {
            map.put("pkPassword", pkPassword)
            if (pkPassword.length < 4) {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton, R.string.pk_hide_enter_pwd
                )
                return
            }
        }
        NetDialogObservable.getInstance().show()
        executeRequest({ mRideRepository.addPk(map) }, {
            mpkId = it.cyclingPk.id
            mPkRoomBean.value = it
        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }


    //退出PK
    fun leavePk(pkId: String, isCreate: Boolean) {
        val map = HashMap<String, String>()
        map.put("pkId", pkId)
        map.put("userId", mUserId)
        NetDialogObservable.getInstance().show()
        executeRequest({ mRideRepository.leavePk(map, isCreate) }, {
        }, {
            if (it.errCode == 2000) {
                mpkId = ""
                mLeavePk.value = true
            } else if (it.errCode == 1104) {
                mpkId = ""
                mPKDestory.value = true
            } else {
                ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
            }
        })
    }

    //开始PK
    fun startPk(pkId: String) {
        val map = HashMap<String, String>()
        map.put("pkId", pkId)
        map.put("userId", mUserId)
        NetDialogObservable.getInstance().show()
        executeRequest({ mRideRepository.startPk(map) }, {
        }, {
            if (it.errCode == 2000) {
                mpkId = ""
                mStartPk.value = true
            } else {
                ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
            }
        })
    }


    /**
     * {
    "calorie": 0,
    "deviceType": 0,
    "distance": 0,
    "duration": 0,
    "exerciseTime": 0,
    "exerciseType": 0,
    "heartRateArray": "string",
    "powerArray": "string",
    "powerGeneration": 0,
    "relevanceId": "string",
    "steppedFrequencyArray": "string",
    "userId": 0
    }
     */
    fun upgradeCyclingPkRecords(
        durationmill: Long,
        mPKID: String,
        targetDis: Int,
        userId: String,
        deviceTypeId: Int,
        calorie: Int,
        cyclingTime: Long,
        cyclingType: Int,
        distance: Int,
        duration: Int,
        heartRateArray: LinkedList<Int>,
        powerArray: LinkedList<Int>,
        powerGeneration: Int,
        relevanceId: String,
        steppedFrequencyArray: LinkedList<Int>
    ) {


        Log.e(
            "upgradeCyclingRecords",
            "targetDis=" + targetDis + "distance=" + distance + ",duration=" + duration
        )
        val gson = Gson()
        var strHr = ""
        if (Collections.max(heartRateArray) < 30) {
            strHr = gson.toJson(ArrayList<Int>())
        } else {
            var tempList = getsinList(heartRateArray, 15)
            if (Collections.max(tempList) > 0) {
                strHr = gson.toJson(tempList)
            } else {
                strHr = gson.toJson(ArrayList<Int>())
            }
        }
        var strPower = ""
        var strsteppedFrequency = ""
        if (powerArray.size > 3600) {
            var tempList = getsinList(powerArray, 2)
            strPower = gson.toJson(tempList)
        } else {
            strPower = gson.toJson(powerArray)
        }
        if (steppedFrequencyArray.size > 3600) {
            var tempList = getsinList(steppedFrequencyArray, 2)
            strsteppedFrequency = gson.toJson(tempList)
        } else {
            strsteppedFrequency = gson.toJson(steppedFrequencyArray)
        }
        var upgradeBikeBean = UpgradeBikeBean()
        upgradeBikeBean.userId = userId
        upgradeBikeBean.deviceType = deviceTypeId
        upgradeBikeBean.calorie = calorie
        upgradeBikeBean.exerciseTime = cyclingTime
        upgradeBikeBean.exerciseType = cyclingType
        if (distance >= targetDis) {
            upgradeBikeBean.distance = (targetDis)
        } else {
            upgradeBikeBean.distance = (distance)
        }
        upgradeBikeBean.duration = (duration)
        upgradeBikeBean.heartRateArray = strHr
        upgradeBikeBean.powerArray = strPower
        upgradeBikeBean.powerGeneration = (powerGeneration)
        upgradeBikeBean.relevanceId = relevanceId
        upgradeBikeBean.steppedFrequencyArray = strsteppedFrequency


        var pkState = PkState.START.value
        if (distance >= targetDis) {
            pkState = PkState.END.value
        }

        var mill = System.currentTimeMillis() % 1000

        cyclingRecords(
            mPKID,
            userId,
            upgradeBikeBean.duration * 1000L,
            pkState,
            deviceTypeId,
            calorie,
            cyclingTime,
            cyclingType,
            upgradeBikeBean.distance,
            upgradeBikeBean.duration,
            strHr,
            strPower,
            powerGeneration,
            relevanceId,
            strsteppedFrequency
        )

    }

    //0:未开始，1：进行中，2：已结束"

    fun cyclingRecords(
        pkId: String,
        userId: String,
        durationMillis: Long,
        pkStatus: Int,
        deviceTypeId: Int,
        calorie: Int,
        cyclingTime: Long,
        cyclingType: Int,
        distance: Int,
        duration: Int,
        heartRateArray: String,
        powerArray: String,
        powerGeneration: Int,
        scenarioId: String,
        steppedFrequencyArray: String
    ) {

        var tempValue = Summary()
        tempValue.totalDistance = distance.toString()
        tempValue.totalDuration = duration.toString()
        tempValue.totalCalorie = calorie.toString()
        val map = HashMap<String, String>()
        map["userId"] = userId
        map["pkId"] = pkId
        map["durationMillis"] = "" + durationMillis


        Log.e("cyclingRecords  update", "durationMillis=" + durationMillis)
        map["pkStatus"] = "" + pkStatus
        map["deviceCategoryCode"] = "" + DeviceType.DEVICE_BIKE.value
        map["deviceType"] = "" + deviceTypeId
        if (mBikeName.contains(DeviceType.DEVICE_S003.name)) {
            map["deviceType"] = "" + DeviceType.DEVICE_S003.value
        } else if (mBikeName.contains(DeviceType.DEVICE_S005.name)) {
            map["deviceType"] = "" + DeviceType.DEVICE_S005.value
        }
        map["calorie"] = "" + calorie
        map["exerciseTime"] = "" + cyclingTime
        map["exerciseType"] = "" + cyclingType
        map["distance"] = "" + distance
        map["duration"] = "" + duration
        map["heartRateArray"] = heartRateArray
        map["powerArray"] = powerArray
        map["powerGeneration"] = "" + powerGeneration
        map["steppedFrequencyArray"] = steppedFrequencyArray
        if (TextUtils.isEmpty(scenarioId) || scenarioId.equals("0")) {

        } else {
            map["relevanceId"] = "" + scenarioId
        }

        executeRequest({ mRideRepository.updatePKData(map) }, {
            BikeConfig.sBikeBean = null
            BikeDataCache.clearBikeBean(BaseApp.sApplicaton)
            BikeConfig.pPUpdateId = it
        }, {
            if (it.errCode == 2000) {
                BikeConfig.sBikeBean = null
            }
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })

    }

    fun reJoinPk() {
        val map = HashMap<String, String>()
        map.put("pkId", mpkId)
        map.put("userId", mUserId)
        executeRequest({ mRideRepository.reJoinPk(map) }, {
            mReJoinRoom.value = it
        }, {
            ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
        })
    }

    //查询PK的状态
    fun findPKState() {
        Log.e("getPkState", "mPId=" + mpkId + "userId=" + mUserId)
        if (TextUtils.isEmpty(mpkId) || TextUtils.isEmpty(mUserId)) {
            return
        }
        executeRequest({ mRideRepository.findJoinPk(mpkId, mUserId) }, {
            Log.e(
                "getPkState",
                "mPId=" + mpkId + "userId=" + mUserId + "pkStatus++++" + it.pkStatus
            )
            mCanJoin.value = it

        }, {
            if (it.errCode == 2000) {
                mPKDestory.value = true
            } else {
                ToastUtil.showTextToast(BaseApp.sApplicaton, it.errorMsg)
            }
        })
    }


    fun getsinList(heartRateArray: LinkedList<Int>, pagesize: Int): ArrayList<Int> {
        val sumHeartList = ArrayList<Int>()
        var size = heartRateArray.size
        sumHeartList.addAll(heartRateArray)

        var pagecount = 0
        var m = 0
        if (size % pagesize == 0) {
            m = 0
        } else {
            m = 1
        }
        if (m > 0) {
            pagecount = size / pagesize
        } else {
            pagecount = size / pagesize
        }
        val tempList = ArrayList<Int>()

        for (i in 1..pagecount) {
            Log.e(
                "sumHeartList",
                "pagecount+" + pagecount + "i=" + i + "------" + sumHeartList.toString()
            )
            if (m == 0) {
                val subList: List<Int> =
                    sumHeartList.subList((i - 1) * pagesize, pagesize * i)
                tempList.add(getMinHeart(subList))
            } else {
                if (i == pagecount) {
                    val subList: List<Int> =
                        sumHeartList.subList((i - 1) * pagesize, size)
                    tempList.add(getMinHeart(subList))
                } else {
                    val subList: List<Int> =
                        sumHeartList.subList((i - 1) * pagesize, pagesize * i)
                    tempList.add(getMinHeart(subList))
                }
            }
        }
        Log.e(
            "sumHeartList",
            "pagecount+" + pagecount + "temp=------" + tempList.toString()
        )
        return tempList

    }

    @Synchronized
    fun getMinHeart(subList: List<Int>?): Int {
        return try {
            var heart = 0

            Log.e("sumHeartList", "sumHeartList" + subList.toString())
            if (subList == null && subList!!.size == 0) {
                heart = 0
            } else {
                if (Collections.max(subList) == 0) {
                    heart = 0
                } else {
                    val resultList: MutableList<Int> = ArrayList()
                    for (j in subList.indices) {
                        if (subList[j] != 0) {
                            resultList.add(subList[j])
                        }
                    }
                    for (j in resultList.indices) {
                        heart = heart + resultList[j]
                    }
                    heart = heart / resultList.size
                    //tempList.add(heart);
                }
            }
            heart
        } catch (e: Exception) {
            0
        }
    }

}