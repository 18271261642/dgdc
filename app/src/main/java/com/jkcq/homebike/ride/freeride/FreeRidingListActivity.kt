package com.jkcq.homebike.ride.freeride

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.allens.lib_http2.RxHttp
import com.allens.lib_http2.config.HttpLevel
import com.allens.lib_http2.impl.OnDownLoadListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseTitleVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceScanActivity
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.freeride.adpter.FreeListAdapter
import com.jkcq.homebike.ride.mvvm.viewmodel.SceneridingListModel
import com.jkcq.homebike.ride.pk.PKResultActivity
import com.jkcq.homebike.ride.pk.PKRideActivity
import com.jkcq.homebike.ride.sceneriding.SceneManageActivity
import com.jkcq.homebike.ride.sceneriding.SceneRideActivity
import com.jkcq.homebike.ride.sceneriding.adpter.SceneRidingListAdapter
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_scene_riding.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File


class FreeRidingListActivity : BaseTitleVMActivity<SceneridingListModel>(), OnDownLoadListener,
    CoroutineScope by MainScope() {


    var from = false


    private lateinit var rxHttp: RxHttp
    override fun getLayoutResId(): Int = R.layout.activity_scene_riding
    val mSceneridingListModel: SceneridingListModel by lazy { createViewModel(SceneridingListModel::class.java) }

    //运动详情的初始化
    var mDataList = mutableListOf<SceneBean>()
    lateinit var mSceneRidingListAdapter: FreeListAdapter

    override fun initView() {
        // showFragment(FRAGMENT_DAY)
        // rg_main.check(R.id.rbtn_day)


    }

    override fun initData() {
        from = intent.getBooleanExtra("from", false);
        setTitleText(this.resources.getString(R.string.bike_free_bike_title))
        //  setIvRightIcon(R.drawable.icon_scene_manage)
        initPermission()
        initSportDetailRec()

    }

    //    适用场景：0：PK，1：实景，2：PK和实景
    override fun onResume() {
        super.onResume()
        mSceneridingListModel.getFreeBackgroundList()
        /*if (from) {
            mSceneridingListModel.getSceneList("" + BikeConfig.BIKE_TYPE, "0")
        } else {
            mSceneridingListModel.getSceneList("" + BikeConfig.BIKE_TYPE, "1")
        }*/
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
                            this@FreeRidingListActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        // rxHttp.create().doDownLoadCancelAll()
        handler.removeCallbacks(null)
    }

    override fun startObserver() {
        mSceneridingListModel.mSceneBean.observe(this, Observer {
            mDataList.clear()
            mDataList.addAll(it)
            mSceneRidingListAdapter.notifyDataSetChanged()

        })
    }

    override fun ivRight() {
        startActivity(Intent(this, SceneManageActivity::class.java))
    }

    override fun initEvent() {
        super.initEvent()
        rxHttp = RxHttp.Builder()
            .baseUrl("https://www.wanandroid.com")
            .isLog(true)
            .level(HttpLevel.BODY)
            .writeTimeout(10)
            .readTimeout(10)
            .connectTimeout(10)
            .build(this.applicationContext)
    }

    var currentSelectPostion = -1

    var updateDialog: UpdateDialog? = null

    fun initSportDetailRec() {
        recyclerview_sport.layoutManager = LinearLayoutManager(this)
        mSceneRidingListAdapter = FreeListAdapter(mDataList)
        recyclerview_sport.adapter = mSceneRidingListAdapter

        mSceneRidingListAdapter.setOnItemChildClickListener { adapter, view, position ->
            currentSelectPostion = position
            downSenceLine(mDataList.get(position).id.toString(), mDataList.get(position).videoUrl)
        }
        mSceneRidingListAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            //如果没有下载就弹出下载的对话框
            currentSelectPostion = position
            var bean = mDataList.get(position)
            var videourl = bean.videoUrl;
            var audioUrl = bean.audioUrl;
            var currentaudioFileName = ""
            if (!TextUtils.isEmpty(audioUrl)) {
                currentaudioFileName = audioUrl.substring(videourl.lastIndexOf("/"))
            }
            var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))
            val file = File(
                FileUtil.getFreeDir().toString() + currentSencFileName
            )
            if (file.exists()) {

                if (TextUtils.isEmpty(currentaudioFileName)) {
                    goActivity(bean)
                } else {
                    val fileMusic = File(
                        FileUtil.getMusicDir().toString() + currentaudioFileName
                    )
                    if (fileMusic.exists()) {
                        goActivity(bean)

                    } else {

                        downMusicLine(bean.id.toString(), bean.audioUrl, currentaudioFileName)
                    }
                }

            } else {
                downSenceLine(
                    bean.id.toString(),
                    bean.videoUrl
                )


            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (currentSelectPostion == -1) {
            return
        }
        var bean = mDataList.get(currentSelectPostion)
        goActivity(bean)
    }

    fun goActivity(bean: SceneBean) {


        //if (true) {
        if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_SUCCESS) {
            var intent =
                Intent(this@FreeRidingListActivity, FreeRideActivity::class.java)
            intent.putExtra("videoPath", bean.videoUrl);
            intent.putExtra("musicPath", bean.audioUrl);
            if (AppUtil.isCN()) {
                intent.putExtra("name", bean.name);
            } else {
                intent.putExtra("name", bean.nameEn);
            }
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
                        this@FreeRidingListActivity,
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


    var currentSencFileName = "";
    var currentMusicFileName = "";
    var currentVideoUrl = ""
    var currentId = ""
    fun downSenceLine(id: String, videourl: String) {

        Log.e("downSenceLine", "videourl=" + videourl)

        isDownMusic = false
        goDownLoad(id, videourl)
    }

    var isDownMusic = false

    fun downMusicLine(id: String, musicUrl: String, musicFile: String) {
        currentMusicFileName = musicFile
        isDownMusic = true
        downMusicDialog()
        launch {
            startDownLoad(id, musicUrl, musicFile)
        }
    }

    var yesOrNoDialog: YesOrNoDialog? = null

    fun goDownLoad(id: String, videourl: String) {
        if (updateDialog != null && updateDialog!!.isShowing) {
            return
        }
        currentId = id
        updateDialog = UpdateDialog(
            this,
            1,
            this.getResources().getString(R.string.download_free_video_tips)
        )
        updateDialog?.setOnDialogClickListener(OnDialogClickListener {

            when (it) {
                1 -> {
                    //开始下载
                    try {
                        currentSencFileName = videourl.substring(videourl.lastIndexOf("/") + 1)
                        currentVideoUrl = videourl

                        Log.e("startDownLoad", "startDownLoad")


                        launch {
                            startDownLoad(id, currentVideoUrl, currentSencFileName)

                        }

                    } catch (e: Exception) {
                        Log.e("startDownLoad", "startDownLoad" + e.toString())
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
                    cancelDownLoad()
                    mSceneRidingListAdapter.notifyDataSetChanged()
                }
            }
        })
        updateDialog?.show()
    }

    fun disUpdateDialog() {
        if (updateDialog!!.isShowing) {
            updateDialog?.dismiss()
            updateDialog = null
        }
    }

    var downMusicDialog: TipsDialog? = null

    fun downMusicDialog() {
        if (downMusicDialog != null && downMusicDialog!!.isShowing) {
            return
        }
        downMusicDialog = TipsDialog(this, this.getString(R.string.downloading))
        downMusicDialog!!.show()
    }

    fun hideMusicDialog() {
        if (downMusicDialog != null && downMusicDialog!!.isShowing) {
            downMusicDialog!!.dismiss();
            downMusicDialog = null;
        }
    }

    private suspend fun startDownLoad(
        tag: String,
        url: String,
        saveName: String
    ) {
        Log.e("startDownLoad", "startDownLoad")
        Log.e("startDownLoad", "url=" + url + ",saveName=" + saveName)
        rxHttp.create().doDownLoad(tag, url, FileUtil.getTempDir(), saveName, this)
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
        if (isDownMusic) {
            return
        }
        updateDialog?.updateProgress(progress)
        Log.e("down", "progress=" + progress)
        //  TODO("Not yet implemented")
    }

    var downLoadFailDialog: YesOrNoDialog? = null
    override fun onDownLoadError(key: String, throwable: Throwable) {
        if (isDownMusic) {
            hideMusicDialog()
            return
        }
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
                if (!NetUtil.isNetworkConnected(this@FreeRidingListActivity)) {
                    updateDialog?.dismiss();
                    updateDialog = null
                    ToastUtil.showTextToast(
                        BaseApp.sApplicaton,
                        this@FreeRidingListActivity.getString(R.string.net_error)
                    )
                    return
                }
                launch {
                    startDownLoad(currentId, currentVideoUrl, currentSencFileName)
                }
                downLoadFailDialog?.dismiss()
                downLoadFailDialog = null

            }
        })
        Log.e("down", "onDownLoadError=" + key + "--------" + throwable.message)
        //  TODO("Not yet implemented")
    }

    override fun onDownLoadSuccess(key: String, path: String) {
        Log.e("down", "onDownLoadSuccess=" + path + "-----" + currentSencFileName)
        if (isDownMusic) {
            FileUtil.copyFile(path, FileUtil.getMusicDir() + File.separator + currentMusicFileName)
            FileUtil.deletTempFile(path)
            hideMusicDialog()
        } else {
            FileUtil.copyFile(path, FileUtil.getFreeDir() + File.separator + currentSencFileName)

            FileUtil.deletTempFile(path)

            updateDialog?.updateSuccess()
        }
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

        if (isDownMusic) {
            return
        }
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


    fun getEmptempView(): View? {
        val notDataView = layoutInflater.inflate(R.layout.tempty_view, recyclerview_sport, false)
        val tvEmpty = notDataView.findViewById<TextView>(R.id.tv_empty_value)
        //tvEmpty.text = UIUtils.getString(R.string.no_message)
        // notDataView.setOnClickListener { }
        return notDataView
    }

}