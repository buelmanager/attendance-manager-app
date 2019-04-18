package com.buel.holyhelper.view.viewPage.briefingPageViewFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.buel.holyhelper.R;
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
public class GroupBriefingRecyclerFragment extends AbstBriefingFragment {

    ArrayList<BarChartModel> dataArrayList;
    RecyclerView recyclerView;
    RecyclerView.Adapter<BarChartRecyclerViewHolder> holderAdapter;
    View rootView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        if(isVisibleToUser)
        {
            if (CommonData.getGroupUid() != null &&
                    CommonData.getTeamUid() != null) {
                getAttandData();
            } else {
                SuperToastUtil.toastE(getContext(), "부서 또는 소그룹, 날짜 정보가 설정되지 않았습니다.");
            }
        }
        else
        {

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataArrayList = new ArrayList<>();
        LoggerHelper.d("TeamBriefingRecyclerFragment onCreateView" , "onCreateView");
        rootView = inflater.inflate(R.layout.briefing_step2_view_page_fragment, container, false);

        TextView tvTitle = rootView.findViewById(R.id.corps_step2_view_page_tv_title);
        tvTitle.setText("* [ " + CommonData.getHolyModel().name + " ] 의 " + " [ " + CommonData.getGroupModel().name + " ] 그룹 출석을 합산한 통계입니다.");

        if (CommonData.getGroupUid() != null &&
                CommonData.getTeamUid() != null) {
            getAttandData();
        } else {
            SuperToastUtil.toastE(getContext(), "부서 또는 소그룹, 날짜 정보가 설정되지 않았습니다.");
        }

        return rootView;
    }

    public void getAttandData() {

        dataArrayList= new ArrayList<>();
        ArrayList<HolyModel.groupModel.teamModel> teams = new ArrayList<>();
        HolyModel.groupModel group = null;

        try {
            group = CommonData.getGroupModel();
        } catch (Exception e) {
            Toast.makeText(getContext(), CommonString.INFO_TITLE_SELECTL_GROUP, Toast.LENGTH_SHORT).show();
        }

        try {
            LoggerHelper.d("GroupBriefingRecyclerFragment groupModel : " + group);
            LoggerHelper.d("GroupBriefingRecyclerFragment groupModel uid : " + group.uid);
            LoggerHelper.d("GroupBriefingRecyclerFragment groupModel name : " + group.name);
            LoggerHelper.d(group.team);
            teams = (ArrayList<HolyModel.groupModel.teamModel>) SortMapUtil.getSortTeamList();
        }catch (Exception e)
        {
            SuperToastUtil.toastE(getContext(), CommonString.TEAM_NICK + " 을/를 추가해주세요.");
            LoggerHelper.d(CommonString.TEAM_NICK + " 을/를 추가해주세요.");
            return;
        }

        int cnt = 0;
        for( HolyModel.groupModel.teamModel eleTeam : teams ){
            String teamUID = eleTeam.uid;


            BarChartModel barChartModel = BarChartDataUtils.getAttandData(eleTeam,false );
            dataArrayList.add(barChartModel);
            cnt++;
            LoggerHelper.d("cnt : " + cnt + " / " + "teams.size() : " + teams.size());
            if( cnt >= teams.size() )
            {
                setPage();
            }
        }
    }

    @Override
    public void onResume() {
        LoggerHelper.d("onResume" , "onResume");
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
        recyclerView.setAdapter(holderAdapter);
    }
}
