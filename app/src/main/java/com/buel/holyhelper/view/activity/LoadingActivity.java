package com.buel.holyhelper.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FirebaseRemoteHelper;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.SharedPreferenceUtil;
import com.buel.holyhelper.utils.SortMapUtil;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.orhanobut.logger.LoggerHelper;

import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import io.fabric.sdk.android.Fabric;

public class LoadingActivity extends BaseActivity {

    private FirebaseRemoteHelper mFirebaseRemoteHelper;
    private static final boolean IS_DEBUGGING = true;
    private static String sGroupUid;
    private static String sTeamUid;
    private static HolyModel.groupModel.teamModel sTeamModel;
    private static HolyModel.groupModel sGroupModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_loading);

        //package name settings
        Common.PACKAGE_NAME = getApplicationContext().getPackageName();

        setLogger();
        LoggerHelper.i("LOADINGACTIVITY START", "로팅화면을 시작합니다.");

        FirebaseApp.initializeApp(this);

        //firebase remote setting
        mFirebaseRemoteHelper = new FirebaseRemoteHelper();
        mFirebaseRemoteHelper.setRemoteConfigFirebase(this, new FirebaseRemoteHelper.OnCallbackEventListener() {
            @Override
            public void onComplete(FirebaseRemoteConfig firebaseRemoteConfig) {
                LoggerHelper.i("setRemoteConfigFirebase START", "리모트 컨피그를 시작합니다.");
                displayMessage(firebaseRemoteConfig);
            }
        });

        //stage clicked event -> loginActivity
        RelativeLayout linearLayout_main = findViewById(R.id.splashactivity_linearlayout);
        linearLayout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });

        setPreferenced();
    }



    private void setPreferenced() {

        LoggerHelper.i("setPreferenced start", "프리퍼런스 설정을 시작합니다.");
        SharedPreferenceUtil.init(LoadingActivity.this);

        sGroupUid = SharedPreferenceUtil.getDatea(SharedPreferenceUtil.GROUP_UID, null);
        sTeamUid = SharedPreferenceUtil.getDatea(SharedPreferenceUtil.TEAM_UID, null);
        sTeamModel = SharedPreferenceUtil.getTeamData();
        sGroupModel = SharedPreferenceUtil.getGroupData();

        LoggerHelper.d("sGroupUid: " + sGroupUid);
        LoggerHelper.d("sTeamUid: " + sTeamUid);

        if (sGroupModel.name != null) {
            CommonData.setGroupModel(sGroupModel);
            LoggerHelper.d("sTeamModel: " + sGroupModel.name);
        }
        if (sTeamModel.name != null){
            CommonData.setTeamModel(sTeamModel);
            LoggerHelper.d("sTeamModel: " + sTeamModel.name);
        }
        if (sGroupUid != null) CommonData.setGroupUid(sGroupUid);
        if (sTeamUid != null) CommonData.setTeamUid(sTeamUid);

        LoggerHelper.i("프리퍼런스의 데이터(groupModel/teamModel)를 설정하였습니다.");

        if (CommonData.getSelectedYear() == -1) {
            Calendar cal = Calendar.getInstance();
            CommonData.setSelectedYear(cal.get(Calendar.YEAR));
            CommonData.setSelectedMonth(cal.get(Calendar.MONTH) + 1);
            CommonData.setSelectedDay(cal.get(Calendar.DAY_OF_MONTH));
            CommonData.setSelectedDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
            CommonData.setSelectedDays(1);

            LoggerHelper.i("날짜 데이터가 없어 현재시간으로 설정하였습니다.");
        }
    }


    /**
     * logger 세팅
     */
    private void setLogger() {
        LoggerHelper.setLogger(Common.PACKAGE_NAME, IS_DEBUGGING);
        LoggerHelper.i("LOG SETTING START", "로그를 세팅합니다.");
    }

    private void displayMessage(FirebaseRemoteConfig firebaseRemoteConfig) {
        LoggerHelper.i("displayMessage");
        boolean isCaps = firebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = firebaseRemoteConfig.getString("splash_message");
        String app_notice = firebaseRemoteConfig.getString("app_notice");
        boolean app_upgrade = firebaseRemoteConfig.getBoolean("app_upgrade");
        String app_upgrade_url = firebaseRemoteConfig.getString("app_upgrade_url");
        String ads_video_id = firebaseRemoteConfig.getString("ads_video_id");
        String ads_banner_id = firebaseRemoteConfig.getString("ads_banner_id");
        String ads_interstitial_id = firebaseRemoteConfig.getString("ads_interstitial_id");
        String app_ads_open = firebaseRemoteConfig.getString("app_ads_open");
        int app_ver = SortMapUtil.getInteger(firebaseRemoteConfig.getString("app_ver"));
        int personal_join_point = SortMapUtil.getInteger(firebaseRemoteConfig.getString("personal_join_point"));
        int tutorial_point = SortMapUtil.getInteger(firebaseRemoteConfig.getString("tutorial_point"));
        int video_ads_point = SortMapUtil.getInteger(firebaseRemoteConfig.getString("video_ads_point"));

        try {
            CommonData.setIsAdsOpen(app_ads_open);
            CommonData.setVideoAdsPoint(video_ads_point);
            CommonData.setPersonalJoinPoint(personal_join_point);
            CommonData.setTutorialPoint(tutorial_point);
            CommonData.setLevel1(0);
            CommonData.setLevel2(Integer.parseInt(firebaseRemoteConfig.getString("app_level2")));
            CommonData.setLevel3(Integer.parseInt(firebaseRemoteConfig.getString("app_level3")));
            CommonData.setLevel4(Integer.parseInt(firebaseRemoteConfig.getString("app_level4")));
            CommonData.setLevel5(Integer.parseInt(firebaseRemoteConfig.getString("app_level5")));
        } catch (Exception e) {
            LoggerHelper.e(e.getMessage());
        }
        CommonData.setAdsBannerId(ads_banner_id);
        CommonData.setAdsInterstitialId(ads_interstitial_id);
        CommonData.setAdsVideoId(ads_video_id);
        int device_version = 0;

        try {
            device_version = SortMapUtil.getInteger((getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
            LoggerHelper.d("device_version : " + device_version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoggerHelper.i("device_version > app_ver : " + device_version + " // " + app_ver);
        if (device_version < app_ver) {
            LoggerHelper.i("displayMessage app_upgrade");
            MaterialDailogUtil.Companion.simpleDoneDialog(
                    LoadingActivity.this,
                    "업그레이드가 필요합니다.",
                    "확인버튼을 클릭하여 업그레이드를 실행하여주세요. \n업데이트가 되지 않으면 삭제후 다시 설치해주세요.",
                    new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {
                            //SuperToastUtil.toastE(LoadingActivity.this, "업그레이드가 필요합니다. : " + app_upgrade_url);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(app_upgrade_url));
                            startActivity(intent);
                            finish();
                        }
                    });
            return;
        }

        LoggerHelper.d("app_notice : " + app_notice);
        if (app_notice != null) CommonData.setAppNotice(app_notice);

        if (isCaps) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //goLogin();
                }
            });
            builder.create().show();
        } else {
            if (!app_upgrade) {
                //goLogin();
            }
        }
    }
}