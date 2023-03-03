package com.jkcq.homebike.login

import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.jkcq.base.app.CacheManager
import com.jkcq.base.app.Preference
import com.jkcq.base.base.BaseVMActivity
import com.jkcq.homebike.MainActivity
import com.jkcq.homebike.R
import com.jkcq.homebike.bike.BikeConfig
import com.jkcq.platform.umeng.PlatformLoginManager
import com.jkcq.util.*
import com.jkcq.util.ktx.ToastUtil
import kotlinx.android.synthetic.main.activity_about_activity.*
import kotlinx.android.synthetic.main.activity_login.*

@Route(path = "/app/LoginActivity")
class LoginActivity : BaseVMActivity<LoginViewModel>() {

    val mLoginViewModel: LoginViewModel by lazy { createViewModel(LoginViewModel::class.java) }
    var mMobile: String by Preference(Preference.MOBILE, "")
    var isFirstUSerApp: Boolean by Preference(Preference.ISFIRSTUSERAPP, true)

    var isPhoneLogin = true
    override fun getLayoutResId(): Int = R.layout.activity_login

    override fun initView() {
        if (!AppUtil.isCN()) {
            imgbtn_login_twitter.visibility = View.VISIBLE
            imgbtn_login_facebook.visibility = View.VISIBLE
            view_space.visibility = View.GONE
            //PlatformLoginManager.setLoginOption(this)
        }
    }

    override fun initEvent() {
        AnimationUtil.ScaleUpView(rbtn_phone)
        rg_login.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbtn_phone -> {
                    isPhoneLogin = true
                    ll_phone.visibility = View.VISIBLE
                    et_email.visibility = View.INVISIBLE
                    AnimationUtil.ScaleUpView(rbtn_phone)
                    AnimationUtil.ScaleDownView(rbtn_home_email)
//                    rbtn_phone.textSize = resources.getDimension(R.dimen.sp20)
//                    rbtn_home_email.textSize = resources.getDimension(R.dimen.sp16)

                }
                R.id.rbtn_home_email -> {
                    isPhoneLogin = false
                    ll_phone.visibility = View.INVISIBLE
                    et_email.visibility = View.VISIBLE
                    AnimationUtil.ScaleUpView(rbtn_home_email)
                    AnimationUtil.ScaleDownView(rbtn_phone)
                }
            }
        }
        timerview.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            timerview.startTimer()
            if (isPhoneLogin) {
                mLoginViewModel.getVerifyCodeByMobile(et_phone.text.toString())
            } else {
                mLoginViewModel.getVerifyCodeByEmail(et_email.text.toString())
            }
        }

        btn_login.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            if (isPhoneLogin) {
                loginByPhone()
            } else {
                loginByEmail()
            }
        }

        imgbtn_login_weixin.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            mLoginViewModel.loginByWechat(this@LoginActivity)
        }
        imgbtn_login_qq.setOnClickListener { mLoginViewModel.LoginByQQ(this@LoginActivity) }

        imgbtn_login_facebook.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            mLoginViewModel.loginByFaceBook(this@LoginActivity)
        }
        imgbtn_login_twitter.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            mLoginViewModel.loginByGoogle(this@LoginActivity)
        }


        tv_pro.setOnClickListener {
            if (ToastUtil.isFastDoubleClick()) {
                return@setOnClickListener
            }
            var intent = Intent(this@LoginActivity, WebViewActivity::class.java)
            intent.putExtra(
                "url",
                " https://test.gateway.spinning.fitalent.com.cn/spinning-h5/#/userAgreement"
            )
            intent.putExtra("title", this@LoginActivity.getString(R.string.user_agreement))
            startActivity(intent)


        }
        privacy_agreement.setOnClickListener {
            var intent = Intent(this@LoginActivity, WebViewActivity::class.java)
            intent.putExtra(
                "url",
                " https://test.gateway.spinning.fitalent.com.cn/spinning-h5/#/privacyAgreement"
            )
            intent.putExtra("title", this@LoginActivity.getString(R.string.privacy_agreement))
            startActivity(intent)
        }


    }

    override fun initData() {

        if (isFirstUSerApp) {
            var priDialog = PriDialog(this, "", "", "", "")
            priDialog.setBtnOnclick(object : OnButtonPriListener {
                override fun onClickPri() {
                    var intent = Intent(this@LoginActivity, WebViewActivity::class.java)
                    intent.putExtra(
                        "url",
                        " https://test.gateway.spinning.fitalent.com.cn/spinning-h5/#/privacyAgreement"
                    )
                    intent.putExtra(
                        "title",
                        this@LoginActivity.getString(R.string.privacy_agreement)
                    )
                    startActivity(intent)
                }

                override fun onCancleOnclick() {
                    finish()
                }

                override fun onSureOnclick() {
                    isFirstUSerApp = false
                }
            })
            priDialog.show()

        }
    }

    override fun startObserver() {
        mLoginViewModel.mUserInfoLiveData.observe(this, Observer {
            timerview.stopTimer()
            CacheManager.mUserInfo = it
            if (!TextUtils.isEmpty(it.mobile)) {
                mMobile = it.mobile
            }
            BikeConfig.currentUser = it
            mUserId = it.userId.toString()
            if (it.isIsRegidit) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        SupplementUserInfoActivity::class.java
                    )
                )
            }

            finish()
        })
    }


    fun loginByPhone() {
        val phone = et_phone.text.toString()
        val pwd = et_code.text.toString()
        mLoginViewModel.loginByMobile(phone, pwd)
    }


    fun loginByEmail() {
        val email = et_email.text.toString()
        val pwd = et_code.text.toString()
        mLoginViewModel.loginByEmail(email, pwd)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PlatformLoginManager.handleActivityResult(this@LoginActivity, requestCode, resultCode, data)
    }
}