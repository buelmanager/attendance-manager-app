package com.buel.holyhelper.utils;

import android.content.Context;
import android.view.View;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.PremiupType;
import com.buel.holyhelper.view.SimpleListener;
import com.commonLib.Common;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.orhanobut.logger.LoggerHelper;

public class AdmobUtils {

    private static InterstitialAd mInterstitialAd;

    public static void setBottomBannerAds(Context context) {
        //하단 배너
        MobileAds.initialize(context, CommonData.getAdsBannerId());
        View rootView = Common.getRootView(context);

        AdView mAdView;
        mAdView = rootView.findViewById(R.id.adViewBottom);
        //mAdView.setAdSize(AdSize.SEARCH);

        AdRequest adRequest = new AdRequest.Builder().build();
        if (CommonData.getIsAdsOpen().equals("true")) {
            if (CommonData.getCurrentPremiumType() == PremiupType.NORAML)
                mAdView.loadAd(adRequest);
        }
    }

    public static void setInterstitialAds(Context context, SimpleListener.OnCompleteListener onCompleteListener) {

        if (mInterstitialAd != null) return;
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(CommonData.getAdsInterstitialId());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(mContext, "onAdLoaded", Toast.LENGTH_SHORT).show();
                startRewardInterstitialAd();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //Toast.makeText(mContext, "onAdFailedToLoad : " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                //Toast.makeText(mContext, "onAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                //Toast.makeText(mContext, "onAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                //Toast.makeText(mContext, "onAdClosed", Toast.LENGTH_SHORT).show();

                if (onCompleteListener != null) onCompleteListener.onComplete();

                /*if (CommonData.getHolyModel() != null) {
                    LoggerHelper.d("Plus point");
                    PointManager.setPlusPoint(10, new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            if(onCompleteListener != null) onCompleteListener.onComplete();
                        }
                    });
                }*/
            }
        });
    }

    public static void startRewardInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            LoggerHelper.d("mInterstitialAd", "The interstitial wasn't loaded yet.");
        }
    }

    public static void loadRewardInterstitialAd() {
        if (CommonData.getIsAdsOpen().equals("true")) {
            if (CommonData.getCurrentPremiumType() == PremiupType.NORAML)
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }
}
