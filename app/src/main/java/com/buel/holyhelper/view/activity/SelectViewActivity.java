package com.buel.holyhelper.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.TutorialViewerUtil;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.management.MemberManager;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.SimpleListener;
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

        /*String strHelper = "";

        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            strHelper = "<strong>† 수정/추가/관리</strong><br> " +
                    "버튼을 클릭하여 " +  CommonString.CORP_NICK+"/"+CommonString.GROUP_NICK+"/"+ CommonString.TEAM_NICK+" 선택 및 관리가 가능합니다." + "<br><br>" +
                    "<strong>† 멤버 선택</strong><br><br>" +
                    "<strong>† 멤버 찾기</strong><br> " +
                    "상단의 얼굴 버튼을 클릭하여 <br>멤버를 찾을수 있습니다.<br><br>" +
                    "입력된 단어를 포함한 <br>모든 멤버의 리스트가 노출됩니다.<br><br>" +
                    "빈칸을 넣을시에는 <br>전체 멤버가 노출됩니다.<br>" ;
        }else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            strHelper ="<strong>† 멤버 찾기</strong><br> " +
                    "상단의 얼굴 버튼을 클릭하여 <br>멤버를 찾을수 있습니다.<br><br>" +
                    "입력된 단어를 포함한 <br>모든 멤버의 리스트가 노출됩니다.<br><br>" +
                    "빈칸을 넣을시에는 <br>전체 멤버가 노출됩니다.<br>" ;
        }


        MaterialDailogUtil.noticeDialog(
                SelectViewActivity.this,
                strHelper,
                CommonString.INFO_HELPER_TITLE,
                new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {
                        CommonData.setIsFstEnter(false);
                        LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.getIsFstEnter());
                    }
                });*/
    }

    private void setLayout() {
        setAdminMode();

        if (CommonData.getHolyModel() != null) {
            textViewCorpsTitle.setText(CommonString.CORP_NICK + " : " + CommonData.getHolyModel().name);
            textViewCorpsDesc.setText(Html.fromHtml(
                    "<strong>" + " 목사 " + "</strong>" + CommonData.getHolyModel().owner + "<br>" +
                            "<strong>" + " 전화 " + "</strong>" + CommonData.getHolyModel().phone + "<br>" +
                            "<strong>" + " 주소 " + "</strong>" + CommonData.getHolyModel().address + "<br>"

            ));
        }

        if (CommonData.getGroupModel() != null) {
            textViewGroupTitle.setText(CommonString.GROUP_NICK + " : " + CommonData.getGroupModel().name);
            textViewGroupDesc.setText(Html.fromHtml(
                    "<strong>" + CommonString.DEFINITION_NAME_LEADER + "</strong> " + CommonData.getGroupModel().leader + "<br>" +
                            "<strong>" + CommonString.DEFINITION_NAME_DEFAULT + "</strong> " + CommonData.getGroupModel().etc
            ));
        } else {
            CommonData.setTeamModel(null);
            CommonData.setMemberModel(null);
            return;
        }

        if (CommonData.getTeamModel() != null) {
            textViewTeamTitle.setText(CommonString.TEAM_NICK + " : " + SortMapUtil.getInteger(CommonData.getTeamModel().name));
            textViewTeamDesc.setText(Html.fromHtml(
                    "<strong>" + CommonString.DEFINITION_NAME_LEADER + "</strong> " + CommonData.getTeamModel().leader + "<br>" +
                            "<strong>" + CommonString.DEFINITION_NAME_TEAM_NICK + "</strong> " + CommonData.getTeamModel().etc
            ));
        } else {
            CommonData.setTeamModel(null);
            CommonData.setMemberModel(null);
            return;
        }

        if (CommonData.getMemberModel() != null) {
            if (!CommonData.getMemberModel().groupUID.equals(CommonData.getGroupModel().uid) ||
                    !CommonData.getMemberModel().teamUID.equals(CommonData.getTeamModel().uid)) {
                CommonData.setMemberModel(null);
                return;
            }

            /*textViewMemberTitle.setText("MEMBER : " + CommonData.getMemberModel().name);
            textViewMemberDesc.setText(Html.fromHtml(
                    "<strong>" + " POSITION " + "</strong>" + CommonData.getMemberModel().position + "<br>" +
                            "<strong>" + " ADDRESS " + "</strong>" + CommonData.getMemberModel().address + "<br>" +
                            "<strong>" + " PHONE " + "</strong>" + CommonData.getMemberModel().phone
            ));*/
        } else {
            CommonData.setMemberModel(null);
            return;
        }
    }

    private void setModifyMode() {

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
            CommonData.setHistoryClass((Class) SelectViewActivity.this.getClass());
        } else {
            LoggerHelper.d("[ " + v.getId() + " ] 없는 ID 입니다.");
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
