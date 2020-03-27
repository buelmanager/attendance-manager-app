package com.buel.holyhelpers.model;

import java.util.HashMap;

/**
 * Created by 19001283 on 2018-07-12.
 */

public class DateModel {
    public HashMap<String , AttendModel> member;
    public String day;
    public String time;
    public String date;
    public String fdate;
    public String timestamp;
    public String year;
    public String month;
    public String teamUID;

    @Override
    public String toString() {
        return "DateModel{" +
                "member=" + member +
                '}';
    }
}
