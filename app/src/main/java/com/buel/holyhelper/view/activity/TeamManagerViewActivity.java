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
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.management.Management;
import com.buel.holyhelper.management.TeamManager;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.DataTypeListener;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;

public class TeamManagerViewActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "teamManagerViewActivity";
    EditText etName;
    EditText etLeader;
    EditText etEtc;
    TextView tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manager_view);

        tvDesc = findViewById(R.id.recycler_view_main_tv_desc);
        etName = findViewById(R.id.team_manager_activity_view_et_name);
        etLeader = findViewById(R.id.team_manager_activity_view_et_leader);
        etEtc = findViewById(R.id.team_manager_activity_view_et_etc);

        //etName.setText(CommonString.DEFINITION_NAME_U_NUMBER_NICK);
        //etEtc.setText(CommonString.DEFINITION_NAME_TEAM_NICK + "(필요한 경우 입력해주세요.)");
        //etLeader.setText(CommonString.DEFINITION_NAME_LEADER);

        setFocusEditText(etName);
        setFocusEditText(etLeader);
        setFocusEditText(etEtc);
        setTopLayout(this);

        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            super.setTopTitleDesc("편집");

            etName.setText(getInteger((CommonData.getSelectedTeam().name)).toString());
            etLeader.setText(CommonData.getSelectedTeam().leader);
            etEtc.setText(CommonData.getSelectedTeam().etc);

            TextView textViewGuide = findViewById(R.id.manager_tv_title);
            textViewGuide.setText("* 아래의 내용을 수정할 수 있습니다.");

        } else if (CommonData.getViewMode() == ViewMode.ADMIN) {
            super.setTopTitleDesc(CommonString.TEAM_NICK + " 생성");
        }
    }

    private void setTitle(String str) {
        tvDesc.setText(str);
    }

    /**
     * 문자열의 숫자를 뽑아 리턴
     *
     * @param str 숫자가 뒤에 들어가도록...
     * @return 리턴되는 숫자
     */
    public static Integer getInteger(String str) {
        String tempStr = "";
        //charAt를 이용하여 숫자가 아니면 넘기는 식으로 해서 뽑아 낼 수 있다.
        for (int i = 0; i < str.length(); i++) {
            // 48 ~ 57은 아스키 코드로 0~9이다.
            if (48 <= str.charAt(i) && str.charAt(i) <= 57)
                tempStr += str.charAt(i);
        }
        return Integer.valueOf(tempStr);
    }

    private void sendServer() {

        LoggerHelper.e("sendServer 1");
        LoggerHelper.e("CommonData.getHolyModel() : " + CommonData.getHolyModel());
        LoggerHelper.e("CommonData.getGroupModel() : " + CommonData.getGroupModel());

        isSaving = true;
        if (CommonData.getHolyModel() == null || CommonData.getGroupModel() == null) {
            isSaving = false;
            return;
        }

        ArrayList<HolyModel.groupModel.teamModel> teamArrayList = getData();

        if (etName.getText().toString().equals("")) {
            SuperToastUtil.toast(TeamManagerViewActivity.this, CommonString.DEFINITION_NAME_U_NUMBER_NICK + " 을 입력해주세요.");
            isSaving = false;
            return;
        }
        int tempCnt = -1;

        try {
            tempCnt = Integer.parseInt((etName.getText().toString()));
        } catch (Exception e) {
            SuperToastUtil.toast(TeamManagerViewActivity.this, CommonString.DEFINITION_NAME_U_NUMBER_NICK + "에 '0' 이상의 숫자만 입력해주세요.");
            isSaving = false;
            return;
        }

        if (tempCnt < 0) {
            SuperToastUtil.toast(TeamManagerViewActivity.this, CommonString.DEFINITION_NAME_U_NUMBER_NICK + " 에 '0' 이상의 숫자만 입력해주세요.");
            isSaving = false;
            return;
        }

        Boolean isCompare = getCompareData(etName.getText().toString(), teamArrayList);

        if (isCompare) {
            SuperToastUtil.toast(TeamManagerViewActivity.this, "같은" + CommonString.DEFINITION_NAME_U_NUMBER_NICK + "가 있습니다.");
            Log.d(TAG, "sendServer: isCompare = " + isCompare);
            isSaving = false;
            return;
        }


        TeamManager teamManager = new TeamManager();
        HolyModel.groupModel.teamModel teamModel = new HolyModel.groupModel.teamModel();
        teamModel.name = etName.getText().toString();
        teamModel.leader = etLeader.getText().toString();
        teamModel.groupUid = CommonData.getGroupModel().uid;

        if (etEtc.getText().length() > 0)
            teamModel.etc = etEtc.getText().toString();
        else
            teamModel.etc = CommonData.getGroupModel().name;

        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            teamManager.insert(teamModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "onComplete: teamManager insert complete!!!");

                    FDDatabaseHelper.INSTANCE.getTeamDataToStore(new DataTypeListener.OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(QuerySnapshot queryDocumentSnapshots) {

                            if (CommonData.isTutoMode()) {
                                try {
                                    ArrayList<HolyModel.groupModel.teamModel> teams
                                            = (ArrayList<HolyModel.groupModel.teamModel>) SortMapUtil.getSortTeamList();
                                    for (HolyModel.groupModel.teamModel eleteam : teams) {
                                        CommonData.setTeamModel(eleteam);
                                    }
                                } catch (Exception e) {
                                }
                                MaterialDailogUtil.Companion.simpleDoneDialog(TeamManagerViewActivity.this,
                                        "#4 단계, 최초 회원 등록을 진행합니다.", new MaterialDailogUtil.OnDialogSelectListner() {
                                            @Override
                                            public void onSelect(String s) {
                                                goSetAddMember();
                                            }
                                        });
                            } else {
                                isSaving = false;
                                goTeamRecycler();
                            }

                        }
                    });
                }
            });
        } else if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            teamModel.uid = CommonData.getTeamUid();
            teamManager.modify(teamModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "onComplete: teamManager modify complete!!!");

                    FDDatabaseHelper.INSTANCE.getTeamDataToStore(new DataTypeListener.OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(QuerySnapshot queryDocumentSnapshots) {
                            isSaving = false;
                            goTeamRecycler();
                        }
                    });
                }
            });
        }
    }

    public ArrayList getData() {
        ArrayList<HolyModel.groupModel.teamModel> teams = new ArrayList<>();
        HolyModel.groupModel groupModel = CommonData.getGroupModel();
        try {
            teams = (ArrayList<HolyModel.groupModel.teamModel>) SortMapUtil.getSortTeamList();
        } catch (Exception e) {
        }
        return teams;
    }

    public boolean getCompareData(String strComare, ArrayList<HolyModel.groupModel.teamModel> teams) {
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            if (CommonData.getSelectedTeam().name.equals(strComare)) {
                return false;
            }
        }

        LoggerHelper.d("getCompareData start teams.size " + teams.size());
        for (HolyModel.groupModel.teamModel eleTeam : teams) {
            LoggerHelper.d("eleTeam.name : " + eleTeam.name + " // strComare : " + strComare);
            if (SortMapUtil.getInteger(eleTeam.name) == SortMapUtil.getInteger(strComare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_btn_ok:

                if (!CommonData.getUserModel().userType.equals(UserType.SUPER_ADMIN.toString())) {
                    String title = "권한이 없습니다.";
                    String ment = CommonData.getUserModel().userType + " 유저는 해당 권한이없습니다. 관리자에게 문의하세요.";
                    MaterialDailogUtil.Companion.simpleDoneDialog(TeamManagerViewActivity.this, title, ment, new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {
                            goGroupRecycler();
                        }
                    });

                    isSaving = false;
                    return;
                }

                Common.hideKeyboard(TeamManagerViewActivity.this);

                if (!isSaving) {
                    sendServer();
                } else {
                    SuperToastUtil.toastE(this, "저장중입니다. 잠시만 기다리세요.");
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
