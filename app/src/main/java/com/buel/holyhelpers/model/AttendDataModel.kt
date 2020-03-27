package com.buel.holyhelpers.model

import com.github.mikephil.charting.data.BarData
import java.util.*
import kotlin.collections.ArrayList

/**
 * AttendDataManager 받은 데이터셋
 */
class AttendDataModel {

    //브리핑 멘트
    var ment = ""
    //텍스트 멘트
    var txtypeMent = ""

    var popMent = ""
    //평균
    var avg = ""
    //성장률
    var growthRate = 0.0
    //선택 달
    var curMonth: Int = 0
    //선택 요일
    var dayOfWeek: Int = 0
    //선택 연도
    var curYear: Int = 0
    var curDays: Int = 0
    var growthCnt: Double = 0.toDouble()
    var barData: BarData? = null
    private var xAxisArr: Array<String>? = null
    var newList: ArrayList<String> = ArrayList()
    var reasonList: ArrayList<String> = ArrayList()
    var okExcutiveAttendList: ArrayList<String> = ArrayList()
    var okAttendList: ArrayList<String> = ArrayList()
    var noAttendList: ArrayList<String> = ArrayList()

    //데이터 리스트
    var weekAttendDataListMap: HashMap<String, ArrayList<AttendModel>>? = null

    fun getxAxisArr(): Array<String>? {
        return xAxisArr
    }

    fun setxAxisArr(xAxisArr: Array<String>?) {
        this.xAxisArr = xAxisArr
    }
}
