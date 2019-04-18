package com.buel.holyhelper.utils;

import android.content.Context;
import android.graphics.Color;

import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.model.AttendModel;
import com.buel.holyhelper.model.BarChartModel;
import com.buel.holyhelper.model.DateModel;
import com.buel.holyhelper.model.HolyModel;
import com.commonLib.Common;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;

public class BarChartDataUtils {

    static ArrayList<AttendModel> attendModels = new ArrayList<>();
    static HashMap<String, Integer> okDateMaps;
    static String teamUid;
    static HolyModel.groupModel.teamModel teamModel;
    static Context context;
    static Boolean isGroup;
    static String[] xAxisArr;

    /**
     * getAttandData
     *
     * @param teamModel
     * @param isGroup
     */
    public static BarChartModel getAttandData(HolyModel.groupModel.teamModel teamModel, Boolean isGroup) {
        BarChartDataUtils.isGroup = isGroup;
        okDateMaps = new HashMap<>();
        BarChartDataUtils.teamModel = teamModel;
        return getAttendRate();
    }


    private static BarChartModel getAttendRate() {

        xAxisArr = CalendarUtils.getWeekendsDate(
                CommonData.getSelectedYear(),
                CommonData.getSelectedMonth(),
                CommonData.getSelectedDayOfWeek());
        HashMap<String, DateModel> dateModelHashMap = CommonData.getAttendDateMaps();

        if (dateModelHashMap == null) {
            if (teamModel != null)
                LoggerHelper.e(teamModel.name + " 의 " + CommonData.getSelectedYear() + "." + CommonData.getSelectedMonth() + "(" + CommonData.getSelectedDayOfWeek() + ")" + "출석데이터가 올바르지 않습니다.");
            return null;
        }

        int size = xAxisArr.length;
        String xAxisStr = "";
        for (int i = 0; i < size; i++) {

            if (xAxisArr[i] == null) {
                xAxisArr[i] = "-";
                //LoggerHelper.d(xAxisArr[xAxisArr.length - 1]);
            }

            xAxisStr = xAxisArr[i];

            String mapkey =
                    CommonData.getSelectedYear() + "-" +
                            Common.addZero(CommonData.getSelectedMonth()) + "-" +
                            xAxisStr + "-" +
                            CommonData.getSelectedDayOfWeek() + "-" +
                            CommonData.getSelectedDays();

            //LoggerHelper.d("mapkey : " + mapkey);

            if (dateModelHashMap.get(mapkey) == null) {
                okDateMaps.put(xAxisStr, 0);
            } else {
                DateModel dateModel = dateModelHashMap.get(mapkey);
                HashMap<String, AttendModel> itemModelmap = dateModel.member;
                //member 데이터
                for (Map.Entry<String, AttendModel> elem : itemModelmap.entrySet()) {
                    //System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
                    attendModels.add(elem.getValue());

                    AttendModel eleAttendModel = elem.getValue();

                    Integer okCnt = 0;

                    if (isGroup) {
                        if (okDateMaps.get(eleAttendModel.date) != null) {
                            okCnt = okDateMaps.get(eleAttendModel.date);
                        }
                        try {
                            if (eleAttendModel.attend.equals("true")) {
                                okDateMaps.put(eleAttendModel.date, okCnt + 1);
                            } else {
                                okDateMaps.put(eleAttendModel.date, okCnt);
                            }
                        }catch (Exception e){
                            okDateMaps.put(eleAttendModel.date, okCnt);
                        }
                    } else {
                        if (eleAttendModel.teamUID.equals(teamModel.uid)) {
                            if (okDateMaps.get(eleAttendModel.date) != null) {
                                okCnt = okDateMaps.get(eleAttendModel.date);
                            }

                            try {
                                if (eleAttendModel.attend.equals("true")) {
                                    okDateMaps.put(eleAttendModel.date, okCnt + 1);
                                } else {
                                    okDateMaps.put(eleAttendModel.date, okCnt);
                                }
                            }catch (Exception e){
                                okDateMaps.put(eleAttendModel.date, okCnt);
                            }
                        }
                    }
                }
            }
        }

        HashMap<String, String> mentMap = getSortList(okDateMaps);

        String type1 = "출석";
        String ment = getMakeMent(mentMap, type1);

        //MakeBarChartView chart = new MakeBarChartView(generateDataBar(), context, xAxisArr, ment);
        //View chartView = chart.getConvertView();

        BarChartModel barChartModel = new BarChartModel();
        barChartModel.bardata = generateDataBar();
        barChartModel.ment = ment;
        barChartModel.evg = mentMap.get("evgNum");

        if (teamModel != null)
            barChartModel.title = "<strong> [ " + CommonData.getGroupModel().name + " " + SortMapUtil.getInteger(teamModel.name) + " : " + teamModel.etc + " ] </strong>\n" + CommonData.getCurrentFullDateStr();
        else {
            if (CommonData.getSelectedMonth() <= 0) {
                barChartModel.title = (CommonData.getSelectedYear() - 1) + "년 " + (12 + CommonData.getSelectedMonth()) + "월 (" + CalendarUtils.getDateDay(CommonData.getSelectedDayOfWeek()) + ") " + CalendarUtils.getDaysTime(CommonData.getSelectedDays()) + "예배";
            } else {
                barChartModel.title = CommonData.getSelectedYear() + "년 " + CommonData.getSelectedMonth() + "월 (" + CalendarUtils.getDateDay(CommonData.getSelectedDayOfWeek()) + ")" + CalendarUtils.getDaysTime(CommonData.getSelectedDays()) + "예배";
            }
        }

        barChartModel.XaxisValues = xAxisArr;
        return barChartModel;
    }

    private static int sumOkCnt = 0;
    private static int sumNoCnt = 0;

    private static HashMap getSortList(HashMap<String, Integer> dateMaps) {

        LoggerHelper.i("TeamBriefingRecyclerFragment getSortList");
        HashMap<String, String> mentMap = new HashMap<>();
        ArrayList<String> tempDateList = new ArrayList<>();
        ArrayList<String> tempCntList = new ArrayList<>();
        TreeMap<String, Integer> tm = new TreeMap<>(dateMaps);
        Iterator iteratorValue = sortByValue(tm).iterator();

        int cnt = 0;
        int evgCnt = 0;
        double sumNum = 0;
        double evgNum = 0;
        while (iteratorValue.hasNext()) {
            String key = (String) iteratorValue.next();

            sumNum += dateMaps.get(key);
            HashMap<String, Integer> tempMap = new HashMap<>();
            tempMap.put(key, tm.get(key));
            tempDateList.add(key);
            tempCntList.add(String.valueOf(tm.get(key)));
            cnt++;

            if (dateMaps.get(key) != 0)
                evgCnt++;
        }
        try {
            mentMap.put("cnt", String.valueOf(cnt));
            mentMap.put("sumNum", String.valueOf(sumNum));
            mentMap.put("fst_date", tempDateList.get(0));
            mentMap.put("fst", tempCntList.get(0));
            mentMap.put("last_date", tempDateList.get(evgCnt - 1));
            mentMap.put("last", tempCntList.get(evgCnt - 1));
            LoggerHelper.d("mentMap : " + mentMap);
            evgNum = sumNum / evgCnt;
            mentMap.put("evgNum", String.valueOf(evgNum));
        } catch (Exception e) {
            //LoggerHelper.d("e.getMessage() ::::::::::: + " + e.getMessage());
        }
        return mentMap;
    }

    public static List sortByValue(final Map map) {

        List<String> list = new ArrayList();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable) v2).compareTo(v1);
            }
        });
        //Collections.reverse(list); // 주석시 오름차순
        return list;
    }

    private static BarData generateDataBar() {

        ArrayList<BarEntry> okEntries = new ArrayList<>();

        Integer dCnt = 0;
        sumOkCnt = 0;
        for (String eleWeekOfDay : xAxisArr) {
            if (okDateMaps.get(eleWeekOfDay) != null) {
                okEntries.add(new BarEntry(dCnt, okDateMaps.get(eleWeekOfDay)));
                sumOkCnt = +okDateMaps.get(eleWeekOfDay);
            }
            dCnt++;
        }

        return getBarData(okEntries, Color.BLUE, CommonData.getSelectedMonth() + " 월 출석");
    }

    @NonNull
    private static BarData getBarData(ArrayList<BarEntry> entries, int color, String dataName) {
        BarDataSet d = new BarDataSet(entries, dataName);

        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);

        return cd;
    }

    @NonNull
    private static String getMakeMent(HashMap<String, String> mentMap, String type) {

        if (mentMap.get("sumNum") == null) mentMap.put("sumNum", "-");
        if (mentMap.get("evgNum") == null) mentMap.put("evgNum", "-");
        if (mentMap.get("fst_date") == null) mentMap.put("fst_date", "-");
        if (mentMap.get("last_date") == null) mentMap.put("last_date", "-");
        if (mentMap.get("last") == null) mentMap.put("last", "-");
        if (mentMap.get("fst") == null) mentMap.put("fst", "-");

        String ment = String.valueOf(Common.addZero(CommonData.getSelectedMonth())) + "월 " + type + "현황입니다. <br>" +
                "전체 " + type + "은 <strong>" + mentMap.get("sumNum") + " 명</strong>,  " +
                "전체 평균은 <strong>" + mentMap.get("evgNum") + " 명</strong>입니다. <br>" +
                type + "이 높은 날은 <strong>" + mentMap.get("fst_date") + "일 " + mentMap.get("fst") + "명</strong>, <br>" +
                type + "이 낮은 날은 <strong>" + mentMap.get("last_date") + "일 " + mentMap.get("last") + "명</strong> 입니다. <br>";
        return ment;
    }
}