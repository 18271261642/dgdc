package com.jkcq.homebike.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.mine.mvvm.viewmodel.UserModel
import com.jkcq.util.*
import kotlinx.android.synthetic.main.activity_about_activity.*
import kotlinx.android.synthetic.main.activity_setting_activity.*
import kotlinx.android.synthetic.main.activity_setting_activity.iv_back
import kotlinx.android.synthetic.main.activity_supplement_user_info.*

class AboutActivity : BaseVMActivity<UserModel>() {


    override fun getLayoutResId(): Int = R.layout.activity_about_activity


    override fun initEvent() {
        super.initEvent()
        iv_back.setOnClickListener { finish() }

    }

    override fun initView() {
        tv_user_protol.setOnClickListener {
            var intent = Intent(this@AboutActivity, WebViewActivity::class.java)
            intent.putExtra(
                "url",
                " https://test.gateway.spinning.fitalent.com.cn/spinning-h5/#/userAgreement"
            )
            intent.putExtra("title", this@AboutActivity.getString(R.string.user_agreement))
            startActivity(intent)


        }
        tv_privacy.setOnClickListener {
            var intent = Intent(this@AboutActivity, WebViewActivity::class.java)
            intent.putExtra(
                "url",
                " https://test.gateway.spinning.fitalent.com.cn/spinning-h5/#/privacyAgreement"
            )
            intent.putExtra("title", this@AboutActivity.getString(R.string.privacy_agreement))
            startActivity(intent)
        }
    }

    override fun initData() {
        val version: String = getVersion(this)
        tv_version.text = (
                java.lang.String.format(
                    resources.getString(R.string.app_version),
                    version
                )
                )
    }

    fun getVersion(context: Context): String {
        val packageManager = context.packageManager
        val packInfo: PackageInfo
        var version: String = ""
        try {
            packInfo = packageManager.getPackageInfo(
                context.packageName,
                0
            )
            version = packInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    override fun startObserver() {

    }


    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, iv_back)
        StatusBarUtil.setLightMode(this)
    }

}