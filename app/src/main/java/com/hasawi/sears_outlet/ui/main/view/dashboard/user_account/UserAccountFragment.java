package com.hasawi.sears_outlet.ui.main.view.dashboard.user_account;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentUserAccountBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.address_book.AddressBookFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.order_history.OrderHistoryFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.profile.UserProfileFragment;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;
import com.hasawi.sears_outlet.utils.AppConstants;
import com.hasawi.sears_outlet.utils.dialogs.LoginSignupDialog;

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
        dashboardActivity.handleActionMenuBar(true, true, "My Account");
        dashboardActivity.handleActionBarIcons(false);
        fragmentUserAccountBinding.constraintLayoutProfile.setOnClickListener(this);
        fragmentUserAccountBinding.constraintLayoutPreference.setOnClickListener(this);
        fragmentUserAccountBinding.constraintLayoutOrder.setOnClickListener(this);
        fragmentUserAccountBinding.constrainLayoutHelpCenter.setOnClickListener(this);
        fragmentUserAccountBinding.constraintLayoutReferEarn.setOnClickListener(this);
        fragmentUserAccountBinding.constrainLayoutWishlist.setOnClickListener(this);
        fragmentUserAccountBinding.constrainLayoutAddressBook.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewFacebook.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewInstagram.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewSnapChat.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewTiktok.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewYoutube.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.constraintLayoutProfile:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, true, "My Profile");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new UserProfileFragment(), null, true, false);
                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.constraintLayoutPreference:
                dashboardActivity.handleActionMenuBar(true, false, "My Preference");
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyPreferenceFragment(), null, true, false);
                break;
            case R.id.constraintLayoutOrder:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, true, "My Orders");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderHistoryFragment(), null, true, false);

                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.constrainLayoutHelpCenter:
                break;
            case R.id.constraintLayoutReferEarn:
                break;
            case R.id.constrainLayoutWishlist:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, true, "Wishlist");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);

                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.constrainLayoutAddressBook:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, false, "My Addresses");
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddressBookFragment(), null, true, false);

                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.btn_loginSignup:
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
                dashboardActivity.finish();
                break;
            case R.id.imageViewFacebook:
                launchBrowser(AppConstants.SEARS_FACEBOOK);
                break;
            case R.id.imageViewInstagram:
                launchBrowser(AppConstants.SEARS_INSTAGRAM);
                break;
            case R.id.imageViewYoutube:
                launchBrowser(AppConstants.SEARS_YOUTUBE);
                break;
            case R.id.imageViewSnapChat:
                launchBrowser(AppConstants.SEARS_SNAPCHAT);
                break;
            case R.id.imageViewTiktok:
                launchBrowser(AppConstants.SEARS_TIKTOK);
                break;
            default:
                break;
        }
    }

    private void launchBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
