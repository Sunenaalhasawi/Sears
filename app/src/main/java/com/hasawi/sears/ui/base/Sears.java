package com.hasawi.sears.ui.base;

import android.app.Application;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

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

        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);
        String deviceName = Settings.Global.getString(getContentResolver(), "device_name");
        String os = Build.VERSION.RELEASE;
        String AppLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        mFirebaseAnalytics.setUserProperty("device", deviceName);
        mFirebaseAnalytics.setUserProperty("os", os);
        mFirebaseAnalytics.setUserProperty("language", AppLang);
    }
}
