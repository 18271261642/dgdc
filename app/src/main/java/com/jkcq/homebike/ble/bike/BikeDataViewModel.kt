package com.jkcq.homebike.ble.bike

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseViewModel
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bean.RankingBean
import com.jkcq.homebike.ble.bike.reponsebean.DailybriefBean
import com.jkcq.homebike.db.*
import com.jkcq.homebike.gen.DailyBriefDao
import com.jkcq.homebike.ota.DeviceUpgradeBean
import com.jkcq.homebike.ride.history.bean.BarInfo
import com.jkcq.homebike.ride.history.bean.HistoryDateBean
import com.jkcq.homebike.ride.history.bean.SportDetialBean
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.sceneriding.bean.PraiseBean
import com.jkcq.homebike.ride.util.BikeDataCache
import com.jkcq.util.DateUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.fragment_ride.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import java.util.*
import kotlin.collections.HashMap

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class BikeDataViewModel : BaseViewModel() {
    var mBikeName: String by Preference(Preference.BIKENAME, "")

    lateinit var bikeDbModel: BikeDbModel

    var mHasDataMonthDate = MutableLiveData<List<String>>()
    var mPaiseBean = MutableLiveData<PraiseBean>()
    var mDeviceUpgradeBean = MutableLiveData<DeviceUpgradeBean>()
    var updateBikeBean = MutableLiveData<Summary>()
    var mRankingBeans = MutableLiveData<List<RankingBean>>()
    var mRankingSumNumber = MutableLiveData<String>()
    var myRankingBean = MutableLiveData<RankingBean>()
    var mHistoryDateBean = MutableLiveData<List<HistoryDateBean>>()
    var mDailySummariesBean = MutableLiveData<List<DailySummaries>>()
    var sumSummerBean = MutableLiveData<Summary>()
    var barInfo = MutableLiveData<List<BarInfo>>()
    var barInfoNodate = MutableLiveData<Boolean>()
    var sportEveryCountDetail = MutableLiveData<List<SportDetialBean>>()
    var sumDis = MutableLiveData<String>()
    val mLoginRepository: BikeDataRepository by lazy { BikeDataRepository() }


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
        isCourse: Boolean,
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
            "isCourse=" + isCourse + "targetDis=" + targetDis + "distance=" + distance + ",duration=" + duration
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

        if (isCourse) {
            if (duration >= targetDis) {
                upgradeBikeBean.duration = (targetDis)
            } else {
                upgradeBikeBean.duration = (duration)
            }
            upgradeBikeBean.distance = (distance)
        } else {

            if (distance >= targetDis) {
                upgradeBikeBean.distance = (targetDis)
            } else {
                upgradeBikeBean.distance = (distance)
            }
            upgradeBikeBean.duration = (duration)
        }
        upgradeBikeBean.heartRateArray = strHr
        upgradeBikeBean.powerArray = strPower
        upgradeBikeBean.powerGeneration = (powerGeneration)
        upgradeBikeBean.relevanceId = relevanceId
        upgradeBikeBean.steppedFrequencyArray = strsteppedFrequency

        bikeDbModel = BikeDbModel.getInstance()

        bikeDbModel.addNote(upgradeBikeBean)

        cyclingRecords(
            userId,
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

    fun upgradeCyclingRecords(
        isCourse: Boolean,
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
            "isCourse=" + isCourse + "targetDis=" + targetDis + "distance=" + distance + ",duration=" + duration
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

        if (isCourse) {
            if (duration >= targetDis) {
                upgradeBikeBean.duration = (targetDis)
            } else {
                upgradeBikeBean.duration = (duration)
            }
            upgradeBikeBean.distance = (distance)
        } else {

            if (distance >= targetDis) {
                upgradeBikeBean.distance = (targetDis)
            } else {
                upgradeBikeBean.distance = (distance)
            }
            upgradeBikeBean.duration = (duration)
        }
        upgradeBikeBean.heartRateArray = strHr
        upgradeBikeBean.powerArray = strPower
        upgradeBikeBean.powerGeneration = (powerGeneration)
        upgradeBikeBean.relevanceId = relevanceId
        upgradeBikeBean.steppedFrequencyArray = strsteppedFrequency

        bikeDbModel = BikeDbModel.getInstance()

        bikeDbModel.addNote(upgradeBikeBean)

        cyclingRecords(
            userId,
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


    fun getDeviceDetaiUrl() {
        executeRequest({ mLoginRepository.getDetailUrl() }, {
            Log.e("getDeviceDetaiUrl", "" + it)
            BikeConfig.detailUrlBean.dark = it.dark
            BikeConfig.detailUrlBean.light = it.light
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })
    }


    fun getBikeWeekData(data: Int) {
        var wristbandsteps = mutableListOf<HistoryDateBean>()
        val calendar = Calendar.getInstance()
        calendar.time = Date(data * 1000L)
        var strDate = DateUtil.dataToString(calendar.time, "yyyy-MM-dd")
        var wristbandstep1: HistoryDateBean? = null
        for (i in 0..6) {
            wristbandstep1 = HistoryDateBean()
            if (i != 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            strDate = DateUtil.dataToString(calendar.time, "yyyy-MM-dd")
            wristbandstep1.mdDate = DateUtil.dataToString(calendar.time, "M/d")
            wristbandstep1.date = strDate
            wristbandsteps.add(wristbandstep1)
        }
        Collections.reverse(wristbandsteps)
        mHistoryDateBean.value = wristbandsteps
    }

    fun getBikeMonthData(data: Int) {
        var wristbandsteps = mutableListOf<HistoryDateBean>()
        val calendar = Calendar.getInstance()
        //需要判断是不是当前的月
        //需要判断是不是当前的月
        calendar.time = Date(data * 1000L)
        calendar[Calendar.DAY_OF_MONTH] = 1
        val monthCount: Int = DateUtil.getMonthOfDay(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1
        )
        var strDate = DateUtil.dataToString(calendar.time, "yyyy-MM-dd")

        var historyDateBean: HistoryDateBean
        for (i in 0 until monthCount) {
            historyDateBean = HistoryDateBean()
            if (i != 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            strDate = DateUtil.dataToString(calendar.time, "yyyy-MM-dd")
            historyDateBean.date = strDate
            historyDateBean.mdDate = DateUtil.dataToString(calendar.time, "MM-dd")
            wristbandsteps.add(historyDateBean)
        }
        mHistoryDateBean.value = wristbandsteps
    }

    fun getDeviceSumDis(
        day: String,
        deviceType: String,
        summaryType: String,
        userId: String
    ) {

        executeRequest({ mLoginRepository.getSummary(day, deviceType, summaryType, userId) }, {
            sumDis.value = it.totalDistance
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })

    }

    fun getDeviceSummary(
        day: String,
        deviceType: String,
        summaryType: String,
        userId: String
    ) {
        executeRequest({ mLoginRepository.getSummary(day, deviceType, summaryType, userId) }, {
            Log.e("getDeviceSummary", it.toString())

            sumSummerBean.value = it
            if (!TextUtils.isEmpty(it.deviceType)) {
                bikeDbModel = BikeDbModel.getInstance()
                bikeDbModel.addSummary(it, day, it.deviceType, summaryType, userId)
            }
        }, {
            // longToast(content = it.errorMsg)
        })
    }

    fun getDeviceDailyBrief(
        day: String,
        deviceType: String,
        userId: String
    ) {
        executeRequest({ mLoginRepository.getDailyBrief(day, deviceType, userId) }, {
            // updateSuccessId.value = it
            successDailyBrief(it)
            var bikeDbModel = BikeDbModel.getInstance()
            it.forEach {
                bikeDbModel.addDailyBrief(it, it.deviceType, userId)
            }
            // dailyBriefBean.value = it
        }, {

            barInfoNodate.value = true

            // longToast(content = it.errorMsg)
        })
    }


    fun successDailyBrief(it: List<DailybriefBean>) {
        var mSportDetialBean = mutableListOf<SportDetialBean>()
        var mDataList = mutableListOf<BarInfo>()
        var max = 0;
        var min = 0;
        it.forEach {
            if (max < it.calorie.toInt()) {
                max = it.calorie.toInt()
            }
        }

        Log.e("successDailyBrief", "it.size=" + it.size)
        it.forEach {
            //String date, String sportName, String sportUrl, String sportDis, String sportTime, String sportCal
            var name = ""
            var nameEn = ""
            var url = "";

            when (it.exerciseType) {
                "1" -> {
                    name =
                        BaseApp.sApplicaton.resources.getString(R.string.bike_type_scen_bike) + ":" + it.scenario.name
                    nameEn =
                        BaseApp.sApplicaton.resources.getString(R.string.bike_type_scen_bike) + ":" + it.scenario.nameEn
                    url = it.scenario.imageUrl;
                }
                "2" -> {
                    name =
                        BaseApp.sApplicaton.resources.getString(R.string.pk_title) + ":" + it.pkInfo.pkName
                    nameEn =
                        BaseApp.sApplicaton.resources.getString(R.string.pk_title) + ":" + it.pkInfo.pkName
                    url = it.scenario.imageUrl
                }
                "3" -> {
                    name =
                        BaseApp.sApplicaton.resources.getString(R.string.bike_course_title) + ":" + it.course.name
                    nameEn =
                        BaseApp.sApplicaton.resources.getString(R.string.bike_course_title) + ":" + it.course.name
                    url = it.course.imageUrl;
                }
            }

            /* when (type) {
                 BikeConfig.BIKE_LINE.toString() -> {
                     name = it.scenario.name
                     nameEn = it.scenario.nameEn
                     url = it.scenario.imageUrl;
                 }
                 BikeConfig.BIKE_COURSE.toString() -> {
                     name = it.course.name
                     nameEn = it.course.name
                     url = it.course.imageUrl;
                 }
             }*/


            val dis = SiseUtil.disUnitCoversion(it.distance, BikeConfig.userCurrentUtil)
            val cal = SiseUtil.calCoversion(it.calorie)
            val hms: DateUtil.HMS = DateUtil.getHMSFromMillis(it.duration.toLong() * 1000)


            val min = String.format(
                "%02d:%02d:%02d",
                hms.getHour(),
                hms.getMinute(),
                hms.getSecond()
            )
            //String date, String sportName, String sportUrl, String sportDis, String sportTime, String sportCal
            mSportDetialBean.add(
                SportDetialBean(
                    it.id,
                    DateUtil.getYyyyMm(it.exerciseTime.toLong()),
                    DateUtil.getHHmmss(it.exerciseTime.toLong()),
                    name,
                    nameEn,
                    url,
                    dis,
                    min,
                    cal
                )
            )
            mDataList.add(
                BarInfo(
                    DateUtil.getHHmmss(it.exerciseTime.toLong()),
                    it.calorie.toInt(),
                    max,
                    false
                )
            )
        }
        mDataList.get(0).isSelect = true;
        sportEveryCountDetail.value = mSportDetialBean
        barInfo.value = mDataList
    }


    fun findDailyBrief(
        userId: String?,
        strDate: String?,
        deviceType: String?
    ) {

        var bikeDbModel = BikeDbModel.getInstance()

        var dailyBriefRxQuery = bikeDbModel.daoSession.dailyBriefDao.queryBuilder().where(
            DailyBriefDao.Properties.UserId.eq(userId),
            DailyBriefDao.Properties.StrDate.eq(strDate),
            DailyBriefDao.Properties.DeviceType.eq(deviceType)
        ).rx()
        dailyBriefRxQuery.list().observeOn(AndroidSchedulers.mainThread())
            .subscribe(Action1<List<DailyBrief?>> { note1: List<DailyBrief?>? ->


                var beans = mutableListOf<DailybriefBean>()

                note1!!.forEach {
                    var dailybriefBean = DailybriefBean()
                    beans.add(dailybriefBean)
                }
                successDailyBrief(beans)
            })
    }


    fun getDeviceDailySummaries(
        day: String,
        deviceType: String,
        summaryType: String,
        userId: String
    ) {


        executeRequest(
            { mLoginRepository.getDailySummaries(day, deviceType, summaryType, userId) },
            {
                mDailySummariesBean.value = it
                bikeDbModel = BikeDbModel.getInstance()
                it.forEach {
                    bikeDbModel.addDailySummarise(it, day, it.deviceType, summaryType, userId)
                }

                //updateSuccessId.value = it
            },
            {
                // longToast(content = it.errorMsg)
            })
    }

    fun getDeviceExerciseDaysInMonth(
        deviceType: String,
        month: String,
        userId: String
    ) {
        executeRequest(
            { mLoginRepository.getExerciseDaysInMonth(deviceType, month, userId) },
            {
                // updateSuccessId.value = it

                mHasDataMonthDate.value = it
            },
            {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton, it.errorMsg
                )
            })
    }


    fun cyclingRecords(
        userId: String,
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

        executeRequest({ mLoginRepository.updateBikeData(map) }, {
            tempValue.upgradeId = it
            updateBikeBean.value = tempValue
            BikeConfig.sBikeBean = null
            BikeConfig.isUpgradeSuccess = true
            if (cyclingType == BikeConfig.BIKE_FREE) {
                BikeDataCache.clearBikeBean(BaseApp.sApplicaton, "free")
            } else if (cyclingType == BikeConfig.BIKE_LINE) {
                BikeDataCache.clearBikeBean(BaseApp.sApplicaton, "scene")
            } else if (cyclingType == BikeConfig.BIKE_COURSE) {
                BikeDataCache.clearBikeBean(BaseApp.sApplicaton, "course")
            }
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })

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


    //0 场景，1课程
    fun getSceneRankings(sceneId: String, praiseType: String) {
        executeRequest({ mLoginRepository.getRankings(sceneId, praiseType) }, {
            mRankingBeans.value = it.list
            mRankingSumNumber.value = "" + it.totalNum
            myRankingBean.value = it.myRanking
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })
    }

    fun praiseOther(fromUserId: String, praiseType: String, relevanceId: String, toUserId: String) {


        /**
         * {
        "fromUserId": 0,
        "praiseType": 0,
        "relevanceId": "string",
        "toUserId": 0
        }
         */
        val map = HashMap<String, String>()
        map.put("fromUserId", fromUserId);
        map.put("praiseType", praiseType);
        map.put("relevanceId", relevanceId);
        map.put("toUserId", toUserId);
        executeRequest({ mLoginRepository.praiseToOther(map) }, {
            it.userId = toUserId


            mPaiseBean.value = it
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })
    }

    fun upPraiseOther(relevanceId: String, userId: String) {
        executeRequest({ mLoginRepository.unPraiseToOther(relevanceId) }, {
            it.userId = userId
            if (it.praiseNums.equals("-1")) {
                return@executeRequest
            }
            mPaiseBean.value = it
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })
    }

    fun getdeviceVersion(deviceType: String) {
        executeRequest({ mLoginRepository.deviceVersion(deviceType) }, {
            mDeviceUpgradeBean.value = it
        }, {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, it.errorMsg
            )
        })
    }

}