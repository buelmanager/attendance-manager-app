package com.buel.holyhelpers.utils

import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.model.HolyModel.groupModel
import com.buel.holyhelpers.model.HolyModel.groupModel.teamModel
import com.buel.holyhelpers.model.HolyModel.memberModel
import com.orhanobut.logger.LoggerHelper
import java.util.*

/**
 * Created by blue7 on 2018-06-20.
 */
object SortMapUtil {
    @JvmStatic
    fun getSortGroupList(map: Map<String, groupModel>): List<groupModel> {
        var tempList:MutableList<groupModel>  = map.values as MutableList<groupModel>
        tempList.sortBy { it.name }
        LoggerHelper.d("getSortTeamList tempList :  $tempList")
        return tempList
    }

    //키값 오름차순 정렬(기본)
    //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
    @JvmStatic
    val sortTeamList: List<*>
        get() {
            LoggerHelper.d("getSortTeamList 1 ")
            var teamMap = CommonData.teamMap as HashMap<String, teamModel>
            var tempList:MutableList<teamModel>  = teamMap.values as MutableList<teamModel>
            tempList.sortBy { it.name }
            LoggerHelper.d("getSortTeamList tempList :  $tempList")
            return tempList
        }

    fun getSortMemberMap(map: Map<String?, memberModel>): List<*> {
        val tempMap = HashMap<String, memberModel>()
        for ((key, elemTemp) in map) {
            elemTemp.uid = key
            tempMap[elemTemp.name] = elemTemp
        }
        val tempList: MutableList<memberModel?> = ArrayList()
        val tm = TreeMap(tempMap)
        val iteratorKey: Iterator<String> = tm.keys.iterator() //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        while (iteratorKey.hasNext()) {
            val key = iteratorKey.next()
            tempList.add(tm[key])
        }
        return tempList
    }

    fun getSortHolyList(holyModels: ArrayList<HolyModel>): List<*> {
        val tempMap = HashMap<Int, HolyModel>()
        var c = 0
        for (elem in holyModels) {
            tempMap[c] = elem
            c++
        }
        val tempList: MutableList<HolyModel?> = ArrayList()
        val tm = TreeMap(tempMap)
        val iteratorKey: Iterator<Int> = tm.keys.iterator() //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        while (iteratorKey.hasNext()) {
            val key = iteratorKey.next()
            tempList.add(tm[key])
        }
        return tempList
    }

    fun getSortMemberList(members: ArrayList<memberModel>): List<*> {
        val tempMap = HashMap<String, memberModel>()
        for (elem in members) {
            tempMap[elem.name + "|" + elem.uid] = elem
        }
        val tempList: MutableList<memberModel?> = ArrayList()
        val tm = TreeMap(tempMap)
        val iteratorKey: Iterator<String> = tm.keys.iterator() //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        while (iteratorKey.hasNext()) {
            val key = iteratorKey.next()
            tempList.add(tm[key])
        }
        return tempList
    }

    /**
     * 문자열의 숫자를 뽑아 리턴
     *
     * @param str 숫자가 뒤에 들어가도록...
     * @return 리턴되는 숫자
     */
    @JvmStatic
    fun getInteger(str: String?): Int {

        if(str.isNullOrBlank())return -1
        var tempStr = ""
        try {
            val tempNum = str.toInt()
            if (tempNum >= 0) {
                return tempNum
            }
        } catch (e: Exception) {
            LoggerHelper.d("getInteger  : [ $str ] 은 문자열입니다.")
        }
        //charAt를 이용하여 숫자가 아니면 넘기는 식으로 해서 뽑아 낼 수 있다.
        for (i in 0 until str.length) { // 48 ~ 57은 아스키 코드로 0~9이다.
            if (48 <= str[i].toInt() && str[i].toInt() <= 57) tempStr += str[i]
        }
        return if (str.length == 0) {
            0
        } else Integer.valueOf(tempStr)
    }
}