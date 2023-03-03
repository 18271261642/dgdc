package com.jkcq.homebike.ride.history

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMFragment
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.ride.history.adpter.HistoryWeekDetailAdapter
import com.jkcq.homebike.ride.history.bean.BarChartEntity
import com.jkcq.homebike.ride.history.bean.BarInfo
import com.jkcq.homebike.ride.history.bean.SportDetialBean
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.share.MonthShareActivity
import com.jkcq.homebike.share.WeekShareActivity
import com.jkcq.util.MessageEvent
import com.jkcq.util.SiseUtil
import kotlinx.android.synthetic.main.fragment_sport_bar.iv_date_left
import kotlinx.android.synthetic.main.fragment_sport_bar.iv_date_right
import kotlinx.android.synthetic.main.fragment_sport_bar.recycle_sport_detail
import kotlinx.android.synthetic.main.fragment_sport_bar.tv_date
import kotlinx.android.synthetic.main.fragment_sport_month_bar.*
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

class MonthFragment : BaseVMFragment<BikeDataViewModel>() {


    var date: Int = 0
    var strDate: String = ""
    lateinit var strStartWeek: String
    lateinit var strEndWeek: String

    //柱状图的变量初始化
    var mDataList = mutableListOf<BarInfo>()

    // var mDateBeans = mutableListOf<HistoryDateBean>()
    /* lateinit var mBarAdapter: BarAdapter
     lateinit var mCenterLayoutManager: CenterLayoutManager
 */
    //运动详情的初始化
    var mSportDetialBean = mutableListOf<SportDetialBean>()
    lateinit var bikeSportDetailAdapter: HistoryWeekDetailAdapter


    val mBikeDataViewModel: BikeDataViewModel by lazy {

        createViewModel(
            BikeDataViewModel::class.java
        )
    }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val dateFormatYYMM = SimpleDateFormat("yyyy-MM")
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
                // createShareBean()

                var intent = Intent(
                    Intent(
                        activity,
                        MonthShareActivity::class.java
                    )
                )
                intent.putExtra("date", date)
                startActivity(intent)
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
                barChartView.visibility = View.INVISIBLE
                iv_no_data.visibility = View.VISIBLE

                //  mBarAdapter.setEmptyView(R.layout.include_rope_empty_view)
            } else {
                barChartView.visibility = View.VISIBLE
                iv_no_data.visibility = View.INVISIBLE
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
                it.forEach {
                    var date = it.exerciseDay
                    var bean = mDataList.findLast {
                        it.mdDate.equals(date)
                    }
                    if (maxValue < it.totalCalorie.toInt()) {
                        maxValue = it.totalCalorie.toInt()
                    }
                    bean?.currentValue = it.totalCalorie.toInt()
                    bean?.maxVlaue = maxValue

                }

                setWeekBarChartData()

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
            mDataList.clear()
            containCurrentDay = false
            it?.forEach {
                if (it.date.equals(dateFormat.format(Date()))) {
                    containCurrentDay = true
                }
                mDataList.add(BarInfo(it.mdDate, it.date, 0, 100, false))
            }
            if (containCurrentDay) {
                iv_date_right.visibility = View.INVISIBLE
            } else {
                iv_date_right.visibility = View.VISIBLE
            }



            mBikeDataViewModel.getDeviceDailySummaries(
                mDataList.get(0).mdDate,
                "" + DeviceType.DEVICE_S003.value + "," + DeviceType.DEVICE_S005.value,
                BikeConfig.BIKE_SPORT_DATE_MONTH,
                mUserId
            )
        })
        mBikeDataViewModel.barInfo.observe(this, androidx.lifecycle.Observer {


            /* mDataList.clear()
             mDataList.addAll(it)
             mBarAdapter.notifyDataSetChanged()*/
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
        return R.layout.fragment_sport_month_bar
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
        tv_date.text = dateFormatYYMM.format(Date(date * 1000L))
        //mBikeDataViewModel.getDeviceDailyBrief(strDate, "" + BikeConfig.BIKE_TYPE, mUserId)


        mBikeDataViewModel.getBikeMonthData(date)

        mBikeDataViewModel.getDeviceSummary(
            strDate,
            "" + DeviceType.DEVICE_S005.value + "," + DeviceType.DEVICE_S003.value,
            BikeConfig.BIKE_SPORT_DATE_MONTH,
            mUserId
        )



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

    private fun setWeekBarChartData() {
        val datas: MutableList<BarChartEntity> = ArrayList()
        for (i in mDataList.indices) {
            //val cal = StepArithmeticUtil.stepsConversionCaloriesFloat(userInfoBean.getWeight().toFloat(), stepList[i].toLong()).toInt()
            // val dis = StepArithmeticUtil.stepsConversionDistanceFloat(userInfoBean.getHeight().toFloat(), userInfoBean.getGender(), stepList[i].toLong())
            datas.add(
                BarChartEntity(
                    i.toString(),
                    mDataList.get(i).date,
                    arrayOf(mDataList.get(i).currentValue * 1.0f, 0f, 0f)
                )
            )
        }
        /* if (datas.size() == 31) {
            datas.add(new BarChartEntity(String.valueOf(0), new Float[]{Float.valueOf(0), Float.valueOf(0), Float.valueOf(0)}));
            date.add(date.get(date.size() - 1));
        }*/

        var index = 0
        var clickPostion = 0
        while (index < mDataList?.size) {
            if (mDataList.get(index).currentValue > 0) {
                clickPostion = index
            }
            //println("item at $index is ${items[index]}")
            index++
        }

        //  barChartView.setOnItemBarClickListener { position -> Log.e("TAG", "点击了：$position") }
        barChartView.setData(datas, intArrayOf(Color.parseColor("#6FC5F4")), "分组", "数量")
        barChartView.startAnimation()
        handler.postDelayed({
            if (barChartView != null) {
                barChartView.setmClickPosition(clickPostion)
            }
        }, 50)


    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(null)
    }

    var handler: Handler = Handler()

}