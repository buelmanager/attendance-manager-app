package com.buel.holyhelper.view.recyclerView.memberRecyclerListView

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelper.R
import com.buel.holyhelper.data.*
import com.buel.holyhelper.management.AttendMemberManager
import com.buel.holyhelper.management.MemberManager
import com.buel.holyhelper.management.firestore.FireStoreAttendManager
import com.buel.holyhelper.model.AttendModel
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.utils.AdmobUtils
import com.buel.holyhelper.utils.AppUtil
import com.buel.holyhelper.utils.SortMapUtil
import com.buel.holyhelper.view.DataTypeListener
import com.buel.holyhelper.view.SimpleListener
import com.buel.holyhelper.view.activity.BaseActivity
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.orhanobut.logger.LoggerHelper
import java.text.DecimalFormat
import java.util.*


class MemberRecyclerViewActivity : BaseActivity(), MemberRecyclerViewListener.OnCompleteListener, View.OnClickListener {
    lateinit internal var recyclerView: RecyclerView
    lateinit internal var holderAdapter: MemberRecyclerViewAdapter
    private var isIntoMode: ViewMode? = null
    private val isModifyMode = false

    private var totalAttendCnt = 0
    private var noAttendCnt = 0
    private var okAttendCnt = 0
    private var isAttendModifyed = false

    private var fabActionBtn: FloatingActionButton? = null
    private var membersArrayList: ArrayList<HolyModel.memberModel>? = null

    internal var attendMap: HashMap<String, String>? = HashMap()

    private var okExcutiveList: MutableList<String>? = null
    private var okList: MutableList<String>? = null
    private var noList: MutableList<String>? = null
    private var noReasonList: MutableList<String>? = null
    private var sendMsg: String? = null
    private var sendMsg2: String? = null
    internal var mCurAttendMembers: ArrayList<HolyModel.memberModel>? = null

    internal var maxMember = 0
    internal var curCnt = 0

    private var attendBatchList: MutableList<AttendModel>? = null

    //LoggerHelper.d("MemberRecyclerViewActivity", "start CommonData.getViewMode() :" + CommonData.getViewMode());
    //CommonData.getHolyModel().memberModel;
    //LoggerHelper.d("CommonData.getStrSearch() : " + CommonData.getStrSearch());
    //if (elemMembers.groupUID.equals(CommonData.getGroupModel().uid)) {
    //     if (elemMembers.teamUID.equals(CommonData.getTeamModel().uid)) {
    //LoggerHelper.d("MemberRecyclerViewActivity", "멤버를 추가합니다. => " + elemMembers.name);
    //  }
    //}
    /*
        if (memberModel.size() < 1) {
            LoggerHelper.e("member getdata error");
            throw new Exception();
        }*/ val data: ArrayList<*>
        @Throws(Exception::class)
        get() {
            var members = ArrayList<HolyModel.memberModel>()

            val membersMap = CommonData.getMembersMap()
            LoggerHelper.e("holyModel membersie", membersMap.size.toString())

            var cnt = 0

            LoggerHelper.e("getGroupModel().uid : " + CommonData.getGroupModel().uid)
            LoggerHelper.e("getGroupModel().name : " + CommonData.getGroupModel().name)
            LoggerHelper.e("getTeamModel().uid : " + CommonData.getTeamModel().uid)
            LoggerHelper.e("getTeamModel().name : " + CommonData.getTeamModel().name)

            for (key in membersMap.keys) {
                cnt++
                val elemMembers = membersMap[key]
                elemMembers!!.uid = key

                LoggerHelper.e("elemMembers.groupUID : " + elemMembers.groupUID)
                LoggerHelper.e("elemMembers.teamUID : " + elemMembers.teamUID)

                if (elemMembers.groupUID != null && elemMembers.teamUID != null || CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                    if (CommonData.getViewMode() == ViewMode.ADMIN) {
                        LoggerHelper.d("MemberRecyclerViewActivity", "ViewMode.ADMIN 데이터를 수집합니다.")

                        LoggerHelper.e("CommonData.getGroupModel().uid : " + CommonData.getGroupModel().uid)
                        LoggerHelper.e("CommonData.getTeamModel().uid : " + CommonData.getTeamModel().uid)

                        if (elemMembers.groupUID == CommonData.getGroupModel().uid) {
                            if (elemMembers.teamUID == CommonData.getTeamModel().uid) {
                                members.add(elemMembers)

                                LoggerHelper.e("memberModel ADMIN : " + members.size)
                            }
                        }
                    } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                        LoggerHelper.d("MemberRecyclerViewActivity", "ViewMode.ATTENDANCE 데이터를 수집합니다. : ")
                        if (elemMembers.groupUID == CommonData.getGroupModel().uid) {
                            if (elemMembers.teamUID == CommonData.getTeamModel().uid) {
                                LoggerHelper.d("elemMembers : " + elemMembers.name)
                                members.add(elemMembers)
                            }
                        }
                    } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                        if (CommonData.getStrSearch() == "") {
                            members.add(elemMembers)
                        } else {
                            LoggerHelper.d("MemberRecyclerViewActivity", "ViewMode.SEARCH_MEMBER 데이터를 수집합니다.")

                            val compareMemberName = elemMembers.name.indexOf(CommonData.getStrSearch())
                            if (compareMemberName != -1) {
                                members.add(elemMembers)
                            }
                        }
                    }

                    LoggerHelper.e("memberModel : " + members.size)
                } else {
                    LoggerHelper.e("MemberRecyclerViewActivity", "선택된 수집 방법이 없습니다.")
                }
            }

            members = SortMapUtil.getSortMemberList(members) as ArrayList<HolyModel.memberModel>
            return members
        }

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        //LoggerHelper.d("onCreate");
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fd_cell_recycler_view)

        findViewById<View>(R.id.recycler_view_main_iv_close).setOnClickListener(this)
        setTopLayout(this)
        super.setBaseFloatingActionButton()

        if (CommonData.getHolyModel() == null) {
            Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_CORP, Toast.LENGTH_SHORT).show()
            goSelect()
            return
        }

        try {
            setTitle(CommonData.getGroupModel().name + " / " + CommonData.getTeamModel().name)
            //tvDesc.setText(CommonData.getGroupModel().name + " / " + CommonData.getTeamModel().name);
        } catch (e: Exception) {
            if (CommonData.getViewMode() != ViewMode.SEARCH_MEMBER) {
                Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP, Toast.LENGTH_SHORT).show()
                goSelect()
            }
        }

        isIntoMode = CommonData.getViewMode()
        fabActionBtn = super.fabFstActBtn
        //super.setFabBackImg(super.fab2ndBtn, R.drawable.ic_anal);
        super.setFabSnd(ViewMode.BRIEFING)

        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_add_member)
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_select_btn);\
            super.setTopDetailDesc(View.VISIBLE, " ")
            super.setFabFst(ViewMode.ADMIN)
            super.setFabBackColor(super.fabHelper, R.color.vordiplom_color_orange)
            super.setFabBackImg(super.fabHelper, R.drawable.ic_share)
        } else if (CommonData.getViewMode() == ViewMode.ADMIN || CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_atten_check);
            super.setFabFst(ViewMode.ATTENDANCE)
        }
        if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            setTitle("교적 설정 및 관리")
        }

        super.setTopOkBtnVisibled(View.INVISIBLE)
        setRecyclerView()

    }

    override fun onResume() {
        refresh()
        super.onResume()
    }

    override fun setSndFabBtn() {
        goMain()
    }

    override fun setFstFabBtn() {
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            goSelect()
        } else if (CommonData.getViewMode() == ViewMode.ADMIN || CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            CommonData.setViewMode(ViewMode.ATTENDANCE)
            goMemberRecycler()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setRecyclerView() {
        membersArrayList = null
        try {
            membersArrayList = this.data as ArrayList<HolyModel.memberModel>
        } catch (e: Exception) {

            super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp)

            if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                //tvDesc.setText(CommonString.INFO_TITLE_DONT_SEARCH_DATA);
                setTitle(CommonString.INFO_TITLE_DONT_SEARCH_DATA)
            } else {
                CommonData.setViewMode(ViewMode.ADMIN)
                //tvDesc.setText(CommonString.INFO_TITLE_DONT_LIST_DATA);
                setTitle(CommonString.INFO_TITLE_DONT_LIST_DATA)
                //SuperToastUtil.toastE(this, CommonString.INFO_TITLE_DONT_LIST_DATA);
                val ment = CommonString.GUIDE_FLOATING_BUTTON_ADD + " 또는 선택/관리 화면에서 멤버를 검색하여 " + CommonString.GROUP_NICK + " / " + CommonString.TEAM_NICK + " 수정이 가능합니다."
                val title = CommonString.INFO_TITLE_DONT_LIST_DATA
                super@MemberRecyclerViewActivity.setGuideDailogAndOpenFabset(ment, title, super@MemberRecyclerViewActivity.fabFstActBtn)
            }
            return
        }

        recyclerView = findViewById(R.id.recycler_view_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                /*if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();*/
                if (dy < 0)
                //fab.hide();
                    super@MemberRecyclerViewActivity.setAllFabVisibled(true)
                else if (dy > 0)
                    super@MemberRecyclerViewActivity.setAllFabVisibled(false)//fabActionBtn.hide();
                //fab.hide();
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        holderAdapter = MemberRecyclerViewAdapter(membersArrayList, this, this)
        recyclerView.adapter = holderAdapter
        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            if (CommonData.getSelectedYear() != -1 && CommonData.getSelectedDays() != -1) {
                val title = CommonData.getCurrentFullDayAndDaysStr() + " 출석"
                setTitle(title)
                getAttandData()
            } else {
                setDataAndTime()
            }
            super.setTopOkBtnVisibled(View.VISIBLE)
        }

        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_add_member)
            setAdminMode()
        } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_calendar)
            setAttandMode()
        } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            setSearchMode()
        }
    }

    private fun setDataAndTime() {
        if (CommonData.getViewMode() != ViewMode.ATTENDANCE) return
        MaterialDailogUtil.datePickerDialog(
                this@MemberRecyclerViewActivity,
                object : MaterialDailogUtil.OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        MaterialDailogUtil.showSingleChoice(
                                this@MemberRecyclerViewActivity,
                                R.array.days_option,
                                object : MaterialDailogUtil.OnDialogSelectListner {
                                    override fun onSelect(s1: String) {
                                        CommonData.setSelectedDays(Integer.parseInt(s1))
                                        LoggerHelper.d(CommonData.getSelectedDays())
                                        val title = CommonData.getCurrentFullDayAndDaysStr() + " 출석"

                                        setTitle(title)
                                        getAttandData()
                                    }
                                })
                    }
                }
        )
    }

    private fun getAttandData() {
        val attendModel = AttendModel()
        attendModel.timestamp = Common.currentTimestamp()
        attendModel.year = CommonData.getSelectedYear().toString()
        attendModel.month = Common.addZero(CommonData.getSelectedMonth()).toString()
        attendModel.date = Common.addZero(CommonData.getSelectedDay()).toString()
        attendModel.day = CommonData.getSelectedDayOfWeek().toString()
        attendModel.time = CommonData.getSelectedDays().toString()
        attendModel.corpsUID = CommonData.getCorpsUid()
        attendModel.groupUID = CommonData.getGroupUid()
        attendModel.teamUID = CommonData.getTeamUid()
        attendModel.fdate = attendModel.year + "-" + attendModel.month + "-" + attendModel.date + "-" + attendModel.day + "-" + attendModel.time

        val attendModels = ArrayList<AttendModel>()
        attendMap = HashMap()


        FDDatabaseHelper.getAttendDayData(attendModel,
                DataTypeListener.OnCompleteListener<HashMap<String, String>> { stringAttendModelHashMap: HashMap<String, String>? ->
                    LoggerHelper.d("stringAttendModelHashMap : " + stringAttendModelHashMap!!)

                    if (stringAttendModelHashMap != null) {
                        attendMap = stringAttendModelHashMap
                    }
                    setCountAttend()
                    holderAdapter.notifyDataSetChanged()
                    holderAdapter.setAttendMap(attendMap)
                    recyclerView.refreshDrawableState()
                })
    }

    private fun setCountAttend() {

        okAttendCnt = 0
        noAttendCnt = 0

        if (mCurAttendMembers != null) {
            mCurAttendMembers!!.clear()
            mCurAttendMembers = null
        }
        mCurAttendMembers = ArrayList<HolyModel.memberModel>()

        if (okExcutiveList != null) {
            okExcutiveList!!.clear()
            okExcutiveList = null
        }
        okExcutiveList = ArrayList()
        if (okList != null) {
            okList!!.clear()
            okList = null
        }

        okList = ArrayList()


        if (noReasonList != null) {
            noReasonList!!.clear()
            noReasonList = null
        }

        noReasonList = ArrayList()

        if (noList != null) {
            noList!!.clear()
            noList = null
        }
        noList = ArrayList()

        val membersMap = CommonData.getMembersMap() //CommonData.getHolyModel().memberModel;

        val members = ArrayList<HolyModel.memberModel>()
        var cnt = 0
        for (key in membersMap.keys) {
            cnt++
            val elemMembers = membersMap[key]
            elemMembers!!.uid = key
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                if (elemMembers.groupUID != null && elemMembers.teamUID != null) {
                    if (elemMembers.groupUID == CommonData.getGroupModel().uid) {
                        if (elemMembers.teamUID == CommonData.getTeamModel().uid) {
                            members.add(elemMembers)
                            mCurAttendMembers!!.add(elemMembers)
                            if (attendMap != null) {
                                if (attendMap!![elemMembers.name] != null) {
                                    if (attendMap!![elemMembers.name] == "true") {
                                        elemMembers.attend = "true"
                                        mCurAttendMembers!!.add(elemMembers)
                                        try {
                                            if (elemMembers.isExecutives == "임원")
                                                okExcutiveList!!.add(elemMembers.name)
                                            else
                                                okList!!.add(elemMembers.name)
                                        } catch (e: Exception) {
                                            okList!!.add(elemMembers.name)
                                        }

                                    } else {
                                        elemMembers.attend = "false"
                                        mCurAttendMembers!!.add(elemMembers)
                                        noList!!.add(elemMembers.name)
                                        if (elemMembers.noAttendReason != null && Common.trim(elemMembers.noAttendReason) != "")
                                            noReasonList!!.add(elemMembers.name + " : " + elemMembers.noAttendReason)
                                    }
                                } else {
                                    noList!!.add(elemMembers.name)
                                    //checkAttend(elemMembers, "no");
                                }
                            }
                        }
                    }
                }
            }
        }

        totalAttendCnt = members.size
        val totalMember = totalAttendCnt
        okAttendCnt = okList!!.size + okExcutiveList!!.size
        val okCnt = okAttendCnt
        noAttendCnt = noList!!.size
        val noCnt = noAttendCnt

        LoggerHelper.d("totalMember : $totalMember")
        LoggerHelper.d("okCnt : $okCnt")
        LoggerHelper.d("noCnt : $noCnt")

        var okRate = 0.0
        val sum = (okCnt + noCnt).toDouble()

        if (okCnt != 0 && noCnt != 0) {
            okRate = okCnt / sum * 100
        } else if (okCnt == 0) {
            okRate = 0.0
        } else if (noCnt == 0) {
            okRate = 100.0
        }

        okRate = java.lang.Double.parseDouble(DecimalFormat("##.#").format(okRate))

        val title = "[" + CommonData.getGroupModel().name + " " +
                SortMapUtil.getInteger(CommonData.getTeamModel().name) + " : " +
                CommonData.getTeamModel().etc + "] "

        val strDate = CommonData.getCurrentFullDayAndDaysStr() + " 출석"

        sendMsg = (title + "\n" + strDate + "\n" + "\n" + "총 원 : " + totalMember + "명\n" +
                "출 석 : " + okCnt + "명 / 결 석 : " + noCnt + "명\n" + "\n" +
                "* 출석률 : " + okRate + " % 입니다." + "\n" + "\n" +
                "* 임원출석 명단" + "\n" + okExcutiveList!!.toString() + "\n" + "\n" +
                "* 성도/회원출석 명단" + "\n" + okList!!.toString() + "\n" + "\n" +
                "* 결석 명단" + "\n" + noList!!.toString() + "\n\n"
                + "* 결석 사유 \n" +
                noReasonList!!.toString())

        sendMsg2 = (title + "<br>" + strDate + "<br>" + "<br>" + "<strong>총 원 : " + totalMember + "명<br>" +
                "출 석 : " + okCnt + "명 / 결 석 : " + noCnt + "명<br>" + "<br></strong>" +
                "<strong>* 출석률 : </strong>" + okRate + " % 입니다." + "<br>" + "<br>" +
                "<strong>* 임원출석 명단</strong>" + "<br>" + okExcutiveList!!.toString() + "<br>" + "<br>" +
                "<strong>* 성도/회원출석 명단</strong>" + "<br>" + okList!!.toString() + "<br>" + "<br>" +
                "<strong>* 결석 명단</strong>" + "<br>" + noList!!.toString() + "<br><br>"
                + "* 결석 사유<br>" +
                noReasonList!!.toString())

        setAttendDesc()
    }

    fun checkAllAttend(s: String) {

        curCnt = 0
        val membersList = mCurAttendMembers

        maxMember = membersList!!.size
        showProgressDialog(true)

        if (attendBatchList != null) {
            attendBatchList!!.clear()
            attendBatchList = null
        }
        attendBatchList = ArrayList()

        for (eleMember in membersList) {
            checkAttend(eleMember, eleMember.attend)
        }
    }

    private fun checkAttend(members: HolyModel.memberModel, attend: String) {

        val attendModel = AttendModel()
        attendModel.timestamp = Common.currentTimestamp()
        attendModel.year = CommonData.getSelectedYear().toString()
        attendModel.month = Common.addZero(CommonData.getSelectedMonth()).toString()
        attendModel.date = Common.addZero(CommonData.getSelectedDay()).toString()
        attendModel.day = CommonData.getSelectedDayOfWeek().toString()
        attendModel.time = CommonData.getSelectedDays().toString()
        attendModel.fdate = attendModel.year + "-" + attendModel.month + "-" + attendModel.date + "-" + attendModel.day + "-" + attendModel.time
        attendModel.attend = attend
        attendModel.corpsUID = CommonData.getCorpsUid()
        attendModel.groupUID = CommonData.getGroupUid()
        attendModel.teamUID = CommonData.getTeamUid()
        attendModel.memberUID = members.uid
        attendModel.memberName = members.name
        attendModel.memberPosition = members.position
        attendModel.noAttendReason = members.noAttendReason
        attendModel.isExecutives = members.isExecutives
        val attendMemberManager = AttendMemberManager()
        attendMap!![attendModel.memberName] = attendModel.attend
        attendBatchList!!.add(attendModel)
        curCnt++
        if (curCnt == maxMember) {
            //setCountAttend();

            FireStoreAttendManager.multiInsert(attendBatchList!!, DataTypeListener.OnCompleteListener {
                showProgressDialog(false)
                popToast("서버 전송이 완료되었습니다.")
            })
        }
    }

    private fun setAttendDesc() {
        //MemberRecyclerViewActivity.super.setTopDetailDesc( View.VISIBLE ,  "현재 출석 "+ okAttendCnt + " 명 , 결석 "+ noAttendCnt + " 명");
        if (okAttendCnt == 0 && noAttendCnt == 0)
            super.setTopDetailDesc(View.VISIBLE, "아직 출석 체크하지 않았습니다.")
        super.setTopDetailDesc(View.VISIBLE, "총원 : " + totalAttendCnt + "명 현재 [ 출석 ] " + okAttendCnt + " 명 , [ 결석 ] " + noAttendCnt + " 명   [ + 더보기 ]")
    }

    private fun refresh() {
        LoggerHelper.d("refresh")
        FDDatabaseHelper.getAllcorpsMembers(SimpleListener.OnCompleteListener {
            try {
                membersArrayList = this.data as ArrayList<HolyModel.memberModel>
                LoggerHelper.d("refresh remembers size : " + membersArrayList!!.size)
                holderAdapter.setItemArrayList(membersArrayList)
                recyclerView.refreshDrawableState()
                holderAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@MemberRecyclerViewActivity, CommonString.INFO_TITLE_DONT_LIST_DATA, Toast.LENGTH_SHORT).show()
                //goSelect();
            }
        })
    }

    private fun setSearchMode() {
        //super.setTopOkBtnVisibled(View.VISIBLE);
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp)
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_search)
        //setViewMode(ViewMode.ADMIN, " 검색 결과");
    }

    private fun setAttandMode() {
        AdmobUtils.setInterstitialAds(this@MemberRecyclerViewActivity) { }
        AdmobUtils.loadRewardInterstitialAd()
        //super.setTopOkBtnBackground(R.drawable.ic_atten_check);
        AppUtil.setBackColor(this@MemberRecyclerViewActivity, findViewById<View>(R.id.top_bar_btn_ok) as Button, R.color.colorAccent)

        super.setTopOkBtnVisibled(View.VISIBLE)
        if (CommonData.getGroupModel() != null)
            setViewMode(ViewMode.ATTENDANCE, CommonData.getCurrentFullDayAndDaysStr() + " 출석")
    }

    private fun setAdminMode() {
        super.setTopOkBtnVisibled(View.INVISIBLE)
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp)
        if (CommonData.getGroupModel() != null)
            setViewMode(ViewMode.ADMIN, "[ " + CommonData.getGroupModel().name + " ] " + CommonString.MEMBER_NICK + " 선택 / 관리")
    }

    private fun setViewMode(mode: ViewMode, title: String) {
        CommonData.setViewMode(mode)
        setTitle(title)
        holderAdapter.notifyDataSetChanged()
    }

    override fun setHelperButton() {
        var strHelper = ""
        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            strHelper = CommonString.GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ADMIN
        } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            strHelper = CommonString.GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ATTENDANCE
        } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            strHelper = CommonString.GUIDE_HELPER_MEMBER_RECUCLER_VIEW_ADMIN
        }

        if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            AppUtil.sendSharedData(applicationContext, sendMsg)
        } else {
            MaterialDailogUtil.noticeDialog(
                    this@MemberRecyclerViewActivity,
                    strHelper,
                    CommonString.INFO_HELPER_TITLE,
                    object : MaterialDailogUtil.OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            CommonData.setIsFstEnter(false)
                            LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.getIsFstEnter())
                        }
                    })
        }
    }

    override fun setActionButton() {
        LoggerHelper.d("recycler_view_main_fab !!!!")
        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            CommonData.setAdminMode(AdminMode.NORMAL)
            goSetAddMember()
            //CommonData.setHistoryClass(MemberRecyclerViewActivity::class.java as Class<*>)
        } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
            setDataAndTime()
        } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
            MemberManager.searchMember(this) { goMemberRecycler() }
        }
    }

    private fun setTitle(str: String) {
        //vDesc.setText(str);
        super.setTopTitleDesc(str)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.top_bar_btn_back) {             //나가기 버튼
            if (CommonData.getViewMode() == ViewMode.ADMIN) {
                goSelect()
            } else if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                //showProgressDialog(true);

                if (isAttendModifyed) {
                    MaterialDailogUtil.simpleYesNoDialog(this@MemberRecyclerViewActivity, "출석이 변경되었습니다. 저장하지 않고 나가시겠습니까?", object : MaterialDailogUtil.OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            goMain()
                        }
                    })
                } else {
                    goMain()
                }
            } else if (CommonData.getViewMode() == ViewMode.SEARCH_MEMBER) {
                goSelect()
            }
        } else if (v.id == R.id.top_bar_tv_desc) {            //상단 타이틀
            setDataAndTime()
        } else if (v.id == R.id.top_bar_tv_detail_desc) {            //수정하기 버튼

            MaterialDailogUtil.CustomDailogManager(
                    this,
                    sendMsg2!!,
                    "통계 보기",
                    object : MaterialDailogUtil.OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            if (s == "share") {
                                AppUtil.sendSharedData(applicationContext, sendMsg)
                            }
                        }
                    })

        } else if (v.id == R.id.top_bar_btn_ok) {            //수정하기 버튼

            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                MaterialDailogUtil.noticeDialog(
                        this,
                        sendMsg2!!,
                        "서버에 저장 하시겠습니까?",
                        true,
                        object : MaterialDailogUtil.OnDialogSelectListner {
                            override fun onSelect(s: String) {

                                isAttendModifyed = false
                                checkAllAttend(s)

                            }
                        })
            }
        }
    }


    override fun onComplete(members: HolyModel.memberModel, value: String, v: View) {

        LoggerHelper.d(v.id)

        if (v.id == R.id.recycler_view_item_rl_main) {                     //아이템 버튼

            CommonData.setAdminMode(AdminMode.MODIFY)
            CommonData.setSelectedMember(members)
            goSetAddMember()
            //CommonData.setHistoryClass(MemberRecyclerViewActivity::class.java as Class<*>)
        } else if (v.id == R.id.recycler_view_item_btn_delete) {           //삭제버튼
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                //checkAttend(memberModel, value);
                setCountAttend()
                findViewById<View>(R.id.top_bar_btn_ok).visibility = View.VISIBLE
                isAttendModifyed = true
            } else {
                val memberManager = MemberManager()
                memberManager.delete(members) { data -> refresh() }
            }
        } else if (v.id == R.id.button1) {           //삭제버튼
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                //checkAttend(memberModel, value);
                setCountAttend()
                findViewById<View>(R.id.top_bar_btn_ok).visibility = View.VISIBLE
                isAttendModifyed = true
            }
        } else if (v.id == R.id.recycler_view_item_btn_select) {           //선택버튼
            if (CommonData.getViewMode() == ViewMode.ATTENDANCE) {
                LoggerHelper.d("선택버튼 버튼이 클릭되었습니다.!!!!")
                LoggerHelper.d("memberModel : " + members.noAttendReason)
                setCountAttend()
                isAttendModifyed = true
                //checkAttend(memberModel, value);
            }
        }
    }

    companion object {
        private val TAG = "MemberRecyclerViewActivity"
    }
}