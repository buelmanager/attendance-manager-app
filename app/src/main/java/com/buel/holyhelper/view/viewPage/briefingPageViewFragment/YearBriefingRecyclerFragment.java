package com.buel.holyhelper.view.viewPage.briefingPageViewFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AnalMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.model.BarChartModel;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.BarChartDataUtils;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.recyclerView.barChartRecyclerView.BarChartRecyclerViewAdapter;
import com.buel.holyhelper.view.recyclerView.barChartRecyclerView.BarChartRecyclerViewHolder;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by blue7 on 2018-05-16.
 */
public class YearBriefingRecyclerFragment extends AbstBriefingFragment {

    ArrayList<BarChartModel> dataArrayList;
    RecyclerView recyclerView;
    RecyclerView.Adapter<BarChartRecyclerViewHolder> holderAdapter;
    View rootView;
    TextView tvTitle;
    String[] monthEvgList;
    String[] prevYearMonthEvgList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataArrayList = new ArrayList<>();
        rootView = inflater.inflate(R.layout.briefing_step2_view_page_fragment, container, false);

        tvTitle = rootView.findViewById(R.id.corps_step2_view_page_tv_title);

        if (CommonData.getGroupUid() != null && CommonData.getTeamModel() != null) {

            String title = "* 선택하신 [" + CommonData.getGroupModel().name + "] " + CommonString.GROUP_NICK + "의 월간 출석 통계   [ + 상세설명 ]";

            tvTitle.setText(title);

            String message = "<br>" +
                    "<strong>월간 출석 통계는 선택하신 [" + CommonData.getGroupModel().name + "] " + CommonString.GROUP_NICK + "의부서의 각 소그룹의 전체 출석수을 통합한 수치이고,</strong>" + "<br><br>" +
                    "<strong> [" + CommonData.getGroupModel().name + "] 부서의 </strong>" + String.valueOf(CommonData.getSelectedYear())+ "년도 각월의 출석 통계입니다." + "<br><br>" +
                    "<strong>[전월 대비] 란? </strong><BR>해당 월의 전 월과의 출석수의 차이를 표시" + "<br><br>" +
                    "<strong>[" + String.valueOf(CommonData.getSelectedYear() - 1) + "년 동월 대비] 란?</strong> <BR>해당 월의 작년의 같은 월과의 출석수의 차이를 표시" + "<br><br>" +
                    "<strong>[ 돋보기 버튼] 을 클릭하면,</strong> <BR> 클릭한 월에 해당하는 상세 데이터 표시화면이로 이동합니다." + "<br>";

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialDailogUtil.Companion.noticeDialog(
                            view.getContext(),
                            message,
                            "월간 통계란?",
                            null);
                }
            });
            getAttandData();
        }else{
            //SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.d("팀을 추가해주세요.");
        }
        return rootView;
    }

    public void getAttandData() {

        dataArrayList = new ArrayList<>();
        ArrayList<HolyModel.groupModel.teamModel> teams = new ArrayList<>();
        HolyModel.groupModel group = null;

        try {
            group = CommonData.getGroupModel();
        } catch (Exception e) {
            Toast.makeText(getContext(), "선택된 그룹 정보가 없습니다.", Toast.LENGTH_SHORT).show();
        }

        try {
            teams = (ArrayList<HolyModel.groupModel.teamModel>) SortMapUtil.getSortTeamList();
        } catch (Exception e) {
            //SuperToastUtil.toastE(getContext(), CommonString.TEAM_NICK + " 을/를 추가해주세요.");
            LoggerHelper.e(CommonString.TEAM_NICK + " 을/를 추가해주세요.");
            return;
        }

        int cnt = 0;
        Calendar cal = Calendar.getInstance();
        //현재 년도, 월, 일

        int cYear = cal.get(cal.YEAR);
        int cMonth = cal.get(cal.MONTH) + 1;
        int cSelectedMonth = CommonData.getSelectedMonth();
        int viewMonth = cMonth;
        if (cYear > CommonData.getSelectedYear()) {
            cMonth = 12;
        }

        //tvTitle.setText("");
        monthEvgList = new String[viewMonth];
        prevYearMonthEvgList = new String[12];

        double evgSum = 0;
        double monthEvg = 0;
        for (int i = -12; i < viewMonth; i++) {
            CommonData.setSelectedMonth(i + 1);
            BarChartModel barChartModel;
            LoggerHelper.d("CommonData.getAnalMode()  : " + CommonData.getAnalMode() );
            if(CommonData.getAnalMode() == AnalMode.GROP_MODE){
                barChartModel = BarChartDataUtils.getAttandData(null, true);
            }else{
                barChartModel = BarChartDataUtils.getAttandData(CommonData.getTeamModel(), false);
            }
            if (barChartModel.evg == "-" || barChartModel.evg == "") {
                barChartModel.evg = "0";
            }

            if (i >= 0) {
                dataArrayList.add(barChartModel);
                evgSum += Double.parseDouble(barChartModel.evg);

                monthEvgList[i] = (barChartModel.evg);
            } else {
                prevYearMonthEvgList[12 + i] = (barChartModel.evg);
            }

            if (i > 0) {

                barChartModel.prevDiff = String.format("%.2f", Double.parseDouble(monthEvgList[i]) - Double.parseDouble(monthEvgList[i - 1]));
                barChartModel.prevYearDiff = String.format("%.2f", Double.parseDouble(monthEvgList[i]) - Double.parseDouble(prevYearMonthEvgList[i]));
            } else if (i == 0) {
                barChartModel.prevDiff = String.format("%.2f", Double.parseDouble(monthEvgList[i]) - Double.parseDouble(prevYearMonthEvgList[11]));
                barChartModel.prevYearDiff = String.format("%.2f", Double.parseDouble(monthEvgList[i]) - Double.parseDouble(prevYearMonthEvgList[i]));
            }
        }

        monthEvg = evgSum / viewMonth;

        CommonData.setSelectedMonth(cSelectedMonth);
        setPage();

    }

    @Override
    public void onResume() {
        LoggerHelper.d("onResume", "onResume");
        super.onResume();
    }

    @SuppressLint("LongLogTag")
    private void setPage() {

        LoggerHelper.d("setPage");

        recyclerView = rootView.findViewById(R.id.briefing_step2_fragment_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        holderAdapter = new BarChartRecyclerViewAdapter(dataArrayList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holderAdapter.notifyDataSetChanged();
        ((BarChartRecyclerViewAdapter) holderAdapter).setItemArrayList(dataArrayList);
        recyclerView.refreshDrawableState();

        holderAdapter = new BarChartRecyclerViewAdapter(dataArrayList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        recyclerView.setAdapter(holderAdapter);
    }
}
