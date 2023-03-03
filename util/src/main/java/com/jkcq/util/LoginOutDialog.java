package com.jkcq.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

/*
 *
 *
 * @author mhj
 * Create at 2019/3/30 17:27
 */
public class LoginOutDialog extends BaseDialog {

    Context context;
    TextView tvSure;
    TextView tv_message, tv_title;
    String message, buttonValue;


    public LoginOutDialog(@NonNull Context context) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_loginout_layout);
        this.context = context;

        initView();
        initEvent();

    }

    public LoginOutDialog(@NonNull Context context, String message, String buttonValue) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_loginout_layout);
        this.context = context;
        this.message = message;
        this.buttonValue = buttonValue;
        initView();
        initEvent();

    }


    public void initView() {
        tv_title = findViewById(R.id.tv_title);
        tvSure = findViewById(R.id.tv_sure);
        tv_message = findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(message)) {
            tv_message.setText(message);
        }
        if (!TextUtils.isEmpty(buttonValue)) {
            tvSure.setText(buttonValue);
        }

    }

    public void initEvent() {

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    dismiss();
                    listener.onSureOnclick();
                }
            }
        });


    }


    @Override
    public void show() {
        super.show();
        //   WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        // layoutParams.gravity = Gravity.CENTER;
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT ;
        /*layoutParams.width = (int) context.getResources().getDimension(R.dimen.dilaog_common_width);
        layoutParams.height = (int) context.getResources().getDimension(R.dimen.dilaog_common_height);*/
        //getWindow().getDecorView().setPadding(0, 0, 0, 0);
        //  getWindow().setAttributes(layoutParams);
        this.setCancelable(false);
    }


    private OnButtonListener listener;

    public void setBtnOnclick(OnButtonListener listener) {
        this.listener = listener;
    }


}
