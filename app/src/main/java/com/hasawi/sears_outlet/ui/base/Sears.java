package com.hasawi.sears_outlet.ui.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hasawi.sears_outlet.BuildConfig;
import com.hasawi.sears_outlet.data.api.RetrofitApiClient;
import com.hasawi.sears_outlet.utils.AppConstants;
import com.hasawi.sears_outlet.utils.DateTimeUtils;

public class Sears extends Application implements LifecycleObserver {

    private RetrofitApiClient retrofitApiClient;
    public static final String TAG = "Sears";
    private FirebaseAnalytics mFirebaseAnalytics;
    private AppEventsLogger facebookEventLogger;
    InstallReferrerClient referrerClient;


    public RetrofitApiClient getRetrofitApiClient() {
        retrofitApiClient = RetrofitApiClient.getInstance();
        return retrofitApiClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        facebookEventLogger = AppEventsLogger.newLogger(this);
        AppEventsLogger.activateApp(this);
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
////                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        Toast.makeText(Sears.this, token, Toast.LENGTH_LONG).show();
//                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, token);
//                    }
//                });

        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

        ReferrerDetails response = null;
        try {
            response = referrerClient.getInstallReferrer();
            String referrerUrl = response.getInstallReferrer();
            long referrerClickTime = response.getReferrerClickTimestampSeconds();
            long appInstallTime = response.getInstallBeginTimestampSeconds();
            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);
        String deviceName = Settings.Global.getString(getContentResolver(), "device_name");
        String os = Build.VERSION.RELEASE;
        String AppLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        mFirebaseAnalytics.setUserProperty("device", deviceName);
        mFirebaseAnalytics.setUserProperty("os", os);
        mFirebaseAnalytics.setUserProperty("language", AppLang);
        appGetFirstTimeRun();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onAppBackgrounded() {
        Log.d("SEARS", "App in background");
        logLastSessionEvent();
    }

    private void logLastSessionEvent() {
        Bundle bundle = new Bundle();
        bundle.putString("time_stamp", DateTimeUtils.getCurrentStringDateTime());
        mFirebaseAnalytics.logEvent("LAST_SESSION", bundle);
        facebookEventLogger.logEvent("LAST_SESSION", bundle);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onAppForegrounded() {
        Log.d("SEARS", "App in foreground");
    }

    private int appGetFirstTimeRun() {
        //Check if App Start First Time
        SharedPreferences appPreferences = getSharedPreferences("SEARS", 0);
        int appCurrentBuildVersion = BuildConfig.VERSION_CODE;
        int appLastBuildVersion = appPreferences.getInt("app_first_time", 0);

        //Log.d("appPreferences", "app_first_time = " + appLastBuildVersion);

        if (appLastBuildVersion == appCurrentBuildVersion) {
            // Logging facebook event first_open
            logFirstAppOpenEvent();
            return 1; // first time
        } else {
            appPreferences.edit().putInt("app_first_time",
                    appCurrentBuildVersion).apply();
            if (appLastBuildVersion == 0) {
                return 0; //already started before
            } else {
                return 2; //It has started once, but not that version , ie it is an update.
            }
        }
    }

    private void logFirstAppOpenEvent() {
        Bundle params = new Bundle();
        try {
            params.putString("date", DateTimeUtils.getCurrentStringDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        facebookEventLogger.logEvent("APP_FIRST_OPEN", params);
    }

}
