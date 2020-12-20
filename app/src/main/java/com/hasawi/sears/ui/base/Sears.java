package com.hasawi.sears.ui.base;

import android.app.Application;
import android.content.res.Resources;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hasawi.sears.data.api.RetrofitApiClient;
import com.hasawi.sears.utils.AppConstants;

public class Sears extends Application {

    private RetrofitApiClient retrofitApiClient;
    public static final String TAG = "Sears";
    private FirebaseAnalytics mFirebaseAnalytics;
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
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
//                        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                    }
                });

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

    }
}
