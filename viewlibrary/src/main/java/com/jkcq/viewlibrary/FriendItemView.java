package com.jkcq.viewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/*
 * 通用Item控件封装 包含提示点/复选框等
 * classes : view
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016/10/9 8:52
 */
public class FriendItemView extends LinearLayout {

    TextView tvValue, tvTitle;
    String valueStr, titleStr;
    Integer valueColor, titleColor;
    float valueSize, titleSize;

    public FriendItemView(Context context) {
        this(context, null);
    }

    public FriendItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FriendItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initBase(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FriendItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initBase(context, attrs, defStyleAttr);
    }

    private void initBase(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FriendItemView,
                defStyleAttr, 0);
        valueStr = a.getString(R.styleable.FriendItemView_friendItemviewValueStr);
        titleStr = a.getString(R.styleable.FriendItemView_friendItemviewTitleStr);

        valueColor = a.getColor(R.styleable.FriendItemView_friendItemviewValueColor, context.getResources().getColor(R.color.white));
        titleColor = a.getColor(R.styleable.FriendItemView_friendItemviewTitleColor, context.getResources().getColor(R.color.white));

        valueSize = a.getDimension(R.styleable.FriendItemView_friendItemviewValueSize, 15);
        titleSize = a.getDimension(R.styleable.FriendItemView_friendItemviewTitleSize, 15);
        a.recycle();

        initView();
        setListener();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_friend_item, this, true);

        tvValue = (TextView) findViewById(R.id.tv_value);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void initData() {
        if (!TextUtils.isEmpty(valueStr) && tvValue != null) {
            tvValue.setText(valueStr);
            tvValue.setVisibility(View.VISIBLE);
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueSize);
            tvValue.setTextColor(valueColor);
        }

        if (!TextUtils.isEmpty(titleStr) && tvTitle != null) {
            tvTitle.setText(titleStr);
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            tvTitle.setTextColor(titleColor);
        }


    }

    public void setValue(int number) {
        if (tvValue != null) {
            tvValue.setText(number + "");
        }
    }

    private void setListener() {

    }


    public void setContentText(int resId) {
        if ((resId >>> 24) < 2) {
            return;
        }
        setContentText(getContext().getString(resId));
    }

    public void setContentText(String text) {
        if (null == tvValue) {
            return;
        }
        this.valueStr = text;
        tvValue.setVisibility(View.VISIBLE);
        tvValue.setText(TextUtils.isEmpty(text) ? "" : text);
    }


    public interface OnItemViewModeCheckedChangeListener {
        void onModeCheckedChanged(int id, boolean isChecked);
    }

    public interface OnItemViewRadioCheckedChangeListener {
        void onRadioCheckedChanged(int id, boolean isActive);
    }

    public interface OnContentClickListener {
        void onContentClick();
    }


}