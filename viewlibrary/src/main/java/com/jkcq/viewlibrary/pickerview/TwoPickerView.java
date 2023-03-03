package com.jkcq.viewlibrary.pickerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jkcq.viewlibrary.R;
import com.jkcq.viewlibrary.pickerview.adapter.ArrayWheelAdapter;
import com.jkcq.viewlibrary.pickerview.listener.OnItemSelectedListener;
import com.jkcq.viewlibrary.pickerview.view.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
 *
 * classes : com.jkcq.gym.view.pickerview
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016/10/22 16:46
 */
public class TwoPickerView extends LinearLayout {

    private WheelView arrayWheelView;
    private WheelView arrayWheelView2;
    private ItemSelectedValue itemSelectedValue;

    /**
     * 设置是否循环滚动
     */
    private boolean cyclic = true;

    private List<String> listSource;
    private ArrayList<String> listSource2;

    public TwoPickerView(Context context) {
        this(context, null);
    }

    public TwoPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initBase(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TwoPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initBase(context, attrs, defStyleAttr);
    }

    private void initBase(Context context, AttributeSet attrs, int defStyleAttr) {
        initView();
        setListener();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_picker_two_array, this, true);

        arrayWheelView = (WheelView) findViewById(R.id.array_picker1);
        arrayWheelView2 = (WheelView) findViewById(R.id.array_picker2);
    }

    private void initData() {

    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
        arrayWheelView.setCyclic(cyclic);
    }

    public void setSelectItem(int index) {
        arrayWheelView.setCurrentItem(index);
    }

    public void setData(String[] items, String unitl) {

        listSource = new ArrayList<>();
        listSource.addAll(Arrays.asList(items));
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>((ArrayList<String>) listSource);
        arrayWheelView.setAdapter(arrayWheelAdapter);
    }

    public void setItemOnclick(ItemSelectedValue itemOnclick) {
        itemSelectedValue = itemOnclick;
    }

    public void setData(ArrayList<String> items, String unitl) {
        listSource = items;
        listSource2.clear();
        listSource2.add("0");
        listSource2.add("1");
        listSource2.add("2");
        listSource2.add("3");
        listSource2.add("4");
        listSource2.add("5");
        listSource2.add("6");
        listSource2.add("7");
        listSource2.add("8");
        listSource2.add("9");
        if (TextUtils.isEmpty(unitl)) {

        } else {
            arrayWheelView2.setLabel(unitl);
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(items);
        arrayWheelView.setAdapter(arrayWheelAdapter);
        ArrayWheelAdapter arrayWheelAdapter2 = new ArrayWheelAdapter<String>(listSource2);
        arrayWheelView2.setAdapter(arrayWheelAdapter2);
    }

    private void setListener() {
        arrayWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (itemSelectedValue != null) {
                    itemSelectedValue.onItemSelectedValue(getItem());
                }
            }
        });
    }


    public interface ItemSelectedValue {
        public void onItemSelectedValue(String str);
    }

    public int getCurrentItem() {
        return arrayWheelView.getCurrentItem();
    }

    public String getItem() {
        if (null == listSource || listSource.isEmpty()) {
            return null;
        }
        int position = arrayWheelView.getCurrentItem();
        if (position < 0 || position >= listSource.size()) {
            return null;
        }

        return listSource.get(position);
    }
}