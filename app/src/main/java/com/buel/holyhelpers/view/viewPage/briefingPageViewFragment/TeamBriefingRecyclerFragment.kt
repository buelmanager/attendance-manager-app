package com.buel.holyhelpers.view.viewPage.briefingPageViewFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.AnalMode
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.model.BarChartModel
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.utils.BarChartDataUtils
import com.buel.holyhelpers.utils.SortMapUtil
import com.buel.holyhelpers.view.recyclerView.barChartRecyclerView.BarChartRecyclerViewAdapter
import com.buel.holyhelpers.view.recyclerView.barChartRecyclerView.BarChartRecyclerViewHolder
import com.orhanobut.logger.LoggerHelper
import java.util.*

/**
 * Created by blue7 on 2018-05-16.
 */
class TeamBriefingRecyclerFragment : AbstBriefingFragment() {

    lateinit internal var dataArrayList: ArrayList<BarChartModel>
    lateinit internal var recyclerView: RecyclerView
    lateinit internal var holderAdapter: RecyclerView.Adapter<BarChartRecyclerViewHolder>
    lateinit internal var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataArrayList = ArrayList()
        rootView = inflater.inflate(R.layout.briefing_step2_view_page_fragment, container, false)

        val tvTitle = rootView.findViewById<TextView>(R.id.corps_step2_view_page_tv_title)

        if (CommonData.getGroupUid() != null && CommonData.getTeamModel() != null) {
            tvTitle.text = "* 현재 설정된 [ " + CommonData.getGroupModel().name + "] 의 각 " + CommonString.TEAM_NICK + "별 출석을 나타낸 통계입니다."
            getAttandData()
        } else {
            //SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.e("팀을 추가해주세요.")
        }


        return rootView
    }

    override fun getAttandData() {

        dataArrayList = ArrayList()
        var teams = ArrayList<HolyModel.groupModel.teamModel>()
        var groups = ArrayList<HolyModel.groupModel>()
        var group: HolyModel.groupModel? = null

        try {
            group = CommonData.getGroupModel()
        } catch (e: Exception) {
            Toast.makeText(context, CommonString.INFO_TITLE_SELECTL_GROUP, Toast.LENGTH_SHORT).show()
        }

        if (CommonData.getTeamModel() == null || CommonData.getGroupModel() == null) {
            //SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.d("팀을 추가해주세요.")
            return
        }

        try {
            var teammap: MutableCollection<HolyModel.groupModel.teamModel> = CommonData.getTeamMap().values
            teams = teammap
                    .filter { it.groupUid == CommonData.getGroupModel().uid }
                    as ArrayList<HolyModel.groupModel.teamModel>
            //LoggerHelper.d(teams)

            //LoggerHelper.d("teams : $teams")

            groups = SortMapUtil.getSortGroupList(CommonData.getHolyModel().group) as ArrayList<HolyModel.groupModel>
            //LoggerHelper.d("groups : $groups")
        } catch (e: Exception) {
            //SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.d("팀을 추가해주세요.")
            return
        }

        var cnt = 0

        if (CommonData.getAnalMode() == AnalMode.GROP_MODE) {
            for (eleGroup in groups) {
                val teamUID = eleGroup.uid
                val barChartModel = BarChartDataUtils.getAttandData(null!!, true)
                dataArrayList.add(barChartModel)
                cnt++
                if (cnt >= teams.size) {
                    setPage()
                }
            }
        } else {
            for (eleTeam in teams) {
                val teamUID = eleTeam.uid
                val barChartModel = BarChartDataUtils.getAttandData(eleTeam, false)
                dataArrayList.add(barChartModel)
                cnt++
                if (cnt >= teams.size) {
                    setPage()
                }
            }
        }


    }

    override fun onResume() {
        //LoggerHelper.d("onResume", "onResume")
        super.onResume()
    }

    @SuppressLint("LongLogTag")
    private fun setPage() {

        //LoggerHelper.d("setPage")

        recyclerView = rootView.findViewById(R.id.briefing_step2_fragment_rv)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        holderAdapter = BarChartRecyclerViewAdapter(dataArrayList, View.OnClickListener { })

        holderAdapter.notifyDataSetChanged()
        (holderAdapter as BarChartRecyclerViewAdapter).setItemArrayList(dataArrayList)
        recyclerView.refreshDrawableState()

        holderAdapter = BarChartRecyclerViewAdapter(dataArrayList, View.OnClickListener { })

        recyclerView.adapter = holderAdapter
    }
}
