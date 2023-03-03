package com.jkcq.homebike.share

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.CacheManager
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.base.observable.NetDialogObservable
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.ride.history.adpter.BarAdapter
import com.jkcq.homebike.ride.history.adpter.CenterLayoutManager
import com.jkcq.homebike.ride.history.bean.BarChartEntity
import com.jkcq.homebike.ride.history.bean.BarInfo
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.StatusBarUtil
import com.jkcq.util.ThreadPoolUtils
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_share_day.*
import kotlinx.android.synthetic.main.activity_share_month.*
import kotlinx.android.synthetic.main.activity_share_month.iv_back
import kotlinx.android.synthetic.main.activity_share_month.iv_head
import kotlinx.android.synthetic.main.activity_share_month.iv_share_all
import kotlinx.android.synthetic.main.activity_share_month.scrollview
import kotlinx.android.synthetic.main.activity_share_month.tv_date
import kotlinx.android.synthetic.main.activity_share_month.tv_nike_name
import kotlinx.android.synthetic.main.view_bike_sport_share_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_share_dis_count.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MonthShareActivity : BaseVMActivity<BikeDataViewModel>() {

    //柱状图的变量初始化
    var mDataList = mutableListOf<BarInfo>()
    var temmDataList = mutableListOf<BarInfo>()

    // var mDateBeans = mutableListOf<HistoryDateBean>()

    lateinit var mBarAdapter: BarAdapter
    lateinit var mCenterLayoutManager: CenterLayoutManager
    val mBikeDataViewModel: BikeDataViewModel by lazy { createViewModel(BikeDataViewModel::class.java) }


    override fun getLayoutResId(): Int = R.layout.activity_share_month
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
                            this@MonthShareActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    var date: Int = 0

    private val dateFormatYYMM = SimpleDateFormat("yyyy-MM")
    override fun initData() {

        /*intent.putExtra("date",date)
        intent.putExtra("strStartWeek",strStartWeek)
        intent.putExtra("strEndWeek",strEndWeek)*/
        //   var strDate = "2020-12-10"
        var date = intent.getIntExtra("date", 0)
        var headUrl = ""
        var nikeName = ""
        if (CacheManager.mUserInfo != null) {
            headUrl = CacheManager.mUserInfo!!.headUrlTiny
            nikeName = CacheManager.mUserInfo!!.nickName
        }
        LoadImageUtil.getInstance().load(this, headUrl, iv_head, R.mipmap.friend_icon_default_photo)
        tv_nike_name.text = nikeName
        mBikeDataViewModel.getDeviceSummary(
            dateFormatYYMM.format(Date(date * 1000L)) + "-01",
            "" + DeviceType.DEVICE_S005.value + "," + DeviceType.DEVICE_S003.value,
            BikeConfig.BIKE_SPORT_DATE_MONTH,
            mUserId
        )
        tv_date.text = dateFormatYYMM.format(Date(date * 1000L))
        //mBikeDataViewModel.getDeviceDailyBrief(strDate, "" + BikeConfig.BIKE_TYPE, mUserId)


        mBikeDataViewModel.getBikeMonthData(date)
    }

    /**
     * 显示fragment
     */

    /**
     * 隐藏所有的Fragment
     */

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun startObserver() {


        //每周日的概要数据
        mBikeDataViewModel.mDailySummariesBean.observe(this, androidx.lifecycle.Observer {
            if (it == null || it.size == 0) {
                barChartView.visibility = View.INVISIBLE
                iv_no_data.visibility = View.VISIBLE

                //  mBarAdapter.setEmptyView(R.layout.include_rope_empty_view)
            } else {
                barChartView.visibility = View.VISIBLE
                iv_no_data.visibility = View.INVISIBLE
                var maxValue = 0
                it.forEach {
                    if (maxValue < it.totalCalorie.toInt()) {
                        maxValue = it.totalCalorie.toInt()
                    }

                }
                it.forEach {
                    var date = it.exerciseDay
                    var bean = mDataList.findLast {
                        it.mdDate.equals(date)
                    }
                    if (maxValue < it.totalCalorie.toInt()) {
                        maxValue = it.totalCalorie.toInt()
                    }
                    bean?.currentValue = it.totalCalorie.toInt()
                    bean?.maxVlaue = maxValue

                }

                setWeekBarChartData()


            }
        })


        //获取每周的日期
        mBikeDataViewModel.mHistoryDateBean.observe(this, androidx.lifecycle.Observer {
            mDataList.clear()
            it?.forEach {
                mDataList.add(BarInfo(it.mdDate, it.date, 0, 100, false))
            }



            mBikeDataViewModel.getDeviceDailySummaries(
                mDataList.get(0).mdDate,
                "" + DeviceType.DEVICE_S003.value + "," + DeviceType.DEVICE_S005.value,
                BikeConfig.BIKE_SPORT_DATE_MONTH,
                mUserId
            )
        })

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
                    this@MonthShareActivity,
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

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

    private fun setWeekBarChartData() {
        val datas: MutableList<BarChartEntity> = ArrayList()
        for (i in mDataList.indices) {
            //val cal = StepArithmeticUtil.stepsConversionCaloriesFloat(userInfoBean.getWeight().toFloat(), stepList[i].toLong()).toInt()
            // val dis = StepArithmeticUtil.stepsConversionDistanceFloat(userInfoBean.getHeight().toFloat(), userInfoBean.getGender(), stepList[i].toLong())
            datas.add(
                BarChartEntity(
                    i.toString(),
                    mDataList.get(i).date,
                    arrayOf(mDataList.get(i).currentValue * 1.0f, 0f, 0f)
                )
            )
        }
        /* if (datas.size() == 31) {
            datas.add(new BarChartEntity(String.valueOf(0), new Float[]{Float.valueOf(0), Float.valueOf(0), Float.valueOf(0)}));
            date.add(date.get(date.size() - 1));
        }*/

        var index = 0
        var clickPostion = 0
        while (index < mDataList?.size) {
            if (mDataList.get(index).currentValue > 0) {
                clickPostion = index
            }
            //println("item at $index is ${items[index]}")
            index++
        }

        //  barChartView.setOnItemBarClickListener { position -> Log.e("TAG", "点击了：$position") }
        barChartView.setData(datas, intArrayOf(Color.parseColor("#6FC5F4")), "分组", "数量")
        barChartView.startAnimation()
        handler.postDelayed({
            barChartView.setmClickPosition(clickPostion)
        }, 50)


    }

}