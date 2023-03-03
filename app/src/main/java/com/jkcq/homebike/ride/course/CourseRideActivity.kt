package com.jkcq.homebike.ride.course

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jkcq.base.app.ActivityLifecycleController
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.CacheManager
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.bike.arithmetic.BikeUtil
import com.jkcq.homebike.bike.bean.RealDataBean
import com.jkcq.homebike.bike.observable.RealDataObservable
import com.jkcq.homebike.ble.bean.RankingBean
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel
import com.jkcq.homebike.ble.devicescan.hr.HrDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.hr.HrDeviceListAdapter
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.login.WebHitoryViewActivity
import com.jkcq.homebike.ride.freeride.CountTimer
import com.jkcq.homebike.ride.freeride.FreeRideViewModel
import com.jkcq.homebike.ride.freeride.dialog.BikeCompletyDialog
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.sceneriding.adpter.SceneRankingAdapter
import com.jkcq.homebike.ride.sceneriding.adpter.ViewPagerAdapter
import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean
import com.jkcq.homebike.ride.sceneriding.bean.SenceBeans
import com.jkcq.homebike.ride.util.BikeDataCache
import com.jkcq.homebike.ride.util.CourseUtil
import com.jkcq.homebike.ride.util.HeartRateConvertUtils
import com.jkcq.homebike.ride.util.SceneUtil
import com.jkcq.homebike.ride.view.AnimSporEndView
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import com.opensource.svgaplayer.SVGACallback
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import kotlinx.android.synthetic.main.activity_course_ride.*
import kotlinx.android.synthetic.main.activity_course_ride.imageView
import kotlinx.android.synthetic.main.activity_course_ride.imageViewpoint
import kotlinx.android.synthetic.main.activity_course_ride.itemview_consume_kcal
import kotlinx.android.synthetic.main.activity_course_ride.itemview_electric_kj
import kotlinx.android.synthetic.main.activity_course_ride.itemview_milage_km
import kotlinx.android.synthetic.main.activity_course_ride.itemview_power_w
import kotlinx.android.synthetic.main.activity_course_ride.iv_bike_conn_state
import kotlinx.android.synthetic.main.activity_course_ride.iv_guide_next
import kotlinx.android.synthetic.main.activity_course_ride.iv_guide_previous
import kotlinx.android.synthetic.main.activity_course_ride.iv_hr_device_tips_close
import kotlinx.android.synthetic.main.activity_course_ride.iv_option_pause
import kotlinx.android.synthetic.main.activity_course_ride.iv_ranking_hide
import kotlinx.android.synthetic.main.activity_course_ride.iv_sport_start
import kotlinx.android.synthetic.main.activity_course_ride.iv_sport_stop
import kotlinx.android.synthetic.main.activity_course_ride.layout_devicelist
import kotlinx.android.synthetic.main.activity_course_ride.layout_guide
import kotlinx.android.synthetic.main.activity_course_ride.layout_hr
import kotlinx.android.synthetic.main.activity_course_ride.layout_hr_device_close
import kotlinx.android.synthetic.main.activity_course_ride.layout_hr_device_high
import kotlinx.android.synthetic.main.activity_course_ride.layout_number
import kotlinx.android.synthetic.main.activity_course_ride.layout_option
import kotlinx.android.synthetic.main.activity_course_ride.layout_point
import kotlinx.android.synthetic.main.activity_course_ride.layout_ranking_detail
import kotlinx.android.synthetic.main.activity_course_ride.layout_user_ranking
import kotlinx.android.synthetic.main.activity_course_ride.noiseboardView
import kotlinx.android.synthetic.main.activity_course_ride.ranking_view
import kotlinx.android.synthetic.main.activity_course_ride.rec_char
import kotlinx.android.synthetic.main.activity_course_ride.recycle_device_list
import kotlinx.android.synthetic.main.activity_course_ride.recycle_ranking
import kotlinx.android.synthetic.main.activity_course_ride.resistance_view
import kotlinx.android.synthetic.main.activity_course_ride.timer
import kotlinx.android.synthetic.main.activity_course_ride.tv_bpm_util
import kotlinx.android.synthetic.main.activity_course_ride.tv_current_resistance
import kotlinx.android.synthetic.main.activity_course_ride.tv_current_rpm
import kotlinx.android.synthetic.main.activity_course_ride.tv_duration
import kotlinx.android.synthetic.main.activity_course_ride.tv_guide_count
import kotlinx.android.synthetic.main.activity_course_ride.tv_hide_user_ranking
import kotlinx.android.synthetic.main.activity_course_ride.tv_hr_close
import kotlinx.android.synthetic.main.activity_course_ride.tv_hr_con_state
import kotlinx.android.synthetic.main.activity_course_ride.tv_hr_value
import kotlinx.android.synthetic.main.activity_course_ride.tv_mode_title
import kotlinx.android.synthetic.main.activity_course_ride.tv_number
import kotlinx.android.synthetic.main.activity_course_ride.tv_scan_hr_device
import kotlinx.android.synthetic.main.activity_course_ride.tv_too_high
import kotlinx.android.synthetic.main.activity_course_ride.video_loader
import kotlinx.android.synthetic.main.activity_course_ride.view_page2
import kotlinx.android.synthetic.main.activity_free_ride.*
import kotlinx.android.synthetic.main.activity_pk_ride.*
import kotlinx.android.synthetic.main.item_scene_mine_ranking.*
import java.io.File
import java.util.*


class CourseRideActivity : BaseVMActivity<BikeDeviceConnectViewModel>(), java.util.Observer,
    SVGACallback {
    var mSceneNoFirst: Boolean by Preference(Preference.CourseFirst, true)


    private var path = ""

    //

    /**
     * 阻力详情数据
     */
    var slope = ""
    var dis = ""
    var name = "";
    var sceneId = "";
    var mResistanceIntervalBean = mutableListOf<ResistanceIntervalBean>()


    /**
     * 排行榜
     */


    var mRanklist = mutableListOf<RankingBean>()
    lateinit var mSceneRankingAdapter: SceneRankingAdapter

    //

    //lateinit var map: TreeMap<String, ExtendedBluetoothDevice>
    var mDeviceDataList = mutableListOf<ExtendedBluetoothDevice>()
    lateinit var mHrDeviceListAdapter: HrDeviceListAdapter


    var currentHr = 0
    var mDataList = mutableListOf<RealDataBean>()


    val mFreeRideViewModel: FreeRideViewModel by lazy {
        createViewModel(FreeRideViewModel::class.java)
    }

    val mHrDeviceConnectViewModel: HrDeviceConnectViewModel by lazy {
        createViewModel(
            HrDeviceConnectViewModel::class.java
        )

    }
    val mBikeDataViewModel: BikeDataViewModel by lazy {
        createViewModel(
            BikeDataViewModel::class.java
        )

    }


    fun initRankingRec() {
        recycle_ranking.layoutManager = LinearLayoutManager(this)
        mSceneRankingAdapter = SceneRankingAdapter(mRanklist)
        recycle_ranking.adapter = mSceneRankingAdapter

        mSceneRankingAdapter.setOnItemChildClickListener { adapter, view, position ->
            var rankingBean = mRanklist.get(position)


            if (rankingBean.userId.equals(mUserId)) {
                return@setOnItemChildClickListener
            }

            if (TextUtils.isEmpty(rankingBean.dataId)) {
                mBikeDataViewModel.praiseOther(mUserId, "1", sceneId, rankingBean.userId)
            } else {
                mBikeDataViewModel.upPraiseOther(rankingBean.dataId, rankingBean.userId)
            }


        }
    }

    val mDeviceScanViewModel: DeviceScanViewModel by lazy { createViewModel(DeviceScanViewModel::class.java) }
    val mBikeDeviceConnectViewModel: BikeDeviceConnectViewModel by lazy {
        createViewModel(
            BikeDeviceConnectViewModel::class.java
        )
    }
    lateinit var sceneBean: SenceBeans

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var start_resistance = 50
    var start_rpm = 50
    var current_resistance = 50;
    var current_rpm = 50;

    var connecetHr: ConnectDeviceDialog? = null
    fun initSportDetailRec() {
        mDeviceDataList.clear()
        mDeviceScanViewModel.stopLeScan()
        layout_devicelist.visibility = View.VISIBLE
        recycle_device_list.layoutManager = LinearLayoutManager(this)
        mHrDeviceListAdapter =
            HrDeviceListAdapter(mDeviceDataList)
        recycle_device_list.adapter = mHrDeviceListAdapter

        mHrDeviceListAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->

            mDeviceScanViewModel.stopLeScan()
            showConnetDialog()
            mHrDeviceConnectViewModel.conectHrDevice(mDeviceDataList.get(position).device)


        })
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                //连接超时
                1001 -> {
                    hideConnectDialog()
                    ToastUtil.showTextToast(
                        this@CourseRideActivity,
                        this@CourseRideActivity.getString(R.string.device_state_fail)
                    )

                }
            }
        }
    }

    fun hideConnectDialog() {
        handler.removeMessages(1001)
        if (connecetHr != null && connecetHr!!.isShowing) {
            connecetHr!!.dismiss()
            connecetHr = null

        }
    }

    fun showConnetDialog() {

        if (!AppUtil.isOpenBle()) {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton,
                BaseApp.sApplicaton.getString(R.string.openBle)
            )
            return
        }
        handler.removeMessages(1001)
        handler.sendEmptyMessageDelayed(1001, 10000)
        if (connecetHr != null && connecetHr!!.isShowing) {
            return
        }
        connecetHr = ConnectDeviceDialog(this, "");
        connecetHr?.show()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_course_ride
    }

    //name
    //dis
    //slope

    fun setResitance() {
        slope = intent.getStringExtra("slope")
        dis = intent.getStringExtra("dis")
        name = intent.getStringExtra("name");
        sceneId = intent.getStringExtra("sceneId");

        val hms: DateUtil.HMS = DateUtil.getHMSFromMillis(dis.toInt() * 1000L)
        tv_duration.text = String.format(
            "%02d:%02d:%02d",
            hms.getHour(),
            hms.getMinute(),
            hms.getSecond()
        )
        var strdis = ""
        if (BikeConfig.userCurrentUtil == BikeConfig.METRIC_UNITS) {

            strdis = SiseUtil.disUnitCoversion(
                dis,
                BikeConfig.userCurrentUtil
            ) + this.resources.getString(R.string.sport_dis_unitl_km)
        } else {
            strdis = SiseUtil.disUnitCoversion(
                dis,
                BikeConfig.userCurrentUtil
            ) +
                    this.resources.getString(R.string.sport_dis_unitl_mile)
        }

        tv_mode_title.text = name


        var list = CourseUtil.getLineBeanList(CourseUtil.splitSemicolonStr(slope))
        mResistanceIntervalBean.addAll(
            CourseUtil.getResisteanceList(
                list,
                dis.toInt()
            )
        )

        if (mResistanceIntervalBean.size > 0) {
            start_resistance = mResistanceIntervalBean.get(0).getmResistances()
            current_resistance = mResistanceIntervalBean.get(0).getmResistances()
            setTargetValue(mResistanceIntervalBean.get(0).getmRpm())
            calCurrentResisAndRpm(current_resistance.toFloat(), 0f)
        }
        start_rpm = 0
        current_rpm = 0
        setCurrentResitance()
        setrpmRealValue(current_rpm)
        resistance_view.setdata(mResistanceIntervalBean, dis.toInt())
        rec_char.setSumDis(dis.toInt())
    }


    fun setTargetValue(value: Int) {

        noiseboardView.setTargetSpeed(value)

    }

    fun rotationPoint(value: Int) {
        val fvalue = 270.0f / 200
        val pointerDegree = (135 + fvalue * value)
        var rotation = layout_point.getRotation();
        // if(pointerDegree-rotation)
        layout_point.rotation = pointerDegree;
        Log.e(
            "setTargetValue",
            "setrpmRealValue=" + start_rpm + "value=" + "pointerDegree=" + pointerDegree + "rotation=" + rotation
        )
    }

    fun setrpmRealValue(value: Int) {

        Log.e("setrpmRealValue", "setrpmRealValue=" + start_rpm + "value=" + value)

        noiseboardView.setTargetSpeed(start_rpm)
        noiseboardView.setRealTimeValue(value.toFloat())
        tv_current_rpm.text = "" + value
    }

    override fun initView() {
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //TODO("Not yet implemented")
    }

    var commonDialog: CommonDialog? = null


    fun isGuider() {

        BikeConfig.sBikeBean = null
        BikeConfig.isUpgradeSuccess = false;
        initBle()
        RealDataObservable.getInstance().addObserver(this)
        layout_devicelist.visibility = View.GONE
        //  initPlayer()
        if (mSceneNoFirst) {
            tv_guide_count.setTag("1")
            layout_guide.visibility = View.VISIBLE
            view_page2.adapter = ViewPagerAdapter(this, false)
            view_page2.setCurrentItem(0, false)
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/4"
            iv_guide_previous.visibility = View.GONE

        } else {

            //  view_page2.isUserInputEnabled = false
            startScence()
        }

        view_page2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                try {
                    tv_guide_count.text = "" + (position + 1) + "/4"
                    tv_guide_count.setTag("" + (position + 1))
                    if (position + 1 == 1) {
                        iv_guide_previous.visibility = View.GONE
                    }else{
                        iv_guide_previous.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {

                }

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun startScence() {
        mSceneNoFirst = false
        layout_guide.visibility = View.GONE
        startNumber()
        setResitance()
        initRankingRec()
        mBikeDataViewModel.getSceneRankings(sceneId, "1")
        path = intent.getStringExtra("path")
        val file = File(
            FileUtil.getVideoCorseDir().toString() + path
        )
        video_loader.setVideoURI(Uri.parse(file.path))
        startVideo()
        video_loader.setOnPreparedListener {
            it.setScreenOnWhilePlaying(true);
            video_loader.start()
            if (BikeConfig.isPause || isFistrCon || BikeConfig.speed == 0) {
                handler.postDelayed(Runnable {
                    video_loader.pause()
                }, 50)
                isFistrCon = false
            }
            it.isLooping = true
        }
    }


    override fun initEvent() {
        layout_number.setOnClickListener {

        }
        layout_option.setOnClickListener {

        }
        layout_devicelist.setOnClickListener {

        }
        iv_ranking_hide.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            rankingRecShow(false)
        }

        layout_user_ranking.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            rankingRecShow(true)
        }

        iv_guide_previous.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            view_page2.setCurrentItem(view_page2.currentItem - 1)
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/4"
            tv_guide_count.setTag("" + (view_page2.currentItem + 1))
            if (view_page2.currentItem + 1 == 1) {
                iv_guide_previous.visibility = View.GONE
            }

        }
        iv_guide_next.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            iv_guide_previous.visibility = View.VISIBLE
            if (tv_guide_count.getTag().equals("4")) {
                startScence()
            }
            view_page2.setCurrentItem(view_page2.currentItem + 1)
            tv_guide_count.setTag("" + (view_page2.currentItem + 1))
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/4"
        }

        //暂停
        iv_option_pause.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            pauseOption()
        }
        //开始
        iv_sport_start.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            BikeConfig.isPause = false
            mFreeRideViewModel.reStartFreeRide()
            video_loader.start()
            mFreeRideViewModel.reStartFreeRide()
            layout_option.visibility = View.GONE


        }
        tv_hr_close.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            layout_devicelist.visibility = View.GONE

            if (BikeConfig.isPause) {
                iv_sport_start.setImageResource(R.mipmap.icon_sport_big_pause)
            } else {
                iv_sport_start.setImageResource(R.mipmap.icon_sport_big_pause)
            }

        }

        layout_hr.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            if (BikeConfig.HRConnState != BikeConfig.BIKE_CONN_SUCCESS) {
                initSportDetailRec()
                mDeviceScanViewModel.startLeScan()
                layout_hr_device_close.visibility = View.GONE
            }

            // startActivity(Intent(this@FreeRideActivity, HrDeviceScanActivity::class.java))
        }

        iv_hr_device_tips_close.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            layout_hr_device_close.visibility = View.GONE
        }
        tv_scan_hr_device.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            initSportDetailRec()
            mDeviceScanViewModel.startLeScan()
        }

        //结束运动
        iv_sport_stop.setonSportEndViewOnclick(object : AnimSporEndView.OnSportEndViewOnclick {
            override fun onStartButton() {
            }

            override fun onProgressCompetly() {
                if (!BikeConfig.isPause) {
                    return
                }
                layout_option.visibility = View.GONE
                endSport()
                //DialogUtil.showStandardDialog(this@FreeRideActivity,)
            }
        })
    }


    fun endSport() {
        commonDialog =
            CommonDialog(
                this@CourseRideActivity,
                "",
                this@CourseRideActivity.resources.getString(R.string.end_bike_sport),
                "",
                ""
            )
        commonDialog!!.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
                // ShowResultDialogUtil.hidDialog(commonDialog)
                BikeConfig.isPause = false
                mFreeRideViewModel.reStartFreeRide()
                layout_option.visibility = View.GONE
            }

            override fun onSureOnclick() {
                updateBikeData(true)
            }
        })
        commonDialog!!.show()
    }

    var yesOrNoDialog: CommonDialog? = null

    fun showToShortDisDialog(dialogShowTwobutton: Boolean) {
        if (yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {
            return
        }
        var endValue = this.resources.getString(R.string.sport_end)
        if (dialogShowTwobutton) {
            endValue = this.resources.getString(R.string.sport_end)
        } else {
            endValue = this.resources.getString(R.string.dialog_ok)
        }



        yesOrNoDialog = CommonDialog(
            this,
            "",
            this.resources.getString(R.string.sport_dis_too_short_tips),
            this.resources.getString(R.string.sport_continue),
            endValue,
            dialogShowTwobutton

        )
        yesOrNoDialog?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
                //  TODO("Not yet implemented")

                if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_SUCCESS) {
                    BikeConfig.isPause = false
                    startVideo()
                    mFreeRideViewModel.reStartFreeRide()
                } else {
                    ToastUtil.showTextToast(
                        this@CourseRideActivity,
                        this@CourseRideActivity.resources.getString(R.string.device_state_fail)
                    )
                }
            }

            override fun onSureOnclick() {
                ActivityLifecycleController.finishAllActivity("MainActivity")
            }
        })
        yesOrNoDialog?.show()

    }


    fun autoendUpdateBikeData() {
        updateBikeData(false)
    }

    fun updateBikeData(dialogShowTwobutton: Boolean) {

        if (BikeConfig.isUpgradeSuccess) {
            return
        }
        pauseVideo()
        mHrDeviceConnectViewModel.disconectDevice()
        //                    Logger.myLog("dateStr == " + (year + "-" + formatTwoStr(non) + "-" + formatTwoStr(day)) + "heartRateList == " + heartRateList.toString() + " heartRateList.size == " + heartRateList.size() + " AVG == " + Math.round(sum / (float) size));

        //骑行类型，0：自由骑行，1：线路骑行，2：PK骑行
        mFreeRideViewModel.endFreeRide()
        startBg(0)
        if (BikeConfig.sBikeBean == null) {
            showToShortDisDialog(dialogShowTwobutton)
            return

        } else {
            Log.e("dis", "" + BikeConfig.sBikeBean.dis)
            if (BikeConfig.sBikeBean.dis < 200f) {
                showToShortDisDialog(dialogShowTwobutton)
                return
            }
            //上传单位卡
            var calorie: Int = BikeConfig.sBikeBean.cal.toInt()
            var cyclingTime: Long = System.currentTimeMillis()
            var cyclingType: Int = BikeConfig.BIKE_COURSE
            //单位是米
            var distance: Int = BikeConfig.sBikeBean.dis.toInt()
            var duration: Int = BikeConfig.sBikeBean.duration
            var heartRateArray = BikeConfig.sBikeBean.hrlist
            var powerArray = BikeConfig.sBikeBean.powList
            //单位是 w
            var powerGeneration: Int = BikeConfig.sBikeBean.light.toInt()
            var scenarioId: String = sceneId
            var steppedFrequencyArray = BikeConfig.sBikeBean.rpmList




            mBikeDataViewModel.upgradeCyclingRecords(
                true,
                dis.toInt(),
                mUserId,
                BikeConfig.BIKE_TYPE,
                calorie,
                cyclingTime,
                cyclingType,
                distance,
                duration,
                heartRateArray,
                powerArray,
                powerGeneration,
                scenarioId,
                steppedFrequencyArray
            )

        }
    }

    fun startNumber() {

        Log.e("startNumber", "startNumber")
        BikeConfig.isPause = true
        val repeatCount = 2 //定义重复字数（执行动画1次 + 重复动画4次 = 公共5次）


        // 设置透明度渐变动画

        // 设置透明度渐变动画
        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = 910 //动画持续时间(定义900~1000,也就是1秒左右)

        alphaAnimation.repeatMode = Animation.RESTART
        alphaAnimation.repeatCount = repeatCount
        alphaAnimation.interpolator = LinearInterpolator()
        // 设置缩放渐变动画
        // 设置缩放渐变动画
        val scaleAnimation = ScaleAnimation(
            0.5f,
            1f,
            0.5f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnimation.duration = 910 //动画持续时间(定义900~1000,也就是1秒左右)

        scaleAnimation.repeatMode = Animation.RESTART
        scaleAnimation.repeatCount = repeatCount
        scaleAnimation.interpolator = LinearInterpolator()

        val animationSet = AnimationSet(false)
        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)


        timer.startAnimation(animationSet)
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            var count = repeatCount + 1 // 加1为第一次要显示的数字 5
            override fun onAnimationStart(animation: Animation) { // 此方法执行1次
                layout_number.visibility = View.VISIBLE
                timer.setVisibility(View.VISIBLE)
                timer.setText("" + count) //设置显示的数字
                count--
            }

            override fun onAnimationEnd(animation: Animation) { // 此方法执行1次

                // 动画结束 隐藏控件
                timer.setVisibility(View.GONE)
                startBg(0)
                mFreeRideViewModel.startFreeRide()
                layout_number.visibility = View.GONE
                BikeConfig.isPause = false
            }

            override fun onAnimationRepeat(animation: Animation) { // 此方法执行4次（repeatCount值）
                timer.setText("" + count)
                count--
            }
        })


    }


    fun startBg(rpm: Int) {

        if (rpm != 0) {
        } else {
        }

    }

    override fun onBackPressed() {
        /* super.onBackPressed()
         mHrDeviceConnectViewModel.disconectDevice()*/

    }

    var filter: IntentFilter? = null

    fun initBle() {
        filter = IntentFilter()
        filter!!.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(broadcastReceiver, filter)
        mHrDeviceConnectViewModel.setCallBack(this)
        mBikeDeviceConnectViewModel.setCallBack(this)
        mBikeDeviceConnectViewModel.sendQuitData(current_resistance)
        setHrValue(0)
        when (BikeConfig.HRConnState) {
            BikeConfig.BIKE_CONN_DISCONN -> {
                tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device_discon)
            }
            BikeConfig.BIKE_CONN_SUCCESS -> {
                tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device)
                layout_hr_device_close.visibility = View.GONE
                layout_devicelist.visibility = View.GONE
                mDeviceDataList.clear()
            }
        }
    }

    var parser: SVGAParser? = null
    var parserpoint: SVGAParser? = null
    override fun initData() {

        parser = SVGAParser(this@CourseRideActivity)
        imageView.callback = this

        if (CacheManager.mUserInfo != null) {
            birthday = CacheManager!!.mUserInfo!!.birthday
            age = CacheManager.getAge(birthday)
            sex = CacheManager!!.mUserInfo!!.getGender()
        }


        imageView.loops = (1)

        parserpoint = SVGAParser(this@CourseRideActivity)
        imageViewpoint.loops = 1
        imageViewpoint.callback = (object : SVGACallback {
            override fun onFinished() {
                Log.e("imageViewpoint", "imageViewpoint onFinished")
                // TODO("Not yet implemented")
            }

            override fun onPause() {
                Log.e("imageViewpoint", "imageViewpoint onPause")
                // TODO("Not yet implemented")
            }

            override fun onRepeat() {
                //  TODO("Not yet implemented")
            }

            override fun onStep(frame: Int, percentage: Double) {
                // TODO("Not yet implemented")
            }

        })


        //TODO("Not yet implemented")
        // showCountDownPopWindow()

        BikeConfig.disUnitBike(itemview_milage_km.getUnitText(), this)
        BikeConfig.isPause = false
        isGuider()
        setHrValue(0)
    }


    fun statSVGaPoint(name: String) {
        imageViewpoint.visibility = View.VISIBLE
        if (imageViewpoint.isAnimating) {
            return
        }
        parser?.parse(name, object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                var drawable = SVGADrawable(videoItem);
                imageViewpoint.setImageDrawable(drawable);
                imageViewpoint.startAnimation();
            }

            override fun onError() {
            }
        })
    }

    fun starSVGa(name: String) {
        imageView.visibility = View.VISIBLE
        parser?.parse(name, object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                var drawable = SVGADrawable(videoItem);
                imageView.setImageDrawable(drawable);
                imageView.startAnimation();
            }

            override fun onError() {
            }
        })
    }

    var currentId = "";

    fun setItemvalue(summerBean: Summary) {


        Log.e("setItemvalue", "" + summerBean)
        currentId = summerBean.upgradeId

        val dis =
            SiseUtil.disUnitCoversion(summerBean.totalDistance, BikeConfig.userCurrentUtil)
        val cal = SiseUtil.calCoversion(summerBean.totalCalorie)
        val min = SiseUtil.timeCoversionMin(summerBean.totalDuration)
        var dialog = BikeCompletyDialog(this, dis, min, cal, true)
        dialog.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
                // TODO("Not yet implemented")
                mHrDeviceConnectViewModel.disconectDevice()
                ActivityLifecycleController.finishAllActivity("MainActivity")
            }

            override fun onSureOnclick() {
                mHrDeviceConnectViewModel.disconectDevice()
                //https://test.gateway.spinning.fitalent.com.cn/spinning-h5/#/recordDetails?exerciseId=1334702675943280642&userId
                //TODO("Not yet implemented")
                var intent = Intent(this@CourseRideActivity, WebHitoryViewActivity::class.java)
                intent.putExtra(
                    "lighturl",
                    BikeConfig.detailUrlBean.light + "?exerciseId=" + currentId + "&userId=" + mUserId
                )
                intent.putExtra(
                    "darkurl",
                    BikeConfig.detailUrlBean.dark + "?exerciseId=" + currentId + "&userId=" + mUserId
                )
                intent.putExtra(
                    "title",
                    this@CourseRideActivity.getString(R.string.sport_detail_title)
                )
                startActivity(intent)

                ActivityLifecycleController.finishAllActivity("MainActivity")
                //跳转到详情页面
            }
        })
        dialog.show()
    }

    var curIndex = 0
    override fun onFinished() {
        imageView.visibility = View.GONE
        count = 0
        // TODO("Not yet implemented")
    }

    fun pauseOption() {
        if (imageViewpoint != null && imageViewpoint.isAnimating) {
            imageViewpoint.stopAnimation()
        }
        if (imageView != null && imageView.isAnimating) {
            imageView.stopAnimation();
        }
        BikeConfig.isPause = true
        mFreeRideViewModel.endFreeRide()
        pauseVideo()
        startBg(0)
        if (commonDialog != null && commonDialog!!.isShowing || yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {

        } else {
            layout_option.visibility = View.VISIBLE
        }
    }


    override fun onPause() {
        super.onPause()

        curIndex = video_loader.getCurrentPosition();
        mFreeRideViewModel.endFreeRide()

        pauseOption()
    }

    override fun onRepeat() {
        // TODO("Not yet implemented")
    }

    override fun onStep(frame: Int, percentage: Double) {
        // TODO("Not yet implemented")
    }

    var isFistrCon = true;

    override fun onResume() {
        super.onResume()
        Log.e("onResume", "onResum")
        if (!isFistrCon) {

            if (video_loader.isPlaying) {

            } else {
                video_loader.seekTo(curIndex);

                if (BikeConfig.isPause) {

                } else {
                    startVideo()
                    mFreeRideViewModel.reStartFreeRide()
                }
            }
        }

    }


    fun startVideo() {

        Log.e("startVideo", "startVideo")
        if (video_loader.isPlaying) {
            return
        }
        video_loader.start()
        video_loader.requestFocus();

    }

    fun pauseVideo() {
        Log.e("pauseVideo", "pauseVideo")
        if (video_loader.isPlaying) {
            video_loader.pause()
        }
    }

    override fun onStop() {
        super.onStop()

        //mBikePresenter.unRegisterCallback(mRoadCallBack);

        /* Log.e("mediaPlayer onStop", "" + mediaPlayer!!.playerState)
         mediaPlayer!!.pause()
         Log.e("mediaPlayer onStop", "" + mediaPlayer!!.playerState)*/
    }

    var starCal = 0
    var currentUserRanking: RankingBean? = null


    fun updateRankinglist(cal: Float) {
        if (currentUserRanking == null) {

        } else {

            if (currentUserRanking?.totalCalorie == starCal + cal.toInt()) {
                return
            }

            currentUserRanking?.totalCalorie =
                starCal + cal.toInt()
            mRanklist.forEach {
                if (currentUserRanking?.userId.equals(it.userId)) {
                    it?.totalCalorie = currentUserRanking!!.totalCalorie
                }
            }
            Collections.sort(mRanklist)
            tv_ranking.text = "" + currentUserRanking?.rankingNo
            tv_hide_user_ranking.text = "" + currentUserRanking?.rankingNo
            val strCal =
                SiseUtil.calCoversion(currentUserRanking?.totalCalorie.toString())
            tv_mine_cal.text =
                strCal + this.resources.getString(R.string.sport_cal_unitl)
            var i = 0
            while (i < mRanklist.size) {
                mRanklist.get(i).rankingNo = i + 1
                if (mRanklist.get(i).userId.equals(mUserId)) {
                    currentUserRanking?.rankingNo = i + 1
                }
                i++
            }
            updateRanking()

        }


    }

    fun updateRanking() {
        try {
            mSceneRankingAdapter.notifyDataSetChanged()
        } catch (e: Exception) {

        }
    }


    var isUpdate = false;
    override fun startObserver() {
        mBikeDataViewModel.mPaiseBean.observe(this, Observer {
            isUpdate = true
            var paiseBean = it
            mRanklist.forEach {
                if (it.userId.equals(paiseBean.userId)) {
                    it.dataId = paiseBean.dataId
                    it.praiseNums = paiseBean.praiseNums
                    it.isWhetherPraise = paiseBean.isWhetherPraise
                }
            }
            updateRanking()
            isUpdate = false
        })

        mBikeDataViewModel.myRankingBean.observe(this, Observer {

            tv_mine_name.text = it.nickName

            if (it.isWhetherPraise) {
                iv_mine_praise.setImageResource(R.mipmap.icon_like_press)
            } else {
                iv_mine_praise.setImageResource(R.mipmap.icon_like_nor)
            }
            tv_mine_praise.text = it.praiseNums
            LoadImageUtil.getInstance()
                .load(this, it.headUrl, iv_mine_head, R.mipmap.friend_icon_default_photo)

            if (!mRanklist.contains(it)) {
                mRanklist.add(it)
            }
            Collections.sort(mRanklist)

            var i = 0
            while (i < mRanklist.size) {
                mRanklist.get(i).rankingNo = i + 1
                if (mRanklist.get(i).userId.equals(mUserId)) {

                    if (currentUserRanking == null) {
                        currentUserRanking = it;
                        starCal = it.totalCalorie
                    }
                    currentUserRanking?.rankingNo = i + 1
                }
                i++
            }
            currentUserRanking?.praiseNums = it.praiseNums;
            currentUserRanking?.isWhetherPraise = it.isWhetherPraise;
            currentUserRanking?.totalCalorie = it.totalCalorie

            tv_ranking.text = "" + currentUserRanking?.rankingNo
            tv_hide_user_ranking.text = "" + currentUserRanking?.rankingNo
            val strCal = SiseUtil.calCoversion((it.totalCalorie).toString())
            tv_mine_cal.text = strCal + this.resources.getString(R.string.sport_cal_unitl)
            updateRanking()
        })

        mBikeDataViewModel.mRankingSumNumber.observe(this, Observer {
            tv_number.text = String.format(
                this.resources.getString(R.string.sport_scene_use),
                "" + it
            )
        })
        //当前场景的排名列表
        mBikeDataViewModel.mRankingBeans.observe(this, Observer {


            mRanklist.clear()
            mRanklist.addAll(it)
            updateRanking()

        })


        //上传数据成功

        mBikeDataViewModel.updateBikeBean.observe(this, Observer {
            BikeConfig.sBikeBean = null
            BikeConfig.isUpgradeSuccess = true;
            setItemvalue(it)
        })
        mBikeDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {

            Log.e("startObserver", "mBikeDeviceConnectViewModel.mDeviceConnState")
            when (it) {

                BikeConfig.BIKE_CONN_DISCONN -> {
                    if (BikeConfig.isOpenBle) {
                        coutTime =
                            CountTimer(15000, object : CountTimer.OnCountTimerListener {
                                override fun onCountTimerChanged(millisecond: Long) {
                                    //TODO("Not yet implemented")
                                    if (BikeConfig.isOpenBle) {
                                        if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_DISCONN) {
                                            BikeConfig.isCanRecon = false;
                                            updateBikeData(false)

                                        }
                                    }

                                }
                            })

                        coutTime?.start()
                        iv_bike_conn_state.setImageResource(R.mipmap.icon_bike_device_discon)
                    }
                }
                BikeConfig.BIKE_CONN_SUCCESS -> {
                    iv_bike_conn_state.setImageResource(R.mipmap.icon_bike_device)
                }
            }
        })

        mBikeDeviceConnectViewModel.mRealData.observe(this, Observer
        {

            Log.e("mRealData", it.toString())
            mDataList.add(it)
        })


        mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer
        {
            LogUtil.e("startLeScan6")
            it.forEach {
                if (!TextUtils.isEmpty(it.name)) {
                    if (!it.name.contains(DeviceType.DEVICE_S003.name) || !it.name.contains(
                            DeviceType.DEVICE_S005.name
                        )
                    ) {
                        if (!mDeviceDataList.contains(it)) {
                            mDeviceDataList.add(it)
                        }
                    }
                }
            }
            try {
                Collections.sort(mDeviceDataList)
            } catch (e: Exception) {

            } finally {
                mHrDeviceListAdapter.notifyDataSetChanged()
            }


        })
        mHrDeviceConnectViewModel.mDeviceConnState.observe(this, Observer
        {
            when (it) {
                BikeConfig.BIKE_CONN_DISCONN -> {
                    hideConnectDialog()
                    tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device_discon)
                }
                BikeConfig.BIKE_CONN_SUCCESS -> {
                    hideConnectDialog()
                    tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device)
                    layout_hr_device_close.visibility = View.GONE
                    layout_devicelist.visibility = View.GONE
                    mDeviceDataList.clear()
                }
            }

        })
        mHrDeviceConnectViewModel.mDeviceHrValue.observe(this, Observer
        {
            Log.e("mDeviceHrValue", "" + it)
            if (!BikeConfig.isPause) {
                setHrValue(it)
                currentHr = it
            }
        })


        mFreeRideViewModel.mTimer.observe(this, Observer
        {
            /* BikeConfig.speed = 5000
             BikeConfig.power = 2000*/

            var hrValue = 0;
            try {
                hrValue = currentHr
                currentHr = 0;
                mDataList.clear()
                itemview_power_w.setValue("" + BikeConfig.power)
                if (BikeConfig.speed == 0 && BikeConfig.power == 0) {
                    BikeUtil.getGlobalBikebean(
                        0,
                        0,
                        it,
                        hrValue
                    )
                    if (BikeConfig.sBikeBean != null) {
                        calCurrentResisAndRpm(BikeConfig.sBikeBean.dis, 0f)
                    }
                    setrpmRealValue(0)
                    startBg(0)
                } else {
                    BikeUtil.getGlobalBikebean(
                        BikeConfig.power,
                        BikeConfig.speed,
                        it,
                        hrValue
                    )

                    BikeConfig.speed = 0
                    BikeConfig.power = 0
                    //如果距离大于目标距离就提示结束对话框


                    if (BikeConfig.sBikeBean != null) {
                        updateRankinglist(BikeConfig.sBikeBean.cal)
                        if (BikeConfig.sBikeBean.dis >= 200) {
                            BikeConfig.sBikeBean.sceneId = sceneId
                            BikeDataCache.saveBike(this, "course", BikeConfig.sBikeBean)
                        }
                        setItemValue(
                            BikeConfig.sBikeBean.dis,
                            BikeConfig.sBikeBean.cal,
                            BikeConfig.sBikeBean.light.toDouble(),
                            BikeConfig.sBikeBean.tzSpeed
                        )

                    } else {

                        setItemValue(
                            0f,
                            0f,
                            0.0, 0f
                        )
                    }
                }

                if (isHigh) {
                    playHeartbeatAnimation()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {

                //这里需要显示倒计时
                resistance_view.setcurrentDis(it.toFloat() / 1000)
                rec_char.setcurrentDis(it.toFloat() / 1000)
                val hms: DateUtil.HMS = DateUtil.getHMSFromMillis(dis.toInt() * 1000 - it)
                tv_duration.text = String.format(
                    "%02d:%02d:%02d",
                    hms.getHour(),
                    hms.getMinute(),
                    hms.getSecond()
                )


                if (video_loader.isPlaying) {

                } else {
                    if (!BikeConfig.isPause) {
                        startVideo()
                    }
                }
                if (BikeConfig.sBikeBean == null) {
                    calCurrentResisAndRpm((it / 1000).toFloat(), 0f)
                } else {
                    calCurrentResisAndRpm((it / 1000).toFloat(), BikeConfig.sBikeBean.tzSpeed)
                }




                if ((dis.toInt() * 1000 - it) == 0L) {
                    autoendUpdateBikeData()
                }
            }


        })
    }

    var coutTime: CountTimer? = null;
    fun setItemValue(dis: Float, cal: Float, jk: Double, rpm: Float) {

        SceneUtil.calculateMatchLeverWithCurrentCadence(current_resistance, rpm.toInt())
        val strdis = SiseUtil.disUnitCoversion(dis.toString(), BikeConfig.userCurrentUtil)
        val strCal = SiseUtil.calCoversion(cal.toString())
        val power = SiseUtil.powerCoversion(jk.toString())

        itemview_milage_km.setValue(strdis)
        itemview_consume_kcal.setValue(strCal)
        itemview_electric_kj.setValue(power)

        setrpmRealValue(rpm.toInt())
        startBg(rpm.toInt())


    }

    var birthday = "";
    var age = 18
    var sex = "Female"
    var isHigh = false
    fun setHrValue(value: Int) {

        try {
            Log.e("isHigh", "isHigh=1")
            isHigh = HeartRateConvertUtils.hrValueColor(
                value,
                HeartRateConvertUtils.getMaxHeartRate(age, sex),
                tv_hr_value,
                tv_bpm_util,
                tv_too_high
            )
            Log.e("isHigh", "isHigh=" + isHigh)
            if (isHigh) {
                tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device_high)
                layout_hr_device_high.visibility = View.VISIBLE
            } else {

                when (BikeConfig.HRConnState) {
                    BikeConfig.BIKE_CONN_DISCONN -> {
                        tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device_discon)
                    }
                    BikeConfig.BIKE_CONN_SUCCESS -> {
                        tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device)
                        layout_hr_device_close.visibility = View.GONE
                        layout_devicelist.visibility = View.GONE
                        mDeviceDataList.clear()
                    }
                }
                layout_hr_device_high.visibility = View.INVISIBLE
            }
            tv_hr_value.text = "" + value
            if (isHigh) {
                //  playHeartbeatAnimation()
            } else {
                // playHeartbeatAnimation()
            }

        } catch (e: Exception) {
            Log.e("setHrValue", e.toString())
        }

        if (value > 30) {
            // itemview_hr_bpm.setValue("" + value)
            tv_hr_value.text = ("" + value)
        } else {
            // itemview_hr_bpm.setValue("---")
            tv_hr_value.text = "---"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (coutTime != null) {
            coutTime?.stop()
        }
        mHrDeviceConnectViewModel.disconectDevice()
        if (yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {
            yesOrNoDialog!!.dismiss()
        }
        video_loader.stopPlayback();
        mFreeRideViewModel.endFreeRide()
        RealDataObservable.getInstance().deleteObserver(this)
        unregisterReceiver(broadcastReceiver)
    }

    override fun update(o: Observable?, arg: Any?) {
        Log.e("update", "update")
        if (o is RealDataObservable) {
            if (arg is RealDataBean) {
                mDataList.add(arg)
            }
        }
    }


    private
    val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state =
                    intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.STATE_OFF
                    )
                Log.e(
                    "BleService",
                    "ACTION_STATE_CHANGED" + state + BluetoothAdapter.STATE_ON
                )
                if (state == BluetoothAdapter.STATE_ON) {

                } else if (state == BluetoothAdapter.STATE_OFF) {
                    BikeConfig.isOpenBle = false;
                    BikeConfig.isCanRecon = false;
                    iv_bike_conn_state.setImageResource(R.mipmap.icon_bike_device_discon)
                    tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device_discon)
                    setHrValue(0)
                    updateBikeData(false)
                    //弹出失败对话框

                }
            }
        }
    }

    /**
     * 视频播放代码
     */


    var count = 0

    fun calCurrentResisAndRpm(dis: Float, rpm: Float) {

        mResistanceIntervalBean.forEach {

            Log.e("mResistanceIntervalBean", "" + it + "dis" + dis)
            if (dis >= it.getmIntervalStart() && dis <= it.getmIntervalEnd()) {
                current_resistance = it.getmResistances();
                current_rpm = it.getmRpm()
            }
        }

        rotationPoint(rpm.toInt())
        if (count == 5) {
            var svagName = SceneUtil.calculateMatchLeverWithCurrentCadence(rpm.toInt(), current_rpm)

            if (TextUtils.isEmpty(svagName)) {
                count = 0
            } else {
                starSVGa(svagName)


            }
        }
        var pointName = SceneUtil.calculatePoint(rpm.toInt(), current_rpm)
        if (TextUtils.isEmpty(pointName)) {
            imageViewpoint.stopAnimation()
        } else {
            statSVGaPoint(pointName)
        }
        count++
        Log.e(
            "mResistanceIntervalBean",
            "start_resistance=" + start_resistance + "current_resistance=" + current_resistance
        )
        Log.e(
            "mResistanceIntervalBean",
            "start_rpm=" + start_rpm + "current_rpm=" + current_rpm
        )

        if (start_resistance != current_resistance) {
            start_resistance = current_resistance;
            setCurrentResitance()
        }
        if (start_rpm != current_rpm) {
            start_rpm = current_rpm;
            setTargetValue(start_rpm)

        }

    }

    fun setCurrentResitance() {


        tv_current_resistance.text = "" + current_resistance
        mBikeDeviceConnectViewModel.sendQuitData(start_resistance)
    }

    fun rankingRecShow(isShow: Boolean) {
        if (isShow) {
            layout_user_ranking.visibility = View.GONE
            ranking_view.visibility = View.GONE
            iv_ranking_hide.visibility = View.VISIBLE
            layout_ranking_detail.visibility = View.VISIBLE
        } else {
            layout_user_ranking.visibility = View.VISIBLE
            ranking_view.visibility = View.VISIBLE
            iv_ranking_hide.visibility = View.GONE
            layout_ranking_detail.visibility = View.GONE
        }

    }


    private fun playHeartbeatAnimation() {
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(
            ScaleAnimation(
                1.0f, 1.5f, 1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f
            )
        )
        animationSet.addAnimation(AlphaAnimation(1.0f, 0.4f))
        animationSet.duration = 200
        animationSet.interpolator = AccelerateInterpolator()
        animationSet.fillAfter = true
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(
                    ScaleAnimation(
                        1.5f, 1.0f, 1.5f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    )
                )
                animationSet.addAnimation(AlphaAnimation(0.4f, 1.0f))
                animationSet.duration = 600
                animationSet.interpolator = DecelerateInterpolator()
                animationSet.fillAfter = false
                // 实现心跳的View
                tv_hr_con_state.startAnimation(animationSet)
            }
        })
        // 实现心跳的View
        tv_hr_con_state.startAnimation(animationSet)
    }


}