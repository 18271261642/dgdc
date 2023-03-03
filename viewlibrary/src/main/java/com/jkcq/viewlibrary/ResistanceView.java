package com.jkcq.viewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.jkcq.viewlibrary.bean.ViewBarInfo;

import java.util.ArrayList;


public class ResistanceView extends View {

    /**
     * 柱子的矩形
     */
    private RectF mBarRect; //mBarRect

    ArrayList<ViewBarInfo> list = new ArrayList<>();
    private Paint barPaint;


    public ResistanceView(Context context) {
        this(context, null);
    }

    public ResistanceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResistanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        list.clear();
        list.add(new ViewBarInfo(9, 2));
        list.add(new ViewBarInfo(5, 1));
        list.add(new ViewBarInfo(7, 3));
        list.add(new ViewBarInfo(3, 4));
        list.add(new ViewBarInfo(8, 5));
        // list.add(new ViewBarInfo(30, 10));
        //  list.add(new ViewBarInfo(50, 20));

        int sumWith = 0;
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setStartIndex(sumWith);
            sumWith += list.get(i).getWidth();
        }
        mBarRect = new RectF(0, 0, 0, 0);
        barPaint = new Paint();


        barPaint.setColor(Color.parseColor("#FF0000"));
    }

    public ResistanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float height = getMeasuredHeight();
        float viewHeight = height / 10;
        float with = getMeasuredWidth();
        float viewWith = with / list.get(list.size() - 1).getStartIndex() + list.get(list.size() - 1).getWidth();

        for (int i = 0; i < list.size(); i++) {
            canvas.drawRect(mBarRect, barPaint);
            LinearGradient linearGradient = new LinearGradient(0, getMeasuredHeight() - list.get(i).getViewHeight() * viewHeight, 0, getMeasuredHeight(), Color.parseColor("#ff1dce74"), Color.parseColor("#1a1dce74"), Shader.TileMode.MIRROR);
            barPaint.setShader(linearGradient);
            Log.e("onDraw", "list()" + list.get(i) + "" + list.get(i).getWidth() * viewWith + "，list.get(i).getStartIndex() * viewWith=" + list.get(i).getStartIndex() * viewWith);
            barPaint.setStrokeWidth(list.get(i).getWidth() * viewWith);
            if (i != 0) {
              //  canvas.drawLine((list.get(i).getStartIndex() * viewWith) / 2 + (list.get(i).getWidth() * viewWith / 2), getMeasuredHeight() - list.get(i).getViewHeight() * viewHeight, (list.get(i).getStartIndex() * viewWith) / 2 + (list.get(i).getWidth() * viewWith / 2), getMeasuredHeight(), barPaint);
                canvas.drawLine((list.get(i).getStartIndex() * viewWith)  , getMeasuredHeight() - list.get(i).getViewHeight() * viewHeight, (list.get(i).getStartIndex() * viewWith)  , getMeasuredHeight(), barPaint);

            } else {

                canvas.drawLine(0, getMeasuredHeight() - list.get(i).getViewHeight() * viewHeight, 0, getMeasuredHeight(), barPaint);
            }
        }
    }
}
