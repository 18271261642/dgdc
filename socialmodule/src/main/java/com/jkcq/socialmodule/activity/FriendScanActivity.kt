package com.jkcq.socialmodule.activity

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.socialmodule.FriendConstant
import com.jkcq.socialmodule.mvvm.FriendViewmodel
import com.jkcq.socialmodule.R
import com.jkcq.util.LogUtil
import com.jkcq.util.StatusBarUtil
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_friend_scan.*
import me.devilsen.czxing.code.BarcodeFormat
import me.devilsen.czxing.util.BarCodeUtil
import me.devilsen.czxing.util.ScreenUtil
import me.devilsen.czxing.util.SoundPoolUtil
import me.devilsen.czxing.view.ScanListener
import me.devilsen.czxing.view.ScanListener.AnalysisBrightnessListener

class FriendScanActivity : BaseVMActivity<FriendViewmodel>(),
    View.OnClickListener, ScanListener, AnalysisBrightnessListener {

    //播放提示音
    private var mSoundPoolUtil: SoundPoolUtil? = null
    override fun getLayoutResId(): Int {
        return R.layout.activity_friend_scan
    }

    override fun initView() {
        val supportActionBar = supportActionBar
        supportActionBar?.hide()
        ScreenUtil.setFullScreen(this)
        tv_title.setText(getString(R.string.friend_scan))

        // 设置扫描模式
//        mScanView.setScanMode(ScanView.SCAN_MODE_MIX);
        // 设置扫描格式 BarcodeFormat
//        mScanView.setBarcodeFormat();
        val scanBox = scanview.getScanBox()
        // 设置扫码框上下偏移量，可以为负数
//       scanBox.setBoxTopOffset(-BarCodeUtil.dp2px(this, 100));
        // 设置边框大小
        scanBox.setBorderSize(BarCodeUtil.dp2px(this, 200f), BarCodeUtil.dp2px(this, 200f))
        // 设置扫码框四周的颜色
        scanBox.setMaskColor(Color.parseColor("#9C272626"))
        // 设定四个角的颜色
//        scanBox.setCornerColor();
        // 设定扫描框的边框颜色
//        scanBox.setBorderColor();
        // 设置边框长度(扫码框大小)
//        scanBox.setBorderSize();
        // 设定扫描线的颜色
//        scanBox.setScanLineColor();
        // 设置扫码线移动方向为水平（从左往右）
//      scanBox.setHorizontalScanLine();
        // 设置手电筒打开时的图标
        scanBox.setFlashLightOnDrawable(R.drawable.ic_highlight_blue_open_24dp)
        // 设置手电筒关闭时的图标
        scanBox.setFlashLightOffDrawable(R.drawable.ic_highlight_white_close_24dp)
        // 设置闪光灯打开时的提示文字
        scanBox.setFlashLightOnText(getString(R.string.friend_open_light))
        // 设置闪光灯关闭时的提示文字
        scanBox.setFlashLightOffText(getString(R.string.friend_close_light))
        // 不使用手电筒图标及提示
//        scanBox.invisibleFlashLightIcon();
        // 设置扫码框下方的提示文字
        scanBox.setScanNoticeText(getString(R.string.friend_input_qrcode_to_frame))
        iv_back.setOnClickListener(this)
        // 获取扫码回调
        scanview.setScanListener(this)
        // 获取亮度测量结果
        scanview.setAnalysisBrightnessListener(this)

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_back.getLayoutParams();
//        layoutParams.topMargin = ScreenUtil.getStatusBarHeight(this);
        mSoundPoolUtil = SoundPoolUtil()
        mSoundPoolUtil?.loadDefault(this)
    }

    override fun initData() {}
    override fun startObserver() {}
    override fun onResume() {
        super.onResume()
        scanview.openCamera() // 打开后置摄像头开始预览，但是并未开始识别
        scanview.startScan() // 显示扫描框，并开始识别
        Log.e("onScanSuccess", "onResume")
    }

    override fun onPause() {
        super.onPause()
        scanview.stopScan()
        scanview.closeCamera() // 关闭摄像头预览，并且隐藏扫描框
    }

    override fun onDestroy() {
        scanview.onDestroy() // 销毁二维码扫描控件
        mSoundPoolUtil?.release()
        super.onDestroy()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.iv_back) {
            finish()
        }
    }

    override fun onScanSuccess(
        result: String,
        format: BarcodeFormat
    ) {
        LogUtil.d("qrResult=" + result)
        mSoundPoolUtil?.play()
        val size = result.length
        if (result.contains(FriendConstant.QR_HEAD)) {
            getUserIdByQrString(result.substring(8))
        } else if (result.startsWith("http")) {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse(result)
            intent.data = content_url
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@FriendScanActivity, "QRCode=$result", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 可以通过此回调来控制自定义的手电筒显隐藏
     *
     * @param isDark 是否处于黑暗的环境
     */
    override fun onAnalysisBrightness(isDark: Boolean) {
        if (isDark) {
            Log.d("analysisBrightness", "您处于黑暗的环境，建议打开手电筒")
        } else {
            Log.d("analysisBrightness", "正常环境，如果您打开了手电筒，可以关闭")
        }
    }

    override fun onOpenCameraError() {
        Log.e("onOpenCameraError", "onOpenCameraError")
    }

    /**
     * 最好跳转进来之前做权限检查
     */
    private fun requestCameraPermission() {
        PermissionUtil.checkPermission(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {
                        scanview.openCamera()
                        scanview.startScan()
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton, getString(R.string.please_open_camera_permission)
                        )
                    }
                }

            })
    }


    private fun getUserIdByQrString(qrString: String) {}

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, rl_back)
        StatusBarUtil.setLightMode(this)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CAMERA = 1
        private const val PERMISSIONS_REQUEST_STORAGE = 2
        private const val CODE_SELECT_IMAGE = 100
    }
}