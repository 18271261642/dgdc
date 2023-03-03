package com.jkcq.homebike.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.widget.NestedScrollView;

public class LockableNestedScrollView extends NestedScrollView {
    // by default is scrollable
    private boolean scrollable = true;

    public LockableNestedScrollView(Context context) {
        super(context);
    }

    public LockableNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable && super.onInterceptTouchEvent(ev);
    }

    public void setScrollingEnabled(boolean enabled) {
        scrollable = enabled;
    }
}

