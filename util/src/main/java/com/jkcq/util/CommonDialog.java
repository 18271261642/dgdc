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
public class CommonDialog extends BaseDialog {

    Context context;
    TextView tvMessage;
    TextView tvCancel, tvSure;
    View view_center;


    public CommonDialog(@NonNull Context context, String title, String message, String cancelStr, String sureStr) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_common_layout);
        this.context = context;

        initView();
        initEvent();
        initData(title, message, cancelStr, sureStr);

    }

    public CommonDialog(@NonNull Context context, String title, String message, String cancelStr, String sureStr, boolean cancel) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_common_layout);
        this.context = context;

        initView();
        initEvent();
        if (!cancel) {
            tvCancel.setVisibility(View.GONE);
            view_center.setVisibility(View.GONE);
        }
        initData(title, message, cancelStr, sureStr);

    }

    private void initData(String title, String message, String cancelStr, String sureStr) {

        //  tvTitle.setText(title);
        tvMessage.setText(message);
        if (!TextUtils.isEmpty(cancelStr)) {
            tvCancel.setText(cancelStr);
        }
        if (!TextUtils.isEmpty(sureStr)) {
            tvSure.setText(sureStr);
        }
    }


    public void initView() {
        tvCancel = findViewById(R.id.cancel);
        tvSure = findViewById(R.id.sure);
        tvMessage = findViewById(R.id.tv_message);
        view_center = findViewById(R.id.view_center);

    }

    public void initEvent() {

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    dismiss();
                    listener.onCancleOnclick();
                }
            }
        });
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
