package com.hasawi.sears.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hasawi.sears.data.api.RetrofitApiClient;

public class Sears extends Application {

    private RetrofitApiClient retrofitApiClient;

    public RetrofitApiClient getRetrofitApiClient() {
        retrofitApiClient = RetrofitApiClient.getInstance();
        return retrofitApiClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
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
//                        Log.d(TAG, msg);
//                        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
