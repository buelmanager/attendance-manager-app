package com.buel.holyhelpers.management;

import com.buel.holyhelpers.data.FDDatabaseHelper;
import com.buel.holyhelpers.model.HolyModel;

/**
 * Created by 19001283 on 2018-06-11.
 */
public class GroupManager implements Management<HolyModel.groupModel> {

    private HolyModel.groupModel groupModel;
    public HolyModel.groupModel getGroupModel() {
        return groupModel;
    }
    public void setGroupModel(HolyModel.groupModel groupModel) {
        this.groupModel = groupModel;
    }

    public GroupManager() {}
    public GroupManager(HolyModel.groupModel groupModel) {
        this.groupModel = groupModel;
    }

    @Override
    public void insert(
            HolyModel.groupModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendGroupInsertData(dataModel, listener);
    }



    @Override
    public void modify(
            HolyModel.groupModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendGroupModifyData(dataModel, listener);
    }



    @Override
    public void delete(HolyModel.groupModel dataModel, final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendGroupDeleteData(dataModel, listener);
    }
}