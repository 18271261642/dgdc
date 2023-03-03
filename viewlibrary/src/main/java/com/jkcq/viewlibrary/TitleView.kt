package com.jkcq.viewlibrary

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_head.view.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/30
 */
class TitleView : LinearLayout {
    private var titleText: String? = null
    private var leftIconDrawbl: Drawable? = null
    private var rightIconDrawbl: Drawable? = null
    private var calenderIconDrawbl: Drawable? = null


    private var titleTextColor: Int? = null

    private var titleTextSize: Float? = null

    var onTitleOnClick: OnTitleViewOnclick? = null

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
            attrs, R.styleable.TitleView,
            defStyleAttr, 0
        )
        titleText = a.getString(R.styleable.TitleView_titletextvalue)
        titleTextColor = a.getColor(
            R.styleable.TitleView_titletextColor,
            context.resources.getColor(R.color.common_text_color)
        )
        titleTextSize = a.getDimension(
            R.styleable.TitleView_titletextSize,
            context.resources.getDimension(R.dimen.sp18)
        )
        leftIconDrawbl = a.getDrawable(R.styleable.TitleView_titleLeftIcon)
        rightIconDrawbl = a.getDrawable(R.styleable.TitleView_titleRightIcon)
        calenderIconDrawbl = a.getDrawable(R.styleable.TitleView_calenderIcon)
        a.recycle()
        initView()
    }

    private fun initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_head, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initData()
        setListener()
    }

    private fun initData() {
        tv_title.run {
            text = titleText
            setTextColor(titleTextColor!!)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize!!)
        }
        iv_left.run {
            setImageDrawable(leftIconDrawbl)
        }
        iv_right.run {
            setImageDrawable(rightIconDrawbl)
        }
        iv_calender.run {
            setImageDrawable(calenderIconDrawbl)
        }

    }

    fun setShowCalender(boolean: Boolean) {
        if (boolean) {
            iv_calender.visibility = View.VISIBLE
        } else {
            iv_calender.visibility = View.GONE
        }

    }

    fun setTitleText(content: String) {
        tv_title.text = content
    }

    fun setTitleOnClick(titleViewOnclick: OnTitleViewOnclick) {
        onTitleOnClick = titleViewOnclick
    }

    private fun setListener() {
        iv_left.setOnClickListener {
            if (onTitleOnClick != null) {
                onTitleOnClick!!.onLeftOnclick()
            }
        }
        iv_right.setOnClickListener {
            if (onTitleOnClick != null) {
                onTitleOnClick!!.onRightOnclick()
            }
        }
        iv_calender.setOnClickListener {
            if (onTitleOnClick != null) {
                onTitleOnClick!!.onCalenderOnclick()
            }
        }
    }


    interface OnTitleViewOnclick {
        fun onLeftOnclick()
        fun onRightOnclick()
        fun onCalenderOnclick()
    }

}