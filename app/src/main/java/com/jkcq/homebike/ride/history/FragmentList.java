package com.jkcq.homebike.ride.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jkcq.homebike.R;
import com.jkcq.util.DateUtil;
import com.jkcq.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FragmentList extends Fragment {

    public static final int TYPE_DAY = 1;
    public static final int TYPE_WEEK = 2;
    public static final int TYPE_MONTH = 3;
    private int type;
    private int count = 5000;

    private ViewPager viewPager;

    private FragmentStatePagerAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type", TYPE_DAY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, null);
        viewPager = (ViewPager) view;

        EventBus.getDefault().register(this);
        pagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (type) {
                    case TYPE_DAY:
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -(getCount() - (position + 1)));
                        int date = (int) (cal.getTimeInMillis() / 1000);
                        DayFragment fragment = new DayFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("date", date);
                        fragment.setArguments(bundle);
                        return fragment;
                    case TYPE_WEEK:
                        Calendar weekcal = getCurretWeek(getCount() - (position + 1));
                        Date firstDayWeek = getFirstDayOfWeek(weekcal.getTime());
                        String startWeek = DateUtil.dataToString(firstDayWeek, "yyyy-MM-dd");
                        String endWeek = DateUtil.dataToString(getLastDayOfWeek(weekcal.getTime()), "yyyy-MM-dd");
                        int weekdate = (int) (weekcal.getTimeInMillis() / 1000);
                        Fragment weekfragment = new WeekFragment();
                        Bundle weekbundle = new Bundle();
                        weekbundle.putInt("date", (int) (firstDayWeek.getTime() / 1000));
                        weekbundle.putString("startdate", startWeek);
                        weekbundle.putString("enddate", endWeek);
                        weekfragment.setArguments(weekbundle);
                        return weekfragment;
                    case TYPE_MONTH:
                        Calendar monthcal = Calendar.getInstance();
                        monthcal.add(Calendar.MONTH, -(getCount() - (position + 1)));
                        int monthdate = (int) (monthcal.getTimeInMillis() / 1000);
                        Fragment monthfragment = new MonthFragment();
                        Bundle monthbundle = new Bundle();
                        monthbundle.putInt("date", monthdate);
                        monthfragment.setArguments(monthbundle);
                        return monthfragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                if (type == TYPE_DAY) {
                    return count;
                } else {
                    return count;
                }
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

        };

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

               /* if (position == 0) {
                    int temp = viewPager.getAdapter().getCount();
                    int pagesize = viewPager.getAdapter().getCount() / count;
                    int pageyu = viewPager.getAdapter().getCount() % count;
                    if (pageyu != 0) {
                        pagesize++;
                    }
                    count = count * pagesize + count;
                    viewPager.getAdapter().notifyDataSetChanged();
                    viewPager.setCurrentItem(temp);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(count);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        switch (messageEvent.getMsg()) {
            case MessageEvent.DayAdd:
                int currentCount = viewPager.getCurrentItem();
                if (currentCount - 1 < 0) {
                    currentCount = 0;
                }
                viewPager.setCurrentItem(currentCount - 1);
                break;
            case MessageEvent.DaySub:
                int currentSubCount = viewPager.getCurrentItem();
                if (viewPager.getAdapter().getCount() < currentSubCount) {
                    currentSubCount = currentSubCount - 1;
                }
                viewPager.setCurrentItem(currentSubCount + 1);
                break;
            case MessageEvent.viewPageChage:
                //???????????????????????????count??????

                String dateStr = (String) messageEvent.getObj();
                //???????????????
                Calendar calendar = Calendar.getInstance();//????????????
                Calendar selectCalendar = Calendar.getInstance();//???????????????


                String[] strings = dateStr.split("-");
                try {
                    selectCalendar.set(Calendar.YEAR, Integer.parseInt(strings[0]));
                    selectCalendar.set(Calendar.MONTH, Integer.parseInt(strings[1]) - 1);
                    selectCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strings[2]));
                } catch (Exception e) {

                } finally {
                    int days = daysBetween(calendar, selectCalendar);
                    int page = days / 31;
                    int pageYu = days % 31;
                    if (pageYu > 0) {
                        page = page + 1;
                    }
                    if (page == 0) {
                        page = 1;
                    }

                    count = 30 * page;
                    viewPager.getAdapter().notifyDataSetChanged();
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - days - 1);


                }
                break;

        }


    }

    public static int daysBetween(Calendar date1, Calendar date2) {

        //????????????,???????????????????????????
        int days;
        if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
            int day1 = date1.get(Calendar.DAY_OF_YEAR);
            int day2 = date2.get(Calendar.DAY_OF_YEAR);
            days = day1 - day2;
        } else {
            days = (int) ((date1.getTimeInMillis() - date2.getTimeInMillis())
                    / (1000 * 60 * 60 * 24));
        }
        return days;
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    public Calendar getCurretWeek(int positon) {

        Calendar weekcal = Calendar.getInstance();

        //?????????????????????2015???11???17???14:40:12
        Calendar cal = Calendar.getInstance();//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        cal.setFirstDayOfWeek(Calendar.SUNDAY); // ????????????????????????????????????
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// ?????????????????????
        cal.setMinimalDaysInFirstWeek(7); // ?????????????????????7???
        cal.setTime(new Date());
        weekcal.add(Calendar.WEEK_OF_YEAR, -positon);
        int weeks = cal.get(Calendar.WEEK_OF_YEAR);

        System.out.println(weeks);
        return weekcal;
    }

    public static String getWeek(Calendar calendar) {
        String Week = "???";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "???";
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "???";
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "???";
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "???";
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "???";
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "???";
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "???";
        }
        return Week;
    }


    /**
     * ???????????????????????????????????????
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime();
    }


}
