package com.jkcq.viewlibrary

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_sport_value_item.view.*

/*
 * 通用定时器控件封装/针对短信发送
 * classes : com.topoto.project.view
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016/10/9 8:52
 */
class ItemSportShareView : LinearLayout {
    /**
     * 标题
     */
    private var titleText: String? = null
    private var valueText: String? = null
    private var unitlText: String? = null
    private var iconDrawbl: Drawable? = null


    private var titleTextColor: Int? = null
    private var valueTextColor: Int? = null
    private var unitlTextColor: Int? = null

    private var titleTextSize: Float? = null
    private var valueTextSize: Float? = null
    private var unitlTextSize: Float? = null


    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        initBase(context, attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initBase(context, attrs, defStyleAttr)
    }

    private fun initBase(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ItemSportView,
            defStyleAttr, 0
        )
        titleText = a.getString(R.styleable.ItemSportView_sport_title_value)
        unitlText = a.getString(R.styleable.ItemSportView_sport_unitl_value)
        iconDrawbl = a.getDrawable(R.styleable.ItemSportView_sport_type_icon)

        titleTextSize = a.getDimension(R.styleable.ItemSportView_sport_title_size, 12f)
        unitlTextSize = a.getDimension(R.styleable.ItemSportView_sport_unitl_size, 10f)
        valueTextSize = a.getDimension(R.styleable.ItemSportView_sport_unitl_size, 18f)

        valueTextColor = a.getColor(
            R.styleable.ItemSportView_sport_value_color,
            context.getResources().getColor(R.color.common_text_color)
        )
        unitlTextColor = a.getColor(
            R.styleable.ItemSportView_sport_unitl_color,
            context.getResources().getColor(R.color.common_text_color)
        )
        titleTextColor = a.getColor(
            R.styleable.ItemSportView_sport_unitl_color,
            context.getResources().getColor(R.color.common_text_color)
        )

        a.recycle()
        initView()
        setListener()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initData()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.view_sport_share_value_item, this, true)
    }

    private fun initData() {
        if (!TextUtils.isEmpty(titleText)) {

            tv_title.text = titleText
        }
        if (!TextUtils.isEmpty(unitlText)) {
            tv_unitl.text = unitlText
        }
        setRemindIcon()
    }


    fun setRemindIcon() {
        if (null == iconDrawbl) {
            return
        }
        // iv_icon.setCompoundDrawables(remindIcon, null, null, null);
        iv_view_bg.visibility = View.VISIBLE
        iv_view_bg.setImageDrawable(iconDrawbl)
    }

    private fun setListener() {}
    fun setValue(resId: Int) {
        if (resId ushr 24 < 2) {
            return
        }
        setValueText(context.getString(resId))
    }
    fun getUnitlTextView():TextView{
       return tv_unitl
    }

    fun setValueText(text: String?) {

        valueText = text
        tv_value.text = if (TextUtils.isEmpty(text)) "" else text
    }


    fun setBg(bg: Int) {
        if (layout_bg != null) {
            layout_bg.setBackgroundResource(bg)
        }
    }


}