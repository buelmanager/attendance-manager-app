package com.buel.holyhelper.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by blue7 on 2018-07-06.
 */

public class AttendModel {

    @Override
    public String toString() {
        return "AttendModel{" +
                "timestamp='" + timestamp + '\'' +
                ", memberPosition='" + memberPosition + '\'' +
                ", memberName='" + memberName + '\'' +
                ", isExecutives='" + isExecutives + '\'' +
                ", attend='" + attend + '\'' +
                ", corpsUID='" + corpsUID + '\'' +
                ", groupUID='" + groupUID + '\'' +
                ", teamUID='" + teamUID + '\'' +
                ", memberUID='" + memberUID + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", fdate='" + fdate + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", reason='" + reason + '\'' +
                ", noAttendReason='" + noAttendReason + '\'' +
                '}';
    }

    public String timestamp;
    public String memberPosition;
    public String memberName;
    public String isExecutives;
    public String attend;
    public String corpsUID;
    public String groupUID;
    public String teamUID;
    public String memberUID;
    public String day;
    public String time;
    public String date;
    public String fdate;
    public String year;
    public String month;
    public String reason;
    public String noAttendReason;

    public Map<String, Object> convertMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", timestamp);
        map.put("attend", attend);
        map.put("corpsUID", corpsUID);
        map.put("groupUID", groupUID);
        map.put("isExecutives", isExecutives);
        map.put("memberName", memberName);
        map.put("teamUID", teamUID);
        map.put("memberPosition", memberPosition);
        map.put("memberUID", memberUID);
        map.put("time", time);
        map.put("noAttendReason", noAttendReason);
        map.put("fdate", fdate);
        map.put("date", date);
        map.put("day", day);
        map.put("year", year);
        map.put("month", month);
        return map;
    }
}
