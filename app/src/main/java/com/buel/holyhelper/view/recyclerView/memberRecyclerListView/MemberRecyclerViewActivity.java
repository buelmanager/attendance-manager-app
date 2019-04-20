package com.buel.holyhelper.view.recyclerView.memberRecyclerListView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.management.AttendMemberManager;
import com.buel.holyhelper.management.MemberManager;
import com.buel.holyhelper.management.firestore.FireStoreAttendManager;
import com.buel.holyhelper.model.AttendModel;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.AdmobUtils;
import com.buel.holyhelper.utils.AppUtil;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.DataTypeListener;
import com.buel.holyhelper.view.SimpleListener;
import com.buel.holyhelper.view.activity.BaseActivity;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.LoggerHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MemberRecyclerViewActivity
        extends BaseActivity
        implements
        MemberRecyclerViewListener.OnCompleteListener,
        View.OnClickListener {

    private static final String TAG = "MemberRecyclerViewActivity";
    RecyclerView recyclerView;
    MemberRecyclerViewAdapter holderAdapter;
    private ViewMode isIntoMode;
    private Boolean isModifyMode = false;

    private int totalAttendCnt = 0;
    private int noAttendCnt = 0;
    private int okAttendCnt = 0;
    private boolean isAttendModifyed = false;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //LoggerHelper.d("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fd_cell_recycler_view);

        findViewById(R.id.recycler_view_main_iv_close).setOnClickListener(this);
        setTopLayout(this);
        super.setBaseFloatingActionButton();

        if (CommonData.getHolyModel() == null) {
            Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_CORP, Toast.LENGTH_SHORT).show();
            goSelect();
            return;
        }

        try {
            setTitle(CommonData.getGroupModel().name + " / " + CommonData.getTeamModel().name);
            //tvDesc.setText(CommonData.getGroupModel().name + " / " + CommonData.getTeamModel().name);
        } catch (Exception e) {
            if (CommonData.getViewMode() != ViewMode.SEARCH_MEMBER) {
                Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP, Toast.LENGTH_SHORT).show();
                goSelect();
            }
        }
        isIntoMode = CommonData.getViewMode();
        fabActionBtn = super.fabFstActBtn;
        //super.setFabBackImg(super.fab2ndBtn, R.drawable.ic_anal);
        super.setFabSnd(ViewMode.BRIEFING);

        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_add_member);
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_select_btn);\
            super.setTopDetailDesc(View.VISIBLE, " ");
            super.setFabFst(ViewMode.ADMIN);
            super.setFabBackColor(super.fabHelper, R.color.vordiplom_color_orange);
            super.setFabBackImg(super.fabHelper, R.drawable.ic_share);
        } else if (CommonData.getViewMode() == ViewMode.ADMIN || CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_atten_check);
            super.setFabFst(ViewMode.ATTENDANCE);
        }
        if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            setTitle("교적 설정 및 관리");
        }

        super.setTopOkBtnVisibled(View.INVISIBLE);
        setRecyclerView();

    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
    }

    @Override
    public void setSndFabBtn() {
        goMain();
    }

    @Override
    public void setFstFabBtn() {
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            goSelect();
        } else if (CommonData.getViewMode() == ViewMode.ADMIN || CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            CommonData.setViewMode(ViewMode.ATTENDANCE);
            goMemberRecycler();
        }
    }

    private FloatingActionButton fabActionBtn;
    private ArrayList<HolyModel.memberModel> membersArrayList;

    @SuppressLint("SetTextI18n")
    private void setRecyclerView() {
        membersArrayList = null;
        try {
            membersArrayList = getData();
        } catch (Exception e) {

            super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp);

            if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                //tvDesc.setText(CommonString.INFO_TITLE_DONT_SEARCH_DATA);
                setTitle(CommonString.INFO_TITLE_DONT_SEARCH_DATA);
            } else {
                CommonData.setViewMode(ViewMode.ADMIN);
                //tvDesc.setText(CommonString.INFO_TITLE_DONT_LIST_DATA);
                setTitle(CommonString.INFO_TITLE_DONT_LIST_DATA);
                //SuperToastUtil.toastE(this, CommonString.INFO_TITLE_DONT_LIST_DATA);
                String ment = CommonString.GUIDE_FLOATING_BUTTON_ADD + " 또는 선택/관리 화면에서 멤버를 검색하여 " + CommonString.GROUP_NICK + " / " + CommonString.TEAM_NICK + " 수정이 가능합니다.";
                String title = CommonString.INFO_TITLE_DONT_LIST_DATA;
                MemberRecyclerViewActivity.super.setGuideDailogAndOpenFabset(ment, title, MemberRecyclerViewActivity.super.fabFstActBtn);
            }
            return;
        }

        recyclerView = findViewById(R.id.recycler_view_main);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /*if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();*/
                if (dy < 0)
                    //fab.hide();
                    MemberRecyclerViewActivity.super.setAllFabVisibled(true);
                    //fabActionBtn.hide();
                else if (dy > 0)
                    MemberRecyclerViewActivity.super.setAllFabVisibled(false);
                //fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        holderAdapter = new MemberRecyclerViewAdapter(membersArrayList, this, this);
        recyclerView.setAdapter(holderAdapter);
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            if (CommonData.getSelectedYear() != -1 && CommonData.getSelectedDays() != -1) {
                String title = CommonData.getCurrentFullDayAndDaysStr() + " 출석";
                setTitle(title);
                getAttandData();
            } else {
                setDataAndTime();
            }
            super.setTopOkBtnVisibled(View.VISIBLE);
        }

        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_add_member);
            setAdminMode();
        } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_calendar);
            setAttandMode();
        } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            setSearchMode();
        }
    }

    private void setDataAndTime() {
        if (CommonData.getViewMode() != ViewMode.ATTENDANCE) return;
        MaterialDailogUtil.Companion.datePickerDialog(
                MemberRecyclerViewActivity.this,
                s -> MaterialDailogUtil.Companion.showSingleChoice(
                        MemberRecyclerViewActivity.this,
                        R.array.days_option,
                        s1 -> {

                            CommonData.setSelectedDays(Integer.parseInt(s1));

                            LoggerHelper.d(CommonData.getSelectedDays());
                            String title = CommonData.getCurrentFullDayAndDaysStr() + " 출석";

                            setTitle(title);
                            getAttandData();
                        })
        );
    }

    HashMap<String, String> attendMap = new HashMap<>();

    private void getAttandData() {
        AttendModel attendModel = new AttendModel();
        attendModel.timestamp = Common.currentTimestamp();
        attendModel.year = String.valueOf(CommonData.getSelectedYear());
        attendModel.month = String.valueOf(Common.addZero(CommonData.getSelectedMonth()));
        attendModel.date = String.valueOf(Common.addZero(CommonData.getSelectedDay()));
        attendModel.day = String.valueOf((CommonData.getSelectedDayOfWeek()));
        attendModel.time = String.valueOf(CommonData.getSelectedDays());
        attendModel.corpsUID = CommonData.getCorpsUid();
        attendModel.groupUID = CommonData.getGroupUid();
        attendModel.teamUID = CommonData.getTeamUid();
        attendModel.fdate = attendModel.year + "-" + attendModel.month + "-" + attendModel.date + "-" + attendModel.day + "-" + attendModel.time;

        ArrayList<AttendModel> attendModels = new ArrayList<>();
        attendMap = new HashMap<>();


        FDDatabaseHelper.INSTANCE.getAttendDayData(attendModel, stringAttendModelHashMap -> {

            LoggerHelper.d("stringAttendModelHashMap : " + stringAttendModelHashMap);

            if (stringAttendModelHashMap != null) {
                attendMap = stringAttendModelHashMap;
            }
            setCountAttend();
            holderAdapter.notifyDataSetChanged();
            holderAdapter.setAttendMap(attendMap);
            recyclerView.refreshDrawableState();
        });

    }

    private List<String> okExcutiveList;
    private List<String> okList;
    private List<String> noList;
    private List<String> noReasonList;
    private String sendMsg;
    private String sendMsg2;
    ArrayList<HolyModel.memberModel> mCurAttendMembers;

    private void setCountAttend() {

        okAttendCnt = 0;
        noAttendCnt = 0;

        if (mCurAttendMembers != null) {
            mCurAttendMembers.clear();
            mCurAttendMembers = null;
        }
        mCurAttendMembers = new ArrayList<>();

        if (okExcutiveList != null) {
            okExcutiveList.clear();
            okExcutiveList = null;
        }
        okExcutiveList = new ArrayList<>();
        if (okList != null) {
            okList.clear();
            okList = null;
        }

        okList = new ArrayList<>();


        if (noReasonList != null) {
            noReasonList.clear();
            noReasonList = null;
        }

        noReasonList = new ArrayList<>();

        if (noList != null) {
            noList.clear();
            noList = null;
        }
        noList = new ArrayList<>();

        Map<String, HolyModel.memberModel> membersMap = CommonData.getMembersMap(); //CommonData.getHolyModel().memberModel;

        ArrayList<HolyModel.memberModel> members = new ArrayList<>();
        int cnt = 0;
        for (String key : membersMap.keySet()) {
            cnt++;
            HolyModel.memberModel elemMembers = membersMap.get(key);
            elemMembers.uid = key;
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                if (elemMembers.groupUID != null && elemMembers.teamUID != null) {
                    if (elemMembers.groupUID.equals(CommonData.getGroupModel().uid)) {
                        if (elemMembers.teamUID.equals(CommonData.getTeamModel().uid)) {
                            members.add(elemMembers);
                            mCurAttendMembers.add(elemMembers);
                            if (attendMap != null) {
                                if (attendMap.get(elemMembers.name) != null) {
                                    if (attendMap.get(elemMembers.name).equals("true")) {
                                        elemMembers.attend = "true";
                                        mCurAttendMembers.add(elemMembers);
                                        try {
                                            if (elemMembers.isExecutives.equals("임원"))
                                                okExcutiveList.add(elemMembers.name);

                                            else
                                                okList.add(elemMembers.name);
                                        } catch (Exception e) {
                                            okList.add(elemMembers.name);
                                        }
                                    } else {
                                        elemMembers.attend = "false";
                                        mCurAttendMembers.add(elemMembers);
                                        noList.add(elemMembers.name);
                                        if (elemMembers.noAttendReason != null && !Common.trim(elemMembers.noAttendReason).equals(""))
                                            noReasonList.add(elemMembers.name + " : " + elemMembers.noAttendReason);
                                    }
                                } else {
                                    noList.add(elemMembers.name);
                                    //checkAttend(elemMembers, "no");
                                }
                            }
                        }
                    }
                }
            }
        }

        int totalMember = totalAttendCnt = members.size();
        int okCnt = okAttendCnt = okList.size() + okExcutiveList.size();
        int noCnt = noAttendCnt = noList.size();

        LoggerHelper.d("totalMember : " + totalMember);
        LoggerHelper.d("okCnt : " + okCnt);
        LoggerHelper.d("noCnt : " + noCnt);

        double okRate = 0;
        double sum = (okCnt + noCnt);

        if (okCnt != 0 && noCnt != 0) {
            okRate = (double) (okCnt / sum) * 100;
        } else if (okCnt == 0) {
            okRate = 0;
        } else if (noCnt == 0) {
            okRate = 100;
        }

        okRate = Double.parseDouble(new DecimalFormat("##.#").format(okRate));

        String title = "[" + CommonData.getGroupModel().name + " " +
                SortMapUtil.getInteger(CommonData.getTeamModel().name) + " : " +
                CommonData.getTeamModel().etc + "] ";

        String strDate = CommonData.getCurrentFullDayAndDaysStr() + " 출석";

        sendMsg = title + "\n" + strDate + "\n" + "\n" + "총 원 : " + totalMember + "명\n" +
                "출 석 : " + okCnt + "명 / 결 석 : " + noCnt + "명\n" + "\n" +
                "* 출석률 : " + okRate + " % 입니다." + "\n" + "\n" +
                "* 임원출석 명단" + "\n" + okExcutiveList.toString() + "\n" + "\n" +
                "* 성도/회원출석 명단" + "\n" + okList.toString() + "\n" + "\n" +
                "* 결석 명단" + "\n" + noList.toString() + "\n\n"
                + "* 결석 사유 \n" +
                noReasonList.toString();

        sendMsg2 = title + "<br>" + strDate + "<br>" + "<br>" + "<strong>총 원 : " + totalMember + "명<br>" +
                "출 석 : " + okCnt + "명 / 결 석 : " + noCnt + "명<br>" + "<br></strong>" +
                "<strong>* 출석률 : </strong>" + okRate + " % 입니다." + "<br>" + "<br>" +
                "<strong>* 임원출석 명단</strong>" + "<br>" + okExcutiveList.toString() + "<br>" + "<br>" +
                "<strong>* 성도/회원출석 명단</strong>" + "<br>" + okList.toString() + "<br>" + "<br>" +
                "<strong>* 결석 명단</strong>" + "<br>" + noList.toString() + "<br><br>"
                + "* 결석 사유<br>" +
                noReasonList.toString();

        setAttendDesc();
    }

    int maxMember = 0;
    int curCnt = 0;

    public void checkAllAttend(String s) {

        curCnt = 0;
        List<HolyModel.memberModel> membersList = mCurAttendMembers;

        maxMember = membersList.size();
        showProgressDialog(true);

        if (attendBatchList != null) {
            attendBatchList.clear();
            attendBatchList = null;
        }
        attendBatchList = new ArrayList<>();

        for (HolyModel.memberModel eleMember : membersList) {
            checkAttend(eleMember, eleMember.attend);
        }
    }

    private List<AttendModel> attendBatchList;

    private void checkAttend(HolyModel.memberModel members, String attend) {

        AttendModel attendModel = new AttendModel();
        attendModel.timestamp = Common.currentTimestamp();
        attendModel.year = String.valueOf(CommonData.getSelectedYear());
        attendModel.month = String.valueOf(Common.addZero(CommonData.getSelectedMonth()));
        attendModel.date = String.valueOf(Common.addZero(CommonData.getSelectedDay()));
        attendModel.day = String.valueOf(CommonData.getSelectedDayOfWeek());
        attendModel.time = String.valueOf(CommonData.getSelectedDays());
        attendModel.fdate = attendModel.year + "-" + attendModel.month + "-" + attendModel.date + "-" + attendModel.day + "-" + attendModel.time;
        attendModel.attend = attend;
        attendModel.corpsUID = CommonData.getCorpsUid();
        attendModel.groupUID = CommonData.getGroupUid();
        attendModel.teamUID = CommonData.getTeamUid();
        attendModel.memberUID = members.uid;
        attendModel.memberName = members.name;
        attendModel.memberPosition = members.position;
        attendModel.noAttendReason = members.noAttendReason;
        attendModel.isExecutives = members.isExecutives;
        AttendMemberManager attendMemberManager = new AttendMemberManager();
        attendMap.put(attendModel.memberName, attendModel.attend);
        attendBatchList.add(attendModel);
        curCnt++;
        if (curCnt == maxMember) {
            //setCountAttend();

            FireStoreAttendManager.multiInsert(attendBatchList, new DataTypeListener.OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Boolean aBoolean) {
                    showProgressDialog(false);
                    popToast("서버 전송이 완료되었습니다.");
                }
            });
        }
    }

    private void setAttendDesc() {
        //MemberRecyclerViewActivity.super.setTopDetailDesc( View.VISIBLE ,  "현재 출석 "+ okAttendCnt + " 명 , 결석 "+ noAttendCnt + " 명");
        if (okAttendCnt == 0 && noAttendCnt == 0)
            super.setTopDetailDesc(View.VISIBLE, "아직 출석 체크하지 않았습니다.");
        super.setTopDetailDesc(View.VISIBLE, "총원 : " + totalAttendCnt + "명 현재 [ 출석 ] " + okAttendCnt + " 명 , [ 결석 ] " + noAttendCnt + " 명   [ + 더보기 ]");
    }

    private void refresh() {
        LoggerHelper.d("refresh");
        FDDatabaseHelper.INSTANCE.getAllcorpsMembers(new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                try {
                    membersArrayList = getData();
                    LoggerHelper.d("refresh remembers size : " + membersArrayList.size());
                    holderAdapter.setItemArrayList(membersArrayList);
                    recyclerView.refreshDrawableState();
                    holderAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(MemberRecyclerViewActivity.this, CommonString.INFO_TITLE_DONT_LIST_DATA, Toast.LENGTH_SHORT).show();
                    //goSelect();
                }
            }
        });
    }

    public ArrayList getData() throws Exception {
        //LoggerHelper.d("MemberRecyclerViewActivity", "start CommonData.getViewMode() :" + CommonData.getViewMode());
        ArrayList<HolyModel.memberModel> members = new ArrayList<>();

        Map<String, HolyModel.memberModel> membersMap = CommonData.getMembersMap(); //CommonData.getHolyModel().memberModel;
        LoggerHelper.e("holyModel membersie", String.valueOf(membersMap.size()));

        int cnt = 0;

        LoggerHelper.e("getGroupModel().uid : " + CommonData.getGroupModel().uid);
        LoggerHelper.e("getGroupModel().name : " + CommonData.getGroupModel().name);
        LoggerHelper.e("getTeamModel().uid : " + CommonData.getTeamModel().uid);
        LoggerHelper.e("getTeamModel().name : " + CommonData.getTeamModel().name);

        for (String key : membersMap.keySet()) {
            cnt++;
            HolyModel.memberModel elemMembers = membersMap.get(key);
            elemMembers.uid = key;

            LoggerHelper.e("elemMembers.groupUID : " + elemMembers.groupUID);
            LoggerHelper.e("elemMembers.teamUID : " + elemMembers.teamUID);

            if ((elemMembers.groupUID != null && elemMembers.teamUID != null) || CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                if (CommonData.getViewMode() == ViewMode.ADMIN) {
                    LoggerHelper.d("MemberRecyclerViewActivity", "ViewMode.ADMIN 데이터를 수집합니다.");

                    LoggerHelper.e("CommonData.getGroupModel().uid : " + CommonData.getGroupModel().uid );
                    LoggerHelper.e("CommonData.getTeamModel().uid : " + CommonData.getTeamModel().uid );

                    if (elemMembers.groupUID.equals(CommonData.getGroupModel().uid)) {
                        if (elemMembers.teamUID.equals(CommonData.getTeamModel().uid)) {
                            members.add(elemMembers);

                            LoggerHelper.e("memberModel ADMIN : " + members.size());
                        }
                    }
                } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                    LoggerHelper.d("MemberRecyclerViewActivity", "ViewMode.ATTENDANCE 데이터를 수집합니다. : ");
                    if (elemMembers.groupUID.equals(CommonData.getGroupModel().uid)) {
                        if (elemMembers.teamUID.equals(CommonData.getTeamModel().uid)) {
                            LoggerHelper.d("elemMembers : " + elemMembers.name);
                            members.add(elemMembers);
                        }
                    }
                } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                    //LoggerHelper.d("CommonData.getStrSearch() : " + CommonData.getStrSearch());
                    if (CommonData.getStrSearch().equals("")) {
                        members.add(elemMembers);
                    } else {
                        LoggerHelper.d("MemberRecyclerViewActivity", "ViewMode.SEARCH_MEMBER 데이터를 수집합니다.");
                        //if (elemMembers.groupUID.equals(CommonData.getGroupModel().uid)) {
                        //     if (elemMembers.teamUID.equals(CommonData.getTeamModel().uid)) {

                        int compareMemberName = elemMembers.name.indexOf(CommonData.getStrSearch());
                        if (compareMemberName != -1) {
                            //LoggerHelper.d("MemberRecyclerViewActivity", "멤버를 추가합니다. => " + elemMembers.name);
                            members.add(elemMembers);
                        }
                        //  }
                        //}
                    }
                }

                LoggerHelper.e("memberModel : " + members.size());
            } else {
                LoggerHelper.e("MemberRecyclerViewActivity", "선택된 수집 방법이 없습니다.");
            }
        }

        members = (ArrayList<HolyModel.memberModel>) SortMapUtil.getSortMemberList(members);
/*
        if (memberModel.size() < 1) {
            LoggerHelper.e("member getdata error");
            throw new Exception();
        }*/
        return members;
    }

    private void setSearchMode() {
        //super.setTopOkBtnVisibled(View.VISIBLE);
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp);
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_search);
        //setViewMode(ViewMode.ADMIN, " 검색 결과");
    }

    private void setAttandMode() {
        AdmobUtils.setInterstitialAds(MemberRecyclerViewActivity.this, () -> {
        });
        AdmobUtils.loadRewardInterstitialAd();
        //super.setTopOkBtnBackground(R.drawable.ic_atten_check);
        AppUtil.setBackColor(MemberRecyclerViewActivity.this, (Button) findViewById(R.id.top_bar_btn_ok), R.color.colorAccent);

        super.setTopOkBtnVisibled(View.VISIBLE);
        if (CommonData.getGroupModel() != null)
            setViewMode(ViewMode.ATTENDANCE, CommonData.getCurrentFullDayAndDaysStr() + " 출석");
    }

    private void setAdminMode() {
        super.setTopOkBtnVisibled(View.INVISIBLE);
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp);
        if (CommonData.getGroupModel() != null)
            setViewMode(ViewMode.ADMIN, "[ " + CommonData.getGroupModel().name + " ] " + CommonString.MEMBER_NICK + " 선택 / 관리");
    }

    private void setViewMode(ViewMode mode, String title) {
        CommonData.setViewMode(mode);
        setTitle(title);
        holderAdapter.notifyDataSetChanged();
    }

    @Override
    public void setHelperButton() {
        String strHelper = "";
        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            strHelper = CommonString.GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ADMIN;
        } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            strHelper = CommonString.GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ATTENDANCE;
        } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            strHelper = CommonString.GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ADMIN;
        }

        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            AppUtil.sendSharedData(getApplicationContext(), sendMsg);
        } else {
            MaterialDailogUtil.Companion.noticeDialog(
                    MemberRecyclerViewActivity.this,
                    strHelper,
                    CommonString.INFO_HELPER_TITLE,
                    s -> {
                        CommonData.setIsFstEnter(false);
                        LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.getIsFstEnter());
                    });
        }
    }

    @Override
    public void setActionButton() {
        LoggerHelper.d("recycler_view_main_fab !!!!");
        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            CommonData.setAdminMode(AdminMode.NORMAL);
            goSetAddMember();
            CommonData.setHistoryClass((Class) MemberRecyclerViewActivity.class);
        } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            setDataAndTime();
        } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            MemberManager.searchMember(this, () -> goMemberRecycler());
        }
    }

    private void setTitle(String str) {
        //vDesc.setText(str);
        super.setTopTitleDesc(str);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.top_bar_btn_back) {             //나가기 버튼
            if (CommonData.getViewMode() == ViewMode.ADMIN) {
                goSelect();
            } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                //showProgressDialog(true);

                if (isAttendModifyed) {
                    MaterialDailogUtil.Companion.simpleYesNoDialog(MemberRecyclerViewActivity.this, "출석이 변경되었습니다. 저장하지 않고 나가시겠습니까?", new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {
                            goMain();
                        }
                    });
                } else {
                    goMain();
                }
            } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                goSelect();
            }
        } else if (v.getId() == R.id.top_bar_tv_desc) {            //상단 타이틀
            setDataAndTime();
        } else if (v.getId() == R.id.top_bar_tv_detail_desc) {            //수정하기 버튼

            MaterialDailogUtil.Companion.CustomDailogManager(
                    this,
                    sendMsg2,
                    "통계 보기",
                    s -> {

                        if (s.equals("share")) {
                            AppUtil.sendSharedData(getApplicationContext(), sendMsg);
                        }
                    });
        } else if (v.getId() == R.id.top_bar_btn_ok) {            //수정하기 버튼

            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                MaterialDailogUtil.Companion.noticeDialog(
                        this,
                        sendMsg2,
                        "서버에 저장 하시겠습니까?",
                        true,
                        s -> {
                            isAttendModifyed = false;
                            checkAllAttend(s);
                        });
            }
        }
    }


    @Override
    public void onComplete(HolyModel.memberModel members, String value, View v) {

        LoggerHelper.d(v.getId());

        if (v.getId() == R.id.recycler_view_item_rl_main) {                     //아이템 버튼

            CommonData.setAdminMode(AdminMode.MODIFY);
            CommonData.setSelectedMember(members);
            goSetAddMember();
            CommonData.setHistoryClass((Class) MemberRecyclerViewActivity.class);
        } else if (v.getId() == R.id.recycler_view_item_btn_delete) {           //삭제버튼
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                //checkAttend(memberModel, value);
                setCountAttend();
                findViewById(R.id.top_bar_btn_ok).setVisibility(View.VISIBLE);
                isAttendModifyed = true;
            } else {
                MemberManager memberManager = new MemberManager();
                memberManager.delete(members, data -> refresh());
            }
        } else if (v.getId() == R.id.button1) {           //삭제버튼
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                //checkAttend(memberModel, value);
                setCountAttend();
                findViewById(R.id.top_bar_btn_ok).setVisibility(View.VISIBLE);
                isAttendModifyed = true;
            }
        } else if (v.getId() == R.id.recycler_view_item_btn_select) {           //선택버튼
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                LoggerHelper.d("선택버튼 버튼이 클릭되었습니다.!!!!");
                LoggerHelper.d("memberModel : " + members.noAttendReason);
                setCountAttend();
                isAttendModifyed = true;
                //checkAttend(memberModel, value);
            }
        }
    }
}