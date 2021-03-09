package com.hasawi.sears_outlet.ui.main.view.dashboard.user_settings;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentUserSettingsBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.DashboardActivity;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.AboutUsFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.ContactUsFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.FAQFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.navigation_drawer_menu.PrivatePolicyFragment;
import com.hasawi.sears_outlet.ui.main.view.dashboard.user_account.UserAccountFragment;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;
import com.hasawi.sears_outlet.utils.AppConstants;
import com.hasawi.sears_outlet.utils.PreferenceHandler;

import zendesk.messaging.android.Messaging;

public class UserSettingsFragment extends BaseFragment implements View.OnClickListener {
    FragmentUserSettingsBinding fragmentUserSettingsBinding;
    DashboardActivity dashboardActivity;
    boolean isLoggedIn = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_settings;
    }

    @Override
    protected void setup() {
        fragmentUserSettingsBinding = (FragmentUserSettingsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
        fragmentUserSettingsBinding.tvUserName.setText(username);
        isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        if (isLoggedIn) {
            fragmentUserSettingsBinding.btnLoginSignup.setVisibility(View.GONE);
            fragmentUserSettingsBinding.tvUserName.setVisibility(View.VISIBLE);
        } else {
            fragmentUserSettingsBinding.btnLoginSignup.setVisibility(View.VISIBLE);
            fragmentUserSettingsBinding.tvUserName.setVisibility(View.GONE);
        }

        fragmentUserSettingsBinding.btnLoginSignup.setOnClickListener(this);
        fragmentUserSettingsBinding.constraintLayoutNotification.setOnClickListener(this);
        fragmentUserSettingsBinding.constraintLayoutMyAccount.setOnClickListener(this);
        fragmentUserSettingsBinding.constrainLayoutFaq.setOnClickListener(this);
        fragmentUserSettingsBinding.constraintLayoutAboutUs.setOnClickListener(this);
        fragmentUserSettingsBinding.constrainLayoutPrivatePolicy.setOnClickListener(this);
        fragmentUserSettingsBinding.constrainLayoutContactUs.setOnClickListener(this);
        fragmentUserSettingsBinding.lvSignout.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewFacebook.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewYoutube.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewInstagram.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewSnapChat.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewTiktok.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewCustomerSupport.setOnClickListener(this);

        boolean isNotificationEnabled = preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false);
        if (isNotificationEnabled)
            fragmentUserSettingsBinding.swOnOff.setChecked(true);
        else
            fragmentUserSettingsBinding.swOnOff.setChecked(false);
        fragmentUserSettingsBinding.swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isNotificationEnabled) {
                    preferenceHandler.saveData(PreferenceHandler.NOTIFICATION_STATUS, true);
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);
                } else {
                    preferenceHandler.saveData(PreferenceHandler.NOTIFICATION_STATUS, false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(AppConstants.APP_NAME);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.handleActionMenuBar(true, true, "User Settings");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loginSignup:
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
                dashboardActivity.finish();
                break;
            case R.id.constraintLayoutNotification:
                break;
            case R.id.constraintLayoutMyAccount:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new UserAccountFragment(), null, true, false);
                break;
            case R.id.constrainLayoutFaq:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new FAQFragment(), null, true, false);
                dashboardActivity.hideToolBar();
                break;
            case R.id.constraintLayoutAboutUs:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new AboutUsFragment(), null, true, false);
                dashboardActivity.hideToolBar();
                break;
            case R.id.constrainLayoutPrivatePolicy:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new PrivatePolicyFragment(), null, true, false);
                dashboardActivity.hideToolBar();
                break;
            case R.id.constrainLayoutContactUs:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ContactUsFragment(), null, true, false);
                break;
            case R.id.lv_signout:
                if (dashboardActivity.isAlreadyLoggedinWithFacebbok())
                    dashboardActivity.disconnectFromFacebook();
                dashboardActivity.clearPreferences();
                Intent signoutIntent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(signoutIntent);
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
            case R.id.imageViewCustomerSupport:
                if (dashboardActivity.zendeskMessagingEnabled)
                    Messaging.instance().showMessaging(dashboardActivity);
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

