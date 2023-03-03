package com.jkcq.homebike.web

import android.Manifest
import com.example.utillibrary.PermissionUtil
import com.jkcq.base.app.BaseApp
import com.jkcq.base.base.BaseActivity
import com.jkcq.homebike.R
import com.jkcq.util.ktx.ToastUtil
import com.jkcq.viewlibrary.TitleView.OnTitleViewOnclick
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.app_activity_rope_web.*

class WebActivity : BaseActivity() {


    private var urldark: String? = null
    private var urlLigh: kotlin.String? = null
    override fun getLayoutResId(): Int = R.layout.app_activity_rope_web

    override fun initView() {
    }

    override fun initData() {
        initPermission()


        // url = getIntent().getStringExtra("url");
        urldark = intent.getStringExtra("urldark")
        urlLigh = intent.getStringExtra("urlLigh")
        tk_webview_def.loadUrl(urlLigh)
    }


    override fun initEvent() {
        super.initEvent()
        view_title.onTitleOnClick = object : OnTitleViewOnclick {
            override fun onLeftOnclick() {
                finish()
            }

            override fun onRightOnclick() {
                finish()
            }

            override fun onCalenderOnclick() {
            }
        }
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
                    } else {
                        ToastUtil.showTextToast(
                            BaseApp.sApplicaton,
                            this@WebActivity.getString(R.string.no_required_permission)
                        )
                    }
                }
            })
    }
}