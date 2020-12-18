package com.hasawi.sears.ui.main.view.navigation_drawer_menu.order_history;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentOrderDetailsBinding;
import com.hasawi.sears.ui.base.BaseFragment;

public class OrderHistoryDetailFragment extends BaseFragment {
    FragmentOrderDetailsBinding fragmentOrderDetailsBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_details;
    }

    @Override
    protected void setup() {
        fragmentOrderDetailsBinding = (FragmentOrderDetailsBinding) viewDataBinding;
    }
}
