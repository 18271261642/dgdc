package com.jkcq.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

/*
 *
 *
 * @author mhj
 * Create at 2019/3/30 17:27
 */
public class PriDialog extends BaseDialog {

    Context context;
    TextView tv_refuse, tv_ok, tv_content;


    public PriDialog(@NonNull Context context, String title, String message, String cancelStr, String sureStr) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_pri_select);
        this.context = context;

        initView();
        initEvent();
        initData(title, message, cancelStr, sureStr);

    }

    private void initData(String title, String message, String cancelStr, String sureStr) {

        //  tvTitle.setText(title);
        tv_content.setText(getClickableSpan());
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void initView() {
        tv_refuse = findViewById(R.id.tv_refuse);
        tv_ok = findViewById(R.id.tv_ok);
        tv_content = findViewById(R.id.tv_content);

    }

    public void initEvent() {

        tv_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    dismiss();
                    listener.onCancleOnclick();
                }
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
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


    private OnButtonPriListener listener;

    public void setBtnOnclick(OnButtonPriListener listener) {
        this.listener = listener;
    }

    private SpannableString getClickableSpan() {


        String connent = context.getString(R.string.privacy_agreement_content);
        String connentagreement = context.getString(R.string.privacy_agreement_tips);
        int indexAgreement = connent.indexOf(connentagreement);
        if (indexAgreement >= 0 && indexAgreement < connent.length()) ;
        SpannableString spannableString = new SpannableString(context.getString(R.string.privacy_agreement_content));


//设置下划线文字

        spannableString.setSpan(new UnderlineSpan(), indexAgreement, indexAgreement + connentagreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//设置文字的单击事件

        spannableString.setSpan(new ClickableSpan() {

            @Override

            public void onClick(View widget) {
                if (listener != null) {
                    listener.onClickPri();
                }
                /*dialog.cancel();
                if (onTypeClickListenter != null) {
                    onTypeClickListenter.changeDevcieonClick(2);
                }*/

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                //  super.updateDrawState(false);
            }
        }, indexAgreement, indexAgreement + connentagreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//设置文字的前景色
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.util_color_text)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.util_color_green)), indexAgreement, indexAgreement + connentagreement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        return spannableString;

    }

}
