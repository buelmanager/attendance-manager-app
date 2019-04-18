package com.buel.holyhelper.utils;

import android.content.Context;

import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.view.DataTypeListener;
import com.buel.holyhelper.view.SimpleListener;
import com.commonLib.MaterialDailogUtil;

/**
 * 설정하는 부분을 쉽게 지원한다.
 */
public class CorpsSettingManager {

    public static void setTeam(Context context) {
        MaterialDailogUtil.getTeamDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setTeamModel(CommonData.getSelectedTeam());
                    }
                });
    }

    public static void setGroup(Context context) {
        //@Deprecated : 그룹이동 불가능 하도록 수정
        MaterialDailogUtil.getGroupDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setGroupModel(CommonData.getSelectedGroup());
                        CommonData.setTeamModel(null);
                        setTeam(context);
                    }
                });
    }

    public static void setTeam(Context context,
                               DataTypeListener.OnCompleteListener<HolyModel.groupModel.teamModel> teamOnCompleteListener) {
        MaterialDailogUtil.getTeamDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setTeamModel(CommonData.getSelectedTeam());
                        if (teamOnCompleteListener != null) {
                            teamOnCompleteListener.onComplete(CommonData.getSelectedTeam());
                        }
                    }
                });
    }


    public static void setGroup(Context context,
                                DataTypeListener.OnCompleteListener<HolyModel.groupModel> groupOnCompleteListener) {
        //@Deprecated : 그룹이동 불가능 하도록 수정
        MaterialDailogUtil.getGroupDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setGroupModel(CommonData.getSelectedGroup());
                        CommonData.setTeamModel(null);

                        if (groupOnCompleteListener != null)
                            groupOnCompleteListener.onComplete(CommonData.getSelectedGroup());
                    }
                });
    }

    public static void setTeam(Context context,
                               SimpleListener.OnCompleteListener onCompleteListener) {
        MaterialDailogUtil.getTeamDialog(context,
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

        MaterialDailogUtil.getGroupDialog(context,
                new SimpleListener.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        CommonData.setTeamModel(null);
                        setTeam(context, onCompleteListener);
                    }
                });
    }

}
