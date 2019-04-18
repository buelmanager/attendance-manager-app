package com.buel.holyhelper.data;

import com.buel.holyhelper.model.AnalyticsModel;
import com.buel.holyhelper.model.AttendModel;
import com.buel.holyhelper.model.DateModel;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.model.TutorialModel;
import com.buel.holyhelper.model.UserModel;
import com.buel.holyhelper.utils.CalendarUtils;
import com.buel.holyhelper.utils.SharedPreferenceUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.logger.LoggerHelper;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 19001283 on 2018-06-01.
 */

public class CommonData {

    private static Map<String, HolyModel.memberModel> membersMap;

    public static Map<String, HolyModel.memberModel> getMembersMap() {
        return membersMap;
    }

    public static void setMembersMap(Map<String, HolyModel.memberModel> membersMap) {
        if (membersMap!=null)LoggerHelper.d("setMembersMap 를 세팅합니다. " + membersMap.toString());
        else LoggerHelper.d("setMembersMap 를 세팅합니다. :: null ");

        CommonData.membersMap = membersMap;
    }

    private static int currentSelectViewPageNum = 0;

    public static int getCurrentSelectViewPageNum() {
        return currentSelectViewPageNum;
    }

    public static void setCurrentSelectViewPageNum(int currentSelectViewPageNum) {
        CommonData.currentSelectViewPageNum = currentSelectViewPageNum;
    }

    private static Boolean isFireStoreMode = true;

    public static Boolean getIsFireStoreMode() {
        return isFireStoreMode;
    }

    public static void setIsFireStoreMode(Boolean isFireStoreMode) {
        CommonData.isFireStoreMode = isFireStoreMode;
    }

    private static PremiupType currentPremiumType = PremiupType.NORAML;

    public static PremiupType getCurrentPremiumType() {
        return currentPremiumType;
    }

    public static void setCurrentPremiumType(PremiupType currentPremiumType) {
        LoggerHelper.d("현재 프리미엄 타입은 :  " + currentPremiumType);
        CommonData.currentPremiumType = currentPremiumType;
    }

    private static List<TutorialModel> tutorialModelList;

    public static List<TutorialModel> getTutorialModelList() {
        return tutorialModelList;
    }

    public static void setTutorialModelList(List<TutorialModel> tutorialModelList) {
        CommonData.tutorialModelList = tutorialModelList;
    }

    private static String searchAddress;

    public static String getSearchAddress() {
        return searchAddress;
    }

    public static void setSearchAddress(String searchAddress) {
        CommonData.searchAddress = searchAddress;
    }

    private static AnalMode analMode;

    public static AnalMode getAnalMode() {
        return analMode;
    }

    public static void setAnalMode(AnalMode analMode) {
        LoggerHelper.d("setAnalMode 를 세팅합니다. " + analMode.toString());
        CommonData.analMode = analMode;
    }

    public static Class<T> getHistoryClass() {
        return historyClass;
    }

    public static void setHistoryClass(Class<T> historyClass) {
        CommonData.historyClass = historyClass;
    }

    private static Class<T> historyClass;
    private static List<Class> historyClassList = new ArrayList<>();

    public static Class getHistoryClassListLastIndex() {
        Class historyClass = CommonData.historyClassList.get(CommonData.historyClassList.size() - 1);
        CommonData.setHistoryClassListLastRemove();
        return historyClass;

    }

    public static List<Class> getHistoryClassList() {
        return historyClassList;
    }

    public static void setHistoryClassListLastRemove() {
        CommonData.historyClassList.remove(CommonData.historyClassList.size() - 1);
    }

    public static void setHistoryClassList(List<Class> historyClassList) {
        CommonData.historyClassList = historyClassList;
    }

    public static boolean isTutoMode() {
        return isTutoMode;
    }

    public static void setIsTutoMode(boolean isTutoMode) {
        CommonData.isTutoMode = isTutoMode;
    }

    private static boolean isTutoMode = false;

    //TABLE NAME
    public static final String ADMOB_APP_ID = "ca-app-pub-7585440404153175~9114562877";
    //TEST "ca-app-pub-3940256099942544/5224354917";
    //ca-app-pub-7585440404153175/9007585812

    private static String appNotice;

    public static String getAppNotice() {
        return appNotice;
    }

    public static void setAppNotice(String appNotice) {
        CommonData.appNotice = appNotice;
    }

    private static boolean isFstEnter = true;

    public static boolean getIsFstEnter() {
        return CommonData.isFstEnter;
    }

    public static void setIsFstEnter(boolean isFstEnter) {
        CommonData.isFstEnter = isFstEnter;
    }

    //@@@@@ 최고 관리자의 UID
    private static String adminUid;
    //@@@@@

    private static String isAdsOpen = "true";

    public static String getIsAdsOpen() {
        return isAdsOpen;
    }

    public static void setIsAdsOpen(String isAdsOpen) {
        LoggerHelper.d("광고모드가 변경됩니다 : " + isAdsOpen);
        CommonData.isAdsOpen = isAdsOpen;
    }

    //회원관리 관련
    private static int corpsCnt;
    private static String corpsUid;
    private static String groupUid;
    private static String teamUid;
    private static String memberUid;
    private static HolyModel holyModel;
    private static HolyModel.groupModel groupModel;
    private static HolyModel.groupModel.teamModel teamModel;
    private static HolyModel.memberModel memberModel;
    private static UserModel userModel;
    private static Map<Integer, HolyModel> holyModelMap;
    private static FirebaseUser firebaseUser;
    private static String corpsPassWord;
    private static GoogleSignInClient GoogleSignInClient;

    public static com.google.android.gms.auth.api.signin.GoogleSignInClient getGoogleSignInClient() {
        return GoogleSignInClient;
    }

    public static void setGoogleSignInClient(com.google.android.gms.auth.api.signin.GoogleSignInClient googleSignInClient) {
        GoogleSignInClient = googleSignInClient;
    }

    public static String getCorpsPassWord() {
        return corpsPassWord;
    }

    public static void setCorpsPassWord(String corpsPassWord) {
        CommonData.corpsPassWord = corpsPassWord;
    }

    public static String adsVideoId;
    public static String adsBannerId;

    public static String getAdsBannerId() {
        return adsBannerId;
    }

    public static void setAdsBannerId(String adsBannerId) {
        CommonData.adsBannerId = adsBannerId;
    }

    public static String getAdsInterstitialId() {
        return adsInterstitialId;
    }

    public static void setAdsInterstitialId(String adsInterstitialId) {
        CommonData.adsInterstitialId = adsInterstitialId;
    }

    public static String adsInterstitialId;

    public static String getAdsVideoId() {
        return adsVideoId;
    }

    public static void setAdsVideoId(String adsVideoId) {
        LoggerHelper.d("setAdsVideoId 를 세팅합니다. " + adsVideoId);
        CommonData.adsVideoId = adsVideoId;
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public static void setFirebaseUser(FirebaseUser firebaseUser) {
        CommonData.firebaseUser = firebaseUser;
    }

    public static Map<Integer, HolyModel> getHolyModelMap() {
        return holyModelMap;
    }

    public static void setHolyModelMap(Map<Integer, HolyModel> holyModelMap) {
        CommonData.holyModelMap = holyModelMap;
    }

    public static List<HolyModel> getHolyModelList() {
        return holyModelList;
    }

    public static void setHolyModelList(List<HolyModel> holyModelList) {
        if (holyModelList!=null)LoggerHelper.d("setHolyModelList 를 세팅합니다. " + holyModelList.toString());
        else LoggerHelper.d("setHolyModelList 를 세팅합니다. :: null ");

        CommonData.holyModelList = holyModelList;
    }

    private static List<HolyModel> holyModelList;

    public static UserType getMemberShipType() {
        return memberShipType;
    }

    //@@@@
    public static void setMemberShipType(UserType memberShipType) {
        if (memberShipType!=null)LoggerHelper.d("setMemberShipType 를 세팅합니다. " + memberShipType.toString());
        else LoggerHelper.d("setMemberShipType 를 세팅합니다. :: null ");

        CommonData.memberShipType = memberShipType;
    }

    private static UserType memberShipType;

    //통계관리 관련
    private static int selectedYear = -1;
    private static int selectedMonth = -1;
    private static int selectedDays = -1;
    private static int selectedDay = -1;
    private static int selectedHour = -1;
    private static int selectedMinute = -1;
    private static int selectedDayOfWeek = -1;
    private static int level1 = -1;
    private static int level2 = -1;
    private static int level3 = -1;
    private static int level4 = -1;

    public static int getVideoAdsPoint() {
        return videoAdsPoint;
    }

    public static void setVideoAdsPoint(int videoAdsPoint) {
        CommonData.videoAdsPoint = videoAdsPoint;
    }

    private static int videoAdsPoint = 0;

    public static int getPersonalJoinPoint() {
        return personalJoinPoint;
    }

    public static void setPersonalJoinPoint(int personalJoinPoint) {
        CommonData.personalJoinPoint = personalJoinPoint;
    }

    private static int tutorialPoint = 0;
    private static int personalJoinPoint = 0;

    public static int getTutorialPoint() {
        return tutorialPoint;
    }

    public static void setTutorialPoint(int tutorialPoint) {
        CommonData.tutorialPoint = tutorialPoint;
    }

    public static int getLevel1() {
        return level1;
    }

    public static void setLevel1(int level1) {
        CommonData.level1 = level1;
    }

    public static int getLevel2() {
        return level2;
    }

    public static void setLevel2(int level2) {
        CommonData.level2 = level2;
    }

    public static int getLevel3() {
        return level3;
    }

    public static void setLevel3(int level3) {
        CommonData.level3 = level3;
    }

    public static int getLevel4() {
        return level4;
    }

    public static void setLevel4(int level4) {
        CommonData.level4 = level4;
    }

    public static int getLevel5() {
        return level5;
    }

    public static void setLevel5(int level5) {
        CommonData.level5 = level5;
    }

    private static int level5 = -1;
    //현재 그룹의 출석 모델
    private static AnalyticsModel.Attendance.group currentAnalyticsGroupModel;
    //현재 단체의 그룹 map
    private static HashMap<String, AnalyticsModel.Attendance.group> attendGroupMaps;
    //현재 단체,그룹의 Date map
    private static HashMap<String, DateModel> attendDateMaps;
    //현재 단체, 그룹의 DateModel
    private static DateModel currentAnalyticsDateModel;
    //현재 단체, 그룹, 날짜의 멤버 MAP
    private static HashMap<String, AttendModel> attendMemberMap;
    //화면 모드 관련
    private static ViewMode viewMode;

    public static AdminMode getAdminMode() {
        return adminMode;
    }

    public static void setAdminMode(AdminMode adminMode) {

        LoggerHelper.d("setAdminMode", "AdminMode : " + adminMode   + " 으로 변경됩니다.");
        CommonData.adminMode = adminMode;
    }

    private static AdminMode adminMode;

    public static String getStrSearch() {
        return strSearch;
    }

    //@@@
    public static void setStrSearch(String strSearch) {
        LoggerHelper.d("setStrSearch 를 세팅합니다. " + strSearch);
        CommonData.strSearch = strSearch;
    }

    private static String strSearch;

    public static String getGroupUid() {
        return groupUid;
    }

    public static void setGroupUid(String groupUid) {
        LoggerHelper.d("setGroupUid 를 세팅합니다. " + groupUid);
        CommonData.groupUid = groupUid;
    }

    public static String getTeamUid() {
        return teamUid;
    }

    public static void setTeamUid(String teamUid) {
        LoggerHelper.d("setTeamUid 를 세팅합니다. " + teamUid);
        CommonData.teamUid = teamUid;
    }

    public static String getMemberUid() {
        return memberUid;
    }

    public static void setMemberUid(String memberUid) {
        LoggerHelper.d("setMemberUid 를 세팅합니다. " + memberUid);
        CommonData.memberUid = memberUid;
    }

    public static HolyModel.memberModel getMemberModel() {
        return memberModel;
    }

    public static List<String> getPositionList() {
        List<String> strings = new ArrayList<>();
        strings.add("담임목사");
        strings.add("담임사모");
        strings.add("목사");
        strings.add("전도사");
        strings.add("교육사");
        strings.add("간사");
        strings.add("부장");
        strings.add("지역장");
        strings.add("부지역장");
        strings.add("회장");
        strings.add("부회장");
        strings.add("총무");
        strings.add("부총무");
        strings.add("장로");
        strings.add("권사");
        strings.add("안수집사");
        strings.add("집사");
        strings.add("성도");
        strings.add("단장");
        strings.add("지휘자");
        strings.add("부단장");
        strings.add("고문");
        strings.add("회계");
        return strings;
    }

    public static void setMemberModel(HolyModel.memberModel memberModel) {

        if (memberModel!=null)LoggerHelper.d("setMemberModel 를 세팅합니다. " + memberModel.toString());
        else LoggerHelper.d("setMemberModel 를 세팅합니다. :: null ");

        CommonData.memberModel = memberModel;
    }

    private static HolyModel.groupModel selectedGroup;
    private static HolyModel.groupModel.teamModel selectedTeam;

    public static HolyModel.groupModel getSelectedGroup() {
        return selectedGroup;
    }

    public static void setSelectedGroup(HolyModel.groupModel selectedGroup) {
        if (selectedGroup!=null)LoggerHelper.d("setSelectedGroup 를 세팅합니다. " + selectedGroup.toString());
        else LoggerHelper.d("setSelectedGroup 를 세팅합니다. :: null ");

        CommonData.setSelectedTeam(null);
        CommonData.selectedGroup = selectedGroup;
    }

    public static HolyModel.groupModel.teamModel getSelectedTeam() {
        return selectedTeam;
    }

    public static void setSelectedTeam(HolyModel.groupModel.teamModel selectedTeam) {

        if (selectedTeam!=null)
            LoggerHelper.d("setSelectedTeam 를 세팅합니다. " + selectedTeam.toString());
        else
            LoggerHelper.d("setSelectedTeam 를 세팅합니다 >> null 입니다.");
        CommonData.selectedTeam = selectedTeam;
    }

    public static HolyModel.memberModel getSelectedMember() {
        return selectedMember;
    }

    public static void setSelectedMember(HolyModel.memberModel selectedMember) {

        if (selectedMember!=null)LoggerHelper.d("setSelectedMember 를 세팅합니다. " + selectedMember.toString());
        else LoggerHelper.d("setSelectedMember 를 세팅합니다. :: null ");

        CommonData.selectedMember = selectedMember;
    }

    private static HolyModel.memberModel selectedMember;

    public static HolyModel.groupModel.teamModel getTeamModel() {
        return teamModel;
    }

    public static void setTeamModel(HolyModel.groupModel.teamModel teamModel) {
        if (teamModel!=null)LoggerHelper.d("setTeamModel 를 세팅합니다. " + teamModel.toString());
        else LoggerHelper.d("setTeamModel 를 세팅합니다. :: null ");

        if (teamModel != null) {
            CommonData.teamModel = teamModel;
            CommonData.setTeamUid(teamModel.uid);
            SharedPreferenceUtil.putTeamModel(teamModel);

        } else {
            CommonData.teamModel = null;
            CommonData.teamUid = null;
            SharedPreferenceUtil.initTeamModel();
        }
    }

    public static Boolean getSelectCorpInfo() {
        if (CommonData.getHolyModel() == null ||
                CommonData.getGroupModel() == null ||
                CommonData.getTeamModel() == null)
            return false;
        else return true;
    }

    public static HolyModel.groupModel getGroupModel() {
        return groupModel;
    }

    public static void setGroupModel(HolyModel.groupModel groupModel) {

        if (groupModel!=null)LoggerHelper.d("setGroupModel 를 세팅합니다. " + groupModel.toString());
        else LoggerHelper.d("setGroupModel 를 세팅합니다. :: null ");

        if (groupModel != null) {
            CommonData.groupModel = groupModel;
            CommonData.setGroupUid(groupModel.uid);
            SharedPreferenceUtil.putGroupModel(groupModel);

        } else {
            CommonData.groupModel = null;
            CommonData.groupUid = null;
            SharedPreferenceUtil.initGroupModel();
        }
    }

    public static UserModel getUserModel() {
        return userModel;
    }

    public static void setUserModel(UserModel userModel) {
        CommonData.userModel = userModel;
    }

    public static int getCorpsCnt() {
        return corpsCnt;
    }

    public static void setCorpsCnt(int corpsCnt) {
        LoggerHelper.d("setCorpsCnt 를 세팅합니다. " + corpsCnt);
        CommonData.corpsCnt = corpsCnt;
    }

    public static String getCorpsUid() {
        return corpsUid;
    }

    public static void setCorpsUid(String corpsUid) {
        LoggerHelper.d("setCorpsUid 를 세팅합니다. " + corpsUid);
        CommonData.corpsUid = corpsUid;
    }

    public static String getAdminUid() {
        return adminUid;
    }

    public static void setAdminUid(String adminUid) {
        LoggerHelper.d("setAdminUid", "setAdminUid " + adminUid + " 로 변경되었습니다.");
        CommonData.adminUid = adminUid;
    }

    public static HolyModel getHolyModel() {
        return holyModel;
    }

    public static void setHolyModel(HolyModel holyModel) {

        if (holyModel!=null)LoggerHelper.d("setHolyModel 를 세팅합니다. " + holyModel.toString());
        else LoggerHelper.d("setHolyModel 를 세팅합니다. :: null ");

        CommonData.holyModel = holyModel;
    }

    public static ViewMode getViewMode() {
        return viewMode;
    }

    public static void setViewMode(ViewMode viewMode) {
        LoggerHelper.d("setViewMode", "뷰 모드가 " + viewMode + " 로 변경되었습니다.");
        CommonData.viewMode = viewMode;
    }

    public static int getSelectedYear() {
        return selectedYear;
    }

    public static void setSelectedYear(int selectedYear) {
        LoggerHelper.d("setSelectedYear 를 세팅합니다. " + selectedYear);
        CommonData.selectedYear = selectedYear;
    }

    public static int getSelectedMonth() {
        return selectedMonth;
    }

    public static void setSelectedMonth(int selectedMonth) {
        LoggerHelper.d("setSelectedMonth 를 세팅합니다. " + selectedMonth);
        CommonData.selectedMonth = selectedMonth;
    }

    public static int getSelectedDays() {
        return selectedDays;
    }

    public static void setSelectedDays(int selectedDays) {
        LoggerHelper.d("setSelectedDays 를 세팅합니다. " + selectedDays);
        CommonData.selectedDays = selectedDays;
    }

    public static int getSelectedDay() {
        return selectedDay;
    }

    public static void setSelectedDay(int selectedDay) {
        LoggerHelper.d("setSelectedDay 를 세팅합니다. " + selectedDay);
        CommonData.selectedDay = selectedDay;
    }

    public static int getSelectedHour() {
        return selectedHour;
    }

    public static void setSelectedHour(int selectedHour) {
        CommonData.selectedHour = selectedHour;
    }

    public static int getSelectedMinute() {
        return selectedMinute;
    }

    public static void setSelectedMinute(int selectedMinute) {
        CommonData.selectedMinute = selectedMinute;
    }

    public static int getSelectedDayOfWeek() {
        return selectedDayOfWeek;
    }

    public static void setSelectedDayOfWeek(int selectedDayOfWeek) {
        LoggerHelper.d("setSelectedDayOfWeek 를 세팅합니다. " + selectedDayOfWeek);
        CommonData.selectedDayOfWeek = selectedDayOfWeek;
    }

    public static AnalyticsModel.Attendance.group getCurrentAnalyticsGroupModel() {
        return currentAnalyticsGroupModel;
    }

    public static void setCurrentAnalyticsGroupModel(AnalyticsModel.Attendance.group currentAnalyticsGroupModel) {
        CommonData.currentAnalyticsGroupModel = currentAnalyticsGroupModel;
    }

    public static HashMap<String, AnalyticsModel.Attendance.group> getAttendGroupMaps() {
        return attendGroupMaps;
    }

    public static void setAttendGroupMaps(HashMap<String, AnalyticsModel.Attendance.group> attendGroupMaps) {
        CommonData.attendGroupMaps = attendGroupMaps;
    }

    public static HashMap<String, DateModel> getAttendDateMaps() {
        return attendDateMaps;
    }

    public static void setAttendDateMaps(HashMap<String, DateModel> attendDateMaps) {
        LoggerHelper.d("setAttendDateMaps 를 세팅합니다. " + attendDateMaps);


        if (attendDateMaps!=null)LoggerHelper.d("setAttendDateMaps 를 세팅합니다. " + attendDateMaps.toString());
        else LoggerHelper.d("setAttendDateMaps 를 세팅합니다. :: null ");

        CommonData.attendDateMaps = attendDateMaps;
    }

    public static DateModel getCurrentAnalyticsDateModel() {
        return currentAnalyticsDateModel;
    }

    public static void setCurrentAnalyticsDateModel(DateModel currentAnalyticsDateModel) {
        CommonData.currentAnalyticsDateModel = currentAnalyticsDateModel;
    }

    public static HashMap<String, AttendModel> getAttendMemberMap() {
        return attendMemberMap;
    }

    public static void setAttendMemberMap(HashMap<String, AttendModel> attendMemberMap) {
        CommonData.attendMemberMap = attendMemberMap;
    }

    public static String getGroupName(String uid){
        return CommonData.getHolyModel().group.get(uid).name;
    }

    public static String getTeamName(String uid){
        return CommonData.getGroupModel().team.get(uid).name;
    }
    /**
     * 현재 설정된 0000년00월(요일) 형태로 리턴
     *
     * @return
     */
    public static String getCurrentFullDateStr() {
        return CommonData.getSelectedYear() + "년 " + CommonData.getSelectedMonth() + "월 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(CommonData.getSelectedDayOfWeek())) + ")";
    }

    /**
     * 현재 설정된 0000년00월00일(요일) 형태로 리턴
     *
     * @return
     */
    public static String getCurrentFullDayAndDaysStr() {
        return CommonData.getSelectedYear() + "년 " +
                CommonData.getSelectedMonth() + "월 " +
                CommonData.getSelectedDay() + "일 " +
                "(" + CalendarUtils.getDateDay(Integer.valueOf(CommonData.getSelectedDayOfWeek())) + ") " +
                CalendarUtils.getDaysTime(Integer.valueOf(CommonData.getSelectedDays()));
    }

    /**
     * 앱설정을 초기화 합니다.
     */
    public static void setInitCommonSettings() {
        setStrSearch(null);
        setMemberShipType(null);
        setHolyModelList(null);
        setIsFstEnter(false);
        setFirebaseUser(null);
        setGroupUid(null);
        setTeamUid(null);
        setMemberUid(null);
        setMemberModel(null);
        setSelectedTeam(null);
        setTeamModel(null);
        setAdminUid(null);
        setCorpsUid(null);
        setCorpsCnt(-1);
        setHolyModel(null);
    }
}
