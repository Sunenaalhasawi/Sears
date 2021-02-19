package com.hasawi.sears_outlet.ui.main.view.dashboard.product;

import android.view.View;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentSizeChartBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;

public class SizeChartFragment extends BaseFragment {
    FragmentSizeChartBinding fragmentSizeChartBinding;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_size_chart;
    }

    @Override
    protected void setup() {

        fragmentSizeChartBinding = (FragmentSizeChartBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        fragmentSizeChartBinding.imageViewSizeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }
}
