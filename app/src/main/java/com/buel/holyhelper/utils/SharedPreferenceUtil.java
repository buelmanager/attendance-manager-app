package com.buel.holyhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.buel.holyhelper.model.HolyModel;
import com.orhanobut.logger.LoggerHelper;

/**
 * Created by 19001283 on 2018-07-23.
 */

public class SharedPreferenceUtil {

    public static final String  TEAM_NAME ="teamName";
    public static final String  GROUP_NAME ="groupName";

    public static final String  GROUP_UID ="groupUid";
    public static final String  TEAM_UID ="teamUid";
    private static SharedPreferences setting;
    private static SharedPreferences.Editor editor;

    public static void init(Context context) {
        setting = context.getSharedPreferences("setting", 0);
        editor = setting.edit();
    }
    public static HolyModel.groupModel getGroupData()
    {
        HolyModel.groupModel group = new HolyModel.groupModel();
        group.uid = getDatea("groupUid" , null);
        group.name = getDatea("groupName" , null);
        group.leader = getDatea("groupLeader" , null);
        return  group;
    }

    public static HolyModel.groupModel.teamModel getTeamData()
    {
        HolyModel.groupModel.teamModel team= new HolyModel.groupModel.teamModel();
        team.uid = getDatea("teamUid" , null);
        team.name = getDatea("teamName" , null);
        team.leader = getDatea("teamLeader" , null);
        return  team;
    }
    public static void initModelData(){
        LoggerHelper.d("SharedPreferenceUtil" ,"모든 데이터를 초기화합니다.");
        initGroupModel();
        initTeamModel();
    }
    public static void initGroupModel() {
        LoggerHelper.d("SharedPreferenceUtil" ,"그룹 데이터를 초기화합니다.");
        putDatea("groupUid", null);
        putDatea("groupName", null);
        putDatea("groupLeader", null);
    }

    public static void initTeamModel() {
        LoggerHelper.d("SharedPreferenceUtil" ,"팀 데이터를 초기화합니다.");
        putDatea("teamUid", null);
        putDatea("teamName", null);
        putDatea("teamLeader", null);
    }

    public static void putTeamModel(HolyModel.groupModel.teamModel team) {
        LoggerHelper.d("SharedPreferenceUtil 팀 데이터를 세팅합니다." ,team.toString());
        putDatea("teamUid", team.uid);
        putDatea("teamName", team.name);
        putDatea("teamLeader", team.leader);
    }

    public static void putGroupModel(HolyModel.groupModel group) {
        LoggerHelper.d("SharedPreferenceUtil 그룹 데이터를 세팅합니다." ,group.toString());
        putDatea("groupUid", group.uid);
        putDatea("groupName", group.name);
        putDatea("groupLeader", group.leader);
    }

    public static void putDatea(String key, String value) {
        try {
            editor.putString(key, value);
            editor.commit();
        }catch (Exception e)
        {
            LoggerHelper.e(e.getMessage());
        }
    }

    public static String getDatea(String key, String value) {
        return setting.getString(key, value);
    }
}
