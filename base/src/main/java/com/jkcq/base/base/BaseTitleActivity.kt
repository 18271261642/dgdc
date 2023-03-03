package com.jkcq.base.base

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.jkcq.base.R
import com.jkcq.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_base_title_layout.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/30
 */
abstract class BaseTitleActivity : BaseActivity() {


    override fun setCustomContentView() {
        var all_content =
            LayoutInflater.from(this).inflate(R.layout.activity_base_title_layout, null)

        var contentView = LayoutInflater.from(this).inflate(getLayoutResId(), null)
        var real_content = all_content.findViewById<View>(R.id.fl_real_content) as FrameLayout
//        val params = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            real_content.getLayoutParams().height
//        )
        real_content.removeAllViews()
        real_content.addView(contentView)
        //        fl_real_content.addView(contentView,params)
        setContentView(all_content)
    }

    override fun initHeander() {
        iv_title_back.setOnClickListener { finish() }
        iv_right.setOnClickListener {
            ivRight()
        }
    }

    abstract fun ivRight()

    override fun setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, rl_title)
        StatusBarUtil.setLightMode(this)
    }

    fun setTitleText(title: String) {
        tv_title.text = title
    }

    fun setTitleText(titleResId: Int) {
        setTitle(getString(titleResId))
    }

    fun setIvRightIcon(iconRes: Int) {
        iv_right.visibility = View.VISIBLE
        iv_right.setImageResource(iconRes)
    }
    fun setHideRightIcon() {
        iv_right.visibility = View.INVISIBLE
    }
}