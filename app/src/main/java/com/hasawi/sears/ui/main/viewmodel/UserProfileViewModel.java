package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.model.User;
import com.hasawi.sears.data.api.response.ChangePasswordResponse;
import com.hasawi.sears.data.api.response.UserProfileResponse;
import com.hasawi.sears.data.repository.UserAccountRepository;
import com.hasawi.sears.data.repository.UserAuthenticationRepository;

import java.util.Map;

public class UserProfileViewModel extends ViewModel {

    SavedStateHandle savedStateHandle;
    String userID;
    MutableLiveData<User> user;
    UserAccountRepository userAccountRepository;
    UserAuthenticationRepository userAuthenticationRepository;

    public UserProfileViewModel() {
        userAccountRepository = new UserAccountRepository();
        userAuthenticationRepository = new UserAuthenticationRepository();
    }

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<User>();
        }
        return user;
    }

    public MutableLiveData<Resource<UserProfileResponse>> userProfile(String emailId, String sessionToken) {
        return userAccountRepository.userProfile(emailId, sessionToken);
    }

    public MutableLiveData<Resource<ChangePasswordResponse>> changePassword(String customerId, String oldPassword, String newPassword, String sessiontoken) {
        return userAuthenticationRepository.changePaaword(customerId, oldPassword, newPassword, sessiontoken);
    }

    public MutableLiveData<Resource<UserProfileResponse>> editUserProfile(String customerId, String sessiontoken, Map<String, Object> inputParms) {
        return userAccountRepository.editUserProfile(customerId, sessiontoken, inputParms);
    }
}
