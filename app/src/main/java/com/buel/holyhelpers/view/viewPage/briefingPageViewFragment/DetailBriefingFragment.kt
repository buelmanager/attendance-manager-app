package com.buel.holyhelpers.view.viewPage.briefingPageViewFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.attendDateMaps
import com.buel.holyhelpers.data.CommonData.currentFullDateStr
import com.buel.holyhelpers.data.CommonData.getGroupUid
import com.buel.holyhelpers.data.CommonData.getTeamUid
import com.buel.holyhelpers.data.CommonData.groupModel
import com.buel.holyhelpers.data.CommonData.selectedDay
import com.buel.holyhelpers.data.CommonData.selectedDayOfWeek
import com.buel.holyhelpers.data.CommonData.selectedDays
import com.buel.holyhelpers.data.CommonData.selectedYear
import com.buel.holyhelpers.data.CommonData.teamModel
import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.model.AttendDataModel
import com.buel.holyhelpers.model.AttendModel
import com.buel.holyhelpers.model.ChartListenerModel
import com.buel.holyhelpers.utils.AttendDataManager
import com.buel.holyhelpers.utils.SortMapUtil.getInteger
import com.buel.holyhelpers.view.DataTypeListener
import com.buel.holyhelpers.view.MakeBarChartView
import com.commonLib.MaterialDailogUtil.Companion.shareKakaoMessage
import com.commonLib.SuperToastUtil
import com.orhanobut.logger.LoggerHelper
import java.util.*

/**
 * Created by blue7 on 2018-05-16.
 */
class DetailBriefingFragment : AbstBriefingFragment() {
    protected var mTfRegular: Typeface? = null
    protected var mTfLight: Typeface? = null
    private var linearLayout: LinearLayout? = null
    private var scrollView: NestedScrollView? = null
    private lateinit var tvTitle: TextView
    var rootView: View? = null
    var okDateMaps: HashMap<String, Int>? = null
    var noDateMaps: HashMap<String, Int>? = null
    var attendModel: AttendModel? = null
    var attendDataModel: AttendDataModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.corps_step1_view_page_fragment, container, false)
        LoggerHelper.i("GroupBriefingFragment onCreateView")
        this.rootView = rootView
        mTfRegular = Typeface.createFromAsset(getContext()!!.assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(getContext()!!.assets, "OpenSans-Light.ttf")
        linearLayout = rootView.findViewById(R.id.corps_step1_view_page_fragment_ll)
        scrollView = rootView.findViewById(R.id.corps_step1_view_page_fragment_sv)
        tvTitle = rootView.findViewById(R.id.corps_step1_view_page_tv_title)
        //LoggerHelper.d("CommonData.getGroupUid() : " + CommonData.getGroupUid());
//LoggerHelper.d("CommonData.getGroupUid() : " + CommonData.getTeamUid());
        if (getGroupUid() != null &&
                getTeamUid() != null) {
            getAttandData()
            tvTitle.setText("* 현재 설정된 [ " + groupModel.name + " " + getInteger(teamModel.name) + " : " + teamModel.etc + " ] 의 통계입니다.")
        } else {
            SuperToastUtil.toastE(getContext(), "부서 또는 소그룹, 날짜 정보가 설정되지 않았습니다.")
        }
        return rootView
    }

    lateinit var ctx:Context
    @SuppressLint("SetTextI18n")
    private fun setPage(attendDataModel: AttendDataModel) {
        this.attendDataModel = attendDataModel
        if (groupModel == null || teamModel == null) {
            SuperToastUtil.toastE(getContext(), CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP)
            return
        }
        if (linearLayout!!.clipChildren) linearLayout!!.removeAllViews()
        ctx = rootView!!.context
        val convertView3 = LayoutInflater.from(ctx).inflate(
                R.layout.viewpager_title, null)
        val titleTv3 = convertView3.findViewById<View>(R.id.view_pager_tv_title) as TextView
        titleTv3.textSize = 20f
        titleTv3.text = Html.fromHtml(
                currentFullDateStr + "  " +
                        "<strong>월간 통계</strong>")
        linearLayout!!.addView(convertView3)
        val convertView = LayoutInflater.from(ctx).inflate(R.layout.viewpager_title, null)
        val titleTv = convertView.findViewById<View>(R.id.view_pager_tv_title) as TextView
        titleTv.text = Html.fromHtml("<strong> [ " + " " + groupModel.name + " " + getInteger(teamModel.name) + " : " + teamModel.etc + " ]</strong> 출석통계")
        linearLayout!!.addView(convertView)
        val type1 = "출석"
        val mentStr = attendDataModel.ment
        LoggerHelper.s("attendDataModel", attendDataModel.barData.toString())
        val chart = MakeBarChartView(attendDataModel.barData!!, ctx, attendDataModel.getxAxisArr(), mentStr, object : DataTypeListener.OnCompleteListener<ChartListenerModel?> {
            /**
             * CHART 를 클릭했을 때 해당 값을 받아온다.
             * @param chartListenerModel
             */
            override fun onComplete(chartListenerModel: ChartListenerModel?) {
                if (chartListenerModel!!.xData == "-") return
                if (chartListenerModel!!.yData > 0) {
                    getCurDateAttendData(chartListenerModel.xData, type1)
                }
            }
        })
        val chartView = chart.convertView
        linearLayout!!.addView(chartView)
        convertView5 = LayoutInflater.from(ctx).inflate(R.layout.viewpager_content, null)
        contentTv = convertView5.findViewById<View>(R.id.view_pager_tv_content) as TextView
        contentTitletv = convertView5.findViewById<View>(R.id.view_pager_tv_title) as TextView
        shareIV = convertView5.findViewById<View>(R.id.view_pager_content_share) as ImageView
        //AppUtil.setBackColor(context , shareIV , R.color.vordiplom_color_orange);
        linearLayout!!.addView(convertView5)
        shareIV!!.setOnClickListener {
            if (selectedAttendDataModel != null) {
                shareKakaoMessage(ctx, "$type1 통계 ", selectedAttendDataModel!!.popMent, selectedAttendDataModel!!.txtypeMent)
            } else {
                Toast.makeText(ctx, "출석 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        setDetailMent()
    }

    private fun setDetailMent() {
        if (selectedAttendDataModel == null) {
            contentTitletv!!.text = Html.fromHtml("<strong>[ 당일 통계 ] </strong><BR> 확인하실 날짜의 그래프를 클릭하세요.")
            contentTv!!.text = ""
            shareIV!!.visibility = View.GONE
        } else {
            contentTitletv!!.text = Html.fromHtml("<strong> [ " + selectedAttendDataModel!!.curDays + "일 출석통계 ] </strong>")
            contentTv!!.text = Html.fromHtml(selectedAttendDataModel!!.popMent)
            shareIV!!.visibility = View.VISIBLE
        }
    }

    private var selectedAttendDataModel: AttendDataModel? = null
    private fun getCurDateAttendData(xData: String, type1: String) {
        selectedAttendDataModel = attendDataManager!!.getCurDateAttendDataModel(xData)
        val popMent = selectedAttendDataModel!!.popMent
        val sendMent = selectedAttendDataModel!!.txtypeMent
        setDetailMent()
    }

    var shareIV: ImageView? = null
    lateinit var convertView5: View
    var contentTv: TextView? = null
    var contentTitletv: TextView? = null
    var attendDataManager: AttendDataManager? = null
    /**
     * 월간 데이터를 AttendDataModel 형식으로 받아온다.
     */
    override fun getAttandData() {
        okDateMaps = HashMap()
        noDateMaps = HashMap()
        attendModel = AttendModel()
        attendDataManager = AttendDataManager()
        attendDataManager!!.getMonthlyDate(
                AttendDataManager.DATA_TYPE.TEAM_DATA,
                selectedYear,
                CommonData.selectedMonth,
                selectedDayOfWeek,
                selectedDay,
                selectedDays,
                getTeamUid(),
                attendDateMaps,
                object : DataTypeListener.OnCompleteListener<AttendDataModel> {
                    private val attendDataModel: AttendDataModel? = null
                    override fun onComplete(attendDataModel: AttendDataModel) {
                        setPage(attendDataModel)
                    }
                })
    }
}