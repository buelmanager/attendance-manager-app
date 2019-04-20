package com.buel.holyhelper.management;

import android.content.Context;

import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.view.SimpleListener;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;

/**
 * Created by 19001283 on 2018-06-11.
 */
public class MemberManager
        implements Management<HolyModel.memberModel> {

    private HolyModel.memberModel memberModel;

    public HolyModel.memberModel getMemberModel() {
        return memberModel;
    }

    public void setMemberModel(HolyModel.memberModel memberModel) {
        this.memberModel = memberModel;
    }

    public MemberManager() {
    }

    public static void searchMember(Context context, SimpleListener.OnCompleteListener onCompleteListener) {
        MaterialDailogUtil.Companion.simpleInputDoneDialog(context, "이름을 입력하세요.", "이름", new MaterialDailogUtil.OnDialogSelectListner() {
            @Override
            public void onSelect(String s) {
                CommonData.setViewMode(ViewMode.SEARCH_MEMBER);
                String strName = s;
                LoggerHelper.d("btnSelectSearchMember", "strName : " + strName);
                CommonData.setStrSearch(strName);
                if (onCompleteListener != null) onCompleteListener.onComplete();
            }
        });
    }

    public MemberManager(HolyModel.memberModel memberModel) {
        this.memberModel = memberModel;
    }

    @Override
    public void insert(
            HolyModel.memberModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendMemberInsertData(dataModel, listener);
    }

    @Override
    public void modify(
            HolyModel.memberModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendMemberAddMapdata(dataModel, listener);
    }

    @Override
    public void delete(
            HolyModel.memberModel dataModel,
            final OnCompleteListener listener) {
        FDDatabaseHelper.INSTANCE.sendMemberDeleteData(dataModel, listener);
    }


}
