package com.buel.holyhelper.view.viewPage.briefingPageViewFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelper.R
import com.buel.holyhelper.data.AnalMode
import com.buel.holyhelper.data.CommonData
import com.buel.holyhelper.data.CommonString
import com.buel.holyhelper.model.BarChartModel
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.utils.BarChartDataUtils
import com.buel.holyhelper.view.recyclerView.barChartRecyclerView.BarChartRecyclerViewAdapter
import com.buel.holyhelper.view.recyclerView.barChartRecyclerView.BarChartRecyclerViewHolder
import com.commonLib.MaterialDailogUtil
import com.orhanobut.logger.LoggerHelper
import java.util.*

/**
 * Created by blue7 on 2018-05-16.
 */
class YearBriefingRecyclerFragment : AbstBriefingFragment() {

    lateinit internal var dataArrayList: ArrayList<BarChartModel>
    lateinit internal var recyclerView: RecyclerView
    lateinit internal var holderAdapter: RecyclerView.Adapter<BarChartRecyclerViewHolder>
    lateinit internal var rootView: View
    lateinit internal var tvTitle: TextView
    lateinit internal var monthEvgList: Array<String?>
    lateinit internal var prevYearMonthEvgList: Array<String?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataArrayList = ArrayList()
        rootView = inflater.inflate(R.layout.briefing_step2_view_page_fragment, container, false)

        tvTitle = rootView.findViewById(R.id.corps_step2_view_page_tv_title)

        if (CommonData.getGroupUid() != null && CommonData.getTeamModel() != null) {

            val title = "* 선택하신 [" + CommonData.getGroupModel().name + "] " + CommonString.GROUP_NICK + "의 월간 출석 통계   [ + 상세설명 ]"

            tvTitle.text = title

            val message = "<br>" +
                    "<strong>월간 출석 통계는 선택하신 [" + CommonData.getGroupModel().name + "] " + CommonString.GROUP_NICK + "의부서의 각 소그룹의 전체 출석수을 통합한 수치이고,</strong>" + "<br><br>" +
                    "<strong> [" + CommonData.getGroupModel().name + "] 부서의 </strong>" + CommonData.getSelectedYear().toString() + "년도 각월의 출석 통계입니다." + "<br><br>" +
                    "<strong>[전월 대비] 란? </strong><BR>해당 월의 전 월과의 출석수의 차이를 표시" + "<br><br>" +
                    "<strong>[" + (CommonData.getSelectedYear() - 1).toString() + "년 동월 대비] 란?</strong> <BR>해당 월의 작년의 같은 월과의 출석수의 차이를 표시" + "<br><br>" +
                    "<strong>[ 돋보기 버튼] 을 클릭하면,</strong> <BR> 클릭한 월에 해당하는 상세 데이터 표시화면이로 이동합니다." + "<br>"

            tvTitle.setOnClickListener { view ->
                MaterialDailogUtil.noticeDialog(
                        view.context,
                        message,
                        "월간 통계란?", null!!)
            }
            getAttandData()
        } else {
            //SuperToastUtil.toastE(getContext(), "팀을 추가해주세요.");
            LoggerHelper.d("팀을 추가해주세요.")
        }
        return rootView
    }

    override fun getAttandData() {

        dataArrayList = ArrayList()
        var teams = ArrayList<HolyModel.groupModel.teamModel>()
        var group: HolyModel.groupModel? = null

        try {
            group = CommonData.getGroupModel()
        } catch (e: Exception) {
            Toast.makeText(context, "선택된 그룹 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }



        try {
            var teammap: MutableCollection<HolyModel.groupModel.teamModel> = CommonData.getTeamMap().values
            teams = teammap
                    .filter { it.groupUid == CommonData.getGroupModel().uid }
                    as ArrayList<HolyModel.groupModel.teamModel>
            //LoggerHelper.d(teams)

            //teams = SortMapUtil.getSortTeamList() as ArrayList<HolyModel.groupModel.teamModel>
        } catch (e: Exception) {
            //SuperToastUtil.toastE(getContext(), CommonString.TEAM_NICK + " 을/를 추가해주세요.");
            LoggerHelper.e(CommonString.TEAM_NICK + " 을/를 추가해주세요.")
            return
        }

        val cnt = 0
        val cal = Calendar.getInstance()
        //현재 년도, 월, 일

        val cYear = cal.get(Calendar.YEAR)
        var cMonth = cal.get(Calendar.MONTH) + 1
        val cSelectedMonth = CommonData.getSelectedMonth()
        val viewMonth = cMonth
        if (cYear > CommonData.getSelectedYear()) {
            cMonth = 12
        }

        //tvTitle.setText("");
        monthEvgList = arrayOfNulls(viewMonth)
        prevYearMonthEvgList = arrayOfNulls(12)

        var evgSum = 0.0
        var monthEvg = 0.0
        for (i in -12 until viewMonth) {
            CommonData.setSelectedMonth(i + 1)
            val barChartModel: BarChartModel
            //LoggerHelper.d("CommonData.getAnalMode()  : " + CommonData.getAnalMode())
            if (CommonData.getAnalMode() == AnalMode.GROP_MODE) {
                barChartModel = BarChartDataUtils.getAttandData(null!!, true)
            } else {
                barChartModel = BarChartDataUtils.getAttandData(CommonData.getTeamModel(), false)
            }
            if (barChartModel.evg === "-" || barChartModel.evg === "") {
                barChartModel.evg = "0"
            }

            if (i >= 0) {
                dataArrayList.add(barChartModel)
                evgSum += java.lang.Double.parseDouble(barChartModel.evg)

                monthEvgList[i] = barChartModel.evg
            } else {
                prevYearMonthEvgList[12 + i] = barChartModel.evg
            }

            if (i > 0) {

                barChartModel.prevDiff = String.format("%.2f", java.lang.Double.parseDouble(monthEvgList[i]) - java.lang.Double.parseDouble(monthEvgList[i - 1]))
                barChartModel.prevYearDiff = String.format("%.2f", java.lang.Double.parseDouble(monthEvgList[i]) - java.lang.Double.parseDouble(prevYearMonthEvgList[i]))
            } else if (i == 0) {
                barChartModel.prevDiff = String.format("%.2f", java.lang.Double.parseDouble(monthEvgList[i]) - java.lang.Double.parseDouble(prevYearMonthEvgList[11]))
                barChartModel.prevYearDiff = String.format("%.2f", java.lang.Double.parseDouble(monthEvgList[i]) - java.lang.Double.parseDouble(prevYearMonthEvgList[i]))
            }
        }

        monthEvg = evgSum / viewMonth

        CommonData.setSelectedMonth(cSelectedMonth)
        setPage()

    }

    override fun onResume() {
       // LoggerHelper.d("onResume", "onResume")
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
