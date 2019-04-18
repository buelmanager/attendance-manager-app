package com.buel.holyhelper.model

import java.util.*

data class AnalyticeAttendModel(
        var groupMap: HashMap<String, AnalyticeAttendModel.group>? = null
){
    class group (
        var date: HashMap<String, DateModel>? = null
    )
}