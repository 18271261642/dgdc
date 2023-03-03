package com.jkcq.base.view;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jkcq.base.observable.ProgressObservable;

public class LoadingupdateAnimatorView extends View {
    float radius;

    Context mContext;


    // ColorInt
    private int startColor;
    // ColorInt
    private int endColor;

    public LoadingupdateAnimatorView(Context context) {

        super(context);
        mContext = context;
        initPaint();
    }

    public LoadingupdateAnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public LoadingupdateAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initPaint();
    }

    private void initPaint() {


    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radius = (getWidth() / 2);
    }

    public float progresss = 0;

    public float getProgress() {
        return progresss;
    }

    public void setProgress(float progress) {

        this.progresss = progress;
        ProgressObservable.getInstance().updateProgress(progress);
        // invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    public void setCurrentType(int crrentType) {
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
}
