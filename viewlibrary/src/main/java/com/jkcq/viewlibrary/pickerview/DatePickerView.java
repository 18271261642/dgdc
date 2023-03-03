package com.jkcq.viewlibrary.pickerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jkcq.util.DateUtil;
import com.jkcq.viewlibrary.R;
import com.jkcq.viewlibrary.pickerview.adapter.NumericWheelAdapter;
import com.jkcq.viewlibrary.pickerview.listener.OnItemSelectedListener;
import com.jkcq.viewlibrary.pickerview.utils.DateUtils;
import com.jkcq.viewlibrary.pickerview.view.WheelView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/*
 *
 * classes : com.jkcq.gym.view.pickerview
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016/10/22 16:46
 */
public class DatePickerView extends LinearLayout {

    private final int all = 0;

    private final int year_month_day_hour_min = 1;

    private final int year_month_day = 2;

    private final int hour_min = 3;

    private final int hour = 4;

    public static final int DEFAULT_START_YEAR = 1900;

    public static int DEFAULT_END_YEAR = 2100;

    private final List<String> month_big = Arrays.asList(new String[]{"1", "3", "5", "7", "8", "10", "12"});

    private final List<String> month_little = Arrays.asList(new String[]{"4", "6", "9", "11"});

    private WheelView yearWheelView;
    private WheelView monthWheelView;
    private WheelView dayWheelView;
    private WheelView hourWheelView;
    private WheelView minuteWheelView;
    private WheelView secondWheelView;
    private int localYear;
    private int localMonth;
    /**
     * 设置是否循环滚动
     */
    private boolean cyclic = true;

    /**
     * 设置是否显示文字
     */
    private boolean showLabel = true;

    //    private int type = all;
    private int type = year_month_day;

    public DatePickerView(Context context) {
        this(context, null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initBase(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initBase(context, attrs, defStyleAttr);
    }

    public void setType(int type) {
        this.type = type;
        initData();
    }

    private void initBase(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker,
                defStyleAttr, 0);
        showLabel = a.getBoolean(R.styleable.DatePicker_showLabel, showLabel);
        cyclic = a.getBoolean(R.styleable.DatePicker_cyclic, cyclic);
        type = a.getInt(R.styleable.DatePicker_type, year_month_day);
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
        LayoutInflater.from(getContext()).inflate(R.layout.view_picker_date, this, true);

        yearWheelView = (WheelView) findViewById(R.id.year);
        monthWheelView = (WheelView) findViewById(R.id.month);
        dayWheelView = (WheelView) findViewById(R.id.day);
        hourWheelView = (WheelView) findViewById(R.id.hour);
        minuteWheelView = (WheelView) findViewById(R.id.min);
        secondWheelView = (WheelView) findViewById(R.id.sec);
    }

    private void initData() {
        setCyclic(cyclic);
        setShowLabel(showLabel);

        setDefaultItemAdapter(null);

        if (type == year_month_day_hour_min) {
            secondWheelView.setVisibility(View.GONE);
        } else if (type == year_month_day) {
            hourWheelView.setVisibility(View.GONE);
            minuteWheelView.setVisibility(View.GONE);
            secondWheelView.setVisibility(View.GONE);
        } else if (type == hour_min) {
            yearWheelView.setVisibility(GONE);
            monthWheelView.setVisibility(GONE);
            dayWheelView.setVisibility(GONE);
            secondWheelView.setVisibility(GONE);
            hourWheelView.setVisibility(View.VISIBLE);
            minuteWheelView.setVisibility(View.VISIBLE);
        } else if (type == hour) {
            yearWheelView.setVisibility(GONE);
            monthWheelView.setVisibility(GONE);
            dayWheelView.setVisibility(GONE);
            secondWheelView.setVisibility(GONE);
            minuteWheelView.setVisibility(View.GONE);
            hourWheelView.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        yearWheelView.setOnItemSelectedListener(wheelListener_year);
        monthWheelView.setOnItemSelectedListener(wheelListener_month);
    }

    private int year;
    private int month;
    private int day;
    private int selcetYear;

    public void setDefaultItemAdapter(String time) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        Calendar calendar;
        if (TextUtils.isEmpty(time)) {
            calendar = Calendar.getInstance();
            calendar.setTime(new Date());
        } else {
            Date date = new Date();
            if (type == all) {
                date = DateUtils.getDateFromString(time, "yyyy-MM-dd HH:mm:ss");
            } else if (type == year_month_day_hour_min) {
                date = DateUtils.getDateFromString(time, "yyyy-MM-dd HH:mm");
            } else if (type == year_month_day) {
                date = DateUtils.getDateFromString(time, "yyyy-MM-dd");
            } else {
                date = DateUtils.getDateFromString(time, "HH:mm");
            }
            if (null == date) {
                date = new Date();
            }
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }

        year = Calendar.getInstance().get(Calendar.YEAR);
        selcetYear = calendar.get(Calendar.YEAR);

        localYear = selcetYear;
        DEFAULT_END_YEAR = year;
        month = calendar.get(Calendar.MONTH);
        localMonth = month + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        monthWheelView.setAdapter(new NumericWheelAdapter(1, month + 1));
        dayWheelView.setAdapter(new NumericWheelAdapter(1, day + 1));
        //int hour = calendar.get(Calendar.HOUR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        yearWheelView.setCurrentItem(selcetYear - DEFAULT_START_YEAR);
        monthWheelView.setCurrentItem(month);
        dayWheelView.setCurrentItem(day);

        hourWheelView.setCurrentItem(hour);
        minuteWheelView.setCurrentItem(minute);
        secondWheelView.setCurrentItem(second);

        yearWheelView.setAdapter(new NumericWheelAdapter(DEFAULT_START_YEAR, DEFAULT_END_YEAR));

        setHourAndDay(selcetYear, year);
//        monthWheelView.setAdapter(new NumericWheelAdapter(1, 12));
        // 判断大小月及是否闰年,用来确定"日"的数据
//        if (month_big.contains(String.valueOf(month + 1))) {
//            dayWheelView.setAdapter(new NumericWheelAdapter(1, 31));
//        } else if (month_little.contains(String.valueOf(month + 1))) {
//            dayWheelView.setAdapter(new NumericWheelAdapter(1, 30));
//        } else {
//            // 闰年
//            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
//                dayWheelView.setAdapter(new NumericWheelAdapter(1, 29));
//            else
//                dayWheelView.setAdapter(new NumericWheelAdapter(1, 28));
//        }

        hourWheelView.setAdapter(new NumericWheelAdapter(0, 23));
        minuteWheelView.setAdapter(new NumericWheelAdapter(0, 59));
        secondWheelView.setAdapter(new NumericWheelAdapter(0, 59));
    }


    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
        yearWheelView.setCyclic(cyclic);
        monthWheelView.setCyclic(cyclic);
        dayWheelView.setCyclic(cyclic);
        hourWheelView.setCyclic(cyclic);
        minuteWheelView.setCyclic(cyclic);
        secondWheelView.setCyclic(cyclic);
    }

    /**
     * 设置是否显示标签文字 默认显示
     *
     * @param showLabel
     */
    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;

        yearWheelView.setLabel(!showLabel ? "" : getContext().getString(R.string.pickerview_year));
        monthWheelView.setLabel(!showLabel ? "" : getContext().getString(R.string.pickerview_month));
        dayWheelView.setLabel(!showLabel ? "" : getContext().getString(R.string.pickerview_day));
        hourWheelView.setLabel(!showLabel ? "" : getContext().getString(R.string.pickerview_hours));
        minuteWheelView.setLabel(!showLabel ? "" : getContext().getString(R.string.pickerview_minutes));
        secondWheelView.setLabel(!showLabel ? "" : getContext().getString(R.string.pickerview_seconds));
    }

    public String getTime() {
        StringBuffer buffer = new StringBuffer();
        if (type == all) {
            buffer.append((yearWheelView.getCurrentItem() + DEFAULT_START_YEAR)).append("-")
                    .append((monthWheelView.getCurrentItem() + 1)).append("-")
                    .append((dayWheelView.getCurrentItem() + 1)).append(" ")
                    .append(hourWheelView.getCurrentItem()).append(":")
                    .append(minuteWheelView.getCurrentItem()).append(":")
                    .append(secondWheelView.getCurrentItem());
        } else if (type == year_month_day_hour_min) {
            buffer.append((yearWheelView.getCurrentItem() + DEFAULT_START_YEAR)).append("-")
                    .append((monthWheelView.getCurrentItem() + 1)).append("-")
                    .append((dayWheelView.getCurrentItem() + 1)).append(" ")
                    .append(hourWheelView.getCurrentItem()).append(":")
                    .append(minuteWheelView.getCurrentItem());
        } else if (type == year_month_day) {
            String day;
            String month;
            if ((monthWheelView.getCurrentItem() + 1) < 10) {
                month = "0" + (monthWheelView.getCurrentItem() + 1);
            } else {
                month = String.valueOf((monthWheelView.getCurrentItem() + 1));
            }
            if ((dayWheelView.getCurrentItem() + 1) < 10) {
                day = "0" + (dayWheelView.getCurrentItem() + 1);
            } else {
                day = String.valueOf(dayWheelView.getCurrentItem() + 1);
            }

            buffer.append((yearWheelView.getCurrentItem() + DEFAULT_START_YEAR)).append("-")
                    .append(month).append("-")
                    .append(day);
        } else if (type == hour_min) {
            buffer.append((DateUtil.formatTwoStr(hourWheelView.getCurrentItem()))).append(":")
                    .append(DateUtil.formatTwoStr(minuteWheelView.getCurrentItem()));
        } else if (type == hour) {
            buffer.append((DateUtil.formatTwoStr(hourWheelView.getCurrentItem()))).append(":")
                    .append(DateUtil.formatTwoStr(0));
        }
        return buffer.toString();
    }

    private void setHourAndDay(int year_num, int localYear) {
        if (localYear == year_num) {
            Calendar calendar = Calendar.getInstance();
            monthWheelView.setAdapter(new NumericWheelAdapter(1, calendar.get(Calendar.MONTH) + 1));
            if (localMonth > (calendar.get(Calendar.MONTH) + 1)) {
                monthWheelView.setCurrentItem(calendar.get(Calendar.MONTH) + 1);
            } else if (localMonth == calendar.get(Calendar.MONTH) + 1) {

                dayWheelView.setAdapter(new NumericWheelAdapter(1, calendar.get(Calendar.DAY_OF_MONTH)));
                if (dayWheelView.getCurrentItem() > calendar.get(Calendar.DAY_OF_MONTH)) {
                    dayWheelView.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH));
                }
            } else {
                int maxItem = 30;
                if (month_big
                        .contains(String.valueOf(monthWheelView.getCurrentItem() + 1))) {
                    dayWheelView.setAdapter(new NumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (month_little.contains(String.valueOf(monthWheelView
                        .getCurrentItem() + 1))) {
                    dayWheelView.setAdapter(new NumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0) {
                        dayWheelView.setAdapter(new NumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        dayWheelView.setAdapter(new NumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (dayWheelView.getCurrentItem() > maxItem - 1) {
                    dayWheelView.setCurrentItem(maxItem - 1);
                }
            }

            return;
        } else {
            monthWheelView.setAdapter(new NumericWheelAdapter(1, 12));
        }

        // 判断大小月及是否闰年,用来确定"日"的数据
        int maxItem = 30;
        if (month_big
                .contains(String.valueOf(localMonth))) {
            dayWheelView.setAdapter(new NumericWheelAdapter(1, 31));
            maxItem = 31;
        } else if (month_little.contains(String.valueOf(localMonth))) {
            dayWheelView.setAdapter(new NumericWheelAdapter(1, 30));
            maxItem = 30;
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                dayWheelView.setAdapter(new NumericWheelAdapter(1, 29));
                maxItem = 29;
            } else {
                dayWheelView.setAdapter(new NumericWheelAdapter(1, 28));
                maxItem = 28;
            }
        }
        if (dayWheelView.getCurrentItem() > maxItem - 1) {
            dayWheelView.setCurrentItem(maxItem - 1);
        }
    }

    // 添加"年"监听
    OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int index) {
            int year_num = index + DEFAULT_START_YEAR;
            localYear = year_num;
            setHourAndDay(year_num, year);

        }
    };
    // 添加"月"监听
    OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int index) {

            int month_num = index + 1;
            localMonth = month_num;
            Calendar calendar = Calendar.getInstance();
            if (localYear == year && month_num == (calendar.get(Calendar.MONTH) + 1)) {
                dayWheelView.setAdapter(new NumericWheelAdapter(1, calendar.get(Calendar.DAY_OF_MONTH)));
                if (dayWheelView.getCurrentItem() > calendar.get(Calendar.DAY_OF_MONTH)) {
                    dayWheelView.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH));
                }
                return;
            }
            int maxItem = 30;
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (month_big.contains(String.valueOf(month_num))) {
                dayWheelView.setAdapter(new NumericWheelAdapter(1, 31));
                maxItem = 31;
            } else if (month_little.contains(String.valueOf(month_num))) {
                dayWheelView.setAdapter(new NumericWheelAdapter(1, 30));
                maxItem = 30;
            } else {
                if (((yearWheelView.getCurrentItem() + DEFAULT_START_YEAR) % 4 == 0 && (yearWheelView
                        .getCurrentItem() + DEFAULT_START_YEAR) % 100 != 0)
                        || (yearWheelView.getCurrentItem() + DEFAULT_START_YEAR) % 400 == 0) {
                    dayWheelView.setAdapter(new NumericWheelAdapter(1, 29));
                    maxItem = 29;
                } else {
                    dayWheelView.setAdapter(new NumericWheelAdapter(1, 28));
                    maxItem = 28;
                }
            }
            if (dayWheelView.getCurrentItem() > maxItem - 1) {
                dayWheelView.setCurrentItem(maxItem - 1);
            }
        }
    };
}