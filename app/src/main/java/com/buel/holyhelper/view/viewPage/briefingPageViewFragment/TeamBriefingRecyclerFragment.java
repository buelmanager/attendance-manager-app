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
import com.commonLib.SuperToastUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by blue7 on 2018-05-16.
 */
public class TeamBriefingRecyclerFragment extends AbstBriefingFragment {

    ArrayList<BarChartModel> dataArrayList;
    RecyclerView recyclerView;
    RecyclerView.Adapter<BarChartRecyclerViewHolder> holderAdapter;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataArrayList = new ArrayList<>();
        rootView = inflater.inflate(R.layout.briefing_step2_view_page_fragment, container, false);

        TextView tvTitle = rootView.findViewById(R.id.corps_step2_view_page_tv_title);

        if (CommonData.getGroupUid() != null && CommonData.getTeamModel() != null) {
            tvTitle.setText("* 현재 설정된 [ " + CommonData.getGroupModel().name + "] 의 각 " + CommonString.TEAM_NICK + "별 출석을 나타낸 통계입니다.");
            getAttandData();
        }else {
            //SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.e("팀을 추가해주세요.");
        }


        return rootView;
    }

    public void getAttandData() {

        dataArrayList = new ArrayList<>();
        ArrayList<HolyModel.groupModel.teamModel> teams = new ArrayList<>();
        ArrayList<HolyModel.groupModel> groups = new ArrayList<>();
        HolyModel.groupModel group = null;

        try {
            group = CommonData.getGroupModel();
        } catch (Exception e) {
            Toast.makeText(getContext(), CommonString.INFO_TITLE_SELECTL_GROUP, Toast.LENGTH_SHORT).show();
        }

        if( CommonData.getTeamModel() == null || CommonData.getGroupModel() == null){
            SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.d("팀을 추가해주세요.");
            return;
        }

        try {
            teams = (ArrayList<HolyModel.groupModel.teamModel>) SortMapUtil.getSortTeamList();
            LoggerHelper.d("teams : " + teams);
            groups = (ArrayList<HolyModel.groupModel>) SortMapUtil.getSortGroupList(CommonData.getHolyModel().group);
            LoggerHelper.d("groups : " + groups);
        } catch (Exception e) {
            SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.d("팀을 추가해주세요.");
            return;
        }

        int cnt = 0;

        if(CommonData.getAnalMode() == AnalMode.GROP_MODE){
            for (HolyModel.groupModel eleGroup : groups) {
                String teamUID = eleGroup.uid;
                BarChartModel barChartModel = BarChartDataUtils.getAttandData(null, true);
                dataArrayList.add(barChartModel);
                cnt++;
                if (cnt >= teams.size()) {
                    setPage();
                }
            }
        }else{
            for (HolyModel.groupModel.teamModel eleTeam : teams) {
                String teamUID = eleTeam.uid;
                BarChartModel barChartModel = BarChartDataUtils.getAttandData(eleTeam, false);
                dataArrayList.add(barChartModel);
                cnt++;
                if (cnt >= teams.size()) {
                    setPage();
                }
            }
        }


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
