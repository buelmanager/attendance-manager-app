package com.buel.holyhelpers.utils

import android.content.Context
import android.graphics.Color
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.attendDateMaps
import com.buel.holyhelpers.data.CommonData.currentFullDateStr
import com.buel.holyhelpers.data.CommonData.groupModel
import com.buel.holyhelpers.data.CommonData.selectedDayOfWeek
import com.buel.holyhelpers.data.CommonData.selectedDays
import com.buel.holyhelpers.data.CommonData.selectedYear
import com.buel.holyhelpers.model.AttendModel
import com.buel.holyhelpers.model.BarChartModel
import com.buel.holyhelpers.model.DateModel
import com.buel.holyhelpers.model.HolyModel.groupModel.teamModel
import com.buel.holyhelpers.utils.SortMapUtil.getInteger
import com.commonLib.Common
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.orhanobut.logger.LoggerHelper
import java.util.*

object BarChartDataUtils {
    var attendModels = ArrayList<AttendModel>()
    var okDateMaps: HashMap<String?, Int?>? = null
    var teamUid: String? = null
    var teamModel: teamModel? = null
    var context: Context? = null
    var isGroup: Boolean? = null
    lateinit var xAxisArr: Array<String?>
    /**
     * getAttandData
     *
     * @param teamModel
     * @param isGroup
     */
    @JvmStatic
    fun getAttandData(teamModel: teamModel, isGroup: Boolean?): BarChartModel? {
        BarChartDataUtils.isGroup = isGroup
        okDateMaps = HashMap()
        if (teamModel.etc == null) teamModel.etc = ""
        BarChartDataUtils.teamModel = teamModel
        return attendRate
    }//System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
    //MakeBarChartView chart = new MakeBarChartView(generateDataBar(), context, xAxisArr, ment);
//View chartView = chart.getConvertView();
//member 데이터

    //LoggerHelper.d(xAxisArr[xAxisArr.length - 1]);
    //LoggerHelper.d("mapkey : " + mapkey);
    private val attendRate: BarChartModel?
        private get() {
            xAxisArr = CalendarUtils.getWeekendsDate(
                    selectedYear,
                    CommonData.selectedMonth,
                    selectedDayOfWeek)
            val dateModelHashMap: HashMap<String, DateModel> = attendDateMaps
            if (dateModelHashMap == null) {
                if (teamModel != null) LoggerHelper.e(teamModel!!.name + " 의 " + selectedYear + "." + CommonData.selectedMonth + "(" + selectedDayOfWeek + ")" + "출석데이터가 올바르지 않습니다.")
                return null
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
                //LoggerHelper.d("mapkey : " + mapkey);
                if (dateModelHashMap[mapkey] == null) {
                    okDateMaps!![xAxisStr] = 0
                } else {
                    val dateModel = dateModelHashMap[mapkey]
                    val itemModelmap = dateModel!!.member
                    //member 데이터
                    for ((_, eleAttendModel) in itemModelmap) { //System.out.println(String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
                        attendModels.add(eleAttendModel)
                        var okCnt: Int? = 0
                        if (isGroup!!) {
                            if (okDateMaps!![eleAttendModel.date] != null) {
                                okCnt = okDateMaps!![eleAttendModel.date]
                            }
                            try {
                                if (eleAttendModel.attend == "true") {
                                    okDateMaps!![eleAttendModel.date] = okCnt!! + 1
                                } else {
                                    okDateMaps!![eleAttendModel.date] = okCnt
                                }
                            } catch (e: Exception) {
                                okDateMaps!![eleAttendModel.date] = okCnt
                            }
                        } else {
                            if (eleAttendModel.teamUID == teamModel!!.uid) {
                                if (okDateMaps!![eleAttendModel.date] != null) {
                                    okCnt = okDateMaps!![eleAttendModel.date]
                                }
                                try {
                                    if (eleAttendModel.attend == "true") {
                                        okDateMaps!![eleAttendModel.date] = okCnt!! + 1
                                    } else {
                                        okDateMaps!![eleAttendModel.date] = okCnt
                                    }
                                } catch (e: Exception) {
                                    okDateMaps!![eleAttendModel.date] = okCnt
                                }
                            }
                        }
                    }
                }
            }
            val mentMap: HashMap<String, String> = getSortList(okDateMaps)
            val type1 = "출석"
            val ment = getMakeMent(mentMap, type1)
            //MakeBarChartView chart = new MakeBarChartView(generateDataBar(), context, xAxisArr, ment);
//View chartView = chart.getConvertView();
            val barChartModel = BarChartModel()
            barChartModel.bardata = generateDataBar()
            barChartModel.ment = ment
            barChartModel.evg = mentMap["evgNum"]
            val strEtc: String
            strEtc = if (teamModel!!.etc === "") {
                teamModel!!.etc
            } else {
                " : " + teamModel!!.etc
            }
            if (teamModel != null) barChartModel.title = "<strong> [ " + groupModel.name + " " + getInteger(teamModel!!.name) + strEtc + " ] </strong>\n" + currentFullDateStr else {
                if (CommonData.selectedMonth <= 0) {
                    barChartModel.title = (selectedYear - 1).toString() + "년 " + (12 + CommonData.selectedMonth) + "월 (" + CalendarUtils.getDateDay(selectedDayOfWeek) + ") " + CalendarUtils.getDaysTime(selectedDays) + "예배"
                } else {
                    barChartModel.title = "" + selectedYear + "년 " + CommonData.selectedMonth + "월 (" + CalendarUtils.getDateDay(selectedDayOfWeek) + ")" + CalendarUtils.getDaysTime(selectedDays) + "예배"
                }
            }
            barChartModel.XaxisValues = xAxisArr
            return barChartModel
        }

    private var sumOkCnt = 0
    private const val sumNoCnt = 0
    private fun getSortList(dateMaps: HashMap<String?, Int?>?): HashMap<String, String> { // LoggerHelper.i("TeamBriefingRecyclerFragment getSortList");
        val mentMap = HashMap<String, String>()
        val tempDateList = ArrayList<String>()
        val tempCntList = ArrayList<String>()
        val tm = TreeMap(dateMaps)
        val iteratorValue = sortByValue(tm).iterator()
        var cnt = 0
        var evgCnt = 0
        var sumNum = 0.0
        var evgNum = 0.0
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
            evgNum = sumNum / evgCnt
            mentMap["evgNum"] = evgNum.toString()
        } catch (e: Exception) { //LoggerHelper.d("e.getMessage() ::::::::::: + " + e.getMessage());
        }
        return mentMap
    }

    fun sortByValue(map: Map<String?, Int?>): List<Int?> {
        /*val list: MutableList<String?> = ArrayList<Any?>()
        list.addAll(map.keys)
        Collections.sort(list) { o1, o2 ->
            val v1 = map[o1]
            val v2 = map[o2]
            (v2 as Comparable<*>?)!!.compareTo(v1)
        }
        //Collections.reverse(list); // 주석시 오름차순*/
        return map.values.sortedBy { it }
    }

    private fun generateDataBar(): BarData {
        val okEntries = ArrayList<BarEntry>()
        var dCnt = 0f
        sumOkCnt = 0
        for (eleWeekOfDay in xAxisArr) {
            if (okDateMaps!![eleWeekOfDay] != null) {
                okEntries.add(BarEntry(dCnt, okDateMaps!![eleWeekOfDay] as Float))
                sumOkCnt = +okDateMaps!![eleWeekOfDay]!!
            }
            dCnt++
        }
        return getBarData(okEntries, Color.BLUE, ""+ CommonData.selectedMonth + " 월 출석")
    }

    private fun getBarData(entries: ArrayList<BarEntry>, color: Int, dataName: String): BarData {
        val d = BarDataSet(entries, dataName)
        d.setColors(*ColorTemplate.VORDIPLOM_COLORS)
        d.highLightAlpha = 255
        val cd = BarData(d)
        cd.barWidth = 0.9f
        return cd
    }

    private fun getMakeMent(mentMap: HashMap<String, String>, type: String): String {
        if (mentMap["sumNum"] == null) mentMap["sumNum"] = "-"
        if (mentMap["evgNum"] == null) mentMap["evgNum"] = "-"
        if (mentMap["fst_date"] == null) mentMap["fst_date"] = "-"
        if (mentMap["last_date"] == null) mentMap["last_date"] = "-"
        if (mentMap["last"] == null) mentMap["last"] = "-"
        if (mentMap["fst"] == null) mentMap["fst"] = "-"
        return Common.addZero(CommonData.selectedMonth).toString() + "월 " + type + "현황입니다. <br>" +
                "전체 " + type + "은 <strong>" + mentMap["sumNum"] + " 명</strong>,  " +
                "전체 평균은 <strong>" + mentMap["evgNum"] + " 명</strong>입니다. <br>" +
                type + "이 높은 날은 <strong>" + mentMap["fst_date"] + "일 " + mentMap["fst"] + "명</strong>, <br>" +
                type + "이 낮은 날은 <strong>" + mentMap["last_date"] + "일 " + mentMap["last"] + "명</strong> 입니다. <br>"
    }
}