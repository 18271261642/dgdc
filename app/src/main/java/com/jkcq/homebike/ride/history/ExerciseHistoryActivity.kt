package com.jkcq.homebike.ride.history

import android.Manifest
import android.os.Bundle
import android.widget.RadioGroup
import androidx.fragment.app.FragmentTransaction
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.base.BaseActivity
import com.jkcq.homebike.R
import com.jkcq.util.MessageEvent
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.TitleView.OnTitleViewOnclick
import kotlinx.android.synthetic.main.activity_exercise_record.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class ExerciseHistoryActivity : BaseActivity() {

    var mDayFragment: DayFragment? = null

    val TYPE_DAY = 1
    val TYPE_WEEK = 2
    val TYPE_MONTH = 3


    var currentType = TYPE_DAY

    override fun getLayoutResId(): Int = R.layout.activity_exercise_record

    override fun initView() {
        // showFragment(FRAGMENT_DAY)
        // rg_main.check(R.id.rbtn_day)


        try {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val fragmentList = FragmentList()
            val bundle = Bundle()
            bundle.putInt("type", FragmentList.TYPE_DAY)
            fragmentList.setArguments(bundle)
            transaction.replace(R.id.fl_content, fragmentList)
            transaction.commitAllowingStateLoss()
        } catch (e: Exception) {
        }

        rg_main.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbtn_day -> TodayObservable.getInstance()
                    .cheackType(TYPE_DAY)
                R.id.rbtn_month -> TodayObservable.getInstance()
                    .cheackType(TYPE_WEEK)
                R.id.rbtn_year -> TodayObservable.getInstance()
                    .cheackType(TYPE_MONTH)
            }
        })

    }

    override fun initData() {
        TodayObservable.getInstance().addObserver(this)
        view_title.setTitleText(this.getString(R.string.history_record))
        initPermission()
    }

    /**
     * 显示fragment
     */
    fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (index) {
            TYPE_DAY -> {
                if (mDayFragment == null) {
                    mDayFragment = DayFragment()
                    transaction.add(R.id.fl_content, mDayFragment!!)
                } else {
                    transaction.show(mDayFragment!!)
                }
            }
            TYPE_WEEK -> {
                /* if (mMineFragment == null) {
                     mMineFragment = MineFragment()
                     transaction.add(R.id.fl_content, mMineFragment!!)
                 } else {
                     transaction.show(mMineFragment!!)
                 }*/
            }
            TYPE_MONTH -> {
                /* if (mMineFragment == null) {
                     mMineFragment = MineFragment()
                     transaction.add(R.id.fl_content, mMineFragment!!)
                 } else {
                     transaction.show(mMineFragment!!)
                 }*/
            }
        }
        transaction.commitAllowingStateLoss()
    }

    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)
        if (o is TodayObservable) {
            try {
                val type = arg as Int
                currentType = type
                if (type === FragmentList.TYPE_DAY) {
                    view_title.setShowCalender(true)
                } else {
                    view_title.setShowCalender(false)
                }
                val manager = supportFragmentManager
                val transaction =
                    manager.beginTransaction()
                val fragmentList = FragmentList()
                val bundle = Bundle()
                bundle.putInt("type", type)
                //bundle.putSerializable(JkConfiguration.DEVICE, deviceBean)
                fragmentList.arguments = bundle
                transaction.replace(R.id.fl_content, fragmentList)
                transaction.commitAllowingStateLoss()
            } catch (e: java.lang.Exception) {
            }
        }
    }

    override fun initEvent() {
        super.initEvent()

        view_title.onTitleOnClick = object : OnTitleViewOnclick {
            override fun onLeftOnclick() {
                finish()
            }

            override fun onRightOnclick() {
                //  finish()
                if (ToastUtil.isFastDoubleClick()) {
                    return
                }
                EventBus.getDefault().post(MessageEvent(MessageEvent.share))

                /*when(currentType){
                    FragmentList.TYPE_DAY->{
                        this@ExerciseHistoryActivity.startActivity(Intent(this@ExerciseHistoryActivity,DayShareActivity::class.java))

                    }FragmentList.TYPE_WEEK->{
                    this@ExerciseHistoryActivity.startActivity(Intent(this@ExerciseHistoryActivity,WeekShareActivity::class.java))


                }FragmentList.TYPE_MONTH->{
                    this@ExerciseHistoryActivity.startActivity(Intent(this@ExerciseHistoryActivity,MonthShareActivity::class.java))

                }
                }
*/
            }

            override fun onCalenderOnclick() {
                //显示日历对话框
                if (ToastUtil.isFastDoubleClick()) {
                    return
                }
                EventBus.getDefault().post(MessageEvent(MessageEvent.calender))
            }
        }
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        // mRideFragment?.let { transaction.hide(it) }
        // mMineFragment?.let { transaction.hide(it) }
    }

    fun initPermission() {
        PermissionUtil.checkPermission(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            permissonCallback = object : PermissionUtil.OnPermissonCallback {
                override fun isGrant(grant: Boolean) {
                    if (grant) {
                        // toast("success")
                    } else {
                        //toast("failed")
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        TodayObservable.getInstance().deleteObserver(this)
    }
}