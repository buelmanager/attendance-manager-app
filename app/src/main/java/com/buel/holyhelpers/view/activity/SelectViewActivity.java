package com.buel.holyhelpers.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.data.CommonString;
import com.buel.holyhelpers.data.FDDatabaseHelper;
import com.buel.holyhelpers.data.TutorialViewerUtil;
import com.buel.holyhelpers.data.ViewMode;
import com.buel.holyhelpers.management.MemberManager;
import com.buel.holyhelpers.view.SimpleListener;
import com.orhanobut.logger.LoggerHelper;

@SuppressLint("NewApi")
public class SelectViewActivity extends BaseActivity implements View.OnClickListener {

    EditText editTextSerach = null;
    TextView textViewCorpsTitle;
    TextView textViewCorpsDesc;
    TextView textViewGroupTitle;
    TextView textViewGroupDesc;
    TextView textViewTeamTitle;
    TextView textViewTeamDesc;
    TextView textViewMemberTitle;
    TextView textViewMemberDesc;
    TextView textVioewAllMemberTitle;
    TextView textVioewAllMemberDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity_custom);

        editTextSerach = findViewById(R.id.preference_activity_edittext_search);
        setFocusEditText(editTextSerach);

        findViewById(R.id.preference_activity_btn_search_member).setOnClickListener(this);
        findViewById(R.id.preference_activity_edittext_search).setOnClickListener(this);
        findViewById(R.id.preference_activity_ll_4).setOnClickListener(this);
        findViewById(R.id.preference_activity_ll_1).setOnClickListener(this);
        findViewById(R.id.preference_activity_ll_2).setOnClickListener(this);
        findViewById(R.id.preference_activity_ll_3).setOnClickListener(this);
        findViewById(R.id.preference_activity_ll_5).setOnClickListener(this);

        textViewCorpsTitle = findViewById(R.id.select_activity_ll1_tv1);
        textViewCorpsDesc = findViewById(R.id.select_activity_ll1_tv2);
        textViewGroupTitle = findViewById(R.id.select_activity_ll2_tv1);
        textViewGroupDesc = findViewById(R.id.select_activity_ll2_tv2);
        textViewTeamTitle = findViewById(R.id.select_activity_ll3_tv1);
        textViewTeamDesc = findViewById(R.id.select_activity_ll3_tv2);
        textViewMemberTitle = findViewById(R.id.select_activity_ll4_tv1);
        textViewMemberDesc = findViewById(R.id.select_activity_ll4_tv2);

        textVioewAllMemberTitle = findViewById(R.id.select_activity_ll5_tv1);
        textVioewAllMemberDesc = findViewById(R.id.select_activity_ll5_tv2);

        textViewCorpsTitle.setText(CommonString.CORP_NICK + "정보가 설정되지 않음");
        textViewCorpsDesc.setText("클릭하여 설정해주세요");
        textViewGroupTitle.setText(CommonString.GROUP_NICK + "정보가 설정되지 않음");
        textViewGroupDesc.setText("클릭하여 설정해주세요");

        textViewTeamTitle.setText(CommonString.TEAM_NICK + "정보가 설정되지 않음");
        textViewTeamDesc.setText("클릭하여 설정해주세요");
        textViewMemberTitle.setText("[ 부서 " + CommonString.MEMBER_NICK + "  관리 ]");
        textViewMemberDesc.setText("클릭하여 확인/수정/삭제가 가능합니다.");

        textVioewAllMemberTitle.setText("[ 전체 교적관리 ] ");
        textVioewAllMemberDesc.setText("클릭하여 확인/수정/삭제가 가능합니다.");

        setFocusEditText(findViewById(R.id.preference_activity_edittext_search));

        FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                showProgressDialog(false);
                setLayout();
            }
        });

        CommonData.setViewMode(ViewMode.ADMIN);
        super.setTopTitleDesc("선택·추가·관리");
        super.setTopLayout(this);
        super.setBaseFloatingActionButton();
        //super.setFabBackImg(super.fab2ndBtn, R.drawable.ic_anal);
        super.setFabSnd(ViewMode.BRIEFING);
        super.setFabFst(ViewMode.ATTENDANCE);
        //super.isPageAction = false;
        //super.fabHelper.setX(155);

        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_search);
    }

    @Override
    public void setActionButton() {
        LoggerHelper.d("recycler_view_main_fab !!!!");
        MemberManager.searchMember(this, new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                goMemberRecycler();
            }
        });
    }

    @Override
    public void setSndFabBtn() {
        goMain();
    }

    @Override
    public void setHelperButton() {
        TutorialViewerUtil.getMemberAccountAdminTutorialModels(SelectViewActivity.this);
    }

    private void setLayout() {
        setAdminMode();

        String cName = "";
        String cphone = "";
        String caddress = "";
        String cowner = "";

        if (CommonData.getHolyModel() != null) {
            cName = CommonData.getHolyModel().name;
            if (CommonData.getHolyModel().owner != null) cphone = CommonData.getHolyModel().name;
            if (CommonData.getHolyModel().phone != null) caddress = CommonData.getHolyModel().phone;
            if (CommonData.getHolyModel().address != null)
                cowner = CommonData.getHolyModel().address;

            textViewCorpsTitle.setText(Html.fromHtml(CommonString.CORP_NICK + " : " + cName));
            textViewCorpsDesc.setText(Html.fromHtml(
                    "<strong>" + " 목사 " + "</strong>" + cowner + "<br>" +
                            "<strong>" + " 전화 " + "</strong>" + cphone + "<br>" +
                            "<strong>" + " 주소 " + "</strong>" + caddress + "<br>"));
        } else {
            CommonData.setCurTeamModel(null);
            CommonData.setMemberModel(null);
            return;
        }

        String gName = "";
        String gEtc = "";
        String gLeader = "";

        if (CommonData.getGroupModel() != null) {
            if (CommonData.getGroupModel().name != null) gName = CommonData.getGroupModel().name;
            if (CommonData.getGroupModel().leader != null)
                gLeader = CommonData.getGroupModel().leader;
            if (CommonData.getGroupModel().leader != null) gEtc = CommonData.getGroupModel().etc;
            textViewGroupTitle.setText((CommonString.GROUP_NICK + " : " + gName));
            textViewGroupDesc.setText(Html.fromHtml(
                    "<strong>" + CommonString.DEFINITION_NAME_LEADER + "</strong> " + gLeader + "<br>" +
                            "<strong>" + CommonString.DEFINITION_NAME_DEFAULT + "</strong> " + gEtc));
        } else {
            CommonData.setCurTeamModel(null);
            CommonData.setMemberModel(null);
            return;
        }

        String tName = "";
        String tleader = "";
        String tetc = "";

        if (CommonData.getTeamModel() != null) {

            if (CommonData.getTeamModel().name != null) tName = CommonData.getTeamModel().name;
            if (CommonData.getTeamModel().leader != null)
                tleader = CommonData.getTeamModel().leader;
            if (CommonData.getTeamModel().etc != null) tetc = CommonData.getTeamModel().etc;

            textViewTeamTitle.setText(Html.fromHtml(CommonString.TEAM_NICK + " : " + tName));
            textViewTeamDesc.setText(Html.fromHtml(
                    "<strong>" + CommonString.DEFINITION_NAME_LEADER + "</strong> " + tleader + "<br>" +
                            "<strong>" + CommonString.DEFINITION_NAME_TEAM_NICK + "</strong> " + tetc));


        } else {
            CommonData.setCurTeamModel(null);
            CommonData.setMemberModel(null);
            return;
        }
    }

    private void setAdminMode() {
        CommonData.setViewMode(ViewMode.ADMIN);
        findViewById(R.id.select_activity_top).setVisibility(View.GONE);
        findViewById(R.id.top_bar_btn_ok).setBackgroundResource(R.drawable.ic_face_white_24dp);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //멤버 찾기버튼
        if (v.getId() == R.id.preference_activity_btn_search_member) {
            /*CommonData.setViewMode(ViewMode.SEARCH_MEMBER);
            String strName = editTextSerach.getText().toString();
            LoggerHelper.d("btnSelectSearchMember", "strName : " + strName);

            CommonData.setStrSearch(strName);
            goMemberRecycler();*/

            MemberManager.searchMember(this, new SimpleListener.OnCompleteListener() {
                @Override
                public void onComplete() {
                    goMemberRecycler();
                }
            });
        }

        //
        else if (v.getId() == R.id.top_bar_btn_ok) {
            /*if (CommonData.getViewMode() == ViewMode.ADMIN) {
                setSearchMode();
            } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                setAdminMode();
            }*/

            MemberManager.searchMember(this, new SimpleListener.OnCompleteListener() {
                @Override
                public void onComplete() {
                    goMemberRecycler();
                }
            });

        } else if (v.getId() == R.id.top_bar_btn_back) {
            goMain();
            //CommonData.setHistoryClass((Class)MainActivity.class);
        }

        //단체 선택/수정
        else if (v.getId() == R.id.preference_activity_ll_1) {
            goSetCorps();
        }

        //그룹 선택/수정
        else if (v.getId() == R.id.preference_activity_ll_2) {
            goGroupRecycler();
        }

        //팀 선택/수정
        else if (v.getId() == R.id.preference_activity_ll_3) {
            goTeamRecycler();
        }

        //멤버 선택/수정
        else if (v.getId() == R.id.preference_activity_ll_4) {
            if (CommonData.getViewMode() == ViewMode.BRIEFING) {
                goBriefing();
            } else if (CommonData.getViewMode() == ViewMode.ADMIN) {
                goMemberRecycler();
            }
        } else if (v.getId() == R.id.preference_activity_ll_5) {
            CommonData.setViewMode(ViewMode.SEARCH_MEMBER);
            String strName = "";
            LoggerHelper.d("btnSelectSearchMember", "strName : " + strName);
            CommonData.setStrSearch(strName);
            //goMemberRecycler();
            goManageMentPage();
            CommonData.historyClass = (Class) SelectViewActivity.this.getClass();
        } else {
            LoggerHelper.d("[ " + v.getId() + " ] 없는 ID 입니다.");
        }
    }
}
