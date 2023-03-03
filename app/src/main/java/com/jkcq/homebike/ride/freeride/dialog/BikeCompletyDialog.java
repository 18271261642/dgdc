package com.jkcq.homebike.ride.freeride.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jkcq.homebike.R;
import com.jkcq.homebike.bike.BikeConfig;
import com.jkcq.util.BaseDialog;
import com.jkcq.util.OnButtonListener;
import com.jkcq.viewlibrary.ItemSportView;

/*
 *
 *
 * @author mhj
 * Create at 2019/3/30 17:27
 */
public class BikeCompletyDialog extends BaseDialog {

    Context context;
    ItemSportView item_dis, item_time, item_cal;
    LinearLayout layout_detail;
    ImageView iv_finish_right;
    TextView tv_back, tv_detail;
    boolean isSuccess;


    public BikeCompletyDialog(@NonNull Context context, String dis, String time, String cal, boolean isSuccess) {
        super(context, R.style.SimpleHUD1);
        setContentView(R.layout.dialog_bike_complety_layout);
        this.context = context;

        initView();
        initEvent();
        initData(dis, time, cal, isSuccess);

    }

    private void initData(String dis, String time, String cal, boolean isSuccess) {
        this.isSuccess = isSuccess;
        item_time.setBg(R.drawable.shape_layout_gray_bg_15);
        item_dis.setBg(R.drawable.shape_layout_gray_bg_15);
        BikeConfig.disUnit(item_dis.getUnitlTextView(), context);
        item_cal.setBg(R.drawable.shape_layout_gray_bg_15);
        item_dis.setValueText(dis);
        item_time.setValueText(time);
        item_cal.setValueText(cal);
        if (isSuccess) {
            tv_detail.setText(context.getResources().getText(R.string.sport_detail));
            iv_finish_right.setVisibility(View.VISIBLE);
        } else {
            tv_detail.setText(context.getResources().getText(R.string.sport_update_fail));
            iv_finish_right.setVisibility(View.INVISIBLE);
        }

        //  tvTitle.setText(title);
    }


    public void initView() {
        item_dis = findViewById(R.id.item_dis);
        iv_finish_right = findViewById(R.id.iv_finish_right);
        item_time = findViewById(R.id.item_time);
        item_cal = findViewById(R.id.item_cal);
        tv_detail = findViewById(R.id.tv_detail);
        layout_detail = findViewById(R.id.layout_detail);
        tv_back = findViewById(R.id.tv_back);

    }

    public void initEvent() {

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    dismiss();
                    listener.onCancleOnclick();
                }
            }
        });
        layout_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    dismiss();
                    if (isSuccess) {
                        listener.onSureOnclick();
                    } else {
                        listener.onCancleOnclick();
                    }
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
