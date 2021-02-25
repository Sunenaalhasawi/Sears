package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.os.Handler;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentOnboardEndBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;

public class EndFragment extends BaseFragment {

    FragmentOnboardEndBinding fragmentOnboardEndBinding;
    private OnBoardActivity onBoardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_onboard_end;
    }

    @Override
    protected void setup() {
        fragmentOnboardEndBinding = (FragmentOnboardEndBinding) viewDataBinding;
        onBoardActivity = (OnBoardActivity) getActivity();
        final int intervalTime = 5000; // 10 sec
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardActivity.redirectToHomePage();
            }
        }, intervalTime);
    }
}
