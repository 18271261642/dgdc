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
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.app.CacheManager
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.util.ThreadPoolUtils
import com.jkcq.homebike.ble.bike.BikeDataViewModel
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.ride.history.adpter.BarBlackAdapter
import com.jkcq.homebike.ride.history.adpter.CenterLayoutManager
import com.jkcq.homebike.ride.history.bean.BarInfo
import com.jkcq.homebike.ride.pk.bean.enumbean.DeviceType
import com.jkcq.homebike.util.FileUtil
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.StatusBarUtil
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_share_day.*
import kotlinx.android.synthetic.main.activity_share_month.*
import kotlinx.android.synthetic.main.activity_share_week.*
import kotlinx.android.synthetic.main.activity_share_week.iv_back
import kotlinx.android.synthetic.main.activity_share_week.iv_head
import kotlinx.android.synthetic.main.activity_share_week.iv_share_all
import kotlinx.android.synthetic.main.activity_share_week.scrollview
import kotlinx.android.synthetic.main.activity_share_week.tv_date
import kotlinx.android.synthetic.main.activity_share_week.tv_nike_name
import kotlinx.android.synthetic.main.view_bike_sport_share_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_share_dis_count.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WeekShareActivity : BaseVMActivity<BikeDataViewModel>() {

    //???????????????????????????
    var mDataList = mutableListOf<BarInfo>()
    var temmDataList = mutableListOf<BarInfo>()

    // var mDateBeans = mutableListOf<HistoryDateBean>()

    lateinit var mBarAdapter: BarBlackAdapter
    lateinit var mCenterLayoutManager: CenterLayoutManager
    val mBikeDataViewModel: BikeDataViewModel by lazy { createViewModel(BikeDataViewModel::class.java) }

    override fun getLayoutResId(): Int = R.layout.activity_share_week
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
                            this@WeekShareActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }

    var date: Int = 0
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.e("1001", "1001")
            when (msg.what) {
                //????????????
                1001 -> {
                    canCLike = false
                    Log.e("1001", "1001")

                }
            }
        }
    }

    override fun initData() {

        /*intent.putExtra("date",date)
        intent.putExtra("strStartWeek",strStartWeek)
        intent.putExtra("strEndWeek",strEndWeek)*/
        var date = intent.getIntExtra("date", 0)
        var strStartWeek = intent.getStringExtra("strStartWeek")
        var strEndWeek = intent.getStringExtra("strEndWeek")
        var headUrl = ""
        var nikeName = ""
        if (CacheManager.mUserInfo != null) {
            headUrl = CacheManager.mUserInfo!!.headUrlTiny
            nikeName = CacheManager.mUserInfo!!.nickName
        }
        LoadImageUtil.getInstance().load(this, headUrl, iv_head, R.mipmap.friend_icon_default_photo)
        tv_nike_name.text = nikeName
        mBikeDataViewModel.getDeviceSummary(
            strStartWeek,
            "" + DeviceType.DEVICE_S005.value + "," + DeviceType.DEVICE_S003.value,
            BikeConfig.BIKE_SPORT_DATE_WEEK,
            mUserId
        )
        tv_date.text = strStartWeek + "~" + strEndWeek
        //mBikeDataViewModel.getDeviceDailyBrief(strDate, "" + BikeConfig.BIKE_TYPE, mUserId)


        mBikeDataViewModel.getBikeWeekData(date)
        initBarRec()
    }

    fun initBarRec() {
        mCenterLayoutManager = CenterLayoutManager(this, RecyclerView.HORIZONTAL, true)
        recyclerview_sport.layoutManager = mCenterLayoutManager
        mBarAdapter = BarBlackAdapter(mDataList, 7)
        recyclerview_sport.adapter = mBarAdapter
        mBarAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->

            if (TextUtils.isEmpty(mDataList.get(position).date)) {
                return@OnItemClickListener;
            }

            mCenterLayoutManager.smoothScrollToPosition(
                recyclerview_sport,
                RecyclerView.State(),
                position
            )
            for (user in mDataList) {
                user.isSelect = false;
            }

            mDataList.get(position).isSelect = true;
            adapter.notifyDataSetChanged()
        })
    }

    fun setEmpView() {
        mDataList.clear()
        mBarAdapter.setEmptyView(R.layout.include_rope_blac_empty_view)
        mBarAdapter.notifyDataSetChanged()
    }
    /**
     * ??????fragment
     */

    /**
     * ???????????????Fragment
     */

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun startObserver() {


        //????????????????????????
        mBikeDataViewModel.mDailySummariesBean.observe(this, androidx.lifecycle.Observer {
            if (it == null || it.size == 0) {
                mDataList.clear()
                mBarAdapter.notifyDataSetChanged()
                setEmpView()
                //  mBarAdapter.setEmptyView(R.layout.include_rope_empty_view)
            } else {
                recyclerview_sport.visibility = View.VISIBLE
                var maxValue = 0
                it.forEach {
                    if (maxValue < it.totalCalorie.toInt()) {
                        maxValue = it.totalCalorie.toInt()
                    }

                }
                mDataList.clear()
                mDataList.addAll(temmDataList)
                it.forEach {
                    var date = it.exerciseDay
                    var bean = mDataList.findLast {
                        it.mdDate.equals(date)
                    }
                    if (maxValue < it.totalCalorie.toInt()) {
                        maxValue = it.totalCalorie.toInt()
                    }
                    bean!!.currentValue = it.totalCalorie.toInt()
                    bean!!.maxVlaue = maxValue

                }
                var size = 0
                while (size < mDataList.size) {
                    if (mDataList.get(size).currentValue > 0) {
                        mDataList.get(size).isSelect = true
                        break;
                    }
                    size++
                }
                mBarAdapter.notifyDataSetChanged()
            }
        })


//?????????????????????
        mBikeDataViewModel.mHistoryDateBean.observe(this, androidx.lifecycle.Observer {
            temmDataList.clear()
            it!!.forEach {
                temmDataList.add(BarInfo(it.mdDate, it.date, 0, 100, false))
            }
            mBikeDataViewModel.getDeviceDailySummaries(
                temmDataList.get(temmDataList.size - 1).mdDate,
                "" + DeviceType.DEVICE_S003.value + "," + DeviceType.DEVICE_S005.value,
                BikeConfig.BIKE_SPORT_DATE_WEEK,
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
     * ???????????????
     *
     * @return
     */
    fun getFullScreenBitmap(scrollVew: NestedScrollView): File? {
        var h = 0
        val bitmap: Bitmap
        for (i in 0 until scrollVew.childCount) {
            h += scrollVew.getChildAt(i).height
        }
        // ?????????????????????bitmap
        bitmap = Bitmap.createBitmap(
            scrollVew.width, h,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(this.resources.getColor(R.color.common_view_color))
        scrollVew.draw(canvas)

        //?????????????????????bitmap
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
        // cv.drawBitmap(head, 0f, 0f, null) // ??? 0???0??????????????????headBitmap
        //  cv.drawBitmap(bitmap, 0f, head.height.toFloat(), null) // ??? 0???headHeight???????????????????????????Bitmap
        canvas.save() // ??????
        canvas.restore() // ??????
        //??????
        //head.recycle()
        // ????????????
        return FileUtil.writeImage(bitmap, FileUtil.getImageFile(FileUtil.getPhotoFileName()), 100)
    }


    fun shareFile(file: File?) {
        if (null != file && file.exists()) {
            val share = Intent(Intent.ACTION_SEND)
            var uri: Uri? = null
            // ????????????????????????7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // "????????????.fileprovider"?????????????????????????????????authorities

                //?????????????????????7.0??????
                //??????1 ?????????, ??????2 Provider???????????? ??????????????????????????????   ??????3  ???????????????
//            Uri apkUri = FileProvider.getUriForFile(context, "com.jkcq.gym.phone.fileProvider", file);
                uri = FileProvider.getUriForFile(
                    this@WeekShareActivity,
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
            share.type = getMimeType(file.absolutePath) //???????????????????????????
            share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            this.startActivity(Intent.createChooser(share, "Share Image"))
        }
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

    // ????????????????????????????????????MIME?????????
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