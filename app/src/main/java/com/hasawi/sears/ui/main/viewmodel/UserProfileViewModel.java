package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.Resource;
import com.hasawi.sears.data.api.model.pojo.User;
import com.hasawi.sears.data.api.response.UserProfileResponse;
import com.hasawi.sears.data.repository.UserAccountRepository;

public class UserProfileViewModel extends ViewModel {

    SavedStateHandle savedStateHandle;
    String userID;
    MutableLiveData<User> user;
    UserAccountRepository userAccountRepository;

    public UserProfileViewModel() {
        userAccountRepository = new UserAccountRepository();
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
}
