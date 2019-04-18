package com.buel.holyhelper.view.viewPage.briefingPageViewFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.model.AttendModel;
import com.buel.holyhelper.model.DateModel;
import com.buel.holyhelper.utils.CalendarUtils;
import com.buel.holyhelper.view.MakeBarChartView;
import com.buel.holyhelper.view.MakePieChartView;
import com.commonLib.Common;
import com.commonLib.SuperToastUtil;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.orhanobut.logger.LoggerHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

/**
 * Created by blue7 on 2018-05-16.
 */
public class GroupBriefingFragment extends AbstBriefingFragment {

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private LinearLayout linearLayout;
    private NestedScrollView scrollView;
    private TextView tvTitle;

    private ArrayList<AttendModel> attendModels = new ArrayList<>();
    private HashMap<String, Integer> okDateMaps;
    private HashMap<String, Integer> noDateMaps;

    private String[] xAxisArr;
    private AttendModel attendModel = null;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.corps_step1_view_page_fragment, container, false);
        LoggerHelper.i("GroupBriefingFragment onCreateView");

        this.rootView = rootView;

        mTfRegular = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");

        linearLayout = rootView.findViewById(R.id.corps_step1_view_page_fragment_ll);
        scrollView = rootView.findViewById(R.id.corps_step1_view_page_fragment_sv);
        tvTitle = rootView.findViewById(R.id.corps_step1_view_page_tv_title);

        if (CommonData.getGroupUid() != null &&
                CommonData.getTeamUid() != null) {
            tvTitle.setText("* [ " + CommonData.getHolyModel().name + " ] 의 " + " [ " + CommonData.getGroupModel().name + " ] 그룹 출석을 합산한 통계입니다.");
            getAttandData();
        } else {
            SuperToastUtil.toastE(getContext(), CommonString.GROUP_NICK + " 또는 " + CommonString.TEAM_NICK + ", 날짜 정보가 설정되지 않았습니다.");
        }
        return rootView;
    }

    private Context context;

    @SuppressLint("SetTextI18n")
    private void setPage() {

        if (CommonData.getGroupModel() == null || CommonData.getTeamModel() == null) {
            SuperToastUtil.toastE(getContext(), CommonString.GROUP_NICK + " 또는 "+ CommonString.TEAM_NICK + " 정보가 설정되지 않았습니다.");
            return;
        }

        if (linearLayout.getClipChildren()) linearLayout.removeAllViews();

        context = rootView.getContext();
        View convertView3 = LayoutInflater.from(context).inflate(
                R.layout.viewpager_title, null);
        TextView titleTv3 = (TextView) convertView3.findViewById(R.id.view_pager_tv_title);
        titleTv3.setTextSize(20);

        titleTv3.setText(Html.fromHtml(
                CommonData.getCurrentFullDateStr() + "  " +
                        "<strong>월간 통계</strong>"));

        linearLayout.addView(convertView3);

        View convertView = LayoutInflater.from(context).inflate(R.layout.viewpager_title, null);
        TextView titleTv = (TextView) convertView.findViewById(R.id.view_pager_tv_title);

        titleTv.setText(Html.fromHtml("<strong> [ " + CommonData.getGroupModel().name + " ]</strong> 출석통계"));
        linearLayout.addView(convertView);

        HashMap<String, String> mentMap = getSortList(okDateMaps);
        String type1 = "출석";
        String mentStr = getBarMakeMent(mentMap, type1);

        MakeBarChartView chart = new MakeBarChartView(generateDataBar(), context, xAxisArr, mentStr, null);
        View chartView = chart.getConvertView();
        linearLayout.addView(chartView);

        View convertView2 = LayoutInflater.from(context).inflate(
                R.layout.viewpager_title, null);
        TextView titleTv2 = (TextView) convertView2.findViewById(R.id.view_pager_tv_title);
        titleTv2.setText(Html.fromHtml("<strong> [ " + CommonData.getGroupModel().name + " ]</strong> 결석통계"));
        linearLayout.addView(convertView2);

        HashMap<String, String> mentMap2 = getSortList(noDateMaps);
        String type2 = "결석";
        String mentStr2 = getBarMakeMent(mentMap2, type2);

        MakeBarChartView chart2 = new MakeBarChartView(generateNoAttendDataBar(), context, xAxisArr, mentStr2 ,null);
        View chartView2 = chart2.getConvertView();
        linearLayout.addView(chartView2);

        PieData pieData = generateDataPie();
        String pieMent = getPieMakeMent(sumOkCnt, sumNoCnt, CommonData.getGroupModel().name + " 월간 전체 출결");
        MakePieChartView chart3 = new MakePieChartView(pieData, context, "[ " + CommonData.getGroupModel().name + " ] " +
                "월간 전체 출결", pieMent, "출결현황");
        View chartView3 = chart3.getConvertView();
        linearLayout.addView(chartView3);
    }

    @NonNull
    private String getBarMakeMent(HashMap<String, String> mentMap, String type) {

        if (mentMap.get("sumNum") == null) mentMap.put("sumNum", "-");
        if (mentMap.get("evgNum") == null) mentMap.put("evgNum", "-");
        if (mentMap.get("fst_date") == null) mentMap.put("fst_date", "-");
        if (mentMap.get("last_date") == null) mentMap.put("last_date", "-");
        if (mentMap.get("last") == null) mentMap.put("last", "-");
        if (mentMap.get("fst") == null) mentMap.put("fst", "-");

        String ment = CommonData.getSelectedMonth()+ "월 " + type + "현황입니다. <br>" +
                "전체 " + type + "은 <strong>" + mentMap.get("sumNum") + " 명</strong>,  " +
                "전체 평균은 <strong>" + mentMap.get("evgNum") + " 명</strong>입니다. <br>" +
                type + "이 높은 날은 <strong>" + mentMap.get("fst_date") + "일 " + mentMap.get("fst") + "명</strong>, <br>" +
                type + "이 낮은 날은 <strong>" + mentMap.get("last_date") + "일 " + mentMap.get("last") + "명</strong> 입니다. <br>";
        return ment;
    }

    @NonNull
    private String getPieMakeMent(double okCnt, double noCnt, String type) {

        double okRate = 0;
        double noRate = 0;
        double sum = (okCnt + noCnt);

        if (okCnt != 0 && noCnt != 0) {
            okRate = (double) (okCnt / sum)*100;
            noRate = (double) (noCnt / sum)*100;
        } else if (okCnt == 0 && noCnt != 0) {
            noRate = 100;
        } else if (noCnt != 0 && okCnt == 0) {
            okRate = 100;
        }

        noRate = Double.parseDouble(new DecimalFormat("##.#").format(noRate));
        okRate = Double.parseDouble(new DecimalFormat("##.#").format(okRate));

        /*LoggerHelper.d("ok : " + okCnt);
        LoggerHelper.d("no : " + noCnt);
        LoggerHelper.d("sum: " + sum);
        LoggerHelper.d("okRate: " + okRate);
        LoggerHelper.d("noRate: " + noRate);*/

        String ment = CommonData.getSelectedMonth() + "월 " + type + "현황입니다. <br>" +
                "전체 출석은 <strong>" + okCnt + " 명</strong>,  " +
                "전체 출석평균은 <strong>" + okRate + " %</strong>입니다. <br>" +
                "전체 결석은 <strong>" + noCnt + " 명</strong> 입니다.  " +
                "전체 결석평균은 <strong>" + noRate + " %</strong>입니다. <br>";
        return ment;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry((float) (sumOkCnt), "전체 출석"));
        entries.add(new PieEntry((float) (sumNoCnt), "전체 결석"));


        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);


        d.setColors(Common.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateNoAttendDataBar() {

        ArrayList<BarEntry> noEntries = new ArrayList<>();
        Integer dCnt = 0;
        sumNoCnt = 0;

        for (String eleWeekOfDay : xAxisArr) {
            if (noDateMaps.get(eleWeekOfDay) != null) {
                noEntries.add(new BarEntry(dCnt, noDateMaps.get(eleWeekOfDay)));
                sumNoCnt += noDateMaps.get(eleWeekOfDay);
            }
            dCnt++;
        }

        return getBarData(noEntries, Common.VORDIPLOM_RED, CommonData.getSelectedMonth() + " 월 결석");
    }

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

        //LoggerHelper.d("generateDataBar", "sumOkCnt : " + sumOkCnt);

        return getBarData(okEntries, Common.VORDIPLOM_BLUE, CommonData.getSelectedMonth() + " 월 출석");
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


    public void getAttandData() {
        okDateMaps = new HashMap<>();
        noDateMaps = new HashMap<>();
        attendModel = new AttendModel();
        getAttendRate();
        setPage();
    }

    private void getAttendRate() {

        xAxisArr = CalendarUtils.getWeekendsDate(
                CommonData.getSelectedYear(),
                CommonData.getSelectedMonth(),
                CommonData.getSelectedDayOfWeek());

        HashMap<String, DateModel> dateModelHashMap = CommonData.getAttendDateMaps();

        if (dateModelHashMap == null) {
            LoggerHelper.e("출석데이터가 올바르지 않습니다.");
            return;
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

            LoggerHelper.d("mapkey : " + mapkey);

            if (dateModelHashMap.get(mapkey) == null) {
                okDateMaps.put(xAxisStr, 0);
                noDateMaps.put(xAxisStr, 0);
            } else {
                DateModel dateModel = dateModelHashMap.get(mapkey);
                HashMap<String, AttendModel> itemModelmap = dateModel.member;
                //member 데이터
                for (Map.Entry<String, AttendModel> elem : itemModelmap.entrySet()) {
                    //System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
                    attendModels.add(elem.getValue());

                    AttendModel eleAttendModel = elem.getValue();

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
                        } else {
                            noDateMaps.put(eleAttendModel.date, noCnt + 1);
                            okDateMaps.put(eleAttendModel.date, okCnt);
                        }
                    }catch (Exception e){
                        noDateMaps.put(eleAttendModel.date, noCnt + 1);
                        okDateMaps.put(eleAttendModel.date, okCnt);
                    }
                }
            }
        }
    }

    private int sumOkCnt = 0;
    private int sumNoCnt = 0;

    private HashMap getSortList(HashMap<String, Integer> dateMaps) {

        LoggerHelper.i("GroupBriefingFragment getSortList");

        HashMap<String, String> mentMap = new HashMap<>();
        ArrayList<String> tempDateList = new ArrayList<>();
        ArrayList<String> tempCntList = new ArrayList<>();
        TreeMap<String, Integer> tm = new TreeMap<>(dateMaps);
        //Iterator<String> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        Iterator iteratorValue = sortByValue(tm).iterator();

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
            //LoggerHelper.d("mentMap : " + mentMap);
            evgNum = sumNum / evgCnt;
            mentMap.put("evgNum", String.valueOf(evgNum));
        } catch (Exception e) {
            LoggerHelper.d(e.getMessage());
        }
        return mentMap;
    }

    public List sortByValue(final Map map) {

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
}
