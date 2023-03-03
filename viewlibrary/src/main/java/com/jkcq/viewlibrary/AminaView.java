package com.jkcq.viewlibrary;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;

public class AminaView extends androidx.appcompat.widget.AppCompatImageView {


    ArrayList<Integer> imgList=new ArrayList<>();

    public AminaView(Context context) {
        super(context);
    }

    public AminaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AminaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
