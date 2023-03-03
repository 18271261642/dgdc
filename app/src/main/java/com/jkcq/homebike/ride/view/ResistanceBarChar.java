package com.jkcq.homebike.ride.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
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
import java.util.List;


public class ResistanceBarChar extends View {

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
    private List<Point> currentpoints = new ArrayList<>();

    public ResistanceBarChar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResistanceBarChar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mPaintBgValue.setStrokeWidth(2);
        mPaintBgValue.setStyle(Paint.Style.STROKE);
        mPaintBgValue.setColor(Color.parseColor("#1DCE74"));

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setColor(Color.parseColor("#2F2F33"));
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
    int sum = 100;
    LinearGradient lg;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (list == null || list.size() == 0) {
            return;
        }
        mValueWidth = ((getWidth() - DateUtil.dip2px(6f)) * 1f / sum);


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
            lg = new LinearGradient((rectF.right - rectF.left) / 2, rectF.top, (rectF.right - rectF.left) / 2, rectF.bottom, Color.parseColor("#80ffffff"), Color.parseColor("#1affffff"), Shader.TileMode.MIRROR);
            mPaintValue.setShader(lg);
            canvas.drawRect(rectF, mPaintLine);
            canvas.drawRect(rectF, mPaintValue);
            points.add(new Point((int) (rectF.left), (int) (rectF.top)));
            points.add(new Point((int) (rectF.right), (int) (rectF.top)));
            Log.e("setdata", "rectF.left=" + rectF.left + "rectF.top=" + rectF.top + ",rectF.right=" + rectF.right + ",rectF.top=" + rectF.top);
        }
        currentpoints.clear();
        boolean isStart = true;
        for (int i = 0; i < size; i++) {
            if (isStart) {
                // Integer value = list.get(size - 1 - i).getValue();
                Integer value = list.get(i).getmResistances();
                int bottom = mYLineAxis;
                int top = mYLineAxis - ((mYLineAxis - mTopMargin) * value / max);
                //画圆柱
                int left = (int) (mLeftMargin + mValueWidth * list.get(i).getmIntervalStart());
                int right = (int) (mLeftMargin + mValueWidth * list.get(i).getmIntervalEnd());
                if (currentDis >= list.get(i).getmIntervalStart() && currentDis < list.get(i).getmIntervalEnd()) {
                    left = (int) (mLeftMargin + mValueWidth * list.get(i).getmIntervalStart());
                    right = (int) (mLeftMargin + mValueWidth * currentDis);
                    isStart = false;
                }
                rectF.set(left, top, right, bottom);
                currentpoints.add(new Point((int) (rectF.left), (int) (rectF.top)));
                currentpoints.add(new Point((int) (rectF.right), (int) (rectF.top)));
                Log.e("setdata", "rectF.left=" + rectF.left + "rectF.top=" + rectF.top + ",rectF.right=" + rectF.right + ",rectF.top=" + rectF.top);

            }


        }
        DrawLine(canvas);

        mPaintText.setColor(Color.WHITE);
        canvas.drawCircle(currentpoints.get(currentpoints.size() - 1).x, currentpoints.get(currentpoints.size() - 1).y, DateUtil.dip2px(3), mPaintText);
        mPaintText.setColor(Color.parseColor("#1DCE74"));
        canvas.drawCircle(currentpoints.get(currentpoints.size() - 1).x, currentpoints.get(currentpoints.size() - 1).y, DateUtil.dip2px(2), mPaintText);
        // drawAllUserIcon(canvas, mPath);
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
        for (int i = 0; i < currentpoints.size(); i++) {
            Point point = currentpoints.get(i);
            if (i == 0) {
                mPath.moveTo(point.x, point.y);
            } else {
                mPath.lineTo(point.x, point.y);
            }
            // canvas.drawText(value[i] + "", point.x, point.y - radius, mBrokenLineTextPaint);
        }
        canvas.drawPath(mPath, mPaintBgValue);
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
            mPaintText.setColor(Color.WHITE);
            canvas.drawCircle(points.get(0).x + DateUtil.dip2px(3), points.get(0).y, DateUtil.dip2px(3), mPaintText);
            mPaintText.setColor(Color.parseColor("#1DCE74"));
            canvas.drawCircle(points.get(0).x + DateUtil.dip2px(3), points.get(0).y, DateUtil.dip2px(2), mPaintText);
            return;
        }

        double precent = (distancs) / getmTotalDistans();
        if (precent > 1) {
            //  distancs = distancs - getmTotalDistans() * (int) Math.floor(precent);
        }

        if (distancs >= getmTotalDistans()) {
            drawScale = 1;
        } else {
            drawScale = distancs / getmTotalDistans();
        }

        if (drawScale == 0) {
            mPaintText.setColor(Color.WHITE);
            canvas.drawCircle(points.get(0).x + DateUtil.dip2px(3), points.get(0).y, DateUtil.dip2px(3), mPaintText);
            mPaintText.setColor(Color.parseColor("#1DCE74"));
            canvas.drawCircle(points.get(0).x + DateUtil.dip2px(3), points.get(0).y, DateUtil.dip2px(2), mPaintBgValue);
        }
        if (drawScale != 1) {
            //未到终点，继续画
            mPathMeasure = new PathMeasure(mPath, false);
            float distance = mPathMeasure.getLength() * drawScale;

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
                mPaintText.setColor(Color.WHITE);
                canvas.drawCircle(pos[0], pos[1], DateUtil.dip2px(3), mPaintText);
                mPaintText.setColor(Color.parseColor("#1DCE74"));
                canvas.drawCircle(pos[0], pos[1], DateUtil.dip2px(2), mPaintText);
                canvas.drawPath(dst, mPaintBgValue);//绘制截取的片段
                //drawUserIcon(uBmp, bean, canvas, pos);
            }
        }


    }  //总里程

    private int mTotalDistans = 0;

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
        max = 0;
        for (int i = 0; i < this.list.size(); i++) {
            strmDatas.add(list.get(i).getmResistances());
            if (list.get(i).getmResistances() > max) {
                max = list.get(i).getmResistances();
            }
        }
        invalidate();
    }


    public void calMax(int type) {
        max = 100;
    }


}
