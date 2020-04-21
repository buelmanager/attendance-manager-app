package com.buel.holyhelpers.utils;

import android.content.Context;

import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.model.HolyModel;
import com.buel.holyhelpers.view.DataTypeListener;
import com.buel.holyhelpers.view.SimpleListener;
import com.commonLib.MaterialDailogUtil;

/**
 * 설정하는 부분을 쉽게 지원한다.
 */
public class CorpsSettingManager {

    public static void setTeam(Context context) {
        MaterialDailogUtil.Companion.getTeamDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setCurTeamModel(CommonData.getSelectedTeam());
                    }
                });
    }

    public static void setGroup(Context context) {
        //@Deprecated : 그룹이동 불가능 하도록 수정
        MaterialDailogUtil.Companion.getGroupDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setCurGroupModel(CommonData.getSelectedGroup());
                        CommonData.setCurTeamModel(null);
                        setTeam(context);
                    }
                });
    }

    public static void setTeam(Context context,
                               DataTypeListener.OnCompleteListener<HolyModel.groupModel.teamModel> teamOnCompleteListener) {
        MaterialDailogUtil.Companion.getTeamDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setCurTeamModel(CommonData.getSelectedTeam());
                        if (teamOnCompleteListener != null) {
                            teamOnCompleteListener.onComplete(CommonData.getSelectedTeam());
                        }
                    }
                });
    }


    public static void setGroup(Context context,
                                DataTypeListener.OnCompleteListener<HolyModel.groupModel> groupOnCompleteListener) {
        //@Deprecated : 그룹이동 불가능 하도록 수정
        MaterialDailogUtil.Companion.getGroupDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setCurGroupModel(CommonData.getSelectedGroup());
                        CommonData.setCurTeamModel(null);

                        if (groupOnCompleteListener != null)
                            groupOnCompleteListener.onComplete(CommonData.getSelectedGroup());
                    }
                });
    }

    public static void setTeam(Context context,
                               SimpleListener.OnCompleteListener onCompleteListener) {
        MaterialDailogUtil.Companion.getTeamDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {

                        if (onCompleteListener != null) {
                            onCompleteListener.onComplete();
                        }
                    }
                });
    }


    public static void setGroup(Context context,
                                SimpleListener.OnCompleteListener onCompleteListener) {

        MaterialDailogUtil.Companion.getGroupDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setCurTeamModel(null);
                        setTeam(context, onCompleteListener);
                    }
                });
    }

}
