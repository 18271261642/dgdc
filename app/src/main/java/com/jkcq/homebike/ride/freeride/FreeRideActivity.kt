package com.jkcq.homebike.ride.freeride

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel
import com.jkcq.homebike.ble.devicescan.hr.HrDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.hr.HrDeviceListAdapter
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.login.WebHitoryViewActivity
import com.jkcq.homebike.ride.freeride.adpter.ViewPagerAdapter
import com.jkcq.homebike.ride.freeride.dialog.BikeCompletyDialog
import com.jkcq.homebike.ride.freeride.view.scroll.ScollLinearLayoutManager
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.util.BikeDataCache
import com.jkcq.homebike.ride.util.HeartRateConvertUtils
import com.jkcq.homebike.ride.view.AnimSporEndView
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_course_ride.*
import kotlinx.android.synthetic.main.activity_free_ride.*
import kotlinx.android.synthetic.main.activity_free_ride.itemview_consume_kcal
import kotlinx.android.synthetic.main.activity_free_ride.itemview_electric_kj
import kotlinx.android.synthetic.main.activity_free_ride.itemview_milage_km
import kotlinx.android.synthetic.main.activity_free_ride.itemview_power_w
import kotlinx.android.synthetic.main.activity_free_ride.iv_bike_conn_state
import kotlinx.android.synthetic.main.activity_free_ride.iv_guide_next
import kotlinx.android.synthetic.main.activity_free_ride.iv_guide_previous
import kotlinx.android.synthetic.main.activity_free_ride.iv_hr_device_tips_close
import kotlinx.android.synthetic.main.activity_free_ride.iv_option_pause
import kotlinx.android.synthetic.main.activity_free_ride.iv_sport_start
import kotlinx.android.synthetic.main.activity_free_ride.iv_sport_stop
import kotlinx.android.synthetic.main.activity_free_ride.layout_devicelist
import kotlinx.android.synthetic.main.activity_free_ride.layout_guide
import kotlinx.android.synthetic.main.activity_free_ride.layout_hr
import kotlinx.android.synthetic.main.activity_free_ride.layout_hr_device_close
import kotlinx.android.synthetic.main.activity_free_ride.layout_hr_device_high
import kotlinx.android.synthetic.main.activity_free_ride.layout_number
import kotlinx.android.synthetic.main.activity_free_ride.layout_option
import kotlinx.android.synthetic.main.activity_free_ride.noiseboardView
import kotlinx.android.synthetic.main.activity_free_ride.recycle_device_list
import kotlinx.android.synthetic.main.activity_free_ride.timer
import kotlinx.android.synthetic.main.activity_free_ride.tv_bpm_util
import kotlinx.android.synthetic.main.activity_free_ride.tv_current_resistance
import kotlinx.android.synthetic.main.activity_free_ride.tv_current_rpm
import kotlinx.android.synthetic.main.activity_free_ride.tv_duration
import kotlinx.android.synthetic.main.activity_free_ride.tv_guide_count
import kotlinx.android.synthetic.main.activity_free_ride.tv_hr_close
import kotlinx.android.synthetic.main.activity_free_ride.tv_hr_con_state
import kotlinx.android.synthetic.main.activity_free_ride.tv_hr_value
import kotlinx.android.synthetic.main.activity_free_ride.tv_mode_title
import kotlinx.android.synthetic.main.activity_free_ride.tv_scan_hr_device
import kotlinx.android.synthetic.main.activity_free_ride.tv_too_high
import kotlinx.android.synthetic.main.activity_free_ride.video_loader
import kotlinx.android.synthetic.main.activity_free_ride.view_page2
import kotlinx.android.synthetic.main.activity_pk_ride.*
import kotlinx.android.synthetic.main.view_bike_sport_dis_count.*
import java.io.File
import java.io.IOException
import java.util.*


class FreeRideActivity : BaseVMActivity<BikeDeviceConnectViewModel>(), java.util.Observer {


    var videoPath = "";
    var musicPath = "";

    var mSceneNoFirst: Boolean by Preference(Preference.FreeFirst, true)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    fun isGuider() {
        BikeConfig.sBikeBean = null
        BikeConfig.isUpgradeSuccess = false
        initBle()
        RealDataObservable.getInstance().addObserver(this)
        layout_devicelist.visibility = View.GONE
        startFree()
        if (mSceneNoFirst) {
            tv_guide_count.setTag("1")
            layout_guide.visibility = View.VISIBLE
            view_page2.adapter = ViewPagerAdapter(this)
            view_page2.setCurrentItem(0, false)
            iv_guide_previous.visibility = View.GONE
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/3"

        } else {

            //  view_page2.isUserInputEnabled = false
            startNumber()
            // startScence()
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
                    tv_guide_count.text = "" + (position + 1) + "/3"
                    tv_guide_count.setTag("" + (position + 1))
                    if (position + 1 == 1) {
                        iv_guide_previous.visibility = View.GONE
                    } else {
                        iv_guide_previous.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {

                }

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

    }

    val mDeviceScanViewModel: DeviceScanViewModel by lazy { createViewModel(DeviceScanViewModel::class.java) }
    val mBikeDeviceConnectViewModel: BikeDeviceConnectViewModel by lazy {
        createViewModel(
            BikeDeviceConnectViewModel::class.java
        )
    }

    var current_resistance = 50;
    lateinit var scollLinearLayoutManager: ScollLinearLayoutManager


    var connecetHr: ConnectDeviceDialog? = null


    fun initSportDetailRec() {

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
                        this@FreeRideActivity,
                        this@FreeRideActivity.getString(R.string.device_state_fail)
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
        return R.layout.activity_free_ride
    }


    override fun initView() {
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //TODO("Not yet implemented")
    }

    var commonDialog: CommonDialog? = null

    override fun initEvent() {
        layout_number.setOnClickListener {

        }
        layout_option.setOnClickListener {

        }
        layout_devicelist.setOnClickListener {

        }
        iv_guide_previous.setOnClickListener {

            view_page2.setCurrentItem(view_page2.currentItem - 1)
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/3"
            tv_guide_count.setTag("" + (view_page2.currentItem + 1))
            if (view_page2.currentItem + 1 == 1) {

                iv_guide_previous.visibility = View.GONE
            }

        }
        iv_guide_next.setOnClickListener {

            iv_guide_previous.visibility = View.VISIBLE
            if (tv_guide_count.getTag().equals("3")) {
                startNumber()
            }
            view_page2.setCurrentItem(view_page2.currentItem + 1)
            tv_guide_count.setTag("" + (view_page2.currentItem + 1))
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/3"
        }
        //暂停
        iv_option_pause.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            BikeConfig.isPause = true
            mFreeRideViewModel.endFreeRide()
            startBg(0)
            stopPlayMusic()
            layout_option.visibility = View.VISIBLE
        }
        //开始
        iv_sport_start.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            BikeConfig.isPause = false
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
                if (!AppUtil.isOpenBle()) {
                    return@setOnClickListener
                }
                initSportDetailRec()
                mDeviceScanViewModel.startLeScan()
                layout_hr_device_close.visibility = View.GONE
            }

            // startActivity(Intent(this@FreeRideActivity, HrDeviceScanActivity::class.java))
        }



        iv_resistance_up.setOnClickListener {
            if (current_resistance == 1) {
                current_resistance = 0
            }
            current_resistance += 10
            if (current_resistance >= 100) {
                iv_resistance_up.setColorFilter(this@FreeRideActivity.resources.getColor(R.color.common_rbtn_gray_color))
            } else {
                iv_resistance_up.setColorFilter(this@FreeRideActivity.resources.getColor(R.color.white))
            }
            if (current_resistance > 100) {
                current_resistance = 100
            }
            tv_current_resistance.text = "" + current_resistance
            mBikeDeviceConnectViewModel.sendQuitData(current_resistance)
            iv_resistance_down.setColorFilter(this@FreeRideActivity.resources.getColor(R.color.white))

        }

        iv_resistance_down.setOnClickListener {
            current_resistance -= 10
            if (current_resistance <= 1) {
                iv_resistance_down.setColorFilter(this@FreeRideActivity.resources.getColor(R.color.common_rbtn_gray_color))
            } else {
                iv_resistance_down.setColorFilter(this@FreeRideActivity.resources.getColor(R.color.white))
            }
            if (current_resistance < 1) {
                current_resistance = 1
            }
            mBikeDeviceConnectViewModel.sendQuitData(current_resistance)
            iv_resistance_up.setColorFilter(this@FreeRideActivity.resources.getColor(R.color.white))
            tv_current_resistance.text = "" + current_resistance

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
            if (!AppUtil.isOpenBle()) {
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
                this@FreeRideActivity,
                "",
                this@FreeRideActivity.resources.getString(R.string.end_bike_sport),
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
            endValue, dialogShowTwobutton

        )
        yesOrNoDialog?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
                //  TODO("Not yet implemented")

                if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_SUCCESS) {
                    BikeConfig.isPause = false
                    mFreeRideViewModel.reStartFreeRide()
                } else {
                    ToastUtil.showTextToast(
                        this@FreeRideActivity,
                        this@FreeRideActivity.resources.getString(R.string.device_state_fail)
                    )
                }
            }

            override fun onSureOnclick() {
                ActivityLifecycleController.finishAllActivity("MainActivity")

            }
        })
        yesOrNoDialog?.show()

    }


    fun updateBikeData(dialogShowTwobutton: Boolean) {
        //                    Logger.myLog("dateStr == " + (year + "-" + formatTwoStr(non) + "-" + formatTwoStr(day)) + "heartRateList == " + heartRateList.toString() + " heartRateList.size == " + heartRateList.size() + " AVG == " + Math.round(sum / (float) size));

        //骑行类型，0：自由骑行，1：线路骑行，2：PK骑行

        if (BikeConfig.isUpgradeSuccess) {
            return
        }
        mFreeRideViewModel.endFreeRide()
        stopPlayMusic()
        startBg(0)
        if (BikeConfig.sBikeBean == null) {
            showToShortDisDialog(dialogShowTwobutton)
            return

        } else {
            if (BikeConfig.sBikeBean.dis < 200f) {
                showToShortDisDialog(dialogShowTwobutton)
                return
            }
            //上传单位卡
            var calorie: Int = BikeConfig.sBikeBean.cal.toInt()
            var cyclingTime: Long = System.currentTimeMillis()
            var cyclingType: Int = BikeConfig.BIKE_FREE
            //单位是米
            var distance: Int = BikeConfig.sBikeBean.dis.toInt()
            var duration: Int = BikeConfig.sBikeBean.duration
            var heartRateArray = BikeConfig.sBikeBean.hrlist
            var powerArray = BikeConfig.sBikeBean.powList
            //单位是 w
            var powerGeneration: Int = BikeConfig.sBikeBean.light.toInt()
            var scenarioId: String = ""
            var steppedFrequencyArray = BikeConfig.sBikeBean.rpmList



            mBikeDataViewModel.upgradeCyclingRecords(
                false,
                distance,
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
        mSceneNoFirst = false

        Log.e("startNumber", "startNumber")
        layout_guide.visibility = View.GONE
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
        BikeConfig.isPause = true

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

    var curIndex = 0;
    override fun onResume() {
        super.onResume()
        if (!isFistrCon) {

            if (video_loader.isPlaying) {

            } else {


                video_loader.seekTo(curIndex);

                if (BikeConfig.isPause) {

                } else {
                    mFreeRideViewModel.reStartFreeRide()
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        BikeConfig.isPause = true
        mFreeRideViewModel.endFreeRide()
        startBg(0)

        if (commonDialog != null && commonDialog!!.isShowing || yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {

        } else {
            layout_option.visibility = View.VISIBLE
        }
        stopPlayMusic()
        curIndex = video_loader.getCurrentPosition();
    }

    fun startBg(rpm: Int) {

        if (rpm != 0) {
            //framesSequenceAnimation.start()
            //framesSequenceAnimation.setDuration((1000 * (60f / rpm)).toLong()) //设置播放总时间
            //scollLinearLayoutManager.setSpeedSlow(5 / (rpm / 60f))
        } else {
            //framesSequenceAnimation.stop() //设置播放总时间
        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        // mHrDeviceConnectViewModel.disconectDevice()

    }

    var filter: IntentFilter? = null

    fun initBle() {
        filter = IntentFilter()
        filter!!.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(broadcastReceiver, filter)
        mHrDeviceConnectViewModel.setCallBack(this)
        mBikeDeviceConnectViewModel.setCallBack(this)
        mBikeDeviceConnectViewModel.sendQuitData(current_resistance)

        when (BikeConfig.HRConnState) {
            BikeConfig.BIKE_CONN_DISCONN -> {
                setHrValue(0)
                // tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device_discon)
            }
            BikeConfig.BIKE_CONN_SUCCESS -> {
                tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device)
                layout_hr_device_close.visibility = View.GONE
                layout_devicelist.visibility = View.GONE
                mDeviceDataList.clear()
            }
        }
    }

    var name = "";

    override fun initData() {
        //TODO("Not yet implemented")
        // showCountDownPopWindow()

        if (CacheManager.mUserInfo != null) {
            birthday = CacheManager!!.mUserInfo!!.birthday
            age = CacheManager.getAge(birthday)
            sex = CacheManager!!.mUserInfo!!.getGender()
        }

        videoPath = intent.getStringExtra("videoPath")
        musicPath = intent.getStringExtra("musicPath")
        name = intent.getStringExtra("name");
        tv_mode_title.text = name
        isGuider()
        BikeConfig.disUnitBike(itemview_milage_km.getUnitText(), this)


        RealDataObservable.getInstance().addObserver(this)
        tv_current_resistance.text = "" + current_resistance

        setHrValue(0)
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
                var intent = Intent(this@FreeRideActivity, WebHitoryViewActivity::class.java)
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
                    this@FreeRideActivity.getString(R.string.sport_detail_title)
                )
                startActivity(intent)
                ActivityLifecycleController.finishAllActivity("MainActivity")
                //跳转到详情页面
            }
        })
        dialog.show()
    }

    override fun startObserver() {


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

        mBikeDeviceConnectViewModel.mRealData.observe(this, Observer {

            Log.e("mRealData", it.toString())
            mDataList.add(it)
        })


        mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {
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
        mHrDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {

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

        mHrDeviceConnectViewModel.mDeviceHrValue.observe(this, Observer {
            Log.e("mDeviceHrValue", "" + it)
            if (!BikeConfig.isPause) {
                setHrValue(it)
                currentHr = it
            }
        })


        mFreeRideViewModel.mTimer.observe(this, Observer {
            var hrValue = 0;
            try {
                hrValue = currentHr
                currentHr = 0;
                mDataList.clear()
                /* BikeConfig.speed = 500
                 BikeConfig.power = 200*/
                itemview_power_w.setValue("" + BikeConfig.power)
                if (BikeConfig.speed == 0 && BikeConfig.power == 0) {
                    stopPlayMusic()
                    BikeUtil.getGlobalBikebean(
                        0,
                        0,
                        it,
                        hrValue
                    )
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
                    if (BikeConfig.sBikeBean != null) {

                        if (BikeConfig.sBikeBean.dis >= 200) {
                            BikeDataCache.saveBike(this, "free", BikeConfig.sBikeBean)
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

                val hms: DateUtil.HMS = DateUtil.getHMSFromMillis(it)
                tv_duration.text = String.format(
                    "%02d:%02d:%02d",
                    hms.getHour(),
                    hms.getMinute(),
                    hms.getSecond()
                )
            }


        })
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

    fun setTargetValue(value: Int) {
        noiseboardView.setTargetSpeed(value)
    }

    fun setrpmRealValue(value: Int) {
        noiseboardView.setTargetSpeed(-1)
        noiseboardView.setRealTimeValue(value.toFloat())
        tv_current_rpm.text = "" + value
    }

    var coutTime: CountTimer? = null;
    fun setItemValue(dis: Float, cal: Float, jk: Double, rpm: Float) {

        val strdis = SiseUtil.disUnitCoversion(dis.toString(), BikeConfig.userCurrentUtil)
        val strCal = SiseUtil.calCoversion(cal.toString())
        val power = SiseUtil.powerCoversion(jk.toString())

        itemview_milage_km.setValue(strdis)

        itemview_consume_kcal.setValue(strCal)
        itemview_electric_kj.setValue(power)
        setrpmRealValue(rpm.toInt())
        startBg(rpm.toInt())
        if (video_loader.isPlaying) {
            if (BikeConfig.sBikeBean.tzSpeed == 0f) {
                stopPlayMusic()
            } else {
                var speed = BikeConfig.sBikeBean.tzSpeed / 60
                if (speed >= 2.0f) {
                    speed = 2.0f
                }
                setPlaySpeed(speed)
            }

        } else {
            if (BikeConfig.sBikeBean.tzSpeed != 0f) {
                startPlayMusic()
                var speed = BikeConfig.sBikeBean.tzSpeed / 60
                if (speed >= 2.0f) {
                    speed = 2.0f
                }
                setPlaySpeed(speed)
            }
        }

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
        if (yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {
            yesOrNoDialog!!.dismiss()
        }
        mHrDeviceConnectViewModel.disconectDevice()
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

    fun forceStopRecyclerViewScroll(mRecyclerView: RecyclerView) {
        mRecyclerView.dispatchTouchEvent(
            MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_CANCEL,
                0f,
                0f,
                0
            )
        )
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
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
                    setHrValue(0)
                    updateBikeData(false)
                    //弹出失败对话框

                }
            }
        }
    }

    var isFistrCon = true;

    fun startFree() {


        var path = videoPath.substring(videoPath.lastIndexOf("/"))
        val file = File(
            FileUtil.getFreeDir().toString() + path
        )
        initDataPlayer(musicPath)
        video_loader.setVideoURI(Uri.parse(file.path))
        video_loader.setOnPreparedListener {
            it.setScreenOnWhilePlaying(true);
            it.setVolume(0f, 0f);
            mediaPlayer = it
            video_loader.start()
            handler.postDelayed(Runnable {
                video_loader.pause()

            }, 10)
            isFistrCon = false
            it.isLooping = true
        }
    }

    var mediaPlayer: MediaPlayer? = null;
    private fun setPlaySpeed(speed: Float): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (mediaPlayer != null) {
                    val params: PlaybackParams = mediaPlayer!!.getPlaybackParams()
                    params.speed = speed
                    mediaPlayer!!.setPlaybackParams(params)
                }
                Log.e("setPlaySpeed", "setPlaySpeed: " + "" + speed)
                true
            } catch (e: Exception) {
                Log.e("setPlaySpeed", "setPlaySpeed: ", e)
                false
            }
        } else false
    }

    var player: MediaPlayer? = MediaPlayer()
    fun initDataPlayer(musicPath: String) {
        if (TextUtils.isEmpty(musicPath)) {
            return
        }
        var music = musicPath.substring(musicPath.lastIndexOf("/"))
        val file = File(
            FileUtil.getMusicDir().toString() + music
        )


        //1 初始化AudioManager对象
        val mAudioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        //2 申请焦点
        mAudioManager.requestAudioFocus(
            mAudioFocusChange,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )

        val fileDescriptor: AssetFileDescriptor
        try {
            //3 获取音频文件,我从网上下载的歌曲，放到了assets目录下
            fileDescriptor = getResources().openRawResourceFd(R.raw.music)
            //4 实例化MediaPlayer对象
            //5 设置播放流类型
            player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            //6 设置播放源，有多个参数可以选择，具体参考相关文档，本文旨在介绍音频焦点
            /*  player!!.setDataSource(
                  fileDescriptor.fileDescriptor,
                  fileDescriptor.startOffset,
                  fileDescriptor.length
              )*/
            player!!.setDataSource(
                file.path
            )
            //7 设置循环播放
            player!!.setLooping(true)
            //8 准备监听
            player!!.setOnPreparedListener(MediaPlayer.OnPreparedListener { //9 准备完成后自动播放
                //  mMediaPlayer.start()o
                player!!.setVolume(0.5f, 0.5f);
            })
            //10 异步准备
            player!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun startPlay() {
        try {
            if (player != null && player!!.isPlaying()) {
                // player!!.prepare()
                player!!.setVolume(0.5f, 0.5f)
                // player!!.setLooping(true);
            } else {
                player!!.start()
            }
        } catch (e: Exception) {

        }
    }

    fun startPlayMusic() {
        video_loader.start()
        Log.e("startPlayMusic", "startPlayMusic")

        startPlay()
    }


    fun stopPlayMusic() {


        if (video_loader.isPlaying) {
            video_loader.pause()
        }
        if (player != null) {

            //  Build.BRAND

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || isSamsung()) {

                player!!.pause()
            } else {
                player!!.setVolume(0f, 0f);
            }


        }
        /* if (video_loader.isPlaying) {
             video_loader.pause()
         }
         if (player != null && player!!.isPlaying) {
             player!!.stop()
         }*/
    }

    fun isSamsung(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "samsung"
    }

    var TAG = "mAudioFocusChange"
    private val mAudioFocusChange: AudioManager.OnAudioFocusChangeListener =
        object : AudioManager.OnAudioFocusChangeListener {
            override fun onAudioFocusChange(focusChange: Int) {
                when (focusChange) {
                    AudioManager.AUDIOFOCUS_LOSS -> {
                        //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                        //会触发此回调事件，例如播放QQ音乐，网易云音乐等
                        //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                        Log.d(TAG, "AUDIOFOCUS_LOSS")
                        //stop()
                        //释放焦点，该方法可根据需要来决定是否调用
                        //若焦点释放掉之后，将不会再自动获得
                        //mAudioManager.abandonAudioFocus(this)
                    }
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                        //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                        //会触发此回调事件，例如播放短视频，拨打电话等。
                        //通常需要暂停音乐播放
                        // stop()
                        player!!.setVolume(0.2f, 0.2f);
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT")
                    }
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->                     //短暂性丢失焦点并作降音处理
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                    AudioManager.AUDIOFOCUS_GAIN -> {
                        //当其他应用申请焦点之后又释放焦点会触发此回调
                        //可重新播放音乐
                        player!!.setVolume(0.5f, 0.5f);
                        Log.d(TAG, "AUDIOFOCUS_GAIN")
                        // start()
                    }
                }
            }
        }


}