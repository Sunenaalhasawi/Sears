package com.hasawi.sears.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.hasawi.sears.data.api.model.User;

public class UserProfileViewModel extends ViewModel {

    SavedStateHandle savedStateHandle;
    String userID;
    MutableLiveData<User> user;

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<User>();
        }
        return user;
    }
}
