package com.buel.holyhelpers.utils

import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.model.AttendDataModel
import com.buel.holyhelpers.model.AttendModel
import com.buel.holyhelpers.model.DateModel
import com.buel.holyhelpers.view.DataTypeListener
import com.commonLib.Common
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.orhanobut.logger.LoggerHelper
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendDataManager {
    private var selectDay: Int = 0

    private val reasonList = ArrayList<String>()
    private val okAttendList1 = ArrayList<String>()
    private val okAttendList2 = ArrayList<String>()
    private val okAttendList3 = ArrayList<String>()
    private val okAttendList4 = ArrayList<String>()
    private val okAttendList5 = ArrayList<String>()

    private val okExcutiveAttendList1 = ArrayList<String>()
    private val okExcutiveAttendList2 = ArrayList<String>()
    private val okExcutiveAttendList3 = ArrayList<String>()
    private val okExcutiveAttendList4 = ArrayList<String>()
    private val okExcutiveAttendList5 = ArrayList<String>()

    private val noAttendList1 = ArrayList<String>()
    private val noAttendList2 = ArrayList<String>()
    private val noAttendList3 = ArrayList<String>()
    private val noAttendList4 = ArrayList<String>()
    private val noAttendList5 = ArrayList<String>()

    private val tempNewList1 = ArrayList<String>()
    private val tempNewList2 = ArrayList<String>()
    private val tempNewList3 = ArrayList<String>()
    private val tempNewList4 = ArrayList<String>()
    private val tempNewList5 = ArrayList<String>()


    private val reasonAttendList1 = ArrayList<String>()
    private val reasonAttendList2 = ArrayList<String>()
    private val reasonAttendList3 = ArrayList<String>()
    private val reasonAttendList4 = ArrayList<String>()
    private val reasonAttendList5 = ArrayList<String>()

    private var okDateMaps: HashMap<String, Int?>? = null
    private var noDateMaps: HashMap<String, Int?>? = null
    private var curDayOkCnt: Int = 0
    private var curDaynoCnt: Int = 0
    private var xAxisArr: Array<String>? = null
    private var attendModel: AttendModel? = null
    //해당 주에 속하는 attendModels
    private var weekAttendDataListMap: HashMap<String, AttendDataModel>? = null
    private var selectDays: Int = 0
    private var selectYear: Int = 0
    private var selectMonth: Int = 0
    private var selectDayOfWeek: Int = 0
    private var dateMap: HashMap<String, DateModel>? = null
    private val attendModels = ArrayList<AttendModel>()
    private var selectUid: String? = null
    private var data_type: DATA_TYPE? = null

    private val attendDataModel = AttendDataModel()
    private var onCompleteListener: DataTypeListener.OnCompleteListener<AttendDataModel>? = null

    private var sumOkCnt = 0

    enum class DATA_TYPE {
        TEAM_DATA,
        GROUP_DATA,
        PERSON_DATA
    }

    /**
     * @param data_type
     * @param selectYear
     * @param selectMonth
     * @param selectDayOfWeek
     * @param selectDays
     * @param dateMap
     * @param onCompleteListener
     */
    fun getMonthlyDate(
            data_type: DATA_TYPE?,
            selectYear: Int,
            selectMonth: Int,
            selectDayOfWeek: Int,
            selectDay: Int,
            selectDays: Int,
            selectUid: String?,
            dateMap: HashMap<String, DateModel>,
            onCompleteListener: DataTypeListener.OnCompleteListener<AttendDataModel>?) {

        if (data_type == null ||
                selectYear < 0 ||
                selectUid == null ||
                selectDayOfWeek < 0 ||
                onCompleteListener == null ||
                selectMonth < 0)
            return

        this.data_type = data_type
        this.selectUid = selectUid
        this.dateMap = dateMap
        this.selectYear = selectYear
        this.selectDays = selectDays
        this.selectDay = selectDay
        this.selectMonth = selectMonth
        this.selectDayOfWeek = selectDayOfWeek
        this.onCompleteListener = onCompleteListener

        LoggerHelper.s("okDateMaps  " , okDateMaps.toString())
        LoggerHelper.s("noDateMaps  " , noDateMaps.toString())
        LoggerHelper.s("attendModel  " , attendModel.toString())
        okDateMaps = HashMap()
        noDateMaps = HashMap()
        attendModel = AttendModel()

        getAttendRate()
    }

    fun getCurDateAttendDataModel(dateKey: String): AttendDataModel {
        val tempAttendDataModel = weekAttendDataListMap!![dateKey]

        getDayMakeMent(tempAttendDataModel!!)
        return tempAttendDataModel
    }

    private fun getAttendRate() {

        curDaynoCnt = 0
        curDayOkCnt = 0
        xAxisArr = CalendarUtils.getWeekendsDate(
                selectYear,
                selectMonth,
                selectDayOfWeek)

        attendDataModel.setxAxisArr(xAxisArr)

        val dateModelHashMap = dateMap

        if (dateModelHashMap == null) {
            LoggerHelper.e("출석데이터가 올바르지 않습니다.")
            return
        }
        val size = xAxisArr!!.size
        var xAxisStr = ""

        weekAttendDataListMap = HashMap()

        for (i in 0 until size) {

            if (xAxisArr!![i] == null) {
                xAxisArr!![i] = "-"
                //LoggerHelper.d(xAxisArr[xAxisArr.length - 1]);
            }

            xAxisStr = xAxisArr!![i]

            val xAxisNum: Int
            if (xAxisStr == "-") {
                xAxisNum = 0
            } else {
                xAxisNum = Integer.parseInt(xAxisStr)
            }
            val mapkey = selectYear.toString() + "-" +
                    Common.addZero(selectMonth) + "-" +
                    xAxisStr + "-" +
                    selectDayOfWeek + "-" +
                    selectDays

            //LoggerHelper.d("mapkey : " + mapkey);

            if (dateModelHashMap[mapkey] == null) {
                okDateMaps!![xAxisStr] = 0
                noDateMaps!![xAxisStr] = 0
            } else {
                val dateModel = dateModelHashMap[mapkey]
                val itemModelmap = dateModel!!.member
                //member 데이터
                for ((_, eleAttendModel) in itemModelmap) {

                    var strCompareUid: String? = null

                    if (data_type == DATA_TYPE.GROUP_DATA)
                        strCompareUid = eleAttendModel.groupUID
                    else if (data_type == DATA_TYPE.TEAM_DATA)
                        strCompareUid = eleAttendModel.teamUID
                    else if (data_type == DATA_TYPE.PERSON_DATA)
                        strCompareUid = eleAttendModel.memberUID

                    if (strCompareUid == null) return

                    if (strCompareUid == selectUid) {
                        attendModels.add(eleAttendModel)
                        val weekAttendModels = ArrayList<AttendModel>()
                        weekAttendModels.add(eleAttendModel)

                        var tempAttendDataModel: AttendDataModel? = null

                        //현재 weekAttendDataListMap의 해당 일에 데이터가 있다면 그 데이터를 가지고 오고
                        //LoggerHelper.d("eleAttendModel.date : " + eleAttendModel.date);

                        val curWeekIndex = Math.floor((Integer.valueOf(eleAttendModel.date) / 7).toDouble()).toInt() + 1
                        //LoggerHelper.d("curWeekIndex : " + curWeekIndex);

                        var tempOkList: MutableList<String>? = null
                        var tempNoList: MutableList<String>? = null
                        var tempReasonList: MutableList<String>? = null
                        var tempNewList: MutableList<String>? = null
                        var tempExcutiveAttendList: MutableList<String>? = null

                        if (curWeekIndex == 1) {
                            tempOkList = okAttendList1
                            tempNoList = noAttendList1
                            tempReasonList = reasonAttendList1
                            tempNewList = tempNewList1
                            tempExcutiveAttendList = okExcutiveAttendList1
                        } else if (curWeekIndex == 2) {
                            tempOkList = okAttendList2
                            tempNoList = noAttendList2
                            tempReasonList = reasonAttendList2
                            tempNewList = tempNewList2
                            tempExcutiveAttendList = okExcutiveAttendList2
                        } else if (curWeekIndex == 3) {
                            tempOkList = okAttendList3
                            tempNoList = noAttendList3
                            tempReasonList = reasonAttendList3
                            tempNewList = tempNewList3
                            tempExcutiveAttendList = okExcutiveAttendList3
                        } else if (curWeekIndex == 4) {
                            tempOkList = okAttendList4
                            tempNoList = noAttendList4
                            tempNewList = tempNewList4
                            tempReasonList = reasonAttendList4
                            tempExcutiveAttendList = okExcutiveAttendList4
                        } else if (curWeekIndex == 5) {
                            tempOkList = okAttendList5
                            tempNoList = noAttendList5
                            tempNewList = tempNewList5
                            tempReasonList = reasonAttendList5
                            tempExcutiveAttendList = okExcutiveAttendList5
                        }

                        if (weekAttendDataListMap!![eleAttendModel.date] != null && eleAttendModel.date != null) {
                            tempAttendDataModel = weekAttendDataListMap!![eleAttendModel.date]
                        } else {
                            tempAttendDataModel = AttendDataModel()
                        }//데이터가 없으면 새로 생성한다.

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
                                if (xAxisNum > 0) {

                                    try {
                                        if (eleAttendModel.isExecutives == "임원") {
                                            tempExcutiveAttendList!!.add(eleAttendModel.memberName)
                                        } else {
                                            tempOkList!!.add(eleAttendModel.memberName)
                                        }
                                    } catch (e: Exception) {
                                        tempOkList!!.add(eleAttendModel.memberName)
                                    }

                                }
                                //LoggerHelper.e("eleAttendModel.isNew : " + eleAttendModel.isNew);

                                if (eleAttendModel.isNew != null) {
                                    LoggerHelper.e("eleAttendModel.isNew : " + eleAttendModel.isNew)
                                    if (eleAttendModel.isNew == "새신자") {
                                        tempNewList!!.add(eleAttendModel.memberName)
                                    }
                                }
                            } else {
                                noDateMaps!![eleAttendModel.date] = noCnt!! + 1
                                okDateMaps!![eleAttendModel.date] = okCnt

                                if (xAxisNum > 0) {

                                    tempNoList!!.add(eleAttendModel.memberName)
                                    if (eleAttendModel.noAttendReason != null && Common.trim(eleAttendModel.noAttendReason) != "")
                                        tempReasonList!!.add(eleAttendModel.memberName + " : " + eleAttendModel.noAttendReason)
                                }
                            }
                        } catch (e: Exception) {
                            noDateMaps!![eleAttendModel.date] = noCnt!! + 1
                            okDateMaps!![eleAttendModel.date] = okCnt

                            if (xAxisNum > 0) {
                                tempNoList!!.add(eleAttendModel.memberName)
                                if (eleAttendModel.noAttendReason != null && Common.trim(eleAttendModel.noAttendReason) != "")
                                    tempReasonList!!.add(eleAttendModel.memberName + " : " + eleAttendModel.noAttendReason)
                            }
                        }

                        tempAttendDataModel!!.newList = tempNewList as ArrayList<String>
                        tempAttendDataModel.reasonList = tempReasonList as ArrayList<String>
                        tempAttendDataModel.okAttendList = tempOkList as ArrayList<String>
                        tempAttendDataModel.noAttendList = tempNoList as ArrayList<String>
                        tempAttendDataModel.okExcutiveAttendList = tempExcutiveAttendList as ArrayList<String>
                        tempAttendDataModel.curDays = Integer.parseInt(eleAttendModel.date)
                        weekAttendDataListMap!![eleAttendModel.date] = tempAttendDataModel
                    }
                }
            }
        }

        val mentMap = getSortList(okDateMaps)
        val type1 = "출석"
        val mentStr = getMakeMent(mentMap as HashMap<String, String?> , type1)

        attendDataModel.ment = mentStr
        attendDataModel.curMonth = selectMonth
        attendDataModel.curYear = selectYear
        attendDataModel.dayOfWeek = selectDayOfWeek
        attendDataModel.curDays = selectDays
        //attendDataModel.setWeekAttendDataListMap(weekAttendDataListMap);
        //attendDataModel.setOkAttendList(okAttendList);
        //attendDataModel.setNoAttendList(noAttendList);
        attendDataModel.barData = generateDataBar()
        attendDataModel.reasonList = reasonList
        onCompleteListener!!.onComplete(attendDataModel)
    }

    private fun getMakeMent(mentMap: HashMap<String, String?>, type: String): String {

        if (mentMap["sumNum"] == null) mentMap["sumNum"] = "-"
        if (mentMap["evgNum"] == null) mentMap["evgNum"] = "-"
        if (mentMap["fst_date"] == null) mentMap["fst_date"] = "-"
        if (mentMap["last_date"] == null) mentMap["last_date"] = "-"
        if (mentMap["last"] == null) mentMap["last"] = "-"
        if (mentMap["fst"] == null) mentMap["fst"] = "-"

        var growthCnt = "-"
        val lastCnt: Double
        val avgCnt: Double
        if (mentMap["last"] != "-" && mentMap["last"] != "-") {
            lastCnt = java.lang.Double.parseDouble(mentMap["last"]!!)
            avgCnt = java.lang.Double.parseDouble(mentMap["evgNum"]!!)
            if (lastCnt > 0 && avgCnt > 0) {
                val maxCnt = java.lang.Double.parseDouble(mentMap["last"]!!)
                val evgCnt = java.lang.Double.parseDouble(mentMap["evgNum"]!!)
                growthCnt = (maxCnt - evgCnt).toString()
                attendDataModel.growthCnt = java.lang.Double.parseDouble(growthCnt)
            }
        }

        attendDataModel.avg = mentMap["evgNum"]!!

        val cSum = curDayOkCnt + curDaynoCnt

        var okRate = 0.0
        val sum = (curDayOkCnt + curDaynoCnt).toDouble()

        if (curDayOkCnt != 0 && curDaynoCnt != 0) {
            okRate = curDayOkCnt / sum * 100
        } else if (curDaynoCnt != 0 && curDayOkCnt == 0) {
            okRate = 0.0
        }
        okRate = java.lang.Double.parseDouble(DecimalFormat("##.#").format(okRate))

        val title = "[" + CommonData.groupModel.name + " " +
                SortMapUtil.getInteger(CommonData.teamModel.name) + " : " +
                CommonData.teamModel.etc + "] " +

                selectMonth + "월" +
                selectDay + "일 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(selectDayOfWeek)) + ") " +
                CalendarUtils.getDaysTime(Integer.valueOf(selectDays)) + " 출석"

        return selectMonth.toString() + "월 " + type + "현황입니다. <br>" +
                "전체 " + type + "은 <strong>" + mentMap["sumNum"] + " 명</strong>,  " +
                "전체 평균은 <strong>" + mentMap["evgNum"] + " 명</strong>입니다. <br>" +
                type + "이 높은 날은 <strong>" + mentMap["fst_date"] + "일 " + mentMap["fst"] + "명</strong>, <br>" +
                type + "이 낮은 날은 <strong>" + mentMap["last_date"] + "일 " + mentMap["last"] + "명</strong> 입니다. <br>"
    }

    private fun getDayMakeMent(attendDataModel: AttendDataModel) {
        if (attendDataModel.okAttendList == null || attendDataModel.noAttendList == null) {
            if (attendDataModel.okAttendList.size < 0 || attendDataModel.noAttendList.size < 0) {
                attendDataModel.popMent = CommonString.INFO_TITLE_DONT_ATTEND_DATA
                attendDataModel.txtypeMent = CommonString.INFO_TITLE_DONT_ATTEND_DATA
                return
            }
        }
        //selectDay;
        val selectDayOkCnt = attendDataModel.okAttendList.size + attendDataModel.okExcutiveAttendList.size
        val selectDayNoCnt = attendDataModel.noAttendList.size

        val cSum = selectDayOkCnt + selectDayNoCnt

        var okRate = 0.0
        val sum = (selectDayOkCnt + selectDayNoCnt).toDouble()

        if (selectDayOkCnt != 0 && selectDayOkCnt != 0) {
            okRate = selectDayOkCnt / sum * 100
        } else if (selectDayNoCnt != 0 && selectDayOkCnt == 0) {
            okRate = 0.0
        }

        okRate = java.lang.Double.parseDouble(DecimalFormat("##.#").format(okRate))

        val title = "[" + CommonData.groupModel.name + " " +
                SortMapUtil.getInteger(CommonData.teamModel.name) + " : " +
                CommonData.teamModel.etc + "] " +
                selectMonth + "월" +
                selectDay + "일 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(selectDayOfWeek)) + ") " +
                CalendarUtils.getDaysTime(Integer.valueOf(selectDays)) + " 출석"


        val okExcutiveAttendList:MutableList<String> = attendDataModel.okExcutiveAttendList
        val okAttendList:MutableList<String> = attendDataModel.okAttendList!!
        val reasonList:MutableList<String> = attendDataModel.reasonList!!
        val newList:MutableList<String> = attendDataModel.newList!!
        val noAttendList:MutableList<String> = attendDataModel.noAttendList!!

        noAttendList.sort()
        okExcutiveAttendList.sort()
        okAttendList.sort()
        reasonList.sort()
        newList.sort()

        val okExcutiveListStr =if (okExcutiveAttendList.size > 0)okExcutiveAttendList.toString() else "-"
        val okListStr = if (okAttendList.size > 0) okAttendList.toString() else "-"
        val noReasonListStr = if (reasonList.size > 0) reasonList.toString() else "-"
        val newListStr = if (newList.size > 0) newList.toString() else "-"
        val noListStr = if (noAttendList.size > 0) noAttendList.toString() else "-"

        val popTxt = "<center><strong> " + title + "</strong></center>" +
                "<br><br>" + "<strong>총 원 : " + cSum + "명<br>" +
                "출 석 : " + selectDayOkCnt + "명 / 결 석 : " + selectDayNoCnt + "명<br>" + "<br></strong>" +
                "<strong>* 출석률 : </strong>" + okRate + " % 입니다." + "<br><br>" +
                "* 임원출석 명단<br>" +
                okExcutiveListStr+ "<br><br>" +
                "* 성도/회원출석 명단<br>" +
                okListStr+ "<br><br>" +
                "*새신자 출석 명단<br> " +
                newListStr+ "<br><br>" +
                "* 결석 명단 <br>" +
                noListStr + "<br><br> " +
                "* 결석 사유 <br>" +
                noReasonListStr

        val mentTxt = title +
                "\n\n" + "총 원 : " + cSum + "명\n" +
                "출 석 : " + selectDayOkCnt + "명 / 결 석 : " + selectDayNoCnt + "명\n\n" +
                "* 출석률 : " + okRate + " % 입니다." + "\n\n" +
                "* 임원출석 명단 \n" +
                okExcutiveListStr + "\n\n" +
                "* 성도/회원출석 명단 \n" +
                okListStr+ "\n\n" +
                "* 새신자 출석 명단 \n" +
                newListStr+ "\n\n" +
                "* 결석 명단 \n" +
                noListStr + "\n\n" +
                "* 결석 사유 \n" +
                noReasonListStr

        attendDataModel.popMent = popTxt
        attendDataModel.txtypeMent = mentTxt
    }


    private fun getSortList(dateMaps: HashMap<String, Int?>?): HashMap<*, *> {

        //LoggerHelper.i("GroupBriefingFragment getSortList");

        val mentMap = HashMap<String, String>()
        val tempDateList = ArrayList<String>()
        val tempCntList = ArrayList<String>()
        val tm = TreeMap(dateMaps)
        //Iterator<String> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        val iteratorValue = Common.sortByValue(tm).iterator()

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

            if (dateMaps[key] != 0)
                evgCnt++
        }
        try {
            mentMap["cnt"] = cnt.toString()
            mentMap["sumNum"] = sumNum.toString()
            mentMap["fst_date"] = tempDateList[0]
            mentMap["fst"] = tempCntList[0]
            mentMap["last_date"] = tempDateList[evgCnt - 1]
            mentMap["last"] = tempCntList[evgCnt - 1]
            //LoggerHelper.d("mentMap : " + mentMap);
            evgNum = (sumNum / evgCnt).toFloat()
            mentMap["evgNum"] = evgNum.toString()
        } catch (e: Exception) {
            LoggerHelper.d("e.getMessage() ::::::::::: + " + e.message)
        }
        return mentMap
    }

    private fun generateDataBar(): BarData {
        val okEntries = ArrayList<BarEntry>()
        var dCnt: Int? = 0
        sumOkCnt = 0
        for (eleWeekOfDay in xAxisArr!!) {
            if (okDateMaps!![eleWeekOfDay] != null) {
                okEntries.add(BarEntry(dCnt!!.toFloat(), okDateMaps!![eleWeekOfDay]!!.toFloat()))
                sumOkCnt += okDateMaps!![eleWeekOfDay]!!
            }
            dCnt = dCnt!! + 1
        }

        //LoggerHelper.d("generateDataBar", "sumOkCnt : " + sumOkCnt);
        return getBarData(okEntries, Common.VORDIPLOM_COLORS, "$selectMonth 월 출석")
    }

    private fun getBarData(entries: ArrayList<BarEntry>, colorset: IntArray, dataName: String): BarData {
        val d = BarDataSet(entries, dataName)

        d.setColors(*colorset)
        d.highLightAlpha = 255

        val cd = BarData(d)
        cd.barWidth = 0.9f


        return cd
    }
}
