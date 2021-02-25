package com.hasawi.sears_outlet.ui.main.view.onboard;

import android.view.View;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentOnboardSizeBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;

public class SelectSizeFragment extends BaseFragment {
    FragmentOnboardSizeBinding fragmentOnboardSizeBinding;
    OnBoardActivity onBoardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_onboard_size;
    }

    @Override
    protected void setup() {

        fragmentOnboardSizeBinding = (FragmentOnboardSizeBinding) viewDataBinding;
        onBoardActivity = (OnBoardActivity) getActivity();
        String[] data = new String[]{"Extra Large", "Large", "Medium", "Small", "Extra Small"};
        fragmentOnboardSizeBinding.numberPicker.setMinValue(0);
        fragmentOnboardSizeBinding.numberPicker.setMaxValue(data.length - 1);
        fragmentOnboardSizeBinding.numberPicker.setDisplayedValues(data);
        fragmentOnboardSizeBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.redirectToHomePage();
            }
        });
        fragmentOnboardSizeBinding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardActivity.replaceFragment(new SelectBrandFragment(), null);
            }
        });
    }
}
