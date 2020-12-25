package com.hasawi.sears.ui.main.view.splash;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hasawi.sears.R;
import com.hasawi.sears.databinding.ActivitySplashBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.home.user_details.UserPreferenceActivity;
import com.hasawi.sears.utils.PreferenceHandler;

public class SplashActivity extends BaseActivity {
    boolean isLoggedin;
    ActivitySplashBinding activitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        isLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        Glide.with(this)
                .asGif()
                .load(R.raw.sears_splash_gif)
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                //do whatever after specified number of loops complete
                                redirectToHome();
                            }
                        });
                        return false;
                    }
                })
                .into(activitySplashBinding.imageView8);

    }

    private void redirectToHome() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        boolean isCategoryAlreadyShown = preferenceHandler.getData(PreferenceHandler.HAS_CATEGORY_PAGE_SHOWN, false);
        if (isCategoryAlreadyShown) {
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, UserPreferenceActivity.class);
            startActivity(intent);
            finish();
        }

    }
}