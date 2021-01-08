package com.hasawi.sears.ui.main.view.dashboard.home;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentNotificationsBinding;
import com.hasawi.sears.ui.base.BaseFragment;

public class NotificationFragment extends BaseFragment {
    FragmentNotificationsBinding fragmentNotificationsBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_notifications;
    }

    @Override
    protected void setup() {
        fragmentNotificationsBinding = (FragmentNotificationsBinding) viewDataBinding;
    }
}
