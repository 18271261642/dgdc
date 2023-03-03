package com.jkcq.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.jkcq.base.R;
import com.jkcq.base.observable.ProgressObservable;
import com.jkcq.util.DateUtil;

import java.util.Observable;
import java.util.Observer;

public class LoadingAnimatorView extends View implements Observer {
    float radius;

    public float progresss = 0;
    RectF arcRectF = new RectF();
    Context mContext;


    Paint circlepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint arcCirclepaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public LoadingAnimatorView(Context context) {

        super(context);
        mContext = context;
        initPaint();
    }

    public LoadingAnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public LoadingAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initPaint();
    }

    private void initPaint() {

        circlepaint.setStyle(Paint.Style.STROKE);
        circlepaint.setStrokeWidth(DateUtil.dip2px(5f));
        arcCirclepaint.setColor(mContext.getResources().getColor(R.color.black_50));


    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radius = (getWidth() / 2);
    }

    public void addObserver() {
        ProgressObservable.getInstance().addObserver(this);
    }

    public void deletObserVer() {
        ProgressObservable.getInstance().deleteObserver(this);
    }

    public float getProgress() {
        return progresss;
    }

    public void setProgress(float progress) {
        progresss = (int) progress;

        Log.e("progresss", "progresss=" + progresss);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        arcRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawArc(arcRectF, -90, progresss * 3.6f, true, arcCirclepaint);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ProgressObservable) {
            this.progresss = (float) arg;
            invalidate();
        }
    }
}

