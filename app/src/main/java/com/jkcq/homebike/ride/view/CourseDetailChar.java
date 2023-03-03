package com.jkcq.homebike.ride.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean;
import com.jkcq.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CourseDetailChar extends View {

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
    private int mxLineAxis;
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

    public CourseDetailChar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseDetailChar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    private void init() {


        mPaintValue = new Paint();
        mPaintValue.setAntiAlias(true);
//        mPaintValue.setStyle(Paint.Style.FILL);
        mPaintValue.setColor(Color.parseColor("#1DCE74"));
/*
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
//        mPaintValue.setStyle(Paint.Style.FILL);
        mPaintText.setColor(mContext.getResources().getColor(R.color.white));
        //  mPaintText.setTypeface(Typeface.DEFAULT_BOLD);
        mPaintText.setTextSize(DisplayUtils.dip2px(mContext, 12));*/

        mPaintBgValue = new Paint();
        mPaintBgValue.setAntiAlias(true);
        mPaintBgValue.setStrokeWidth(DateUtil.dip2px(1f));
        mPaintBgValue.setStyle(Paint.Style.STROKE);
        mPaintBgValue.setColor(Color.parseColor("#1DCE74"));

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setColor(Color.parseColor("#6E6E77"));
        mPaintLine.setTextSize(DateUtil.dip2px(10));
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
        mYLineAxis = mViewHeight - DateUtil.dip2px(40);
        mxLineAxis = DateUtil.dip2px(40f);
      /*  Log.e("test", "mViewWidth=" + mViewWidth + " mViewHeight=" + mViewHeight + " mYLineAxis=" + mYLineAxis
                + " mLeftMargin=" + mLeftMargin + " mValueWidth=" + mValueWidth + " mItemMargin=" + mItemMargin);*/
    }

    RectF rectF = new RectF();
    int sum = 100;
    LinearGradient lg;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (list == null || list.size() == 0) {
            return;
        }
        mValueWidth = ((getWidth() - 2 * mxLineAxis) * 1f / sum);
        mLeftMargin = mxLineAxis;


        points.clear();
        //canvas.drawLine(0, mYLineAxis, mViewWidth, mYLineAxis, mPaintLine);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            // Integer value = list.get(size - 1 - i).getValue();
            Log.e("setdata", "setdata" + list.get(i));
            Integer value = list.get(i).getmResistances();
            //画圆柱
            int left = (int) (mLeftMargin + mValueWidth * list.get(i).getmIntervalStart());
            int right = (int) (mLeftMargin + mValueWidth * list.get(i).getmIntervalEnd());


            int bottom = mYLineAxis;
            int top = mYLineAxis - ((mYLineAxis - mTopMargin) * value / max);
            rectF.set(left, top, right, bottom);
            //需要变色
            lg = new LinearGradient((rectF.right - rectF.left) / 2, 0, (rectF.right - rectF.left) / 2, rectF.bottom, Color.parseColor("#1DCE74"), Color.parseColor("#FFFFFF"), Shader.TileMode.MIRROR);
            mPaintValue.setShader(lg);
            canvas.drawRect(rectF, mPaintLine);
            canvas.drawRect(rectF, mPaintValue);
            points.add(new Point((int) (rectF.left), (int) (rectF.top)));
            points.add(new Point((int) (rectF.right), (int) (rectF.top)));
            Log.e("setdata", "rectF.left=" + rectF.left + "rectF.top=" + rectF.top + ",rectF.right=" + rectF.right + ",rectF.top=" + rectF.top);

            //canvas.drawCircle(left + radius, top, radius, mPaintValue);


        }
        DrawLine(canvas);
        drawx(canvas);
        drawY(canvas);
        //  drawAllUserIcon(canvas, mPath);
    }

    /**
     * 根据值绘制折线
     */
    Path mPath = new Path();


    public void setcurrentDis(float currentDis) {
        if (this.currentDis == currentDis) {
            return;
        }
        this.currentDis = currentDis;
        invalidate();
    }

    public void DrawLine(Canvas canvas) {
        mPath.reset();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (i == 0) {
                mPath.moveTo(point.x, point.y);
            } else {
                mPath.lineTo(point.x, point.y);
            }
            // canvas.drawText(value[i] + "", point.x, point.y - radius, mBrokenLineTextPaint);
        }
        //lg = new LinearGradient((rectF.right - rectF.left) / 2, 0, (rectF.right - rectF.left) / 2, mViewHeight / 2, Color.parseColor("#1DCE74"), Color.parseColor("#FFFFFF"), Shader.TileMode.MIRROR);
        // mPaintBgValue.setShader(lg);
        canvas.drawPath(mPath, mPaintBgValue);
    }


    /**
     * 當前的数据在那个阻力区间
     * <p>
     * mPath  需追踪的路径
     */


    public int getmTotalDistans() {

        return sum;
    }

    public void setdata(List<ResistanceIntervalBean> beans, int sum) {


        Log.e("setdata", "setdata" + beans);

        this.sum = sum;
        this.currentType = currentType;
        this.list = new ArrayList<>();
        this.list.addAll(beans);
        strmDatas = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            strmDatas.add(list.get(i).getmResistances());
        }
        calMax(currentType);
        invalidate();
    }

    public void drawx(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mPaintLine.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        for (int i = 0; i < yList.size(); i++) {
            int y = (int) (mYLineAxis - ((mYLineAxis - mTopMargin) * yList.get(i) / max - distance));
            int x = (int) (mxLineAxis - mPaintLine.measureText("0" + yList.get(i)));
            canvas.drawText("" + yList.get(i), x, y, mPaintLine);

        }
        canvas.drawLine(mxLineAxis, mYLineAxis, mxLineAxis, 0, mPaintLine);
    }

    public void drawY(Canvas canvas) {
        float xWith = (points.get(points.size() - 1).x - mxLineAxis) / 4.f;
        for (int i = 0; i < xList.size(); i++) {
            int y = mYLineAxis + DateUtil.dip2px(10);
            int x = (int) ((int) (xWith * i + mxLineAxis) - mPaintLine.measureText(xList.get(i)) / 2);
            canvas.drawText("" + xList.get(i), x, y, mPaintLine);

        }
        canvas.drawLine(mxLineAxis, mYLineAxis, points.get(points.size() - 1).x, mYLineAxis, mPaintLine);
    }

    ArrayList<Integer> yList = new ArrayList<>();
    ArrayList<String> xList = new ArrayList<>();

    public void calMax(int type) {

        yList.clear();
        int tempMax = Collections.max(strmDatas);
        if (tempMax % 15 != 0) {
            max = tempMax + (15 - tempMax % 15);
        } else {
            max = tempMax;
        }

        int value = max / 3;
        yList.add(0);
        for (int i = 0; i < 4; i++) {
            yList.add(i * value);
        }


        // max = max + 10;

        xList.clear();
        value = sum / 4;
        xList.add("0");
        DateUtil.HMS hms;
        for (int i = 1; i < 5; i++) {
            if (i == 4) {
                hms = DateUtil.getHMSFromMillis(sum * 1000);
            } else {
                hms = DateUtil.getHMSFromMillis(i * value * 1000);
            }
            xList.add(String.format(
                    "%02d:%02d",
                    hms.getMinute(),
                    hms.getSecond()
            ));
        }
    }


}
