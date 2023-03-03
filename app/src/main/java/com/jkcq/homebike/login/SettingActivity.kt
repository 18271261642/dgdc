package com.jkcq.homebike.login

import android.content.Intent
import android.os.Handler
import android.os.Message
import androidx.lifecycle.Observer
import com.jkcq.base.app.ActivityLifecycleController
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.CacheManager
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.ali.PhotoChoosePopUtil
import com.jkcq.util.ThreadPoolUtils
import com.jkcq.homebike.ble.devicescan.bike.BikeDeviceConnectViewModel
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.homebike.util.GlideCacheUtil
import com.jkcq.util.OnButtonListener
import com.jkcq.util.StatusBarUtil
import com.jkcq.util.YesOrNoDialog
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_setting_activity.*

class SettingActivity : BaseVMActivity<UserModel>() {


    var photoChoosePopUtil: PhotoChoosePopUtil? = null

    val mUserModel: UserModel by lazy { createViewModel(UserModel::class.java) }
    val mBikeDeviceConnectViewModel: BikeDeviceConnectViewModel by lazy {
        createViewModel(
            BikeDeviceConnectViewModel::class.java
        )
    }


    override fun getLayoutResId(): Int = R.layout.activity_setting_activity
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0x00 -> {
                    ToastUtil.showTextToast(
                        BaseApp.sApplicaton,
                        this@SettingActivity.resources.getString(R.string.friend_option_success)
                    )
                    val size = GlideCacheUtil.getInstance().getCacheSize(this@SettingActivity)
                    item_clear_cach.setRightText(size)
                }
            }
        }
    }


    override fun initEvent() {
        super.initEvent()
        iv_back.setOnClickListener { finish() }
        item_question.setOnClickListener {
            if(ToastUtil.isFastDoubleClick()){
                return@setOnClickListener
            }

            mUserModel.getQuestionUrl()
        }
        item_faceback.setOnClickListener {
            if(ToastUtil.isFastDoubleClick()){
                return@setOnClickListener
            }

            mUserModel.getFeedUrl()
        }
        item_about.setOnClickListener {
            if(ToastUtil.isFastDoubleClick()){
                return@setOnClickListener
            }

            startActivity(Intent(this@SettingActivity, AboutActivity::class.java))
        }
        item_clear_cach.setOnClickListener {
            if(ToastUtil.isFastDoubleClick()){
                return@setOnClickListener
            }

            var yesOrNoDialog = YesOrNoDialog(
                this,
                "",
                this.resources.getString(R.string.clear_cache),
                "",
                resources.getString(R.string.dialog_ok)
            )
            yesOrNoDialog.show()
            yesOrNoDialog.setBtnOnclick(object : OnButtonListener {
                override fun onCancleOnclick() {
                }

                override fun onSureOnclick() {
                    ThreadPoolUtils.getInstance().addTask(Runnable {
                        GlideCacheUtil.getInstance().clearImageAllCache(this@SettingActivity)
                    })
                    handler.sendEmptyMessageDelayed(0x00, 1000)

                }
            })
        }
        item_unit.setOnClickListener {
            if(ToastUtil.isFastDoubleClick()){
                return@setOnClickListener
            }
            showPhotoChoosePop()
        }
        btn_login_out.setOnClickListener {
            if(ToastUtil.isFastDoubleClick()){
                return@setOnClickListener
            }

            var yesOrNoDialog = YesOrNoDialog(
                this,
                "",
                this.resources.getString(R.string.log_out_notice),
                "",
                resources.getString(R.string.dialog_ok)
            )
            yesOrNoDialog.show()
            yesOrNoDialog.setBtnOnclick(object : OnButtonListener {
                override fun onCancleOnclick() {
                }

                override fun onSureOnclick() {
                    mUserId = ""
                    mBikeMac = ""
                    mBikeName = ""
                    mVersion = ""

                    startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                    mBikeDeviceConnectViewModel.setCallBack(this@SettingActivity)
                    mBikeDeviceConnectViewModel.disconectDevice()
                    ActivityLifecycleController.finishAllActivity("LoginActivity")
                }
            })


        }
    }

    override fun initView() {


    }

    private fun showPhotoChoosePop() {
        if (null == photoChoosePopUtil) {
            photoChoosePopUtil = PhotoChoosePopUtil(
                this,
                this.resources.getString(R.string.setting_dis_unitl_km),
                this.resources.getString(R.string.setting_dis_unitl_mile)
            )
        }
        photoChoosePopUtil?.show(window.decorView)
        photoChoosePopUtil?.setOnPhotoChooseListener(object :
            PhotoChoosePopUtil.OnPhotoChooseListener {
            override fun onChooseCamera() {
                //camera()  在下面
                item_unit.setRightText(this@SettingActivity.resources.getString(R.string.setting_dis_unitl_mile))
                mUserModel.setmeasurement("1")
            }

            override fun onChoosePhotograph() {
                item_unit.setRightText(this@SettingActivity.resources.getString(R.string.setting_dis_unitl_km))
                mUserModel.setmeasurement("0")

                // gallery() 在上面
            }
        })
    }

    var headUrl = ""
    var bgUrl = ""
    var strProfile = ""
    var nikename = ""

    override fun initData() {

        var strUnit = this@SettingActivity.resources.getString(R.string.setting_dis_unitl_km)
        if (CacheManager.mUserInfo != null) {
            if (CacheManager.mUserInfo!!.measurement.equals("1")) {
                strUnit = this@SettingActivity.resources.getString(R.string.setting_dis_unitl_mile)
            }
        }

        val size = GlideCacheUtil.getInstance().getCacheSize(this@SettingActivity)
        item_clear_cach.setRightText(size)
        item_unit.setRightText(strUnit)

    }

    override fun startObserver() {

        mUserModel.mFeedBackUrl.observe(this, Observer {
            var intent = Intent(this@SettingActivity, WebViewActivity::class.java)
            intent.putExtra(
                "url",
                it + "?userId=" + mUserId
            )
            intent.putExtra("title", this.getString(R.string.setting_faceback))

            startActivity(intent)
        })
        mUserModel.mQuestionUrl.observe(this, Observer {
            var intent = Intent(this@SettingActivity, WebViewActivity::class.java)
            intent.putExtra(
                "url",
                it
            )
            intent.putExtra("title", this.getString(R.string.setting_question))
            startActivity(intent)
        })
    }


    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

}