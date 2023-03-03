package com.jkcq.homebike.ride.pk

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.ActivityLifecycleController
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.login.WebHitoryViewActivity
import com.jkcq.homebike.ride.mvvm.viewmodel.PKModel
import com.jkcq.homebike.ride.pk.adapter.PKResultListAdapter
import com.jkcq.homebike.ride.pk.bean.PKResultBean
import com.jkcq.homebike.ride.pk.bean.enumbean.PkBestState
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.CircleImageView
import kotlinx.android.synthetic.main.activity_pk_list.*
import kotlinx.android.synthetic.main.activity_pk_result.*
import kotlinx.android.synthetic.main.activity_pk_result.recyclerview_sport
import kotlinx.android.synthetic.main.activity_pk_result.video_loader
import kotlinx.android.synthetic.main.activity_pk_ride.*
import kotlinx.android.synthetic.main.activity_prepare_pk.*
import kotlinx.android.synthetic.main.fragment_top_three_layout.*
import kotlinx.android.synthetic.main.fragment_top_two_layout.*
import java.io.File
import java.io.IOException


class PKResultActivity : BaseVMActivity<PKModel>() {

    var mSceneBean: SceneBean? = null;
    var mPkId = ""
    override fun getLayoutResId(): Int = R.layout.activity_pk_result
    val mPKModel: PKModel by lazy { createViewModel(PKModel::class.java) }

    //运动详情的初始化
    var mDataList = mutableListOf<PKResultBean>()
    lateinit var mSceneRidingListAdapter: PKResultListAdapter

    override fun initView() {
    }

    fun startVideo() {

        Log.e("startVideo", "startVideo")
        if (video_loader.isPlaying) {
            return
        }

        video_loader.setOnPreparedListener {
            it.setScreenOnWhilePlaying(true);
            it.isLooping = true
            video_loader.start()
            it.setVolume(0.01f, 0.01f)
        }
        startPlayMusic()
        // video_loader.requestFocus();


    }

    var path = ""
    override fun initData() {
        mSceneBean = intent.getSerializableExtra("scene") as SceneBean?
        mPkId = intent.getStringExtra("mPkId")
        initDataPlayer(mSceneBean!!.audioUrl)
        path = mSceneBean!!.videoUrl.substring(mSceneBean!!.videoUrl.lastIndexOf("/"))
        mPKModel.getPKResultList(mPkId)
        mPKModel.getPKRestutUrl()
        //  initPermission()
        initSportDetailRec()
        Log.e("initData", "path=" + path + "mPkId=" + mPkId)

        val file = File(
            FileUtil.getVideoDir() + File.separator + path
        )

        video_loader.setVideoURI(Uri.parse(file.path))
        // startVideo()


    }


    //适用场景：0：PK，1：实景，2：PK和实景

    override fun onResume() {
        super.onResume()
        startVideo()
        // mPKModel.getSceneList("" + BikeConfig.BIKE_TYPE, "0")
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
                            this@PKResultActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        // rxHttp.create().doDownLoadCancelAll()
        onPuserMusic()
        handler.removeCallbacks(null)
    }


    var count = 2
    var shareUrl = ""

    override fun startObserver() {
        mPKModel.shareUrl.observe(this, Observer {
            shareUrl = it
        })
        mPKModel.mPKResultList.observe(this, Observer {

            var i = 0
            var bean: PKResultBean? = null;
            count = it.size
            if (count == 2) {
                layout_three_layout.visibility = View.GONE
                layout_two_layout.visibility = View.VISIBLE
                layout_result.visibility = View.GONE
            } else {
                layout_three_layout.visibility = View.VISIBLE
                layout_two_layout.visibility = View.GONE
                layout_result.visibility = View.VISIBLE
            }
            mDataList.clear()
            mDataList.addAll(it)
            while (i < mDataList.size) {
                bean = mDataList.get(i)
                if (i == 0) {
                    Log.e(
                        "setBestAllValue",
                        "count=" + count + ",bean.isFinishFlag=" + bean.isFinishFlag
                    )
                    if (count == 2) {
                        setBestAllValue(
                            bean.avatar,
                            iv_two_one,
                            bean.nickName,
                            tv_two_one_nikename,
                            bean.durationMillis,
                            tv_two_one_time
                        )
                        if (bean.isFinishFlag) {
                            setBestValue(tv_two_one_best_value, bean.recordBreakingStatus)
                        }

                    } else {
                        if (bean.isFinishFlag) {
                            setBestAllValue(
                                bean.avatar,
                                iv_one,
                                bean.nickName,
                                tv_one_nikename,
                                bean.durationMillis,
                                tv_one_time
                            )
                            setBestValue(tv_one_best_value, bean.recordBreakingStatus)
                        } else {
                            iv_one.visibility = View.INVISIBLE
                        }
                    }
                } else if (i == 1) {


                    if (count == 2) {
                        setBestAllValue(
                            bean.avatar,
                            iv_two_two,
                            bean.nickName,
                            tv_two_two_nikename,
                            bean.durationMillis,
                            tv_two_two_time
                        )
                        if (bean.isFinishFlag) {
                            setBestValue(tv_two_two_best_value, bean.recordBreakingStatus)
                        }

                    } else {
                        if (bean.isFinishFlag) {

                            if (bean.isFinishFlag) {
                                setBestAllValue(
                                    bean.avatar,
                                    iv_two,
                                    bean.nickName,
                                    tv_two_nikename,
                                    bean.durationMillis,
                                    tv_two_time
                                )
                                setBestValue(tv_two_best_value, bean.recordBreakingStatus)
                            }
                        } else {
                            iv_two.visibility = View.INVISIBLE
                        }

                    }

                } else if (i == 2) {
                    if (bean.isFinishFlag) {

                        setBestAllValue(
                            bean.avatar,
                            iv_three,
                            bean.nickName,
                            tv_three_nikename,
                            bean.durationMillis,
                            tv_three_time
                        )
                        setBestValue(tv_three_best_value, bean.recordBreakingStatus)
                    } else {
                        iv_three.visibility = View.INVISIBLE
                    }
                }

                bean.ranking = i + 1
                i++
            }
            //把前三名取出来
            if (count != 2) {
                mSceneRidingListAdapter.notifyDataSetChanged()
            }


        })
    }


    fun setBestAllValue(
        avatar: String,
        ivHead: CircleImageView,
        nickName: String,
        tvNikeName: TextView,
        durationMillis: String,
        tvTime: TextView
    ) {


        Log.e("setBestAllValue", "avatar=" + avatar + ",nickName=" + nickName)

        LoadImageUtil.getInstance()
            .load(
                this@PKResultActivity,
                avatar,
                ivHead,
                R.mipmap.friend_icon_default_photo
            )
        tvNikeName.text = nickName
        tvTime.text =
            SiseUtil.timeCoversionMin((durationMillis.toInt() / 1000).toString())
    }

    fun setBestValue(textView: TextView, status: Int) {
        when (status) {
            PkBestState.NO_RECORD.value -> {
                textView.text = (this.getString(R.string.Pk_complety))
                textView.visibility = View.INVISIBLE
            }
            PkBestState.PERSONAL_RECORDS.value -> {
                textView.text = (this.getString(R.string.PK_person))
                textView.visibility = View.VISIBLE
            }
            PkBestState.WORLD_RECORD.value -> {
                textView.text = (this.getString(R.string.Pk_world))
                textView.visibility = View.VISIBLE
            }
        }

    }


    override fun initEvent() {
        super.initEvent()

        tv_end_share.setOnClickListener {

            if (TextUtils.isEmpty(shareUrl)) {
                ToastUtil.showTextToast(
                    BaseApp.sApplicaton,
                    this@PKResultActivity.getString(R.string.data_is_processing)
                )
                return@setOnClickListener
            }
            var intent = Intent(this, WebHitoryViewActivity::class.java)
            intent.putExtra("showOne", true)
            intent.putExtra(
                "lighturl",
                shareUrl + "?pkId=" + mPkId + "&userId=" + mUserId
            )
            intent.putExtra(
                "darkurl",
                shareUrl + "?pkId=" + mPkId + "&userId=" + mUserId
            )

            intent.putExtra("title", this.getString(R.string.sport_pk_rank_title))
            ActivityLifecycleController.finishAllActivity("MainActivity")
            startActivity(intent)
        }

        tv_end_history.setOnClickListener {
            if (TextUtils.isEmpty(BikeConfig.pPUpdateId)) {
                return@setOnClickListener
            }
            var intent = Intent(this, WebHitoryViewActivity::class.java)
            intent.putExtra(
                "lighturl",
                BikeConfig.detailUrlBean.light + "?exerciseId=" + BikeConfig.pPUpdateId + "&userId=" + mUserId
            )
            intent.putExtra(
                "darkurl",
                BikeConfig.detailUrlBean.dark + "?exerciseId=" + BikeConfig.pPUpdateId + "&userId=" + mUserId
            )
            intent.putExtra("title", this.getString(R.string.sport_detail_title))
            ActivityLifecycleController.finishAllActivity("MainActivity")
            startActivity(intent)
        }


        iv_back.setOnClickListener {
            ActivityLifecycleController.finishAllActivity("MainActivity")
        }
    }


    fun initSportDetailRec() {
        recyclerview_sport.layoutManager = LinearLayoutManager(this)
        mSceneRidingListAdapter = PKResultListAdapter(mDataList)
        recyclerview_sport.adapter = mSceneRidingListAdapter

    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

    fun downSenceLine(id: String, videourl: String) {


    }

    var yesOrNoDialog: YesOrNoDialog? = null


    var handler = Handler()
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
                        //  player!!.setVolume(0.2f, 0.2f);
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT")
                    }
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->                     //短暂性丢失焦点并作降音处理
                        Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                    AudioManager.AUDIOFOCUS_GAIN -> {
                        //当其他应用申请焦点之后又释放焦点会触发此回调
                        //可重新播放音乐
                        // player!!.setVolume(0.5f, 0.5f);
                        Log.d(TAG, "AUDIOFOCUS_GAIN")
                        // start()
                    }
                }
            }
        }

    fun startPlayMusic() {
        handler.postDelayed(Runnable {
            try {
                Log.e("startPlayMusic", "startMusic")
                if (player != null && player!!.isPlaying()) {
                    // player!!.prepare()
                    Log.e("startPlayMusic", "startMusic2")
                    player!!.setVolume(1f, 1f)
                    Log.e("startPlayMusic", "startMusic2")
                    // player!!.setLooping(true);
                } else {
                    Log.e("startPlayMusic", "startMusic1")
                    player!!.start()
                    player!!.setVolume(1f, 1f)
                    Log.e("startPlayMusic", "startMusic1")
                }
            } catch (e: Exception) {

            }
        }, 2000)

    }

    fun onPuserMusic() {
        if (player != null && player!!.isPlaying) {
            player!!.stop()

        }
    }


}