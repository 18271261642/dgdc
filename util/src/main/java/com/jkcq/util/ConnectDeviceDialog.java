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
public class ConnectDeviceDialog extends BaseDialog {

    Context context;
    TextView tv_connect_state;





    public ConnectDeviceDialog(@NonNull Context context, String connectState) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_connect_device_layout);
        this.context = context;

        initView();
        initEvent();
        initData(connectState);

    }

    private void initData(String title) {

        //  tvTitle.setText(title);
        if (!TextUtils.isEmpty(title)) {
            tv_connect_state.setText(title);
        }
    }


    public void initView() {
        tv_connect_state = findViewById(R.id.cancel);

    }

    public void initEvent() {


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
