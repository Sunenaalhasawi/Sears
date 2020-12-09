package com.hasawi.sears.ui.main.view.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hasawi.sears.R;
import com.hasawi.sears.databinding.ActivitySplashBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.main.view.signin.user_details.SelectUserDetailsActivity;
import com.hasawi.sears.utils.PreferenceHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends BaseActivity {
    boolean isLoggedin;
    ActivitySplashBinding activitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        isLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        Glide.with(this)
                .asGif()
                .load(R.raw.splash_gif)
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        return false;
                    }
                })
                .into(activitySplashBinding.imageView8);
//        Glide.with(this)
//                .asGif()
//                .load(getResources().getDrawable(R.drawable.splash_gif))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .addListener(new RequestListener<GifDrawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
//                        resource.setLoopCount(1);
//                        return false;
//                    }
//                })
//                .into(activitySplashBinding.imageView8);


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
        worker.schedule(runnable, 1000 * 7, TimeUnit.MILLISECONDS);


    }
}