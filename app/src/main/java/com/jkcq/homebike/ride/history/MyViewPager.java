package com.jkcq.homebike.ride.history;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }*/

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
