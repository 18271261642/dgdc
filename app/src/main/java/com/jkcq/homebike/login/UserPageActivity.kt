package com.jkcq.homebike.login

import android.Manifest
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.CacheManager
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.MainActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.socialmodule.activity.FriendScanActivity
import com.jkcq.util.KeyboardUtils
import com.jkcq.util.LoadImageUtil
import com.jkcq.util.SiseUtil
import com.jkcq.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_user_page.*
import kotlinx.android.synthetic.main.view_bike_sport_cal_time_pow.*
import kotlinx.android.synthetic.main.view_bike_sport_dis_count.*

class UserPageActivity : BaseVMActivity<UserModel>() {

    val mUserModel: UserModel by lazy { createViewModel(UserModel::class.java) }


    override fun getLayoutResId(): Int = R.layout.activity_user_page

    override fun initView() {


        iv_back.setOnClickListener { finish() }
        iv_edit.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    EditUserInfoActivity::class.java
                )
            )

        }
    }

    var headUrl = ""
    var bgUrl = ""
    var strProfile = ""
    var nikename = ""

    override fun initData() {

        if (CacheManager.mUserInfo != null) {
            headUrl = CacheManager.mUserInfo!!.headUrlTiny
            bgUrl = CacheManager.mUserInfo!!.backgroundUrl
            strProfile = CacheManager.mUserInfo!!.myProfile
            nikename = CacheManager.mUserInfo!!.nickName

        }
        if (BikeConfig.summary != null) {
            setItemvalue(BikeConfig.summary)
        }
        tv_nickname.text = nikename
        tv_profile.text = this.resources.getString(R.string.user_introduction) + strProfile
        LoadImageUtil.getInstance().load(this, headUrl, iv_head, R.mipmap.friend_icon_default_photo)
        LoadImageUtil.getInstance()
            .load(this, bgUrl, iv_head_bg, R.mipmap.friend_icon_default_photo)

    }

    override fun startObserver() {
        mUserModel.mEditState.observe(this, Observer {
            if (it) {
                startActivity(Intent(this@UserPageActivity, MainActivity::class.java))
            }
        })
    }

    fun showSelectPopWindow(v: View) {
        KeyboardUtils.hideKeyboard(v)
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

    fun setItemvalue(summerBean: Summary) {

        val dis = SiseUtil.disUnitCoversion(summerBean.totalDistance, BikeConfig.userCurrentUtil)
        val cal = SiseUtil.calCoversion(summerBean.totalCalorie)
        val min = SiseUtil.timeCoversionMin(summerBean.totalDuration)
        val power = SiseUtil.powerCoversion(summerBean.totalPowerGeneration)
        BikeConfig.disUnit(tv_dis_unitl, this)
        tv_dis_value.text = dis
        tv_view_sport_count.text = summerBean.totalTimes
        item_time.setValueText(min)
        item_cal.setValueText(cal)
        item_power.setValueText(power)
    }
}