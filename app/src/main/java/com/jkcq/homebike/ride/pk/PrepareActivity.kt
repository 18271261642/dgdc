package com.jkcq.homebike.ride.pk

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.utillibrary.PermissionUtil
import com.example.websocket.PKUserCacheManager
import com.example.websocket.Util
import com.example.websocket.WsManager
import com.example.websocket.bean.JoinUser
import com.example.websocket.bean.LeaveBean
import com.example.websocket.bean.PKType
import com.example.websocket.bean.PkUsers
import com.example.websocket.observable.PkObservable
import com.example.websocket.observable.TimeOutObservable
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.base.observable.LoginOutObservable
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.mvvm.viewmodel.PKModel
import com.jkcq.homebike.ride.pk.adapter.PrepareListAdapter
import com.jkcq.homebike.ride.pk.bean.PKRoomBean
import com.jkcq.homebike.ride.pk.bean.enumbean.PkState
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_pk_result.*
import kotlinx.android.synthetic.main.activity_pk_ride.*
import kotlinx.android.synthetic.main.activity_prepare_pk.*
import kotlinx.android.synthetic.main.activity_prepare_pk.recyclerview_sport
import kotlinx.android.synthetic.main.activity_prepare_pk.video_loader
import kotlinx.android.synthetic.main.activity_scene_ride.*
import java.io.File
import java.io.IOException
import java.util.*


class PrepareActivity : BaseVMActivity<PKModel>() {

    var mpkId: String by Preference(Preference.PK_ID, "")
    var mAudioUrl = ""
    var msceneBean: SceneBean? = null
    var mPKRoomBean: PKRoomBean? = null
    var mDataList = mutableListOf<PkUsers>()
    var sumNumber = "0"
    var pkname = ""
    var currentNumber = 0
    var pkId = ""
    var path = ""
    var isCreate = false;
    override fun getLayoutResId(): Int = R.layout.activity_prepare_pk
    val mpkModel: PKModel by lazy { createViewModel(PKModel::class.java) }

    //运动详情的初始化
    lateinit var mSceneRidingListAdapter: PrepareListAdapter

    override fun initView() {


    }

    fun startPlayMusic() {
        video_loader.start()
        startMusic()
    }


    fun startMusic() {
        Log.e("startPlayMusic", "startMusic")

        handler.postDelayed(Runnable {
            try {
                Log.e("startPlayMusic", "startMusic")
                if (player != null && player!!.isPlaying()) {
                    // player!!.prepare()
                    Log.e("startPlayMusic", "startMusic2")
                    player!!.setVolume(0.5f, 0.5f)
                    Log.e("startPlayMusic", "startMusic2")
                    // player!!.setLooping(true);
                } else {
                    Log.e("startPlayMusic", "startMusic1")
                    player!!.start()
                    player!!.setVolume(0.5f, 0.5f)
                    Log.e("startPlayMusic", "startMusic1")
                }
            } catch (e: Exception) {

            }
        }, 2000)


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

                Log.e("setOnPreparedListener", "setOnPreparedListener")

                //player!!.start()
                // player!!.setVolume(1f, 1f);
            })
            //10 异步准备
            player!!.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun onPuserMusic() {
        if (player != null && player!!.isPlaying) {
            player!!.stop()
        }
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
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT")
                    }
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->                     //短暂性丢失焦点并作降音处理
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                    AudioManager.AUDIOFOCUS_GAIN -> {
                        //当其他应用申请焦点之后又释放焦点会触发此回调
                        //可重新播放音乐
                        Log.d(TAG, "AUDIOFOCUS_GAIN")
                        // start()
                    }
                }
            }
        }


    override fun onBackPressed() {
    }

    fun startVideo() {
        startPlayMusic()

    }


    override fun initData() {
        TimeOutObservable.getInstance().addObserver(this)
        PkObservable.getInstance().addObserver(this)
        msceneBean = intent.getSerializableExtra("scene") as SceneBean?
        mAudioUrl = msceneBean!!.audioUrl;

        Log.e("mAudioUrl", "mAudioUrl=" + mAudioUrl)

        mPKRoomBean = intent.getSerializableExtra("pkinfo") as PKRoomBean?
        mDataList.addAll(mPKRoomBean!!.pkUsers)
        PKUserCacheManager.addPkUsers(mPKRoomBean!!.pkUsers)
        PKUserCacheManager.clearMap()
        pkId = mPKRoomBean!!.cyclingPk.id
        mDataList.sort()
        Util.token = mtoken
        Util.userId = mUserId
        Util.roomId = mpkId
        WsManager.getInstance().connetSocket(mUserId, pkId, mtoken)
        isCreate = false
        mDataList.forEach {
            PKUserCacheManager.addValue(it.userId, it.nickName, it.avatar)
            if (it.isCreatorFlag) {
                if (mUserId.equals(it.userId)) {
                    isCreate = true;
                }
            }
        }
        isCreate(isCreate)
        sumNumber = mPKRoomBean!!.cyclingPk.participantNum
        pkname = mPKRoomBean!!.cyclingPk.pkName

        currentNumber = mDataList.size


        setFull()

        initPermission()
        initSportDetailRec()

        path = msceneBean!!.videoUrl.substring(msceneBean!!.videoUrl.lastIndexOf("/") + 1)


        Log.e("initData", "path=" + path)

        val file = File(
            FileUtil.getVideoDir() + File.separator + path
        )

        video_loader.setVideoURI(Uri.parse(file.path))
        initDataPlayer(mAudioUrl)
        video_loader.setOnPreparedListener {
            //mediaPlayer = it
            //setPlaySpeed(0f)
            Log.e("setOnPreparedListener", "setOnPreparedListener")
            // video_loader.start()
            it.setVolume(0f, 0f)
            //player!!.setVolume(1f, 1f)
            it.setScreenOnWhilePlaying(true);
            it.isLooping = true

        }

        //  startVideo()

    }


    //适用场景：0：PK，1：实景，2：PK和实景

    override fun onResume() {
        super.onResume()
        // mpkModel.findPKState()
        //mpkModel.getSceneList("" + BikeConfig.BIKE_TYPE, "0")
        startVideo()

    }


    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)

        Log.e("update", "-----" + o)
        runOnUiThread {
            if (o is TimeOutObservable) {
                if (arg == 0) {
                    endPk(this@PrepareActivity.getString(R.string.connect_timeout_exit_PK))
                } else if (arg == 1) {
                    mpkModel.findPKState()
                }
            } else if (o is PkObservable) {
                if (arg is LeaveBean) {
                    // findUserById(arg.userId)
                    updateUserInfo()
                    mDataList.sort()
                    setFull()
                    mSceneRidingListAdapter.notifyDataSetChanged()
                } else if (arg is JoinUser) {
                    updateUserInfo()
                    mDataList.sort()
                    setFull()
                    mSceneRidingListAdapter.notifyDataSetChanged()
                } else if (arg is Int) {
                    when (arg) {
                        PKType.LOGOUT.value -> {
                            WsManager.getInstance().disConnect()
                            mpkId = ""
                            LoginOutObservable.getInstance().show()
                        }
                        PKType.START.value -> {
                            startPk()
                        }
                        PKType.DESTROY.value -> {
                            endPk(this@PrepareActivity.getString(R.string.pk_destory_pk_tips))
                        }
                    }


                }
            }


        }
    }


    fun setFull() {


        Log.e("setFull", "currentNumber=" + currentNumber + "sumNumber=" + sumNumber)

        if (currentNumber.toString().equals(sumNumber)) {
            tv_pk_name.text = String.format(
                this.getString(R.string.Pk_prepare_full),
                mPKRoomBean!!.cyclingPk.pkName
            )
        } else {
            tv_pk_name.text = String.format(
                this.getString(R.string.Pk_prepare_title),
                mPKRoomBean!!.cyclingPk.pkName,
                "" + currentNumber,
                sumNumber
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // PkObservable.getInstance().deleteObserver(this)
    }

    fun initPermission() {
        PermissionUtil.checkPermission(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissonCallback = object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {
                        //toast("success")
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@PrepareActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        // rxHttp.create().doDownLoadCancelAll()
        PkObservable.getInstance().deleteObserver(this)
        TimeOutObservable.getInstance().deleteObserver(this)
        onPuserMusic()
        video_loader.stopPlayback()
        handler.removeCallbacks(null)
    }

    override fun startObserver() {
        mpkModel.mCanJoin.observe(this, Observer {

            when (it.pkStatus) {
                PkState.UNSTART.value -> {
                    // WsManager.getInstance().connetSocket(mUserId, pkId, mtoken)
                    // reJoin(it.id)
                }
                PkState.START.value -> {
                    startPk()
                }
                PkState.END.value -> {
                    endPk(this@PrepareActivity.getString(R.string.connect_one_exit_PK_end))
                }
                PkState.DESTROY.value -> {
                    endPk(this.resources.getString(R.string.pk_destory_pk_tips))
                }
                PkState.COLLECT_DATA.value -> {
                    endPk(this@PrepareActivity.getString(R.string.connect_one_exit_PK_end))
                }
            }
        })
        mpkModel.mPKDestory.observe(this, Observer {
            endPk(this.resources.getString(R.string.pk_destory_pk_tips))
        })
        mpkModel.mLeavePk.observe(this, Observer {
            if (it) {
                WsManager.getInstance().disConnect()
                finish()
            }
        })
        mpkModel.mStartPk.observe(this, Observer {
            if (it) {
                // startPk()
            }
        })
    }


    override fun initEvent() {
        super.initEvent()

        //判断是否是房主


        tv_end_pk.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            var tips = resources.getString(R.string.pk_end_pk_tips)
            if (!isCreate) {
                tips = resources.getString(R.string.pk_signout_pk_tips)
            }

            var dialog = this?.let { it1 ->
                YesOrNoDialog(
                    it1,
                    "",
                    tips,
                    "",
                    ""
                )
            }
            dialog?.setBtnOnclick(object : OnButtonListener {
                override fun onCancleOnclick() {
                }

                override fun onSureOnclick() {
                    PkObservable.getInstance().deleteObserver(this@PrepareActivity)
                    mpkModel.leavePk(pkId, isCreate)
                }
            })
            dialog?.show()
        }


        iv_start.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            if (currentNumber < 2) {
                var dialog = this?.let { it1 ->
                    YesOrNoDialog(
                        it1,
                        "",
                        it1.resources.getString(R.string.pk_start_number_tips),
                        "",
                        "", false
                    )
                }
                dialog?.setBtnOnclick(object : OnButtonListener {
                    override fun onCancleOnclick() {
                        //finish()
                    }

                    override fun onSureOnclick() {
                        //mpkModel.startPk(pkId)
                        //  startPk()
                    }
                })
                dialog?.show()
                return@setOnClickListener
            } else {
                mpkModel.startPk(pkId)
            }

        }

    }

    fun updateUserInfo() {
        //  PKUserCacheManager.addValue(userInfo.userId, userInfo.nickName, userInfo.avatar)
        mDataList.clear();
        currentNumber = PKUserCacheManager.mPkUsers.size
        PKUserCacheManager.mPkUsers.forEach {
            mDataList.add(it)
        }
        addDefuUser()
        /* var i = 0
         while (i < mDataList.size) {
             var bean = mDataList.get(i)
             if (bean.userId.equals("0")) {
                 bean.nickName = userInfo.nickName
                 bean.avatar = userInfo.avatar;
                 bean.createTimestamp = userInfo.createTimestamp
                 bean.isCreatorFlag = false;
                 bean.pkRecordId = userInfo.pkId
                 bean.userId = userInfo.userId
                 break
             }
             i++
         }*/
    }

    fun findUserById(userId: String) {
        var i = 0

        while (i < mDataList.size) {
            if (mDataList.get(i).userId.equals(userId)) {
                mDataList.removeAt(i)
                break
            }
            i++
        }
    }

    fun addDefuUser() {
        var i = mDataList.size
        while (i < sumNumber.toInt()) {
            mDataList.add(PkUsers("0", this.getString(R.string.Pk_watting), false))
            i++
        }
    }

    fun initSportDetailRec() {
        recyclerview_sport.layoutManager = GridLayoutManager(this, 2)
        addDefuUser()
        mSceneRidingListAdapter = PrepareListAdapter(mDataList)
        recyclerview_sport.adapter = mSceneRidingListAdapter

    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, tv_pk_name)
        StatusBarUtil.setLightMode(this)
    }

    fun downSenceLine(id: String, videourl: String) {


    }

    var yesOrNoDialog: YesOrNoDialog? = null


    var handler = Handler()


    fun isCreate(isShow: Boolean) {
        if (isShow) {
            iv_start.visibility = View.VISIBLE
        } else {
            iv_start.visibility = View.GONE
        }
    }


    fun endPk(value: String) {
        video_loader.pause()
        onPuserMusic()
        WsManager.getInstance().disConnect()
        var dialog = this?.let { it1 ->
            YesOrNoDialog(
                it1,
                "",
                value,

                "",
                "", false
            )
        }
        dialog?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {

            }

            override fun onSureOnclick() {
                onPuserMusic()
                finish()
            }
        })
        dialog?.show()
        mpkId = ""

    }

    fun startPk() {
        var intent = Intent(this@PrepareActivity, PKRideActivity::class.java)
        mpkId = mPKRoomBean!!.cyclingPk.id
        msceneBean!!.name = mPKRoomBean!!.cyclingPk.pkName
        intent.putExtra("scene", msceneBean)
        intent.putExtra("isReJoin", false)
        intent.putExtra("mpkId", mPKRoomBean!!.cyclingPk.id)
        onPuserMusic()
        PKUserCacheManager.addPkUsers(mDataList)
        startActivity(intent)
        finish()

    }


}