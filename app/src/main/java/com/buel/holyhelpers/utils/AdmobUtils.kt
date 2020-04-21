package com.buel.holyhelpers.utils

import android.content.Context
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.currentPremiumType
import com.buel.holyhelpers.data.PremiupType
import com.buel.holyhelpers.view.SimpleListener
import com.commonLib.Common
import com.google.android.gms.ads.*
import com.orhanobut.logger.LoggerHelper

object AdmobUtils {
    private var mInterstitialAd: InterstitialAd? = null
    fun setBottomBannerAds(context: Context?) { //하단 배너
        MobileAds.initialize(context, CommonData.adsBannerId)
        val rootView = Common.getRootView(context)
        val mAdView: AdView
        mAdView = rootView.findViewById(R.id.adViewBottom)
        //mAdView.setAdSize(AdSize.SEARCH);
        val adRequest = AdRequest.Builder().build()
        if (CommonData.isAdsOpen.equals("true")) {
            if (currentPremiumType == PremiupType.NORAML) mAdView.loadAd(adRequest)
        }
    }

    fun setInterstitialAds(context: Context?, onCompleteListener: SimpleListener.OnCompleteListener?) {
        if (mInterstitialAd != null) return
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd!!.adUnitId = CommonData.adsInterstitialId
        mInterstitialAd!!.adListener = object : AdListener() {
            override fun onAdLoaded() { // Code to be executed when an ad finishes loading.
//Toast.makeText(mContext, "onAdLoaded", Toast.LENGTH_SHORT).show();
                startRewardInterstitialAd()
            }

            override fun onAdFailedToLoad(errorCode: Int) { // Code to be executed when an ad request fails.
//Toast.makeText(mContext, "onAdFailedToLoad : " + errorCode, Toast.LENGTH_SHORT).show();
            }

            override fun onAdOpened() { // Code to be executed when the ad is displayed.
//Toast.makeText(mContext, "onAdOpened", Toast.LENGTH_SHORT).show();
            }

            override fun onAdLeftApplication() { // Code to be executed when the user has left the app.
//Toast.makeText(mContext, "onAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            override fun onAdClosed() { // Code to be executed when when the interstitial ad is closed.
//Toast.makeText(mContext, "onAdClosed", Toast.LENGTH_SHORT).show();
                onCompleteListener?.onComplete()
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
        }
    }

    fun startRewardInterstitialAd() {
        if (mInterstitialAd!!.isLoaded) {
            mInterstitialAd!!.show()
        } else {
            LoggerHelper.d("mInterstitialAd", "The interstitial wasn't loaded yet.")
        }
    }

    fun loadRewardInterstitialAd() {
        if (CommonData.isAdsOpen.equals("true")) {
            if (currentPremiumType == PremiupType.NORAML) if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.loadAd(AdRequest.Builder().build())
        }
    }
}