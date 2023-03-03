package com.jkcq.homebike.share

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.CacheManager
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.util.ThreadPoolUtils
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.StatusBarUtil
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_edit_user_info.*
import kotlinx.android.synthetic.main.activity_share_day.*
import kotlinx.android.synthetic.main.activity_share_day.iv_back
import kotlinx.android.synthetic.main.activity_share_day.iv_head
import kotlinx.android.synthetic.main.activity_share_day.iv_share_all
import kotlinx.android.synthetic.main.activity_share_day.scrollview
import kotlinx.android.synthetic.main.activity_share_day.tv_date
import kotlinx.android.synthetic.main.activity_share_day.tv_nike_name
import kotlinx.android.synthetic.main.activity_share_month.*
import kotlinx.android.synthetic.main.view_bike_sport_share_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_share_dis_count.*
import java.io.File

class DayShareActivity : BaseVMActivity<BikeDataViewModel>() {


    val mBikeDataViewModel: BikeDataViewModel by lazy { createViewModel(BikeDataViewModel::class.java) }

    override fun getLayoutResId(): Int = R.layout.activity_share_day
    var picFile: File? = null
    var canCLike = false
    override fun initView() {

        iv_back.setOnClickListener {
            finish()
        }
        iv_share_all.setOnClickListener {

            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            if (canCLike) {
                return@setOnClickListener
            }
            canCLike = true
            if (picFile != null) {
                handler.sendEmptyMessageDelayed(1001, 1000)
                shareFile(picFile)
            } else {
                handler.sendEmptyMessageDelayed(1001, 3000)
                initPermission()
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
                        ThreadPoolUtils.getInstance().addTask(Runnable {
                            // ThreadPoolUtils.getInstance().addTask(new ShootTask(scrollView, ActivityScaleReport.this, ActivityScaleReport.this));
                            picFile = getFullScreenBitmap(scrollview)
                            Log.e("setOnClickListener", "" + picFile)
                            handler.post {
                                shareFile(picFile)
                            }

                            // initLuBanRxJava(getFullScreenBitmap(scrollView));
                        })
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@DayShareActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }


    override fun initData() {
        var strDate = intent.getStringExtra("date")
        var headUrl = ""
        var nikeName = ""
        tv_date.text = strDate
        if (CacheManager.mUserInfo != null) {
            headUrl = CacheManager.mUserInfo!!.headUrlTiny
            nikeName = CacheManager.mUserInfo!!.nickName
        }
        LoadImageUtil.getInstance().load(this, headUrl, iv_head, R.mipmap.friend_icon_default_photo)
        tv_nike_name.text = nikeName
        mBikeDataViewModel.getDeviceSummary(
            strDate,
            "" + DeviceType.DEVICE_S005.value + "," + DeviceType.DEVICE_S003.value,
            BikeConfig.BIKE_SPORT_DATE_DAY,
            mUserId
        )
    }


    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.e("1001", "1001")
            when (msg.what) {
                //连接超时
                1001 -> {
                    canCLike = false
                    Log.e("1001", "1001")

                }
            }
        }
    }

    /**
     * 显示fragment
     */

    /**
     * 隐藏所有的Fragment
     */


    override fun startObserver() {

        mBikeDataViewModel.sumSummerBean.observe(this, androidx.lifecycle.Observer {
            setItemvalue(it)
        })
    }

    fun setItemvalue(summerBean: Summary) {

        BikeConfig.disUnit(tv_dis_unitl, this)
        val dis = SiseUtil.disUnitCoversion(summerBean.totalDistance, BikeConfig.userCurrentUtil)
        val cal = SiseUtil.calCoversion(summerBean.totalCalorie)
        val min = SiseUtil.timeCoversionMin(summerBean.totalDuration)
        val power = SiseUtil.powerCoversion(summerBean.totalPowerGeneration)
        tv_dis_value.text = dis
        tv_view_sport_count.text = summerBean.totalTimes
        item_time.setValueText(min)
        item_cal.setValueText(cal)
        item_power.setValueText(power)
    }


    override fun onDestroy() {
        super.onDestroy()

    }


    /**
     * 获取长截图
     *
     * @return
     */
    fun getFullScreenBitmap(scrollVew: NestedScrollView): File? {
        var h = 0
        val bitmap: Bitmap
        for (i in 0 until scrollVew.childCount) {
            h += scrollVew.getChildAt(i).height
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(
            scrollVew.width, h,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(this.resources.getColor(R.color.common_view_color))
        scrollVew.draw(canvas)

        //获取顶部布局的bitmap
        /*val head = Bitmap.createBitmap(
            rl_scale_report_head.getWidth(), rl_scale_report_head.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvasHead = Canvas(head)
        canvasHead.drawColor(Color.WHITE)
        rl_scale_report_head.draw(canvasHead)*/
        /*  val newbmp =
              Bitmap.createBitmap(scrollVew.width, h + head.height, Bitmap.Config.ARGB_8888)
          val cv = Canvas(newbmp)*/
        // cv.drawBitmap(head, 0f, 0f, null) // 在 0，0坐标开始画入headBitmap
        //  cv.drawBitmap(bitmap, 0f, head.height.toFloat(), null) // 在 0，headHeight坐标开始填充课表的Bitmap
        canvas.save() // 保存
        canvas.restore() // 存储
        //回收
        //head.recycle()
        // 测试输出

        Log.e("bitmap", "bitmap=" + bitmap)
        return FileUtil.writeImage(bitmap, FileUtil.getImageFile(FileUtil.getPhotoFileName()), 100)
    }


    fun shareFile(file: File?) {
        if (null != file && file.exists()) {
            val share = Intent(Intent.ACTION_SEND)
            var uri: Uri? = null
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // "项目包名.fileprovider"即是在清单文件中配置的authorities

                //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//            Uri apkUri = FileProvider.getUriForFile(context, "com.jkcq.gym.phone.fileProvider", file);
                uri = FileProvider.getUriForFile(
                    this@DayShareActivity,
                    "com.jkcq.homebike.bike.fileProvider",
                    file
                )
                /*uri = FileProvider.getUriForFile(
                    this@DayShareActivity, "$packageName.fileprovider",
                    file
                )*/
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                uri = Uri.fromFile(file)
            }
            share.putExtra(Intent.EXTRA_STREAM, uri)
            share.type = getMimeType(file.absolutePath) //此处可发送多种文件
            share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            this.startActivity(Intent.createChooser(share, "Share Image"))
        }
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

    // 根据文件后缀名获得对应的MIME类型。
    private fun getMimeType(filePath: String): String? {
        val mmr = MediaMetadataRetriever()
        /* if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }*/return "image/*"
    }
}