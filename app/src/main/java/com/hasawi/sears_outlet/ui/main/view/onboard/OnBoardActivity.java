package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.ActivityOnBoardBinding;
import com.hasawi.sears_outlet.ui.base.BaseActivity;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;

public class OnBoardActivity extends BaseActivity {

    ActivityOnBoardBinding activityOnBoardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOnBoardBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_board);
        replaceFragment(new WelcomeFragment(), null);

    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
        replaceFragment(R.id.fragment_replacer_onboard, fragment, bundle, true, false);
    }

    public void showProgressBar(boolean shouldShow) {
        if (shouldShow)
            activityOnBoardBinding.progressBar.setVisibility(View.VISIBLE);
        else
            activityOnBoardBinding.progressBar.setVisibility(View.GONE);
    }

    public void redirectToHomePage() {
        Intent intent = new Intent(OnBoardActivity.this, DashboardActivity.class);
        startActivity(intent);
        this.finish();
    }

}