package com.buel.holyhelpers.view

import admob.AppLovinExtrasBundleBuilder
import admob.ApplovinAdapter
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.applovin.sdk.AppLovinAd
import com.applovin.sdk.AppLovinPrivacySettings
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.holyModel
import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.data.FDDatabaseHelper.getUserData
import com.buel.holyhelpers.management.PointManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil.Companion.noticeDialog
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.LoggerHelper

class CreateNavigationView : RewardedVideoAdListener {
    private lateinit var mAd: RewardedVideoAd
    private var mContext: Context? = null
    private var tvUserPoint: TextView? = null
    private var tvUserLvName: TextView? = null
    private var tvUserCPoint: TextView? = null
    private val loadedAd: AppLovinAd? = null
    fun setView(context: Context?, onCompleteListener: SimpleListener.OnCompleteListener?) {
        val view = Common.getRootView(context)
        mContext = context
        val navigationView: NavigationView = view.findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(context as NavigationView.OnNavigationItemSelectedListener?)
        val navHeader = navigationView.getHeaderView(0)
        val ivUserPhoto = navHeader.findViewById<ImageView>(R.id.nav_header_iv_user_photo)
        tvUserPoint = navHeader.findViewById(R.id.nav_header_tv_user_point)
        tvUserLvName = navHeader.findViewById(R.id.nav_header_tv_user_c_name)
        tvUserCPoint = navHeader.findViewById(R.id.nav_header_tv_user_c_point)
        val tvUserName = navHeader.findViewById<TextView>(R.id.nav_header_tv_user_name)
        val tvUserEmail = navHeader.findViewById<TextView>(R.id.nav_header_tv_user_email)
        val dominateAdsBtn = navHeader.findViewById<Button>(R.id.nav_header_btn_user_point)
        val dominateAdsHelpBtn = navHeader.findViewById<Button>(R.id.nav_header_btn_user_point_helper)
        MobileAds.initialize(mContext, CommonData.ADMOB_APP_ID)
        mAd = MobileAds.getRewardedVideoAdInstance(context)
        mAd.setRewardedVideoAdListener(this)
        AppLovinPrivacySettings.setHasUserConsent(true, context)
        AppLovinPrivacySettings.setIsAgeRestrictedUser(true, context)
        val extras = AppLovinExtrasBundleBuilder()
                .setMuteAudio(true)
                .build()
        val request = AdRequest.Builder()
                .addNetworkExtrasBundle(ApplovinAdapter::class.java, extras)
                .build()
        loadRewardVideoAd()
        val GUIDE_HELPER_POINT = " <strong> * 누적포인트는 전교인이 함께!!</strong><br><br>" +
                " * <strong>App 등급이 Level 2 가 되면 </strong><br>" +
                "<strong> 커스터마이징 앱</strong> 만들어 드립니다!!!" + "<br>" +
                "추가로 Personal 메뉴가 오픈됩니다." + "<br><br>" +
                "<strong>† 포인트 관련</strong><br> " +
                "포인트는 누적포인트에 바로 적용되고" + "<br>" +
                "추후 콘텐츠에 사용될 예정입니다." + "<br><br>" +
                "<strong>†포인트 얻는 방법 </strong>" + "<br>" +
                "<strong>개인계정가입: " + CommonData.personalJoinPoint + "P / 우리교회 후원:" + CommonData.videoAdsPoint + "P" + "</strong><br><br>" +
                "<strong>† 누적 포인트 관련</strong><br> " +
                "누적 포인트는 차감되지 않으며 <br> " +
                "포인트량에 따라 App등급이 오르고<br> " +
                "등급에 따라 프리미엄 콘텐츠가 오픈됩니다.<br> <br> " +
                "Level 1 : " + CommonData.level1 + " point 이상 ~" + "<br>" +
                "Level 2 : " + CommonData.level2 + " point 이상 ~" + "<br>" +
                "Level 3 : " + CommonData.level3 + " point 이상 ~" + "<br>" +
                "Level 4 : " + CommonData.level4 + " point 이상 ~" + "<br>" +
                "Level 5 : " + CommonData.level5 + " point 이상 ~" + "<br>"
        dominateAdsHelpBtn.setOnClickListener { view1: View? ->
            noticeDialog(
                    context!!,
                    GUIDE_HELPER_POINT,
                    CommonString.INFO_LEVEL_HELPER_TITLE,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            CommonData.isFstEnter = false
                            LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.isFstEnter)
                        }
                    })
        }
        dominateAdsBtn.setOnClickListener { steartRewardVideoAd() }
        val firebaseAuth = FirebaseAuth.getInstance()
        val userEmail = firebaseAuth.currentUser!!.email
        LoggerHelper.d("CommonData.getHolyModel() : $holyModel")
        val userName = firebaseAuth.currentUser!!.displayName
        //Toast.makeText(context, "firebaseAuth.getCurrentUser(). : " + firebaseAuth.getCurrentUser().getPhoneNumber() , Toast.LENGTH_SHORT).show();
        val userPhotoUri = FirebaseAuth.getInstance().currentUser!!.photoUrl
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        setPointTxt()
        tvUserName.text = userName
        tvUserEmail.text = userEmail
        getUserData(uid, DataTypeListener.OnCompleteListener { (_, userPhotoUri1) ->
            try {
                try {
                    Glide.with(context!!)
                            .load(userPhotoUri1)
                            .apply(RequestOptions().circleCrop())
                            .into(ivUserPhoto)
                    onCompleteListener?.onComplete()
                } catch (e: Exception) {
                    LoggerHelper.e(e.message)
                }
            } catch (e: Exception) {
                LoggerHelper.e(e.message)
            }
        })
        onCompleteListener?.onComplete()
    }

    private fun setPointTxt() {
        LoggerHelper.d("setPointTxt")
        var lvName = ""
        var corpsPoint = 0
        var corpsCPoint = 0
        if (holyModel != null) {
            corpsPoint = holyModel.point
            corpsCPoint = holyModel.cumulativePoint
        }
        if (corpsCPoint >= CommonData.level1) {
            lvName = "Level 1"
        }
        if (corpsCPoint > CommonData.level2) {
            lvName = "Level 2"
        }
        if (corpsCPoint > CommonData.level3) {
            lvName = "Level 3"
        }
        if (corpsCPoint > CommonData.level4) {
            lvName = "Level 4"
        }
        if (corpsCPoint > CommonData.level5) {
            lvName = "Level 5"
        }
        tvUserPoint!!.text = "사용가능 포인트 : $corpsPoint"
        tvUserCPoint!!.text = "누적 포인트 : $corpsCPoint"
        tvUserLvName!!.text = "등급 : $lvName"
    }

    private fun loadRewardVideoAd() {
        LoggerHelper.d("mAd.loadAd")
        mAd!!.loadAd(CommonData.adsVideoId,
                AdRequest.Builder()
                        .build())
    }

    private fun steartRewardVideoAd() {
        LoggerHelper.d("mAd.isLoaded() :" + mAd!!.isLoaded)
        if (mAd!!.isLoaded) {
            LoggerHelper.d("mAd.start")
            mAd!!.show()
        }
    }

    override fun onRewarded(reward: RewardItem) {
        if (holyModel != null) {
            LoggerHelper.d("Plus point")
            PointManager.setPlusPoint(CommonData.videoAdsPoint) {
                Toast.makeText(mContext, "우리교회 후원활동으로 " + CommonData.videoAdsPoint + " 포인트가 적립됩니다.",
                        Toast.LENGTH_SHORT).show()
                /*Toast.makeText(mContext, "포인트를 최신화 합니다.",
                        Toast.LENGTH_SHORT).show();*/setPointTxt()
            }
        }
    }

    override fun onRewardedVideoAdLeftApplication() { //Toast.makeText(mContext, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    override fun onRewardedVideoAdClosed() {
        loadRewardVideoAd()
        //Toast.makeText(mContext, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    override fun onRewardedVideoAdFailedToLoad(errorCode: Int) { //Toast.makeText(mContext, "onRewardedVideoAdFailedToLoad " + errorCode, Toast.LENGTH_SHORT).show();
    }

    override fun onRewardedVideoAdLoaded() { //Toast.makeText(mContext, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    override fun onRewardedVideoAdOpened() { //Toast.makeText(mContext, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    override fun onRewardedVideoStarted() { //Toast.makeText(mContext, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    override fun onRewardedVideoCompleted() { //Toast.makeText(mContext, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }
}