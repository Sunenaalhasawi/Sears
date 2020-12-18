package com.hasawi.sears.ui.main.view.navigation_drawer_menu.profile;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentChangePasswordBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.DashboardActivity;
import com.hasawi.sears.ui.main.viewmodel.UserProfileViewModel;
import com.hasawi.sears.utils.PreferenceHandler;

public class ChangePasswordFragment extends BaseFragment {
    FragmentChangePasswordBinding fragmentChangePasswordBinding;
    UserProfileViewModel userProfileViewModel;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected void setup() {
        fragmentChangePasswordBinding = (FragmentChangePasswordBinding) viewDataBinding;
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.setTitle("Change Password");
        fragmentChangePasswordBinding.btnUpdatePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPswd = fragmentChangePasswordBinding.edtOldPswd.getText().toString();
                String newPswd = fragmentChangePasswordBinding.edtNewPswd.getText().toString();
                String reenteredNewPswd = fragmentChangePasswordBinding.edtReenterNewPswd.getText().toString();

                if (newPswd.equals(reenteredNewPswd)) {
                    callUpdatePasswordApi(oldPswd, newPswd);
                } else {
                    Toast.makeText(getActivity(), "Password did not match", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    private void callUpdatePasswordApi(String oldPswd, String newPswd) {

        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessiontoken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        fragmentChangePasswordBinding.progressBar.setVisibility(View.VISIBLE);
        userProfileViewModel.changePassword(userId, oldPswd, newPswd, sessiontoken).observe(getActivity(), changePasswordResponseResource -> {
            switch (changePasswordResponseResource.status) {
                case SUCCESS:
                    Toast.makeText(dashboardActivity, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStackImmediate();
                    break;
                case LOADING:
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), changePasswordResponseResource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
            fragmentChangePasswordBinding.progressBar.setVisibility(View.GONE);
        });
    }
}
