package com.jkcq.viewlibrary

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jkcq.util.ConvertUtil
import kotlinx.android.synthetic.main.view_bike_itemview.view.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/30
 */
class BikeItemUserView : LinearLayout {
    /**
     * 左边内容
     */
    private var mLeftText: String? = null
    private var mLeftTextColor = 0
    private var mLeftTextSize = 0f

    /**
     * 右边内容
     */
    private var mRightText: String? = null
    private var mRightTextColor = 0
    private var mRightTextSize = 0f


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
        mLeftText = a.getString(R.styleable.ItemView_itemLeftText)
        mLeftTextColor = a.getColor(
            R.styleable.ItemView_itemLeftTextColor,
            context.resources.getColor(R.color.common_text_color)
        )
        mLeftTextSize = a.getDimension(
            R.styleable.ItemView_itemLeftTextSize,
            context.resources.getDimension(R.dimen.sp16)
        )
        mRightText = a.getString(R.styleable.ItemView_itemRightText)
        mRightTextColor = a.getColor(
            R.styleable.ItemView_itemRightTextColor,
            context.resources.getColor(R.color.common_text_color)
        )
        mRightTextSize = a.getDimension(
            R.styleable.ItemView_itemRightTextSize,
            context.resources.getDimension(R.dimen.sp16)
        )
        a.recycle()
        initView()
    }

    private fun initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bike_itemview, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initData()
    }

    private fun initData() {
        tv_left.run {
            text = mLeftText
            setTextColor(mLeftTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize)
        }
        tv_right.run {
            text = mRightText
            setTextColor(mRightTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize)
        }

    }

    fun setLeftText(content: String) {
        mLeftText = content
        tv_left.text = content
    }

    fun setRightText(content: String) {
        mRightText = content
        tv_right.text = content
    }

    fun getLeftText(): String {
        return mLeftText ?: ""
    }

    fun getRightText(): String {
        return mRightText ?: ""
    }
}