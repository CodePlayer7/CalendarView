package com.callmexyz.calendarview;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.Calendar;

/**
 * Created by CallMeXYZ on 2016/3/22.
 */
public class MonthPagerAdapter extends PagerAdapter {
    private final String TAG = MonthPagerAdapter.class.getSimpleName();
    private ArrayDeque<View> mViewList;
    private Calendar mRangeStart;
    private Calendar mRangeEnd;
    private CalendarView mCalendarView;

    public MonthPagerAdapter(CalendarView calendarView) {
        mCalendarView = calendarView;
        mRangeStart = Utils.getRangeStart();
        mRangeEnd = Utils.getRangeEnd();
        mViewList = new ArrayDeque<>();
    }

    /**
     * whne start or end is null, the relating rang will be set to default range(200 year former or latter) by the current time
     *
     * @param start
     * @param end
     */
    public void setRange(Calendar start, Calendar end) {
        if (null != start && null != end && end.before(start)) {
            end = (Calendar) start.clone();
            Log.w(TAG, "the range end is former than start u kidding me ");
        }
        if (null != start) mRangeStart = Utils.getRange(start.getTimeInMillis());
        if (null != end) mRangeEnd = Utils.getRange(end.getTimeInMillis());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return getCount(mRangeStart, mRangeEnd);
    }

    private int getCount(Calendar start, Calendar end) {
        if (end.before(start)) {
            Log.w(TAG, "range end is before start");
            return 0;
        }
        return Utils.getMonthDiff(start, end) + 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthItem monthItem = new MonthItem(container.getContext(), getItemCalendar(position),mCalendarView );
        container.addView(monthItem);
        mViewList.add(monthItem);
        return monthItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        MonthItem monthItem = ((MonthItem) object);
        container.removeView(monthItem);
        mViewList.remove(monthItem);

    }

    public Calendar getItemCalendar(int position) {
        Calendar c = (Calendar) mRangeStart.clone();
        c.add(Calendar.MONTH, position);
        return c;
    }

    public int getPosition(Calendar c) {
        int i = Utils.getMonthDiff(mRangeStart, c);
        if (i < 0 || i > getCount() - 1) {
            Log.w(TAG, "the given calendar is out of valid range");
        }
        return i < 0 ? 0 : (i > getCount() ? getCount() : i);
    }
}
