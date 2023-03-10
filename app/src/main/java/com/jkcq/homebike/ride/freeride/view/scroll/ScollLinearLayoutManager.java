package com.jkcq.homebike.ride.freeride.view.scroll;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 更改RecyclerView滚动的速度
 */
public class ScollLinearLayoutManager extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 25f;  //修改可以改变数据,越大速度越慢
    private Context contxt;

    public ScollLinearLayoutManager(Context context) {
        super(context);
        this.contxt = context;
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {


        Log.e("smoothScrollToPosition", "smoothScrollToPosition");


        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        Log.e("smoothScrollToPosition", "computeScrollVectorForPosition");
                        return ScollLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    @Override
                    protected float calculateSpeedPerPixel
                            (DisplayMetrics displayMetrics) {

                        Log.e("calculateSpeedPerPixel", "calculateSpeedPerPixel");
                        return MILLISECONDS_PER_INCH / displayMetrics.density;
                        //返回滑动一个pixel需要多少毫秒
                    }

                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    //可以用来设置速度
    public void setSpeedSlow(float x) {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        //0.3f是自己估摸的一个值，可以根据不同需求自己修改
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.3f + (x);
    }

}
