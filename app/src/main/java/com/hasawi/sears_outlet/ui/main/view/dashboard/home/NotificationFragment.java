package com.hasawi.sears_outlet.ui.main.view.dashboard.home;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentNotificationsBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;

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
