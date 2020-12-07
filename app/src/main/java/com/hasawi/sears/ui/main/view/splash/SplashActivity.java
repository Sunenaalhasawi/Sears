package com.hasawi.sears.ui.main.view.splash;

import android.content.Intent;
import android.os.Bundle;

import com.hasawi.sears.R;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.main.view.signin.user_details.SelectUserDetailsActivity;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends BaseActivity {
    boolean isLoggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        isLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
//                    if (isLoggedin) {
//                        Intent intent = new Intent(SplashActivity.this, SelectUserDetailsActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
//                        startActivity(intent);
//                    }

                    //Let's Finish Splash Activity since we don't want to show this when user press back button.
                    Intent intent = new Intent(SplashActivity.this, SelectUserDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.schedule(runnable, 2000, TimeUnit.MILLISECONDS);


    }
}