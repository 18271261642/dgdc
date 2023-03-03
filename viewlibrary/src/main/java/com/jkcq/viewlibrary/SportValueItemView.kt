package com.jkcq.viewlibrary

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_sport_value.view.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/30
 */
class SportValueItemView : LinearLayout {
    /**
     * 左边内容
     */
    private var mTitleText: String? = null
    private var mValue: String? = null


    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ItemView,
            defStyleAttr, 0
        )
        mTitleText = a.getString(R.styleable.ItemView_itemLeftText)
        a.recycle()
        initView()
    }

    private fun initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_sport_value, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initData()
    }

    private fun initData() {
        tv_title.run {
            text = mTitleText
        }

    }

    fun setValue(content: String) {
        tv_value.text = content
    }

    fun setTitle(content: String) {
        tv_title.text = content
    }

    fun getUnitText(): TextView {
        return tv_title;
    }

}