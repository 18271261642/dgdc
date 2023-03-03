package com.jkcq.homebike.ride.history

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMFragment
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.ride.history.adpter.BarAdapter
import com.jkcq.homebike.ride.history.adpter.CenterLayoutManager
import com.jkcq.homebike.ride.history.adpter.HistoryWeekDetailAdapter
import com.jkcq.homebike.ride.history.bean.BarInfo
import com.jkcq.homebike.ride.history.bean.SportDetialBean
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.share.DayShareActivity
import com.jkcq.homebike.share.WeekShareActivity
import com.jkcq.util.MessageEvent
import com.jkcq.util.SiseUtil
import kotlinx.android.synthetic.main.fragment_sport_bar.*
import kotlinx.android.synthetic.main.view_bike_sport_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_dis_count.*
import kotlinx.android.synthetic.main.view_bike_sport_history_dis_count.*
import kotlinx.android.synthetic.main.view_bike_sport_history_dis_count.layout_dis
import kotlinx.android.synthetic.main.view_bike_sport_history_dis_count.tv_dis_unitl
import kotlinx.android.synthetic.main.view_bike_sport_history_dis_count.tv_dis_value
import kotlinx.android.synthetic.main.view_bike_sport_history_dis_count.tv_view_sport_count
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class WeekFragment : BaseVMFragment<BikeDataViewModel>() {


    var date: Int = 0
    var strDate: String = ""
    lateinit var strStartWeek: String
    lateinit var strEndWeek: String

    //柱状图的变量初始化
    var mDataList = mutableListOf<BarInfo>()
    var temmDataList = mutableListOf<BarInfo>()

    // var mDateBeans = mutableListOf<HistoryDateBean>()

    lateinit var mBarAdapter: BarAdapter
    lateinit var mCenterLayoutManager: CenterLayoutManager

    //运动详情的初始化
    var mSportDetialBean = mutableListOf<SportDetialBean>()
    lateinit var bikeSportDetailAdapter: HistoryWeekDetailAdapter


    val mBikeDataViewModel: BikeDataViewModel by lazy {

        createViewModel(
            BikeDataViewModel::class.java
        )
    }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = arguments!!.getInt("date")
        strStartWeek = arguments!!.getString("startdate").toString()
        strEndWeek = arguments!!.getString("enddate").toString()
        strDate = dateFormat.format(Date(date * 1000L))
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: MessageEvent) {

        when (messageEvent.msg) {
            MessageEvent.share -> if (userVisibleHint) {
                var intent = Intent(
                    Intent(
                        activity,
                        WeekShareActivity::class.java
                    )
                )
                intent.putExtra("date", date)
                intent.putExtra("strStartWeek", strStartWeek)
                intent.putExtra("strEndWeek", strEndWeek)
                startActivity(intent)

                // createShareBean()
            }
            MessageEvent.calender -> if (userVisibleHint) {
                /*  setPopupWindow(activity!!, tv_update_time)
                  val time = (System.currentTimeMillis() / 1000).toInt()
                  initDatePopMonthTitle()
                  calendarview!!.setActiveC(TimeUtil.second2Millis(time))
                  calendarview!!.setMonthDataListen(this@RopeDayFragment)
                  calendarview!!.setTimeInMillis(TimeUtil.second2Millis(time))*/
            }
        }
    }

    var mCurrentMessage: BarInfo? = null
    var mCurrentPosition = 0
    var currentPageNumber = 1

    var containCurrentDay = false;
    override fun startObserver() {


        //每周日的概要数据
        mBikeDataViewModel.mDailySummariesBean.observe(this, androidx.lifecycle.Observer {
            mSportDetialBean.clear()
            if (it == null || it.size == 0) {
                mDataList.clear()
                mBarAdapter.notifyDataSetChanged()
                setEmpView()
                //  mBarAdapter.setEmptyView(R.layout.include_rope_empty_view)
            } else {
                recyclerview_sport.visibility = View.VISIBLE
                var maxValue = 0
                it.forEach {
                    if (maxValue < it.totalCalorie.toInt()) {
                        maxValue = it.totalCalorie.toInt()
                    }
                    mSportDetialBean.add(
                        SportDetialBean(
                            it.exerciseDay,
                            it.totalCalorie,
                            it.totalDistance,
                            it.times
                        )
                    )

                }
                mDataList.clear()
                mDataList.addAll(temmDataList)
                it.forEach {
                    var date = it.exerciseDay
                    var bean = mDataList.findLast {
                        it.mdDate.equals(date)
                    }
                    if (maxValue < it.totalCalorie.toInt()) {
                        maxValue = it.totalCalorie.toInt()
                    }
                    bean!!.currentValue = it.totalCalorie.toInt()
                    bean!!.maxVlaue = maxValue

                }
                var size = 0
                while (size < mDataList.size) {
                    if (mDataList.get(size).currentValue > 0) {
                        mDataList.get(size).isSelect = true
                        break;
                    }
                    size++
                }
                mBarAdapter.notifyDataSetChanged()



                if (mSportDetialBean.size != 0) {
                    currentDetailIndex = 0
                    mSportDetialBean.get(0).isOpen = true
                    mBikeDataViewModel.getDeviceDailyBrief(
                        mSportDetialBean.get(0).date,
                        "" + DeviceType.DEVICE_S003.value + "," + DeviceType.DEVICE_S005.value,
                        mUserId
                    )
                }


            }
        })


        //获取每周的日期
        mBikeDataViewModel.mHistoryDateBean.observe(this, androidx.lifecycle.Observer {
            temmDataList.clear()
            containCurrentDay = false
            it!!.forEach {
                if (it.date.equals(dateFormat.format(Date()))) {
                    containCurrentDay = true
                }
                temmDataList.add(BarInfo(it.mdDate, it.date, 0, 100, false))
            }
            if (containCurrentDay) {
                iv_date_right.visibility = View.INVISIBLE
            } else {
                iv_date_right.visibility = View.VISIBLE
            }
            mBikeDataViewModel.getDeviceDailySummaries(
                temmDataList.get(temmDataList.size - 1).mdDate,
                "" + DeviceType.DEVICE_S003.value + "," + DeviceType.DEVICE_S005.value,
                BikeConfig.BIKE_SPORT_DATE_WEEK,
                mUserId
            )
        })



        mBikeDataViewModel.sportEveryCountDetail.observe(this, androidx.lifecycle.Observer {
            //每天的详情

            var dayDate = it.get(0).strDate
            Log.e(
                "sportEveryCountDetail",
                "dayDate=" + dayDate + ",mSportDetialBean=" + it
            )
            // Log.e("sportEveryCountDetail", "mSportDetialBean.size2" + mSportDetialBean.size)

            var list = it
            mSportDetialBean.forEach {

                if (it.date.equals(dayDate)) {
                    it.list = list as ArrayList<SportDetialBean>?;
                }
                Log.e(
                    "sportEveryCountDetail",
                    "dayDate=" + dayDate + ",mSportDetialBean=" + it
                )
            }
            if (mSportDetialBean.size > 0) {
                recycle_sport_detail.visibility = View.VISIBLE
                bikeSportDetailAdapter.notifyDataSetChanged()
            } else {
                recycle_sport_detail.visibility = View.INVISIBLE

            }
        })

        mBikeDataViewModel.sumSummerBean.observe(this, androidx.lifecycle.Observer {
            setItemvalue(it)
        })
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_sport_bar
    }

    override fun initView() {
    }

    override fun initData() {

        /**
         *  day: String,
        deviceType: String,
        summaryType: String,
        userId: String
         */
        tv_date.text = strStartWeek + "~" + strEndWeek
        //mBikeDataViewModel.getDeviceDailyBrief(strDate, "" + BikeConfig.BIKE_TYPE, mUserId)


        mBikeDataViewModel.getBikeWeekData(date)

        mBikeDataViewModel.getDeviceSummary(
            strDate,
            "" + DeviceType.DEVICE_S005.value + "," + DeviceType.DEVICE_S003.value,
            BikeConfig.BIKE_SPORT_DATE_WEEK,
            mUserId
        )

        /* mBikeDataViewModel.getDeviceDailySummaries(
             strStartWeek,
             "" + BikeConfig.BIKE_TYPE,
             BikeConfig.BIKE_SPORT_DATE_WEEK,
             mUserId
         )*/


        initBarRec()
        initSportDetailRec()
        initEvent()
    }


    fun initEvent() {

        if (strDate.equals(dateFormat.format(Date()))) {
            iv_date_right.visibility = View.INVISIBLE
        } else {
            iv_date_right.visibility = View.VISIBLE
        }
        iv_date_left.setOnClickListener {
            EventBus.getDefault().post(MessageEvent(MessageEvent.DayAdd))
        }
        iv_date_right.setOnClickListener {
            EventBus.getDefault().post(MessageEvent(MessageEvent.DaySub))
        }

    }

    fun setEmpView() {
        mDataList.clear()
        mBarAdapter.setEmptyView(R.layout.include_rope_empty_view)
        mBarAdapter.notifyDataSetChanged()
    }

    fun initBarRec() {
        mCenterLayoutManager = CenterLayoutManager(activity, RecyclerView.HORIZONTAL, true)
        recyclerview_sport.layoutManager = mCenterLayoutManager
        mBarAdapter = BarAdapter(mDataList, 7)
        recyclerview_sport.adapter = mBarAdapter
        mBarAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->

            if (TextUtils.isEmpty(mDataList.get(position).date)) {
                return@OnItemClickListener;
            }

            mCenterLayoutManager.smoothScrollToPosition(
                recyclerview_sport,
                RecyclerView.State(),
                position
            )
            for (user in mDataList) {
                user.isSelect = false;
            }

            mDataList.get(position).isSelect = true;
            adapter.notifyDataSetChanged()
        })
    }

    var currentDetailIndex = -1;

    fun initSportDetailRec() {
        recycle_sport_detail.layoutManager = LinearLayoutManager(activity)
        bikeSportDetailAdapter = HistoryWeekDetailAdapter(mSportDetialBean)
        recycle_sport_detail.adapter = bikeSportDetailAdapter

        bikeSportDetailAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->

            if (mSportDetialBean.get(position).isOpen) {
                mSportDetialBean.get(position).isOpen = false
                bikeSportDetailAdapter.notifyDataSetChanged()
            } else {
                mSportDetialBean.forEach {
                    it.isOpen = false
                }
                if (mSportDetialBean.get(position).list == null || mSportDetialBean.get(position).list.size == 0) {
                    currentDetailIndex = position
                    mBikeDataViewModel.getDeviceDailyBrief(
                        mSportDetialBean.get(position).date,
                        "" + DeviceType.DEVICE_S003.value + "," + DeviceType.DEVICE_S005.value,
                        mUserId
                    )

                }
                mSportDetialBean.get(position).isOpen = true
                bikeSportDetailAdapter.notifyDataSetChanged()
            }


        })
    }

    fun setItemvalue(summerBean: Summary) {


        item_time.setBg(R.drawable.shape_layout_gray_bg_15)
        item_cal.setBg(R.drawable.shape_layout_gray_bg_15)
        item_power.setBg(R.drawable.shape_layout_gray_bg_15)
        layout_dis.setBackgroundResource(R.drawable.shape_layout_gray_bg_15)
        BikeConfig.disUnit(tv_dis_unitl, activity)
        val dis = SiseUtil.disUnitCoversion(summerBean.totalDistance, BikeConfig.userCurrentUtil)
        val cal = SiseUtil.calCoversion(summerBean.totalCalorie)
        val min = SiseUtil.timeCoversionMin(summerBean.totalDuration)
        val power = SiseUtil.powerCoversion(summerBean.totalPowerGeneration)
        tv_dis_value.text = dis
        tv_view_sport_count.text = summerBean.totalTimes
        item_time.setValueText(min)
        item_cal.setValueText(cal)
        item_power.setValueText(power)
    }


}