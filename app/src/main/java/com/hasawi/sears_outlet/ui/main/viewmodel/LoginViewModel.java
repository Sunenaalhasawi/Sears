package com.hasawi.sears_outlet.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears_outlet.data.api.Resource;
import com.hasawi.sears_outlet.data.api.response.LoginResponse;
import com.hasawi.sears_outlet.data.api.response.SignupResponse;
import com.hasawi.sears_outlet.data.repository.UserAuthenticationRepository;

import java.util.Map;

public class LoginViewModel extends ViewModel {
    UserAuthenticationRepository userAuthenticationRepository;

    public LoginViewModel() {
        this.userAuthenticationRepository = new UserAuthenticationRepository();
    }

    public MutableLiveData<Resource<LoginResponse>> userLogin(Map<String, Object> inputParamsMap) {
        return userAuthenticationRepository.userLogin(inputParamsMap);
    }

    public MutableLiveData<Resource<SignupResponse>> userRegistration(Map<String, Object> inputParamsMap) {
        return userAuthenticationRepository.userSignup(inputParamsMap);
    }
}