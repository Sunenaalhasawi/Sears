package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.response.SignupResponse;
import com.hasawi.sears.data.repository.UserAuthenticationRepository;

import java.util.Map;

public class SignupViewModel extends ViewModel {
    UserAuthenticationRepository userAuthenticationRepository;

    public SignupViewModel() {
        this.userAuthenticationRepository = new UserAuthenticationRepository();
    }

    public MutableLiveData<Resource<SignupResponse>> userRegistration(Map<String, Object> inputParamsMap) {
        return userAuthenticationRepository.userSignup(inputParamsMap);
    }
}
