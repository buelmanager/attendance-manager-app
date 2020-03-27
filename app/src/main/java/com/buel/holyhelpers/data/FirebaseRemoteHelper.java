package com.buel.holyhelpers.data;

import android.app.Activity;
import android.content.Context;

import com.buel.holyhelpers.BuildConfig;
import com.buel.holyhelpers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.annotation.NonNull;


/**
 * Created by blue7 on 2018-05-09.
 */

public class FirebaseRemoteHelper {
    private OnCallbackEventListener onCallbackEventListener;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private Context mContext;

    public void setRemoteConfigFirebase(Context context ,
                                        OnCallbackEventListener onCompleteFunc){

        onCallbackEventListener = onCompleteFunc;

        mContext = context;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.defalut_config);
        mFirebaseRemoteConfig.fetch(3600)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            mFirebaseRemoteConfig.activateFetched();
                        onCallbackEventListener.onComplete(mFirebaseRemoteConfig);
                    }
                });


    }
    public interface OnCallbackEventListener {
        void onComplete(FirebaseRemoteConfig firebaseRemoteConfig);
    }
}
