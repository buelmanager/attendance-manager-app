package com.buel.holyhelper.data;

import android.content.Context;
import android.content.Intent;

import com.buel.holyhelper.R;
import com.buel.holyhelper.model.TutorialModel;
import com.buel.holyhelper.utils.AppUtil;
import com.buel.holyhelper.view.activity.TutorialViewActivity;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class TutorialViewerUtil {

   /**
     * 신규가입 튜토리얼
     * @return
     */
    @NonNull
    public static void getNewUserTutorialModels(Context context) {

        LoggerHelper.d("getNewUserTutorialModels start");

        List<TutorialModel> tutorialModels = new ArrayList<>();
        tutorialModels.add( getTutorialModel(
                R.drawable.tuto1,
                "# 튜토리얼을 완료하면 포인트 추가!!",
                "튜토리얼을 완료하면" + CommonData.getTutorialPoint() + "P 를 지급합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto1,
                "#1 단계, 교회설정",
                "확인을 클릭하여 다음으로 넘어갑니다."));
        tutorialModels.add( getTutorialModel(
                R.drawable.tuto2,
                "#1 단계, 교회설정",
                "교회설정에 정확한 정보를 입력하여야\n추후 운영관리자 추가시에 찾기가 가능합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto3,
                "#2 단계, 부서 설정",
                "확인을 클릭하여 다음으로 넘어갑니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto4,
                "#2 단계, 부서 설정",
                "부서명에는 남선교, 찬양단, 성가대 \n청년부 등등 원하시는부서등의 그룹의 이름을 씁니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto5,
                "#3 단계, 소그룹 설정",
                "확인을 클릭하여 다음으로 넘어갑니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto6,
                "#3 단계, 소그룹 설정",
                "소그룹번호는 고유한 번호로 숫자를 입력합니다.\n소그룹번호는 순서입니다.\n명칭/이름에는 해당 소그룹의 명칭 또는\n미입력시에는 해당 그룹명이 들어갑니다.\n 000 찬양단, 000 성가대 등"));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto7,
                "#4 단계, 성도/회원 등록",
                "확인을 클릭하여 다음으로 넘어갑니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto8,
                "#4 단계, 성도/회원 등록",
                "필수항목은 후에 통계에 대한 빅데이터로\n활용이됩니다. 정확한 입력이 필요합니다.\n활용 예)인도자 : 해당인도자의 전도률, 정착률, 전도지역 \n활용 예)주소 : 지역별 전도율 등등.."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto9,
                "#5 단계, 출석체크하기",
                "확인을 클릭하여 다음으로 넘어갑니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto10,
                "#5 단계, 출석체크하기",
                "오른쪽 체크박스를 클릭하여 출석을 체크합니다. \n 체크될때가 출석이된 상태이고 클릭시 바로 서버전송!\n붉은색 말풍선은 결석사유입니다.\n간단한 결석사유를 저장하여 추후 통계로\n사용합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tuto11,
                "#6 준비가 되셨나요?.",
                "준비가 되셨으면 튜토리얼을 진행하여 보세요."));
        setStratTutorialActivity(context,tutorialModels);
    }
    /**
     * 단체, 그룹, 팀, 멤버 설정 관련
     * @return
     */
    @NonNull
    public static void getGroupSelectTutorial(Context context) {

        List<TutorialModel> tutorialModels = new ArrayList<>();
        tutorialModels.add( getTutorialModel(
                R.drawable.group1,
                "#1 부서 선택 ",
                "원하시는 부서를 선택하여\n출석체크/통계화면을 활용할수있습니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.group2,
                "#2 부서 리스트 추가 ",
                "버튼을 클릭하여서 \n부서를 추가하실수 있습니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.group3,
                "#3 부서 수정 하기 ",
                "버튼을 클릭하여서 \n부서를 수정하실수 있습니다."));
        tutorialModels.add( getTutorialModel(
                R.drawable.group4,
                "#4 부서 수정 하기  ",
                "수정 및 삭제가 가능합니다. "));

        setStratTutorialActivity(context,tutorialModels);
    }

    @NonNull
    public static void getTeamSelectTutorial(Context context) {

        List<TutorialModel> tutorialModels = new ArrayList<>();
        tutorialModels.add( getTutorialModel(
                R.drawable.team1,
                "#1 소그룹 선택 ",
                "원하시는 소그룹을 선택하여\n출석체크/통계화면을 활용할수있습니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.team2,
                "#2 소그룹 리스트 추가 ",
                "버튼을 클릭하여서 \n소그룹을 추가하실수 있습니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.team3,
                "#3 소그룹 수정 하기 ",
                "버튼을 클릭하여서 \n소그룹을 수정하실수 있습니다."));
        tutorialModels.add( getTutorialModel(
                R.drawable.team4,
                "#4 소그룹 수정 하기  ",
                "수정 및 삭제가 가능합니다. "));

        setStratTutorialActivity(context,tutorialModels);
    }

    /**
     * 단체, 그룹, 팀, 멤버 설정 관련
     * @return
     */
    @NonNull
    public static void getTeamAnalHelperTutorial(Context context) {

        List<TutorialModel> tutorialModels = new ArrayList<>();
        tutorialModels.add( getTutorialModel(
                R.drawable.tanal6,
                "#1 소그룹별 통계 ",
                "현재 선택된 달에서 현재 선택된 부서의 \n모든 소그룹의 통계가 표현됩니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tanal7,
                "#2 월별 통계 ",
                "현재 선택된 소그룹의 각 월별 통계가 표현됩니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tanal8,
                "#3 소그룹 상세통계 ",
                "현재 그룹의 선택될 달의 통계가 표현됩니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tanal2,
                "#4 소그룹 상세통계 ",
                "화면에 그래프를 클릭하면 아래 상세 데이터가나옵니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tanal3,
                "#5 소그룹 상세통계 ",
                "그래프로 선택된 날짜의 상세 데이터입니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tanal4,
                "#6 소그룹 상세통계 ",
                "공유 버튼을 통해서 공유된 모습니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.tanal5,
                "#7 소그룹 상세통계 ",
                "카카오톡 등 다른 앱으로 공유가능합니다."));

        setStratTutorialActivity(context,tutorialModels);
    }

    @NonNull
    public static void getMemberAccountAdminTutorialModels(Context context) {

        List<TutorialModel> tutorialModels = new ArrayList<>();
        tutorialModels.add( getTutorialModel(
                R.drawable.memberadmin1,
                "#1 교적 관리 진입지점 1",
                "메인화면의 좌측 슬라이드 메뉴에서 진입합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.memberadmin6,
                "#1 교적 관리 진입지점 2",
                "설정화면의 맨아래 버튼에서 진입합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.memberadmin5,
                "#2 교적 관리",
                "현재 교회에 등록된 모든 교인이 나옵니다.\n* 부서가 다르면 이름이 중복될수 있습니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.memberadmin2,
                "#3 교적 관리",
                "하단 퀵메뉴를 통해 검색이 가능합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.memberadmin3,
                "#4 교적 관리",
                "이름을 입력합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.memberadmin4,
                "#5 교적 관리",
                "검색된 리스트가 나옵니다.\n*부서가 다른경우에는 중복이되어서 표시됩니다."));

        setStratTutorialActivity(context,tutorialModels);
    }

    /**
     * 단체, 그룹, 팀, 멤버 설정 관련
     * @return
     */
    @NonNull
    public static void getSelectionTutorialModels(Context context) {

        List<TutorialModel> tutorialModels = new ArrayList<>();
        tutorialModels.add( getTutorialModel(
                R.drawable.selection1,
                "#1 구글로그인",
                "구글 버튼을 클릭하여 로그인을 합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.selection2,
                "#1 구글로그인",
                "구글 버튼을 클릭하여 로그인을 합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.selection3,
                "#1 구글로그인",
                "구글 버튼을 클릭하여 로그인을 합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.selection4,
                "#1 구글로그인",
                "구글 버튼을 클릭하여 로그인을 합니다."));

        setStratTutorialActivity(context,tutorialModels);
    }

    /**
     * 로그인 및 개정 등록
     * @return
     */
    @NonNull
    public static void getCreateAccountTutorialModels(Context context) {
        List<TutorialModel> tutorialModels = new ArrayList<>();

        tutorialModels.add( getTutorialModel(
                R.drawable.log_in1,
                "#1 구글로그인",
                "구글 버튼을 클릭하여 로그인을 합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.log_in2,
                "#2 구글로그인",
                "로그인할 아이디를 클릭합나디."));

        tutorialModels.add( getTutorialModel(
                R.drawable.super_admin1,
                "# 먼저 최고관리자를 진행합니다. >> ",
                "최고 관리자는 모든 권한을 갖는 관리자입니다. " +
                        "\n 교회 정보변경, 부서 설정, 소그룹 설정 \n 교회내의 1개 계정만 가능합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.super_admin1,
                "#3 계정타입선택 : 최고 관리자.",
                "최고 관리자는 모든 권한을 갖는 관리자입니다. " +
                        "\n 교회 정보변경, 부서 설정, 소그룹 설정"));

        tutorialModels.add( getTutorialModel(
                R.drawable.super_admin2,
                "#4 '동의합니다' 선택.",
                "동의합니다. 를 클릭하여 계속진행합니다.\n운영관리자로 변경시 다시선택을 선택하세요. "));

        tutorialModels.add( getTutorialModel(
                R.drawable.super_admin3,
                "#5 관리자 정보 등록 : 최고관리자",
                "홀리헬퍼와 원활한 소통을 위해서\n관리자 정보는 정확히 입력해주세요."));

        tutorialModels.add( getTutorialModel(
                R.drawable.sub_admin1,
                "# 다음 운영 관리자를 진행합니다. >> ",
                "운영 관리자는 제한적인 권한만 갖게 됩니다." +
                        "\n교회내의 계정은 무한대로 가능합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.sub_admin1,
                "#6 계정타입선택 : 운영 관리자.",
                "운영관리자는 성도/회원 등록 및 통계/출석 등의 \n제한적 권한을 갖는 관리자입니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.super_admin2,
                "#7 '동의합니다' 선택.",
                "동의합니다. 를 클릭하여 계속진행합니다.\n최고관리자로 변경시 다시선택을 선택하세요. "));

        tutorialModels.add( getTutorialModel(
                R.drawable.sub_admin2,
                "#8 교회 등록 : 운영관리자",
                "홀리헬퍼와 원활한 소통을 위해서\n관리자 정보는 정확히 입력해주세요.\n운영하고자 하는 교회를 선택해야 합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.sub_admin3,
                "#9 교회 등록 : 운영관리자",
                "교회의 이름만 넣어도 검색이 됩니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.sub_admin4,
                "#10 교회 등록 : 운영관리자",
                "교회의 정보를 확인하고 선택합니다."));

        tutorialModels.add( getTutorialModel(
                R.drawable.sub_admin5,
                "#11 교회 등록 : 운영관리자",
                "가입후에는 최고관리자에게 연락하여\n운영권한을 부여받아야 로그인이 가능합니다."));


        setStratTutorialActivity(context,tutorialModels);
    }

    public static void setStratTutorialActivity(Context context, List<TutorialModel> tutorialModels){
        CommonData.setTutorialModelList(tutorialModels);
        Intent intent = new Intent(context, TutorialViewActivity.class);
        context.startActivity(intent);
    }

    @NonNull
    public static TutorialModel getTutorialModel(
            int drawable, String title, String content) {
        TutorialModel tutorialModel = new TutorialModel();
        tutorialModel.drawable = drawable;
        tutorialModel.title = title;
        tutorialModel.content = content;
        tutorialModel.color = R.color.material_500_deep_purple;//AppUtil.getRandomMaterialColor();
        tutorialModel.color = AppUtil.getRandomMaterialColor();
        return tutorialModel;
    }
}
