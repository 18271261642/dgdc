package com.jkcq.homebike.ride.pk

import android.animation.Animator
import android.animation.ObjectAnimator
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.websocket.PKUserCacheManager
import com.example.websocket.WsManager
import com.example.websocket.bean.*
import com.example.websocket.observable.PkObservable
import com.example.websocket.observable.TimeOutObservable
import com.jkcq.base.app.ActivityLifecycleController
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.CacheManager
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.base.easybarrage.Barrage
import com.jkcq.base.observable.LoginOutObservable
import com.jkcq.base.observable.ProgressObservable
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.bike.arithmetic.BikeUtil
import com.jkcq.homebike.bike.bean.RealDataBean
import com.jkcq.homebike.bike.observable.RealDataObservable
import com.jkcq.homebike.ble.bean.RankingBean
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel
import com.jkcq.homebike.ble.devicescan.hr.HrDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.hr.HrDeviceListAdapter
import com.jkcq.homebike.ble.scanner.ExtendedBluetoothDevice
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.login.WebHitoryViewActivity
import com.jkcq.homebike.ride.freeride.CountTimer
import com.jkcq.homebike.ride.freeride.dialog.BikeCompletyDialog
import com.jkcq.homebike.ride.mvvm.viewmodel.PKModel
import com.jkcq.homebike.ride.pk.adapter.PkRankingAdapter
import com.jkcq.homebike.ride.pk.adapter.PropsListAdapter
import com.jkcq.homebike.ride.pk.bean.BestRecodeBean
import com.jkcq.homebike.ride.pk.bean.LineUser
import com.jkcq.homebike.ride.pk.bean.PKStateBean
import com.jkcq.homebike.ride.pk.bean.PropesBean
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.pk.bean.enumbean.PkState
import com.jkcq.homebike.ride.sceneriding.adpter.ViewPagerAdapter
import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean
import com.jkcq.homebike.ride.util.BikeDataCache
import com.jkcq.homebike.ride.util.HeartRateConvertUtils
import com.jkcq.homebike.ride.util.SceneUtil
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import com.opensource.svgaplayer.SVGACallback
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import kotlinx.android.synthetic.main.activity_course_ride.*
import kotlinx.android.synthetic.main.activity_pk_ride.*
import kotlinx.android.synthetic.main.activity_pk_ride.imageView
import kotlinx.android.synthetic.main.activity_pk_ride.itemview_consume_kcal
import kotlinx.android.synthetic.main.activity_pk_ride.itemview_electric_kj
import kotlinx.android.synthetic.main.activity_pk_ride.itemview_milage_km
import kotlinx.android.synthetic.main.activity_pk_ride.itemview_power_w
import kotlinx.android.synthetic.main.activity_pk_ride.iv_bike_conn_state
import kotlinx.android.synthetic.main.activity_pk_ride.iv_guide_next
import kotlinx.android.synthetic.main.activity_pk_ride.iv_guide_previous
import kotlinx.android.synthetic.main.activity_pk_ride.iv_hr_device_tips_close
import kotlinx.android.synthetic.main.activity_pk_ride.iv_option_pause
import kotlinx.android.synthetic.main.activity_pk_ride.iv_sport_start
import kotlinx.android.synthetic.main.activity_pk_ride.layout_devicelist
import kotlinx.android.synthetic.main.activity_pk_ride.layout_guide
import kotlinx.android.synthetic.main.activity_pk_ride.layout_hr
import kotlinx.android.synthetic.main.activity_pk_ride.layout_hr_device_close
import kotlinx.android.synthetic.main.activity_pk_ride.layout_hr_device_high
import kotlinx.android.synthetic.main.activity_pk_ride.layout_number
import kotlinx.android.synthetic.main.activity_pk_ride.layout_option
import kotlinx.android.synthetic.main.activity_pk_ride.layout_ranking_detail
import kotlinx.android.synthetic.main.activity_pk_ride.layout_user_ranking
import kotlinx.android.synthetic.main.activity_pk_ride.noiseboardView
import kotlinx.android.synthetic.main.activity_pk_ride.ranking_view
import kotlinx.android.synthetic.main.activity_pk_ride.rec_char
import kotlinx.android.synthetic.main.activity_pk_ride.recycle_device_list
import kotlinx.android.synthetic.main.activity_pk_ride.recycle_ranking
import kotlinx.android.synthetic.main.activity_pk_ride.resistance_view
import kotlinx.android.synthetic.main.activity_pk_ride.timer
import kotlinx.android.synthetic.main.activity_pk_ride.tv_bpm_util
import kotlinx.android.synthetic.main.activity_pk_ride.tv_current_resistance
import kotlinx.android.synthetic.main.activity_pk_ride.tv_current_rpm
import kotlinx.android.synthetic.main.activity_pk_ride.tv_duration
import kotlinx.android.synthetic.main.activity_pk_ride.tv_guide_count
import kotlinx.android.synthetic.main.activity_pk_ride.tv_hide_user_ranking
import kotlinx.android.synthetic.main.activity_pk_ride.tv_hr_close
import kotlinx.android.synthetic.main.activity_pk_ride.tv_hr_con_state
import kotlinx.android.synthetic.main.activity_pk_ride.tv_hr_value
import kotlinx.android.synthetic.main.activity_pk_ride.tv_mode_title
import kotlinx.android.synthetic.main.activity_pk_ride.tv_number
import kotlinx.android.synthetic.main.activity_pk_ride.tv_scan_hr_device
import kotlinx.android.synthetic.main.activity_pk_ride.tv_too_high
import kotlinx.android.synthetic.main.activity_pk_ride.video_loader
import kotlinx.android.synthetic.main.activity_pk_ride.view_page2
import kotlinx.android.synthetic.main.item_pk_mine_ranking.*
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*


class PKRideActivity : BaseVMActivity<BikeDeviceConnectViewModel>(), java.util.Observer,
    CountTimer.OnCountTimerListener {


    private val countTimer = CountTimer(1000, this)
    private var millisecond: Long = 0


    var mSceneNoFirst: Boolean by Preference(Preference.PKFirst, true)
    var mpkId: String by Preference(Preference.PK_ID, "")
    private var path = ""
    var currentPKId = ""
    var bestRecodeBean: BestRecodeBean? = null

    var wordUserId = ""
    var myRecodeUserId = ""
    var wordBestSpeed = 0f
    var myRecodeSpeed = 0f

    //


    // 道具播放列表
    var mSendGiftData = mutableListOf<String>()

    /**
     * 阻力详情数据
     */
    var mSceneBean: SceneBean? = null;
    var slope = ""
    var dis = ""
    var name = "";
    var sceneId = "";
    var mResistanceIntervalBean = mutableListOf<ResistanceIntervalBean>()


    /**
     * 排行榜
     */

    var mRanklist = mutableListOf<PkdataList>()
    var mTempRanklist = mutableListOf<PkdataList>()
    var mTemAboveUserIds = mutableListOf<PkdataList>()
    var mTemBeUserIds = mutableListOf<PkdataList>()

    var mLineUser = mutableListOf<LineUser>()
    lateinit var mSceneRankingAdapter: PkRankingAdapter


    /**
     * 道具列表
     */
    var mPropesBean = mutableListOf<PropesBean>()
    lateinit var mPropesListAdapter: PropsListAdapter

    //
    var mDeviceDataList = mutableListOf<ExtendedBluetoothDevice>()
    lateinit var mHrDeviceListAdapter: HrDeviceListAdapter


    var currentHr = 0
    var mDataList = mutableListOf<RealDataBean>()


    /*  val mFreeRideViewModel: FreeRideViewModel by lazy {
          createViewModel(FreeRideViewModel::class.java)
      }
  */
    val mHrDeviceConnectViewModel: HrDeviceConnectViewModel by lazy {
        createViewModel(
            HrDeviceConnectViewModel::class.java
        )

    }
    val mPKmodel: PKModel by lazy {
        createViewModel(
            PKModel::class.java
        )

    }


    fun initPropsRec() {
        recycle_props.layoutManager = LinearLayoutManager(this)
        mPropesListAdapter = PropsListAdapter(mPropesBean)
        recycle_props.adapter = mPropesListAdapter

        mPropesListAdapter.setOnItemClickListener() { adapter, view, position ->
            var rankingBean = mPropesBean.get(position)
            // recycle_props.visibility = View.GONE

            if (rankingBean.isLengque) {
                return@setOnItemClickListener
            }

            mPropesBean.forEach {
                it.isLengque = true
            }
            mPropesListAdapter.notifyDataSetChanged()
            WsManager.getInstance()
                .sendGiftData(currentPKId, rankingBean.code, mUserId, currentUserId)


            val animator: ObjectAnimator = ObjectAnimator.ofFloat(
                loadUpgrade, "progress", 0f, 100f
            )
            animator.duration = 5000
            animator.start()
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    //  item.isLengque = false
                    //  view.progress = 0f
                    ProgressObservable.getInstance().updateProgress(0f)
                    mPropesBean.forEach {
                        it.isLengque = false
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })


        }
    }


    var currentUserId = ""

    fun initRankingRec() {
        var ranking = 1
        PKUserCacheManager.mPkUsers.forEach {
            if (it.userId.equals("0")) {
            } else {
                if (it.userId.equals(mUserId)) {
                    updateCurrentDisRankingValue(ranking, 0)
                    userValue(it.nickName, it.avatar)
                }
                mRanklist.add(PkdataList(it.userId, 0, 0, true, 0, ranking))
                ranking++
            }
        }
        mTempRanklist.clear()
        mTempRanklist.addAll(mRanklist)
        setRankingValue(mRanklist.size)
        recycle_ranking.layoutManager = LinearLayoutManager(this)
        mSceneRankingAdapter = PkRankingAdapter(mRanklist)
        recycle_ranking.adapter = mSceneRankingAdapter

        mSceneRankingAdapter.setOnItemClickListener() { adapter, view, position ->
            var rankingBean = mRanklist.get(position)




            if (rankingBean.userId.equals(mUserId)) {
                return@setOnItemClickListener
            }
            if (recycle_props.visibility == View.VISIBLE) {
                recycle_props.visibility = View.INVISIBLE
            } else {
                recycle_props.visibility = View.VISIBLE
            }

            currentUserId = rankingBean.userId

        }
    }

    val mDeviceScanViewModel: DeviceScanViewModel by lazy { createViewModel(DeviceScanViewModel::class.java) }
    val mBikeDeviceConnectViewModel: BikeDeviceConnectViewModel by lazy {
        createViewModel(
            BikeDeviceConnectViewModel::class.java
        )
    }

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


    var isTenSec = false;

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                //连接超时
                1001 -> {
                    hideConnectDialog()

                    ToastUtil.showTextToast(
                        this@PKRideActivity,
                        this@PKRideActivity.getString(R.string.device_state_fail)
                    )

                }
                1002 -> {
                    //10秒的超时
                    isTenSec = true
                }
                1003 -> {
                    goResultPage()
                }
                1004 -> {
                    dealDataDialog()
                    autoendUpdateBikeData()
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
        handler.sendEmptyMessageDelayed(1001, 20000)
        if (connecetHr != null && connecetHr!!.isShowing) {
            return
        }
        connecetHr = ConnectDeviceDialog(this, "");
        connecetHr?.show()
    }

    fun userValue(nikeName: String, headUrl: String) {
        tv_name.text = nikeName;
        LoadImageUtil.getInstance()
            .load(
                this,
                headUrl,
                iv_head,
                R.mipmap.friend_icon_default_photo
            )

    }


    fun setRankingValue(value: Int) {
        tv_number.text = String.format(
            this.resources.getString(R.string.sport_pk_use),
            "" + value
        )
    }

    fun updateCurrentDisRankingValue(ranking: Int, dis: Int) {
        tv_ranking.text = "" + ranking
        tv_hide_user_ranking.text = "" + ranking

        val dis = SiseUtil.disUnitCoversion(
            "" + dis,
            BikeConfig.userCurrentUtil
        )
        if (BikeConfig.METRIC_UNITS == BikeConfig.userCurrentUtil) {
            tv_dis.text = (
                    dis + this.resources.getString(R.string.sport_dis_unitl_km)
                    )
        } else {
            tv_dis.text = (
                    dis + this.resources.getString(R.string.sport_dis_unitl_mile)
                    )
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_pk_ride
    }

    //name
    //dis
    //slope

    fun setResitance() {

        slope = mSceneBean!!.slope
        dis = mSceneBean!!.length.toFloat().toInt().toString()
        name = mSceneBean!!.name
        sceneId = mSceneBean!!.id;
        mPKmodel.getBestRecodes(mSceneBean!!.id, mUserId)
        mPKmodel.getPropsAll()
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

        tv_mode_title.text = String.format(
            this.resources.getString(R.string.scene_mode_title), name, strdis
        )
        var list = SceneUtil.getLineBeanList(SceneUtil.splitSemicolonStr(slope))
        mResistanceIntervalBean.addAll(SceneUtil.getResisteanceList(list))
        if (mResistanceIntervalBean.size > 0) {
            start_resistance = mResistanceIntervalBean.get(0).getmResistances()
            current_resistance = mResistanceIntervalBean.get(0).getmResistances()
            start_rpm = mResistanceIntervalBean.get(0).getmRpm()
            current_rpm = mResistanceIntervalBean.get(0).getmRpm()
            calCurrentResisAndRpm(start_resistance.toFloat(), start_rpm.toFloat())
        }



        setCurrentResitance()
        noiseboardView.setTargetSpeed(-1)
        //noiseboardView.setRealTimeValue(start_rpm.toFloat())
        resistance_view.setdata(mResistanceIntervalBean, dis.toInt())
        resistance_view.setCurrentDisList(
            mLineUser,
            0f

        )
        rec_char.setSumDis(dis.toInt())
    }


    override fun initView() {
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    fun isGuider() {
        //
        if (isReJoin) {
            BikeConfig.sBikeBean = BikeDataCache.getBikeBean(this, currentPKId)


            var subtime = (System.currentTimeMillis() - mConnTime) / 1000


            Log.e("mConnTime", "mConnTime=" + mConnTime + ",subtime=" + subtime)


            var i = 0
            if (subtime > 0) {
                var powlist = BikeConfig.sBikeBean.powList
                var hrList = BikeConfig.sBikeBean.hrlist
                var rpmList = BikeConfig.sBikeBean.rpmList
                while (i < subtime) {
                    powlist.add(0)
                    hrList.add(0)
                    rpmList.add(0)
                    i++
                }
            }


        } else {
            BikeConfig.sBikeBean = null
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
                    } else {
                        iv_guide_previous.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {

                }

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        BikeConfig.isUpgradeSuccess = false;
        initBle()
        RealDataObservable.getInstance().addObserver(this)
        layout_devicelist.visibility = View.GONE
        if (mSceneNoFirst) {
            tv_guide_count.setTag("1")
            layout_guide.visibility = View.VISIBLE
            view_page2.adapter = ViewPagerAdapter(this, true)
            view_page2.setCurrentItem(0, false)
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/4"
            iv_guide_previous.visibility = View.GONE

        } else {

            //  view_page2.isUserInputEnabled = false
            startScence()
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

    fun startScence() {

        mSceneNoFirst = false
        layout_guide.visibility = View.GONE
        //如果是重新进入不需要倒计时

        Log.e("isReJoin--", "" + isReJoin)
        if (isReJoin) {
            startBg(0)
            countTimer.start()
            layout_number.visibility = View.GONE
            BikeConfig.isPause = false

        } else {
            startNumber()
        }
        startPlayMusic()
        setResitance()

        path = mSceneBean!!.videoUrl.substring(mSceneBean!!.videoUrl.lastIndexOf("/"))
        val file = File(
            FileUtil.getVideoDir().toString() + path
        )
        video_loader.setVideoURI(Uri.parse(file.path))
        video_loader.setOnPreparedListener {
            it.setScreenOnWhilePlaying(true);
            it.setVolume(0f, 0f);
            video_loader.start()
            handler.postDelayed(Runnable {
                video_loader.pause()
                stopPlayMusic()

            }, 500)
            isFistrCon = false
            it.isLooping = true
        }
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
                // player!!.setVolume(1f, 1f);
            })
            //10 异步准备
            player!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun startPlayMusic() {
        video_loader.start()

        Log.e("startPlayMusic", "startPlayMusic")
        try {
            if (player != null && player!!.isPlaying()) {
                // player!!.prepare()
                player!!.setVolume(1f, 1f)
                // player!!.setLooping(true);
            } else {
                player!!.start()
                player!!.setVolume(1f, 1f)
            }
        } catch (e: Exception) {

        }

    }

    fun onPuserMusic() {
        if (video_loader.isPlaying) {
            video_loader.pause()
        }
        if (player != null && player!!.isPlaying) {
            player!!.stop()
        }
    }


    fun stopPlayMusic() {


        if (video_loader.isPlaying) {
            video_loader.pause()
        }
        if (player != null) {
            //三星
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P || isSamsung()) {
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


    var scanBike = false

    fun bikeState() {
        if (BikeConfig.isConn()) {
            iv_bike_conn_state.setImageResource(R.mipmap.icon_bike_device)
        } else {
            iv_bike_conn_state.setImageResource(R.mipmap.icon_bike_device_discon)
        }
    }

    fun connectDevice() {
        Log.e("iv_bike_conn_state", "start1" + "mBikeName" + mBikeName)
        if (!AppUtil.isOpenBle()) {

            ToastUtil.showTextToast(
                BaseApp.sApplicaton, R.string.openBle
            )
            return
        }
        if (TextUtils.isEmpty(mBikeName)) {
            mDeviceScanViewModel.startLeScan()
        } else {
            if (null == BikeConfig.device) {
                mDeviceScanViewModel.startLeScan()
            } else {
                mBikeDeviceConnectViewModel.conectBikeDevice(BikeConfig.device)
            }
        }
    }

    override fun initEvent() {
        layout_number.setOnClickListener {

        }
        iv_bike_conn_state.setOnClickListener {
            Log.e("iv_bike_conn_state", "start1")
            if (!BikeConfig.isConn()) {
                mDeviceScanViewModel.stopLeScan()
                Log.e("iv_bike_conn_state", "start2")
                scanBike = true
                showConnetDialog()
                connectDevice()
            }
        }
        imageView.callback = (object : SVGACallback {
            override fun onFinished() {
                Log.e("imageViewpoint", "imageViewpoint onFinished" + mSendGiftData.size)
                // TODO("Not yet implemented")
                imageView.stopAnimation(true)
                imageView.clear()
                mSendGiftData.removeAt(0)
                if (mSendGiftData.size > 0) {
                    playSVAG(mSendGiftData.get(0))
                }
            }

            override fun onPause() {
                Log.e("imageViewpoint", "imageViewpoint onPause")
            }

            override fun onRepeat() {
            }

            override fun onStep(frame: Int, percentage: Double) {
            }

        })




        layout_option.setOnClickListener {

        }
        layout_devicelist.setOnClickListener {

        }
        iv_hide_pk_ranking.setOnClickListener {
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
            view_page2.setCurrentItem(view_page2.currentItem - 1)
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/4"
            tv_guide_count.setTag("" + (view_page2.currentItem + 1))
            if (view_page2.currentItem + 1 == 1) {
                iv_guide_previous.visibility = View.GONE
            }

        }
        iv_guide_next.setOnClickListener {

            iv_guide_previous.visibility = View.VISIBLE
            if (tv_guide_count.getTag().equals("4")) {
                startScence()
            }
            view_page2.setCurrentItem(view_page2.currentItem + 1)
            tv_guide_count.setTag("" + (view_page2.currentItem + 1))
            tv_guide_count.text = "" + (view_page2.currentItem + 1) + "/4"
        }

        //退出PK
        iv_option_pause.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            showEndPk()
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
                scanBike = false
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
            if (!AppUtil.isOpenBle()) {
                return@setOnClickListener
            }
            initSportDetailRec()
            scanBike = false
            mDeviceScanViewModel.startLeScan()
        }

    }


    var endPk: CommonDialog? = null
    fun showEndPk() {

        if (endPk != null && endPk!!.isShowing) {
            return
        }
        var endValue = this.resources.getString(R.string.pking_signout_pk_tips)


        endPk = CommonDialog(
            this,
            "",
            endValue,
            "",
            "", true

        )
        endPk?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {

            }

            override fun onSureOnclick() {
                PkObservable.getInstance().deleteObserver(this@PKRideActivity)
                TimeOutObservable.getInstance().deleteObserver(this@PKRideActivity)
                mPKmodel.leavePk(mpkId, false)
            }
        })
        endPk?.show()
    }

    fun autoendUpdateBikeData() {
        mHrDeviceConnectViewModel.disconectDevice()
        if (BikeConfig.sBikeBean != null) {
            video_loader!!.pause()
            countTimer.stop()
            startBg(0)
            var calorie: Int = BikeConfig.sBikeBean.cal.toInt()
            var cyclingTime: Long = System.currentTimeMillis()
            var cyclingType: Int = BikeConfig.BIKE_PK
            //单位是米
            var distance: Int = BikeConfig.sBikeBean.dis.toInt()
            var duration: Int = BikeConfig.sBikeBean.duration
            var heartRateArray = BikeConfig.sBikeBean.hrlist
            var powerArray = BikeConfig.sBikeBean.powList
            //单位是 w
            var powerGeneration: Int = BikeConfig.sBikeBean.light.toInt()
            var scenarioId: String = sceneId
            var steppedFrequencyArray = BikeConfig.sBikeBean.rpmList
            var durationmill = cyclingTime - BikeConfig.sBikeBean.connTime


            mPKmodel.upgradeCyclingPkRecords(
                durationmill,
                currentPKId,
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


    fun countdown() {
        layout_number.visibility = View.VISIBLE
        timer.visibility = View.VISIBLE
        val repeatCount = 9 //定义重复字数（执行动画1次 + 重复动画4次 = 公共5次）


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

                //正在汇总数据请耐心等待

                // 动画结束 隐藏控件
                timer.setVisibility(View.GONE)
                //startBg(0)
                //mFreeRideViewModel.startFreeRide()

                //显示对话框超时
                // handler.removeMessages(1003)
                // handler.sendEmptyMessageDelayed(1003, 7000)
                //autoendUpdateBikeData()
                layout_number.visibility = View.GONE
                BikeConfig.isPause = false
            }

            override fun onAnimationRepeat(animation: Animation) { // 此方法执行4次（repeatCount值）
                timer.setText("" + count)
                count--
            }
        })
    }


    fun startNumber() {


        Log.e("startNumber", "startNumber")

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
                countTimer.start()
                layout_number.visibility = View.GONE
                BikeConfig.isPause = false
            }

            override fun onAnimationRepeat(animation: Animation) { // 此方法执行4次（repeatCount值）
                timer.setText("" + count)
                count--
            }
        })


    }

    var pkState = PkState.START.value
    var dealDialog: TipsDialog? = null
    fun dealDataDialog() {
        Log.e("dealDataDialog", "dealDataDialog")
        try {
            if (isFinishing) {
                return
            }
            if (dealDialog != null) {
                return
            }
            dealDialog = TipsDialog(this, this.getString(R.string.data_is_processing))
            dealDialog?.show()

        } catch (e: Exception) {


        } finally {
            dealDialog = null
        }

    }

    var yesOrNoDialog: YesOrNoDialog? = null
    fun timeOut(title: String) {
        handler.removeMessages(1004)
        handler.removeMessages(1003)
        try {
            if (dealDialog != null && dealDialog!!.isShowing) {
                dealDialog!!.dismiss()
                dealDialog = null
            }
            if (yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {
                return
            }
            video_loader!!.pause()
            countTimer.stop()
            yesOrNoDialog = YesOrNoDialog(
                this,
                "",
                title,
                "",
                this.resources.getString(R.string.dialog_ok), false
            )
            yesOrNoDialog?.show()
            yesOrNoDialog?.setBtnOnclick(object : OnButtonListener {
                override fun onCancleOnclick() {
                }

                override fun onSureOnclick() {
                    yesOrNoDialog?.dismiss()
                    yesOrNoDialog = null
                    ActivityLifecycleController.finishAllActivity("MainActivity")
                    //mAliYunModel.cancelDownTask()
                }
            })
        } catch (e: Exception) {

        }

    }

    var isWord = false
    var isMyRecod = false

    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)
        runOnUiThread {
            try {
                if (o is TimeOutObservable) {
                    if (arg == 0) {
                        timeOut(this.resources.getString(R.string.connect_timeout_exit_PK))
                    } else if (arg == 1) {
                        mPKmodel.findPKState()
                    }

                } else if (o is PkObservable) {
                    if (arg is MqttMessageResponse) {
                        mTempRanklist.clear()
                        mTempRanklist.addAll(arg.dataList)
                        mRanklist.clear();
                        var bean: PkdataList? = null
                        mLineUser.clear()
                        mTempRanklist.forEach {
                            if (it.userId.equals(mUserId)) {
                                bean = it
                            } else {
                                mLineUser.add(LineUser(it.userId, it.distance))

                            }
                        }
                        //大于十秒才开始计算排名的
                        var index = mTempRanklist.indexOf(bean)
                        var i = 0
                        while (i < mTempRanklist.size) {
                            var temp = mTempRanklist.get(i)
                            if (i < index && !mTemAboveUserIds.contains(temp)) {


                                if (isTenSec) {
                                    if (temp.distance != bean!!.distance) {
                                        barrageView.addBarrage(
                                            Barrage(
                                                String.format(
                                                    this.getString(R.string.exceed_tips),
                                                    PKUserCacheManager.pkuserName.get(temp.userId)
                                                )
                                            )
                                        )
                                    }
                                }

                            } else if (i > index) {
                                if (!mTemBeUserIds.contains(temp)) {
                                    if (isTenSec) {
                                        if (temp.distance != bean!!.distance) {
                                            barrageView.addBarrage(
                                                Barrage(
                                                    String.format(
                                                        this.getString(R.string.you_exceed_tips),
                                                        PKUserCacheManager.pkuserName.get(temp.userId)
                                                    )
                                                )
                                            )
                                        }
                                    }

                                }
                            }
                            i++
                        }
                        mTemAboveUserIds.clear()
                        mTemBeUserIds.clear()
                        if (index >= 0 && index < mTempRanklist.size) {
                            mTemAboveUserIds.addAll(mTempRanklist.subList(0, index))
                        }
                        if (index >= 0 && (index + 1) < mTempRanklist.size) {
                            mTemBeUserIds.addAll(
                                mTempRanklist.subList(
                                    index + 1,
                                    mTempRanklist.size
                                )
                            )
                        }
                        if (bean != null) {

                            Log.e(
                                "wordUserId",
                                "wordUserId=" + wordUserId + ",wordBestSpeed=" + wordBestSpeed
                            )

                            if (bestRecodeBean != null && !TextUtils.isEmpty(wordUserId) && wordBestSpeed > 0f && isTenSec) {
                                if (bean!!.distance.toFloat() > (wordBestSpeed * arg.duration).toInt() && !isWord) {
                                    isWord = true
                                    barrageView.addBarrage(
                                        Barrage(
                                            this.getString(R.string.surpassed_world_record)
                                        )
                                    )
                                } else {
                                    // isWord = false
                                }
                                mLineUser.add(
                                    LineUser(
                                        wordUserId,
                                        (wordBestSpeed * arg.duration).toInt()
                                    )
                                )

                            }

                            if (bestRecodeBean != null && !TextUtils.isEmpty(myRecodeUserId) && myRecodeSpeed > 0f && isTenSec) {
                                //v=s/t
                                if (bean!!.distance.toFloat() > (myRecodeSpeed * arg.duration).toInt() && !isMyRecod) {
                                    isMyRecod = true
                                    barrageView.addBarrage(
                                        Barrage(
                                            this.getString(R.string.surpassed_my_record)
                                        )
                                    )
                                } else {
                                    // isMyRecod = false
                                }
                                mLineUser.add(
                                    LineUser(
                                        myRecodeUserId,
                                        (myRecodeSpeed * arg.duration).toInt()
                                    )
                                )
                            }



                            mLineUser.add(LineUser(bean!!.userId, bean!!.distance))
                            resistance_view.setCurrentDisList(
                                mLineUser,
                                bean!!.distance.toFloat()

                            )
                            updateCurrentDisRankingValue(bean!!.rankNo, bean!!.distance)

                            //计算前面一个与后面一个与自己的距离
                            mTempRanklist.forEach {
                                it.subvalue = it.distance - bean!!.distance
                            }

                            //刷新计时器
                            if (pkState == PkState.START.value) {
                                val hms: DateUtil.HMS =
                                    DateUtil.getHMSFromMillis(arg.duration * 1000L)
                                tv_duration.text = String.format(
                                    "%02d:%02d:%02d",
                                    hms.getHour(),
                                    hms.getMinute(),
                                    hms.getSecond()
                                )

                            } else {

                            }



                            setRankingValue(mTempRanklist.size)
                            mRanklist.addAll(mTempRanklist)
                            mSceneRankingAdapter.notifyDataSetChanged()
                        }


                        //需要计算出当前在我前面的名称


                    } else if (arg is SendGiftData) {
                        var propesBean = mPropesBean.findLast {
                            it.code.equals(arg.giftCode)
                        }
                        //  Log.e("SendGiftData", "" + arg + ",mUserId=" + mUserId)

                        if (propesBean == null) {
                            return@runOnUiThread
                        }
                        var codeName = ""
                        if (AppUtil.isCN()) {
                            codeName = propesBean.name
                        } else {
                            codeName = propesBean.nameEn
                        }
                        Log.e("propesBean", "" + propesBean)
                        //xxx送你一个xxxx道具
                        if (arg.toUserId.equals(mUserId)) {

                            starSVGa(propesBean!!.effectUrl)

                            barrageView.addBarrage(
                                Barrage(
                                    String.format(
                                        this.getString(R.string.gift_tips),
                                        PKUserCacheManager.pkuserName.get(arg.fromUserId),
                                        this.getString(R.string.your),
                                        codeName
                                    )
                                )
                            )


                        } else if (arg.fromUserId.equals(mUserId)) {

                            barrageView.addBarrage(
                                Barrage(
                                    String.format(
                                        this.getString(R.string.gift_tips),
                                        this.getString(R.string.your),
                                        PKUserCacheManager.pkuserName.get(arg.toUserId),
                                        codeName
                                    )
                                )
                            )

                        } else {
                            //xxx送你一个xxxx道具
                            barrageView.addBarrage(
                                Barrage(
                                    String.format(
                                        this.getString(R.string.gift_tips),
                                        PKUserCacheManager.pkuserName.get(arg.fromUserId),
                                        PKUserCacheManager.pkuserName.get(arg.toUserId),
                                        codeName
                                    )
                                )
                            )
                        }

                    } else if (arg is Int) {
                        Log.e("update", "" + arg)
                        when (arg) {
                            PKType.LOGOUT.value -> {
                                WsManager.getInstance().disConnect()
                                mBikeDeviceConnectViewModel.disconectDevice()
                                mHrDeviceConnectViewModel.disconectDevice()
                                mpkId = ""
                                LoginOutObservable.getInstance().show()
                            }
                            PKType.START.value -> {
                            }
                            PKType.DESTROY.value -> {
                            }
                            PKType.PK_END_ONE.value -> {
                                WsManager.getInstance().disConnect()
                                autoendUpdateBikeData()
                                timeOut(this.resources.getString(R.string.connect_one_exit_PK))
                            }
                            PKType.PK_END_CONTDOWN.value -> {

                                if (isEnd) {
                                    return@runOnUiThread
                                }
                                isEnd = true

                                stopPlayMusic()
                                handler.removeMessages(1003)
                                handler.removeMessages(1004)
                                handler.sendEmptyMessageDelayed(1003, 20000)
                                handler.sendEmptyMessageDelayed(1004, 10000)
                                countdown()
                            }
                            PKType.END.value -> {
                                goResultPage()
                            }
                        }

                    } else if (arg is LeaveBean) {
                        barrageView.addBarrage(
                            Barrage(
                                String.format(
                                    this.getString(R.string.has_exit_pk),
                                    PKUserCacheManager.pkuserName.get(arg.userId)
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }

    }

    var isEnd = false;

    fun goResultPage() {
        Log.e("goResultPage", "----goResultPage")
        RealDataObservable.getInstance().deleteObserver(this)
        handler.removeMessages(1003)
        handler.removeMessages(1004)
        try {
            if (dealDialog != null && dealDialog!!.isShowing) {
                dealDialog?.dismiss()
                dealDialog = null
            }
        } catch (e: Exception) {

        } finally {
            dealDialog = null
            Log.e("goResultPage", "----goResultPage2 +currentPKId=" + currentPKId)
            if (NetUtil.isNetworkConnected(this)) {
                Log.e("goResultPage", "----goResultPage3")
                WsManager.getInstance().disConnect()
                var intent = Intent(this@PKRideActivity, PKResultActivity::class.java)
                intent.putExtra("scene", mSceneBean)
                intent.putExtra("mPkId", currentPKId)
                startActivity(intent)
                finish()
            }

        }


    }

    fun startBg(rpm: Int) {

        if (rpm != 0) {
        } else {
        }

    }

    override fun onBackPressed() {
        /*  super.onBackPressed()
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


    var isReJoin = false
    var parser: SVGAParser? = null
    var birthday = "";
    var age = 18
    var sex = "Female"
    override fun initData() {


        Log.e("PKRideActivity", "initData-------------")
        PkObservable.getInstance().addObserver(this)
        if (CacheManager.mUserInfo != null) {
            birthday = CacheManager!!.mUserInfo!!.birthday
            age = CacheManager.getAge(birthday)
            sex = CacheManager!!.mUserInfo!!.getGender()
        }

        isTenSec = false
        handler.sendEmptyMessageDelayed(1002, 10000)

        mSceneBean = intent.getSerializableExtra("scene") as SceneBean?
        currentPKId = intent.getStringExtra("mpkId")
        mpkId = currentPKId
        isReJoin = intent.getBooleanExtra("isReJoin", false)
        parser = SVGAParser(this@PKRideActivity)
        imageView.loops = 1
        //异常退出需要在Pk中页面重新连接socket

        PKUserCacheManager.mPkUsers.forEach {
            if (mUserId.equals(it.userId)) {

                mLineUser.add(LineUser(it.userId, 0))
                LoadImageUtil.getInstance()
                    .loadDownload(
                        false,
                        this,
                        it.avatar,
                        it.userId,
                        R.drawable.icon_pk_mine, 5, R.mipmap.friend_icon_default_photo
                    )
            } else {
                LoadImageUtil.getInstance()
                    .loadDownload(
                        false,
                        this,
                        it.avatar,
                        it.userId,
                        R.drawable.icon_pk_other, 5, R.mipmap.friend_icon_default_photo
                    )
            }
        }


        bikeState()
        if (!WsManager.getInstance().isConn()) {
            WsManager.getInstance().connetSocket(mUserId, mpkId, mtoken)
        }
        BikeConfig.disUnitBike(itemview_milage_km.getUnitText(), this)
        BikeConfig.isPause = false
        initRankingRec()
        initPropsRec()
        initDataPlayer(mSceneBean!!.audioUrl)
        isGuider()
        setHrValue(0)

    }

    var currentId = "";

    fun setItemvalue(summerBean: Summary) {


        currentId = summerBean.upgradeId

        val dis =
            SiseUtil.disUnitCoversion(summerBean.totalDistance, BikeConfig.userCurrentUtil)
        val cal = SiseUtil.calCoversion(summerBean.totalCalorie)
        val min = SiseUtil.timeCoversionMin(summerBean.totalDuration)
        var dialog = BikeCompletyDialog(this, dis, min, cal, true)
        dialog.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
                mHrDeviceConnectViewModel.disconectDevice()
                ActivityLifecycleController.finishAllActivity("MainActivity")

            }

            override fun onSureOnclick() {
                mHrDeviceConnectViewModel.disconectDevice()


                var intent = Intent(this@PKRideActivity, WebHitoryViewActivity::class.java)
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
                    this@PKRideActivity.getString(R.string.sport_detail_title)
                )
                startActivity(intent)


                finish()
                //跳转到详情页面
            }
        })
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        // PkObservable.getInstance().deleteObserver(this)
        TimeOutObservable.getInstance().deleteObserver(this)
        if (video_loader.isPlaying) {
            stopPlayMusic()
        }
        curIndex = video_loader.getCurrentPosition();
        Log.e("curIndex", "curIndex" + curIndex)
    }

    var curIndex = 0;
    var isFistrCon = true;

    override fun onResume() {
        super.onResume()
        // mPKmodel.findPKState()
        TimeOutObservable.getInstance().addObserver(this)
        if (!isFistrCon) {

            if (video_loader.isPlaying) {

            } else {
                video_loader.seekTo(curIndex);

                if (BikeConfig.isPause) {
                    handler.postDelayed(Runnable {
                        video_loader.pause()
                    }, 400)

                } else {
                    startPlayMusic()
                    countTimer.reStart(millisecond)
                }
            }
        }
        //  isFistrCon = false
    }

    override fun onStop() {
        super.onStop()
        //mBikePresenter.unRegisterCallback(mRoadCallBack);

        /* Log.e("mediaPlayer onStop", "" + mediaPlayer!!.playerState)
         mediaPlayer!!.pause()
         Log.e("mediaPlayer onStop", "" + mediaPlayer!!.playerState)*/
    }


    override fun startObserver() {

        mPKmodel.mCanJoin.observe(this, Observer {

            when (it.pkStatus) {
                PkState.UNSTART.value -> {
                }
                PkState.START.value -> {
                }
                PkState.END.value -> {
                    timeOut(this.resources.getString(R.string.connect_one_exit_PK_end))
                }
                PkState.DESTROY.value -> {
                    timeOut(this.resources.getString(R.string.connect_one_exit_PK_end))
                }
                PkState.COLLECT_DATA.value -> {
                    //  timeOut(this.resources.getString(R.string.connect_timeout_exit_PK))
                }
            }
        })
        mPKmodel.mPKDestory.observe(this, Observer {
            showEndPk()
        })
        mPKmodel.mPropesList.observe(this, Observer {
            mPropesBean.clear()
            mPropesBean.addAll(it)
            mPropesListAdapter.notifyDataSetChanged()
        })

        mPKmodel.mBestRecodeBean.observe(this, Observer {

            Log.e("startObserver", wordUserId + "-----" + it)
            if (!TextUtils.isEmpty(it.worldRecord.userId)) {
                wordUserId = it.worldRecord.userId + "world"
                if (TextUtils.isEmpty(it.worldRecord.bestScore)) {
                    wordBestSpeed = 0f
                } else {
                    wordBestSpeed = it.worldRecord.bestScore.toFloat()
                }
                PKUserCacheManager.addValue(
                    wordUserId,
                    it.worldRecord.nickName,
                    it.worldRecord.avatar
                );
            }
            if (!TextUtils.isEmpty(it.myRecord.userId)) {
                myRecodeUserId = it.myRecord.userId + "myRecord"
                if (TextUtils.isEmpty(it.myRecord.bestScore)) {
                    myRecodeSpeed = 0f
                } else {
                    myRecodeSpeed = it.myRecord.bestScore.toFloat()
                }
                PKUserCacheManager.addValue(
                    myRecodeUserId,
                    it.myRecord.nickName,
                    it.myRecord.avatar
                );
            }
            bestRecodeBean = it
            LoadImageUtil.getInstance()
                .load(
                    this,
                    it.worldRecord.avatar,
                    iv_hide_head,
                    R.mipmap.friend_icon_default_photo
                )

            handler.postDelayed(Runnable {
                LoadImageUtil.getInstance()
                    .loadDownload(
                        false,
                        this,
                        it.worldRecord.avatar,
                        wordUserId,
                        R.drawable.icon_pk_word_recode, 2, R.mipmap.friend_icon_default_photo
                    )
            }, 3000)

            LoadImageUtil.getInstance()
                .load(
                    this,
                    it.myRecord.avatar,
                    iv_hide_head_recode,
                    R.mipmap.friend_icon_default_photo
                )

            handler.postDelayed(Runnable {
                LoadImageUtil.getInstance()
                    .loadDownload(
                        true,
                        this,
                        it.myRecord.avatar,
                        myRecodeUserId,
                        R.drawable.icon_pk_mine, 5, R.mipmap.friend_icon_default_photo
                    )

            }, 3000)


        })
        mPKmodel.mLeavePk.observe(this, Observer {
            WsManager.getInstance().disConnect()
            video_loader.pause()
            countTimer.stop()
            ActivityLifecycleController.finishAllActivity("MainActivity")
            //finish()
        })

        mBikeDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {

            Log.e("startObserver", "mBikeDeviceConnectViewModel.mDeviceConnState")

            if (BikeConfig.isConn()) {
                hideConnectDialog()
            } else {

            }
            bikeState()
        })

        mBikeDeviceConnectViewModel.mRealData.observe(this, Observer {
            Log.e("mRealData", it.toString())
            mDataList.add(it)
        })


        mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {
            LogUtil.e("startLeScan6")
            if (scanBike) {
                it.forEach {
                    LogUtil.e("startLeScan6" + it.name)
                    if (!TextUtils.isEmpty(it.name)) {
                        if (TextUtils.isEmpty(mBikeName)) {
                            if (it.name.contains(DeviceType.DEVICE_S003.name)
                                || it.name.contains(
                                    DeviceType.DEVICE_S005.name
                                )
                            ) {
                                mDeviceScanViewModel.stopLeScan()
                                mBikeDeviceConnectViewModel.conectBikeDevice(it.device)
                            }
                        } else {
                            if (it.name.equals(mBikeName)) {
                                mDeviceScanViewModel.stopLeScan()
                                mBikeDeviceConnectViewModel.conectBikeDevice(it.device)
                            }
                        }
                    }
                }
            } else {

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

            }

        })
        mHrDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {
            when (it) {
                BikeConfig.BIKE_CONN_DISCONN -> {
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
    }

    fun setItemValue(dis: Float, cal: Float, jk: Double, rpm: Float) {


        Log.e("setItemvalue-----", "" + rpm)

        noiseboardView.setRealTimeValue(BikeConfig.sBikeBean.tzSpeed)
        tv_current_rpm.text = "" + (BikeConfig.sBikeBean.tzSpeed).toInt()

        var strdis = ""
        if (dis >= this.dis.toFloat()) {
            strdis = SiseUtil.disUnitCoversion(this.dis, BikeConfig.userCurrentUtil)
        } else {
            strdis = SiseUtil.disUnitCoversion(dis.toString(), BikeConfig.userCurrentUtil)
        }
        val strCal = SiseUtil.calCoversion(cal.toString())
        val power = SiseUtil.powerCoversion(jk.toString())


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

        itemview_milage_km.setValue(strdis)
        itemview_consume_kcal.setValue(strCal)
        itemview_electric_kj.setValue(power)
        noiseboardView.setRealTimeValue(rpm)
        startBg(rpm.toInt())
        calCurrentResisAndRpm(dis, rpm)
    }

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


    fun releaseRes() {
        mHrDeviceConnectViewModel.disconectDevice()
        onPuserMusic()
        barrageView.destroy()
        video_loader.stopPlayback();
        countTimer.stop()
    }

    override fun onDestroy() {
        PkObservable.getInstance().deleteObserver(this@PKRideActivity)
        TimeOutObservable.getInstance().deleteObserver(this@PKRideActivity)
        releaseRes()
        if (yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {
            yesOrNoDialog!!.dismiss()
        }
        if (dealDialog != null) {
            dealDialog!!.dismiss()
        }
        Log.e("PKRideActivitn", "onDestroy")
        handler.removeMessages(1003)
        handler.removeMessages(1004)
        handler.removeCallbacks(null)
        RealDataObservable.getInstance().deleteObserver(this)
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()

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
                    setHrValue(0)
                    // tv_hr_con_state.setImageResource(R.mipmap.icon_hr_device_discon)
                    //弹出失败对话框

                }
            }
        }
    }

    /**
     * 视频播放代码
     */


    fun calCurrentResisAndRpm(dis: Float, rpm: Float) {

        mResistanceIntervalBean.forEach {

            if (dis >= it.getmIntervalStart() && dis <= it.getmIntervalEnd()) {
                current_resistance = it.getmResistances();
                current_rpm = it.getmRpm()
            }
        }
        SceneUtil.calculateMatchLeverWithCurrentCadence(current_resistance, rpm.toInt())
        if (start_resistance != current_resistance) {
            start_resistance = current_resistance;
            setCurrentResitance()
        }
        if (start_rpm != current_rpm) {
            start_rpm = current_rpm;
            noiseboardView.setTargetSpeed(-1)
            // tv_current_rpm.text = "" + (start_rpm)
        }

    }

    fun setCurrentResitance() {

        tv_current_resistance.text = "" + current_resistance
        mBikeDeviceConnectViewModel.sendQuitData(start_resistance)
    }

    fun rankingRecShow(isShow: Boolean) {
        recycle_props.visibility = View.INVISIBLE
        if (isShow) {
            layout_user_ranking.visibility = View.GONE
            ranking_view.visibility = View.GONE
            layout_ranking_detail.visibility = View.VISIBLE
        } else {
            layout_user_ranking.visibility = View.VISIBLE
            ranking_view.visibility = View.VISIBLE
            layout_ranking_detail.visibility = View.GONE
        }

    }

    fun playSVAG(name: String) {
        parser?.parse(URL(name), object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                var drawable = SVGADrawable(videoItem);
                imageView.setImageDrawable(drawable);
                imageView.startAnimation();

            }

            override fun onError() {
                Log.e("onError", "onError");
                mSendGiftData.removeAt(0)
            }
        })
    }

    fun starSVGa(name: String) {
        imageView.visibility = View.VISIBLE
        mSendGiftData.add(name)
        Log.e("imageViewpoint", "imageViewpoint starSVGa" + mSendGiftData.size)
        if (mSendGiftData.size > 1) {
            return
        }
        playSVAG(name)
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

    override fun onCountTimerChanged(millisecond: Long) {

        var hrValue = 0;
        try {
            this.millisecond = millisecond
            hrValue = currentHr
            currentHr = 0;
            mDataList.clear()
            itemview_power_w.setValue("" + BikeConfig.power)
            if (BikeConfig.speed == 0 && BikeConfig.power == 0) {
                stopPlayMusic()
                BikeUtil.getGlobalBikebean(
                    0,
                    0,
                    millisecond,
                    hrValue
                )
                noiseboardView.setTargetSpeed(-1)
                tv_current_rpm.text = "0"
                noiseboardView.setRealTimeValue(0.toFloat())
                startBg(0)
                // resistance_view.setcurrentDis(0f)
            } else {
                BikeUtil.getGlobalBikebean(
                    BikeConfig.power,
                    BikeConfig.speed,
                    millisecond,
                    hrValue
                )

                BikeConfig.speed = 0
                BikeConfig.power = 0
                //如果距离大于目标距离就提示结束对话框


                if (BikeConfig.sBikeBean != null) {


                    // resistance_view.setcurrentDis(BikeConfig.sBikeBean.dis)
                    rec_char.setcurrentDis(BikeConfig.sBikeBean.dis)


                    //updateRankinglist(BikeConfig.sBikeBean.cal)
                    //发送数据


                    setItemValue(
                        BikeConfig.sBikeBean.dis,
                        BikeConfig.sBikeBean.cal,
                        BikeConfig.sBikeBean.light.toDouble(),
                        BikeConfig.sBikeBean.tzSpeed
                    )

                } else {
                    // resistance_view.setcurrentDis(0f)
                    rec_char.setcurrentDis(0f)
                    setItemValue(
                        0f,
                        0f,
                        0.0, 0f
                    )
                }
            }
            BikeDataCache.saveBike(this, currentPKId, BikeConfig.sBikeBean)
            mConnTime = System.currentTimeMillis()
            if (BikeConfig.sBikeBean.dis >= dis.toInt()) {
                autoendUpdateBikeData()
                countTimer.stop()
                pkState = PkState.END.value
                WsManager.getInstance().sendRealData(
                    mpkId,
                    mUserId,
                    dis.toInt(),
                    "" + PkState.END.value
                )
            } else {
                pkState = PkState.START.value
                WsManager.getInstance().sendRealData(
                    mpkId,
                    mUserId,
                    BikeConfig.sBikeBean.dis.toInt(),
                    "" + PkState.START.value
                )
            }

            if (isHigh) {
                playHeartbeatAnimation()
            }

        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {


        }

    }


}