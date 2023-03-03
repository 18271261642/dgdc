package com.jkcq.homebike.ride.history

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jkcq.base.base.BaseVMFragment
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.login.WebHitoryViewActivity
import com.jkcq.homebike.ride.history.adpter.BarAdapter
import com.jkcq.homebike.ride.history.adpter.BikeSportDetailAdapter
import com.jkcq.homebike.ride.history.adpter.CenterLayoutManager
import com.jkcq.homebike.ride.history.bean.BarInfo
import com.jkcq.homebike.ride.history.bean.SportDetialBean
import com.jkcq.homebike.ride.history.calendar.TimeUtil
import com.jkcq.homebike.ride.history.calendar.WatchPopCalendarView
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.util.TimeUtils
import com.jkcq.homebike.share.DayShareActivity
import com.jkcq.util.DateUtil
import com.jkcq.util.MessageEvent
import com.jkcq.util.SiseUtil
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.dialog.dialog.BaseDialog
import kotlinx.android.synthetic.main.fragment_sport_bar.*
import kotlinx.android.synthetic.main.view_bike_sport_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_history_dis_count.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : BaseVMFragment<BikeDataViewModel>(), WatchPopCalendarView.MonthDataListen {


    var date: Int = 0
    var strDate: String = ""

    //柱状图的变量初始化
    var mDataList = mutableListOf<BarInfo>()
    var tempList = mutableListOf<BarInfo>()
    lateinit var mBarAdapter: BarAdapter
    lateinit var mCenterLayoutManager: CenterLayoutManager

    //运动详情的初始化
    var mSportDetialBean = mutableListOf<SportDetialBean>()
    lateinit var bikeSportDetailAdapter: BikeSportDetailAdapter


    val mBikeDataViewModel: BikeDataViewModel by lazy {

        createViewModel(
            BikeDataViewModel::class.java
        )
    }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")


    //日历
    private var calendarview: WatchPopCalendarView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = arguments!!.getInt("date")
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
                        DayShareActivity::class.java
                    )
                )
                intent.putExtra("date", strDate)
                startActivity(intent)

            }
            MessageEvent.calender -> if (userVisibleHint) {


                setPopupWindow(activity!!, tv_date)
                val time = (System.currentTimeMillis() / 1000).toInt()
                initDatePopMonthTitle()
                calendarview!!.setActiveC(TimeUtil.second2Millis(time))
                calendarview!!.setMonthDataListen(this@DayFragment)
                calendarview!!.setTimeInMillis(TimeUtil.second2Millis(time))
            }
        }
    }

    var mCurrentMessage: BarInfo? = null
    var mCurrentPosition = 0
    var currentPageNumber = 1


    fun setEmpView() {
        mDataList.clear()
        mBarAdapter.setEmptyView(R.layout.include_rope_empty_view)
        mBarAdapter.notifyDataSetChanged()
    }

    override fun startObserver() {
        mBikeDataViewModel.mHasDataMonthDate.observe(this, androidx.lifecycle.Observer {
            calendarview!!.setSummary(it!!)
        })
        mBikeDataViewModel.barInfoNodate.observe(this, androidx.lifecycle.Observer {
            setEmpView()
        })
        mBikeDataViewModel.barInfo.observe(this, androidx.lifecycle.Observer {

            tempList.clear()
            tempList.addAll(it)

            if (tempList.size == 0) {
                mDataList.clear()
                setEmpView()
            } else {
                mDataList.clear()
                var j = it.size
                Log.e("startObserver", "----------" + j)
                while (j < 5) {
                    mDataList.add(BarInfo())
                    j++
                }
                Log.e("startObserver", "----------" + mDataList.size)
                // mDataList.get(0).isSelect = true
                mDataList.addAll(tempList)
                Log.e("startObserver", "----------" + mDataList.size)
                mBarAdapter.notifyDataSetChanged()

            }


        })
        mBikeDataViewModel.sportEveryCountDetail.observe(this, androidx.lifecycle.Observer {
            mSportDetialBean.clear()
            mSportDetialBean.addAll(it)
            if (it.size > 0) {
                recycle_sport_detail.visibility = View.VISIBLE
                bikeSportDetailAdapter.notifyDataSetChanged()
            }

        })

        mBikeDataViewModel.sumSummerBean.observe(this, androidx.lifecycle.Observer {
            setItemvalue(it)
        })
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_sport_day_bar
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

        tv_date.text = strDate
        mBikeDataViewModel.getDeviceDailyBrief(
            strDate,
            "" + DeviceType.DEVICE_S003.value + "," + DeviceType.DEVICE_S005.value,
            mUserId
        )
        mBikeDataViewModel.getDeviceSummary(
            strDate,
            "" + DeviceType.DEVICE_S005.value + "," + DeviceType.DEVICE_S003.value,
            BikeConfig.BIKE_SPORT_DATE_DAY,
            mUserId
        )

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

    fun initBarRec() {

        mCenterLayoutManager = CenterLayoutManager(activity, RecyclerView.HORIZONTAL, true)
        recyclerview_sport.layoutManager = mCenterLayoutManager
        mBarAdapter = BarAdapter(mDataList, 5)
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

    fun initSportDetailRec() {
        recycle_sport_detail.visibility = View.INVISIBLE
        recycle_sport_detail.layoutManager = LinearLayoutManager(activity)
        bikeSportDetailAdapter = BikeSportDetailAdapter(mSportDetialBean)
        recycle_sport_detail.adapter = bikeSportDetailAdapter

        bikeSportDetailAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            var intent = Intent(activity, WebHitoryViewActivity::class.java)
            intent.putExtra(
                "lighturl",
                BikeConfig.detailUrlBean.light + "?exerciseId=" + mSportDetialBean.get(position).sportId + "&userId=" + mUserId
            )
            intent.putExtra(
                "darkurl",
                BikeConfig.detailUrlBean.dark + "?exerciseId=" + mSportDetialBean.get(position).sportId + "&userId=" + mUserId
            )
            intent.putExtra("title", activity!!.getString(R.string.sport_detail_title))
            startActivity(intent)

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

    /**
     * 日历代码
     */
    var mMenuViewBirth: BaseDialog? = null
    private var ivPreDate: ImageView? = null
    private var ivNextDate: ImageView? = null
    private var tvDatePopTitle: TextView? = null
    private var tvBackToay: TextView? = null


    fun setPopupWindow(context: Context, view: View?) {
        try {

            if (mMenuViewBirth != null && mMenuViewBirth!!.isShowing) {
                return
            }

            mMenuViewBirth = BaseDialog.Builder(context)
                .setContentView(R.layout.app_activity_watch_dem)
                .fullWidth()
                .setCanceledOnTouchOutside(true)
                .fromBottom(true)
                .setOnClickListener(R.id.tv_cancel,
                    DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
                .setOnClickListener(R.id.tv_determine,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.cancel()
                    })
                .show()


            val view_hide = mMenuViewBirth!!.findViewById<View>(R.id.view_hide)
            calendarview =
                mMenuViewBirth!!.findViewById<View>(R.id.calendar) as WatchPopCalendarView
            ivPreDate = mMenuViewBirth!!.findViewById<View>(R.id.iv_pre) as ImageView
            ivNextDate = mMenuViewBirth!!.findViewById<View>(R.id.iv_next) as ImageView
            tvDatePopTitle = mMenuViewBirth!!.findViewById<View>(R.id.tv_date) as TextView
            tvBackToay = mMenuViewBirth!!.findViewById<View>(R.id.tv_back_today) as TextView
            //popupWindowBirth.setHeight(DisplayUtils.dip2px(context, 150));
            view_hide!!.setOnClickListener { mMenuViewBirth!!.dismiss() }
            calendarview!!.setOnCellTouchListener(WatchPopCalendarView.OnCellTouchListener { cell -> /*if (cell.getStartTime() <= 0) {
                    Toast.makeText(BaseApp.getApp(),"没有数据", Toast.LENGTH_SHORT).show();
                    return;
                }
               */
                val stime = cell.startTime
                val dateStr = cell.dateStr
                if (TextUtils.isEmpty(dateStr)) {
                    return@OnCellTouchListener
                }
                val date = TimeUtils.stringToDate(dateStr)
                val mCurrentDate = TimeUtils.getCurrentDate()
                //未来的日期提示用户不可选
                if (!date.before(mCurrentDate)) {
                    // ToastUtils.showToast(UIUtils.getContext(), UIUtils.getString(R.string.select_date_error))
                    return@OnCellTouchListener
                }

                mMenuViewBirth!!.dismiss()
                //tv_update_time.setText(dateStr)

                val messageEvent = MessageEvent(MessageEvent.viewPageChage)
                messageEvent.obj = dateStr
                EventBus.getDefault().post(messageEvent)
                //mRopeSkippingPresenter.getDailyBrief(dateStr, TokenUtil.getInstance().getPeopleIdInt(BaseApp.instance))

            })
            tvBackToay!!.setOnClickListener(View.OnClickListener {
                mMenuViewBirth!!.dismiss()
                val dateStr = DateUtil.dataToString(Date(), "yyyy-MM-dd")
                // tv_update_time.setText(dateStr)

                val messageEvent = MessageEvent(MessageEvent.viewPageChage)
                messageEvent.obj = dateStr
                EventBus.getDefault().post(messageEvent)


                // mRopeSkippingPresenter.getDailyBrief(dateStr, TokenUtil.getInstance().getPeopleIdInt(BaseApp.instance))
            })

            ToastUtil.isFastDoubleClick()
            ivPreDate!!.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
                calendarview!!.previousMonth()
                calendarview!!.clearSummary()
                getLastMonthData()
                initDatePopMonthTitle()
            })
            ivNextDate!!.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
                calendarview!!.nextMonth()
                calendarview!!.clearSummary()
                getLastMonthData()
                initDatePopMonthTitle()
            })


            //获取当月月初0点时间戳,毫秒值
            val instance = Calendar.getInstance()
            instance[Calendar.DAY_OF_MONTH] = 1
            instance[Calendar.HOUR_OF_DAY] = 0
            instance[Calendar.MINUTE] = 0
            instance[Calendar.SECOND] = 0
            //向前移动一个月
//        getMonthData(instance);//获取当月的数据,主页已经获取
            instance.add(Calendar.MONTH, -1)
            //首次进入获取上一个月的数据,
            getMonthData(instance)
        } catch (e: Exception) {

        }

    }

    private fun getMonthData(instance: Calendar) {

        //mRopeSkippingPresenter.getsportDaysInMonth(TokenUtil.getInstance().getPeopleIdInt(BaseApp.instance), DateUtil.dateFormatyyMM.format(instance.time))
    }

    private val dateFormatMMDD = SimpleDateFormat("yyyy-MM")
    private fun getLastMonthData() {
        val calendar = calendarview!!.date
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        //向前移动一个月
        calendar.add(Calendar.MONTH, -1)
        getMonthData(calendar) //获取上月的数据
        calendar.add(Calendar.MONTH, 1)
    }

    private fun initDatePopMonthTitle() {
        val calendar = calendarview!!.date
        tvDatePopTitle!!.setText(dateFormatMMDD.format(calendar.time))
    }

    override fun getMontData(strYearAndMonth: String?) {
        //   TODO("Not yet implemented")
        mBikeDataViewModel.getDeviceExerciseDaysInMonth(
            "" + BikeConfig.BIKE_TYPE,
            strYearAndMonth!!,
            mUserId
        )
    }


}