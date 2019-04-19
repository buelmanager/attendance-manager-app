package com.buel.holyhelper.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 19001283 on 2018-07-16.
 */

public class AnalyticsModel {
    public Map<String, Attendance> attend;

    public static class Attendance {
        public HashMap<String, group> group;

        public static class group {
            public HashMap<String, DateModel> date;
        }
    }
}
