package com.buel.holyhelpers.view.viewPage.briefingPageViewFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.attendDateMaps
import com.buel.holyhelpers.data.CommonData.currentFullDateStr
import com.buel.holyhelpers.data.CommonData.getGroupUid
import com.buel.holyhelpers.data.CommonData.getTeamUid
import com.buel.holyhelpers.data.CommonData.groupModel
import com.buel.holyhelpers.data.CommonData.holyModel
import com.buel.holyhelpers.data.CommonData.selectedDayOfWeek
import com.buel.holyhelpers.data.CommonData.selectedDays
import com.buel.holyhelpers.data.CommonData.selectedYear
import com.buel.holyhelpers.data.CommonData.teamModel
import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.model.AttendModel
import com.buel.holyhelpers.model.DateModel
import com.buel.holyhelpers.utils.CalendarUtils
import com.buel.holyhelpers.view.MakeBarChartView
import com.buel.holyhelpers.view.MakePieChartView
import com.commonLib.Common
import com.commonLib.SuperToastUtil
import com.github.mikephil.charting.data.*
import com.orhanobut.logger.LoggerHelper
import java.text.DecimalFormat
import java.util.*

/**
 * Created by blue7 on 2018-05-16.
 */
class GroupBriefingFragment : AbstBriefingFragment() {
    protected var mTfRegular: Typeface? = null
    protected var mTfLight: Typeface? = null
    private var linearLayout: LinearLayout? = null
    private var scrollView: NestedScrollView? = null
    private lateinit var tvTitle: TextView
    private val attendModels = ArrayList<AttendModel>()
    private var okDateMaps: HashMap<String?, Int?>? = null
    private var noDateMaps: HashMap<String?, Int?>? = null
    private lateinit var xAxisArr: Array<String>
    private var attendModel: AttendModel? = null
    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.corps_step1_view_page_fragment, container, false)
        LoggerHelper.i("GroupBriefingFragment onCreateView")
        this.rootView = rootView
        mTfRegular = Typeface.createFromAsset(getContext()!!.assets, "OpenSans-Regular.ttf")
        mTfLight = Typeface.createFromAsset(getContext()!!.assets, "OpenSans-Light.ttf")
        linearLayout = rootView.findViewById(R.id.corps_step1_view_page_fragment_ll)
        scrollView = rootView.findViewById(R.id.corps_step1_view_page_fragment_sv)
        tvTitle = rootView.findViewById(R.id.corps_step1_view_page_tv_title)
        if (getGroupUid() != null &&
                getTeamUid() != null) {
            tvTitle.setText("* [ " + holyModel.name + " ] 의 " + " [ " + groupModel.name + " ] 그룹 출석을 합산한 통계입니다.")
            getAttandData()
        } else {
            SuperToastUtil.toastE(getContext(), CommonString.GROUP_NICK + " 또는 " + CommonString.TEAM_NICK + ", 날짜 정보가 설정되지 않았습니다.")
        }
        return rootView
    }

    private lateinit var ctx: Context
    @SuppressLint("SetTextI18n")
    private fun setPage() {
        if (groupModel == null || teamModel == null) {
            SuperToastUtil.toastE(getContext(), CommonString.GROUP_NICK + " 또는 " + CommonString.TEAM_NICK + " 정보가 설정되지 않았습니다.")
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
        titleTv.text = Html.fromHtml("<strong> [ " + groupModel.name + " ]</strong> 출석통계")
        linearLayout!!.addView(convertView)
        val mentMap: HashMap<String, String> = getSortList(okDateMaps)
        val type1 = "출석"
        val mentStr = getBarMakeMent(mentMap, type1)
        val chart = MakeBarChartView(generateDataBar(), ctx!!, xAxisArr, mentStr, null)
        val chartView = chart.convertView
        linearLayout!!.addView(chartView)
        val convertView2 = LayoutInflater.from(ctx).inflate(
                R.layout.viewpager_title, null)
        val titleTv2 = convertView2.findViewById<View>(R.id.view_pager_tv_title) as TextView
        titleTv2.text = Html.fromHtml("<strong> [ " + groupModel.name + " ]</strong> 결석통계")
        linearLayout!!.addView(convertView2)
        val mentMap2: HashMap<String, String> = getSortList(noDateMaps)
        val type2 = "결석"
        val mentStr2 = getBarMakeMent(mentMap2, type2)
        val chart2 = MakeBarChartView(generateNoAttendDataBar(), ctx, xAxisArr, mentStr2, null)
        val chartView2 = chart2.convertView
        linearLayout!!.addView(chartView2)
        val pieData = generateDataPie()
        val pieMent = getPieMakeMent(sumOkCnt.toDouble(), sumNoCnt.toDouble(), groupModel.name + " 월간 전체 출결")
        val chart3 = MakePieChartView(pieData, ctx, "[ " + groupModel.name + " ] " +
                "월간 전체 출결", pieMent, "출결현황")
        val chartView3 = chart3.convertView
        linearLayout!!.addView(chartView3)
    }

    private fun getBarMakeMent(mentMap: HashMap<String, String>, type: String): String {
        if (mentMap["sumNum"] == null) mentMap["sumNum"] = "-"
        if (mentMap["evgNum"] == null) mentMap["evgNum"] = "-"
        if (mentMap["fst_date"] == null) mentMap["fst_date"] = "-"
        if (mentMap["last_date"] == null) mentMap["last_date"] = "-"
        if (mentMap["last"] == null) mentMap["last"] = "-"
        if (mentMap["fst"] == null) mentMap["fst"] = "-"
        return ""+ CommonData.selectedMonth + "월 " + type + "현황입니다. <br>" +
                "전체 " + type + "은 <strong>" + mentMap["sumNum"] + " 명</strong>,  " +
                "전체 평균은 <strong>" + mentMap["evgNum"] + " 명</strong>입니다. <br>" +
                type + "이 높은 날은 <strong>" + mentMap["fst_date"] + "일 " + mentMap["fst"] + "명</strong>, <br>" +
                type + "이 낮은 날은 <strong>" + mentMap["last_date"] + "일 " + mentMap["last"] + "명</strong> 입니다. <br>"
    }

    private fun getPieMakeMent(okCnt: Double, noCnt: Double, type: String): String {
        var okRate = 0.0
        var noRate = 0.0
        val sum = okCnt + noCnt
        if (okCnt != 0.0 && noCnt != 0.0) {
            okRate = (okCnt / sum) * 100
            noRate = (noCnt / sum) * 100
        } else if (okCnt == 0.0 && noCnt != 0.0) {
            noRate = 100.0
        } else if (noCnt != 0.0 && okCnt == 0.0) {
            okRate = 100.0
        }
        noRate = DecimalFormat("##.#").format(noRate).toDouble()
        okRate = DecimalFormat("##.#").format(okRate).toDouble()
        /*LoggerHelper.d("ok : " + okCnt);
        LoggerHelper.d("no : " + noCnt);
        LoggerHelper.d("sum: " + sum);
        LoggerHelper.d("okRate: " + okRate);
        LoggerHelper.d("noRate: " + noRate);*/
        return ""+ CommonData.selectedMonth + "월 " + type + "현황입니다. <br>" +
                "전체 출석은 <strong>" + okCnt + " 명</strong>,  " +
                "전체 출석평균은 <strong>" + okRate + " %</strong>입니다. <br>" +
                "전체 결석은 <strong>" + noCnt + " 명</strong> 입니다.  " +
                "전체 결석평균은 <strong>" + noRate + " %</strong>입니다. <br>"
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private fun generateDataPie(): PieData {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(sumOkCnt.toFloat(), "전체 출석"))
        entries.add(PieEntry(sumNoCnt.toFloat(), "전체 결석"))
        val d = PieDataSet(entries, "")
        // space between slices
        d.sliceSpace = 2f
        d.setColors(*Common.VORDIPLOM_COLORS)
        return PieData(d)
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private fun generateNoAttendDataBar(): BarData {
        val noEntries = ArrayList<BarEntry>()
        var dCnt = 0f
        sumNoCnt = 0
        for (eleWeekOfDay in xAxisArr) {
            if (noDateMaps!![eleWeekOfDay] != null) {
                noEntries.add(BarEntry(dCnt, noDateMaps!![eleWeekOfDay] as Float))
                sumNoCnt += noDateMaps!![eleWeekOfDay]!!
            }
            dCnt++
        }
        return getBarData(noEntries, Common.VORDIPLOM_RED, ""+ CommonData.selectedMonth + " 월 결석")
    }

    private fun generateDataBar(): BarData {
        val okEntries = ArrayList<BarEntry>()
        var dCnt = 0f
        sumOkCnt = 0
        for (eleWeekOfDay in xAxisArr) {
            if (okDateMaps!![eleWeekOfDay] != null) {
                okEntries.add(BarEntry(dCnt, okDateMaps!![eleWeekOfDay] as Float))
                sumOkCnt += okDateMaps!![eleWeekOfDay]!!
            }
            dCnt++
        }
        //LoggerHelper.d("generateDataBar", "sumOkCnt : " + sumOkCnt);
        return getBarData(okEntries, Common.VORDIPLOM_BLUE, ""+ CommonData.selectedMonth + " 월 출석")
    }

    private fun getBarData(entries: ArrayList<BarEntry>, colorset: IntArray, dataName: String): BarData {
        val d = BarDataSet(entries, dataName)
        d.setColors(*colorset)
        d.highLightAlpha = 255
        val cd = BarData(d)
        cd.barWidth = 0.9f
        return cd
    }

    override fun getAttandData() {
        okDateMaps = HashMap()
        noDateMaps = HashMap()
        attendModel = AttendModel()
        attendRate
        setPage()
    }//System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));//member 데이터

    //LoggerHelper.d(xAxisArr[xAxisArr.length - 1]);
    private val attendRate: Unit
        private get() {
            xAxisArr = CalendarUtils.getWeekendsDate(
                    selectedYear,
                    CommonData.selectedMonth,
                    selectedDayOfWeek)
            val dateModelHashMap: HashMap<String, DateModel> = attendDateMaps
            if (dateModelHashMap == null) {
                LoggerHelper.e("출석데이터가 올바르지 않습니다.")
                return
            }
            val size = xAxisArr.size
            var xAxisStr: String? = ""
            for (i in 0 until size) {
                if (xAxisArr[i] == null) {
                    xAxisArr[i] = "-"
                    //LoggerHelper.d(xAxisArr[xAxisArr.length - 1]);
                }
                xAxisStr = xAxisArr[i]
                val mapkey: String = "" + selectedYear + "-" +
                        Common.addZero(CommonData.selectedMonth) + "-" +
                        xAxisStr + "-" +
                        selectedDayOfWeek + "-" +
                        selectedDays
                LoggerHelper.d("mapkey : $mapkey")
                if (dateModelHashMap[mapkey] == null) {
                    okDateMaps!![xAxisStr] = 0
                    noDateMaps!![xAxisStr] = 0
                } else {
                    val dateModel = dateModelHashMap[mapkey]
                    val itemModelmap = dateModel!!.member
                    //member 데이터
                    for ((_, eleAttendModel) in itemModelmap) { //System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
                        attendModels.add(eleAttendModel)
                        var okCnt: Int? = 0
                        var noCnt: Int? = 0
                        if (okDateMaps!![eleAttendModel.date] != null) {
                            okCnt = okDateMaps!![eleAttendModel.date]
                        }
                        if (noDateMaps!![eleAttendModel.date] != null) {
                            noCnt = noDateMaps!![eleAttendModel.date]
                        }
                        try {
                            if (eleAttendModel.attend == "true") {
                                okDateMaps!![eleAttendModel.date] = okCnt!! + 1
                                noDateMaps!![eleAttendModel.date] = noCnt
                            } else {
                                noDateMaps!![eleAttendModel.date] = noCnt!! + 1
                                okDateMaps!![eleAttendModel.date] = okCnt
                            }
                        } catch (e: Exception) {
                            noDateMaps!![eleAttendModel.date] = noCnt!! + 1
                            okDateMaps!![eleAttendModel.date] = okCnt
                        }
                    }
                }
            }
        }

    private var sumOkCnt = 0
    private var sumNoCnt = 0
    private fun getSortList(dateMaps: HashMap<String?, Int?>?): HashMap<String, String> {
        LoggerHelper.i("GroupBriefingFragment getSortList")
        val mentMap = HashMap<String, String>()
        val tempDateList = ArrayList<String>()
        val tempCntList = ArrayList<String>()
        val tm = TreeMap(dateMaps)
        //Iterator<String> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
//Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        val iteratorValue = sortByValue(tm).iterator()
        var cnt = 0
        var evgCnt = 0
        var sumNum = 0
        var evgNum = 0f
        while (iteratorValue.hasNext()) {
            val key = iteratorValue.next() as String
            sumNum += dateMaps!![key]!!
            val tempMap = HashMap<String, Int?>()
            tempMap[key] = tm[key]
            tempDateList.add(key)
            tempCntList.add(tm[key].toString())
            cnt++
            if (dateMaps[key] != 0) evgCnt++
        }
        try {
            mentMap["cnt"] = cnt.toString()
            mentMap["sumNum"] = sumNum.toString()
            mentMap["fst_date"] = tempDateList[0]
            mentMap["fst"] = tempCntList[0]
            mentMap["last_date"] = tempDateList[evgCnt - 1]
            mentMap["last"] = tempCntList[evgCnt - 1]
            //LoggerHelper.d("mentMap : " + mentMap);
            evgNum = sumNum / evgCnt.toFloat()
            mentMap["evgNum"] = evgNum.toString()
        } catch (e: Exception) {
            LoggerHelper.d(e.message)
        }
        return mentMap
    }

    fun sortByValue(map: Map<String?, Int?>): List<Int?> {
        return map.values.sortedBy { it }
    }
}