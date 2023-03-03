package com.jkcq.homebike.ride.sceneriding.view;

/*
 *
 *
 * @author mhj
 * Create at 2018/9/4 15:42
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by john on 2017/7/13.
 */

public class CustomSuVideoView extends SurfaceView {
    public  float width1;
    public float height1;

    public CustomSuVideoView(Context context) {
        super(context);
    }

    public void setMeasure(float width,float height){
        this.width1 = width;
        this.height1 = height;
    }

    public CustomSuVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSuVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomSuVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

       /* int width = MeasureSpec.getSize(widthMeasureSpec);
        int hight = MeasureSpec.getSize(heightMeasureSpec);
        if (this.width1>0 ){
            width = (int) width1;
        }
        setMeasuredDimension(width, hight);*/
        int width;
        int height;
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        width = widthSpecSize;
        height = heightSpecSize;
        setMeasuredDimension(width, height);
    }
}
