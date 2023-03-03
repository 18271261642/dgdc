package com.jkcq.homebike.ride.course

import android.Manifest
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseTitleVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.ride.course.adpter.CouserListAdapter
import com.jkcq.homebike.ride.mvvm.viewmodel.SceneridingListModel
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.util.AppUtil
import com.jkcq.util.NetUtil
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_scene_riding.*


class CourseListActivity : BaseTitleVMActivity<SceneridingListModel>() {


    override fun getLayoutResId(): Int = R.layout.activity_scene_riding
    val mSceneridingListModel: SceneridingListModel by lazy { createViewModel(SceneridingListModel::class.java) }

    //运动详情的初始化
    var mDataList = mutableListOf<SceneBean>()
    lateinit var mSceneRidingListAdapter: CouserListAdapter;

    override fun initView() {


    }

    override fun initData() {
        setTitleText(this.resources.getString(R.string.bike_type_course_title))
        setIvRightIcon(R.drawable.icon_scene_manage)
        initPermission()
        initSportDetailRec()

    }

    var lagu = "zh"
    override fun onResume() {
        super.onResume()
        if (AppUtil.isCN()) {
            lagu = "zh"
        } else {
            lagu = "en";
        }
        mSceneridingListModel.getCourseList("" +DeviceType.DEVICE_BIKE.value, lagu)

        //   mSceneridingListModel.getCourseList("" + BikeConfig.BIKE_TYPE, lagu)
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
                            this@CourseListActivity.getString(R.string.no_required_permission)
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
            mSceneRidingListAdapter.notifyDataSetChanged()

        })
    }

    override fun ivRight() {
        startActivity(Intent(this, CourseManageActivity::class.java))
    }

    override fun initEvent() {
        super.initEvent()
    }

    var currentSelectPostion = -1


    fun initSportDetailRec() {
        recyclerview_sport.layoutManager = LinearLayoutManager(this)
        mSceneRidingListAdapter = CouserListAdapter(mDataList)
        recyclerview_sport.adapter = mSceneRidingListAdapter

        mSceneRidingListAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            //如果没有下载就弹出下载的对话框
            if(ToastUtil.isFastDoubleClick()){
                return@OnItemClickListener
            }
            currentSelectPostion = position
            var bean = mDataList.get(position)
            var videourl = bean.videoUrl;
            var currentSencFileName = videourl.substring(videourl.lastIndexOf("/"))

            var intent = Intent(this@CourseListActivity, CourseDetailActivity::class.java)
            intent.putExtra("sceneId", bean.id.toString());
            startActivity(intent)

        })
    }

    private fun isWifiOpened(): Boolean {

        return NetUtil.checkWifiState(this)
    }

    var currentSencFileName = "";
    var currentVideoUrl = ""


    var handler = Handler()

}