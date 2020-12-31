package com.hasawi.sears.ui.main.view.user_details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hasawi.sears.R;
import com.hasawi.sears.databinding.ActivityUserPreferenceBinding;
import com.hasawi.sears.ui.base.BaseActivity;
import com.hasawi.sears.ui.main.viewmodel.UserPreferenceViewModel;

public class UserPreferenceActivity extends BaseActivity {

    ActivityUserPreferenceBinding activityUserPreferenceBinding;
    UserPreferenceViewModel userPreferenceViewModel;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserPreferenceBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_preference);
        userPreferenceViewModel = new ViewModelProvider(this).get(UserPreferenceViewModel.class);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        replaceFragment(R.id.fragment_replacer_user_preference, new SelectMainCategoryFragment(), null, false, true);
    }

    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    public UserPreferenceViewModel getUserPreferenceViewModel() {
        return userPreferenceViewModel;
    }

    public void showProgressBar(boolean shouldShow) {
        if (shouldShow)
            activityUserPreferenceBinding.progressBar.setVisibility(View.VISIBLE);
        else
            activityUserPreferenceBinding.progressBar.setVisibility(View.GONE);
    }
}
