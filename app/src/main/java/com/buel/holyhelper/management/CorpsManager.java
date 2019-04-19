package com.buel.holyhelper.management;

import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.model.HolyModel;

import java.util.Map;

/**
 * Created by 19001283 on 2018-06-11.
 */
public class CorpsManager implements Management<HolyModel> {

    private HolyModel holyModel;

    public HolyModel getHolyModel() {
        return holyModel;
    }

    public void setHolyModel(HolyModel holyModel) {
        this.holyModel = holyModel;
    }

    public CorpsManager() {
    }

    public CorpsManager(HolyModel holyModel) {
        this.holyModel = holyModel;
    }

    @Override
    public void insert(
            HolyModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendCorpsInsertData(dataModel, listener);
    }


    @Override
    public void modify(
            HolyModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendCorpsModifyData(dataModel, listener);
    }


    public void modify(
            HolyModel dataModel,
            Map<String, Object> map,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendCorpsModifyMapData(dataModel, map, listener);
    }


    @Override
    public void delete(
            HolyModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendCorpsDeleteData(dataModel, listener);
    }

}
