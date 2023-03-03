package com.jkcq.homebike.ride.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jkcq.homebike.R;
import com.jkcq.homebike.ride.pk.bean.LineUser;
import com.jkcq.homebike.ride.sceneriding.bean.ResistanceIntervalBean;
import com.jkcq.util.DateUtil;
import com.jkcq.util.UserBitmapManager;

import java.util.ArrayList;
import java.util.List;


public class PKResistanceBarChar extends View {

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


    private List<LineUser> currentDisList = new ArrayList<>();

    private float currentDis = 0f;


    private List<Point> points = new ArrayList<>();
    private List<Point> currentpoints = new ArrayList<>();
    private List<String> currentuserIds = new ArrayList<>();

    public PKResistanceBarChar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PKResistanceBarChar(Context context, AttributeSet attrs, int defStyleAttr) {
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

    Bitmap EndBitmap;
    int endBitmapY;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = getWidth();
        mViewHeight = getHeight();

        mLeftMargin = DateUtil.dip2px(10);
        mRightMargin = mLeftMargin;
        mItemMargin = 0;
        mTopMargin = DateUtil.dip2px(30);
        mYLineAxis = mViewHeight;
        EndBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_pk_end_pic);
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

        //mValueWidth = DateUtil.dip2px(6f);
        if (sum < 1000) {
            mValueWidth = ((getWidth() - mLeftMargin * 3) * 1f / sum);
        } else {
            mValueWidth = ((getWidth() - mLeftMargin * 3) * 1f / 1000);
        }

        points.clear();
        //canvas.drawLine(0, mYLineAxis, mViewWidth, mYLineAxis, mPaintLine);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            // Integer value = list.get(size - 1 - i).getValue();
            //Log.e("setdata", "setdata" + list.get(i));
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
            //  Log.e("setdata", "rectF.left=" + rectF.left + "rectF.top=" + rectF.top + ",rectF.right=" + rectF.right + ",rectF.top=" + rectF.top);
        }
        currentpoints.clear();
        currentuserIds.clear();
        Log.e("currentDisList", "sum=" + sum + "currentDisList-----------=" + currentDisList);
        int tempDis = 0;
        for (int j = 0; j < currentDisList.size(); j++) {
            boolean isStart = true;
            for (int i = 0; i < size; i++) {
                if (isStart) {
                    tempDis = currentDisList.get(j).getDis();
                    if (tempDis >= sum) {
                        tempDis = sum;
                    }
                    Integer value = list.get(i).getmResistances();
                    int top = mYLineAxis - ((mYLineAxis - mTopMargin) * value / max);
                    //画圆柱
                    if (tempDis >= list.get(i).getmIntervalStart() && tempDis <= list.get(i).getmIntervalEnd()) {
                        int right = (int) (mLeftMargin + mValueWidth * tempDis);
                        isStart = false;
                        currentpoints.add(new Point(right, top));
                        currentuserIds.add(currentDisList.get(j).getUserId());
                    }
                }
            }
        }
        // Log.e("currentDisList", "currentDisList=" + currentDisList);
        // Log.e("currentDisList", "list=" + list);
        // Log.e("currentDisList", "currentpoints=" + currentpoints);

        //需要显示最后段数据才显示结束的旗帜
        if ((currentDisList.size() > 0 && sum - currentDisList.get(currentDisList.size() - 1).getDis() <= 500) || sum <= 1000) {
            Integer value = list.get(list.size() - 1).getmResistances();
            //画圆柱
            endBitmapY = mYLineAxis - ((mYLineAxis - mTopMargin) * value / max) - EndBitmap.getHeight();
            int right = (int) (mLeftMargin + mValueWidth * list.get(list.size() - 1).getmIntervalEnd());
            //Log.e("currentDisList", "endBitmapY=" + endBitmapY + ",mYLineAxis=" + mYLineAxis + ",mYLineAxis - ((mYLineAxis - mTopMargin) * value / max)=" + (mYLineAxis - ((mYLineAxis - mTopMargin) * value / max)));
            canvas.drawBitmap(EndBitmap, right, endBitmapY, mPaintText);
        }


        Bitmap bitmap = null;
        for (int i = 0; i < currentpoints.size(); i++) {


            if (UserBitmapManager.bitmapHashMap.containsKey(currentuserIds.get(i))) {

                bitmap = UserBitmapManager.bitmapHashMap.get(currentuserIds.get(i));

                Log.e("bitmap", "bitmap=" + bitmap);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, currentpoints.get(i).x - bitmap.getWidth() / 2, currentpoints.get(i).y - bitmap.getHeight(), mPaintText);
                }
            } else {
                if (UserBitmapManager.bitmapLoactionHashMap.containsKey(currentuserIds.get(i))) {
                    bitmap = UserBitmapManager.bitmapLoactionHashMap.get(currentuserIds.get(i));

                    Log.e("bitmap", "bitmap=" + bitmap);
                    if (bitmap != null) {
                        canvas.drawBitmap(bitmap, currentpoints.get(i).x - bitmap.getWidth() / 2, currentpoints.get(i).y - bitmap.getHeight(), mPaintText);

                    }
                }
            }


            // drawAllUserIcon(canvas, mPath);
        }
    }


    private void drawPoint(Canvas canvas) {

    }

    /**
     * 根据值绘制折线
     */
    Path mPath = new Path();

    private Handler handler = new Handler();


    public void setCurrentDisList(List<LineUser> list, float currentDis) {


        Log.e("setCurrentDisList", "" + list + "currentDis" + currentDis);

        currentDisList.clear();
        currentDisList.addAll(list);
        setcurrentDis(currentDis);

    }


    private void setcurrentDis(float currentDis) {
        /*if (this.currentDis == currentDis) {
            return;
        }*/
        this.currentDis = currentDis;
        //invalidate();

        if (currentDis <= 500 || sum - currentDis <= 500) {
            postInvalidate();
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    scrollTo((int) ((currentDis - 500) * mValueWidth), 0);
                    postInvalidate();
                }
            });
        }
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
        //  canvas.drawPath(mPath, mPaintBgValue);
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

    public int getmTotalDistans() {

        return sum;
    }

    public void setdata(List<ResistanceIntervalBean> beans, int sum) {


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
       /* if (max + 10 < 100) {
            max = max + 10;
        }*/
        Log.e("setdata", "setdata" + beans + "max=" + max);
        calMax(currentType);
        invalidate();
    }


    public void calMax(int type) {
        // max = 100;
    }


}
