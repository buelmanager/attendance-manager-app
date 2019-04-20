package com.buel.holyhelper.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinPrivacySettings;
import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.management.PointManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.LoggerHelper;

import admob.AppLovinExtrasBundleBuilder;
import admob.ApplovinAdapter;

public class CreateNavigationView implements RewardedVideoAdListener {
    private RewardedVideoAd mAd;
    private Context mContext;

    private TextView tvUserPoint;
    private TextView tvUserLvName;
    private TextView tvUserCPoint;

    private AppLovinAd loadedAd;

    public void setView(Context context, SimpleListener.OnCompleteListener onCompleteListener) {
        View view = Common.getRootView(context);
        mContext = context;
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) context);

        View navHeader = navigationView.getHeaderView(0);

        final ImageView ivUserPhoto = navHeader.findViewById(R.id.nav_header_iv_user_photo);

        tvUserPoint = navHeader.findViewById(R.id.nav_header_tv_user_point);
        tvUserLvName = navHeader.findViewById(R.id.nav_header_tv_user_c_name);
        tvUserCPoint = navHeader.findViewById(R.id.nav_header_tv_user_c_point);

        TextView tvUserName = navHeader.findViewById(R.id.nav_header_tv_user_name);
        TextView tvUserEmail = navHeader.findViewById(R.id.nav_header_tv_user_email);
        Button dominateAdsBtn = navHeader.findViewById(R.id.nav_header_btn_user_point);
        Button dominateAdsHelpBtn = navHeader.findViewById(R.id.nav_header_btn_user_point_helper);

        MobileAds.initialize(mContext, CommonData.ADMOB_APP_ID);

        mAd = MobileAds.getRewardedVideoAdInstance(context);
        mAd.setRewardedVideoAdListener(this);

        AppLovinPrivacySettings.setHasUserConsent(true, context);
        AppLovinPrivacySettings.setIsAgeRestrictedUser(true, context);

        Bundle extras = new AppLovinExtrasBundleBuilder()
                .setMuteAudio(true)
                .build();
        AdRequest request = new AdRequest.Builder()
                .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                .build();

        loadRewardVideoAd();

        String GUIDE_HELPER_POINT =
                " <strong> * 누적포인트는 전교인이 함께!!</strong><br><br>" +
                        " * <strong>App 등급이 Level 2 가 되면 </strong><br>" +
                        "<strong> 커스터마이징 앱</strong> 만들어 드립니다!!!" + "<br>" +
                        "추가로 Personal 메뉴가 오픈됩니다." + "<br><br>" +

                        "<strong>† 포인트 관련</strong><br> " +
                        "포인트는 누적포인트에 바로 적용되고" + "<br>" +
                        "추후 콘텐츠에 사용될 예정입니다." + "<br><br>" +

                        "<strong>†포인트 얻는 방법 </strong>" + "<br>" +
                        "<strong>개인계정가입: " + CommonData.getPersonalJoinPoint() + "P / 우리교회 후원:" + CommonData.getVideoAdsPoint() + "P" + "</strong><br><br>" +

                        "<strong>† 누적 포인트 관련</strong><br> " +
                        "누적 포인트는 차감되지 않으며 <br> " +
                        "포인트량에 따라 App등급이 오르고<br> " +
                        "등급에 따라 프리미엄 콘텐츠가 오픈됩니다.<br> <br> " +
                        "Level 1 : " + CommonData.getLevel1() + " point 이상 ~" + "<br>" +
                        "Level 2 : " + CommonData.getLevel2() + " point 이상 ~" + "<br>" +
                        "Level 3 : " + CommonData.getLevel3() + " point 이상 ~" + "<br>" +
                        "Level 4 : " + CommonData.getLevel4() + " point 이상 ~" + "<br>" +
                        "Level 5 : " + CommonData.getLevel5() + " point 이상 ~" + "<br>";

        dominateAdsHelpBtn.setOnClickListener(view1 -> MaterialDailogUtil.Companion.noticeDialog(
                context,
                GUIDE_HELPER_POINT,
                CommonString.INFO_LEVEL_HELPER_TITLE,
                s -> {
                    CommonData.setIsFstEnter(false);
                    LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.getIsFstEnter());
                }));
        dominateAdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                steartRewardVideoAd();
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        LoggerHelper.d("CommonData.getHolyModel() : " + CommonData.getHolyModel());
        String userName = firebaseAuth.getCurrentUser().getDisplayName();

        //Toast.makeText(context, "firebaseAuth.getCurrentUser(). : " + firebaseAuth.getCurrentUser().getPhoneNumber() , Toast.LENGTH_SHORT).show();

        Uri userPhotoUri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setPointTxt();
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);

        FDDatabaseHelper.INSTANCE.getUserData(uid, userModel -> {
            try {
                try {
                    Glide.with(context)
                            .load(userModel.userPhotoUri)
                            .apply(new RequestOptions().circleCrop())
                            .into(ivUserPhoto);

                    if (onCompleteListener != null) onCompleteListener.onComplete();
                } catch (Exception e) {
                    LoggerHelper.e(e.getMessage());
                }
            } catch (Exception e) {
                LoggerHelper.e(e.getMessage());
            }
        });
        if (onCompleteListener != null) onCompleteListener.onComplete();
    }

    private void setPointTxt() {
        LoggerHelper.d("setPointTxt");
        String lvName = "";
        int corpsPoint = 0;
        int corpsCPoint = 0;

        if (CommonData.getHolyModel() != null) {
            corpsPoint = CommonData.getHolyModel().point;
            corpsCPoint = CommonData.getHolyModel().cumulativePoint;
        }

        if (corpsCPoint >= CommonData.getLevel1()) {
            lvName = "Level 1";
        }

        if (corpsCPoint > CommonData.getLevel2()) {
            lvName = "Level 2";
        }

        if (corpsCPoint > CommonData.getLevel3()) {
            lvName = "Level 3";
        }

        if (corpsCPoint > CommonData.getLevel4()) {
            lvName = "Level 4";
        }

        if (corpsCPoint > CommonData.getLevel5()) {
            lvName = "Level 5";
        }

        tvUserPoint.setText("사용가능 포인트 : " + corpsPoint);
        tvUserCPoint.setText("누적 포인트 : " + corpsCPoint);
        tvUserLvName.setText("등급 : " + lvName);
    }

    private void loadRewardVideoAd() {
        LoggerHelper.d("mAd.loadAd");
        mAd.loadAd(CommonData.getAdsVideoId(),
                new AdRequest.Builder()
                        .build());
    }

    private void steartRewardVideoAd() {
        LoggerHelper.d("mAd.isLoaded() :" + mAd.isLoaded());
        if (mAd.isLoaded()) {
            LoggerHelper.d("mAd.start");
            mAd.show();
        }
    }

    @Override
    public void onRewarded(RewardItem reward) {
        if (CommonData.getHolyModel() != null) {
            LoggerHelper.d("Plus point");
            PointManager.setPlusPoint(CommonData.getVideoAdsPoint(), () -> {
                Toast.makeText(mContext, "우리교회 후원활동으로 " + CommonData.getVideoAdsPoint() + " 포인트가 적립됩니다.",
                        Toast.LENGTH_SHORT).show();
                /*Toast.makeText(mContext, "포인트를 최신화 합니다.",
                        Toast.LENGTH_SHORT).show();*/
                setPointTxt();
            });
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(mContext, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardVideoAd();
        //Toast.makeText(mContext, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        //Toast.makeText(mContext, "onRewardedVideoAdFailedToLoad " + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(mContext, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(mContext, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(mContext, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        //Toast.makeText(mContext, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }
}