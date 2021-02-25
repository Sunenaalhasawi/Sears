package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.view.View;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentOnboardWelcomeBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;

public class WelcomeFragment extends BaseFragment {

    FragmentOnboardWelcomeBinding fragmentOnboardWelcomeBinding;
    OnBoardActivity onBoardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_onboard_welcome;
    }

    @Override
    protected void setup() {
        fragmentOnboardWelcomeBinding = (FragmentOnboardWelcomeBinding) viewDataBinding;
        onBoardActivity = (OnBoardActivity) getActivity();
        fragmentOnboardWelcomeBinding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.replaceFragment(new SelectCategoryFragment(), null);
            }
        });
        fragmentOnboardWelcomeBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.redirectToHomePage();
            }
        });
    }
}
