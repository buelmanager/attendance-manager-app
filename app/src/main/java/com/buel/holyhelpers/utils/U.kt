package com.buel.holyhelpers.utils

import com.buel.holyhelpers.model.HolyModel

object U {
    fun HashMap<String , HolyModel>.corpsCovertList ():List<HolyModel>{
        var tempList : ArrayList<HolyModel> = arrayListOf()
        for ((k, v) in this) {
            tempList.add(v)
        }
        return tempList
    }

    fun HashMap<String , HolyModel.memberModel>.memberaCovertList ():List<HolyModel.memberModel>{
        var tempList : ArrayList<HolyModel.memberModel> = arrayListOf()
        for ((k, v) in this) {
            tempList.add(v)
        }
        tempList.sortBy { it.groupName }
        return tempList
    }

    fun HashMap<String , HolyModel.groupModel>.groupCovertList ():List<HolyModel.groupModel>{
        var tempList : ArrayList<HolyModel.groupModel> = arrayListOf()
        for ((k, v) in this) {
            tempList.add(v)
        }
        tempList.sortBy { it.name }
        return tempList
    }

    fun HashMap<String , HolyModel.groupModel.teamModel>.teamCovertList ():List<HolyModel.groupModel.teamModel>{
        var tempList : ArrayList<HolyModel.groupModel.teamModel> = arrayListOf()
        for ((k, v) in this) {
            tempList.add(v)
        }

        tempList.sortBy { it.name }
        return tempList
    }

    fun ArrayList<HolyModel.memberModel>.memberSort():ArrayList<HolyModel.memberModel>
    {
        this.sortBy { it.name }
        return this
    }

    fun ArrayList<HolyModel>.corpsConvertMap():Map<String,HolyModel>
    {
        val tempMap:HashMap<String,HolyModel> = hashMapOf()
        for (v in this) {
            val tempModel:HolyModel = v
            tempMap[tempModel.uid] = tempModel
        }
        return tempMap
    }

    fun ArrayList<HolyModel.groupModel>.groupConvertMap():Map<String, HolyModel.groupModel>
    {
        val tempMap:HashMap<String, HolyModel.groupModel> = hashMapOf()
        for (v in this) {
            val tempModel: HolyModel.groupModel = v
            tempMap[tempModel.uid] = tempModel
        }
        return tempMap
    }

    fun ArrayList<HolyModel.groupModel.teamModel>.teamConvertMap():Map<String, HolyModel.groupModel.teamModel>
    {
        val tempMap:HashMap<String, HolyModel.groupModel.teamModel> = hashMapOf()
        for (v in this) {
            val tempModel: HolyModel.groupModel.teamModel = v
            tempMap[tempModel.uid] = tempModel
        }
        return tempMap
    }


}