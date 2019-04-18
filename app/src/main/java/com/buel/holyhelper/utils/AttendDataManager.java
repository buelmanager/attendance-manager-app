package com.buel.holyhelper.utils;


import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.model.AttendDataModel;
import com.buel.holyhelper.model.AttendModel;
import com.buel.holyhelper.model.DateModel;
import com.buel.holyhelper.view.DataTypeListener;
import com.commonLib.Common;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.orhanobut.logger.LoggerHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;


public class AttendDataManager {
    private int selectDay;

    public enum DATA_TYPE {
        TEAM_DATA,
        GROUP_DATA,
        PERSON_DATA
    }

    private List<String> reasonList = new ArrayList<>();
    private List<String> okAttendList1 = new ArrayList<>();
    private List<String> okAttendList2 = new ArrayList<>();
    private List<String> okAttendList3 = new ArrayList<>();
    private List<String> okAttendList4 = new ArrayList<>();
    private List<String> okAttendList5 = new ArrayList<>();

    private List<String> okExcutiveAttendList1 = new ArrayList<>();
    private List<String> okExcutiveAttendList2 = new ArrayList<>();
    private List<String> okExcutiveAttendList3 = new ArrayList<>();
    private List<String> okExcutiveAttendList4 = new ArrayList<>();
    private List<String> okExcutiveAttendList5 = new ArrayList<>();

    private List<String> noAttendList1 = new ArrayList<>();
    private List<String> noAttendList2 = new ArrayList<>();
    private List<String> noAttendList3 = new ArrayList<>();
    private List<String> noAttendList4 = new ArrayList<>();
    private List<String> noAttendList5 = new ArrayList<>();

    private List<String> reasonAttendList1 = new ArrayList<>();
    private List<String> reasonAttendList2 = new ArrayList<>();
    private List<String> reasonAttendList3 = new ArrayList<>();
    private List<String> reasonAttendList4 = new ArrayList<>();
    private List<String> reasonAttendList5 = new ArrayList<>();

    private HashMap<String, Integer> okDateMaps;
    private HashMap<String, Integer> noDateMaps;
    private int curDayOkCnt;
    private int curDaynoCnt;
    private String[] xAxisArr;
    private AttendModel attendModel;
    //해당 주에 속하는 attendModels
    private HashMap<String, AttendDataModel> weekAttendDataListMap;
    private int selectDays;
    private int selectYear;
    private int selectMonth;
    private int selectDayOfWeek;
    private HashMap<String, DateModel> dateMap;
    private ArrayList<AttendModel> attendModels = new ArrayList<>();
    private String selectUid;
    private DATA_TYPE data_type;

    private AttendDataModel attendDataModel = new AttendDataModel();
    private DataTypeListener.OnCompleteListener<AttendDataModel> onCompleteListener;

    /**
     * @param data_type
     * @param selectYear
     * @param selectMonth
     * @param selectDayOfWeek
     * @param selectDays
     * @param dateMap
     * @param onCompleteListener
     */
    public void getMonthlyDate(
            DATA_TYPE data_type,
            int selectYear,
            int selectMonth,
            int selectDayOfWeek,
            int selectDay,
            int selectDays,
            String selectUid,
            HashMap<String, DateModel> dateMap,
            DataTypeListener.OnCompleteListener<AttendDataModel> onCompleteListener) {

        if (data_type == null ||
                selectYear < 0 ||
                selectUid == null ||
                selectDayOfWeek < 0 ||
                onCompleteListener == null ||
                selectMonth < 0) return;

        this.data_type = data_type;
        this.selectUid = selectUid;
        this.dateMap = dateMap;
        this.selectYear = selectYear;
        this.selectDays = selectDays;
        this.selectDay = selectDay;
        this.selectMonth = selectMonth;
        this.selectDayOfWeek = selectDayOfWeek;
        this.onCompleteListener = onCompleteListener;

        okDateMaps = new HashMap<>();
        noDateMaps = new HashMap<>();
        attendModel = new AttendModel();

        getAttendRate();
    }

    public AttendDataModel getCurDateAttendDataModel(String dateKey) {
        AttendDataModel tempAttendDataModel = weekAttendDataListMap.get(dateKey);

        getDayMakeMent(tempAttendDataModel);
        return tempAttendDataModel;
    }

    private void getAttendRate() {

        curDaynoCnt = 0;
        curDayOkCnt = 0;
        xAxisArr = CalendarUtils.getWeekendsDate(
                selectYear,
                selectMonth,
                selectDayOfWeek);

        attendDataModel.setxAxisArr(xAxisArr);

        HashMap<String, DateModel> dateModelHashMap = dateMap;

        if (dateModelHashMap == null) {
            LoggerHelper.e("출석데이터가 올바르지 않습니다.");
            return;
        }
        int size = xAxisArr.length;
        String xAxisStr = "";

        weekAttendDataListMap = new HashMap<>();

        for (int i = 0; i < size; i++) {

            if (xAxisArr[i] == null) {
                xAxisArr[i] = "-";
                //LoggerHelper.d(xAxisArr[xAxisArr.length - 1]);
            }

            xAxisStr = xAxisArr[i];

            int xAxisNum;
            if (xAxisStr.equals("-")) {
                xAxisNum = 0;
            } else {
                xAxisNum = Integer.parseInt(xAxisStr);
            }
            String mapkey =
                    selectYear + "-" +
                            Common.addZero(selectMonth) + "-" +
                            xAxisStr + "-" +
                            selectDayOfWeek + "-" +
                            selectDays;

            LoggerHelper.d("mapkey : " + mapkey);

            if (dateModelHashMap.get(mapkey) == null) {
                okDateMaps.put(xAxisStr, 0);
                noDateMaps.put(xAxisStr, 0);
            } else {
                DateModel dateModel = dateModelHashMap.get(mapkey);
                HashMap<String, AttendModel> itemModelmap = dateModel.member;
                //member 데이터
                for (Map.Entry<String, AttendModel> elem : itemModelmap.entrySet()) {

                    AttendModel eleAttendModel = elem.getValue();
                    String strCompareUid = null;

                    if (data_type == DATA_TYPE.GROUP_DATA)
                        strCompareUid = eleAttendModel.groupUID;
                    else if (data_type == DATA_TYPE.TEAM_DATA)
                        strCompareUid = eleAttendModel.teamUID;
                    else if (data_type == DATA_TYPE.PERSON_DATA)
                        strCompareUid = eleAttendModel.memberUID;

                    if (strCompareUid == null) return;

                    if (strCompareUid.equals(selectUid)) {
                        attendModels.add(eleAttendModel);
                        ArrayList<AttendModel> weekAttendModels = new ArrayList<>();
                        weekAttendModels.add(eleAttendModel);

                        AttendDataModel tempAttendDataModel = null;

                        //현재 weekAttendDataListMap의 해당 일에 데이터가 있다면 그 데이터를 가지고 오고
                        LoggerHelper.d("eleAttendModel.date : " + eleAttendModel.date);

                        int curWeekIndex = (int) Math.floor(Integer.valueOf(eleAttendModel.date) / 7) + 1;
                        LoggerHelper.d("curWeekIndex : " + curWeekIndex);

                        List<String> tempOkList = null;
                        List<String> tempNoList = null;
                        List<String> tempReasonList = null;
                        List<String> tempExcutiveAttendList = null;

                        if (curWeekIndex == 1) {
                            tempOkList = okAttendList1;
                            tempNoList = noAttendList1;
                            tempReasonList = reasonAttendList1;
                            tempExcutiveAttendList = okExcutiveAttendList1;
                        } else if (curWeekIndex == 2) {
                            tempOkList = okAttendList2;
                            tempNoList = noAttendList2;
                            tempReasonList = reasonAttendList2;
                            tempExcutiveAttendList = okExcutiveAttendList2;
                        } else if (curWeekIndex == 3) {
                            tempOkList = okAttendList3;
                            tempNoList = noAttendList3;
                            tempReasonList = reasonAttendList3;
                            tempExcutiveAttendList = okExcutiveAttendList3;
                        } else if (curWeekIndex == 4) {
                            tempOkList = okAttendList4;
                            tempNoList = noAttendList4;
                            tempReasonList = reasonAttendList4;
                            tempExcutiveAttendList = okExcutiveAttendList4;
                        } else if (curWeekIndex == 5) {
                            tempOkList = okAttendList5;
                            tempNoList = noAttendList5;
                            tempReasonList = reasonAttendList5;
                            tempExcutiveAttendList = okExcutiveAttendList5;
                        }

                        if (weekAttendDataListMap.get(eleAttendModel.date) != null && eleAttendModel.date != null) {
                            tempAttendDataModel = weekAttendDataListMap.get(eleAttendModel.date);
                        }

                        //데이터가 없으면 새로 생성한다.
                        else {
                            tempAttendDataModel = new AttendDataModel();
                        }

                        Integer okCnt = 0;
                        Integer noCnt = 0;

                        if (okDateMaps.get(eleAttendModel.date) != null) {
                            okCnt = okDateMaps.get(eleAttendModel.date);
                        }

                        if (noDateMaps.get(eleAttendModel.date) != null) {
                            noCnt = noDateMaps.get(eleAttendModel.date);
                        }

                        try {
                            if (eleAttendModel.attend.equals("true")) {
                                okDateMaps.put(eleAttendModel.date, okCnt + 1);
                                noDateMaps.put(eleAttendModel.date, noCnt);

                                if (xAxisNum > 0) {

                                    try {
                                        if (eleAttendModel.isExecutives.equals("임원")) {
                                            tempExcutiveAttendList.add(eleAttendModel.memberName);
                                        } else {
                                            tempOkList.add(eleAttendModel.memberName);
                                        }
                                    } catch (Exception e) {
                                        tempOkList.add(eleAttendModel.memberName);
                                    }

                                }
                            } else {
                                noDateMaps.put(eleAttendModel.date, noCnt + 1);
                                okDateMaps.put(eleAttendModel.date, okCnt);

                                if (xAxisNum > 0) {

                                    tempNoList.add(eleAttendModel.memberName);
                                    if (eleAttendModel.noAttendReason != null && !Common.trim(eleAttendModel.noAttendReason).equals(""))
                                        tempReasonList.add(eleAttendModel.memberName + " : " + eleAttendModel.noAttendReason);
                                }
                            }
                        }catch (Exception e){
                            noDateMaps.put(eleAttendModel.date, noCnt + 1);
                            okDateMaps.put(eleAttendModel.date, okCnt);

                            if (xAxisNum > 0) {

                                tempNoList.add(eleAttendModel.memberName);
                                if (eleAttendModel.noAttendReason != null && !Common.trim(eleAttendModel.noAttendReason).equals(""))
                                    tempReasonList.add(eleAttendModel.memberName + " : " + eleAttendModel.noAttendReason);
                            }
                        }

                        tempAttendDataModel.setReasonList(tempReasonList);
                        tempAttendDataModel.setOkAttendList(tempOkList);
                        tempAttendDataModel.setNoAttendList(tempNoList);
                        tempAttendDataModel.setOkExcutiveAttendList(tempExcutiveAttendList);
                        tempAttendDataModel.setCurDays(Integer.parseInt(eleAttendModel.date));
                        weekAttendDataListMap.put(eleAttendModel.date, tempAttendDataModel);
                    }
                }
            }
        }

        HashMap<String, String> mentMap = getSortList(okDateMaps);
        String type1 = "출석";
        String mentStr = getMakeMent(mentMap, type1);

        attendDataModel.setMent(mentStr);
        attendDataModel.setCurMonth(selectMonth);
        attendDataModel.setCurYear(selectYear);
        attendDataModel.setDayOfWeek(selectDayOfWeek);
        attendDataModel.setCurDays(selectDays);
        //attendDataModel.setWeekAttendDataListMap(weekAttendDataListMap);
        //attendDataModel.setOkAttendList(okAttendList);
        //attendDataModel.setNoAttendList(noAttendList);
        attendDataModel.setBarData(generateDataBar());
        attendDataModel.setReasonList(reasonList);
        onCompleteListener.onComplete(attendDataModel);
    }

    private String getMakeMent(HashMap<String, String> mentMap, String type) {

        if (mentMap.get("sumNum") == null) mentMap.put("sumNum", "-");
        if (mentMap.get("evgNum") == null) mentMap.put("evgNum", "-");
        if (mentMap.get("fst_date") == null) mentMap.put("fst_date", "-");
        if (mentMap.get("last_date") == null) mentMap.put("last_date", "-");
        if (mentMap.get("last") == null) mentMap.put("last", "-");
        if (mentMap.get("fst") == null) mentMap.put("fst", "-");

        String growthCnt = "-";
        double lastCnt;
        double avgCnt;
        if (!mentMap.get("last").equals("-") && !mentMap.get("last").equals("-")) {
            lastCnt = Double.parseDouble(mentMap.get("last"));
            avgCnt = Double.parseDouble(mentMap.get("evgNum"));
            if (lastCnt > 0 && avgCnt > 0) {
                double maxCnt = Double.parseDouble(mentMap.get("last"));
                double evgCnt = Double.parseDouble(mentMap.get("evgNum"));
                growthCnt = String.valueOf((maxCnt - evgCnt));
                attendDataModel.setGrowthCnt(Double.parseDouble(growthCnt));
            }
        }

        attendDataModel.setAvg(mentMap.get("evgNum"));

        int cSum = curDayOkCnt + curDaynoCnt;

        double okRate = 0;
        double sum = (curDayOkCnt + curDaynoCnt);

        if (curDayOkCnt != 0 && curDaynoCnt != 0) {
            okRate = (double) (curDayOkCnt / sum) * 100;
        } else if (curDaynoCnt != 0 && curDayOkCnt == 0) {
            okRate = 0;
        }
        okRate = Double.parseDouble(new DecimalFormat("##.#").format(okRate));

        String title = "[" + CommonData.getGroupModel().name + " " +
                SortMapUtil.getInteger(CommonData.getTeamModel().name) + " : " +
                CommonData.getTeamModel().etc + "] " +

                selectMonth + "월" +
                selectDay + "일 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(selectDayOfWeek)) + ") " +
                CalendarUtils.getDaysTime(Integer.valueOf(selectDays)) + " 출석";

        String ment = selectMonth + "월 " + type + "현황입니다. <br>" +
                "전체 " + type + "은 <strong>" + mentMap.get("sumNum") + " 명</strong>,  " +
                "전체 평균은 <strong>" + mentMap.get("evgNum") + " 명</strong>입니다. <br>" +
                type + "이 높은 날은 <strong>" + mentMap.get("fst_date") + "일 " + mentMap.get("fst") + "명</strong>, <br>" +
                type + "이 낮은 날은 <strong>" + mentMap.get("last_date") + "일 " + mentMap.get("last") + "명</strong> 입니다. <br>";

        return ment;
    }

    private void getDayMakeMent(AttendDataModel attendDataModel) {
        if (attendDataModel.getOkAttendList() == null || attendDataModel.getNoAttendList() == null) {
            if (attendDataModel.getOkAttendList().size() < 0 || attendDataModel.getNoAttendList().size() < 0) {
                attendDataModel.setPopMent(CommonString.INFO_TITLE_DONT_ATTEND_DATA);
                attendDataModel.setTxtypeMent(CommonString.INFO_TITLE_DONT_ATTEND_DATA);
                return;
            }
        }
        //selectDay;
        int selectDayOkCnt = attendDataModel.getOkAttendList().size() + attendDataModel.getOkExcutiveAttendList().size();
        int selectDayNoCnt = attendDataModel.getNoAttendList().size();

        int cSum = selectDayOkCnt + selectDayNoCnt;

        double okRate = 0;
        double sum = (selectDayOkCnt + selectDayNoCnt);

        if (selectDayOkCnt != 0 && selectDayOkCnt != 0) {
            okRate = (double) (selectDayOkCnt / sum) * 100;
        } else if (selectDayNoCnt != 0 && selectDayOkCnt == 0) {
            okRate = 0;
        }

        okRate = Double.parseDouble(new DecimalFormat("##.#").format(okRate));

        String title = "[" + CommonData.getGroupModel().name + " " +
                SortMapUtil.getInteger(CommonData.getTeamModel().name) + " : " +
                CommonData.getTeamModel().etc + "] " +
                selectMonth + "월" +
                selectDay + "일 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(selectDayOfWeek)) + ") " +
                CalendarUtils.getDaysTime(Integer.valueOf(selectDays)) + " 출석";

        String popTxt = "<center><strong> " + title + "</strong></center>" +
                "<br><br>" + "<strong>총 원 : " + cSum + "명<br>" +
                "출 석 : " + selectDayOkCnt + "명 / 결 석 : " + selectDayNoCnt + "명<br>" + "<br></strong>" +
                "<strong>* 출석률 : </strong>" + okRate + " % 입니다." + "<br><br>" +
                "* 임원출석 명단<br>" +
                attendDataModel.getOkExcutiveAttendList() + "<br><br>" +

                "* 성도/회원출석 명단<br>" +
                attendDataModel.getOkAttendList() + "<br><br>" +

                "* 결석 명단 <br>" +
                attendDataModel.getNoAttendList() + "<br><br> " +
                "* 결석 사유 <br>" +
                attendDataModel.getReasonList();

        String mentTxt = title +
                "\n\n" + "총 원 : " + cSum + "명\n" +
                "출 석 : " + selectDayOkCnt + "명 / 결 석 : " + selectDayNoCnt + "명\n\n" +
                "* 출석률 : " + okRate + " % 입니다." + "\n\n" +
                "* 임원출석 명단 \n" +
                attendDataModel.getOkExcutiveAttendList() + "\n\n" +
                "* 성도/회원출석 명단 \n" +
                attendDataModel.getOkAttendList() + "\n\n" +
                "* 결석 명단 \n" +
                attendDataModel.getNoAttendList() + "\n\n" +
                "* 결석 사유 \n" +
                attendDataModel.getReasonList();

        attendDataModel.setPopMent(popTxt);
        attendDataModel.setTxtypeMent(mentTxt);
    }


    private HashMap getSortList(HashMap<String, Integer> dateMaps) {

        LoggerHelper.i("GroupBriefingFragment getSortList");

        HashMap<String, String> mentMap = new HashMap<>();
        ArrayList<String> tempDateList = new ArrayList<>();
        ArrayList<String> tempCntList = new ArrayList<>();
        TreeMap<String, Integer> tm = new TreeMap<>(dateMaps);
        //Iterator<String> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        Iterator iteratorValue = Common.sortByValue(tm).iterator();

        int cnt = 0;
        int evgCnt = 0;
        int sumNum = 0;
        float evgNum = 0;
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
            LoggerHelper.d("e.getMessage() ::::::::::: + " + e.getMessage());
        }
        return mentMap;
    }

    private int sumOkCnt = 0;

    private BarData generateDataBar() {
        ArrayList<BarEntry> okEntries = new ArrayList<>();
        Integer dCnt = 0;
        sumOkCnt = 0;
        for (String eleWeekOfDay : xAxisArr) {
            if (okDateMaps.get(eleWeekOfDay) != null) {
                okEntries.add(new BarEntry(dCnt, okDateMaps.get(eleWeekOfDay)));
                sumOkCnt += okDateMaps.get(eleWeekOfDay);
            }
            dCnt++;
        }

        LoggerHelper.d("generateDataBar", "sumOkCnt : " + sumOkCnt);
        return getBarData(okEntries, Common.VORDIPLOM_COLORS, selectMonth + " 월 출석");
    }

    @NonNull
    private BarData getBarData(ArrayList<BarEntry> entries, int[] colorset, String dataName) {
        BarDataSet d = new BarDataSet(entries, dataName);

        d.setColors(colorset);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);


        return cd;
    }
}
