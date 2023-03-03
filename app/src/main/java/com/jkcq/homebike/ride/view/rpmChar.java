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


public class rpmChar extends View {

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

    public rpmChar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public rpmChar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    private void init() {


        mPaintValue = new Paint();
        mPaintValue.setAntiAlias(true);
//        mPaintValue.setStyle(Paint.Style.FILL);
        mPaintValue.setStrokeWidth(2);
        mPaintValue.setStyle(Paint.Style.STROKE);
        mPaintValue.setColor(Color.parseColor("#1DCE74"));


        mPaintBgValue = new Paint();
        mPaintBgValue.setAntiAlias(true);
        mPaintBgValue.setStyle(Paint.Style.FILL);
        mPaintBgValue.setColor(Color.parseColor("#1DCE74"));

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setColor(Color.parseColor("#ffd8d8d8"));
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

        mLeftMargin = DateUtil.dip2px(0);
        mRightMargin = mLeftMargin;
        mItemMargin = 0;
        mTopMargin = DateUtil.dip2px(5);
        mYLineAxis = mViewHeight;
      /*  Log.e("test", "mViewWidth=" + mViewWidth + " mViewHeight=" + mViewHeight + " mYLineAxis=" + mYLineAxis
                + " mLeftMargin=" + mLeftMargin + " mValueWidth=" + mValueWidth + " mItemMargin=" + mItemMargin);*/
    }

    RectF rectF = new RectF();
    RectF rectLine = new RectF();

    float mviewWith;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        mviewWith = getHeight() * 1.0f / allSum;


        Log.e("onDraw", "allSum=" + allSum + ",sum=" + sum + ",currentDis=" + currentDis);

        rectF.set(mLeftMargin, getHeight() - allSum * mviewWith, getWidth() - mRightMargin, getHeight() - mLeftMargin);
        canvas.drawRoundRect(rectF, (rectF.bottom - rectF.top) / 2, (rectF.bottom - rectF.top) / 2, mPaintLine);


        rectF.set(mLeftMargin, getHeight() - currentDis * mviewWith, getWidth() - mRightMargin, getHeight() - mLeftMargin);
        canvas.drawRoundRect(rectF, (rectF.bottom - rectF.top) / 2, (rectF.bottom - rectF.top) / 2, mPaintBgValue);
        rectLine.set(rectF.left + DateUtil.dip2px(1), getHeight() - sum * mviewWith - DateUtil.dip2px(1f), rectF.right - DateUtil.dip2px(1), getHeight() - sum * mviewWith + DateUtil.dip2px(1f));

        canvas.drawRoundRect(rectLine, (rectLine.bottom - rectLine.top) / 2, (rectLine.bottom - rectLine.top) / 2, mPaintText);


    }

    /**
     * 根据值绘制折线
     */

    public void setTargetRpm(int sumDis) {
        this.sum = sumDis;

    }

    public void setcurrentRpm(float currentDis) {
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


    private int mTotalDistans = 0;

    private int sum = 100;
    private int allSum = 200;

    public int getmTotalDistans() {

        return sum;
    }


}
