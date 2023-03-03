package com.jkcq.homebike.mine

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMFragment
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceScanActivity
import com.jkcq.homebike.ble.setting.BikeDeviceSettingActivity
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.login.*
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.homebike.ota.OtaActivity
import com.jkcq.homebike.ride.history.ExerciseHistoryActivity
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.view_bike_sport_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_dis_count.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MineFragment : BaseVMFragment<UserModel>() {
    var mBikeName: String by Preference(Preference.BIKENAME, "")
    private var param1: String? = null
    private var param2: String? = null
    var summerBean: Summary? = null

    override fun getLayoutResId(): Int = R.layout.fragment_mine

    val mUserModel: UserModel by lazy { createViewModel(UserModel::class.java) }
    var interTime = 0L
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.e("onHiddenChanged", "onHiddenChanged" + hidden)

        if (!hidden) {


            if (System.currentTimeMillis() - interTime < 5000) {

                return
            }
            mUserModel.getUserInfo()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    val mBikeDataViewModel: BikeDataViewModel by lazy {

        createViewModel(
            BikeDataViewModel::class.java
        )

    }


    override fun startObserver() {
        mBikeDataViewModel.sumSummerBean.observe(this, Observer {
            BikeConfig.summary = it
            setItemvalue(it)
        })
        mUserModel.mUserInfo.observe(this, Observer {
            /* LoadImageUtil.getInstance().loadCover(activity, it.headUrlTiny, iv_head_bg,10,10)*/
            LoadImageUtil.getInstance()
                .load(activity, it.headUrlTiny, circle_head, R.mipmap.friend_icon_default_photo)
            LoadImageUtil.getInstance()
                .load(activity, it.headUrlTiny, iv_head_bg, R.mipmap.friend_icon_default_photo)
            tv_nickname.text = it.nickName
            mBikeDataViewModel.getDeviceSummary(
                "2020-12-03",
                "",
                BikeConfig.BIKE_SPORT_DATE_ALL,
                mUserId
            )
        })

    }


    override fun initView() {


        /*for (i in 1..6) {
            mRankList.add(RankInfo())
        }
        mAdapter = MineRankAdapter(mRankList)
        LogUtil.e(mRankList.size.toString())

        val rankLayoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        recyclerview_rank.run {
            layoutManager=rankLayoutManager
            adapter = mAdapter
        }*/

        /* LoadImageUtil.getInstance().load(activity,R.drawable.bg_friend_ranking,iv_friend_ranking,
             SiseUtil.dip2px(10f))*/
    }

    override fun onResume() {
        super.onResume()

        mUserModel.getUserInfo()

        if (TextUtils.isEmpty(mBikeName)) {
            tv_device_mac.text = this.getString(R.string.device_scan)
        } else {
            tv_device_mac.text = mBikeName
        }
    }

    override fun initData() {
        initEvent()
        /* LoadImageUtil.getInstance()
             .load(activity, R.drawable.bg_friend_ranking, iv_friend_ranking, SiseUtil.dip2px(10f))*/
    }

    fun initEvent() {
        /* iv_mine_scan.setOnClickListener {
             PermissionUtil.checkPermission(activity!!,
                 arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                 object : PermissionUtil.OnPermissonCallback {
                     override fun isGrant(grant: Boolean) {
                         if (grant) {
                             startActivity(
                                 Intent(
                                     activity,
                                     FriendScanActivity::class.java
                                 )
                             )
                         } else {
                             toast(activity!!, R.string.please_open_camera_permission)
                         }
                     }

                 })

         }*/


        layout_dis_count.setOnClickListener {
            startActivity(Intent(activity, ExerciseHistoryActivity::class.java))
        }
        itemview_history_record.setOnClickListener {
            startActivity(Intent(activity, ExerciseHistoryActivity::class.java))
        }
        layout_sport_cal_time_pow.setOnClickListener {
            startActivity(Intent(activity, ExerciseHistoryActivity::class.java))
        }

        layout_head.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    EditUserInfoActivity::class.java
                )
            )
            /* startActivity(
                 Intent(
                     activity,
                     FriendQRCodeActivity::class.java
                 )
             )*/
        }
        iv_setting.setOnClickListener {
            startActivity(Intent(activity, SettingActivity::class.java))
        }
        layout_bike_device.setOnClickListener {

            if (TextUtils.isEmpty(mBikeName)) {
                startActivity(Intent(activity, BikeDeviceScanActivity::class.java))
            } else {
                startActivity(Intent(activity, BikeDeviceSettingActivity::class.java))
            }
        }
    }

    override fun setStatusBar() {
        //无标题栏的沉浸式
        StatusBarUtil.setTransparentForImageView(activity, iv_setting)
        StatusBarUtil.setLightMode(activity)
    }

    fun setItemvalue(summerBean: Summary) {


        val dis = SiseUtil.disUnitCoversion(summerBean.totalDistance, BikeConfig.userCurrentUtil)
        val cal = SiseUtil.calCoversion(summerBean.totalCalorie)
        val min = SiseUtil.timeCoversionMin(summerBean.totalDuration)
        val power = SiseUtil.powerCoversion(summerBean.totalPowerGeneration)
        BikeConfig.disUnit(tv_dis_unitl, activity)
        tv_dis_value.text = dis
        tv_view_sport_count.text = summerBean.totalTimes
        item_time.setValueText(min)
        item_cal.setValueText(cal)
        item_power.setValueText(power)
    }


}