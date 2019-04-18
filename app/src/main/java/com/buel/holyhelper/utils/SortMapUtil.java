package com.buel.holyhelper.utils;

import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.model.HolyModel;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by blue7 on 2018-06-20.
 */

public class SortMapUtil {

    public static List getSortGroupList(Map<String, HolyModel.groupModel> map) {

        HashMap<String, HolyModel.groupModel> tempMap = new HashMap<>();

        for (Map.Entry<String, HolyModel.groupModel> elem : map.entrySet()) {
            HolyModel.groupModel elemTemp = elem.getValue();
            elemTemp.uid = elem.getKey();
            String tempName = elemTemp.name;
            tempMap.put(tempName, elemTemp);
        }

        List<HolyModel.groupModel> tempList = new ArrayList<>();

        TreeMap<String, HolyModel.groupModel> tm = new TreeMap<>(tempMap);
        Iterator<String> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

        while (iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            tempList.add(tm.get(key));
        }
        return tempList;
    }

    public static List getSortTeamList() {
        LoggerHelper.d("getSortTeamList 1 ");
        HashMap<String, HolyModel.groupModel.teamModel> teamMap
                = (HashMap<String, HolyModel.groupModel.teamModel>) CommonData.getGroupModel().team;


        LoggerHelper.d("getSortTeamList CommonData.getGroupModel().teamModel : " + CommonData.getGroupModel().team);

        HashMap<Integer, HolyModel.groupModel.teamModel> tempMap = new HashMap<>();

        for (Map.Entry<String, HolyModel.groupModel.teamModel> elem : teamMap.entrySet()) {
            HolyModel.groupModel.teamModel elemTemp = elem.getValue();
            LoggerHelper.d("getSortTeamList elem.getKey() : " + elem.getKey());
            elemTemp.uid = elem.getKey();
            int tempCnt = getInteger(elemTemp.name);
            tempMap.put(tempCnt, elemTemp);
        }

        LoggerHelper.d("getSortTeamList tempMap :  " + tempMap);

        List<HolyModel.groupModel.teamModel> tempList = new ArrayList<>();

        TreeMap<Integer, HolyModel.groupModel.teamModel> tm = new TreeMap<>(tempMap);
        Iterator<Integer> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

        while (iteratorKey.hasNext()) {
            Integer key = iteratorKey.next();
            tempList.add(tm.get(key));
        }

        LoggerHelper.d("getSortTeamList tempList :  " + tempList);

        return tempList;
    }

    public static List getSortMemberMap(Map<String, HolyModel.memberModel> map) {

        HashMap<String, HolyModel.memberModel> tempMap = new HashMap<>();

        for (Map.Entry<String, HolyModel.memberModel> elem : map.entrySet()) {
            HolyModel.memberModel elemTemp = elem.getValue();
            elemTemp.uid = elem.getKey();

            tempMap.put(elemTemp.name, elemTemp);
        }

        List<HolyModel.memberModel> tempList = new ArrayList<>();

        TreeMap<String, HolyModel.memberModel> tm = new TreeMap<>(tempMap);
        Iterator<String> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

        while (iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            tempList.add(tm.get(key));
        }
        return tempList;
    }

    public static List getSortHolyList(ArrayList<HolyModel> holyModels) {

        HashMap<Integer, HolyModel> tempMap = new HashMap<>();

        Integer c = 0;
        for (HolyModel elem : holyModels) {
            HolyModel elemTemp = elem;
            tempMap.put(c, elemTemp);
            c++;
        }

        List<HolyModel> tempList = new ArrayList<>();

        TreeMap<Integer, HolyModel> tm = new TreeMap<>(tempMap);
        Iterator<Integer> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

        while (iteratorKey.hasNext()) {
            Integer key = iteratorKey.next();
            tempList.add(tm.get(key));
        }
        return tempList;
    }

    public static List getSortMemberList(ArrayList<HolyModel.memberModel> members) {

        HashMap<String, HolyModel.memberModel> tempMap = new HashMap<>();

        for (HolyModel.memberModel elem : members) {
            HolyModel.memberModel elemTemp = elem;
            tempMap.put(elemTemp.name + "|" + elemTemp.uid, elemTemp);
        }

        List<HolyModel.memberModel> tempList = new ArrayList<>();

        TreeMap<String, HolyModel.memberModel> tm = new TreeMap<>(tempMap);
        Iterator<String> iteratorKey = tm.keySet().iterator();   //키값 오름차순 정렬(기본)
        //Iterator<String> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬

        while (iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            tempList.add(tm.get(key));
        }
        return tempList;
    }

    /**
     * 문자열의 숫자를 뽑아 리턴
     *
     * @param str 숫자가 뒤에 들어가도록...
     * @return 리턴되는 숫자
     */
    public static Integer getInteger(String str) {
        String tempStr = "";

        try {
            int tempNum = Integer.parseInt(str);
            if (tempNum >= 0) {
                return tempNum;
            }
        }catch (Exception e){
            LoggerHelper.d("getInteger  : [ " + str + " ] 은 문자열입니다.");
        }

        //charAt를 이용하여 숫자가 아니면 넘기는 식으로 해서 뽑아 낼 수 있다.
        for (int i = 0; i < str.length(); i++) {
            // 48 ~ 57은 아스키 코드로 0~9이다.
            if (48 <= str.charAt(i) && str.charAt(i) <= 57)
                tempStr += str.charAt(i);
        }
        return Integer.valueOf(tempStr);
    }
}
