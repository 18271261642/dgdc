package com.jkcq.homebike.ride.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean;
import com.jkcq.util.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class RecChar extends View {

    private Context mContext;
    private int mViewWidth;
    private int mViewHeight;
    private int mLeftMargin;
    private int mRightMargin;
    private int mTopMargin;
    //圆柱宽
    private float mValueWidth;
    //两个Itme的间距
    private int mItemMargin;
    //线的Y坐标
    private int mYLineAxis;
    //预留圆柱顶部高度
    private Paint mPaintValue;
    private Paint mPaintText;
    private Paint mPaintBgValue;
    private Paint mPaintLine;
    private List<Integer> strmDatas;
    private List<ResistanceIntervalBean> list;
    private int currentType;
    private int max = 100;

    private float currentDis = 0f;


    private List<Point> points = new ArrayList<>();

    public RecChar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecChar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    private void init() {


        mPaintValue = new Paint();
        mPaintValue.setAntiAlias(true);
//        mPaintValue.setStyle(Paint.Style.FILL);
        mPaintValue.setStrokeWidth(DateUtil.dip2px(4));
        mPaintValue.setStyle(Paint.Style.FILL);
        mPaintValue.setColor(Color.parseColor("#80000000"));

        mPaintBgValue = new Paint();
        mPaintBgValue.setAntiAlias(true);
        mPaintBgValue.setStrokeWidth(DateUtil.dip2px(2));
        mPaintBgValue.setStyle(Paint.Style.STROKE);
        mPaintBgValue.setColor(Color.parseColor("#1DCE74"));

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setColor(Color.parseColor("#80000000"));
        mPaintLine.setTextSize(DateUtil.dip2px(15));
        mPaintLine.setStrokeWidth(DateUtil.dip2px(1));
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setTextSize(DateUtil.dip2px(12));
        mPaintText.setTypeface(Typeface.DEFAULT_BOLD);
        mPaintText.setStrokeWidth(DateUtil.dip2px(1));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = getWidth();
        mViewHeight = getHeight();

        mLeftMargin = DateUtil.dip2px(3);
        mRightMargin = mLeftMargin;
        mItemMargin = 0;
        mTopMargin = DateUtil.dip2px(5);
        mYLineAxis = mViewHeight;
      /*  Log.e("test", "mViewWidth=" + mViewWidth + " mViewHeight=" + mViewHeight + " mYLineAxis=" + mYLineAxis
                + " mLeftMargin=" + mLeftMargin + " mValueWidth=" + mValueWidth + " mItemMargin=" + mItemMargin);*/
    }

    RectF rectF = new RectF();


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();

        mPath.moveTo(mLeftMargin, 0);
        mPath.quadTo(mLeftMargin, getHeight() - mLeftMargin, mLeftMargin + DateUtil.dip2px(20), getHeight() - mLeftMargin);//二阶贝塞尔曲线

        // mPath.lineTo(getWidth() - mRightMargin - DateUtil.dip2px(20), getHeight() - mLeftMargin);
        mPath.lineTo(getWidth() - mRightMargin - DateUtil.dip2px(20), getHeight() - mLeftMargin);
        mPath.quadTo(getWidth() - mRightMargin, getHeight() - mLeftMargin, getWidth() - mRightMargin, 0);

        // rectF.set(mLeftMargin, 0 + mLeftMargin, getWidth() - mRightMargin, getHeight() - mLeftMargin);
        //mPath.addRoundRect(rectF, (rectF.bottom - rectF.top) / 2, (rectF.bottom - rectF.top) / 2, Path.Direction.CW);
        canvas.drawPath(mPath, mPaintValue);
        // canvas.drawRoundRect(rectF, (rectF.bottom - rectF.top) / 2, (rectF.bottom - rectF.top) / 2, mPaintLine);
        drawAllUserIcon(canvas, mPath);
    }

    /**
     * 根据值绘制折线
     */
    Path mPath = new Path();

    public void setSumDis(int sumDis) {
        this.sum = sumDis;
    }

    public void setcurrentDis(float currentDis) {
        if (this.currentDis == currentDis) {
            return;
        }
        this.currentDis = currentDis;
        invalidate();
    }


    /**
     * 當前的数据在那个阻力区间
     * <p>
     * mPath  需追踪的路径
     */


    private float drawScale = 1f;
    /**
     * 计算后的x，y坐标
     */
    private PathMeasure mPathMeasure;
    Path dst = new Path();

    public void drawAllUserIcon(Canvas canvas, Path mPath) {
        //得出了當前的阻力 y值

        /**
         * 如果用户是空的就不要画点
         */
/*

            Bitmap uBmp = PkFitalentContance.currentBitmap.get(bean.userInfo.getPeopleId());
            // Bitmap uBmp = LoadImageUtil.getInstance().getBitmap(context,bean.userInfo.getHeadShotAddr());
            if (uBmp == null) {
                uBmp = getCirleBitmap(userDefaultBmp);
            } else {
                uBmp = getCirleBitmap(uBmp);
            }
*/

        //用户当前的距离


        /*       */

        float distancs = currentDis;

        if (distancs == 0) {
            // mPaintText.setColor(Color.WHITE);
            // canvas.drawCircle(DateUtil.dip2px(3), (rectF.bottom - rectF.top) / 2, DateUtil.dip2px(3), mPaintText);
            // mPaintText.setColor(Color.parseColor("#1DCE74"));
            //  canvas.drawCircle(DateUtil.dip2px(3), (rectF.bottom - rectF.top) / 2, DateUtil.dip2px(2), mPaintText);
            return;
        }

      /*  double precent = (distancs) / getmTotalDistans();
        if (precent > 1) {
            distancs = distancs - getmTotalDistans() * (int) Math.floor(precent);
        }*/

        if (distancs >= getmTotalDistans()) {
            distancs = getmTotalDistans();
        }
        drawScale = distancs / getmTotalDistans();
        if (drawScale == 0) {
            // mPaintText.setColor(Color.WHITE);
            //canvas.drawCircle(DateUtil.dip2px(3), (rectF.bottom - rectF.top) / 2, DateUtil.dip2px(3), mPaintText);
            //mPaintText.setColor(Color.parseColor("#1DCE74"));
            // canvas.drawCircle(DateUtil.dip2px(3), (rectF.bottom - rectF.top) / 2, DateUtil.dip2px(2), mPaintText);
        }

        Log.e("drawScale", "drawScale=" + drawScale + "distancs=" + distancs + "getmTotalDistans()=" + getmTotalDistans());
        //未到终点，继续画
        mPathMeasure = new PathMeasure(mPath, false);
        float distance = mPathMeasure.getLength() * drawScale;
        Log.e("drawScale", "drawScale=" + drawScale + "distance=" + distance + "mPathMeasure.getLength()=" + mPathMeasure.getLength());
        if (distance > mPathMeasure.getLength()) {
            distance = mPathMeasure.getLength();
        }
        dst.reset();
        dst.lineTo(0, 0);
        if (mPathMeasure.getSegment(0, distance, dst, true)) {
            //绘制线
            float[] pos = new float[2];
            mPathMeasure.getPosTan(distance, pos, null);
            //绘制阴影
            //绘制点
            //绘制用户头像
            // mPaintText.setColor(Color.WHITE);
            // canvas.drawCircle(pos[0], pos[1], DateUtil.dip2px(3), mPaintText);
            // mPaintText.setColor(Color.parseColor("#1DCE74"));
            // canvas.drawCircle(pos[0], pos[1], DateUtil.dip2px(2), mPaintText);
            canvas.drawPath(dst, mPaintBgValue);//绘制截取的片段
            //drawUserIcon(uBmp, bean, canvas, pos);
        }


    }  //总里程

    private int mTotalDistans = 0;

    private int sum = 0;

    public int getmTotalDistans() {

        return sum;
    }


}
