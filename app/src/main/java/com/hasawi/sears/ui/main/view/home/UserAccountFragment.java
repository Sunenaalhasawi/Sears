package com.hasawi.sears.ui.main.view.home;

import android.content.Intent;
import android.view.View;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentUserAccountBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;
import com.hasawi.sears.utils.PreferenceHandler;

public class UserAccountFragment extends BaseFragment implements View.OnClickListener {
    FragmentUserAccountBinding fragmentUserAccountBinding;
    DashboardActivity dashboardActivity;
    boolean isLoggedIn = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_account;
    }

    @Override
    protected void setup() {
        fragmentUserAccountBinding = (FragmentUserAccountBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        fragmentUserAccountBinding.constraintLayoutOrder.setOnClickListener(this);
        fragmentUserAccountBinding.constrainLayoutHelpCenter.setOnClickListener(this);
        fragmentUserAccountBinding.constraintLayoutReferEarn.setOnClickListener(this);
        fragmentUserAccountBinding.constrainLayoutWishlist.setOnClickListener(this);
        fragmentUserAccountBinding.btnLoginSignup.setOnClickListener(this);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
        fragmentUserAccountBinding.tvUserName.setText(username);
        isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        if (isLoggedIn) {
            fragmentUserAccountBinding.btnLoginSignup.setVisibility(View.GONE);
            fragmentUserAccountBinding.tvUserName.setVisibility(View.VISIBLE);
        } else {
            fragmentUserAccountBinding.btnLoginSignup.setVisibility(View.VISIBLE);
            fragmentUserAccountBinding.tvUserName.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.constraintLayoutOrder:
                break;
            case R.id.constrainLayoutHelpCenter:
                break;
            case R.id.constraintLayoutReferEarn:
                break;
            case R.id.constrainLayoutWishlist:
                dashboardActivity.handleActionMenuBar(true, true, "Wishlist");
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);
                break;
            case R.id.btn_loginSignup:
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
                dashboardActivity.finish();
                break;
            default:
                break;
        }
    }
}
