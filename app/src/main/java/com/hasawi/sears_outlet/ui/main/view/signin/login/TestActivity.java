package com.hasawi.sears_outlet.ui.main.view.signin.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentLoginBinding;
import com.hasawi.sears_outlet.ui.base.BaseActivity;

public class TestActivity extends BaseActivity {

    FragmentLoginBinding fragmentForgotPasswordBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentForgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.fragment_login);
    }
}
