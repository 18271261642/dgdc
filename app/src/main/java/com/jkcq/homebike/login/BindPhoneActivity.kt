package com.jkcq.homebike.login

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.Observer
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.MainActivity
import com.jkcq.homebike.R
import com.jkcq.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_bind_phone.*

class BindPhoneActivity : BaseVMActivity<LoginViewModel>() {
    val mLoginViewModel: LoginViewModel by lazy { createViewModel(LoginViewModel::class.java) }
    override fun getLayoutResId(): Int = R.layout.activity_bind_phone

    override fun initView() {
        btn_commit.setOnClickListener {
            mLoginViewModel.loginByMobile(et_phone.text.toString(), et_code.text.toString())
        }
        timerview.setOnClickListener { mLoginViewModel.getVerifyCodeByMobile(et_phone.text.toString()) }
        iv_back.setOnClickListener { finish() }
    }

    override fun initData() {
    }

    override fun startObserver() {
        mLoginViewModel.mUserInfoLiveData.observe(this, Observer {
                startActivity(Intent(this@BindPhoneActivity, MainActivity::class.java))
            finish()
        })
    }

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }
}