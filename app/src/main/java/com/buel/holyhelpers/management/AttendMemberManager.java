package com.buel.holyhelpers.management;

import com.buel.holyhelpers.data.FDDatabaseHelper;
import com.buel.holyhelpers.model.AttendModel;

/**
 * Created by 19001283 on 2018-06-11.
 */
public class AttendMemberManager
        implements Management<AttendModel> {

    @Override
    public void insert(
            AttendModel dataModel,
            final OnCompleteListener listener) {
        //FDDatabaseHelper.sendAttendInsertData(dataModel, listener);
    }



    @Override
    public void modify(
            AttendModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendAttendModifyData(dataModel, listener);
    }



    @Override
    public void delete(
            AttendModel dataModel,
            final OnCompleteListener listener) {
        //FDDatabaseHelper.sendAttendDeleteData(dataModel, listener);
    }


}
