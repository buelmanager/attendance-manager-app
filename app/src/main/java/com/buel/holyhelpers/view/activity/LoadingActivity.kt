package com.buel.holyhelpers.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.adsBannerId
import com.buel.holyhelpers.data.CommonData.adsInterstitialId
import com.buel.holyhelpers.data.CommonData.adsVideoId
import com.buel.holyhelpers.data.CommonData.daumAddUrl
import com.buel.holyhelpers.data.CommonData.groupModel
import com.buel.holyhelpers.data.CommonData.selectedDay
import com.buel.holyhelpers.data.CommonData.selectedDayOfWeek
import com.buel.holyhelpers.data.CommonData.selectedDays
import com.buel.holyhelpers.data.CommonData.selectedYear
import com.buel.holyhelpers.data.CommonData.setGroupUid
import com.buel.holyhelpers.data.CommonData.setTeamUid
import com.buel.holyhelpers.data.CommonData.teamModel
import com.buel.holyhelpers.data.FirebaseRemoteHelper
import com.buel.holyhelpers.model.HolyModel.groupModel
import com.buel.holyhelpers.model.HolyModel.groupModel.teamModel
import com.buel.holyhelpers.utils.SharedPreferenceUtil
import com.buel.holyhelpers.utils.SortMapUtil.getInteger
import com.buel.holyhelpers.view.activity.LoadingActivity
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil.Companion.simpleDoneDialog
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.crashlytics.android.Crashlytics
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.orhanobut.logger.LoggerHelper
import io.fabric.sdk.android.Fabric
import java.util.*

class LoadingActivity : BaseActivity() {
    private var mFirebaseRemoteHelper: FirebaseRemoteHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_loading)
        //package name settings
        Common.PACKAGE_NAME = applicationContext.packageName
        setLogger()
        LoggerHelper.i("LOADINGACTIVITY START", "로팅화면을 시작합니다.")
        FirebaseApp.initializeApp(this)
        //firebase remote setting
        mFirebaseRemoteHelper = FirebaseRemoteHelper()
        mFirebaseRemoteHelper!!.setRemoteConfigFirebase(this) { firebaseRemoteConfig ->
            LoggerHelper.i("setRemoteConfigFirebase START", "리모트 컨피그를 시작합니다.")
            displayMessage(firebaseRemoteConfig)
        }
        //stage clicked event -> loginActivity
        val linearLayout_main = findViewById<RelativeLayout>(R.id.splashactivity_linearlayout)
        linearLayout_main.setOnClickListener { goLogin() }
        setPreferenced()
    }

    private fun setPreferenced() {
        LoggerHelper.i("setPreferenced start", "프리퍼런스 설정을 시작합니다.")
        SharedPreferenceUtil.init(this@LoadingActivity)
        sGroupUid = SharedPreferenceUtil.getDatea(SharedPreferenceUtil.GROUP_UID, "")
        sTeamUid = SharedPreferenceUtil.getDatea(SharedPreferenceUtil.TEAM_UID, "")
        sTeamModel = SharedPreferenceUtil.getTeamData()
        sGroupModel = SharedPreferenceUtil.getGroupData()
        LoggerHelper.d("sGroupUid: $sGroupUid")
        LoggerHelper.d("sTeamUid: $sTeamUid")
        if (sGroupModel.name != null) {
            groupModel = sGroupModel
            LoggerHelper.d("sTeamModel: " + sGroupModel.name)
        }
        if (sTeamModel.name != null) {
            teamModel = sTeamModel
            LoggerHelper.d("sTeamModel: " + sTeamModel.name)
        }
        if (sGroupUid != null) setGroupUid(sGroupUid)
        if (sTeamUid != null) setTeamUid(sTeamUid)
        LoggerHelper.i("프리퍼런스의 데이터(groupModel/teamModel)를 설정하였습니다.")
        if (selectedYear == -1) {
            val cal = Calendar.getInstance()
            selectedYear = cal[Calendar.YEAR]
            CommonData.selectedMonth = cal[Calendar.MONTH] + 1
            selectedDay = cal[Calendar.DAY_OF_MONTH]
            selectedDayOfWeek = cal[Calendar.DAY_OF_WEEK]
            selectedDays = 1
            LoggerHelper.i("날짜 데이터가 없어 현재시간으로 설정하였습니다.")
        }
    }

    /**
     * logger 세팅
     */
    private fun setLogger() {
        LoggerHelper.setLogger(Common.PACKAGE_NAME, IS_DEBUGGING)
        LoggerHelper.i("LOG SETTING START", "로그를 세팅합니다.")
    }

    private fun displayMessage(firebaseRemoteConfig: FirebaseRemoteConfig) {
        LoggerHelper.i("displayMessage")
        val isCaps = firebaseRemoteConfig.getBoolean("splash_message_caps")
        val splash_message = firebaseRemoteConfig.getString("splash_message")
        val app_notice = firebaseRemoteConfig.getString("app_notice")
        val app_upgrade = firebaseRemoteConfig.getBoolean("app_upgrade")
        val app_upgrade_url = firebaseRemoteConfig.getString("app_upgrade_url")
        val ads_video_id = firebaseRemoteConfig.getString("ads_video_id")
        val ads_banner_id = firebaseRemoteConfig.getString("ads_banner_id")
        val ads_interstitial_id = firebaseRemoteConfig.getString("ads_interstitial_id")
        val app_ads_open = firebaseRemoteConfig.getString("app_ads_open")
        LoggerHelper.e("firebaseRemoteConfig.getString(app_ver) : " + firebaseRemoteConfig.getString("app_ver"))
        val app_ver = getInteger(firebaseRemoteConfig.getString("app_ver"))
        val personal_join_point = getInteger(firebaseRemoteConfig.getString("personal_join_point"))
        val tutorial_point = getInteger(firebaseRemoteConfig.getString("tutorial_point"))
        val video_ads_point = getInteger(firebaseRemoteConfig.getString("video_ads_point"))
        val app_daum_address = firebaseRemoteConfig.getString("app_daum_address")
        daumAddUrl = app_daum_address
        try {
            CommonData.isAdsOpen= (app_ads_open)
            CommonData.videoAdsPoint = video_ads_point
            CommonData.personalJoinPoint = personal_join_point
            CommonData.tutorialPoint = tutorial_point
            CommonData.level1 = 0
            CommonData.level2 = firebaseRemoteConfig.getString("app_level2").toInt()
            CommonData.level3 = firebaseRemoteConfig.getString("app_level3").toInt()
            CommonData.level4 = firebaseRemoteConfig.getString("app_level4").toInt()
            CommonData.level5 = firebaseRemoteConfig.getString("app_level5").toInt()
        } catch (e: Exception) {
            LoggerHelper.e(e.message)
        }
        adsBannerId = ads_banner_id
        adsInterstitialId = ads_interstitial_id
        adsVideoId = ads_video_id
        var device_version = 0
        try {
            device_version = getInteger(packageManager.getPackageInfo(packageName, 0).versionName)
            LoggerHelper.d("device_version : $device_version")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LoggerHelper.i("device_version > app_ver : $device_version // $app_ver")
        if (device_version < app_ver) {
            LoggerHelper.i("displayMessage app_upgrade")
            simpleDoneDialog(
                    this@LoadingActivity,
                    "업그레이드가 필요합니다.",
                    "확인버튼을 클릭하여 업그레이드를 실행하여주세요. \n업데이트가 되지 않으면 삭제후 다시 설치해주세요.",
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) { //SuperToastUtil.toastE(LoadingActivity.this, "업그레이드가 필요합니다. : " + app_upgrade_url);
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(app_upgrade_url)
                            startActivity(intent)
                            finish()
                        }
                    })
            return
        }
        LoggerHelper.d("app_notice : $app_notice")
        if (app_notice != null) CommonData.appNotice = app_notice
        //@@@
//isCaps = false;
        if (isCaps) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(splash_message).setPositiveButton("확인") { dialogInterface, i ->
                //goLogin();
            }
            builder.create().show()
        } else {
            if (!app_upgrade) { //goLogin();
            }
        }
    }

    companion object {
        private const val IS_DEBUGGING = true
        private lateinit var sGroupUid: String
        private lateinit var sTeamUid: String
        private lateinit var sTeamModel: teamModel
        private lateinit var sGroupModel: groupModel
    }
}