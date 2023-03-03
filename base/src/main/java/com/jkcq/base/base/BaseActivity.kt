package com.jkcq.base.base

import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.R
import com.jkcq.base.app.ActivityLifecycleController
import com.jkcq.base.app.Preference
import com.jkcq.base.dialog.BaseCommonDialog
import com.jkcq.base.observable.LoginOutObservable
import com.jkcq.base.observable.NetDialogObservable
import com.jkcq.util.LoginOutDialog
import com.jkcq.util.OnButtonListener
import com.jkcq.util.StatusBarUtil
import java.util.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/23
 */
abstract class BaseActivity() : AppCompatActivity(), Observer {

    var mtoken: String by Preference(Preference.TOKEN, "")
    var mConnTime: Long by Preference(Preference.BIKE_CONN_TIME, System.currentTimeMillis())
    var mUserId: String by Preference(Preference.USER_ID, "")
    var mBikeName: String by Preference(Preference.BIKENAME, "")
    var mBikeMac: String by Preference(Preference.BIKEMAC, "")
    var mVersion: String by Preference(Preference.BIKEVERSION, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomContentView()
        initHeander()
        initView()
        initEvent()
        setStatusBar()
//        setSupportActionBar(mToolbar)
        initData()

    }

    var netdialog: BaseCommonDialog? = null


    fun showNetDialog() {


        if (netdialog == null) {

            netdialog = BaseCommonDialog.Builder(this)
                .setContentView(R.layout.common_dialog_load)
                .setCanceledOnTouchOutside(false)
                .fromCenter()
                .show()
        } else if (!netdialog!!.isShowing) {
            netdialog!!.show()

        }
    }

    fun hidNetDialog() {
        if (netdialog != null && netdialog!!.isShowing) {
            netdialog!!.dismiss()
        }
    }

    /**
     * 添加公共标题时，重写该方法
     */
    open fun setCustomContentView() {
        setContentView(getLayoutResId())
    }


    override fun onPause() {
        super.onPause()
        LoginOutObservable.getInstance().deleteObserver(this)
        NetDialogObservable.getInstance().deleteObserver(this)
    }

    override fun onResume() {
        super.onResume()
        LoginOutObservable.getInstance().addObserver(this)
        NetDialogObservable.getInstance().addObserver(this)
    }

    override fun update(o: Observable?, arg: Any?) {

        Log.e("update", "--------------" + o)

        if (o is LoginOutObservable) {
            //删除所有的设备缓存
            //DeviceTypeTableAction.deleteAllDevices()
            val msg = arg as Message

            when (msg.what) {
                LoginOutObservable.SHOW_SCALE_TIPS -> {
                    showLoginOutDialog()
                    Log.e("update", "SHOW_SCALE_TIPS")
                }
                LoginOutObservable.DISMISS_SCALE_TIPS -> {
                    hideLoginOutDialog()
                    Log.e("update", "DISMISS_SCALE_TIPS")
                }
                else -> {
                }
            }
        } else if (o is NetDialogObservable) {
            val msg = arg as Message

            when (msg.what) {
                LoginOutObservable.SHOW_SCALE_TIPS -> {
                    showNetDialog()
                }
                LoginOutObservable.DISMISS_SCALE_TIPS -> {
                    hidNetDialog()
                }
                else -> {
                }
            }
            //  onObserverChange(o, arg)
        } else {

        }
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun initData()

    open fun initHeander() {}
    open fun initEvent() {}

    override fun onDestroy() {
        super.onDestroy()

    }

    open fun setStatusBar() {
        //无标题栏的沉浸式
        StatusBarUtil.setTransparentForImageView(this, null)
        StatusBarUtil.setLightMode(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    var loginOutDialog: LoginOutDialog? = null
    open fun hideLoginOutDialog() {
        if (loginOutDialog != null) {
            if (loginOutDialog!!.isShowing) {
                loginOutDialog?.dismiss()
            }
        }
    }

    open fun showLoginOutDialog() {

        Log.e("show", "show------update" + "msg.what=" + loginOutDialog)

        if (loginOutDialog == null) {
            loginOutDialog = LoginOutDialog(this)
        }
        if (loginOutDialog!!.isShowing) {
            return
        }
        loginOutDialog?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
            }

            override fun onSureOnclick() {
                // Intent intent = new Intent(context, ActivityLogin.class);
                //context.startActivity(intent);
                mUserId = ""
                mBikeMac = ""
                mBikeName = ""
                ARouter.getInstance().build("/app/LoginActivity").navigation()
                ActivityLifecycleController.finishAllActivity("LoginActivity")
            }
        })
        loginOutDialog?.show()

    }
}