package com.buel.holyhelper.utils;

import com.orhanobut.logger.LoggerHelper;

import java.util.Calendar;

/**
 * Created by 19001283 on 2018-07-18.
 */

public class CalendarUtils {

    /**
     * 원하는 월의 임의의 요일을 포함한 날짜를 구한다.
     *
     * @param year
     * @param month
     * @param dayOfWeek
     * @return
     */
    @SuppressWarnings("static-access")
    public static String[] getWeekendsDate(int year, int month, int dayOfWeek) {

        int yyyy = year;
        int mm = month;
        String[] compareWeekOfDay = new String[5];
        Calendar cal = Calendar.getInstance();

        cal.set(yyyy, mm - 1, 1);
        int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int cnt = 0;
        for (int i = 1; i < maxDate + 1; i++) {
            cal.clear();
            cal.set(yyyy, mm - 1, i);

            if (cal.get(cal.DAY_OF_WEEK) == dayOfWeek) {
                if (i < 10) {
                    compareWeekOfDay[cnt] = String.valueOf("0" + i);
                } else {
                    compareWeekOfDay[cnt] = String.valueOf(i);
                }
                cnt++;
            }

            if (compareWeekOfDay.length == 4) {
                compareWeekOfDay[5] = "";
            }
            cal.clear();
        }
        return compareWeekOfDay;
    }

    public static String getDaysTime(Integer dayNum) {
        LoggerHelper.d("dayNum : " + dayNum);
        String day = "";
        switch (dayNum) {
            case 0:
                day = "새벽";
                break;
            case 1:
                day = "낮";
                break;
            case 2:
                day = "저녁";
                break;
            case 3:
                day = "철야";
                break;
            case 4:
                day = "모임";
                break;
        }
        return day;
    }
    public static String getDateDay(Integer dayNum) {
        String day = "";
        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }
        return day;
    }


}
