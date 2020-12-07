package com.hasawi.sears.ui.main.view.signin.login;

import android.view.View;

import com.hasawi.sears.R;
import com.hasawi.sears.databinding.FragmentForgotPasswordBinding;
import com.hasawi.sears.ui.base.BaseFragment;
import com.hasawi.sears.ui.main.view.signin.SigninActivity;

public class ForgotPasswordFragment extends BaseFragment {

    FragmentForgotPasswordBinding forgotPasswordBinding;
    SigninActivity signinActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    protected void setup() {
        forgotPasswordBinding = (FragmentForgotPasswordBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        forgotPasswordBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = forgotPasswordBinding.edtEmail.getText().toString();
                // Call Api
                signinActivity.replaceFragment(new VerifyOtpFragment(), null);
            }
        });
    }
}
