package com.jkcq.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

/*
 *
 *
 * @author mhj
 * Create at 2019/3/30 17:27
 */
public class TipsDialog extends BaseDialog {

    Context context;
    TextView tv_tips;


    public TipsDialog(@NonNull Context context, String title) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_tips_layout);
        this.context = context;
        initView();
        initEvent();
        initData(title);

    }

    private void initData(String title) {

        //  tvTitle.setText(title);
        if (!TextUtils.isEmpty(title)) {
            tv_tips.setText(title);
        }
    }


    public void initView() {
        tv_tips = findViewById(R.id.tv_tips);

    }

    public void initEvent() {


    }


    @Override
    public void show() {
        super.show();
        this.setCancelable(false);
    }


    private OnButtonListener listener;

    public void setBtnOnclick(OnButtonListener listener) {
        this.listener = listener;
    }


}
