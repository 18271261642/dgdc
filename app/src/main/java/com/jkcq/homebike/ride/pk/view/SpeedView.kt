package cn.xmliu.speedview

/**
 * Date: 2020/6/4 17:30
 * Email: diyangxia@163.com
 * Author: liuxunming
 * Description: 码表
 */

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.jkcq.homebike.R
import com.jkcq.util.DateUtil
import kotlin.math.cos
import kotlin.math.sin


class SpeedView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * 当前速度，单位km/h
     */
    private var curSpeed: Int = -1

    /**
     * 最大速度，建议值为60、120、240等
     */
    private var maxSpeed: Int = 0

    /**
     * 背景色：默认黑色
     */
    private var bgColor = 0

    /**
     * 刻度颜色：默认白色
     */
    private var lineColor = 0

    /**
     * 刻度颜色：默认白色
     */
    private var textColor = 0

    /**
     * 指针颜色：默认红色
     */
    private var pointColor = 0

    /**
     * 画笔
     */
    private val paint = Paint()


    /**
     *
     */
    private val bgPaint = Paint()

    /**
     * 画外层圆弧，路径和椭圆
     */
    private var rectF: RectF? = null
    private var bgrectF: RectF? = null
    private var arcPath: Path? = null

    /**
     * 中心点坐标、半径
     */
    private var centerX: Float? = null
    private var centerY: Float? = null
    private var radius: Float? = null

    /**
     * 码表外层RectF
     */
    private var left = -1F
    private var top = -1F
    private var right = -1F
    private var bottom = -1F

    /**
     * 偏移量
     */
    private val pointerOffset = 20
    private val unitOffset = 140
    private val numberOffset = 70
    private val smallOffset = 25
    private val bigOffset = 40

    /**
     * 码表表盘数组
     */
    private val bigMarkArr: MutableList<Double> = mutableListOf()
    private val all: MutableList<Double> = mutableListOf()
    private val smallMarkArr: MutableList<Double> = mutableListOf()
    private val numberArr: MutableList<String> = mutableListOf()


    init {
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.SpeedView)
        bgColor = ta.getColor(
            R.styleable.SpeedView_bgColor,
            ContextCompat.getColor(context, R.color.black)
        );
        lineColor = ta.getColor(
            R.styleable.SpeedView_lineColor,
            ContextCompat.getColor(context, R.color.white)
        );
        textColor = ta.getColor(
            R.styleable.SpeedView_textColor,
            ContextCompat.getColor(context, R.color.white)
        );
        pointColor = ta.getColor(
            R.styleable.SpeedView_pointColor,
            ContextCompat.getColor(context, R.color.common_red)
        );
        maxSpeed = ta.getInteger(R.styleable.SpeedView_maxSpeed, 200);
        ta.recycle()


        all.clear()
        var i = 135f
        var j = 0
        while (i <= 405) {
            all.add(i.toDouble())
            numberArr.add(j.toString())
            j += 10
            i += 13.5f
        }


        // 大刻度数组
        for (i in 180..360 step 45)
            bigMarkArr.add(i.toDouble())
        // 小刻度数组
        for (i in 150..390 step 15)
            smallMarkArr.add(i.toDouble())

    }

    private var arcWidth = 200 //弧形的宽度，通过paint的setStrokeWidth来设置

    private var w_r = 300
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.icon_rpm_point)
        centerX = width / 2.toFloat()
        //  centerY = height * 5 / 6.toFloat()
        centerY = height / 2.toFloat()
        //  radius = height * 3 / 4.toFloat()
        radius = (height / 2) - DateUtil.dip2px(5f).toFloat()

        left = centerX!! - radius!!
        top = centerY!! - radius!!
        right = centerX!! + radius!!
        bottom = centerY!! + radius!!

        rectF = RectF(left, top, right, bottom)

        arcWidth = h / 2 //圆弧宽带


        w_r = h - arcWidth - 20 //外圈半径 = 高度 - 圆弧宽度 - 20（去掉一点，一面刚好顶大view的边上）

        arcPath = Path()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(bgColor)

        drawHalf(canvas)
        drawCenter(canvas)
        drawBig(canvas)
        drawSmall(canvas)
        drawNumber(canvas)
        drawUnit(canvas)
        drawPointer(canvas)
    }

    /**
     * 画外层半圆
     */
    private fun drawHalf(canvas: Canvas?) {
        paint.isAntiAlias = true
        paint.color = lineColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F
        arcPath!!.rewind() // 清除直线数据，保留数据结构，方便快速重用
        arcPath!!.arcTo(rectF, 120F, 300F)// 多截取一点弧会好看点
        canvas?.drawPath(arcPath!!, paint)



        rectF = RectF(left, top, right, bottom)
        bgPaint.isAntiAlias = true
        paint.color = ContextCompat.getColor(context, R.color.black_50)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = (width / 8).toFloat()

        bgrectF = RectF(
            left + width / 16,
            top + width / 16,
            right - width / 16,
            bottom - width / 16
        )
        arcPath!!.rewind() // 清除直线数据，保留数据结构，方便快速重用
        arcPath!!.arcTo(bgrectF, 120F, 300F)// 多截取一点弧会好看点
        canvas?.drawPath(arcPath!!, paint)
    }

    /**
     * 画圆点
     */
    private fun drawCenter(canvas: Canvas?) {
        val centerRadius = DateUtil.dip2px(10f).toFloat()
        paint.color = lineColor
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(centerX!!, centerY!!, centerRadius, paint)
    }

    /**
     * 画大刻度
     */
    private fun drawBig(canvas: Canvas?) {
        paint.color = lineColor
        paint.strokeWidth = 10F
        paint.style = Paint.Style.FILL_AND_STROKE

        for (i in 0..all.size - 1 step 1) {

            if ((i) % 5 == 0) {
                val radian = toRadians(all.get(i)) // 角度转弧度
                val firstX = getRoundX(radian).toFloat()
                val firstY = getRoundY(radian).toFloat()
                val secondX = (firstX - cos(radian) * bigOffset).toFloat()
                val secondY = (firstY - sin(radian) * bigOffset).toFloat()
                canvas?.drawLine(firstX, firstY, secondX, secondY, paint)
            } else {
                val radian = toRadians(all.get(i)) // 角度转弧度
                val firstX = getRoundX(radian).toFloat()
                val firstY = getRoundY(radian).toFloat()
                val secondX = (firstX - cos(radian) * smallOffset).toFloat()
                val secondY = (firstY - sin(radian) * smallOffset).toFloat()
                canvas?.drawLine(firstX, firstY, secondX, secondY, paint)
            }
        }

        /* for (item in all) {
             val radian = toRadians(item) // 角度转弧度
             val firstX = getRoundX(radian).toFloat()
             val firstY = getRoundY(radian).toFloat()
             val secondX = (firstX - cos(radian) * bigOffset).toFloat()
             val secondY = (firstY - sin(radian) * bigOffset).toFloat()
             canvas?.drawLine(firstX, firstY, secondX, secondY, paint)
         }*/
    }

    /**
     * 画小刻度
     */
    private fun drawSmall(canvas: Canvas?) {
        paint.color = lineColor
        paint.strokeWidth = 5F
        paint.style = Paint.Style.FILL_AND_STROKE
        /*for (item in smallMarkArr) {
            val radian = toRadians(item) // 角度转弧度
            val firstX = getRoundX(radian).toFloat()
            val firstY = getRoundY(radian).toFloat()
            val secondX = (firstX - cos(radian) * smallOffset).toFloat()
            val secondY = (firstY - sin(radian) * smallOffset).toFloat()
            canvas?.drawLine(firstX, firstY, secondX, secondY, paint)
        }*/
    }

    /**
     * 画数字
     */
    private fun drawNumber(canvas: Canvas?) {
        // 速度数字数组 12组 需要和刻度数组数量相等 不应该把变量值放在初始化方法中
        /* numberArr.clear()
         for (i in 0..maxSpeed step 5)
             numberArr.add(i.toString())*/

        paint.color = textColor
        paint.textSize = 25F
        paint.strokeWidth = 5F
        paint.textSkewX = 0F // 倾斜度设置为0，就是非斜体
        paint.style = Paint.Style.FILL


        for (index in 0..numberArr.size - 1 step 1) {

            if ((index) % 5 == 0) {
                /*val radian = toRadians(numberArr.get(i).toDouble()) // 角度转弧度
                val firstX = getRoundX(radian).toFloat()
                val firstY = getRoundY(radian).toFloat()
                val secondX = (firstX - cos(radian) * bigOffset).toFloat()
                val secondY = (firstY - sin(radian) * bigOffset).toFloat()
                canvas?.drawLine(firstX, firstY, secondX, secondY, paint)*/


                val radian = toRadians(all.get(index).toDouble()) // 角度转弧度
                val firstX = getRoundX(radian)
                val firstY = getRoundY(radian)
                val secondX = (firstX - cos(radian) * numberOffset - index / 5 * 3).toFloat()//距离微调
                val secondY = (firstY - sin(radian) * numberOffset + index / 5).toFloat()//距离微调
                canvas?.drawText(numberArr[index], secondX, secondY, paint)
                Log.e("number", "number=${numberArr[index]}" + "all=${all[index]}")
            } else {
                /* val radian = toRadians(all.get(i)) // 角度转弧度
                 val firstX = getRoundX(radian).toFloat()
                 val firstY = getRoundY(radian).toFloat()
                 val secondX = (firstX - cos(radian) * smallOffset).toFloat()
                 val secondY = (firstY - sin(radian) * smallOffset).toFloat()
                 canvas?.drawLine(firstX, firstY, secondX, secondY, paint)*/
            }
        }

        /* for (index in numberArr.indices) {
               val radian = toRadians(bigMarkArr[index]) // 角度转弧度
               val firstX = getRoundX(radian)
               val firstY = getRoundY(radian)
               val secondX = (firstX - cos(radian) * numberOffset - index * 3).toFloat()//距离微调
               val secondY = (firstY - sin(radian) * numberOffset + index).toFloat()//距离微调
               canvas?.drawText(numberArr[index], secondX, secondY, paint)
               println("number=${numberArr[index]}")
           }*/
    }

    /**
     * 画单位 km/h
     */
    private fun drawUnit(canvas: Canvas?) {
        paint.textSize = 20F
        paint.textSkewX = -0.25F // 斜体
        val one = toRadians(270.0) // 角度转弧度 最顶端的圆点
        val testX = getRoundX(one)
        val testY = getRoundY(one)
        val finalX = (testX - cos(one) * unitOffset - 27).toFloat()//距离微调
        val finalY = (testY - sin(one) * unitOffset + 9).toFloat()//距离微调
        // canvas?.drawText("km/h", finalX, finalY, paint)
    }


    private val mSrcRect = Rect()
    private val mDestRect = Rect()

    /**
     * 画指针
     */
    private var mBitmap: Bitmap? = null


    /**
     * 画指针
     *
     * @param canvas
     * @param rf1
     */


    /**
     * 画指针
     *
     * @param canvas
     * @param rf1
     */
    private var p_pointer //画指针的画笔
            : Paint? = null


    private fun drawPointer(canvas: Canvas?) {

        curSpeed = 200
        paint.color = pointColor
        paint.strokeWidth = 8F
        // (180 + 3 * curSpeed)的意义在于把速度转换为角度
        val value: Double = 270.0 / maxSpeed
        val pointerDegree = 135 + value * curSpeed
        val radian = toRadians(pointerDegree)
        val firstX = getRoundX(radian)
        val firstY = getRoundY(radian)
        val secondX = (firstX - cos(radian) * pointerOffset).toFloat()
        val secondY = (firstY - sin(radian) * pointerOffset).toFloat()


        /* if (radian > 90) { //某个值对应的角度大于90度的时候，计算对应的x与y的所对应的三角函数不同，所以以90度作为界点分别计算
             //某个值对应的角度大于90度的时候，计算对应的x与y的所对应的三角函数不同，所以以90度作为界点分别计算
             val md: Double = 180 - radian
             x = (centerX!! + (w_r + arcWidth) * Math.cos(Math.PI * md / 180.0f)).toFloat()
             y = (centerY!! - (w_r + arcWidth) * Math.sin(Math.PI * md / 180.0f)).toFloat()
         } else {
             x = (centerX!! - (w_r + arcWidth) * Math.cos(Math.PI * radian / 180.0f)).toFloat()
             y = (centerY!! - (w_r + arcWidth) * Math.sin(Math.PI * radian / 180.0f)).toFloat()
         }


         val x = 0.0f //某个进度值对应的外圆弧上的点的x坐标

         val y = 0.0f //某个进度值对应的外圆弧上的点的y坐标

        */
        /*val path = Path()
        path.moveTo(centerX!!, centerY!!)
        //path.lineTo(firstX.toFloat(), firstY.toFloat())
        path.lineTo(secondX.toFloat(), secondY.toFloat())
        val p = Paint()
        p.color = lineColor
        p.isAntiAlias = true
        p.strokeWidth = 12f
        p.style = Paint.Style.FILL
        path.close()
        canvas!!.drawPath(path, p)
        Log.e("curSpeed", "curSpeed=" + curSpeed)*/
        // mSrcRect.set(centerX!!,centerY!!,secondX,secondY)

        /*canvas?.drawBitmap(
            mBitmap!!,
            secondX,
            centerY!! - mBitmap!!.height,
            paint
        )*/
        canvas?.drawLine(centerX!!, centerY!!, secondX, secondY, paint)
    }

    /**
     * 计算在圆上某个角度的点的x坐标
     */
    private fun getRoundX(radian: Double): Double {
        return centerX!! + cos(radian) * radius!!
    }

    /**
     * 计算在圆上某个角度的点的y坐标
     */
    private fun getRoundY(radian: Double): Double {
        return centerY!! + sin(radian) * radius!!
    }

    /**
     * 设置当前速度
     */
    fun setCurSpeed(speed: Int) {
        curSpeed = speed
        invalidate()
    }

    /**
     * 设置最大速度
     */
    fun setMaxSpeed(speed: Int) {
        maxSpeed = speed
        invalidate()
    }

    /**
     * 设置线条颜色
     */
    fun setLineColor(color: Int) {
        lineColor = color
        invalidate()
    }

    /**
     * 设置文字颜色
     */
    fun setTextColor(color: Int) {
        textColor = color
        invalidate()
    }

    /**
     * 设置指针颜色
     */
    fun setPointerColor(color: Int) {
        pointColor = color
        invalidate()
    }

    /**
     * 设置背景颜色
     */
    fun setBgColor(color: Int) {
        bgColor = color
        invalidate()
    }

    /**
     *  类似Math.toRadians 角度转弧度
     */
    private fun toRadians(degree: Double): Double {
        return degree / 180.0 * Math.PI
    }
}