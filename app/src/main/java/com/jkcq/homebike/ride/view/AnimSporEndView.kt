package com.jkcq.homebike.ride.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.jkcq.homebike.R
import kotlinx.android.synthetic.main.view_anim_end_layout.view.*

/**
 *  Created by BeyondWorlds
 *  on 2020/7/30
 */
class AnimSporEndView : LinearLayout {

    var isShowProgress: Boolean = false
    private var strStartText: String? = ""
    private var strEndText: String? = ""
    private var strEndTipsText: String? = ""
    private var progressColor = 0
    var animator: ObjectAnimator? = null


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
        isShowProgress = a.getBoolean(R.styleable.AnimSporEndView_AnimSporisShowProgress, false)
        strStartText = a.getString(R.styleable.AnimSporEndView_AnimSporShowStarttext)
        strEndText = a.getString(R.styleable.AnimSporEndView_AnimSporShowEndText)
        strEndTipsText = a.getString(R.styleable.AnimSporEndView_AnimSporShowEndTextTip)
        progressColor = a.getColor(
            R.styleable.AnimSporEndView_progressColor,
            context.resources.getColor(R.color.common_text_color)
        )
        a.recycle()
        initView()
    }

    private fun initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_anim_end_layout, this, true)


    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initData()
    }

    var isUP = true;
    var mCurrentProgress = 0;
    private fun initData() {

        sport_stop?.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isUP = false
                    mCurrentProgress = 0
                    startAnimotion()
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    isUP = true
                    if (animator != null) {
                        animator!!.cancel()
                    }
                }
                else -> {
                }
            }
            false
        })


    }


    private fun startAnimotion() {
        /**
         * 不同的类型 开始值和目标值的设置
         * 单位的设置
         *
         */
        //progreesVaule=5000;
        if (sport_stop != null) {
            //  sportStop.setProgress(0);
            animator = ObjectAnimator.ofFloat(sport_stop, "progress", 0f, 100f)
            animator!!.setDuration(3000)
            animator!!.setInterpolator(LinearInterpolator())
            animator!!.start()
            animator!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    sport_stop.setProgress(0f)
                    animator = null
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    sport_stop.setProgress(0f)

                    if (isUP) {

                    } else {
                        animator = null
                        if (onSportEndViewOnclick != null) {
                            onSportEndViewOnclick?.onProgressCompetly()
                        }
                        //	2 为开始   1 为停止并保存数据
                        //

                        /*if (bean != null) {
                            onIsEndChall()
                        } else {
                            sendEnd()
                        }*/
                    }
                }

                override fun onAnimationRepeat(animation: Animator) {
                    super.onAnimationRepeat(animation)
                }

                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                }

                override fun onAnimationPause(animation: Animator) {
                    super.onAnimationPause(animation)
                }

                override fun onAnimationResume(animation: Animator) {
                    super.onAnimationResume(animation)
                }
            })
        }
    }

    var onSportEndViewOnclick: OnSportEndViewOnclick? = null
    fun setonSportEndViewOnclick(onSportEndViewOnclick: OnSportEndViewOnclick) {
        this.onSportEndViewOnclick = onSportEndViewOnclick
    }

    interface OnSportEndViewOnclick {
        fun onStartButton()
        fun onProgressCompetly()
    }


}