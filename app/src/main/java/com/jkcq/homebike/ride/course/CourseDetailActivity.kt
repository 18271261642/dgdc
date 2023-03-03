package com.jkcq.homebike.ride.course

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import com.allens.lib_http2.RxHttp
import com.allens.lib_http2.config.HttpLevel
import com.allens.lib_http2.impl.OnDownLoadListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseTitleVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceScanActivity
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.mvvm.viewmodel.SceneridingListModel
import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean
import com.jkcq.homebike.ride.util.CourseUtil
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_course_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File


class CourseDetailActivity : BaseTitleVMActivity<SceneridingListModel>(),
    OnDownLoadListener, CoroutineScope by MainScope() {
    private lateinit var rxHttp: RxHttp

    override fun getLayoutResId(): Int = R.layout.activity_course_detail
    val mSceneridingListModel: SceneridingListModel by lazy { createViewModel(SceneridingListModel::class.java) }

    var currentCourseDetail: SceneBean? = null


    override fun initView() {

        rxHttp = RxHttp.Builder()
            .baseUrl("https://www.wanandroid.com")
            .isLog(true)
            .level(HttpLevel.BODY)
            .writeTimeout(10)
            .readTimeout(10)
            .connectTimeout(10)
            .build(this.applicationContext)
    }

    var sceneId = ""

    override fun initData() {
        btn_download.setTag("fail")
        sceneId = intent.getStringExtra("sceneId")
        setTitleText(this.resources.getString(R.string.bike_type_course_detail))
        initPermission()
        mSceneridingListModel.getCourseList(sceneId)

    }

    override fun onResume() {
        super.onResume()
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
                            this@CourseDetailActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(null)
    }

    override fun startObserver() {
        mSceneridingListModel.mSingeSceneBean.observe(this, Observer {
            currentCourseDetail = it
            setValue()
            var list = CourseUtil.getLineBeanList(CourseUtil.splitSemicolonStr(it.slope))
            mResistanceIntervalBean.addAll(
                CourseUtil.getResisteanceList(
                    list,
                    it.videoDuration.toInt()
                )
            )
            tv_courseDetailChar.setdata(mResistanceIntervalBean, it.videoDuration.toInt())
        })
    }

    var mResistanceIntervalBean = mutableListOf<ResistanceIntervalBean>()

    fun setValue() {

        tv_course_name.text = currentCourseDetail?.name
        LoadImageUtil.getInstance()
            .loadCirc(
                this,
                currentCourseDetail?.imageUrl,
                iv_scene_pic,
                DateUtil.dip2px(20f)
            )

        val temp: String = currentCourseDetail!!.description.replace(
            "\\n",
            "\n"
        )
        tv_description.text = temp
        val hms: DateUtil.HMS =
            DateUtil.getHMSFromMillis(currentCourseDetail!!.videoDuration.toLong() * 1000)
        tv_time.text = String.format(
            "%02d:%02d",
            hms.getMinute(),
            hms.getSecond()
        )

        var difRes = "★★☆☆☆"

        when (currentCourseDetail!!.difficulty) {
            "1" -> {
                difRes = "★☆☆☆☆"
            }
            "2" -> {
                difRes = "★★☆☆☆"
            }
            "3" -> {
                difRes = "★★★☆☆"
            }
            "4" -> {
                difRes = "★★★★☆"
            }
            "5" -> {
                difRes = "★★★★★"
            }
            else -> {
                difRes = "★★★★★"
            }
        }
        tv_difficulty.text = difRes

        isDownload()
    }


    fun isDownload() {
        var isDown = fileIsDownload()
        if (isDown) {
            btn_download.setTag("success")
            btn_download.text = this.getString(R.string.started_riding)
        } else {
            btn_download.text = this.getString(R.string.file_download)
            btn_download.setTag("fail")
        }
    }

    override fun ivRight() {
    }

    override fun initEvent() {
        super.initEvent()
        btn_download.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            if (btn_download.getTag().equals("success")) {
                starCourseRide()
            } else {
                if (currentCourseDetail == null) {
                    return@setOnClickListener
                }
                downSenceLine(
                    currentCourseDetail!!.id.toString(),
                    currentCourseDetail!!.videoUrl
                )
            }
        }
    }


    var updateDialog: UpdateDialog? = null


    fun fileIsDownload(): Boolean {
        var videourl = currentCourseDetail!!.videoUrl;
        var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))
        val file = File(
            FileUtil.getVideoCorseDir().toString() + currentSencFileName
        )
        return file.exists() && file.length() >= currentCourseDetail!!.videoSize.toFloat()
    }


    fun starCourseRide() {
        //
        currentSencFileName =
            currentCourseDetail!!.videoUrl.substring(currentCourseDetail!!.videoUrl.lastIndexOf("/"))
        if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_SUCCESS) {
            // if (true) {
            var intent = Intent(this@CourseDetailActivity, CourseRideActivity::class.java)
            intent.putExtra("path", currentSencFileName);
            intent.putExtra("sceneId", currentCourseDetail!!.id.toString());
            intent.putExtra("name", currentCourseDetail!!.name);
            intent.putExtra(
                "dis",
                currentCourseDetail!!.videoDuration.toFloat().toInt().toString()
            );
            intent.putExtra("slope", currentCourseDetail!!.slope);
            /* intent.putExtra(
                 "bean",
                 mDataList.get(position)
             )*/
            startActivity(intent)

        } else {
            var dialog = this?.let { it1 ->
                YesOrNoDialog(
                    it1,
                    "",
                    it1.resources.getString(R.string.dialog_con_bike_tips),
                    "",
                    ""
                )
            }
            dialog?.setBtnOnclick(object : OnButtonListener {
                override fun onCancleOnclick() {

                }

                override fun onSureOnclick() {
                    dialog?.dismiss()
                    var intent = Intent(
                        this@CourseDetailActivity,
                        BikeDeviceScanActivity::class.java
                    )
                    intent.putExtra("resutl", true)
                    startActivityForResult(
                        intent, 100
                    )
                }
            })
            dialog?.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        starCourseRide()
    }

    private fun isWifiOpened(): Boolean {

        return NetUtil.checkWifiState(this)
    }

    var currentSencFileName = "";
    var currentVideoUrl = ""
    fun downSenceLine(id: String, videourl: String) {

        goDownLoad(id, videourl)


    }

    var yesOrNoDialog: YesOrNoDialog? = null
    var downLoadFailDialog: YesOrNoDialog? = null
    fun goDownLoad(id: String, videourl: String) {
        if (updateDialog != null && updateDialog!!.isShowing) {
            return
        }
        updateDialog = UpdateDialog(
            this,
            1,
            this.getResources().getString(R.string.download_course_video_tips)
        )
        updateDialog?.setOnDialogClickListener(OnDialogClickListener {

            when (it) {
                1 -> {
                    //开始下载
                    try {
                        currentSencFileName = videourl.substring(videourl.lastIndexOf("/") + 1)
                        currentVideoUrl = videourl

                        Log.e("startDownLoad", "startDownLoad")
                        val currentvalue =
                            AppUpdateUtils.div(0.0, 1024 * 1024.toDouble(), 1)
                                .toString() + "M"
                        val totaltvalue = "/" + AppUpdateUtils.div(
                            currentCourseDetail!!.videoSize.toDouble(),
                            1024 * 1024.toDouble(),
                            1
                        ) + "M"
                        updateDialog?.setTvPackgeSize(currentvalue, totaltvalue)
                        launch {
                            startDownLoad(id, currentVideoUrl, currentSencFileName)
                        }

                    } catch (e: Exception) {
                    }
                }
                2 -> {
                    //取消下载
                    if (yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {
                        return@OnDialogClickListener
                    }
                    yesOrNoDialog = YesOrNoDialog(
                        this,
                        "",
                        this.resources.getString(R.string.cancle_download_tips),
                        "",
                        this.resources.getString(R.string.dialog_ok)
                    )
                    launch {
                        pauseDownLoad(id)
                    }
                    yesOrNoDialog?.show()
                    yesOrNoDialog?.setBtnOnclick(object : OnButtonListener {
                        override fun onCancleOnclick() {
                            launch {
                                startDownLoad(id, currentVideoUrl, currentSencFileName)
                            }
                            yesOrNoDialog?.dismiss()
                            yesOrNoDialog = null
                        }

                        override fun onSureOnclick() {
                            //mAliYunModel.cancelDownTask()
                            pauseDownLoad(id)
                            // DownloadUtils.getInstance().downLoadSenceCancle()
                            disUpdateDialog()
                        }
                    })


                }
                3 -> {
                    disUpdateDialog()
                }
                //下载成功
                4 -> {
                    disUpdateDialog()
                    btn_download.setTag("success")
                    btn_download.text = this.getString(R.string.started_riding)
                }
            }
        })
        updateDialog?.show()
    }


    private suspend fun startDownLoad(
        tag: String,
        url: String,
        saveName: String
    ) {
        Log.e("startDownLoad", "startDownLoad" + tag)
        rxHttp.create().doDownLoad(tag, url, FileUtil.getTempDir(), saveName, this)
    }

    fun disUpdateDialog() {
        isDownload()
        if (updateDialog!!.isShowing) {
            updateDialog?.dismiss()
            updateDialog = null
        }
    }


    private fun pauseDownLoad(targId: String) {
        rxHttp.create().doDownLoadPause(targId)
    }


    private fun cancelDownLoad() {
        // rxHttp.create().doDownLoadCancelAll()
    }


    var handler = Handler()


    override fun onDownLoadPrepare(key: String) {
        Log.e("down", "onDownLoadPrepare")
        //  TODO("Not yet implemented")
    }

    override fun onDownLoadProgress(key: String, progress: Int) {
        updateDialog?.updateProgress(progress)
        Log.e("down", "progress=" + progress)
        //  TODO("Not yet implemented")
    }

    override fun onDownLoadError(key: String, throwable: Throwable) {

        //下载失败
        if (downLoadFailDialog != null && downLoadFailDialog!!.isShowing) {
            return
        }
        downLoadFailDialog = YesOrNoDialog(
            this,
            "",
            this.resources.getString(R.string.file_downlod_fail_tips),
            "",
            this.resources.getString(R.string.dialog_ok),
            false
        )
        downLoadFailDialog?.show()
        downLoadFailDialog?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {

            }

            override fun onSureOnclick() {
                if (!NetUtil.isNetworkConnected(this@CourseDetailActivity)) {
                    updateDialog?.dismiss();
                    updateDialog = null
                    ToastUtil.showTextToast(
                        BaseApp.sApplicaton,
                        this@CourseDetailActivity.getString(R.string.net_error)
                    )
                    return
                }
                launch {
                    startDownLoad(
                        currentCourseDetail!!.id.toString(),
                        currentVideoUrl,
                        currentSencFileName
                    )
                }
                downLoadFailDialog?.dismiss()
                downLoadFailDialog = null

            }
        })
        Log.e("down", "onDownLoadError=" + key + "--------" + throwable.message)
        //  TODO("Not yet implemented")
    }

    override fun onDownLoadSuccess(key: String, path: String) {
        Log.e("down", "onDownLoadSuccess=" + path)
        FileUtil.copyFile(path, FileUtil.getVideoCorseDir() + File.separator + currentSencFileName)
        FileUtil.deletTempFile(path)
        updateDialog?.updateSuccess()

        //把文件


        // TODO("Not yet implemented")
    }

    override fun onDownLoadPause(key: String) {
        Log.e("down", "onDownLoadPause=")
        // TODO("Not yet implemented")
    }

    override fun onDownLoadCancel(key: String) {
        Log.e("down", "onDownLoadCancel=")
        // TODO("Not yet implemented")
    }

    override fun onUpdate(key: String, progress: Int, read: Long, count: Long, done: Boolean) {


        val currentvalue =
            AppUpdateUtils.div(read.toDouble(), 1024 * 1024.toDouble(), 1)
                .toString() + "M"
        val totaltvalue = "/" + AppUpdateUtils.div(
            count.toDouble(),
            1024 * 1024.toDouble(),
            1
        ) + "M"
        updateDialog?.setTvPackgeSize(currentvalue, totaltvalue)

        Log.e("down", "onUpdate=")
        // TODO("Not yet implemented")
    }
}