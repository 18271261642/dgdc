package com.jkcq.homebike.ride.course

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseTitleVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.mvvm.viewmodel.SceneridingListModel
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.ride.sceneriding.adpter.SceneManageAdapter
import com.jkcq.homebike.ride.sceneriding.bean.SceneVideoManaBean
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.AppUtil
import com.jkcq.util.OnDialogClickListener
import com.jkcq.util.ScenAdapterLineNameDialog
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_scene_riding.*
import java.io.File

class CourseManageActivity : BaseTitleVMActivity<SceneridingListModel>() {


    override fun getLayoutResId(): Int = R.layout.activity_scene_riding
    val mSceneridingListModel: SceneridingListModel by lazy { createViewModel(SceneridingListModel::class.java) }

    //运动详情的初始化
    var mDataList = mutableListOf<SceneBean>()
    var mSceneVideoManaBean = mutableListOf<SceneVideoManaBean>()
    lateinit var mSceneRidingListAdapter: SceneManageAdapter

    override fun initView() {
        // showFragment(FRAGMENT_DAY)
        // rg_main.check(R.id.rbtn_day)


    }

    override fun initData() {
        setTitleText(this.resources.getString(R.string.bike_scene_line_download))
        initPermission()
        initSportDetailRec()
        getAllFile()
        var lagu = ""
        if (AppUtil.isCN()) {
            lagu = "zh"
        } else {
            lagu = "en";
        }
        mSceneridingListModel.getCourseList("" + DeviceType.DEVICE_BIKE.value, lagu)
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
                        // toast("success")
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@CourseManageActivity.getString(R.string.no_required_permission)
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
        mSceneridingListModel.mSceneBean.observe(this, Observer {
            mDataList.clear()
            mDataList.addAll(it)
            if (mSceneVideoManaBean.size == 0) {
                mSceneRidingListAdapter.setEmptyView(getEmptempView()!!)
            }
            mSceneVideoManaBean.forEach {
                var senceVideo = it
                mDataList.forEach {
                    val currentSencFileName: String =
                        it.videoUrl.substring(it.videoUrl.lastIndexOf("/") + 1)

                    Log.e(
                        "startObserver",
                        "senceVideo.fileName=" + senceVideo.fileName + ",currentSencFileName=" + currentSencFileName
                    )
                    if (senceVideo.fileName.equals(currentSencFileName)) {
                        senceVideo.url = it.videoUrl
                        senceVideo.picUrl = it.imageUrl
                        senceVideo.sceneEnName.add(it.name)
                        senceVideo.sceneName.add(it.name)
                    }


                }
            }

            Log.e("mSceneVideoManaBean", "mSceneVideoManaBean=" + mSceneVideoManaBean)
            mSceneRidingListAdapter.notifyDataSetChanged()

        })
    }

    override fun ivRight() {
        // TODO("Not yet implemented")

    }

    var currentSelectPostion = -1
    var dialog: ScenAdapterLineNameDialog? = null
    fun initSportDetailRec() {
        recyclerview_sport.layoutManager = LinearLayoutManager(this)
        mSceneRidingListAdapter = SceneManageAdapter(mSceneVideoManaBean, false)
        recyclerview_sport.adapter = mSceneRidingListAdapter

        mSceneRidingListAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            //如果没有下载就弹出下载的对话框
            if (ToastUtil.isFastDoubleClick()) {
                return@OnItemClickListener
            }
            currentSelectPostion = position
            var strValue: StringBuffer = StringBuffer()
            if (AppUtil.isCN()) {
                mSceneVideoManaBean.get(currentSelectPostion).sceneName.forEach {
                    strValue.append(it)
                    strValue.append("\n")
                }

            } else {
                mSceneVideoManaBean.get(currentSelectPostion).sceneEnName.forEach {
                    strValue.append(it)
                    strValue.append("\n")
                }
            }

            if (dialog != null && dialog!!.isShowing) {
                return@OnItemClickListener
            }

            dialog = ScenAdapterLineNameDialog(
                this,
                strValue.toString(),
                this.getString(R.string.course_line_adapter_tips),
                this.getString(R.string.course_line_del_tips)
            )

            dialog?.setOnDialogClickListener(OnDialogClickListener {

                when (it) {
                    1 -> {
                        //开始下载
                        try {
                            dialog?.dismiss()
                            if (currentSelectPostion >= 0 && currentSelectPostion < mSceneVideoManaBean.size) {
                                FileUtil.deleteFile(
                                    File(
                                        mSceneVideoManaBean.get(
                                            currentSelectPostion
                                        ).path
                                    )
                                )
                                mSceneVideoManaBean.removeAt(currentSelectPostion)
                                currentSelectPostion = -1
                                if (mSceneVideoManaBean.size == 0) {
                                    mSceneRidingListAdapter.setEmptyView(getEmptempView()!!)
                                }
                                mSceneRidingListAdapter.notifyDataSetChanged()
                            }
                        } catch (e: Exception) {
                        }
                    }
                    2 -> {
                        //取消下载
                        dialog?.dismiss()
                        dialog = null
                    }
                    3 -> {
                        dialog?.dismiss()
                        dialog = null
                    }
                }
            })
            dialog?.show()
        })
    }

    var currentSencFileName = "";


    var handler = Handler()


    fun getAllFile() {
        val file = File(FileUtil.getVideoCorseDir())
        val files = file.listFiles()
        if (files == null) {
            Log.e("error", "空目录")

            return
        }
        val s: MutableList<String> = ArrayList()

        files.forEach {
            s.add(it.absolutePath)
            mSceneVideoManaBean.add(SceneVideoManaBean(it.length(), it.path))
            Log.e(
                "error",
                it.absolutePath + "----" + it.path + "file.length()=" + it.length()
            )
        }
    }

    fun getEmptempView(): View? {
        val notDataView = layoutInflater.inflate(R.layout.tempty_view, recyclerview_sport, false)
        val tvEmpty = notDataView.findViewById<TextView>(R.id.tv_empty_value)
        //tvEmpty.text = UIUtils.getString(R.string.no_message)
        // notDataView.setOnClickListener { }
        return notDataView
    }
}