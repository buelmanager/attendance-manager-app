package com.buel.holyhelper.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.UserType;
import com.buel.holyhelper.management.GroupManager;
import com.buel.holyhelper.management.Management;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.DataTypeListener;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.Map;

public class GroupManagerViewActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "GroupManagerViewActivity";
    EditText selectedEt;
    EditText etName;
    EditText etLeader;
    EditText etEtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manaber_view);

        etName = findViewById(R.id.group_manager_activity_view_et_name);
        etLeader = findViewById(R.id.group_manager_activity_view_et_leader);
        etEtc = findViewById(R.id.group_manager_activity_view_et_etc);

        setFocusEditText(etName);
        setFocusEditText(etLeader);
        setFocusEditText(etEtc);

        super.setTopLayout(this);
        setTitle(CommonString.INFO_TITLE_CONTROL_GROUP);

        LoggerHelper.d(CommonData.getViewMode());
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {

            if (CommonData.getSelectedGroup() == null) {
                popToast(CommonString.INFO_TITLE_SELECTL_GROUP);
                goBackHistory();
            }
            super.setTopTitleDesc("편집");
            etName.setText(CommonData.getSelectedGroup().name);
            etLeader.setText(CommonData.getSelectedGroup().leader);
            etEtc.setText(CommonData.getSelectedGroup().etc);

            TextView textViewGuide = findViewById(R.id.manager_tv_title);
            textViewGuide.setText("* 아래의 내용을 수정할 수 있습니다.");

        } else if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            super.setTopTitleDesc(CommonString.GROUP_NICK + " 생성");
        }
    }

    @SuppressLint("LongLogTag")
    private void sendServer() {
        isSaving = true;
        LoggerHelper.d("CommonData.getHolyModel() : " + CommonData.getHolyModel());
        if (CommonData.getHolyModel() == null) {
            goLogout();
            isSaving = false;
            return;
        }
        ArrayList<HolyModel.groupModel> groupList = getData();
        Boolean isCompare = getCompareData(etName.getText().toString(), groupList);

        if (etName.getText().toString().equals("")) {
            SuperToastUtil.toast(this, CommonString.DEFINITION_NAME_NICK + "을 입력해주세요.");
            isSaving = false;
            return;
        }

        if (isCompare) {
            SuperToastUtil.toast(GroupManagerViewActivity.this, "같은 " + CommonString.DEFINITION_NAME_NICK + "이 있습니다.");
            isSaving = false;
            Log.d(TAG, "sendServer: isCompare = " + isCompare);
            return;
        }

        GroupManager groupManager = new GroupManager();
        HolyModel.groupModel groupModel = new HolyModel.groupModel();
        groupModel.name = etName.getText().toString();
        groupModel.leader = etLeader.getText().toString();
        groupModel.etc = etEtc.getText().toString();

        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            groupModel.uid = CommonData.getGroupUid();
            groupManager.modify(groupModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "onComplete: memberManager insert complete!!!");

                    FDDatabaseHelper.INSTANCE.getGroupDataToStore(new DataTypeListener.OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(QuerySnapshot queryDocumentSnapshots) {
                            goGroupRecycler();
                            isSaving = false;
                        }
                    });
                }
            });
        } else if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            groupManager.insert(groupModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "onComplete: memberManager insert complete!!!");

                    FDDatabaseHelper.INSTANCE.getGroupDataToStore(new DataTypeListener.OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(QuerySnapshot queryDocumentSnapshots) {
                            if (CommonData.isTutoMode()) {
                                try {
                                    ArrayList<HolyModel.groupModel> groups
                                            = (ArrayList<HolyModel.groupModel>) SortMapUtil.getSortGroupList(CommonData.getHolyModel().group);

                                    LoggerHelper.d("groups :  groups.size() " + groups.size());

                                    for (HolyModel.groupModel eleGroup : groups) {

                                        //여기는 uid 가 아닌 name 으로 비교한다. 아직  uid를 얻기 이전
                                        if (eleGroup.name.equals(groupModel.name)) {
                                            CommonData.setGroupModel(eleGroup);
                                        }
                                    }
                                } catch (Exception e) {
                                }
                                MaterialDailogUtil.Companion.simpleDoneDialog(GroupManagerViewActivity.this,
                                        "#3 단계, 소그룹 설정을 진행합니다.", s -> goSetTeam());
                            } else {
                                goGroupRecycler();
                                finish();
                            }
                        }
                    });
                }
            });
        }
    }

    public ArrayList getData() {
        //Log.d(TAG, "getMyCorpsData: adminUID " + CommonData.getAdminUid());
        final ArrayList<HolyModel.groupModel> groups = new ArrayList<>();
        try {
            for (Map.Entry<String, HolyModel.groupModel> elem : CommonData.getHolyModel().group.entrySet()) {
                HolyModel.groupModel eleGroup = elem.getValue();
                eleGroup.uid = elem.getKey();
                groups.add(eleGroup);
            }
        } catch (Exception e) {
            //Log.d("GroupRecyclerViewActivity", e.getMessage());
        }
        return groups;
    }

    public boolean getCompareData(String strComare, ArrayList<HolyModel.groupModel> groups) {
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            if (CommonData.getGroupModel().name.equals(strComare)) {
                return false;
            }
        }

        if (strComare.equals("")) {
            SuperToastUtil.toast(GroupManagerViewActivity.this, CommonString.DEFINITION_NAME_NICK + "을 입력해주세요.");
            return true;
        }

        for (HolyModel.groupModel eleGroup : groups) {
            if ((eleGroup.name).equals(strComare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_btn_ok:


                LoggerHelper.d("CommonData.getUserModel().userType : " + CommonData.getUserModel().userType);
                LoggerHelper.d("UserType.SUPER_ADMIN.toString()) : " + UserType.SUPER_ADMIN.toString());
                if (!CommonData.getUserModel().userType.equals(UserType.SUPER_ADMIN.toString())) {
                    String title = "권한이 없습니다.";
                    String ment = CommonData.getUserModel().userType + " 유저는 해당 권한이없습니다. 관리자에게 문의하세요.";
                    MaterialDailogUtil.Companion.simpleDoneDialog(GroupManagerViewActivity.this, title, ment, new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {
                            goGroupRecycler();
                        }
                    });
                    isSaving = false;
                    return;
                }

                Common.hideKeyboard(GroupManagerViewActivity.this);

                if (!isSaving) {
                    sendServer();
                } else {
                    SuperToastUtil.toastE(this, "저장 중 입니다. 잠시만 기다리세요.");
                }
                break;

            case R.id.top_bar_btn_back:
                goBackHistory();
                break;

            default:
                break;
        }
    }
}
