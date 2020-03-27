package com.buel.holyhelpers.management;

import com.buel.holyhelpers.data.FDDatabaseHelper;
import com.buel.holyhelpers.model.HolyModel;

/**
 * Created by 19001283 on 2018-06-11.
 */
public class TeamManager implements Management<HolyModel.groupModel.teamModel> {

    private HolyModel.groupModel.teamModel teamModel;

    public HolyModel.groupModel.teamModel getTeamModel() {
        return teamModel;
    }

    public void setTeamModel(HolyModel.groupModel.teamModel teamModel) {
        this.teamModel = teamModel;
    }

    public TeamManager() {
    }

    public TeamManager(HolyModel.groupModel.teamModel teamModel) {
        this.teamModel = teamModel;
    }

    @Override
    public void insert(
            HolyModel.groupModel.teamModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendTeamInsertData(dataModel, listener);
    }

    @Override
    public void modify(
            HolyModel.groupModel.teamModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendTeamModifyData(dataModel, listener);
    }

    @Override
    public void delete(HolyModel.groupModel.teamModel dataModel, final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendTeamDeleteData(dataModel, listener);
    }

}
