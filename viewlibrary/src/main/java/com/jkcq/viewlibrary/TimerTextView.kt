package com.jkcq.viewlibrary

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class TimerTextView : AppCompatTextView {

    var mSecond = 60
    var mCurrentSecond = 0
    var mText: String = ""

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    fun init(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) {

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.Timer,
            defStyleAttr, 0
        )
        mText = text.toString()
        mSecond = a.getInteger(R.styleable.Timer_second, 60)
        a.recycle()
    }


    fun startTimer() {
        mCurrentSecond = mSecond
        isEnabled = false
        text = "${mCurrentSecond}s"
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 1000)
    }

    fun stopTimer() {
        handler.removeCallbacks(runnable)
        isEnabled = true
        text = mText
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {

            try {
                mCurrentSecond--
                if (mCurrentSecond <= 0) {
                    isEnabled = true
                    text = mText
                    return
                }
                text = "${mCurrentSecond}s"
                handler.postDelayed(this, 1000)
            } catch (e: Exception) {

            } finally {

            }

        }
    }
}