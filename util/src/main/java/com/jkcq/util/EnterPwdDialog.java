package com.jkcq.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jkcq.util.ktx.ToastUtil;

/*
 *
 *
 * @author mhj
 * Create at 2019/3/30 17:27
 */
public class EnterPwdDialog extends BaseDialog {

    Context context;
    TextView tvMessage;
    TextView tvCancel, tvSure;
    EditText et_pk_pwd;


    public EnterPwdDialog(@NonNull Context context, String title, String message, String cancelStr, String sureStr) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_enter_pwd_layout);
        this.context = context;
        this.isShowCancel = true;
        initView();
        initEvent();
        initData(title, message, cancelStr, sureStr);

    }

    boolean isShowCancel = true;

    public EnterPwdDialog(@NonNull Context context, String title, String message, String cancelStr, String sureStr, boolean isshowCancle) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_yes_or_no_layout);
        this.context = context;

        this.isShowCancel = isshowCancle;
        initView();
        initEvent();
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
        if (isShowCancel) {
            tvCancel.setVisibility(View.VISIBLE);
        } else {
            tvCancel.setVisibility(View.GONE);
        }
    }


    public void initView() {
        tvCancel = findViewById(R.id.cancel);
        tvSure = findViewById(R.id.sure);
        tvMessage = findViewById(R.id.tv_message);
        et_pk_pwd = findViewById(R.id.et_pk_pwd);

    }

    String pwd;

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
                pwd = et_pk_pwd.getText().toString().trim();
                if (pwd.length() < 4) {
                    ToastUtil.showTextToast(context, context.getString(R.string.pk_hide_enter_pwd));
                    return;
                }
                if (listener != null) {
                    dismiss();
                    listener.onSureOnclick(pwd);
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


    private OnEnterButtonListener listener;

    public void setBtnOnclick(OnEnterButtonListener listener) {
        this.listener = listener;
    }


}
