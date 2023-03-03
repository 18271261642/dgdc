package com.jkcq.homebike

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jkcq.base.app.Preference
import com.jkcq.homebike.login.BindPhoneActivity
import com.jkcq.homebike.login.LoginActivity

class FlashActivity : AppCompatActivity() {

    var mUserId: String by Preference(Preference.USER_ID, "")
    var mMobile: String by Preference(Preference.MOBILE, "")

    val DELAYED_TIME = 1000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_flash)
        initView()
    }

    fun initView() {
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }

        Handler().postDelayed({
            initData()
        }, DELAYED_TIME)
    }

    fun initData() {

        if (mUserId.isBlank()) {
            startActivity(Intent(this@FlashActivity, LoginActivity::class.java))
        } else {
            startActivity(Intent(this@FlashActivity, MainActivity::class.java))

        }
        finish()
//        overridePendingTransition(R.anim.anim_main_show, R.anim.anim_main_hide)
    }
}