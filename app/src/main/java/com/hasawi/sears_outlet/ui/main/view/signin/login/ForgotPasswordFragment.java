package com.hasawi.sears_outlet.ui.main.view.signin.login;

import android.view.View;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentForgotPasswordBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;

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
