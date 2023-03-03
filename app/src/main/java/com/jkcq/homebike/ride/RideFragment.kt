package com.jkcq.homebike.ride

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.websocket.WsManager
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMFragment
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceConnectViewModel
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceScanActivity
import com.jkcq.homebike.ble.devicescan.hr.DeviceScanViewModel
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.homebike.ride.course.CourseListActivity
import com.jkcq.homebike.ride.freeride.CountTimer
import com.jkcq.homebike.ride.freeride.FreeRideActivity
import com.jkcq.homebike.ride.freeride.FreeRidingListActivity
import com.jkcq.homebike.ride.freeride.ReconDeviceUtil
import com.jkcq.homebike.ride.history.ExerciseHistoryActivity
import com.jkcq.homebike.ride.pk.PKListActivity
import com.jkcq.homebike.ride.sceneriding.SceneRidingListActivity
import com.jkcq.homebike.ride.util.DeviceDataCache
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_ride.*
import kotlinx.android.synthetic.main.fragment_ride.layout_device_disconnect
import kotlinx.android.synthetic.main.fragment_ride.layout_device_unbind
import kotlinx.android.synthetic.main.view_bike_connect.*
import kotlinx.android.synthetic.main.view_bike_disconnect.*
import kotlinx.android.synthetic.main.view_bike_sport_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_dis_count.*
import kotlinx.android.synthetic.main.view_bike_unbind.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RideFragment : BaseVMFragment<BikeDataViewModel>(), CountTimer.OnCountTimerListener {


    var mSmallGo = mutableListOf<Int>()
    val type_unbind = 0
    val type_disCon = 1;
    val type_conn = 2;

    private val countTimer = CountTimer(400, this)

    val mUserModel: UserModel by lazy { createViewModel(UserModel::class.java) }


    var mBikeName: String by Preference(Preference.BIKENAME, "")


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    /**
     * 创建viewModel
     */

    val mBikeDataViewModel: BikeDataViewModel by lazy {

        createViewModel(
            BikeDataViewModel::class.java
        )

    }
    val mDeviceScanViewModel: DeviceScanViewModel by lazy { createViewModel(DeviceScanViewModel::class.java) }
    val mBikeDeviceConnectViewModel: BikeDeviceConnectViewModel by lazy {
        createViewModel(
            BikeDeviceConnectViewModel::class.java
        )

    }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun onResume() {
        super.onResume()
        countTimer.start()
        BikeConfig.isBikeScanPage = false;
        activity?.let { mBikeDeviceConnectViewModel.setCallBack(it) }
        setTopeState()
        mBikeDataViewModel.getDeviceDetaiUrl()
        getSumData()
        tv_name.text =
            String.format(this.resources.getString(R.string.hello_blank_fragment, mUserName))
        BikeConfig.disUnit(tv_dis_unitl, activity)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            countTimer.start()
            getSumData()

            tv_name.text =
                String.format(this.resources.getString(R.string.hello_blank_fragment, mUserName))
            BikeConfig.disUnit(tv_dis_unitl, activity)
        } else {
            countTimer.stop()
        }
    }


    var interTime = 0L

    fun getSumData() {

        if (System.currentTimeMillis() - interTime < 5000) {

            return
        }
        interTime = System.currentTimeMillis()
        mBikeDataViewModel.getDeviceSummary(
            dateFormat.format(System.currentTimeMillis()),
            "",
            BikeConfig.BIKE_SPORT_DATE_WEEK,
            mUserId
        )
        mBikeDataViewModel.getDeviceSumDis(
            dateFormat.format(System.currentTimeMillis()),
            "",
            BikeConfig.BIKE_SPORT_DATE_ALL,
            mUserId
        )
    }

    fun setTopeState() {
        if (!TextUtils.isEmpty(mBikeName)) {
            //设备连接成功需要显示成功的页面
            if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_SUCCESS) {
                showTopeItem(type_conn)
            } else {
                showTopeItem(type_disCon)
                if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_DISCONN) {
                    tv_dis_state.text = this.getString(R.string.device_state_fail)
                    connectDevice()
                    tv_dis_conn.isEnabled = true
                } else if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_CONNECTING) {
                    tv_dis_state.text = this.getString(R.string.device_state_connecting)
                    tv_dis_conn.isEnabled = false
                }
            }

        } else {
            showTopeItem(type_unbind)
        }
    }


    fun connectDevice() {
        if (TextUtils.isEmpty(mBikeName)) {
            mDeviceScanViewModel.startLeScan()
        } else {
            mDeviceScanViewModel.startLeScan()
        }
    }


    override fun startObserver() {

        mUserModel.mUserInfo.observe(this, Observer {
            tv_name.text =
                String.format(this.resources.getString(R.string.hello_blank_fragment, it.nickName))

        })

        mBikeDataViewModel.sumDis.observe(this, Observer {
            val dis = SiseUtil.disUnitCoversion(it, BikeConfig.userCurrentUtil)
            tv_bike_sum_dis_value.text = dis
            if (BikeConfig.userCurrentUtil == BikeConfig.METRIC_UNITS) {
                tv_bike_sum_dis_unitl.text =
                    this.resources.getString(R.string.sport_dis_unitl_km)
            } else {
                tv_bike_sum_dis_unitl.text =
                    this.resources.getString(R.string.sport_dis_unitl_mile)
            }

        })


        mBikeDataViewModel.sumSummerBean.observe(this, Observer {
            Log.e("getDeviceSummary2", it.toString())
            setItemvalue(it)
        })


        mDeviceScanViewModel.scanDeviceState.observe(this, Observer {
            when (it) {
                BikeConfig.BIKE_CONN_IS_SCAN -> {
                    tv_dis_state.text = this.getString(R.string.device_state_connecting)
                    tv_dis_conn.isEnabled = false
                }
                BikeConfig.BIKE_CONN_IS_SCAN_TIMEOUT -> {
                    tv_dis_state.text = this.getString(R.string.device_state_fail)
                    tv_dis_conn.isEnabled = true
                }
            }
        })
        mDeviceScanViewModel.mDeviceLiveData.observe(this, Observer {

            Log.e("RiedeFragment", "startObserver mDeviceLiveData")

            try {
                it.forEach {
                    if (!TextUtils.isEmpty(it.name)) {
                        if (it.name.equals(mBikeName)) {
                            //去连接设备
                            mDeviceScanViewModel.stopLeScan()
                            mBikeDeviceConnectViewModel.conectBikeDevice(it.device)
                        }
                    }


                }
            } catch (e: Exception) {

            }


        })
        mBikeDeviceConnectViewModel.mDeviceConnStateTips.observe(this, Observer {

            //tv_conn_disconnect.text = it
        })
        mBikeDeviceConnectViewModel.mDeviceConnState.observe(this, Observer {

            if (it == BikeConfig.BIKE_CONN_SUCCESS) {
                mDeviceScanViewModel.stopLeScan()
                handler.removeMessages(1001)
                showTopeItem(type_conn)
            } else {
                showTopeItem(type_disCon)
                if (it == BikeConfig.BIKE_CONN_DISCONN) {
                    tv_dis_state.text = this.getString(R.string.device_state_fail)
                    tv_dis_conn.isEnabled = true
                } else if (it == BikeConfig.BIKE_CONN_CONNECTING) {
                    tv_dis_state.text = this.getString(R.string.device_state_connecting)
                    tv_dis_conn.isEnabled = false
                }
            }
            // finish()
        })
    }


    fun showTopeItem(type: Int) {
        when (type) {
            type_unbind -> {
                layout_device_unbind.visibility = View.VISIBLE
                layout_device_connect.visibility = View.INVISIBLE
                layout_device_disconnect.visibility = View.GONE
            }
            type_disCon -> {
                layout_device_disconnect.visibility = View.VISIBLE
                layout_device_unbind.visibility = View.GONE
                layout_device_connect.visibility = View.INVISIBLE

            }
            type_conn -> {
                layout_device_connect.visibility = View.VISIBLE
                layout_device_unbind.visibility = View.GONE
                layout_device_disconnect.visibility = View.GONE
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserver()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_ride
    }

    override fun initView() {

    }


    var filter: IntentFilter? = null

    override fun initData() {
        // loadAnimation()
        initEvent()

        mSmallGo.add(R.drawable.icon_go_small_1)
        mSmallGo.add(R.drawable.icon_go_small_2)
        mSmallGo.add(R.drawable.icon_go_small_3)
        mUserModel.getUserInfo()
        filter = IntentFilter()
        filter!!.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        activity?.registerReceiver(broadcastReceiver, filter)
        LoadImageUtil.getInstance()
            .load(activity, R.drawable.bg_free_bike, iv_free_bike, SiseUtil.dip2px(10f))
        LoadImageUtil.getInstance()
            .load(activity, R.drawable.bg_course_bike, iv_course_bike, SiseUtil.dip2px(10f))

        LoadImageUtil.getInstance()
            .load(activity, R.drawable.bg_line_bike, iv_line_bike, SiseUtil.dip2px(10f))
        LoadImageUtil.getInstance()
            .load(activity, R.drawable.bg_pk_bike, iv_pk_bike, SiseUtil.dip2px(10f))
    }

    var mPKId: String by Preference(Preference.PK_ID, "")
    fun initEvent() {

        tv_dis_conn.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            mDeviceScanViewModel.startLeScan()
        }
        tv_unbind_way.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            startActivity(Intent(activity, GuideConDeviceActivity::class.java))
        }
        tv_dis_way.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            startActivity(Intent(activity, GuideConDeviceActivity::class.java))
        }

        tv_unbind_conn.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            startActivity(Intent(activity, BikeDeviceScanActivity::class.java))
        }
        layout_dis_count.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            startActivity(Intent(activity, ExerciseHistoryActivity::class.java))
        }
        layout_sport_cal_time_pow.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            startActivity(Intent(activity, ExerciseHistoryActivity::class.java))
        }
        iv_free_bike.setOnClickListener {


            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            startActivity(Intent(activity, FreeRidingListActivity::class.java))

            /* if (BikeConfig.BikeConnState == BikeConfig.BIKE_CONN_SUCCESS) {


             } else {
                 var dialog = activity?.let { it1 ->
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
                         startActivity(Intent(activity, BikeDeviceScanActivity::class.java))
                     }
                 })
                 dialog?.show()

             }*/
        }
        iv_line_bike.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            startActivity(Intent(activity, SceneRidingListActivity::class.java))
        }
        iv_pk_bike.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }

            startActivity(Intent(activity, PKListActivity::class.java))
            //  startActivity(Intent(activity, BikeDeviceSettingActivity::class.java))
        }
        iv_course_bike.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            startActivity(Intent(activity, CourseListActivity::class.java))

            //  startActivity(Intent(activity, BikeDeviceSettingActivity::class.java))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state =
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                Log.e(
                    "BleService",
                    "ACTION_STATE_CHANGED" + state + BluetoothAdapter.STATE_ON
                )
                if (state == BluetoothAdapter.STATE_ON) {
                    BikeConfig.isOpenBle = true;
                    if (!BikeConfig.isBikeScanPage)
                        connectDevice()
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    ReconDeviceUtil.getInstance().endReconDevice()
                    BikeConfig.isOpenBle = false;
                    //需要停止动画然后清除数据
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mDeviceScanViewModel.stopLeScan()
        handler.removeMessages(0)
        mBikeDeviceConnectViewModel.disconectDevice()
        activity?.unregisterReceiver(broadcastReceiver)
    }

    fun setItemvalue(summerBean: Summary) {

        val dis = SiseUtil.disUnitCoversion(summerBean.totalDistance, BikeConfig.userCurrentUtil)
        val cal = SiseUtil.calCoversion(summerBean.totalCalorie)
        val min = SiseUtil.timeCoversionMin(summerBean.totalDuration)
        val power = SiseUtil.powerCoversion(summerBean.totalPowerGeneration)
        tv_dis_value.text = dis
        BikeConfig.disUnit(tv_dis_unitl, activity)
        tv_view_sport_count.text = summerBean.totalTimes
        item_time.setValueText(min)
        item_cal.setValueText(cal)
        item_power.setValueText(power)
    }

    override fun onPause() {
        super.onPause()
        countTimer.stop()
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                //连接超时
                1001 -> {
                    mBikeDeviceConnectViewModel.clearDevice()
                    setTopeState()
                }
            }
        }
    }


    override fun onCountTimerChanged(millisecond: Long) {
        var smallvalue = mSmallGo.get(2)
        mSmallGo.remove(2)
        mSmallGo.add(0, smallvalue)
        AnimationUtil.showView(iv_free_go_1)
        iv_free_go_1.setImageResource(mSmallGo.get(0))
        AnimationUtil.showView(iv_free_go_2)
        iv_free_go_2.setImageResource(mSmallGo.get(1))
        AnimationUtil.showView(iv_free_go_3)
        iv_free_go_3.setImageResource(mSmallGo.get(2))



        AnimationUtil.showView(iv_course_go_1)
        iv_course_go_1.setImageResource(mSmallGo.get(0))
        AnimationUtil.showView(iv_course_go_2)
        iv_course_go_2.setImageResource(mSmallGo.get(1))
        AnimationUtil.showView(iv_course_go_3)
        iv_course_go_2.setImageResource(mSmallGo.get(2))



        AnimationUtil.showView(iv_line_go_1)
        iv_line_go_1.setImageResource(mSmallGo.get(0))
        AnimationUtil.showView(iv_line_go_2)
        iv_line_go_2.setImageResource(mSmallGo.get(1))
        AnimationUtil.showView(iv_line_go_3)
        iv_line_go_3.setImageResource(mSmallGo.get(2))
        AnimationUtil.showView(iv_pk_go_1)
        iv_pk_go_1.setImageResource(mSmallGo.get(0))
        AnimationUtil.showView(iv_pk_go_2)
        iv_pk_go_2.setImageResource(mSmallGo.get(1))
        AnimationUtil.showView(iv_pk_go_3)
        iv_pk_go_3.setImageResource(mSmallGo.get(2))

        //speedView.setCurSpeed(100)

    }


    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(activity, tv_name)
        StatusBarUtil.setLightMode(activity)
    }

    private val samples: ArrayList<String> = ArrayList()

}