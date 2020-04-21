package com.buel.holyhelpers.data

import com.buel.holyhelpers.model.*
import com.buel.holyhelpers.model.AnalyticsModel.Attendance
import com.buel.holyhelpers.model.HolyModel.groupModel
import com.buel.holyhelpers.model.HolyModel.groupModel.teamModel
import com.buel.holyhelpers.model.HolyModel.memberModel
import com.buel.holyhelpers.utils.CalendarUtils
import com.buel.holyhelpers.utils.SharedPreferenceUtil
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.orhanobut.logger.LoggerHelper
import org.apache.poi.ss.formula.functions.T
import java.util.*

/**
 * Created by 19001283 on 2018-06-01.
 */
object CommonData {


    lateinit var teamMap: Map<String, teamModel?>
    lateinit var groupMap: Map<String, groupModel?>

    @JvmStatic
    lateinit var membersMap: Map<String?, memberModel?>
    /*       get(){
               TODO()
           }
           set(membersMap) {
               if (membersMap != null) LoggerHelper.d("setMembersMap 를 세팅합니다. $membersMap") else LoggerHelper.d("setMembersMap 를 세팅합니다. :: null ")
               field = membersMap
           }*/


    @JvmStatic
    var currentSelectViewPageNum = 0

    var isFireStoreMode = true

    @JvmStatic
    var currentPremiumType = PremiupType.NORAML
    /*    set(currentPremiumType) {
            LoggerHelper.d("현재 프리미엄 타입은 :  $currentPremiumType")
            field = currentPremiumType
        }*/

    @JvmStatic
    lateinit var tutorialModelList: List<TutorialModel>

    var searchAddress: String? = null

    @JvmStatic
    lateinit var analMode: AnalMode
        private set

    @JvmStatic
    fun setAnalMode(analMode: AnalMode) {
        LoggerHelper.d("setAnalMode 를 세팅합니다. $analMode")
        CommonData.analMode = analMode
    }

    @JvmStatic
    lateinit var historyClass: Class<T>
    private var historyClassList: List<Class<*>> = ArrayList()
    val historyClassListLastIndex: Class<*>
        get() {
            val historyClass = historyClassList[historyClassList.size - 1]
            setHistoryClassListLastRemove()
            return historyClass
        }

    fun getHistoryClassList(): List<Class<*>> {
        return historyClassList
    }

    fun setHistoryClassListLastRemove() {
        //historyClassList.removeAt(historyClassList.size - 1)
    }

    fun setHistoryClassList(historyClassList: List<Class<*>>) {
        CommonData.historyClassList = historyClassList
    }

    @JvmStatic
    var isTutoMode :Boolean = false

    //TABLE NAME
    const val ADMOB_APP_ID = "ca-app-pub-7585440404153175~9114562877"
    //TEST "ca-app-pub-3940256099942544/5224354917";
    //ca-app-pub-7585440404153175/9007585812

    @JvmStatic
    lateinit var appNotice: String

    @JvmStatic
    var isFstEnter = false

    //@@@@@ 최고 관리자의 UID
    @JvmStatic
    var adminUid: String? = ""
    /*  set(adminUid) {
          LoggerHelper.d("setAdminUid", "setAdminUid $adminUid 로 변경되었습니다.")
          field = adminUid
      }*/
    //@@@@@
    @JvmStatic
    var isAdsOpen = "true"
        set(isAdsOpen) {
            LoggerHelper.d("광고모드가 변경됩니다 : $isAdsOpen")
            field = isAdsOpen
        }

    lateinit var daumAddUrl: String

    //회원관리 관련
    @JvmStatic
    var corpsCnt = 0
        set(corpsCnt) {
            LoggerHelper.d("setCorpsCnt 를 세팅합니다. $corpsCnt")
            field = corpsCnt
        }
    @JvmStatic
    lateinit var corpsUid: String
    /*       set(corpsUid) {
               LoggerHelper.d("setCorpsUid 를 세팅합니다. $corpsUid")
               field = corpsUid
           }*/
    private var groupUid: String? = null
    private var teamUid: String? = null
    @JvmStatic
    lateinit var memberUid: String/*? = null
        set(memberUid) {
            LoggerHelper.d("setMemberUid 를 세팅합니다. $memberUid")
            field = memberUid
        }*/
    @JvmStatic
    lateinit var holyModel: HolyModel/*? = null
        set(holyModel) {
            if (holyModel != null) LoggerHelper.d("setHolyModel 를 세팅합니다. $holyModel") else LoggerHelper.d("setHolyModel 를 세팅합니다. :: null ")
            field = holyModel
        }*/
    @JvmStatic
    var groupModel: groupModel = HolyModel.groupModel()
        set(groupModel) {
            if (groupModel != null) LoggerHelper.d("setGroupModel 를 세팅합니다. $groupModel") else LoggerHelper.d("setGroupModel 를 세팅합니다. :: null ")
            if (groupModel != null) {
                field = groupModel
                setGroupUid(groupModel.uid)
                SharedPreferenceUtil.putGroupModel(groupModel)
            } else {
                groupUid = null
                SharedPreferenceUtil.initGroupModel()
            }
        }
    @JvmStatic
    var teamModel: teamModel = HolyModel.groupModel.teamModel()
/*        set(teamModel) {
            if (teamModel != null) LoggerHelper.d("setTeamModel 를 세팅합니다. $teamModel") else LoggerHelper.d("setTeamModel 를 세팅합니다. :: null ")
            if (teamModel != null) {
                field = teamModel
                setTeamUid(teamModel.uid)
                SharedPreferenceUtil.putTeamModel(teamModel)
            } else {

                teamUid = null
                SharedPreferenceUtil.initTeamModel()
            }
        }*/

    @JvmStatic
    fun setCurGroupModel( gModel:HolyModel.groupModel?){
        //if (gModel != null) LoggerHelper.d("setGroupModel 를 세팅합니다. $groupModel") else LoggerHelper.d("setGroupModel 를 세팅합니다. :: null ")
        if (gModel != null) {
            groupModel = gModel
            setGroupUid(gModel.uid)
            SharedPreferenceUtil.putGroupModel(gModel)
        } else {
            groupUid = null
            SharedPreferenceUtil.initGroupModel()
        }
    }

    @JvmStatic
    fun setCurTeamModel( tModel:HolyModel.groupModel.teamModel?){
        //if (tModel != null) LoggerHelper.d("setTeamModel 를 세팅합니다. $tModel") else LoggerHelper.d("setTeamModel 를 세팅합니다. :: null ")
        if (tModel != null) {
            teamModel = tModel
            setTeamUid(tModel.uid)
            SharedPreferenceUtil.putTeamModel(tModel)
        } else {
            teamUid = null
            SharedPreferenceUtil.initTeamModel()
        }
    }
    @JvmStatic
    lateinit var memberModel: memberModel/*? = null
        set(memberModel) {
            if (memberModel != null) LoggerHelper.d("setMemberModel 를 세팅합니다. $memberModel") else LoggerHelper.d("setMemberModel 를 세팅합니다. :: null ")
            field = memberModel
        }*/
    @JvmStatic
    lateinit var userModel: UserModel
    lateinit var holyModelMap: Map<Int, HolyModel>

    @JvmStatic
    lateinit var firebaseUser: FirebaseUser
    lateinit var corpsPassWord: String
    lateinit var googleSignInClient: GoogleSignInClient

    @JvmStatic
    var adsVideoId: String = ""
    var adsBannerId: String = ""
    var adsInterstitialId: String? = null

    lateinit var holyModelList: List<HolyModel>/*? = null
        set(holyModelList) {
            if (holyModelList != null) LoggerHelper.d("setHolyModelList 를 세팅합니다. $holyModelList") else LoggerHelper.d("setHolyModelList 를 세팅합니다. :: null ")
            field = holyModelList
        }*/

    //@@@@
    @JvmStatic
    lateinit var memberShipType: UserType/*? = null
        set(memberShipType) {
            if (memberShipType != null) LoggerHelper.d("setMemberShipType 를 세팅합니다. $memberShipType") else LoggerHelper.d("setMemberShipType 를 세팅합니다. :: null ")
            field = memberShipType
        }*/
    //통계관리 관련
    @JvmStatic
    var selectedYear = -1
        set(selectedYear) {
            LoggerHelper.d("setSelectedYear 를 세팅합니다. $selectedYear")
            field = selectedYear
        }
    //LoggerHelper.d("setSelectedMonth 를 세팅합니다. " + selectedMonth);
    @JvmStatic
    var selectedMonth = -1

    @JvmStatic
    var selectedDays = -1
        set(selectedDays) {
            LoggerHelper.d("setSelectedDays 를 세팅합니다. $selectedDays")
            field = selectedDays
        }
    @JvmStatic
    var selectedDay = -1
        set(selectedDay) {
            LoggerHelper.d("setSelectedDay 를 세팅합니다. $selectedDay")
            field = selectedDay
        }
    var selectedHour = -1
    var selectedMinute = -1
    @JvmStatic
    var selectedDayOfWeek = -1
        set(selectedDayOfWeek) {
            LoggerHelper.d("setSelectedDayOfWeek 를 세팅합니다. $selectedDayOfWeek")
            field = selectedDayOfWeek
        }
    @JvmStatic
    var level1 = -1
    @JvmStatic
    var level2 = -1
    @JvmStatic
    var level3 = -1
    @JvmStatic
    var level4 = -1

    @JvmStatic
    var videoAdsPoint = 0

    @JvmStatic
    var tutorialPoint = 0
    @JvmStatic
    var personalJoinPoint = 0

    @JvmStatic
    var level5 = -1
    //현재 그룹의 출석 모델
    var currentAnalyticsGroupModel: Attendance.group? = null
    //현재 단체의 그룹 map
    var attendGroupMaps: HashMap<String, Attendance.group>? = null
    //현재 단체,그룹의 Date map
    @JvmStatic
    lateinit var attendDateMaps: HashMap<String, DateModel>/*? = null
        set(attendDateMaps) {
            LoggerHelper.d("setAttendDateMaps 를 세팅합니다. $attendDateMaps")
            if (attendDateMaps != null) LoggerHelper.d("setAttendDateMaps 를 세팅합니다. $attendDateMaps") else LoggerHelper.d("setAttendDateMaps 를 세팅합니다. :: null ")
            field = attendDateMaps
        }*/
    //현재 단체, 그룹의 DateModel
    var currentAnalyticsDateModel: DateModel? = null
    //현재 단체, 그룹, 날짜의 멤버 MAP
    var attendMemberMap: HashMap<String, AttendModel>? = null
    //화면 모드 관련
    @JvmStatic
    var viewMode: ViewMode? = null
        private set

    @JvmStatic
    fun setAdminMode(adminMode: AdminMode) {
        LoggerHelper.d("setAdminMode", "AdminMode : $adminMode 으로 변경됩니다.")
        CommonData.adminMode = adminMode
    }

    @JvmStatic
    lateinit var adminMode: AdminMode
        private set

    //@@@
    @JvmStatic
    lateinit var strSearch: String/*
        set(strSearch) {
            LoggerHelper.d("setStrSearch 를 세팅합니다. $strSearch")
            field = strSearch
        }*/

    @JvmStatic
    fun getGroupUid(): String? {
        return groupUid
    }

    @JvmStatic
    fun setGroupUid(groupUid: String?) {
        LoggerHelper.d("setGroupUid 를 세팅합니다. $groupUid")
        CommonData.groupUid = groupUid
    }

    @JvmStatic
    fun getTeamUid(): String? {
        return teamUid
    }

    @JvmStatic
    fun setTeamUid(teamUid: String?) {
        LoggerHelper.d("setTeamUid 를 세팅합니다. $teamUid")
        CommonData.teamUid = teamUid
    }

    val positionList: List<String>
        get() {
            val strings: MutableList<String> = ArrayList()
            strings.add("담임목사")
            strings.add("담임사모")
            strings.add("목사")
            strings.add("전도사")
            strings.add("교육사")
            strings.add("간사")
            strings.add("부장")
            strings.add("지역장")
            strings.add("부지역장")
            strings.add("회장")
            strings.add("부회장")
            strings.add("총무")
            strings.add("부총무")
            strings.add("장로")
            strings.add("권사")
            strings.add("안수집사")
            strings.add("집사")
            strings.add("성도")
            strings.add("단장")
            strings.add("지휘자")
            strings.add("부단장")
            strings.add("고문")
            strings.add("회계")
            return strings
        }

    @JvmStatic
    var selectedGroup: groupModel = HolyModel.groupModel()
        set(selectedGroup) {
            if (selectedGroup != null) LoggerHelper.d("setSelectedGroup 를 세팅합니다. $selectedGroup") else LoggerHelper.d("setSelectedGroup 를 세팅합니다. :: null ")
            selectedTeam = HolyModel.groupModel.teamModel()
            field = selectedGroup
        }
    @JvmStatic
    var selectedTeam: teamModel = HolyModel.groupModel.teamModel()
        set(selectedTeam) {
            if (selectedTeam != null) LoggerHelper.d("setSelectedTeam 를 세팅합니다. $selectedTeam") else LoggerHelper.d("setSelectedTeam 를 세팅합니다 >> null 입니다.")
            field = selectedTeam
        }

    @JvmStatic
    var selectedMember: memberModel? = HolyModel.memberModel()
        set(selectedMember) {
            if (selectedMember != null) LoggerHelper.d("setSelectedMember 를 세팅합니다. $selectedMember") else LoggerHelper.d("setSelectedMember 를 세팅합니다. :: null ")
            field = selectedMember
        }

    @JvmStatic
    val selectCorpInfo: Boolean
        get() = if (holyModel == null || groupModel == null || teamModel == null) false else true

    //CommonData.getHolyModel().group.values.also { list -> list.sortedBy { it.name }}
    //CommonData.getHolyModel().group.values.also { list -> list.sortedBy { it.name }}
    val groupList: HolyModel?
        get() =//CommonData.getHolyModel().group.values.also { list -> list.sortedBy { it.name }}
            null //CommonData.getHolyModel().group.values.also { list -> list.sortedBy { it.name }}

    @JvmStatic
    fun setViewMode(viewMode: ViewMode) {
        LoggerHelper.d("setViewMode", "뷰 모드가 $viewMode 로 변경되었습니다.")
        CommonData.viewMode = viewMode
    }

    @JvmStatic
    fun getGroupName(uid: String?): String {
        return groupMap!![uid]!!.name
    }

    @JvmStatic
    fun getTeamName(uid: String?): String {
        var tempName = ""
        if (teamMap!![uid] != null) {
            tempName = teamMap!![uid]!!.name
        }
        return tempName
    }

    /**
     * 현재 설정된 0000년00월(요일) 형태로 리턴
     *
     * @return
     */
    @JvmStatic
    val currentFullDateStr: String
        get() = selectedYear.toString() + "년 " + selectedMonth + "월 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(selectedDayOfWeek)) + " : " +
                CalendarUtils.getDaysTime(Integer.valueOf(selectedDays)) + ")"

    /**
     * 현재 설정된 0000년00월00일(요일) 형태로 리턴
     *
     * @return
     */
    val currentFullDayAndDaysStr: String
        get() = selectedYear.toString() + "년 " +
                selectedMonth + "월 " +
                selectedDay + "일 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(selectedDayOfWeek)) + ") " +
                CalendarUtils.getDaysTime(Integer.valueOf(selectedDays))

    /**
     * 앱설정을 초기화 합니다.
     */
    @JvmStatic
    fun setInitCommonSettings() {
        strSearch = ""
        /*memberShipType = null
        holyModelList = null*/
        isFstEnter = false
        //firebaseUser = null
        setGroupUid(null)
        setTeamUid(null)
        //memberUid = null
        //memberModel = null
        //selectedTeam = null
        //teamModel = null
        //adminUid = null
        //corpsUid = null
        corpsCnt = -1
        //holyModel = null
    }
}