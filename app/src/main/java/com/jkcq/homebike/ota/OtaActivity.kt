package com.jkcq.homebike.ota

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.allens.lib_http2.RxHttp
import com.allens.lib_http2.config.HttpLevel
import com.allens.lib_http2.impl.OnDownLoadListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.homebike.util.Arith
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.AppUtil
import com.jkcq.util.OnButtonListener
import com.jkcq.util.StatusBarUtil
import com.jkcq.util.YesOrNoDialog
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.TitleView
import kotlinx.android.synthetic.main.activity_ota.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import no.nordicsemi.android.dfu.DfuProgressListener
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter
import no.nordicsemi.android.dfu.DfuServiceInitiator
import no.nordicsemi.android.dfu.DfuServiceListenerHelper
import java.io.File

class OtaActivity : BaseVMActivity<UserModel>(), OnDownLoadListener, CoroutineScope by MainScope() {

    val mUserModel: BikeDataViewModel by lazy { createViewModel(BikeDataViewModel::class.java) }

    var deviceName = ""
    var deviceAddress = ""
    private lateinit var rxHttp: RxHttp

    override fun getLayoutResId(): Int = R.layout.activity_ota

    override fun initView() {
        view_title.onTitleOnClick = object : TitleView.OnTitleViewOnclick {
            override fun onLeftOnclick() {

                goBack()
            }

            override fun onRightOnclick() {
                finish()
            }

            override fun onCalenderOnclick() {
            }
        }
    }

    override fun onBackPressed() {
        goBack()
    }


    override fun initData() {
        if (!TextUtils.isEmpty(BikeConfig.currentVersion)) {
            mVersion = BikeConfig.currentVersion
        }
        // checkCameraPersiomm(DFUActivity.this, DFUActivity.this);
        deviceName = mBikeName
        deviceAddress = mBikeMac
        progress_value.setMax(100)
        progress_value.setProgress(100)
        mUserModel.getdeviceVersion("" + BikeConfig.BIKE_TYPE)
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener)
        tv_bike_version.text = BikeConfig.currentVersion
    }

    override fun onDestroy() {
        super.onDestroy()
        rxHttp.create().doDownLoadCancelAll()
        if (yesOrNoDialog != null && yesOrNoDialog!!.isShowing) {
            yesOrNoDialog!!.dismiss()
        }
        if (upgradeDialog != null && upgradeDialog!!.isShowing) {
            upgradeDialog!!.dismiss()
        }
        if (failureDialog != null && failureDialog!!.isShowing) {
            failureDialog!!.dismiss()
        }
        if (updateSuccess != null && updateSuccess!!.isShowing) {
            updateSuccess!!.dismiss()
        }
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener)
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

        tv_btn_state.setOnClickListener {
            if (tv_btn_state.getTag() == null || tv_btn_state.getTag() == "download") {
                initPermission()
            } else if (tv_btn_state.getTag() == "upgrade") {
                showUpgradeDialog(this.resources.getString(R.string.device_upgrade_sure))
                // upload(deviceName, deviceMac);
            }
        }
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

                        dowFile()

                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@OtaActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    fun upgradeDeviceFile() {
        Log.e(
            "upgradeDeviceFile",
            "upload操作展示弹窗" + "deviceName:" + deviceName + "deviceAddress:" + deviceAddress
        )
        val starter = DfuServiceInitiator(deviceAddress)
            .setDeviceName(deviceName)
            .setKeepBond(false)
            .setForceDfu(false)
            .setPacketsReceiptNotificationsEnabled(true)
            .setPacketsReceiptNotificationsValue(6)
            .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true)
        starter.disableResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DfuServiceInitiator.createDfuNotificationChannel(this)
        }
        //Logger.myLog("path:$pathVersion/$fileName")
        var pathVersion = FileUtil.getDeviceDir() + File.separator + serviceVersion
        starter.setZip(pathVersion + File.separator + fileName)
        starter.start(this@OtaActivity, DfuService::class.java)

    }

    private suspend fun startDownLoad(
        tag: String,
        url: String,
        saveName: String
    ) {
        var pathVersion = ""
        var parentpath = FileUtil.getDeviceDir()
        pathVersion = parentpath + File.separator + serviceVersion
        Log.e("startDownLoad", "startDownLoad")
        rxHttp.create().doDownLoad(tag, url, pathVersion, saveName, this)
    }

    fun dowFile() {


        launch {
            startDownLoad(url + "_0", url, fileName)
        }


        /* DownloadBinUtils.getInstance()
             .downBin(url, pathVersion, fileName, object : onDownloadListener {
                 override fun onProgress(currntSize: Long, toalSize: Long) {
                 }

                 override fun onComplete(upgradeId: String?, path: String?) {
                 }

                 override fun onStart(length: Float) {
                     tv_btn_state.setEnabled(false)
                     progress_value.setProgress(0)
                     // isDownding = true;
                 }

                 override fun onProgress(progress: Float) {
                     tv_btn_state.setEnabled(false)
                     progress_value.setProgress((progress * 100).toInt())
                     tv_btn_state.setText(
                         java.lang.String.format(
                             this@OtaActivity.getString(R.string.file_downlod_present),
                             "" + (progress * 100).toInt()
                         )
                     )
                 }

                 override fun onComplete() {
                     tvStateEnabled(true)
                     //tvBtnState.setEnabled(true);
                     tv_btn_state.setText(this@OtaActivity.getString(R.string.device_upgrade))
                     tv_btn_state.setTag("upgrade")
                     showUpgradeDialog(this@OtaActivity.getString(R.string.file_downlod_success_tips))
                 }

                 override fun onFail() {
                     tv_btn_state.setEnabled(true)
                     progress_value.setProgress(100)
                     tv_btn_state.setText(this@OtaActivity.getString(R.string.try_again))
                     tv_btn_state.setTag("download")
                     downloadFail()
                 }
             })
 */
    }


    private fun downloadFail() {
        tv_btn_state.setEnabled(true)
        var message = ""
        message = this.getString(R.string.file_download)

    }


    var isUPLoading = false
    fun tvStateEnabled(isEabled: Boolean) {
        isUPLoading = false
        // loadHandler.removeCallbacksAndMessages(null)
        if (isEabled) {
            tv_btn_state.setEnabled(true)
            progress_value.setProgress(100)
        } else {
            tv_btn_state.setEnabled(false)
            progress_value.setProgress(0)
        }
    }

    var upgradeDialog: YesOrNoDialog? = null
    fun showUpgradeDialog(tips: String) {
        if (upgradeDialog != null && upgradeDialog!!.isShowing) {
            return
        }
        upgradeDialog = YesOrNoDialog(
            this,
            "",
            tips,
            resources.getString(R.string.no),
            resources.getString(R.string.yes)
        )
        upgradeDialog?.show()
        upgradeDialog!!.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {

            }

            override fun onSureOnclick() {
                upgradeDialog = null
                upgradeDeviceFile()
            }
        })
    }

    private fun setShowUpdateContent(isLastestVersion: Boolean) {
        tv_btn_state.setVisibility(if (isLastestVersion) View.GONE else View.VISIBLE)
        progress_value.setVisibility(if (isLastestVersion) View.GONE else View.VISIBLE)
        btn_try_again.setVisibility(View.GONE)
        layout_error.setVisibility(View.GONE)
        tv_lastest_version.setVisibility(if (isLastestVersion) View.VISIBLE else View.GONE)
        // scrollView.setVisibility(if (isLastestVersion) View.GONE else View.VISIBLE)
        // view_bottom_line.setVisibility(if (isLastestVersion) View.GONE else View.VISIBLE)
        layout_bottom.visibility = if (isLastestVersion) View.GONE else View.VISIBLE
    }

    var serviceVersion = ""
    var url = ""
    var fileName = ""
    override fun startObserver() {
        mUserModel.mDeviceUpgradeBean.observe(this, Observer {
            btn_try_again.visibility = View.GONE
            tv_btn_state.visibility = View.VISIBLE
            url =
                if (TextUtils.isEmpty(it.getUrl())) "" else it.getUrl()
            var fileSize = it.getFileSize()
            serviceVersion =
                if (TextUtils.isEmpty(it.getAppVersionName())) "" else it.getAppVersionName()
            //serviceVersion = "00.00.46";
            // "url": "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/device/w516_v0045.zip"
            //serviceVersion = "00.00.46";
            // "url": "https://isportcloud.oss-cn-shenzhen.aliyuncs.com/device/w516_v0045.zip"
            fileName = url.substring(url.lastIndexOf("/") + 1, url.length)
            if (it.getFileSize() > 1024 * 1024) {
                tv_file_size.text =
                    (Arith.div(
                        it.getFileSize().toDouble(),
                        1024 * 1024.0,
                        2

                    ).toString() + "M"
                            )
            } else {
                tv_file_size.text = (
                        Arith.div(
                            it.getFileSize().toDouble(),
                            1024.0,
                            2
                        ).toString() + "KB"
                        )
            }
            tv_new_version.setText(
                String.format(
                    this.getString(R.string.app_device_version),
                    serviceVersion
                )
            )
            if (AppUtil.isZh(this)) {
                //String temp =
                tv_file_content.setText(it.getRemark().replace("\\n", "\n"))
            } else {
                //String temp = ;
                tv_file_content.setText(it.getRemarkEn().replace("\\n", "\n"))
            }
            var currentSencFileName = url.substring(url.lastIndexOf("/"))
            fileExit(currentSencFileName!!, serviceVersion)
            if (tv_new_version.text.equals(mVersion)) {
                setShowUpdateContent(true)
            } else {
                setShowUpdateContent(false)
            }

        })

    }


    fun fileExit(filename: String, serviceVersion: String) {
        val file = File(

            FileUtil.getDeviceDir().toString() + File.separator + serviceVersion + filename
        )
        if (file.exists()) {
            //直接去升级
            tv_btn_state.text = (this.resources.getString(R.string.device_upgrade))
            tv_btn_state.setTag("upgrade")
        } else {
            //直接去升级
            tv_btn_state.text = (this.resources.getString(R.string.file_download))
            tv_btn_state.setTag("download")

            //下载APP
        }
    }

    var yesOrNoDialog: YesOrNoDialog? = null
    private fun goBack() {
        //如果是升级中就弹出对话框
        if (isUPLoading) {

            yesOrNoDialog = YesOrNoDialog(
                this,
                this.resources.getString(R.string.sport_dis_too_short_tips),
                this.getString(R.string.device_upgradeing),
                "",
                this.getString(R.string.dialog_ok),
                false

            )
            yesOrNoDialog?.setBtnOnclick(object : OnButtonListener {
                override fun onCancleOnclick() {
                    //  TODO("Not yet implemented")

                }

                override fun onSureOnclick() {

                }
            })
            yesOrNoDialog?.show()
        } else {
            finish()
        }
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, view_title)
        StatusBarUtil.setLightMode(this)
    }

    var isComplety = false
    var isDfuMode = false
    var handler = Handler()
    fun startDFUMode() {
        handler.post(Runnable {
            isComplety = false
            //小米需要重新去扫描，把扫描到的DFU设备
            tv_btn_state.setEnabled(false)
            //progressValue.setProgress(0);
            isUPLoading = true
        })
    }

    private val mDfuProgressListener: DfuProgressListener = object : DfuProgressListenerAdapter() {
        override fun onDeviceConnecting(deviceAddress: String) {
            Log.e(
                "DFUActivity",
                "DFUActivity onDeviceConnecting deviceAddress = $deviceAddress"
            )
        }

        override fun onDfuProcessStarting(deviceAddress: String) {
            startDFUMode()

            //小米需要重新去扫描，把扫描到的DFU设备
            progress_value.setProgress(0)
            Log.e(
                "DFUActivity",
                "DFUActivity onDfuProcessStarting deviceAddress = " + deviceAddress + "connect  phone info:"
            )
        }

        override fun onEnablingDfuMode(deviceAddress: String) {
            //自己的连接不要去重连
            //已经到了升级模式
            isDfuMode = true
            /*if (Build.BRAND == "Xiaomi") {
                if (tryCount == 0) {
                    loadHandler.sendEmptyMessageDelayed(0x02, 30000)
                } else {
                    loadHandler.sendEmptyMessageDelayed(0x03, 30000)
                }
                //
            }*/
            startDFUMode()
            Log.e("DFUActivity", "DFUActivity onEnablingDfuMode deviceAddress = $deviceAddress")
        }

        override fun onFirmwareValidating(deviceAddress: String) {
            Log.e("DFUActivity", "DFUActivity onFirmwareValidating deviceAddress = $deviceAddress")
        }

        override fun onDeviceDisconnected(deviceAddress: String) {
            Log.e(
                "DFUActivity",
                "DFUActivity onDeviceDisconnected deviceAddress = $deviceAddress"
            )
            BikeConfig.BikeConnState = BikeConfig.BIKE_CONN_DISCONN
            //  super.onDeviceDisconnected(deviceAddress);
            /*uploadOnFailure();
            isUPLoading = false;
            Logger.myLog("DFUActivity OTA 终止了 = ");
            Constants.isDFU = false;*/
        }

        override fun onDeviceDisconnecting(deviceAddress: String) {
            //升级成功了也会调用一次这个函数
            isUPLoading = false
            Log.e(
                "DFUActivity",
                "DFUActivity onDeviceDisconnecting deviceAddress = $deviceAddress"
            )
            //uploadOnFailure();
            //  Logger.myLog("DFUActivity onDeviceDisconnecting deviceAddress$deviceAddress")
            //            if(progressDialog != null && progressDialog.isShowing()){
//                progressDialog.dismiss();
//            }
        }

        override fun onDfuCompleted(deviceAddress: String) {
            Log.e(
                "DFUActivity",
                "DFUActivity onDfuCompleted deviceAddress = $deviceAddress"
            )

            ///OTA 升级完成
            //  loadHandler.removeCallbacks(null)
            // Logger.myLog("DFUActivity onDfuCompleted deviceAddress$deviceAddress")
            stopService(Intent(this@OtaActivity, DfuService::class.java))
            if (!isFinishing) {
                showDeviceUpdateSuccess()
            }
        }

        override fun onDfuAborted(deviceAddress: String) {
            //升级失败
            uploadOnFailure()
            Log.e("DFUActivity", "DFUActivity onDfuAborted OTA 终止了 = ")
            // TODO: 2018/6/30  终止了，重试
            // uploadOnFailure();
        }

        override fun onProgressChanged(
            deviceAddress: String,
            percent: Int,
            speed: Float,
            avgSpeed: Float,
            currentPart: Int,
            partsTotal: Int
        ) {
            //红米手机升级到了100不走完成的回调
            Log.e("DFUActivity", "DFUActivity onProgressChanged  " + percent)
            onDeviceUpgradeProgressChanged(percent)
        }

        override fun onError(
            deviceAddress: String,
            error: Int,
            errorType: Int,
            message: String
        ) {
            Log.e("DFUActivity", "onError onProgressChanged  ")
            uploadOnFailure()
        }
    }
    var isError = false
    fun onDeviceUpgradeProgressChanged(percent: Int) {
        //  loadHandler.removeCallbacksAndMessages(null)
        if (percent == 1) {
            isError = false
        }
        // cancelScan()
        if (percent == 100) {
            isComplety = true
            if (Build.BRAND == "Xiaomi") {
                //stopService(new Intent(DFUActivity.this, DfuService.class));
                handler.postDelayed({ showDeviceUpdateSuccess() }, 100)
            }
        }
        tv_btn_state.setText(
            java.lang.String.format(
                this.getString(R.string.device_upgrade_present),
                "" + percent
            )
        )
        progress_value.setProgress(percent)
    }

    var failureDialog: YesOrNoDialog? = null
    fun uploadOnFailure() {
        if (failureDialog != null && failureDialog!!.isShowing) {
            return
        }
        failureDialog = YesOrNoDialog(
            this,
            resources.getString(R.string.device_upgrade_fail),
            resources.getString(R.string.device_upgrade_fail_tips),
            resources.getString(R.string.no),
            resources.getString(R.string.yes), false
        )
        failureDialog?.show()
        failureDialog?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
            }

            override fun onSureOnclick() {
                this@OtaActivity.finish()
            }
        })
    }


    var updateSuccess: YesOrNoDialog? = null

    fun showDeviceUpdateSuccess() {

        if (updateSuccess != null && updateSuccess!!.isShowing) {
            return
        }

        updateSuccess = YesOrNoDialog(
            this,
            resources.getString(R.string.device_upgrade_success),
            resources.getString(R.string.device_upgrade_success_tips),
            resources.getString(R.string.no),
            resources.getString(R.string.yes), false
        )
        updateSuccess?.show()
        updateSuccess?.setBtnOnclick(object : OnButtonListener {
            override fun onCancleOnclick() {
                this@OtaActivity.finish()
            }

            override fun onSureOnclick() {
                this@OtaActivity.finish()
            }
        })
    }

    override fun onDownLoadPrepare(key: String) {
        tv_btn_state.setEnabled(false)
        progress_value.setProgress(0)
    }

    override fun onDownLoadProgress(key: String, progress: Int) {

        tv_btn_state.setEnabled(false)
        progress_value.setProgress((progress).toInt())
        tv_btn_state.setText(
            java.lang.String.format(
                this@OtaActivity.getString(R.string.file_downlod_present),
                "" + (progress).toInt()
            )
        )
    }

    override fun onDownLoadError(key: String, throwable: Throwable) {
        tv_btn_state.setEnabled(true)
        progress_value.setProgress(100)
        tv_btn_state.setText(this@OtaActivity.getString(R.string.try_again))
        tv_btn_state.setTag("download")
        downloadFail()
    }



    override fun onDownLoadSuccess(key: String, path: String) {
        tvStateEnabled(true)
        //tvBtnState.setEnabled(true);
        tv_btn_state.setText(this@OtaActivity.getString(R.string.device_upgrade))
        tv_btn_state.setTag("upgrade")
        showUpgradeDialog(this@OtaActivity.getString(R.string.file_downlod_success_tips))
    }

    override fun onDownLoadPause(key: String) {
    }

    override fun onDownLoadCancel(key: String) {
    }

    override fun onUpdate(key: String, progress: Int, read: Long, count: Long, done: Boolean) {
    }

}