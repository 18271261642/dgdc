package com.jkcq.homebike

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.utillibrary.PermissionUtil
import com.example.websocket.PKUserCacheManager
import com.example.websocket.WsManager
import com.example.websocket.im.JWebSocketClient
import com.example.websocket.im.JWebSocketClientService
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.bike.bean.BikeBean
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.mine.MineFragment
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.homebike.ride.RideFragment
import com.jkcq.homebike.ride.mvvm.viewmodel.PKModel
import com.jkcq.homebike.ride.pk.PKRideActivity
import com.jkcq.homebike.ride.pk.PrepareActivity
import com.jkcq.homebike.ride.pk.bean.enumbean.PkState
import com.jkcq.homebike.ride.util.BikeDataCache
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_activity.*


class MainActivity : BaseVMActivity<UserModel>() {

    var mRideFragment: RideFragment? = null
    var mMineFragment: MineFragment? = null
    val FRAGMENT_RIDE = 0
    val FRAGMENT_MINE = 1
    val mUserModel: UserModel by lazy { createViewModel(UserModel::class.java) }
    val mPKModel: PKModel by lazy { createViewModel(PKModel::class.java) }

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initView() {


        showFragment(FRAGMENT_RIDE)
        rg_main.check(R.id.rbtn_ride)
        rg_main.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.rbtn_ride -> {
                        rl_bike_press.visibility = View.VISIBLE
                        rl_mine_press.visibility = View.GONE
                        showFragment(FRAGMENT_RIDE)
                    }
                    R.id.rbtn_mine -> {
                        rl_bike_press.visibility = View.GONE
                        rl_mine_press.visibility = View.VISIBLE
                        showFragment(FRAGMENT_MINE)

                    }
                }
            }
        })
    }

    val mBikeDataViewModel: BikeDataViewModel by lazy {

        createViewModel(
            BikeDataViewModel::class.java
        )

    }

    fun updateBikeData(bike: BikeBean, rideType: Int, isCourse: Boolean) {
        //                    Logger.myLog("dateStr == " + (year + "-" + formatTwoStr(non) + "-" + formatTwoStr(day)) + "heartRateList == " + heartRateList.toString() + " heartRateList.size == " + heartRateList.size() + " AVG == " + Math.round(sum / (float) size));

        //骑行类型，0：自由骑行，1：线路骑行，2：PK骑行

        //上传单位卡
        var calorie: Int = bike.cal.toInt()
        var cyclingTime: Long = bike.endTime
        var cyclingType: Int = rideType
        //单位是米
        var distance: Int = bike.dis.toInt()
        var duration: Int = bike.duration
        var heartRateArray = bike.hrlist
        var powerArray = bike.powList
        //单位是 w
        var powerGeneration: Int = bike.light.toInt()
        var scenarioId: String = ""
        if (TextUtils.isEmpty(bike.sceneId)) {
        } else {
            scenarioId = bike.sceneId
        }
        var steppedFrequencyArray = bike.rpmList


        mBikeDataViewModel.upgradeCyclingRecords(
            isCourse,
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

    var filter: IntentFilter? = null
    override fun initData() {


        mPKModel.findPKState()
        initPermission()
        mUserModel.getUserInfo()
        WsManager.getInstance().setContext(this)
        startJWebSClientService()
        bindService()
    }


    override fun onResume() {
        super.onResume()

        var bike = BikeDataCache.getBikeBean(this, "free")
        Log.e("freebike1", "initData=" + bike)
        if (bike != null) {
            updateBikeData(bike, BikeConfig.BIKE_FREE, false)
            bike = null
        }
        bike = BikeDataCache.getBikeBean(this, "scene")
        Log.e("freebike2", "initData=" + bike)
        if (bike != null) {
            updateBikeData(bike, BikeConfig.BIKE_LINE, false)

            bike = null
        }

        bike = BikeDataCache.getBikeBean(this, "course")
        Log.e("freebike3", "initData=" + bike)
        if (bike != null) {
            updateBikeData(bike, BikeConfig.BIKE_COURSE, true)

            bike = null
        }

    }

    /**
     * 显示fragment
     */
    fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (index) {
            FRAGMENT_RIDE -> {
                if (mRideFragment == null) {
                    mRideFragment = RideFragment()
                    transaction.add(R.id.fl_content, mRideFragment!!)
                } else {
                    transaction.show(mRideFragment!!)
                }
            }
            FRAGMENT_MINE -> {
                if (mMineFragment == null) {
                    mMineFragment = MineFragment()
                    transaction.add(R.id.fl_content, mMineFragment!!)
                } else {
                    transaction.show(mMineFragment!!)
                }
            }
        }
        transaction.commitAllowingStateLoss()
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mRideFragment?.let { transaction.hide(it) }
        mMineFragment?.let { transaction.hide(it) }
    }

    fun initPermission() {
        PermissionUtil.checkPermission(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            permissonCallback = object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {
                        // toast("success")
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@MainActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    var failureDialog: YesOrNoDialog? = null
    fun reJoin(mPkId: String) {


        Log.e("failureDialog", "failureDialog=" + failureDialog)
        if (failureDialog != null && failureDialog!!.isShowing) {
            return
        }
        failureDialog = YesOrNoDialog(
            this,
            "",
            resources.getString(R.string.re_jion_pk_tips),
            resources.getString(R.string.no),
            resources.getString(R.string.pk_join), true
        )
        failureDialog?.show()
        failureDialog?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {

                mPKModel.leavePk(mPkId, false)

            }

            override fun onSureOnclick() {
                mPKModel.reJoinPk()
            }
        })
    }

    var currentPkState = -1
    override fun startObserver() {

        mPKModel.mCanJoin.observe(this, Observer {
            Log.e(
                "getPkState", " it.pkStatus=" + it.pkStatus
            )
            currentPkState = it.pkStatus
            when (it.pkStatus) {
                PkState.UNSTART.value -> {
                    reJoin(it.id)
                }
                PkState.START.value -> {
                    reJoin(it.id)
                }
                PkState.END.value -> {
                }
                PkState.DESTROY.value -> {
                }
                PkState.COLLECT_DATA.value -> {
                }
            }


        })
        mPKModel.mReJoinRoom.observe(this, Observer {
            when (currentPkState) {
                PkState.UNSTART.value -> {
                    var intent = Intent(this@MainActivity, PrepareActivity::class.java)
                    intent.putExtra("scene", it.scenario)
                    intent.putExtra("pkinfo", it)
                    startActivity(intent)
                }
                PkState.START.value -> {

                    Log.e("reconn", "" + it)

                    it.scenario.name = it!!.cyclingPk.pkName
                    var intent = Intent(this@MainActivity, PKRideActivity::class.java)
                    intent.putExtra("scene", it.scenario)
                    intent.putExtra("isReJoin", true)
                    var mpkId = it!!.cyclingPk.id
                    intent.putExtra("mpkId", mpkId)
                    PKUserCacheManager.clearMap()
                    PKUserCacheManager.addPkUsers(it.pkUsers)
                    it.pkUsers.forEach {

                        PKUserCacheManager.addValue(it.userId, it.nickName, it.avatar)

                        if (mUserId.equals(it.userId)) {


                            LoadImageUtil.getInstance()
                                .loadDownload(
                                    false,
                                    BaseApp.sApplicaton,
                                    it.avatar,
                                    it.userId,
                                    R.drawable.icon_pk_mine, 5, R.mipmap.friend_icon_default_photo
                                )
                        } else {
                            LoadImageUtil.getInstance()
                                .loadDownload(
                                    false,
                                    BaseApp.sApplicaton,
                                    it.avatar,
                                    it.userId,
                                    R.drawable.icon_pk_other, 5, R.mipmap.friend_icon_default_photo
                                )
                        }
                    }
                    startActivity(intent)
                }
            }


        })

        // TODO("Not yet implemented")
    }

    private var client: JWebSocketClient? = null
    private var binder: JWebSocketClientService.JWebSocketClientBinder? = null
    private var jWebSClientService: JWebSocketClientService? = null


    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            Log.e("MainActivity", "服务与活动成功绑定")
            binder = iBinder as JWebSocketClientService.JWebSocketClientBinder
            jWebSClientService = binder!!.getService()
            client = jWebSClientService!!.client
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e("MainActivity", "服务与活动成功断开")
        }
    }

    /**
     * 绑定服务
     */
    private fun bindService() {
        val bindIntent = Intent(
            this,
            JWebSocketClientService::class.java
        )
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE)
    }


    /**
     * 启动服务（websocket客户端服务）
     */
    private fun startJWebSClientService() {
        val intent = Intent(this, JWebSocketClientService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }


    }


    private fun unbindService() {
        unbindService(serviceConnection)

    }

    private fun stopJWebSClientService() {
        val intent = Intent(this, JWebSocketClientService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
        stopJWebSClientService()

    }
}